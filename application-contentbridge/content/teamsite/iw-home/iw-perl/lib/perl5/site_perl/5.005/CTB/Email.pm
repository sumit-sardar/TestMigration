package CTB::Email;
use TeamSite::Config;
use Net::SMTP;

#------------------------------------------------------------------------------
# Copyright 2003 Interwoven, Inc.   All rights reserved.
#
# Author    Bobby Magee <bobby@interwoven.com>
#
# File      CTB::Email.pm
# Use       Sending Email for the CTB group
#
#------------------------------------------------------------------------------

=head1 NAME

CTB::Email - Sending Email from TeamSite to CTB accounts

%%_VERSION_%%

=head1 SYNOPSIS

Utilities for getting information about and sending emails for CTB.

Note:  TeamSite::Config::iwgethome()  does not end in a trailing slash

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


sub get_address {

	#######################################################################
	#
	# Here is where we get the names of the people getting the email 
	#
	#######################################################################

	my $group;
	my $list;	
	my @address;
	my $mail_cfg = &CTB::Email::get_email_cfg();

	
	
	foreach $group (@_) {
		my ($name, $passwd, $gid, $members) = getgrnam("$group");
		$members =~ s/\s/,/g;
		$list .= "," . $members;
	}
	
	open (CFG, $mail_cfg);
	while (<CFG>) {
		next if /^#/;
		my ($id, $email) = split /:\s/;
		chomp $email;
		push @address, $email if ($list =~ /$id/);
	}
	close CFG;
	
	return &CTB::Email::get_unique(@address);
	
	
}

sub get_unique {
		my @list = @_;
		my %seen = ();
		my @uniqu = grep { ! $seen{$_} ++ } @list;
		return @uniqu;
	}



sub mail_server {
	my $iwov_config = TeamSite::Config::iwgetlocation("iwconfig");

	open (CFG, $iwov_config);
	while (<CFG>) {
		if (/^mailserver=(.*)$/) {
			return $1;
			last;
		}
		next;
	}
}

sub mail_domain {
	my $iwov_config = TeamSite::Config::iwgetlocation("iwconfig");

	open (CFG, $iwov_config);
	while (<CFG>) {
		if (/^maildomain=(.*)$/) {
			return $1;
			last;
		}
		next;
	}
}

sub get_email_cfg {
	my $iwov_config = TeamSite::Config::iwgetlocation("iwconfig");

	open (CFG, $iwov_config);
	while (<CFG>) {
		if (/^email_mapping_file=(.*)$/) {
			return $1;
			last;
		}
		next;
	}
}

sub send_email {
	my ($from, $subject, $body, @addresses) = @_;
	
	my $to = " ";
	
	my $server = &CTB::Email::mail_server();
	
	my $domain = &CTB::Email::mail_domain();
	
	my $smtp = Net::SMTP->new($server,
			       Hello => $domain,
			       Timeout => 30,
			       Debug   => 0,
			      );

        my $address = join(',', @addresses);

    	$smtp->mail($from);
    	$smtp->to($to);
	$smtp->recipient(@addresses,{ SkipBad => 1 });
	$smtp->data();
	$smtp->datasend("Subject: " . $subject . "\n");
        $smtp->datasend("To: $to \n");
        $smtp->datasend("Cc: $address \n");
	$smtp->datasend($body);

	$smtp->dataend();    
	$smtp->quit; 
}
1;
