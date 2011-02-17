package CTB::DEPLOY;

#------------------------------------------------------------------------------
#
# Author    Matthew Short <mwshort@thoughtworks.com>
#
# File      CTB::DEPLOY.pm
# Use       Executing deploy to OAS logic
#
#------------------------------------------------------------------------------

use File::Copy;
use Net::SMTP;
use File::Basename;
use Time::Local;
use CTB::OD;
use CTB::RegEx;
use CTB::XML;
use CTB::Email;
use CTB::CIM;

sub deployTwo {
	my ( $classpath, $ccs_home, $environment,$area, $workingdir, $logfile,$email_to,$error_flag, $mode, $ccsCommand, @files) = @_;
	
	#create file in working dir, make sure etc folder is copied if necessary
	umask(022);
	copyResources($workingdir,$ccs_home);	

	print "Using CCS installation: $ccs_home \n";
    	open (LOG, ">$logfile");	

	chmod (0777,$logfile) || die "$date: could not chmod $logfile";
    	print LOG "\n======= $date: START OF LOG FOR DEPLOY For Job $workingdir =======\n";
	close LOG;
        $file_basename = basename($files[0]);
	copy("$area/$files[0]", "$workingdir/data/$file_basename");

	my $cmd = 'LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/usr/local/lib"; export LD_LIBRARY_PATH; ' .
	    "java -cp $classpath com.ctb.commands.ContentImportModule $ccsCommand itemfile=data/$file_basename env=conf/$environment mediapath=$workingdir imagearea=$area validationmode=AllowDefaults >> $logfile 2>&1";
	#call import
	if ($environment ne "PRODUCTION") {
            if (system("$cmd") != 0) {
		print "Content import failed!\n";
		$error_flag = 1;
	    }
	} else {
	    `echo Creating deployToProduction for $workingdir >> $logfile`;
	    if (!open(DTP, "> $workingdir/deployToProduction")) {
		print "Couldn't open $workingdir/deployToProduction\n";
		$error_flag = 1;
	    }
	    print DTP "#!/bin/sh\n";
	    print DTP $cmd;
	    print DTP "\n";
	    close DTP;
	    if (!chmod (0775, "$workingdir/deployToProduction")) {
		print "Couldn't chmod $workingdir/deployToProduction\n";
		$error_flag = 1;
	    }
	}

	`chown -R iwuser:oasgrp $workingdir`;
	if ($? != 0) {
	    print "Couldn't chown $workingdir\n";
	    $error_flag = 1;
	}
	return ($error_flag, $mode, $logfile, $email_to);
}


sub copyResources {
	
	my ($job_dir,$ccs_home) = @_;

	`rm -r $job_dir` if (-e $job_dir);

	mkdir ($job_dir, 0777) || die "$date: could not create $job_dir";

	chmod (0775,$job_dir) || die "$date: could not chmod $job_dir";

	mkdir ("$job_dir/data", 0777) || die "$date: could not create $job_dir/data";
	
	mkdir ("$job_dir/etc", 0777) || die "$date: could not create $job_dir/etc";
	mkdir ("$job_dir/conf", 0777) || die "$date: could not create $job_dir/conf";

	chmod (0775,"$job_dir/data") || die "$date: could not chmod $job_dir/data";
	chmod (0775,"$job_dir/etc") || die "$date: could not chmod $job_dir/etc";
	chmod (0775,"$job_dir/conf") || die "$date: could not chmod $job_dir/conf";
	my $cmd="cp $ccs_home/ccs/etc/* $job_dir/etc/";
	`$cmd`;
	$cmd="cp $ccs_home/ccs/conf/* $job_dir/conf/";
	`$cmd`;
	$cmd="cp $ccs_home/ccs/etc/*.dtd $job_dir/";
	`$cmd`;
	$cmd="cp -R $ccs_home/ccs/generated $job_dir";
	`$cmd`;

	chdir ("$job_dir") || die "$date: could not change directory to $job_dir/data";
}

sub get_ccs_home {
	my $userdir = $_[0];
	my $ccs_home = "/export/home/" . $userdir . "/interfaces/build/dist/new";
	return $ccs_home;
}


sub get_classpath {
	my $ccs_home = $_[0];
	$path = $ccs_home . "/ccs/lib/ccs.jar"; 
	return $path;
}

sub buildDevArguments {
	
	my ($environment,$iw_user, $jobid, $taskid, $area, @filenames) = @_;
	
	my $task = new TeamSite::WFtask($taskid);
        my $job_dir = "/appl/oas1/".$jobid;
	my $error_flag = 0;
	my $mode = 0755;
	my $logfile = "$job_dir/ctb_deploy.log";
	my $email_to;
	my $ccs_home = &CTB::DEPLOY::get_ccs_home($iw_user); 
	my $classpath = &CTB::DEPLOY::get_classpath($ccs_home);
	return ($jobid,$task,$classpath,$ccs_home,$environment, $area, $job_dir, $logfile,$email_to,$error_flag, $mode, @filenames);
}

sub buildProductionArguments {
	my ($environment,$jobid, $taskid, $area, @filenames) = @_;
	my $task = new TeamSite::WFtask($taskid);
        my $job_dir = "/appl/publishing/production/outbound/".$jobid;
	my $error_flag = 0;
	my $mode = 0755;
	my $logfile = "$job_dir/ctb_deploy.log";
	my $email_to;
	my $ccs_home = "/export/cim";
	my $classpath = &CTB::DEPLOY::get_classpath($ccs_home);
	return ($jobid,$task,$classpath,$ccs_home,$environment, $area, $job_dir, $logfile,$email_to,$error_flag, $mode, @filenames);
}

sub buildCQAArguments {

	my ($environment,$jobid, $taskid, $area, @filenames) = @_;
	my $task = new TeamSite::WFtask($taskid);
        my $job_dir = "/appl/oas1/".$jobid;
	my $error_flag = 0;
	my $mode = 0755;
	my $logfile = "$job_dir/ctb_deploy.log";
	my $email_to;
	my $ccs_home = "/export/cim";
	my $classpath = &CTB::DEPLOY::get_classpath($ccs_home);
	return ($jobid,$task,$classpath,$ccs_home,$environment, $area, $job_dir, $logfile,$email_to,$error_flag, $mode, @filenames);
}

1;
