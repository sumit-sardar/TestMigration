#!/usr/bin/perl

$csvfile="WVA_STRUCTURE.txt";
open(INF, $csvfile) or dienice("Can't open $csvfile: $!");
@array = <INF>;
close(INF);

open(NEWF, ">$csvfile.new") or dienice("Can't open $readfile: $!");

$newline[0]="\"\",\"West Virginia CF Root\",\"WV\",\"0\",\"1\",\"\"\n";
$newline[1]="\"\",\"Reading Language Arts\",\"RLA\",\"WV\",\"2\",\"WV Reading Language Arts\"\n";
$newline[2]="\"\",\"Mathematics\",\"MA\",\"WV\",\"2\",\"WV Mathematics\"\n";
$newline[3]="\"\",\"Social Studies\",\"SS\",\"WV\",\"2\",\"WV Social Studies\"\n";
$newline[4]="\"\",\"Science\",\"SC\",\"WV\",\"2\",\"WV Science\"\n";
$i=5;

foreach $line (@array) {
	chomp ($line);
	($grade, $std_code, $std_name, $obj_code, $obj_name) = split(/\t/, $line);
	$obj_code =~ s/ //g;
	$std_code =~ s/ //g;
	$obj_name =~ s/"//g;
	#$obj_name =~ s///g;
	$obj_name =~ s/\.//g;
	$obj_name = ucfirst(lc($obj_name));
	if ($grade =~ /Grade/)
	{
		next;
	}	
	#Categorize product
             if ($std_code =~ /RLA/ || $std_code =~ /MA/ || $std_code =~ /SC/ || $std_code =~ /SS/ )
                {
                                                if ($std_code =~ /RLA/)
                                                {
                                                        $product = "WV Reading Language Arts";
							$product_code = "RLA";
                                                }
                                                elsif ($std_code =~ /MA/)
                                                {
                                                        $product = "WV Mathematics";
							$product_code = "MA";
                                                }
                                                elsif ($std_code =~ /SS/)
                                                {
                                                        $product = "WV Social Studies";
							$product_code = "SS";
                                                }
                                                elsif ($std_code =~ /SC/)
                                                {
                                                        $product = "WV Science";
							$product_code = "SC";
                                                }
                   }

	#Check if grade exists
	$grade_ca = "$grade"."\."."$product_code";
	$grade_check = "notfound";
	for ($j = 1; $j < $#newline; $j +=1)
	{
		($new_grade, $new_objname, $new_cmscode, $new_parentcode, $new_itemsetcat, $new_product) = split(/","/, $newline[$j]);			
		$new_grade =~ s/"//g;
		#print "GOT $new_grade";
		#print "Comparing grade:$grade with:$new_grade";
		if ( $grade_ca eq $new_cmscode)
		{
		#	print "Grade MATCH!";
			$grade_check = "found";
			last;
		}
	}
 	
	$new_std_code = "$grade"."\."."$std_code";
        #Check if standard exists
	$std_check = "notfound";
        for ($j = 1; $j < $#newline; $j +=1)
        {
                ($new_grade, $new_objname, $new_cmscode, $new_parentcode, $new_itemsetcat, $new_product) = split(/","/, $newline[$j]);

                if ( $new_std_code eq $new_cmscode)
                {
                        $std_check = "found";
                        last;
                }
        }

	if ($grade_check eq "notfound" )
	{
		$newline[$i] = "\"$grade\",\"Grade $grade\",\"$grade_ca\",\"$product_code\",\"3\",\"$product\"\n";
		$i = $i + 1;
	}
	
        if ($std_check eq "notfound" )
        {
                $newline[$i] = "\"$grade\",\"$std_name\",\"$new_std_code\",\"$grade_ca\",\"4\",\"$product\"\n";
                $i = $i + 1;
        }                               

	$newline[$i] = "\"$grade\",\"$obj_name\",\"$obj_code\",\"$new_std_code\",\"5\",\"$product\"\n";
        $i = $i + 1;
	}   

	for ($j = 0; $j < $#newline; $j += 1)
	{
	print NEWF $newline[$j];
	}

close(NEWF);

print "Updated $j of entries in $csvfile.new.\n";



sub dienice {
	my($msg) = @_;
	print "Error:\n";
	print $msg;
	exit;
}

