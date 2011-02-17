#!/usr/bin/perl
#-----------------------------------------#
# Script: backup.pl                       #
# Language: Perl 5                        #
# Author: Cruz deWilde                    #
# Version: 1.2                            #
# Last Modified: 06/14/2004               #
#-----------------------------------------#------------------------------------#
#                                                                              #
# This script creates a usable archive of TeamSite's backing store which can   #
# then be moved off to tape.  First, the script shuts down TeamSite completely #
# in order to protect against file corruption.  Then, the script creates a tar #
# file of the backing store, compressing it as much as possible.  Last, the    #
# script brings TeamSite back up, checking for errors.                         #
#                                                                              #
# USAGE:                                                                       #
#                                                                              #
# backup.pl -a <archive> [-d] [-s]                                             #
#                                                                              #
#      Parameters (mandatory):                                                 #
#            -a      Archive (e.g. /export/interwoven/backups/backup.tgz)      #
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
    use Getopt::Std;
    getopts('dsa:');

#------------------------------------------------------------------------------#
# With $DEBUG set to "1", the script will print output to the command line.    #
# With $DEBUG set to "0", the script will operate silently.                    #
#------------------------------------------------------------------------------#

    $ARCHIVE = $opt_a || &usage("Please supply the archive file path");
    $DEBUG = $opt_d || 0;
    $SIMULATE = $opt_s || 0;
    $ERROR_FILE = "$0\_errors.log";

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

 backup.pl -a <archive> [-d] [-s]

      Parameters (mandatory):
            -a      Archive (e.g. /export/interwoven/backups/backup.tgz)
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

#---------------------------#
# function: createArchive() #
#---------------------------#--------------------------------------------------#
# This function adds a given username to the specified roles file(s).  For     #
# each role specified, this function reads in the contents of the file,        #
# creating an array of usernames, sorts the array (removing duplicates), and   #
# prints the array back out to the originating file, setting the permissions   #
# appropriately on the way out.  This keeps the roles file clean and healthy.  #
#------------------------------------------------------------------------------#

sub createArchive {

    my $archive = $_[0];
    my $role = $_[1];

#------------------------------------------------------------------------------#
# Check to make sure that the given archive has a valid name.                  #
#------------------------------------------------------------------------------#

    if ( !($archive =~ m/\.tar\.gz\Z|\.tgz\Z/) ) {
        &errorHandler("Archive name ($archive) must end in \'.tgz\'\n",0);
    }

#------------------------------------------------------------------------------#
# Check to make sure that the given archive doesn't still exist.               #
#------------------------------------------------------------------------------#

    if (-e $archive) {
        &debug("$archive already exists.  Attempting to overwrite it...\n");
    }

#------------------------------------------------------------------------------#
# Identify the TeamSite home directory in order to locate the roles files.     #
#------------------------------------------------------------------------------#

    my $iwStore=`iwgetlocation iwstore`;
    if ($?) {
        &errorHandler("Failed to find the TeamSite Backing Store.\n",0);
    }
    chomp($iwStore);

    &debug("Path to backing store: $iwStore\n");
    my ($iwBase, $backingStore) = &getParentDir($iwStore);
    &debug("Changing working directory to $iwBase\n");
    chdir($iwBase);

#------------------------------------------------------------------------------#
# Stop all TeamSite services to guarantee a clean backup.                      #
#------------------------------------------------------------------------------#

    &manageTeamSite("stop_all");

    my $tarCmd = "/usr/local/bin/tar czf $archive $backingStore";
    &debug("$tarCmd\n");
    if (!($SIMULATE)) {
        open(TAR, "$tarCmd |") 
            || &errorHandler("Cannot execute $tarCmd : $!\n");
        while(<TAR>) {
            chomp($_);
            &debug("$_\n");
        }
        close(TAR);
    }

    &manageTeamSite("start");

    return(1);

}

#------------------------------------------------------------------------------#
# End of createArchive() function.                                             #
#------------------------------------------------------------------------------#

#--------------------------#
# function: getParentDir() #
#--------------------------#---------------------------------------------------#
# This simple function takes a single string containing the full path to a     #
# file as its only parameter, and returns a string containing the path to the  #
# parent directory and a string containing the name of the file.               #
#------------------------------------------------------------------------------#

sub getParentDir {

    my $filePath = $_[0];
    my @pathDirs = ();
    my $fileName = "";
    my $pDir = "";
    my $sep = "\\\\";
    my $join = "\\";
    if ((index($filePath,"/") != -1)) {
        $sep = "/";
        $join = "/";
    }
    @pathDirs = split(/$sep/,$filePath);
    $fileName = pop(@pathDirs);
    $pDir = join("$join",@pathDirs);
    return($pDir,$fileName);

}

#------------------------------------------------------------------------------#
# End of getParentDir() function.                                              #
#------------------------------------------------------------------------------#


#----------------------------#
# function: manageTeamSite() #
#----------------------------#-------------------------------------------------#
# This function starts and stops TeamSite services.  The $cmd paramter must be #
# one of the following:                                                        #
# start                                                                        #
# stop                                                                         #
# stop_all                                                                     #
#------------------------------------------------------------------------------#

sub manageTeamSite {

    my $cmd = $_[0];
    if (($cmd ne "start") && ($cmd ne "stop") && ($cmd ne "stop_all")) {
        &errorHandler("Invalid parameter $cmd\n",0);
    }

#------------------------------------------------------------------------------#
# Run iw.server (start, stop, or stop_all)                                     #
#------------------------------------------------------------------------------#

    my $iwserverCmd = "/etc/init.d/iw.server $cmd";
    &debug("$iwserverCmd\n");
    return(1) if ($SIMULATE);
    open(IWMANAGE, "$iwserverCmd |")
        || &errorHandler("Cannot execute $iwserverCmd: $!\n");
    while(<IWMANAGE>) {
        chomp($_);
        &debug("$_\n");
        if ($_ =~ m/fail/i) {
            &errorHandler("$iwserverCmd reported the following: $_\n",0);
        }
    }
    close(IWMANAGE);
    return(1);

}

#------------------------------------------------------------------------------#
# End of manageTeamSite() function.                                            #
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
    if (&createArchive($ARCHIVE)) {
        &debug("Successfully created backup $ARCHIVE\n");
    }

}

#------------------------------------------------------------------------------#
# End of main() function.                                                      #
#------------------------------------------------------------------------------#

#------------------------------------------------------------------------------#
# End of backup.pl                                                             #
#------------------------------------------------------------------------------#
