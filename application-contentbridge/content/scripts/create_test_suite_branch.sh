#!/usr/bin/bash

function makedir {
	if [ -d $1 ]; then
		echo "$1 exists";
	else
		echo "Creating directory $1";
		mkdir $1;
	fi
}


###########################
#
# Get the home directory for TeamSite
#
###########################

export iw_home=`iwgethome`

########################
#
# Create Initial Branching Structure
#
########################


###########################################################
#
# TEST_SUITE Branches and Workareas
#
###########################################################

$iw_home/bin/iwmkbr //IWSERVER/default/main TEST_SUITE 'TEST_SUITE branch' INITIAL tsadmin oasgrp
$iw_home/bin/iwmkwa //IWSERVER/default/main/TEST_SUITE load 'load workarea' INITIAL tsadmin oasgrp

makedir /default/main/TEST_SUITE/WORKAREA/load/Content
makedir /default/main/TEST_SUITE/WORKAREA/load/images
makedir /default/main/TEST_SUITE/WORKAREA/load/Job_Documents
makedir /default/main/TEST_SUITE/WORKAREA/load/Upload
makedir /default/main/TEST_SUITE/WORKAREA/load/Excel_Files
makedir /default/main/TEST_SUITE/WORKAREA/load/TeamXML
makedir /default/main/TEST_SUITE/WORKAREA/load/TeamXML/Rules
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_2
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_3
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_4
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_5
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_6
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_7
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_8
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_9
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_10
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_11
makedir /default/main/TEST_SUITE/WORKAREA/load/Content/Grade_12

src_base_dir=~iwuser/dev/interfaces/testdata/functional
src_xml_dir=$src_base_dir
src_img_dir=$src_base_dir/images
tgt_base_dir="/default/main/TEST_SUITE/WORKAREA/load"
tgt_xml_dir=$tgt_base_dir/Job_Documents
tgt_img_dir=$tgt_base_dir/images

# Get latest functional data from cvs
cd $src_base_dir
cvs update -RPd

#Copy xml files from $src_base_dir into $tgt_base_dir/Job_Documents directory
for i in `find . -name "*.xml" | grep -v CVS`; do
        if [ -f $i ]; then
                echo "Copying $i"
                cp $i $tgt_xml_dir/$i;
        fi
done

# Copy images from $src_img_dir to $tgt_img_dir
cd $src_img_dir
for i in `find . -name "*" | grep -v CVS`; do
        if [ -d $i ]; then
                export tgt_dir="$tgt_img_dir/$i";
                makedir $tgt_dir;
        else
                echo "Copying $i"
                cp $i $tgt_img_dir/$i;
        fi
done

cd -
