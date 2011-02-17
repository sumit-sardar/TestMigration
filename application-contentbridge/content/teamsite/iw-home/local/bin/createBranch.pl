#!/usr/bin/perl
#-----------------------------------------#
# Script: createBranch.pl                 #
# Language: Perl 5                        #
# Author: Cruz deWilde                    #
# Version: 1.3                            #
# Last Modified: 06/14/2004               #
#-----------------------------------------#------------------------------------#
#                                                                              #
# This script creates a new branch in TeamSite, complete with directory tree,  #
# and categories.                                                              #
#                                                                              #
# USAGE:                                                                       #
#                                                                              #
# createBranch.pl -b "<branch>" -c "<children>" -o "<user:group>" [-d] [-s]    #
#                                                                              #
#      Parameters (mandatory):                                                 #
#            -b      Branch (e.g. "PENNSYLVANIA")                              #
#            -c      Child Branches (e.g. "MATH, ELA")                         #
#            -o      Owner (e.g. "tsadmin:oasgrp")                             #
#      Boolean Flags (optional):                                               #
#            -d      Debug Mode (verbose output for debugging purposes)        #
#            -s      Simulate Mode (runs script without making changes)        #
#                                                                              #
# NOTE:  This script can only be run by root!                                  #
#                                                                              #
#------------------------------------------------------------------------------#

&main();
exit(1);

#---------------------------#
# function: defineGlobals() #
#---------------------------#--------------------------------------------------#
# Routine defines global variables used throughout the script.                 #
#------------------------------------------------------------------------------#

sub defineGlobals {

    use strict;
    no strict "vars";
    use File::Basename;
    use Getopt::Std;
    getopts('dsb:c:o:');

#------------------------------------------------------------------------------#
# Establish the branch, children, and workarea names.                          #
#------------------------------------------------------------------------------#

    ($SCRIPT_NAME, $SCRIPT_DIR, $SCRIPT_TYPE) = fileparse($0); 
    $DFN_DIR = "$SCRIPT_DIR"."branchTemplates";
    if (!(-e $DFN_DIR)) {
        &errorHandler("Could not locate template directory $DFN_DIR\n");
    }
    $BRANCH = uc($opt_b) || &usage("Please supply the primary branch name.");
    $CHILDREN = $opt_c || &usage("Please supply the child branch names.");
    $OWNER = $opt_o || "tsadmin:oasgrp";
        
    $IWMAIN = "/default/main";

#------------------------------------------------------------------------------#
# With $DEBUG set to "1", the script will print output to the command line.    #
# With $DEBUG set to "0", the script will operate silently.                    #
#------------------------------------------------------------------------------#

    $DEBUG = $opt_d || 0;
    $SIMULATE = $opt_s || 0;
    $ERROR_FILE = "$SCRIPT_NAME\_errors.log";

    $WORKAREA = "load";

#------------------------------------------------------------------------------#
# Populate the @DIRS array with all the directories that need to be created.   #
#------------------------------------------------------------------------------#

    @DIRS = ("Content", "Excel_Files", "images", "Job_Documents", "Upload",
                                                "TeamXML", "TeamXML/Rules");
    for($i=2;$i<=12;$i++) {
        push(@DIRS, "Content/Grade_$i");
    }

}

#------------------------------------------------------------------------------#
# End of defineGlobals() function.                                             #
#------------------------------------------------------------------------------#

#-------------------#
# function: usage() #
#-------------------#----------------------------------------------------------#
# This function displays the usage of the script to the user.                  #
#------------------------------------------------------------------------------#

sub usage {

    my $mesg = $_[0];
    if ($mesg) {
        print <<EOB;

 *** $mesg ***
EOB
    }
    print <<EOB;

 USAGE:

 $SCRIPT_NAME -b "<branch>" -c "<children>" -o "<user:group>" [-d] [-s]

      Parameters (mandatory):
            -b      Branch (e.g. "PENNSYLVANIA")
            -c      Child Branches (e.g. "MATH, ELA")
            -o      Owner (e.g. "tsadmin:oasgrp")
      Boolean Flags (optional):
            -d      Debug Mode (verbose output for debugging purposes)
            -s      Simulate Mode (runs script without making changes)

 NOTE:  This script can only be run by root!

EOB
        exit;
}

#------------------------------------------------------------------------------#
# End of usage() function.                                                     #
#------------------------------------------------------------------------------#

#-------------------#
# function: debug() #
#-------------------#----------------------------------------------------------#
# This function takes a string as its only parameter and prints it if the      #
# global variable $DEBUG is set to 1.                                          #
#------------------------------------------------------------------------------#

sub debug {

    my $line = ${ [caller(0)] }[2];
    my $subroutine = ${ [caller(1)] }[3];
    print "$subroutine($line): $_[0]" if ($DEBUG);

}

#------------------------------------------------------------------------------#
# End of debug() function.                                                     #
#------------------------------------------------------------------------------#

#------------------------#
# function: createDirs() #
#------------------------#-----------------------------------------------------#
# This function creates the necessary directory tree based on the parameters   #
# passed to it.                                                                #
#------------------------------------------------------------------------------#

sub createDirs {

    my $branch = shift(@_);
    my $children = shift(@_);
    my $wa = shift(@_);
    my $owner = shift(@_);
    my @dirs = @_;
    my @subBranches = split(/\,\s*|\s+/,$children);

    my $dirCount = scalar(@dirs);
    &debug("Creating $dirCount directories owned by $owner\n");
    return(0) if ($SIMULATE);
    foreach my $sb (@subBranches) {
        my $path = "$IWMAIN/$branch/$sb/WORKAREA/$wa";
        foreach my $elem (@dirs) {
            my $dir = "$path/$elem";
            if (-e $dir) {
                &errorHandler("directory $dir already exists!\n",0);
            } else {
                &debug("Creating directory $dir...\n");
            }
            mkdir($dir,0775)
                or &errorHandler("Failed to create directory $dir: $!\n",0);
            chmod(0775, ($dir))
                or &errorHandler("Failed to set perms 0775 on $dir: $!\n",0);
            my $chown = `chown $owner $dir`;
            if ($?) {
                &errorHandler("Failed to run chown $owner $dir\n",0);
            }
        }
    }

}

#------------------------------------------------------------------------------#
# End of createDirs() function.                                                #
#------------------------------------------------------------------------------#

#--------------------------#
# function: createBranch() #
#--------------------------#---------------------------------------------------#
# This function creates a branch according to the parameters specified.        #
#------------------------------------------------------------------------------#

sub createBranch {

    my $branch = $_[0];
    my $children = $_[1];
    my $wa = $_[2];
    my $owner = $_[3];
    my ($usr,$grp) = split(/:\s*/, $owner, 2);
    my @subBranches = split(/\,\s*|\s+/,$children);
    &debug("Creating branch...\n");
    &debug("Branch: $branch\n");
    &debug(" Owner: $usr\n"); 
    &debug(" Group: $grp\n"); 
    foreach my $elem (@subBranches) {
        &debug(" Child: $elem\n");
        &debug(" Workarea: $wa\n");
    }

#------------------------------------------------------------------------------#
# Check to make sure that the given branch doesn't already exist.              #
#------------------------------------------------------------------------------#

    if (-e "$IWMAIN/$branch") {
        &errorHandler("$branch already exists.  Please delete it first.\n",0);
    }

#------------------------------------------------------------------------------#
# Identify the TeamSite home directory in order to locate command-line tools.  #
#------------------------------------------------------------------------------#

    my $iwHome =`iwgethome`;
    if ($?) {
        &errorHandler("Failed to run iwgethome. Please check your path\n",0);
    }
    chomp($iwHome);
    &debug("Path to iw-home: $iwHome\n");
    $mkBr = "$iwHome/bin/iwmkbr //IWSERVER$IWMAIN";
    $mkWa = "$iwHome/bin/iwmkwa //IWSERVER$IWMAIN";


#------------------------------------------------------------------------------#
# Create the primary branch using the specified user and group.                #
#------------------------------------------------------------------------------#

    my $Cmd = "$mkBr $branch \'$branch branch\' INITIAL $usr $grp";
    &debug("$Cmd\n");

    return(1) if ($SIMULATE);

    open(BR, "$Cmd |") 
        || &errorHandler("Cannot execute $Cmd: $!\n",0);
    while(<BR>) {
        chomp($_);
        &debug("$_\n");
        if (m/(fail|error)/i) {
            &errorHandler("Failed to create branch $branch: $_\n",0);
        }
    }
    close(BR);
    &debug("Successfully created branch $branch\n");

#------------------------------------------------------------------------------#
# Create each child branch listed in the @subBranches array.                   #
#------------------------------------------------------------------------------#

    foreach my $sb (@subBranches) {
        my $brCmd="$mkBr/$branch $sb \'$branch $sb branch\' INITIAL $usr $grp";
        &debug("$brCmd\n");
        open(SB, "$brCmd |") 
            || &errorHandler("Cannot execute $brCmd: $!\n",0);
        while(<SB>) {
            chomp($_);
            &debug("$_\n");
            if (m/(fail|error)/i) {
                &errorHandler("Failed to create child branch $br: $_\n",0);
            }
        }
        close(SB);
        &debug("Successfully created child $branch/$sb\n");

#------------------------------------------------------------------------------#
# Create the workarea specified in $wa under each child branch.                #
#------------------------------------------------------------------------------#

        my $waCmd="$mkWa/$branch/$sb $wa \'$wa workarea\' INITIAL $usr $grp"; 
        &debug("$waCmd\n");
        open(WA, "$waCmd |") 
            || &errorHandler("Cannot execute $waCmd: $!\n",0);
        while(<WA>) {
            chomp($_);
            &debug("$_\n");
            if (m/(fail|error)/i) {
                &errorHandler("Failed to create workarea $wa: $_\n",0);
            }
        }
        close(WA);
        &debug("Successfully created workarea $branch/$sb/WORKAREA/$wa\n");
    }
    &debug("Branch $branch and Children $children successfully created.\n");
    return(1);

}

#------------------------------------------------------------------------------#
# End of createBranch() function.                                              #
#------------------------------------------------------------------------------#


#------------------------------#
# function: createCategories() #
#------------------------------#-----------------------------------------------#
# This function runs the iwxmlcat command against the desired definition files #
# under $IWMAIN/$BRANCH/$CHILD/WORKAREA/load/TeamXML/Rules in order to make    #
# categories available to users of X-Metal.                                    #
#------------------------------------------------------------------------------#

sub createCategories {

    my $branch = $_[0];
    my $children = $_[1];
    my $wa = $_[2];
    my $owner = $_[3];
    my @definitionFiles = ();
    @subBranches = split(/\,\s*|\s+/,$children);
    &debug("Creating categories under $branch for X-Metal/TeamXML...\n");

#------------------------------------------------------------------------------#
# Read the contents of the template file directory, and build @templateFiles.  #
#------------------------------------------------------------------------------#

    &debug("Reading directory contents of $DFN_DIR...\n");
    opendir(DIR,"$DFN_DIR")
        || &errorHandler("Cannot read $TEAMPLATE_DIR: $!\n",0);
    @templateFiles = grep { /\.dfn/ } readdir(DIR);
    @templateFiles = &uniqSort(@templateFiles);
    closedir(DIR);
    my $dfnCount = scalar (@templateFiles);
    &debug("Found $dfnCount template files in $DFN_DIR\n");

#------------------------------------------------------------------------------#
# Copy each of the template files into the appropriate directory under each    #
# child branch and push the destination file path into the @definitionFiles    #
# array for for category activation.                                           #
#------------------------------------------------------------------------------#

    use File::Copy;
    foreach my $child (@subBranches) {
        my $path = "$IWMAIN/$branch/$child/WORKAREA/$wa/TeamXML/Rules";
        &debug("Copying definition files from $DFN_DIR to $path...\n");
        foreach my $src (@templateFiles) {
            my $dest = "$path/$src";
            if (!($SIMULATE)) { 
                copy("$DFN_DIR/$src",$dest)
                || &errorHandler("Cannot copy $DFN_DIR/$src to $dest: $!\n",0);
                chmod(0775, ($dest))
                    or &errorHandler("Failed to chmod 0775 $dest: $!\n",0);
                my $chown = `chown $owner $dest`;
                if ($?) {
                    &errorHandler("Failed to run chown $owner $dest\n",0);
                }
            }
            push(@definitionFiles,$dest);
        }
    }
    @definitionFiles = &uniqSort(@definitionFiles);

#------------------------------------------------------------------------------#
# Identify the TeamSite home directory in order to locate command-line tools.  #
#------------------------------------------------------------------------------#

    my $iwHome =`iwgethome`;
    if ($?) {
        &errorHandler("Failed to run iwgethome. Please check your path\n",0);
    }
    chomp($iwHome);
    &debug("Path to iw-home: $iwHome\n");

    my $xmlCatCmd = "$iwHome/bin/iwxmlcat -c";
    &debug("iwxmlcat: $xmlCatCmd\n");

#------------------------------------------------------------------------------#
# Run iwxmlcat against each of the newly copied dfn files in @definitionFiles. #
#------------------------------------------------------------------------------#

    foreach my $dfn (@definitionFiles) {
        my $Cmd = "$xmlCatCmd $dfn";
        &debug("$Cmd\n");
        if (!($SIMULATE)) { 
            open(CAT, "$Cmd |")
                || &errorHandler("Cannot execute $Cmd: $!\n",0);
            while(<CAT>) {
                chomp($_);
                &debug("$_\n");
                if ($_ =~ m/(fail|error)/i) {
                    &errorHandler("$Cmd reported the following: $_\n",0);
                }
            }
            close(CAT);
        }
    }
    &debug("Finished creating categories under $branch\n");
    return(1);

}

#------------------------------------------------------------------------------#
# End of createCategories() function.                                          #
#------------------------------------------------------------------------------#

#----------------------#
# function: uniqSort() #
#----------------------#-------------------------------------------------------#
# This function takes an array, sorts it, removes duplicate elements, and      #
# returns the resulting array.                                                 #
#------------------------------------------------------------------------------#
 
sub uniqSort {

    my @array = sort(@_);
    my @uniqArray = ();
    my $i=0;
    foreach my $elem (@array) {
        if (($elem ne $array[$i-1]) || ($i == 0)) {
            push (@uniqArray,"$elem");
        }
        $i++;
    }
    return (@uniqArray);

}

#------------------------------------------------------------------------------#
# End of uniqSort function.                                                    #
#------------------------------------------------------------------------------#

#--------------------------#
# function: isWhiteSpace() #
#--------------------------#---------------------------------------------------#
# This simple function takes a single string as its only parameter, and        #
# determines if it is empty or made up solely of whitespace characters (e.g.,  #
# tabs, spaces, etc.).  If it is, the function returns 1, otherwise 0.         #
#------------------------------------------------------------------------------#

sub isWhiteSpace {

    my $str = $_[0];
    $str =~ s/\s+//g;
    if ($str eq "") {
        return(1);
    } else {
        return(0);
    }

}

#------------------------------------------------------------------------------#
# End of isWhiteSpace() function.                                              #
#------------------------------------------------------------------------------#

#--------------------------#
# errorHandler() function  #
#--------------------------#---------------------------------------------------#
# This function appends the error message contained in the $message parameter  #
# to the file specified in the $ERROR_FILE parameter.  If no file is specified #
# the error file will be named <script>_errors.txt.  If the $keepRunning       #
# parameter is set to 0, the script will exit immediately after the error      #
# message is printed the $ERROR_FILE and STDERR.                               #
#------------------------------------------------------------------------------#

sub errorHandler {

    my $message = $_[0];
    my $keepRunning = $_[1];
    my $line = ${ [caller(0)] }[2];
    my $subroutine = ${ [caller(1)] }[3];
    my $timestamp = &getTimeStamp();
    if (&isWhiteSpace($ERROR_FILE)) {
        ($ERROR_FILE = $0) =~ s/\.pl/_errors\.txt/;
    }
    open(ERROR, ">> $ERROR_FILE") or warn "Cannot open $ERROR_FILE: $!\n";
    print ERROR "$timestamp - $subroutine($line): $message";
    close(ERROR) or warn "Error file did not close properly.";
    print STDERR "$timestamp: $subroutine($line) - $message";
    if ($keepRunning == 0) {
        print STDERR "Exiting $0 due to the abovementioned error...\n";
        exit(0);
    }

}

#------------------------------------------------------------------------------#
# End of errorHandler() function.                                              #
#------------------------------------------------------------------------------#

#--------------------------#
# function: getTimestamp() #
#--------------------------#---------------------------------------------------#
# This function gets the current date and time, formats it as                  #
#                      mm/dd/yyyy - hh:mm:ss                                   #
# and returns it to the calling function.                                      #
#------------------------------------------------------------------------------#

sub getTimeStamp {

    my ($sec,$min,$hr,$day,$mon,$year,$wday,$yday,$isdst)=localtime(time);
    $mon = sprintf("%02d",($mon + 1));
    $day = sprintf("%02d",$day);
    $hr = sprintf("%02d",$hr);
    $min = sprintf("%02d",$min);
    $sec = sprintf("%02d",$sec);
    my $ddyear = substr($year,1);
    my $modYear = 1900 + $year;
    my $timestamp = "$mon/$day/$modYear - $hr:$min:$sec";
    return($timestamp);

}

#------------------------------------------------------------------------------#
# End of getTimeStamp() function.                                              #
#------------------------------------------------------------------------------#

#------------------#
# function: main() #
#------------------#-----------------------------------------------------------#
# Main routine of the program.  Please see subroutine comments for specific    #
# information.                                                                 #
#------------------------------------------------------------------------------#

sub main {

    &defineGlobals();
    &createBranch($BRANCH, $CHILDREN, $WORKAREA, $OWNER);
    &createDirs($BRANCH, $CHILDREN, $WORKAREA, $OWNER,@DIRS);
    &createCategories($BRANCH, $CHILDREN, $WORKAREA, $OWNER);

}

#------------------------------------------------------------------------------#
# End of main() function.                                                      #
#------------------------------------------------------------------------------#

#------------------------------------------------------------------------------#
# End of createBranch.pl                                                       #
#------------------------------------------------------------------------------#
