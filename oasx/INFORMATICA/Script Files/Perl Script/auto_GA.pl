#!/usr/bin/perl
$domain = 'Domain_mcsoas504';
$integration_service = 'oasdevr5_Integration_Service';
$user_name = 'ayan_bandyopadhyay';
$passwd = 'ayan321';
$wsu_target_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/EISS';
$archive_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/Archive/HWDI/Tgt_EISS_Archive';
$par_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/SrcFiles/HWDI/parameter';
$wsu_tmp_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/EISS/tmp';
$log_file = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/auto_GA.log';
$xml_list = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/Monarch/XML/monarchimport_onlinesample.xml.lst';
$xml_tmp_dir  = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/Monarch/XML/tmp';
$xml_target_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/Monarch/XML/';
$zip_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/zip';


# Erase previous log
open (LOGIFLE,">$log_file")
     or die "Cannot open $log_file for writing!\n";
close LOGFILE;

#set path
#system("export PATH=$PATH:/appl/informatica/PowerCenter8.1.1:/appl/informatica/PowerCenter8.1.1/server/tomcat/bin:/appl/informatica/PowerCenter8.1.1:/appl/informatica/PowerCenter8.1.1/ODBC5.1:/appl/oracle/product/10.2.0.3/bin:/appl/oracle/product/10.2.0.3/opatch/OPatch:/bin:.:/usr/kerberos/bin:/usr/local/bin:/bin:/usr/bin:/usr/X11R6/bin:/app/informatica/bin");

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


# Remove old xml list file
system("echo \" \n\n ******* Removing old xml list file ******** \n \" >> $log_file ");
system("rm -f $xml_list >> $log_file ");
system("touch $xml_list >> $log_file ");


system("echo \" \n\n ******* Calling Workflows ******** \n \" >> $log_file ");
system("sh  /appl/informatica/PowerCenter8.1.1/server/infa_shared/Scripts/HWDI/wf.sh >> $log_file ");


open (XMLFILELIST,"<$xml_list") or die "Cannot open $xml_list for reading ! \n";
while(<XMLFILELIST>) {
	my $line = $_;
        $_ =~ s/\s*//g; #  	remove whitespace
	chop($line);
	$xmlfile = $line;


        system("echo \" \n\n ******* Copying xml file $xmlfile to temp dir ******** \n \" >> $log_file ");
        system("cp $xml_target_dir$xmlfile $xml_tmp_dir >> $log_file ");

}


system("echo \" \n\n ******* Zipping files ******** \n \" >> $log_file ");
system("sh /appl/informatica/PowerCenter8.1.1/server/infa_shared/Scripts/HWDI/zip.sh >> $log_file ");

system("/usr/lib/sendmail -t -oi < /appl/informatica/PowerCenter8.1.1/server/infa_shared/Scripts/HWDI/mail.txt >> $log_file ");
