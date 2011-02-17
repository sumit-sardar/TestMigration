package CTB::XML;

#------------------------------------------------------------------------------
# Copyright 2003 Interwoven, Inc.   All rights reserved.
#
# Author    Bobby Magee <bobby@interwoven.com>
#
# File      CTB::XML.pm
# Use       Parse XML documents using Perl's XML::Parser
#
#------------------------------------------------------------------------------
use XML::Parser;
use CTB::RegEx;

=head1 NAME

CTB::XML - CTB XML Parsing

%%_VERSION_%%

=head1 SYNOPSIS

Utilities for parsing CTB files and strings.


=cut

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 get_items()

Takes an XML file and the area string 
(e.g. "/default/main/CAB/MATH/WORKAREA/load")

Returns the document type (e.g. Assessment, SubTest, ItemSet), 
the grade number, a hash with the list of items.  The item ids are the keys.

NOTE: THIS WILL ONLY WORK WITH GRADES OF THE FORMAT "Grade #". 
 
Example:

    use CTB::XML;
    
    my ($gi, $grade, %items) = CTB::XML::get_items("Job_Documents/test.xml", "/default/main/CAB/MATH/WORKAREA/load");

=cut
#------------------------------------------------------------------------------


sub get_items {

	my $file = $_[0];
	my $area = $_[1];
	my $flag = 0;
	my $doc_type = "####";
	my $retval = "";
	my $id;
	my %ids;
	my $grade = "-1";
	my %items;
	my $p= new XML::Parser( Handlers =>
	                         { Start   => \&start,
	                           End     => \&end,
	                           Default => \&default
	                         },
	                      );
	$p->parsefile("$area/$file");

	return $doc_type, $grade, %items;
	exit;
	
	# by default print the UTF-8 encoded string received from the parser

	sub start  { 

			my ($p, $data, %atts)= @_;
			# print $data;
			if ($doc_type eq "####") {
				$doc_type = $data;
			}
			if ($data =~ /^Item$/) {
				# print "$data\n";
				$flag = 1;
				$id = $atts{'ID'};
				my $string= $p->recognized_string();
				$retval .= $string;
			} elsif ($data =~ /^Hierarchy/ && $atts{'Type'} eq "Grade" && $grade eq "-1") {
				$atts{'Name'} =~ /(\d+)$/;
				$grade = $1;
				my $string= $p->recognized_string();
				$retval .= $string;
			} elsif ($flag == 1) {	
				my $string= $p->recognized_string();
				$retval .= $string;
			}
	}

	sub end  { 

		my ($p, $data)= @_;
		# print $data;
		if ($data =~ /^Item$/ && $flag == 1) {
			$flag = 0;

			my $string= $p->recognized_string();
			$retval .= $string;
			
			# Before we return the value we search and replace several strings
			$retval =~ s/\n//mg;
			$retval =~ s/&nbsp;/ /gm;
			$retval = CTB::RegEx::handle_images($retval, $area);
			
			$items{$id} = $retval;
			%ids = ();
			$retval = "";
			$id = "";
			#print "$string";
		}
		elsif ($flag == 1) {	
				my $string= $p->recognized_string();
				$retval .= $string;
		}
	}

	sub default { 

		my ($p, $data)= @_;
		# print $data;
		if (!($data =~ /Item/) && $flag == 1 ) {
			my $string= $p->recognized_string();
			$retval .= $string;
			#print "$string";
		}
	}

}


1;