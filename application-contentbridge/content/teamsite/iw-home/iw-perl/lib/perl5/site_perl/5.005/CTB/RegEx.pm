package CTB::RegEx;

#------------------------------------------------------------------------------
# Copyright 2003 Interwoven, Inc.   All rights reserved.
#
# Author    Bobby Magee <bobby@interwoven.com>
#
# File      CTB::RegEx.pm
# Use       Parse XML documents using Perl's Regular Expression
#
#------------------------------------------------------------------------------

=head1 NAME

CTB::RegEx - CTB Regular Expressions

%%_VERSION_%%

=head1 SYNOPSIS

Utilities for parsing CTB files and strings.


=cut

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

# This is where we define all the replacements that need to be done for JGEN



my %parse4jgen =
(
	'&nbsp;'  =>  ' ',
	'&#160;' => ' ',
	
);

# This is where we define all the replacements that need to be done for FOP

my %parse4fop =
(
	'&nbsp;'  =>  ' ',
	'&USpace;' => '&#160;',
);

=head1 get_items()

Takes an XML file and the area string 
(e.g. "/default/main/CAB/MATH/WORKAREA/load")

Returns a hash with the list of items.  The item ids are the keys.
This is going to be replaced by the CTB::XML::get_items() routine.


Example:

    use CTB::RegEx;
    my %items = CTB::RegEx::get_items("test.xml", 
                                      "/default/main/CAB/MATH/WORKAREA/load");

=cut
#------------------------------------------------------------------------------

sub get_items {

	my $file = $_[0];
	my $area = $_[1];
	my %items; 
	my ($temp_line, $item_line);
	my $flag = " ";

	open (XML, "$area/$file") || die "can't open $file";
		LINE: while (<XML>) {        
			if ($_ =~ /<\/Item>/)
			{
				/(.*<\/Item>)/;
				chomp($temp_line = $1);
				$item_line = "$item_line"."$temp_line";
				$item_line =~ s/&nbsp;/ /gm;
				$item_line =~ s/&USpace;/&#160;/gm;
				$item_line =~ s/"\.\.\/images/"$area\/images/gm;
				$items{ $item_id } = $item_line;
				$flag = " ";
			}
			if (/<Item\s+[^>]*\s+ID="([^"]+)/ || /<Item\s+ID="([^"]+)/)
			{
				$item_line = "";
				$item_id = $1;
				$flag = "$1.swf";
				push (@item_id, "$item_id");
				chomp($temp_line = $_);
				$temp_line =~ /(<Item\s.*)/;
				$item_line = $1;
			}
			elsif ($flag ne " ")
			{
				chomp($temp_line = $_);
				$item_line = "$item_line"."$temp_line";
				next LINE;
			}
			else
			{
				next LINE;
			}
		}
		close XML;

	return %items;
}
#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 full_doc()

Returns a string with the full document.  This subroutine takes a file with
its absolute path. 

This routine will change the file name for all images to be absolute.

(e.g. FileName="iwts:/images/blah.eps"  

	becomes 
	
      FileName="/default/main/CAB/MATH/WORKAREA/load/images/blah.eps")


Example:

    use CTB::RegEx;
    my $file = "/default/main/CAB/MATH/WORKAREA/load/Job_Documents/blah.xml";
    my $doc_string = CTB::RegEx::full_doc($file);

=cut
#------------------------------------------------------------------------------

sub full_doc {
	
	my $file_string;
	my $old_string;
	my $file = $_[0];
	
	$file =~ /^(.*\/WORKAREA\/[^\/]+)\//;
	my $area = $1;
	
	open (XML, $file) || die "can't open $file";
	while (<XML>) {
		chomp;
		$old_string .= $_;
	
	}
	close XML;
	
	$old_string =~ s/^.*DOCTYPE[^>]+>(.*)/$1/;
	$file_string = &CTB::RegEx::handle_images($old_string, $area);
	$file_string =~ s/&USpace;/&#160;/mg;
	$file_string =~ s/&nbsp;/ /mg;
	return $file_string;
}
#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------


=head1 doc_type()

Takes an XML string as an argument. 
Returns a string with the name of the document type.  
This subroutine will be replaced by 

CTB::XML::get_items();

Example:

    use CTB::RegEx;
    my $file = "/default/main/CAB/MATH/WORKAREA/load/Job_Documents/blah.xml";
    my $doc_string = CTB::RegEx::full_doc($file);
    my $doc_type = CTB::RegEx::doc_type($doc_string);

=cut
#------------------------------------------------------------------------------


sub doc_type {

	my $xml = $_[0];
	if ($xml =~ /<Assessment\s/) {
		return "Assessment";
	} elsif ($xml =~ /<SubTest\s/) {
		return "SubTest";
	} elsif ($xml =~ /<ItemSet\s/) {
		return "ItemSet";
	}
}
#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 get_grade()

Takes an XML string as an argument.  Returns grade number.  

This will return "End of Course" if the grade does not exist.

This subroutine will be replaced by 

CTB::XML::get_items();

Example:

    use CTB::RegEx;
    my $file = "/default/main/CAB/MATH/WORKAREA/load/Job_Documents/blah.xml";
    my $doc_string = CTB::RegEx::full_doc($file);
    my $doc_type = CTB::RegEx::doc_type($doc_string);
    my $grade = CTB::RegEx::get_grade($doc_string);

=cut
#------------------------------------------------------------------------------

sub get_grade {
	my $file = $_[0];
	$file =~ /<Hierarchy[^>]*Name="Grade (\d+)"/;
	my $ret = !(defined $1) ? "END OF COURSE" : $1;
	return $ret;
}

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 item_type( $file )

Takes an XML file as an argument.  Returns either "CR" or "SR".  


This subroutine will be replaced by 

CTB::XML::get_items();

Example:

    use CTB::RegEx;
    my $file = "/default/main/CAB/MATH/WORKAREA/load/Job_Documents/blah.xml";
    my $doc_string = CTB::RegEx::full_doc($file);
    my $doc_type = CTB::RegEx::doc_type($doc_string);
    my $grade = CTB::RegEx::get_grade($doc_string);
    my $type = CTB::RegEx::item_type($file);

=cut
#------------------------------------------------------------------------------

sub item_type {
	my $ret;
	my $file = $_[0];
	open(XML, $file);
	while (<XML>) {
		/<Item\s[^>]*ItemType="([^"]+)"/; # "
		if (defined $1) {
			$ret = $1;
			last;
		}
	}
	close XML;
	defined $ret ? return $ret : return "SR";
}

#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 get_images()

Takes an XML string as an argument.Returns an array of names of images.  

This returns the absolute path from the workarea.:

(e.g. FileName="/default/main/CAB/MATH/WORKAREA/load/images/blah.eps" 

	will return
	
      "/images/blah.eps")
      

Example:

    use CTB::RegEx;
    my $file = "/default/main/CAB/MATH/WORKAREA/load/Job_Documents/blah.xml";
    my $doc_string = CTB::RegEx::full_doc($file);
    my $doc_type = CTB::RegEx::doc_type($doc_string);
    my $grade = CTB::RegEx::get_grade($doc_string);
    my @images = CTB::RegEx::get_images($doc_string);

=cut
#------------------------------------------------------------------------------

sub get_images {
        my $str = $_[0];
	my @img = ();
	
	IMAGE:	while ($str =~ /FileName="[^"]+(\/images[^"]+(swf|eps))"/gi) {
		push @img, $1;
	}
	my %seen = ();
        my @uniqu = grep { ! $seen{$_} ++ } @img;
	return @uniqu;
}
#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 handle_uspace()

Takes an XML file as an argument.  The script doesn't return anything.

The script just changes the file that you pass it.

This script has been replaced by CTB::RegEx::parse4jgen()      

Example:

    use CTB::RegEx;
    use File::Copy;
    
    my $file = "/default/main/CAB/MATH/WORKAREA/load/Job_Documents/blah.xml";
    my $compdisp = "/appl/tmp/blah.xml";
    copy($file, $compdisp);
    CTB::RegEx::uspace_file($compdisp);
    
=cut
#------------------------------------------------------------------------------

sub handle_uspace {
	my $file = $_[0];
	
			
	open(OLD, $file)         or die "can't open $old: $!";
	open(NEW, "> $file.new")         or die "can't open $new: $!";
	while (<OLD>) {
	    s/&#160;/ /mg;
	    print NEW $_            or die "can't write $new: $!";
	}
	close(OLD)                  or die "can't close $old: $!";
	close(NEW)                  or die "can't close $new: $!";
	rename($file, "$file.orig")   or die "can't rename $old to $old.orig: $!";
	rename("$file.new", $file)          or die "can't rename $new to $old: $!";
	unlink "$file.orig";
}
#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 handle_images()

Takes an XML string and the area (e.g. "/default/main/CAB/MATH/WORKAREA/load") 
as an argument.  The function returns the modified string.  

NOTE:  CTB::RegEx::$doc_type() already calls this function
Example:

    use CTB::RegEx;
        my $old_$string = "<FileName=\"../images/blah.esp\"/>";
        my $area = "/default/main/CAB/MATH/WORKAREA/load";
        my $new_string = CTB::RegEx::handle_images($old_string, $area);   	
    
=cut
#------------------------------------------------------------------------------

sub handle_images {
	my $string = $_[0];
	my $area = $_[1];
	$string =~ s/FileName="[^\b\/default\/\b][^"]+\/(images[^"]+)/FileName="$area\/$1/ig; # "
	return $string;

}
#-----------------------------------------------------------------------------
#-----------------------------------------------------------------------------

=head1 parse4fop()

Takes an XML file as an argument.  The script doesn't return anything.

The script just changes the file that you pass it.
 
Example:

    use CTB::RegEx;
    use File::Copy;
    
    my $file = "/default/main/CAB/MATH/WORKAREA/load/Job_Documents/blah.xml";
    my $fopfile = "/appl/tmp/blah.xml";
    copy($file, $fopfile);
    CTB::RegEx::parse4fop($fopfile);
    
=cut
#------------------------------------------------------------------------------

sub parse4fop {
	
	my $file = $_[0];
	my $key;
	my $value;
	my $string;
	
			
	open(OLD, $file)         or die "can't open $old: $!";
	open(NEW, "> $file.new")         or die "can't open $new: $!";
	while (<OLD>) {
		my $string = $_;	
		foreach $key (keys (%parse4fop)) {
			$value = $parse4fop{$key};    	
			$string =~ s/$key/$value/mg;
		}
		print NEW $string            or die "can't write $new: $!";
	}
	close(OLD)                  or die "can't close $old: $!";
	close(NEW)                  or die "can't close $new: $!";
	rename($file, "$file.orig") or die "can't rename $old to $old.orig: $!";
	rename("$file.new", $file)  or die "can't rename $new to $old: $!";
	unlink "$file.orig";
	
}

=head1 parse4fop()

Takes an XML file as an argument.  The script doesn't return anything.

The script just changes the file that you pass it.
 
Example:

    use CTB::RegEx;
    use File::Copy;
    
    my $file = "/default/main/CAB/MATH/WORKAREA/load/Job_Documents/blah.xml";
    my $compdisp = "/appl/tmp/blah.xml";
    copy($file, $compdisp);
    CTB::RegEx::parse4fop($compdisp);
    
=cut
#------------------------------------------------------------------------------

sub parse4jgen {
	my $file = $_[0];
	my $key;
	my $value;
	my $string;
	
	
	open(OLD, $file)         or die "can't open $old: $!";
	open(NEW, "> $file.new")         or die "can't open $new: $!";
	while (<OLD>) {
		my $string = $_;	
	    	foreach $key (keys (%parse4jgen)) {
			$value = $parse4jgen{$key};    	
	    		$string =~ s/$key/$value/mg;
	    		# print "s/$key/$value/\n";
	    	}
	    	print NEW $string            or die "can't write $new: $!";
	}
	close(OLD)                  or die "can't close $old: $!";
	close(NEW)                  or die "can't close $new: $!";
	rename($file, "$file.orig")   or die "can't rename $old to $old.orig: $!";
	rename("$file.new", $file)          or die "can't rename $new to $old: $!";
        unlink "$file.orig";
	
}

1;