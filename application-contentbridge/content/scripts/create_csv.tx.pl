#!/usr/bin/perl

$csvfile="objectives.txt.tx";
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
		
	if ($code =~ /CTB/ || $code =~ /TX/ || $code =~ /CO/)
	{
		$framework_code = $code;
		$parent_code = "0";
		$category = 1;
	}
	else
	{
		@pairs = split(/\./, $code);
		$dotlength=$#pairs;
	#	print "WL : $wordlength ";
		if ($dotlength == 0) {
		   foreach $pair (@pairs) {

				$bytesize = length $pair;
				if ($bytesize == 1){
					$category = 2;
					$grade = $pair;
					$parent_code = $framework_code;
					$product = "";
				}
				else{

					if ($pair =~ /R/ || $pair =~ /M/ || $pair =~ /L/ || $pair =~ /SC/ || $pair =~ /H/ || $pair =~ /E/ || $pair =~ /C/ || $pair =~ /G/)
					{
						if ($pair =~ /R/)
						{
							$product = "TX Reading";
						}
						elsif ($pair =~ /M/)
						{
							$product = "TX Mathematics";
						}
						elsif ($pair =~ /L/)
						{
							$product = "TX Language";
						}
						elsif ($pair =~ /H/)
						{
							$product = "TX History";
						}		
						elsif ($pair =~ /E/)
						{
							$product = "TX English Language Arts";
						}
						elsif ($pair =~ /G/)
						{
							$product = "TX Geography";
						}
						elsif ($pair =~ /SC/)
						{
							$product = "TX Science";
						}
						elsif ($pair =~ /C/)
						{
							$product = "TX Civics";
						}
						$category = 3;
						$parent_code = $code;
						$parent_code =~ s/[RMLEHG]|SC//g;
					}
					else
					{
						$category = 2;
						$grade = $pair;
						$parent_code = $framework_code;
						$product = "";
					}
				}
		   }
		}   
		else {
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
		$lines = $lines + 1;
		$parent =~ s/"//g;
		$itemsetcatlevel =~ s/"//g;
		$itemsetcatlevel = $itemsetcatlevel + 1;
		$category = $itemsetcatlevel;

	}

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

