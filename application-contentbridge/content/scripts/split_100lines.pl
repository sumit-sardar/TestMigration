#!`iwgethome`/iw-perl/bin/iwperl -w

use strict;

my @files = @ARGV;
my $i;
my $file_counter = 0;

foreach $i (@files) {
	my $line_counter = 0;
	my $string = "";
	open(FILE, $i);
	while (<FILE>) {
		next if /^$/;
		if ($line_counter == 100) {
			$file_counter++;
			$line_counter = 1;
			&CreateNewFile($file_counter, $i, $string);			
			$string = $_;
		} else {
			$line_counter++;
			$string .= $_;
		}
	}
	
	$file_counter++;
	&CreateNewFile($file_counter, $i, $string);
}

sub CreateNewFile {
	my ($file_counter, $new_file, $string) = @_;
	$new_file =~ s/\./\_$file_counter\./;
				
	open(NEW_FILE, ">$new_file");
	print NEW_FILE $string;
	close NEW_FILE;
}
