#!/usr/bin/perl
#-----------------------------------------#
# Script: cleanRolesFiles.pl              #
# Language: Perl 5                        #
# Author: Cruz deWilde                    #
# Version: 1.4                            #
# Last Modified: 06/21/2004               #
#-----------------------------------------#------------------------------------#
#                                                                              #
# This script cycles through the TeamSite conf/roles files, checking that      #
# each listed user has an active UNIX account on the server.  Users without    #
# a UNIX account are removed from the roles files.                             #
#                                                                              #
# USAGE:                                                                       #
#                                                                              #
# cleanRolesFiles.pl [-d] [-s]                                                 #
#                                                                              #
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
    getopts('ds');
    ($SCRIPT_NAME, $SCRIPT_DIR, $SCRIPT_TYPE) = fileparse($0);

#------------------------------------------------------------------------------#
# With $DEBUG set to "1", the script will print output to the command line.    #
# With $DEBUG set to "0", the script will operate silently.                    #
#------------------------------------------------------------------------------#

    $ROLES = "author editor admin master";
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

 $SCRIPT_NAME [-d] [-s]

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

#------------------------------#
# function: validateUsername() #
#------------------------------#-----------------------------------------------#
# This function checks to see if a given username is available for use.  If    #
# username already exists in the /etc/passwd file, it returns 0.  If no such   #
# entry exists, it returns 1.                                                  #
#------------------------------------------------------------------------------#

sub validateUsername {

    my $username = $_[0];
    my $match = getpwnam($username);
    return (1) if (&isWhiteSpace($match));
    &debug("$username exists in /etc/passwd: uid $match\n");
    return(0);

}

#------------------------------------------------------------------------------#
# End of validateUsername() function.                                          #
#------------------------------------------------------------------------------#

#--------------------------------#
# function: cleanTeamSiteRoles() #
#--------------------------------#---------------------------------------------#
# For each role file, this function reads in the contents of the file,         #
# creating an array of usernames, sorts the array (removing duplicates), and   #
# prints the array back out to the originating file, setting the permissions   #
# appropriately on the way out.  This keeps the roles file clean and healthy.  #
#------------------------------------------------------------------------------#

sub cleanTeamSiteRoles {

    my $role = $_[0];

#------------------------------------------------------------------------------#
# Ensure a clean read of the roles list whether delimited by spaces or commas. #
#------------------------------------------------------------------------------#

    @roles = split(/\,\s*|\s+/,$role);

#------------------------------------------------------------------------------#
# Identify the TeamSite home directory in order to locate the roles files.     #
#------------------------------------------------------------------------------#

    my $iwhomeDir=`iwgethome`;
    if ($?) {
        &errorHandler("Failed to run iwgethome. Please check your path\n",0);
    }
    chomp($iwhomeDir);
    my $rolePath = "$iwhomeDir/conf/roles";
    &debug("Path to roles files: $rolePath\n");

#------------------------------------------------------------------------------#
# Cycle through the give roles files, adding the username to each.             #
#------------------------------------------------------------------------------#

    foreach my $role (@roles) {
        my $roleFile = "$rolePath/$role\.uid";
        my @userList = &readData($roleFile);
        my @newUserList = ();
        my @removedUsers = ();
        my $initialcount = scalar(@userList);
        foreach my $username (@userList) {

#------------------------------------------------------------------------------#
# Check to make sure that the given username has an entry in /etc/passwd.      #
#------------------------------------------------------------------------------#

            if (&validateUsername($username)) {
                &debug("$username not found in /etc/passwd file\n");
                push (@removedUsers, $username);
            } else {
                push (@newUserList, $username);
            }
        }
        @newUserList = &uniqSort(@newUserList);
        @removedUsers = &uniqSort(@removedUsers);
        my $removedCount = scalar(@removedUsers);
        foreach my $removedUser (@removedUsers) {
            &debug("Removed $removedUser from $roleFile\n");
        }
        &debug("$removedCount users removed from $roleFile\n");
        my $count = scalar(@newUserList);
        &printArray(@newUserList, $roleFile) unless ($SIMULATE);
        &debug("New file $roleFile is clean ($count names)\n");
    }

#------------------------------------------------------------------------------#
# Run iwreset to force TeamSite to reread the roles files.                     #
#------------------------------------------------------------------------------#

    my $iwresetCmd = "$iwhomeDir/bin/iwreset";
    open(IWRESET, "$iwresetCmd |") 
        || &errorHandler("Cannot execute $iwresetCmd : $!\n");
    while(<IWRESET>) {
        chomp($_);
        &debug("$_\n");
    }
    close(IWRESET);

    return(1);

}

#------------------------------------------------------------------------------#
# End of cleanTeamSiteRoles() function.                                        #
#------------------------------------------------------------------------------#

#----------------------------#
# function: readData(string) #
#----------------------------#-------------------------------------------------#
# This function takes a string specifying the full path to a file as its only  #
# parameter.  It then reads in the file, pushes the contents into local array  #
# (@recs) one line at a time, and returns the array to the calling function.   #
# This function depends on the errorHandler(), and isWhiteSpace() functions.   #
#------------------------------------------------------------------------------#

sub readData {

    my $inputFile = $_[0];
    &debug("readData() Reading input file $inputFile\n");
    my @recs = ();
    if (&isWhiteSpace("$inputFile")) {
        &errorHandler("Missing file name.\n",0);
        return(0);
    }
    open(READDATA, "$inputFile")
        || &errorHandler("Cannot read $inputFile: $!\n",0);
    while (<READDATA>) {
        chomp($_);
        push(@recs,$_) unless (&isWhiteSpace($_));
    }
    close(READDATA);
    &debug("Read of $inputFile completed successfully\n");
    return(@recs);
                
}

#------------------------------------------------------------------------------#
# End of readData() function.                                                  #
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

#------------------------#
# function: printArray() #
#------------------------#-----------------------------------------------------#
# This function takes an array of strings as its only parameter, the last      #
# element of which contains the name of the file to be printed.  The routine   #
# then prints the array, line by line, into the file.                          #
#------------------------------------------------------------------------------#

sub printArray {

    my @array = @_;
    my $fileName = pop(@array);
    &debug("Printing $fileName...\n");
    if ($fileName eq "STDOUT") {
        foreach my $elem (@array) {
               &debug("$elem\n");
        }
        return(1);
    }
    open(OUTPUT, "> $fileName")
        || &errorHandler("Cannot open $fileName for writing: $!\n");
    foreach my $elem (@array) {
        print OUTPUT "$elem\n";
    }
    close(OUTPUT);
    &debug("Printing of $fileName completed successfully...\n");

}

#------------------------------------------------------------------------------#
# End of printArray() function.                                                #
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
    &cleanTeamSiteRoles($ROLES);

}

#------------------------------------------------------------------------------#
# End of main() function.                                                      #
#------------------------------------------------------------------------------#

#------------------------------------------------------------------------------#
# End of cleanRolesFiles.pl                                                    #
#------------------------------------------------------------------------------#
