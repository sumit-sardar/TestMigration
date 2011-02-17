#!/usr/bin/perl

$csvfile = "$ARGV[0]";

if ( $csvfile eq "")
{ 
	$csvfile = "oas.csv";
}
#$csvfile="tsitems.txt";
open(INF, $csvfile) or dienice("Can't open $csvfile: $!");
@totallist = <INF>;
close(INF);

($day, $mon, $year) = (localtime(time))[3, 4, 5];
$year += 1900;
$mon++;

if ($day < 10) {
  $day = "0$day";
}

if ($mon < 10) {
  $mon = "0$mon";
}

$i = "Item_Detail_";
$info = "";
$prefix = "";
if ($csvfile =~ m/(\w+)(_)($i)(\w+)/) {  
  $prefix = $1;
  $word = $4;
  if ($word =~ m/(\w+)(_)(\w*)/) {     
     $info = $1;
  }
}

#print "info is $info";  
#$outfile="oascounts.csv";
$outfile = "$prefix\_Item_Counts_$info\_$mon$day$year.csv";
open(OUTF,">$outfile") or dienice("Can't open $outfile: $!");
for ($i = 2; $i < 13; $i += 1)
        {
                $math[$i]=0;
                $reading[$i] = 0;
                $lang[$i] = 0;
		$science[$i] = 0;
		$social[$i] = 0;
		$mathsr[$i] = 0;
		$mathcr[$i] = 0;
		$readingsr[$i] = 0;
		$readincr[$i] = 0;
		$sciencesr[$i] = 0;
		$sciencecr[$i] = 0;
		$socialsr[$i] = 0;
		$socialcr[$i] = 0;		
        }
foreach $line (@totallist) {
	chomp ($line);
        ($item_id, $item_type, $dummy ) = split(/,/, $line);
        ($firstdot, $restofitemid) = split (/\./, $item_id);
#	print "got: $firstdot, item: $item_id";
	for ($i = 2; $i < 13; $i += 1)
	{
        	if ($firstdot =~ /$i/ ) {
			count ($i, $firstdot, $item_type);	
		}
	}
#	print "WL : $wordlength ";
}

#ALL ITEM COUNTS
print OUTF "ALL ITEM COUNTS\n";
print OUTF "Grade, Reading, Math , Language, Science, Social Studies,Grade Total\n";
$mtotal = $rtotal = $ltotal = $sctotal = $sstotal = $gtotal = 0;
for ($i = 2; $i < 13; $i += 1)
        {
	      $mtotal = $mtotal + $math[$i];
	      $rtotal = $rtotal + $reading[$i];
	      $ltotal = $ltotal + $lang[$i];
	      $sctotal = $sctotal + $science[$i];
	      $sstotal = $sstotal + $social[$i];
	      $gtotal = $reading[$i] + $math[$i] + $lang[$i] + $science[$i] + $social[$i];
	      print OUTF "Grade $i,$reading[$i],$math[$i], $lang[$i], $science[$i], $social[$i],$gtotal\n";
	}
$grandtotal = $rtotal + $mtotal + $ltotal + $sctotal + $sstotal;
print OUTF "TOTAL:, $rtotal,$mtotal,$ltotal,$sctotal,$sstotal,$grandtotal\n\n";

#SR ITEM COUNTS
print OUTF "SR ITEM COUNTS\n";
print OUTF "Grade, Reading, Math , Language, Science, Social Studies,Grade Total\n";
$msrtotal = $rsrtotal = $lsrtotal = $scsrtotal = $sssrtotal = $gsrtotal = 0;
for ($i = 2; $i < 13; $i += 1)
        {
	      $msrtotal = $msrtotal + $mathsr[$i];
	      $rsrtotal = $rsrtotal + $readingsr[$i];
	      $lsrtotal = $lsrtotal + $langsr[$i];
	      $scsrtotal = $scsrtotal + $sciencesr[$i];
	      $sssrtotal = $sssrtotal + $socialsr[$i];
	      $gsrtotal = 
	       $readingsr[$i] + $mathsr[$i] + $langsr[$i] + $sciencesr[$i] + $socialsr[$i];
	      print OUTF "Grade $i,$readingsr[$i],$mathsr[$i], $langsr[$i], $sciencesr[$i], $socialsr[$i],$gsrtotal\n";
	}
	
$grandsrtotal = $rsrtotal + $msrtotal + $lsrtotal + $scsrtotal + $sssrtotal;
print OUTF "SR ITEM TOTAL:, $rsrtotal,$msrtotal,$lsrtotal,$scsrtotal,$sssrtotal,$grandsrtotal\n\n";

#CR ITEM COUNTS
print OUTF "CR ITEM COUNTS\n";
print OUTF "Grade, Reading, Math , Language, Science, Social Studies,Grade Total\n";
$mcrtotal = $rcrtotal = $lcrtotal = $sccrtotal = $sscrtotal = $gcrtotal = 0;
for ($i = 2; $i < 13; $i += 1)
        {
	      $mcrtotal = $mcrtotal + $mathcr[$i];
	      $rcrtotal = $rcrtotal + $readingcr[$i];
	      $lcrtotal = $lcrtotal + $langcr[$i];
	      $sccrtotal = $sccrtotal + $sciencecr[$i];
	      $sscrtotal = $sscrtotal + $socialcr[$i];
	      $gcrtotal = 
	       $readingcr[$i] + $mathcr[$i] + $langcr[$i] + $sciencecr[$i] + $socialcr[$i];
	      print OUTF "Grade $i,$readingcr[$i],$mathcr[$i], $langcr[$i], $sciencecr[$i], $socialcr[$i],$gcrtotal\n";
	} 
$grandcrtotal = $rcrtotal + $mcrtotal + $lcrtotal + $sccrtotal + $sscrtotal;
print OUTF "CR ITEM TOTAL:, $rcrtotal,$mcrtotal,$lcrtotal,$sccrtotal,$sscrtotal,$grandcrtotal";
close (OUTF);

sub count {

my ($grade) = $_[0];
my ($item) = $_[1];
my ($itemType) = $_[2];
#print "item type $itemType\n";
if ($item =~ /R/){
	$reading[$grade] = $reading[$grade] + 1;
	if ($itemType =~ /SR/) {
	  $readingsr[$grade] = $readingsr[$grade] + 1;
	} 
	elsif ($itemType =~ /CR/) {
	  $readingcr[$grade] = $readingcr[$grade] + 1;
	} 	  
}
elsif ($item =~ /M/){
        $math[$grade] = $math[$grade] + 1;
	if ($itemType =~ /SR/) {
	  $mathsr[$grade] = $mathsr[$grade] + 1;
	} 
	elsif ($itemType =~ /CR/) {
	  $mathcr[$grade] = $mathcr[$grade] + 1;
	}         
}
elsif ($item =~ /L/){
        $lang[$grade] = $lang[$grade] + 1;
	if ($itemType =~ /SR/) {
	  $langsr[$grade] = $langsr[$grade] + 1;
	} 
	elsif ($itemType =~ /CR/) {
	  $langcr[$grade] = $langcr[$grade] + 1;
	}         
}
elsif ($item =~ /SC/){
        $science[$grade] = $science[$grade] + 1;
	if ($itemType =~ /SR/) {
	  $sciencesr[$grade] = $sciencesr[$grade] + 1;
	} 
	elsif ($itemType =~ /CR/) {
	  $sciencecr[$grade] = $sciencecr[$grade] + 1;
	}         
}
elsif ($item =~ /H/ || $item =~ /C/ || $item =~ /E/ || $item =~ /G/ ) {
        $social[$grade] = $social[$grade] + 1;
	if ($itemType =~ /SR/) {
	  $socialsr[$grade] = $socialsr[$grade] + 1;
	} 
	elsif ($itemType =~ /CR/) {
	  $socialcr[$grade] = $socialcr[$grade] + 1;
	}         
}
#print "$grade, $item!";
}

sub dienice {
	my($msg) = @_;
	print "Error:\n";
	print $msg;
	exit;
}

