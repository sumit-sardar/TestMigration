
$filename = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/export_to_file1.out';
$par_file = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/SrcFiles/HWDI/parameter/write_eiss_leveldata.par';
$log_file = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/write_eiss_leveldata.log';
# $server = 'mcsqiso50';
$domain = 'Domain_mcsoas504';
$integration_service = 'oasdevr5_Integration_Service';
$user_name = 'ayan_bandyopadhyay';
$passwd = 'ayan321';
$target_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/EISS';
$archive_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/Archive/HWDI/Tgt_EISS_Archive';
$par_dir = '/appl/informatica/PowerCenter8.1.1/server/infa_shared/SrcFiles/HWDI/parameter';


# Erase previous log
open (LOGIFLE,">$log_file")
     or die "Cannot open $log_file for writing!\n";
close LOGFILE;


open (OPUNITFILE,"<$filename") or die "Cannot open $filename for reading ! \n";

while(<OPUNITFILE>) {
	my $line = $_;
        $_ =~ s/\s*//g; #  	remove whitespace
	chop($line);
	@lineparts = split /,/, $line;
	$district = lc($lineparts[0]);
	$opunit = lc($lineparts[1]);

        # To erase the previous contents, if it exists, OPEN and CLOSE
        open (PARFILE,">$par_file")
                or die "Cannot open $par_file for writing!\n";
        close PARFILE;

        open (PARFILE, ">$par_file") or die "Cannot open $par_file for writing ! \n";
        print PARFILE "[OAS_Export.s_m_O_WSX_LOAD_ISTEP]\n";
        print PARFILE "\$\$DISTRICT_ID=$district\n";
        print PARFILE "\$\$OPUNIT=$opunit\n";
        close(PARFILE);

        system("echo \" \n\n *******Running load for district $district opunit $opunit ******** \n \" >> $log_file ");
#        system("pmcmd startworkflow -s $server:4001 -u $user_name -p $passwd -wait wf_eiss_level_dataload >> $log_file");
#        system ("pmcmd  getworkflowdetails -s $server:4001 -u $user_name -p $passwd wf_eiss_level_dataload >>  $log_file");
        system("/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd startworkflow -sv $integration_service -d $domain -u $user_name -p $passwd -wait wf_eiss_level_dataload >> $log_file");
        system ("/appl/informatica/PowerCenter8.1.1/server/bin/pmcmd  getworkflowdetails -sv $integration_service -d $domain -u $user_name -p $passwd wf_eiss_level_dataload >>  $log_file");
         

        system("sh /appl/informatica/PowerCenter8.1.1/server/infa_shared/Scripts/HWDI/file_split1.sh $target_dir $archive_dir $par_dir $opunit");

}

