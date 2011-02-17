package CTB::OD;
use TeamSite::Config;

#------------------------------------------------------------------------------
# Copyright 2003 Interwoven, Inc.   All rights reserved.
#
# Author    Bobby Magee <bobby@interwoven.com>
#
# File      CTB::OD.pm
# Use       Module for calling and checking OD for the CTB group
#
#------------------------------------------------------------------------------

=head1 NAME

CTB::OD - Managing OpenDeploy for CTB

%%_VERSION_%%

=head1 SYNOPSIS

Utilities for getting information about and calling OD for CTB.

=cut

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 get_address(@groups)

Returns an array of email address gotten from a combination of members of a UNIX 
group and the Interwoven Email Config file. 


Example:

    use CTB::Email;
    my @addresses = get_address("pm", "oasgrp");
    my $iwmount = TeamSite::Config::iwgetmount();

=cut

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------


sub execute {
        my $date = localtime(); 
	chomp(my $cmd = $_[0]);
	# print "$date OD COMMAND: $cmd\n";
	open(PIPE, "$cmd 2>&1 |");
    	my(@text) = <PIPE>;
    	close(PIPE);
    	# print join('', @text);
    	return($?, join('', @text));
}

sub get_node_list {
	print "TEST";
}

sub od_reset {
	print "TEST";
}

sub create_prd_cmd {


	my ($odhome, $config, $jobid, $area, $sim, $file_list) = @_;

	my @odcmd;
	my $stg_area = $area;
	$stg_area =~ s/\/main\//\/appqa\//mg;


	if ($sim eq "sim") {
		
		$odcmd[0] = 	join(" ",
				"$odhome/bin/iwodstart",
				"$config",
				"-inst $$-$jobid", 
				"-k area=\"$area\"",
				"-k file_list=\"$file_list\"",
				"-sim");
				
	}
	else {
		$odcmd[0] = 	join(" ",
				"$odhome/bin/iwodstart",
				"$config",
				"-inst $$-$jobid", 
				"-k area=\"$area\"",
				"-k file_list=\"$file_list\"");
				
	}

	return @odcmd;
}

sub create_stg_cmd {


	my ($odhome, $config, $jobid, $area, $sim, $file_list) = @_;

	my $odcmd;

	if ($sim eq "sim") {
		$odcmd = join(" ",
			"$odhome/bin/iwodstart",
			"$config",
			"-inst $$-$jobid", 
			"-k area=\"$area\"",
			"-k file_list=\"$file_list\"",
			"-sim");
	}
	else {
		$odcmd = join(" ",
			"$odhome/bin/iwodstart",
			"$config",
			"-inst $$-$jobid", 
			"-k area=\"$area\"",
			"-k file_list=\"$file_list\"");
	}

	return $odcmd;
}

1;
