#!/usr/bin/perl

#Check input argument
if ( !$ARGV[0])
{
  print "Need input file.\n";
  exit;
}

$readfile=$ARGV[0];

$tempfile="$readfile.sed";
print "Inputfile: $readfile , Outputfile: $tempfile\n";

if (! -r $readfile) {
die "Can't read input $readfile\n";
}
if (! -f $readfile) {
die "Input $readfile is not a plain file\n";
}

open(INF, $readfile) or dienice("Can't open $readfile: $!");
@temparray = <INF>;
close(INF);


open(OUTF, ">$tempfile") or dienice("Couldn't open $tempfile for writing: $!");

# Reset the file pointer to the end of the file, in case 
# someone wrote to it while we waited for the lock...
#seek(OUTF,0,2);

#change the original single line xml to xml file with line breaks after each tag and P tags
foreach $line (@temparray) {
    $line =~ s/></>\n</g;
    #$line =~ s/<[pP]>/<P>\n/g;
    #$line =~ s/></>\n</g;
    $line =~ s/\cM//g;
    #chomp($line);
      print OUTF "$line";
     
}
print OUTF "\n";
close(OUTF);

open(INF, "$tempfile") or dienice("Can't open $readfile: $!");
@contentarray = <INF>;
close(INF);

open(OUTF,">$tempfile") or dienice("Couldn't open $tempfile for writing: $!");


foreach $line (@contentarray) {
#	use strict;

	$line =~ s/\/default\/main\///g;
 #  print "NEW: $line"; 
    chomp($line);
    print OUTF "$line";
}
close(OUTF);

exit;
