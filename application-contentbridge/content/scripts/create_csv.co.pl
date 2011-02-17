#!/usr/bin/perl

$csvfile="objectives.txt.co";
open(INF, $csvfile) or dienice("Can't open $csvfile: $!");
@array = <INF>;
close(INF);

open(NEWF, ">$csvfile.new") or dienice("Can't open $readfile: $!");
$lines=0;
foreach $line (@array) {
	chomp ($line);
	($subject, $code, $version, $parent, $itemsetcatlevel ) = split(/","/, $line);
	$itemsetcatlevel =~ s/"//g;
	$subject =~ s/"//g;
		
	if ($itemsetcatlevel eq "0" and ($code =~ /CTB/ || $code =~ /TX/ || $code =~ /CO/))
	{
		$framework_code = $code;
		$parent_code = "0";
		$category = 1;
		$product = $framework_code;
	}
	elsif ($itemsetcatlevel eq "1")
	{
		$grade = $subject;
		$grade =~ s/Grade //g;
		$category = 2;
		$parent_code = $framework_code;
		$product = $framework_code;
	}
	elsif ($itemsetcatlevel eq "2")
	{
						if ($subject =~ /Reading/)
						{
							$product = "CO Reading";
						}
						elsif ($subject =~ /Math/)
						{
							$product = "CO Mathematics";
						}
						elsif ($subject =~ /Writing/)
						{
							$product = "CO Writing";
						}
						elsif ($subject =~ /History/)
						{
							$product = "CO History";
						}		
						elsif ($subject =~ /Economics/)
						{
							$product = "CO Economics";
						}
						elsif ($subject =~ /Geography/)
						{
							$product = "CO Geography";
						}
						elsif ($subject =~ /Science/)
						{
							$product = "CO Science";
						}
						elsif ($subject =~ /Civics/)
						{
							$product = "CO Civics";
						}
						$category = 3;
						$parent_code = $grade;
		}   
	if ($itemsetcatlevel > 1) {
		        for ($j = 0; $j < $#array; $j += 1)
			{
        			($subject2, $code2, $version2, $parent2, $itemsetcatlevel2 ) = split(/","/, $array[$j]);
				if ( $parent eq $version2)
				{
					$parent_code = $code2;	
					last;
				}
			}

	}
		$itemsetcatlevel =~ s/"//g;
		$category = $itemsetcatlevel +1;

	print NEWF "\"$grade\",\"$subject\",\"$code\",\"$parent_code\",\"$category\",\"$product\"\n";

	}
close(NEWF);

print "Updated $lines of entries in $readfile.\n";



sub dienice {
	my($msg) = @_;
	print "Error:\n";
	print $msg;
	exit;
}

