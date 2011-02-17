package CTB::MAP;

use XML::Parser;
#------------------------------------------------------------------------------
# Copyright 2003 Interwoven, Inc.   All rights reserved.
#
# Author    Jason Hasselkus <jason@interwoven.com>
#
# File      CTB::MAP.pm
# Use       To Create Mapped objects from one Branch to another
#
#------------------------------------------------------------------------------


#------------------------------------------------------------------------------
# Get the Definitions from TeamXML and return an array of definition paths
# relative to the workarea.
#------------------------------------------------------------------------------

sub getDefinitions {

  my $search_area = $_[0];
  my $search_ca = $_[1];
  my $search_key=$_[2];
  my $map_config_file=$_[3];
  my $search_string = "$search_area-$search_ca";
  my %def_hash;
  my @defs;
  my @clean_defs;

  open(CFG,"$map_config_file") or die "Can't open CFG: $!\n";

  my $flag = 0;
  while(<CFG>) {
	if($_ =~ /^\[(.*)\]$/ && $1 eq $search_string) {
		$flag = 1;
		next;
	}
	elsif($_ =~ /^\[(.*)\]$/ && $1 ne $search_string) {
		$flag =0;
	}
	elsif($flag == 1) {
		my ($grade,$defs) = split("=",$_);
		$def_hash{$grade} = "$defs";
	}
	 
  }
  close CFG;

  while (my($key, $value) =
     each (%def_hash)) {
   
     if($key eq $search_key) { 
     
     	@defs = split(",",$value);
     }
     else{}
  }
  foreach my $item (@defs) {
  	$item =~ s|"||mg; #"
  	push(@clean_defs,$item);
  }
  return @clean_defs;
}

#------------------------------------------------------------------------------
# Check the incoming file (should be a mapping file) to make sure the structure
# is correct. Should be in the form of |"CAB ID","Map to Objective"
#------------------------------------------------------------------------------
sub checkMapFile {

my $file = $_[0];
open(MAP, "$file") or die "Can't open MAP: $!\n";

while (<MAP>) {

	my ($cab_id, $obj_id) = split(",",$_);
	
	if(defined $cab_id && defined $obj_id) {
		next;
	} elsif(!defined $cab_id && !defined $obj_id && !eof) { 
		return 1;
	}
}
return 0;

close MAP;
}

#------------------------------------------------------------------------------
# Check the configuration file to find out what the state code (non-numeric) is.
# This is used to generate an id for the item that is unique.
#------------------------------------------------------------------------------
sub getStateCode {

	my $state=$_[0];
	my $code;
	
	if($state eq 'TEXAS') {
		$code = "TX";
	}
	elsif($state eq 'WV') {
		$code= 'WV';
	}
	elsif($state eq 'COLORADO') {
		$code = 'CO';
	}
	
	return $code;
}

#------------------------------------------------------------------------------
# Check the configuration file to find out what the state code (non-numeric) is.
# This is used to generate an id for the item that is unique.
#------------------------------------------------------------------------------
sub getVPath {

  my $vpath_cfg = $_[0];
  my $search_area = $_[1];
  my $search_ca = $_[2];
  my $search_string = "$search_area-$search_ca";
  my $clean_path;

  open(CFG,"$vpath_cfg") or die "Can't open CFG: $!\n";

  my $flag = 0;
  while(<CFG>) {
	if($_ =~ /^\[(.*)\]$/ && $1 eq $search_string) {
		$flag = 1;
		next;
	}
	elsif($_ =~ /^\[(.*)\]$/ && $1 ne $search_string) {
		$flag =0;
	}
	elsif($flag == 1) {
		$clean_path = $_;
		$clean_path =~ s|"||mg;
		chomp($clean_path);
	}
	 
  }
  close CFG;

  return $clean_path;
}


1;
