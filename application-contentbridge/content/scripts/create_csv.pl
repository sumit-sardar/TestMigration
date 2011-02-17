#!/usr/bin/perl

$csvfile="objectives.txt";

open(INF, $csvfile) or dienice("Can't open $csvfile: $!");
@array = <INF>;
close(INF);

open(TXTF, "ITEM_SET_REP.txt") or dienice("Can't open $csvfile: $!");
@txtarray = <TXTF>;
close(TXTF);

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
							$product = "CTB Reading";
						}
						elsif ($pair =~ /M/)
						{
							$product = "CTB Mathematics";
						}
						elsif ($pair =~ /L/)
						{
							$product = "CTB Language";
						}
						elsif ($pair =~ /H/)
						{
							$product = "CTB History";
						}		
						elsif ($pair =~ /E/)
						{
							$product = "CTB Economics";
						}
						elsif ($pair =~ /G/)
						{
							$product = "CTB Geography";
						}
						elsif ($pair =~ /SC/)
						{
							$product = "CTB Science";
						}
						elsif ($pair =~ /C/)
						{
							$product = "CTB Civics";
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
		        for ($j = 0; $j < $#txtarray; $j += 1)
			{
        			($grade2,$subject2, $code2, $version2, $parent2, $itemsetcatlevel2 ) = split(/","/, $txtarray[$j]);
				if ( $code eq $code2)
				{
					$subject = $subject2;	
					last;
				}
			}
			$parent_code = $code;
			$parent_code =~ s/(.*)\.\d+$/$1/g;
#                        for ($k = 0; $k < $#array; $k += 1)
#                        {
#                                ($subject3, $code3, $version3, $parent3, $itemsetcatlevel3 ) = split(/","/, $array[$k]);
#print "GOT:  $subject3, $code3, $version3, $parent3 -- comparing $parent with $version3\n";
#
#                                if ( $parent eq $version3 )
#                                {
#                                        $parent_cmsid = $code3;
#                                        last;
#                                }
#                        }
#
		}
		$lines = $lines + 1;
		$itemsetcatlevel =~ s/"//g;
		$category = $itemsetcatlevel +1;
	}
	$subject = ucfirst(lc($subject)); 
	print NEWF "\"$grade\",\"$subject\",\"$code\",\"$parent_code\",\"$category\",\"$product\"\n";
#	print " ENTRY: \"$grade\",\"$subject\",\"$code\",\"$parent_cmsid\",\"$category\",\"$product\"\n";
	}

close(NEWF);

print "Updated $lines of entries in $readfile.\n";



sub dienice {
	my($msg) = @_;
	print "Error:\n";
	print $msg;
	exit;
}

