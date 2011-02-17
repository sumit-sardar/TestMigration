#!/usr/bin/perl

if (@ARGV < 4) {
   print "Usage: checkmappint.pl <objective table> <State_Item_Map_Table> <Abbrv_State_Name> <Input Directory>\n\n";
   print "example: checkmappint.pl objectives.txt Texas_CAB_Item_Map_G5_Math.txt TX /default/main/TEXAS/MATH/WORKAREA/load/Content/grade_5/SR\n\n";
   exit 0;
}

use File::Find;
sub process_file {
#   if (/^.*\.xml$/ && ! -d ) {
   if (/^.*$/ && ! -d ) {
#      print $File::Find::name . "\n";
      $f_name[$i++] = $File::Find::name;
  }
}

#$/ = '<\/Item>';             # Input line seperator

$f_objectivefile =  $ARGV[0]; 
shift(@ARGV);

open (objectivefile, "$f_objectivefile") ||
   die ("Unable to open $f_objectivefile");

$i = 0;
while (<objectivefile>) {
   s/"|\n|\r//g;
   @objectivetable = split ("::", $_);
   $objectivehash{$objectivetable[1]} = [ @objectivetable ];
#   print $_;
   $j = 0;
#   while ($j < @objectivetable) {
#      print "$objectivetable[$j]\n";
#      $j++;
#   } 
   $i++;
}
close (objectivefile);

$f_mapfile = $ARGV[0];
shift(@ARGV);

open (mapfile, "$f_mapfile") ||
   die ("Unable to open: $f_mapfile\n");


while (<mapfile>) {
   s /"|\n|\r//g;
   @maptable = split (",", $_);
   $maphash{$maptable[0]} = $maptable[1];
}

#$j = 0;
#foreach $j (keys %objectivehash) {
#   @s_tmp = @{$objectivehash{$j}};
#   print "key: @{$objectivehash{$j}}\n";
#   print "key: $s_tmp[2]\n";
#}

$s_statename = $ARGV[0]; 
shift(@ARGV);

###########################################################
undef $/;           # each read is whole file
$i = 0;
$s_dirname = $ARGV[0];
#$s_dirname = '/default/main/TEXAS/MATH/WORKAREA/load/Content/grade_5/SR';
#find (\&process_file, '/default/main/CAB/MATH/WORKAREA/load/Content/Grade_3');
#find (\&process_file, '/export/home/interwoven/dngo/perl');
#find (\&process_file, '/default/main/TEXAS/MATH/WORKAREA/load/Content/grade_5/SR');
find (\&process_file, $s_dirname);

foreach $item (@f_name) {
   $f_xmlfile = $item;
#   $f_xmlfile = $ARGV[0];
   print "\nProcessing... $f_xmlfile\n";

   open (xmlfile, "$f_xmlfile") ||
      die ("Unable to open: $f_xmlfile\n");

   while (<xmlfile>) {
      if ($_ =~ /(<Item.+? ID="([^"]+))/) {
         $s_ID = $2;
#         print "Item = $2\n";
         if ($_ =~ /(<Item.+? ItemHistory="([^"]+))/) {
            $s_itemhistory = $2;
            if (exists ($maphash{$s_itemhistory})) {
               $s_mapid = $s_itemhistory;
               $s_mapid .= "_";
               $s_mapid .= $s_statename;
               $s_mapid .= "_";
               $s_mapid .= $maphash{$s_itemhistory};
               if ($s_ID !~ /^$s_mapid$/) {
                  print "ItemID=$s_ID: No match found for ID($s_ID)\n";
               }
            }
         }
      }
      if ($_ =~ /(<Item.+? ItemHistory="([^"]+))/) {
         $s_itemhistory = $2;
         if (! exists ($maphash{$s_itemhistory})) {
            print "ItemID=$s_ID: No match found for ItemHistory($s_itemhistory)\n";
         }
      }
      if ($_ =~ /(<Item.+? DisplayID="([^"]+))/) {
         $s_displayID = $2;
         if (! exists ($maphash{$s_displayID})) {
            print "ItemID=$s_ID: No match found for DisplayID($s_displayID)\n";
         }
      }
      if ($_ =~ /(<Item.+? ObjectiveID="([^"]+))/) {
         $s_objectiveID = $2;
         if (exists ($maphash{$s_itemhistory})) {
            if ($s_objectiveID !~ /^$maphash{$s_itemhistory}$/) {
               print "ItemID=$s_ID: No match found for ObjectID($s_objectiveID)\n";
            }
         }
      }
      if ($_ =~ /^.+(<Hierarchy CurriculumID="([^"]+).+?<\/Hierarchy>)/s) {
         $s_curriculumID = $2;
         $_ =~ s/(^.+)(<Hierarchy CurriculumID="([^"]+).+?<\/Hierarchy>)/$1/s;
         if (exists ($objectivehash{$s_curriculumID})) {
            $s_rowkey = $objectivehash{$s_curriculumID}[3];
            if ($s_curriculumID !~ /^$objectivehash{$s_curriculumID}[1]$/) {
               print "ItemID=$s_ID: No match found for Hierarchy CurriculumID($s_curriculumID)\n";
            }
         } else {
            print "ItemID=$s_ID: No match found for Hierarchy CurriculumID($s_curriculumID)\n";
         }    
         while ($_ =~ /^.+(<Hierarchy CurriculumID="([^"]+).+?<\/Hierarchy>)/s) {
            $s_curriculumID = $2;
            $_ =~ s/(^.+)(<Hierarchy CurriculumID="([^"]+).+?<\/Hierarchy>)/$1/s;
            $f_found = 0;
            foreach $key (keys %objectivehash) {
               if ($s_rowkey =~ /^$objectivehash{$key}[2]$/) {
                  if ($s_curriculumID !~ /^$objectivehash{$key}[1]$/) {
                     print "ItemID=$s_ID: No match found for Hierarchy CurriculumID=$s_curriculumID\n";
                  }
                  $s_tmp = $objectivehash{$key}[3];
                  $f_found = 1;
                  break;
               }
            }
            $s_rowkey = $s_tmp;
            if (!$f_found) {
               print "ItemID=$s_ID: no match found for hierarchy($s_curriculumID)\n"; 
            }
         }
      }
   }
}
