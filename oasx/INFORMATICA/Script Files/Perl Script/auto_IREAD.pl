#!/usr/bin/perl
$domain = 'Domain_mcsoas504';
$integration_service = 'oasdevr5_Integration_Service';
$user_name = 'ayan_bandyopadhyay';
$passwd = 'ayan321';
$wsu_target_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/EISS';
$archive_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/Archive/HWDI/Tgt_EISS_Archive';
$par_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/SrcFiles/HWDI/parameter';
$wsu_tmp_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/EISS/tmp';
$log_file = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/auto_ISTEP.log';
$xml_list = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/Monarch/XML/monarchimport_onlinesample.xml.lst';
$xml_tmp_dir  = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/Monarch/XML/tmp';
$xml_target_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/Monarch/XML/';
$zip_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/zip';
$par_file='/appl/informatica/PowerCenter8.1.1/server/infa_shared/SrcFiles/HWDI/parameter/oas_eiss_test_roster.par';
$data_file='/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/DISTRICT_DTL.txt';


# Erase previous log
open (LOGIFLE,">$log_file")
     or die "Cannot open $log_file for writing!\n";
close LOGFILE;

# Remove old tmp dir and create a new one
system("echo \" \n\n ******* Removing temp dirs ******** \n \" >> $log_file ");
system("rm -Rf $wsu_tmp_dir >> $log_file ");
system("rm -Rf $xml_tmp_dir >> $log_file ");
system("echo \" \n\n ******* Creating temp dir ******** \n \" >> $log_file ");
system("mkdir $wsu_tmp_dir >> $log_file ");
system("mkdir $xml_tmp_dir >> $log_file ");

# Remove processed zip files
system("echo \" \n\n ******* Removing processed zip file ******** \n \" >> $log_file ");
system("rm $zip_dir/*done >> $log_file ");

#To generate a dynamic parameter file customer_id and product_id is taking as input


print "Enter The Customer_ID: ";
my $Customer_ID= 7496;
#chomp $Customer_ID;
print $Customer_ID;
print "Enter The Product_ID: ";
my $Product_ID= 6109;
print $Product_ID;
#chomp $Product_ID;

# Calling workflows district wise.Taking the district_id from DISTRICT_DTL.txt

open(handle,$data_file) || die("Could not open file!");
@raw_data=<handle>;
$n=$#raw_data + 1;
close(handle);

for ( $count = 0 ;$count < $n ;$count++)
{
 $district_id= @raw_data[$count];

print $district_id ;


open (PARFILE,">$par_file")
                or die "Cannot open $par_file for writing!\n";
        close PARFILE;
open (PARFILE, ">$par_file") or die "Cannot open $par_file    for writing ! \n";

 print PARFILE  "[OAS_Export.s_m_O_TEST_ROSTER_ID_Extract_ISTEP]\n";
        print PARFILE "\$\$CUSTOMER_ID=$Customer_ID\n";
        print PARFILE "\$\$PRODUCT_ID=$Product_ID\n";
        print PARFILE "\$\$LITHO_START_NUMBER=1\n";
        print PARFILE "\$\$CALIBRATION='N'\n";
        print PARFILE "$district_id\n";
close(PARFILE);

system("echo \" \n\n ******* Calling Workflows District wise******** \n \" >> $log_file ");


system("/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd startworkflow -sv $integration_service -d $domain -u $user_name -p $passwd -wait wf_eiss_IREAD_RETEST >> $log_file");
system ("/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd  getworkflowdetails -sv $integration_service -d $domain -u $user_name -p $passwd wf_eiss_IREAD_RETEST >>  $log_file");

system("/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd startworkflow -sv $integration_service -d $domain -u $user_name -p $passwd -wait wf_MONARCH_Completeness_Check_IREAD_RETEST >> $log_file");
system ("/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd  getworkflowdetails -sv $integration_service -d $domain -u $user_name -p $passwd wf_MONARCH_Completeness_Check_IREAD_RETEST >>  $log_file");



}

open (XMLFILELIST,"<$xml_list") or die "Cannot open $xml_list for reading ! \n";
while(<XMLFILELIST>) {
	my $line = $_;
        $_ =~ s/\s*//g; #  	remove whitespace
	chop($line);
#	@lineparts = split /,/, $line;
	$xmlfile = $line;


        system("echo \" \n\n ******* Copying xml file $xmlfile to temp dir ******** \n \" >> $log_file ");
        system("cp $xml_target_dir$xmlfile $xml_tmp_dir >> $log_file ");

}


#system("echo \" \n\n ******* Zipping files ******** \n \" >> $log_file ");
#system("sh zip.sh >> $log_file ");
