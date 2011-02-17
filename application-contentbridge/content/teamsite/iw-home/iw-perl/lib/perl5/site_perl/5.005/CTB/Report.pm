package CTB::Report;

#------------------------------------------------------------------------------
# Copyright 2003 Interwoven, Inc.   All rights reserved.
#
# Author    Bobby Magee <bobby@interwoven.com>
#
# File      CTB::Report.pm
# Use       Subroutines used to generate reports on TeamSite
#
#------------------------------------------------------------------------------
use XML::Parser;
use File::Find;
use TeamSite::Config;
use DBI;
use User::pwent;
use Time::JulianDay; 


use CTB::RegEx;


#################################################################
#  
# Set some variables to be used to connect to the database
#
#################################################################


$ENV{'ORACLE32_LIB'}='/oracle/oracle32/lib';
$ENV{'ORACLE_HOME'}='/oracle/product/9.2.0.1';
$ENV{'ORACLE_BASE'}='/oracle';
$ENV{'ORACLE_LIB'}=$ENV{'ORACLE32_LIB'};
$ENV{'LD_LIBRARY_PATH'}="$ENV{'ORACLE32_LIB'}:/usr/openwin/lib:/usr/openwin/lib/x11:/usr/dt/lib:/opt/lib:/opt/libx11:/oracle/product/9.2.0.1/lib";
$ENV{'ORACLE_SID'}='oasdev.mcgraw-hill.com';
$ENV{'TWO_TASK'}='oasdev.mcgraw-hill.com';


my %properties = ('user' => 'oasrep', 
		  'password' => 'callatt800', 
		  'host.name' => '198.45.17.19', 
		  'host.port' => '1521');
	
	my $dsn = "dbi:JDBC:hostname=127.0.0.1;port=1521;url=jdbc:oracle:thin:\@198.45.17.19:1521:OASDEV";
	my $dbh = DBI->connect($dsn, "oasrep", "callatt800", 
	          { PrintError => 1, RaiseError => 1, jdbc_properties => \%properties })
          or die "Failed to connect: ($DBI::err) $DBI::errstr\n";
	

=head1 NAME

CTB::Reporting - CTB Reporting

%%_VERSION_%%

=head1 SYNOPSIS

Utilities for generating Reports and updating the IDB


=cut

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 get_cab_comp_items()

Takes no arguement 
(e.g. "/default/main/CAB/MATH/WORKAREA/load")

Returns an array of componetized items in the CAB branch
TODO: Need to dynamically create the list of definition files
Example:

    use CTB::Report;
    
    my @items = CTB::Report::get_cab_comp_items();

=cut
#------------------------------------------------------------------------------

sub get_cab_comp_items {
	my $mount = TeamSite::Config::iwgetmount();
	my %seen = ();

	@branches = &get_workareas("/default/main/CAB");
	
	foreach (@branches){

	  my $branch = $_;
	  $branch =~ m/CAB\/(.*)\/WORKAREA/;

	  if($1 eq "MATH") {
		  @SRdefs = ("TeamXML/Rules/G2MathItemSR.dfn",
		  "TeamXML/Rules/G3MathItemSR.dfn",
		  "TeamXML/Rules/G4MathItemSR.dfn",
		  "TeamXML/Rules/G5MathItemSR.dfn",
		  "TeamXML/Rules/G6MathItemSR.dfn",
		  "TeamXML/Rules/G7MathItemSR.dfn",
		  "TeamXML/Rules/G8MathItemSR.dfn",
		  "TeamXML/Rules/G10MathItemSR.dfn",
		  "TeamXML/Rules/G3MathItem.dfn",
		  "TeamXML/Rules/G4MathItem.dfn",
		  "TeamXML/Rules/G5MathItem.dfn",
		  "TeamXML/Rules/G6MathItem.dfn",
		  "TeamXML/Rules/G7MathItem.dfn",
		  "TeamXML/Rules/G8MathItem.dfn",
		  "TeamXML/Rules/G10MathItem.dfn");

		  @CRdefs = ("TeamXML/Rules/G2MathItemCR.dfn",
		  "TeamXML/Rules/G3MathItemCR.dfn",
		  "TeamXML/Rules/G4MathItemCR.dfn",
		  "TeamXML/Rules/G5MathItemCR.dfn",
		  "TeamXML/Rules/G6MathItemCR.dfn",
		  "TeamXML/Rules/G7MathItemCR.dfn",
		  "TeamXML/Rules/G8MathItemCR.dfn",
		  "TeamXML/Rules/G10MathItemCR.dfn");

	  } elsif( $1 eq "READING") {
		  @SRdefs = ("TeamXML/Rules/G2ReadingItemSR.dfn",
		  "TeamXML/Rules/G3ReadingItemSR.dfn",
		  "TeamXML/Rules/G4ReadingItemSR.dfn",
		  "TeamXML/Rules/G5ReadingItemSR.dfn",
		  "TeamXML/Rules/G6ReadingItemSR.dfn",
		  "TeamXML/Rules/G7ReadingItemSR.dfn",
		  "TeamXML/Rules/G8ReadingItemSR.dfn",
		  "TeamXML/Rules/G10ReadingItem.dfn",
		  "TeamXML/Rules/G3ReadingItem.dfn",
		  "TeamXML/Rules/G4ReadingItem.dfn",
		  "TeamXML/Rules/G5ReadingItem.dfn",
		  "TeamXML/Rules/G6ReadingItem.dfn",
		  "TeamXML/Rules/G7ReadingItem.dfn",
		  "TeamXML/Rules/G8ReadingItem.dfn",
		  "TeamXML/Rules/G10ReadingItem.dfn");

		  @CRdefs = ("TeamXML/Rules/G2ReadingItemCR.dfn",
		  "TeamXML/Rules/G3ReadingItemCR.dfn",
		  "TeamXML/Rules/G4ReadingItemCR.dfn",
		  "TeamXML/Rules/G5ReadingItemCR.dfn",
		  "TeamXML/Rules/G6ReadingItemCR.dfn",
		  "TeamXML/Rules/G7ReadingItemCR.dfn",
		  "TeamXML/Rules/G8ReadingItemCR.dfn",
		  "TeamXML/Rules/G10ReadingItemCR.dfn");


	  } elsif( $1 eq "LANGUAGE") {
		  @SRdefs = ("TeamXML/Rules/G2LanguageItemSR.dfn",
		  "TeamXML/Rules/G3LanguageItemSR.dfn",
		  "TeamXML/Rules/G4LanguageItemSR.dfn",
		  "TeamXML/Rules/G5LanguageItemSR.dfn",
		  "TeamXML/Rules/G6LanguageItemSR.dfn",
		  "TeamXML/Rules/G7LanguageItemSR.dfn",
		  "TeamXML/Rules/G8LanguageItemSR.dfn",
		  "TeamXML/Rules/G10LanguageItemSR.dfn");

		  @CRdefs = ("TeamXML/Rules/G2LanguageItemCR.dfn",
		  "TeamXML/Rules/G3LanguageItemCR.dfn",
		  "TeamXML/Rules/G4LanguageItemCR.dfn",
		  "TeamXML/Rules/G5LanguageItemCR.dfn",
		  "TeamXML/Rules/G6LanguageItemCR.dfn",
		  "TeamXML/Rules/G7LanguageItemCR.dfn",
		  "TeamXML/Rules/G8LanguageItemCR.dfn",
		  "TeamXML/Rules/G10LanguageItemCR.dfn");


	  } elsif( $1 eq "SCIENCE") {
		  @SRdefs = ("TeamXML/Rules/G3ScienceItemSR.dfn",
		  "TeamXML/Rules/G4ScienceItemSR.dfn",
		  "TeamXML/Rules/G5ScienceItemSR.dfn",
		  "TeamXML/Rules/G6ScienceItemSR.dfn",
		  "TeamXML/Rules/G7ScienceItemSR.dfn",
		  "TeamXML/Rules/G8ScienceItemSR.dfn",
		  "TeamXML/Rules/G10ScienceItemSR.dfn");

		  @CRdefs = ("TeamXML/Rules/G3ScienceItemCR.dfn",
		  "TeamXML/Rules/G4ScienceItemCR.dfn",
		  "TeamXML/Rules/G5ScienceItemCR.dfn",
		  "TeamXML/Rules/G6ScienceItemCR.dfn",
		  "TeamXML/Rules/G7ScienceItemCR.dfn",
		  "TeamXML/Rules/G8ScienceItemCR.dfn",
		  "TeamXML/Rules/G10ScienceItemCR.dfn");


	  } elsif( $1 eq "SocialSciences/CIVICS") {
		  @SRdefs = ("TeamXML/Rules/G3CivicsItemSR.dfn",
		  "TeamXML/Rules/G4CivicsItemSR.dfn",
		  "TeamXML/Rules/G5CivicsItemSR.dfn",
		  "TeamXML/Rules/G6CivicsItemSR.dfn",
		  "TeamXML/Rules/G7CivicsItemSR.dfn",
		  "TeamXML/Rules/G8CivicsItemSR.dfn",
		  "TeamXML/Rules/G10CivicsItemSR.dfn");

		  @CRdefs = ("TeamXML/Rules/G3CivicsItemCR.dfn",
		  "TeamXML/Rules/G4CivicsItemCR.dfn",
		  "TeamXML/Rules/G5CivicsItemCR.dfn",
		  "TeamXML/Rules/G6CivicsItemCR.dfn",
		  "TeamXML/Rules/G7CivicsItemCR.dfn",
		  "TeamXML/Rules/G8CivicsItemCR.dfn",
		  "TeamXML/Rules/G10CivicsItemCR.dfn");


	  } elsif( $1 eq "SocialSciences/ECONOMICS") {
		  @SRdefs = ("TeamXML/Rules/G3EconomicsItemSR.dfn",
		  "TeamXML/Rules/G4EconomicsItemSR.dfn",
		  "TeamXML/Rules/G5EconomicsItemSR.dfn",
		  "TeamXML/Rules/G6EconomicsItemSR.dfn",
		  "TeamXML/Rules/G7EconomicsItemSR.dfn",
		  "TeamXML/Rules/G8EconomicsItemSR.dfn");

		  @CRdefs = ("TeamXML/Rules/G3EconomicsItemCR.dfn",
		  "TeamXML/Rules/G4EconomicsItemCR.dfn",
		  "TeamXML/Rules/G5EconomicsItemCR.dfn",
		  "TeamXML/Rules/G6EconomicsItemCR.dfn",
		  "TeamXML/Rules/G7EconomicsItemCR.dfn",
		  "TeamXML/Rules/G8EconomicsItemCR.dfn");

	  } elsif( $1 eq "SocialSciences/GEOGRAPHY") {
		  @SRdefs = ("TeamXML/Rules/G3GeographyItemSR.dfn",
		  "TeamXML/Rules/G4GeographyItemSR.dfn",
		  "TeamXML/Rules/G5GeographyItemSR.dfn",
		  "TeamXML/Rules/G6GeographyItemSR.dfn",
		  "TeamXML/Rules/G7GeographyItemSR.dfn",
		  "TeamXML/Rules/G8GeographyItemSR.dfn");

		  @CRdefs = ("TeamXML/Rules/G3GeographyItemCR.dfn",
		  "TeamXML/Rules/G4GeographyItemCR.dfn",
		  "TeamXML/Rules/G5GeographyItemCR.dfn",
		  "TeamXML/Rules/G6GeographyItemCR.dfn",
		  "TeamXML/Rules/G7GeographyItemCR.dfn",
		  "TeamXML/Rules/G8GeographyItemCR.dfn");

	  } elsif( $1 eq "SocialSciences/HISTORY") {
		  @SRdefs = ("TeamXML/Rules/G3HistoryItemSR.dfn",
		  "TeamXML/Rules/G4HistoryItemSR.dfn",
		  "TeamXML/Rules/G5HistoryItemSR.dfn",
		  "TeamXML/Rules/G6HistoryItemSR.dfn",
		  "TeamXML/Rules/G7HistoryItemSR.dfn",
		  "TeamXML/Rules/G8HistoryItemSR.dfn");

		  @CRdefs = ("TeamXML/Rules/G3HistoryItemCR.dfn",
		  "TeamXML/Rules/G4HistoryItemCR.dfn",
		  "TeamXML/Rules/G5HistoryItemCR.dfn",
		  "TeamXML/Rules/G6HistoryItemCR.dfn",
		  "TeamXML/Rules/G7HistoryItemCR.dfn",
		  "TeamXML/Rules/G8HistoryItemCR.dfn");  	  


	  } 
	  else {}


	  foreach (@SRdefs) {

		@xml_paths = `/appl/TeamSite/iw-home/bin/iwxmlinfo -f script cm $branch/$_`;

			foreach (@xml_paths) {
				my $path = $_;
				$path =~ m/Content([^\s]+)\s/;
				my $out_path = $1;
				$out_path =~ s|\\|/|mg;
				my $filepath = "Content$out_path";

				my @full_file_path = `/appl/TeamSite/iw-home/bin/iwxmlinfo -f script fi $branch/$filepath`;

				foreach (@full_file_path) {
					my $item_id;
					$_ =~ m/"([^"]+)"/mg; #"
					$item_id = "$1";
					next if $seen{$item_id};
					$seen{$item_id} = 1;
					push(@SRIDS,$item_id);
				}
			}
		  }

	  foreach (@CRdefs) {


		@xml_paths = `/appl/TeamSite/iw-home/bin/iwxmlinfo -f script cm $branch/$_`;

			foreach (@xml_paths) {
				my $path = $_;
				$path =~ m/Content([^\s]+)\s/;
				my $out_path = $1;
				$out_path =~ s|\\|/|mg;
				my $filepath = "Content$out_path";

				my @full_file_path = `/appl/TeamSite/iw-home/bin/iwxmlinfo -f script fi $branch/$filepath`;

				foreach (@full_file_path) {
					my $item_id;
					$_ =~ m/"([^"]+)"/mg; #"
					$item_id = "$1";
					next if $seen{$item_id};
					$seen{$item_id} = 1;
					push(@CRIDS,$item_id);
				}
			}
		  }


	}

	my @SRResults = &get_unique(@SRIDS);
	my @CRResults = &get_unique(@CRIDS);
        return @SRResults, @CRResults;
        
	sub get_unique {

		my %seen = ();
		my @uniqu = grep { ! $seen{$_} ++ } @_;
		return @uniqu;

	}	

}

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 get_cab_doc_items()

Takes an argument of a bank 

Returns an array of items in job documents in the CAB branch

Example:

    use CTB::Report;
    
    my %items = CTB::Report::get_cab_doc_items();

=cut
#------------------------------------------------------------------------------

sub get_cab_doc_items {
	$_[0] = "/default/main/CAB" unless defined $_[0];
	
	my %doc_items;
	
	find(\&process_doc, @_);
	
	sub process_doc {
		return unless -f && /xml$/;
		
		my $file = $File::Find::name;
		return unless ($file =~ /WORKAREA/ && $file =~ /\/Job/);
		# print "FILE: $file\n";
		$file =~ /^(.*\/WORKAREA\/load)\/(.*)$/;
		#print "File: $2, AREA: $1\n"; 
		my %items = &CTB::RegEx::get_items($2, $1);
		foreach $keys (keys %items) {
			#print "KEY: $keys\n";
			next if $seen{$keys};
			$seen{$keys} = 1;
			$doc_items{$keys} = $file;
		}		
	}
	
	foreach $key (keys %doc_items) {
		print "$key => " . $doc_items{key} . "\n";;
	}
	return %doc_items;
}


#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 get_workareas()

Takes a branch as an argument.  If there is no argument, it is assumed that 

Returns an array of items in job documents in the CAB branch

Example:

    use CTB::Report;
    
    my @workareas = CTB::Report::get_workareas();

=cut
#------------------------------------------------------------------------------

sub get_workareas {
	$_[0] = "/default/main" unless defined $_[0];
	my %seen = ();
	my @was;
	
	find(\&process_dirs, @_);
	
	sub process_dirs {
		my $dir = $File::Find::dir;
		# print "$dir\n";;
		if ($dir =~ /(.*WORKAREA\/[^\/]+)$/) {
			return if $seen{$1};
			push @was, $1;
			$seen{$1} = 1;
		}
	}
	return @was;
	
}

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 get_teamxml_defs()

This routine will collect the list of TeamXML definitions.

Returns an array of items in TeamXML defs in the a branch

=cut
#------------------------------------------------------------------------------

sub get_teamxml_defs {
	print "THIS NEEDS TO BE implemented\n";	
}

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 test_jdbc()

This routine will test the connection between the TeamSite server and .

Returns an array of items in TeamXML defs in the a branch

=cut
#------------------------------------------------------------------------------

sub test_jdbc {

	my $sth = $dbh->prepare("SELECT count(1) FROM ts_state");
	
	
	
	
	$sth->execute;
	
	while (@row = $sth->fetchrow_array) {
	    #  print $_ . "\n";
	    print join(", ", map {defined $_ ? $_ : "(null)"} @row), "\n";
	}
	$sth->finish;
	
	$dbh->disconnect;
	return 0;
}

sub insert_into_ts_job {
	my ($id, $desc, $taskname, $taskowner, $createdate) = @_;
	my $jd = "TO_DATE('" . gm_julian_day($createdate) . "', 'J')";
	$taskowner = $taskowner eq "<no user>" ? "NULL" : "'" . $taskowner . "'";
	my $sth = $dbh->prepare("INSERT INTO ts_job VALUES ($id, '$desc', '$taskname', $taskowner, $jd, NULL, NULL, NULL)");
	$sth->execute;
	$dbh->commit;
	return 0;
	
	#print "INSERT INTO ts_job VALUES ($id, '$desc', '$taskname', $taskowner, $jd, NULL, NULL, NULL);\n";	
}

sub insert_into_ts_doc {
	my ($file, $gi, $file_size, $num_items, $grade, $file_owner, $area, $wfid, $date_modified, $date_created) = @_;
	my $date_modified = "TO_DATE('" . gm_julian_day($date_modified) . "', 'J')";
	my $date_created = "TO_DATE('" . gm_julian_day($date_created) . "', 'J')";
	my $sth = $dbh->prepare("INSERT INTO ts_document VALUES ('$file', '$gi', 100, $num_items, '$grade', '$file_owner', '$area', $wfid, $date_modified, $date_created)");
	$sth->execute;
	$dbh->commit;
	return 0;
	# print "INSERT INTO ts_document VALUES ('$file', '$gi', 100, $num_items, '$grade', '$file_owner', '$area', $wfid, $date_modified, $date_created);\n";	
}

sub insert_into_ts_item {
	my ($id, $itemtype, $doc, $grade, $tmpid, $cont_area, $obj, $standard, $strand, $benchmark, $area, $bank) = @_;
	my $sth = $dbh->prepare("INSERT INTO ts_item VALUES ('$id', '$itemtype', '$doc', '$grade', '$tmpid', '$cont_area', '$obj', '$standard', '$strand', '$benchmark', '$area', '$bank')");
	$sth->execute;
	$dbh->commit;
	return 0;
	# print "INSERT INTO ts_item VALUES ('$id', '$itemtype', '$doc', '$grade', '$tmpid', '$cont_area', '$obj', '$standard', '$strand', '$benchmark', '$area', '$bank');\n";	
}

sub insert_into_ts_comp_item {
	my ($id) = $_[0];
	my $sth = $dbh->prepare("INSERT INTO ts_comp_item VALUES ('$id')");
	$sth->execute;
	$dbh->commit;
	# print "INSERT INTO ts_comp_item VALUES ('$id')\n";	
	return 0;
}

sub insert_into_ts_doc_item {
	my ($id, $doc) = @_;
	$doc =~ s/'//mg;
	my $sth = $dbh->prepare("INSERT INTO ts_doc_item VALUES ('$id', '$doc')");
	$sth->execute;
	$dbh->commit;
	return 0;
}


1;