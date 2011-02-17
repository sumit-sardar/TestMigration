#!/bin/perl
#-----------------------------------------#
# Script: userReport.pl                   #
# Language: Perl 5                        #
# Author: Cruz deWilde                    #
# Version: 1.1                            #
# Last Modified: 06/24/2004               #
#-----------------------------------------#------------------------------------#
#                                                                              #
# This script reports all the user details related to a given username, group  #
# members, TeamSite role members, or all users.  This script makes no file     #
# modifications -- it is only a reporting tool.  Selecting more than one       #
# reporting criteria will return the union of their result sets.               #
#                                                                              #
# USAGE:                                                                       #
#                                                                              #
# userReport.pl [-u<username>] [-g<group>] [-r<role>] [-o<output>] [-a] [-d]   #
#                                                                              #
#      Parameters (optional):                                                  #
#            -u    User(s) ("<user 1>, <user 2>,<user 3>")                     #
#            -g    Group(s) ("<group 1>, <group 2>, <group 3>, etc.")          #
#            -r    Role(s) ("author editor admin master")                      #
#            -o    Output <file> or <email address> (default: host_users.xml)  #
#      Boolean Flags (optional):                                               #
#            -a    All users in /etc/passwd (overrides parameters above)       #
#            -d    Debug Mode (verbose output for debugging purposes)          #
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
    getopts('dau:g:r:o:');

#------------------------------------------------------------------------------#
# Set the email address of the account administrator.                          #
#------------------------------------------------------------------------------#

    $ADMIN_EMAIL = "Cruz deWilde <cruz_dewilde\@ctb.com>";
    ($SCRIPT_NAME, $SCRIPT_DIR, $SCRIPT_TYPE) = fileparse($0); 

#------------------------------------------------------------------------------#
# With $DEBUG set to "1", the script will print verbose output.                #
# With $DEBUG set to "0", the script will operate quietly.                     #
#------------------------------------------------------------------------------#

    $DEBUG = $opt_d || 0;

    undef %SEARCH;

    if (&isWhiteSpace($opt_a) && &isWhiteSpace($opt_u) 
        && &isWhiteSpace($opt_g) && &isWhiteSpace($opt_r)) {
        &usage("Please provide at least one of the following report criteria:");
    } elsif ($opt_a) {
        $SEARCH{'ALL'} = "YES";
        &debug("Report on All Users\n");
    } else {
        if ($opt_u) {
            $SEARCH{'USERS'} = [ split(/\,\s*|\s+/,$opt_u) ];
            &debug("Username(s) (" . @{$SEARCH{'USERS'}} . "): $opt_u\n");
        }
        if ($opt_g) {
            $SEARCH{'GROUPS'} = [ split(/\,\s*|\s+/,$opt_g) ];
            &debug("Group(s) (" . @{$SEARCH{'GROUPS'}} . "): $opt_g\n");
        }
        if ($opt_r) {
            $SEARCH{'ROLES'} = [ split(/\,\s*|\s+/,$opt_r) ];
            &debug("TeamSite Role(s) (" . @{$SEARCH{'ROLES'}} . "): $opt_r\n");
        }
    }

#------------------------------------------------------------------------------#
# Determine which TeamSite server we are running on, and set variables         #
# accordingly.                                                                 #
#------------------------------------------------------------------------------#

    $HOSTNAME = `/bin/hostname`;
    if ($?) {
        &errorHandler("Failed to run hostname: $! $?\n",0);
    }
    chomp($HOSTNAME);
    &debug("Hostname is $HOSTNAME\n");
    $OUTPUT = $opt_o || "$HOSTNAME\_users.xml";
    $ERROR_FILE = "$SCRIPT_NAME\_errors.log";

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

 This script reports all the user details related to a given username, group
 members, TeamSite role members, or all users.  This script makes no file
 modifications -- it is only a reporting tool.  Selecting more than one
 reporting criteria will return the union of their result sets.

 *** $mesg ***
EOB
    }
    print <<EOB;

 USAGE:

 $SCRIPT_NAME [-u <username>] [-g <groups>] [-r <roles>] [-o <output>] [-a] [-d]

      Parameters (optional):
            -u    User(s) ("<user 1>, <user 2>,<user 3>")
            -g    Group(s) ("<group 1>, <group 2>, <group 3>, etc.")
            -r    Role(s) ("author editor admin master")
            -o    Output <file> or <email address> (default: host_users.xml)
      Boolean Flags (optional):
            -a    All users in /etc/passwd (overrides parameters above)
            -d    Debug Mode (verbose output for debugging purposes)

EOB
        exit;
}

#------------------------------------------------------------------------------#
# End of usage() function.                                                     #
#------------------------------------------------------------------------------#

#----------------------------#
# function: createUserList() #
#----------------------------#-------------------------------------------------#
# Routine creates and validates a list of all the shells specified within the  #
# /etc/passwd file.                                                            #
#------------------------------------------------------------------------------#

sub createShellList () {

    undef %shells;

    &debug("Building \%shells list...\n");
    my @passwdFile = @_;
    foreach my $entry (@passwdFile) {
        my ($name,$enc,$uid,$gid,$comment,$dir,$sh) = split(/:/,$entry);
        $shells{$sh} = 1;
    }
    foreach my $shell (sort keys %shells) {
        if ((-e $shell) && (-s $shell)) {
            my $type = `/bin/file $shell`;     
            &debug("$type");
        } elsif (-z $shell) {
            ($dev,$ino,$mode,$nlink,$uid,$gid,$rdev,$size,
                $atime,$mtime,$ctime,$blksize,$blocks) = stat($shell);
            &errorHandler("Shell $shell is an empty file, last modified on " .
                localtime($mtime) . "\n",1);
            $shells{$sh} = 0;
        } else {
            &debug("Shell $shell does not exist\n");
            $shells{$sh} = 0;
        }
    }
    return(%shells);

}

#------------------------------------------------------------------------------#
# End of createShellList() function.                                           #
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

#----------------------------#
# function: createUserList() #
#----------------------------#-------------------------------------------------#
# Routine creates and validates a list of unique user ids based on the         #
# parameters passed to the script.                                             #
#------------------------------------------------------------------------------#

sub createUserList () {

    undef %users;

    my @passwdFile = @_;

#------------------------------------------------------------------------------#
# If the script received a list of usernames as a parameter (-u option), cycle #
# through the list, validating each username, and add each to the %users hash. #
#------------------------------------------------------------------------------#

    if (exists($SEARCH{'USERS'})) {
        &debug("Adding specified usernames to \%users hash...\n");
        foreach my $user (@ {$SEARCH{'USERS'}} ) {
            if (!(&validateUsername($user))) {
                $users{$user}{'username'} = $user;
                &debug("Adding specified user $user\n");
            } else {
                &errorHandler("$user not found in /etc/passwd. Skipping\n",1);
            }
        }
    }

#------------------------------------------------------------------------------#
# If the script received a list of groups as a parameter (-g option), cycle    #
# through the list, validating each group name, and add the members of each to #
# the %users hash.                                                             #
#------------------------------------------------------------------------------#

    use User::grent;
    if (exists($SEARCH{'GROUPS'})) {
        foreach my $group (@ {$SEARCH{'GROUPS'}} ) {

#------------------------------------------------------------------------------#
# Make sure that the specified group name is valid, and set the group object.  #
#------------------------------------------------------------------------------#

            my $gr = getgr($group) 
                || &errorHandler("Group $group not found. Skipping\n",1);
            next unless ($gr);

#------------------------------------------------------------------------------#
# Obtain the GID for the specified group name.                                 #
#------------------------------------------------------------------------------#

            my $gid = $gr->gid();
            &debug("Adding members of $group ($gid) to \%users hash...\n");

#------------------------------------------------------------------------------#
# First, scan the passwd file and collect all users for whom the sepcified     #
# group is the primary group for the account.                                  #
#------------------------------------------------------------------------------#

            foreach my $entry (@passwdFile) {
                my ($user,$p,$i,$g,$j) = split(/:/,$entry,5);
                if (($g == $gid) && (!(exists($users{$user})))) {
                    $users{$user}{'username'} = $user;
                    &debug("Adding primary $group member $user\n");
                } elsif (($g == $gid) && (exists($users{$member}))) {
                    &debug("Primary $group member $user already added\n");
                }
            }

#------------------------------------------------------------------------------#
# Now, use the $gr object to identify additional users whose membership in     #
# this group is established in the group file (not in passwd).                 #
#------------------------------------------------------------------------------#

            foreach my $user (@ {$gr->members} ) {
                if (&validateUsername($user)) {
                    &errorHandler("$group member $user not in /etc/passwd\n",1);
                    next;
                }
                if (!(exists($users{$user}))) {
                    $users{$user}{'username'} = $user;
                    &debug("Adding secondary $group member $user\n");
                } else {
                    &debug("Secondary $group member $user already added\n");
                }
            }
        }
    }

#------------------------------------------------------------------------------#
# If the script received a list of roles as a parameter (-r option), cycle     #
# through the list, validating each role name, and add the members of each to  #
# the %users hash.                                                             #
#------------------------------------------------------------------------------#

    if (exists($SEARCH{'ROLES'})) {

#------------------------------------------------------------------------------#
# Identify the TeamSite home directory in order to locate the roles files.     #
#------------------------------------------------------------------------------#

        my $iwhome = `/bin/iwgethome`;
        if ($?) {
            &errorHandler("Cannot run iwgethome. Please check your path\n",0);
        }
        chomp($iwhome);
        my $rolePath = "$iwhome/conf/roles";
        &debug("Path to roles files: $rolePath\n");

#------------------------------------------------------------------------------#
# Cycle through the given roles files, adding the username to each.            #
#------------------------------------------------------------------------------#

        foreach my $role (@ {$SEARCH{'ROLES'}} ) {
            &debug("Adding TeamSite $role to \%users hash...\n");
            my $roleFile = "$rolePath/$role\.uid";
            my @roleMembers = &readData($roleFile);
            foreach my $user (sort @roleMembers) {
                if (!(&validateUsername($user))) {
                    if (!(exists($users{$user}))) {
                        $users{$user}{'username'} = $user;
                        &debug("Adding TeamSite $role member $user\n");
                    } else {
                        &debug("TeamSite $role member $user already added\n");
                    }
                } else {
                    &errorHandler("Invalid user $user found in $roleFile.\n",1);
                }
            }
        }
    }

#------------------------------------------------------------------------------#
# If the script received the -a option (all users), simply cycle through the   #
# @passwdFile array and add each member to the %users hash.                    #
#------------------------------------------------------------------------------#

    if (exists($SEARCH{'ALL'})) {
        foreach my $entry (sort @passwdFile) {
            my ($user,$p,$i,$g,$j) = split(/:/,$entry,5);
            $users{$user}{'username'} = $user;
            &debug("Adding $user\n");
        }
    }

#------------------------------------------------------------------------------#
# Return the %users hash to the caller.                                        #
#------------------------------------------------------------------------------#

    return(%users);

}

#------------------------------------------------------------------------------#
# End of createUserList() function.                                            #
#------------------------------------------------------------------------------#

#----------------------------#
# function: getUserDetails() #
#----------------------------#-------------------------------------------------#
# Routine takes a list of validated usernames and collects a number of details #
# for each, passing back a hash containing the complete dataset to be included #
# in the report.                                                               #
#------------------------------------------------------------------------------#

sub getUserDetails () {

    my @userList = @_;
    undef %users;
    &debug("Gathering user details\n");
    use User::grent;
    use File::stat;

#------------------------------------------------------------------------------#
# Identify the TeamSite home directory in order to locate the roles files.     #
#------------------------------------------------------------------------------#

    my $iwhome = `/bin/iwgethome`;
    if ($?) {
        &errorHandler("Cannot run iwgethome. Please check your path\n",0);
    }
    chomp($iwhome);
    my @roles = ("author", "editor", "admin", "master");
    foreach my $role (@roles) {
        my $roleFile = "$iwhome/conf/roles/$role\.uid";
        &debug("Reading $roleFile\n");

#------------------------------------------------------------------------------#
# Create a hash of the role file contents for easy and inexpensive lookup.     #
#------------------------------------------------------------------------------#

        undef %$role;
        for (&readData($roleFile)) { $$role{$_} = 1 }
    }


    foreach my $user (@userList) {
        my ($name,$enc,$uid,$gid,$j,$q,$comment,$dir,$shell) = getpwnam($user);
        $users{$user}{'username'} = $name;
        if (&isWhiteSpace($name)) { &debug("No username found for $user\n") }
        #$users{$user}{'passwd'} = $enc
        #if (&isWhiteSpace($enc)) { &debug("No passwd set for $user\n") }
        $users{$user}{'uid'} = $uid;
        if (&isWhiteSpace($uid)) { &debug("No uid found for $user\n") }
        $users{$user}{'groupIDPrimary'} = $gid;
        if (&isWhiteSpace($uid)) { &debug("No primary group set for $user\n") }
        $users{$user}{'realName'} = $comment;
        if (&isWhiteSpace($uid)) { &debug("No real name set for $user\n") }
        $users{$user}{'homedir'} = $dir;
        if (&isWhiteSpace($uid)) { &debug("No home directory set for $user\n") }

#------------------------------------------------------------------------------#
# Ensure that the user's home directory exists.                                #
#------------------------------------------------------------------------------#

        my $dirInfo = stat($dir."/.");
        if (defined $dirInfo) {
            $users{$user}{'homedirExists'} = "Exists";
            if ($dirInfo->uid != $uid) {
                $users{$user}{'homedirOwnerOK'} = "NO";
                &debug("$user\'s homedir ($dir) not owned by correct " .
                    " uid: (" . $dirInfo->uid . " rather than $uid)\n");
            } else {
                $users{$user}{'homedirOwnerOK'} = "OK";
            }
            if ($dir ne "/") {
                my $du = `/bin/du -dks $dir`;
                chomp($du);
                my ($size,$dname) = split(/\s+/,$du);
                $users{$user}{'homedirSize'} = "$size Kb";
            } else {
                $users{$user}{'homedirSize'} = "N/A";
            }
            $users{$user}{'homedirOwner'} = $dirInfo->uid;
            $users{$user}{'homedirLastMod'} = localtime($dirInfo->ctime);
            &debug("$dir last accessed $users{$user}{'homedirLastMod'}\n");
            if ($dirInfo->mode & 022 and (!$dirInfo->mode & 01000)) {
                $users{$user}{'homedirPermsOK'} = "NO";
                $users{$user}{'homedirPerms'} = $dirInfo->mode;
                &errorHandler("$user\'s homedir ($dir) has wrong permissions " .
                    " uid: (" . $dirInfo->uid . " rather than $uid)\n",1);
            } else {
                $users{$user}{'homedirPermsOK'} = "OK";
            }
            $users{$user}{'homedirPerms'} = $dirInfo->mode;
        } else {
            $users{$user}{'homedirExists'} = "Missing";
            $users{$user}{'homedirSize'} = "N/A";
            $users{$user}{'homedirPermsOK'} = "N/A";
            $users{$user}{'homedirOwner'} = "N/A";
            $users{$user}{'homedirOwnerOK'} = "N/A";
            $users{$user}{'homedirPerms'} = "N/A";
            $users{$user}{'homedirLastMod'} = "N/A";
            &debug("Invalid home dir ($dir) specified for $user\n");
        }

#------------------------------------------------------------------------------#
# Obtain the list of Unix groups of which this user is a member.               #
#------------------------------------------------------------------------------#

        my $unixGroups = `/bin/groups $user`;
        if ($?) {
            &errorHandler("Cannot run /bin/groups: $! $?\n",0);
        }
        chomp($unixGroups);
        $users{$user}{'groupsAll'} = $unixGroups;

#------------------------------------------------------------------------------#
# Obtain the group name for the specified gid.                                 #
#------------------------------------------------------------------------------#

        my $gr = getgr($gid)
            || &errorHandler("User $user found with invalid gid $gid\n",1);
        my $gname = $gr->name;
        $users{$user}{'groupPrimary'} = $gname;

#------------------------------------------------------------------------------#
# Ensure that the user's shell exists.                                         #
#------------------------------------------------------------------------------#

        $users{$user}{'shell'} = $shell;
        if (&isWhiteSpace($shell)) { &debug("No shell set for $user\n") }

        if (!((-e $shell) && (-s $shell))) {
            &debug("Invalid shell ($shell) specified for $user\n");
        }

#------------------------------------------------------------------------------#
# Cycle through each of the roles specified in @roles and find out if this     #
# user is a member. Also, build a list of the roles to which the user belongs. #
#------------------------------------------------------------------------------#

        $users{$user}{'iw-roles'} = "";
        foreach my $role (@roles) {
            if ($$role{$user}) {
                if (!(&isWhiteSpace($users{$user}{'iw-roles'}))) {
                    $users{$user}{'iw-roles'}="$users{$user}{'iw-roles'},$role";
                } else {
                    $users{$user}{'iw-roles'} = "$role";
                }
                $users{$user}{"iw\-$role"} = "YES";
            } else {
                $users{$user}{"iw\-$role"} = "NO";
            }
        }
        &debug("User: $user   Role(s): $users{$user}{'iw-roles'}\n");
    }
    return(%users);

}

#------------------------------------------------------------------------------#
# End of getUserDetails() function.                                            #
#------------------------------------------------------------------------------#

#----------------------------#
# function: generateReport() #
#----------------------------#-------------------------------------------------#
# This function takes a hash containing all the user data collected during the #
# script and prints the contents out as an XML report.  If the -o (output)     #
# argument specified an email address, the xml report will be sent to that     #
# address in the body of the message; if it specified a filename, the report   #
# will be printed out to the named file, silently overwriting, if necessary.   #
#------------------------------------------------------------------------------#

sub generateReport {
    
    my %users = @_;
    my $timeStamp = &getTimeStamp();

#------------------------------------------------------------------------------#
# First, initialize the $report variable with the XML header and schema.       # 
#------------------------------------------------------------------------------#

    my $xmlReport =<<EOB;
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<userReport 
    date = "$timeStamp"
    host = "$HOSTNAME"
    searchAll = "$SEARCH{'ALL'}"
    searchUser = "$opt_u"
    searchGroups = "$opt_g"
    searchRoles = "$opt_r"
>
EOB

#------------------------------------------------------------------------------#
# Next, loop through each username in %users, adding its tagged data to the    #
# $report variable. I chose the nested loop within the %users hash to generate #
# the xml report rather than using XML::Simple or XML::Writer for the simple   #
# reason that I wanted to keep the values sorted in case the resulting xml     #
# gets read into a spreasheet program such as Excel.                           #
# To use the XML::Simple module, replace the following loop with:              #
#     use XML::Simple;                                                         #
#    foreach my $key (sort keys %users) {                                      #
#        $xmlReport = $xmlReport . XMLout($users{$key},noattr=>1);             #
#    }                                                                         #
#------------------------------------------------------------------------------#

    foreach my $key (sort keys %users) {
        $xmlReport = "$xmlReport<user id=\"$key\">\n";
        foreach my $val (reverse sort keys %{ $users{$key} }) {
            $xmlReport = "$xmlReport  \<$val>$users{$key}{$val}<\/$val>\n";
        }
        $xmlReport = "$xmlReport</user>\n";
    }
    $xmlReport = "$xmlReport</userReport>\n";
    &debug("Report will output to $OUTPUT\n");

#------------------------------------------------------------------------------#
# If the paramter $OUTPUT is an email address, generate a formatted email      #
# message to be passed to the sendMail() function.                             #
#------------------------------------------------------------------------------#

    if (&isEmailAddress($OUTPUT)) {
        my $mailMessage =<<EOB;
From: $ADMIN_EMAIL
To: $OUTPUT
Bcc: $ADMIN_EMAIL
Subject: User report from $HOSTNAME

$xmlReport
EOB
        &debug($mailMessage);

#------------------------------------------------------------------------------#
# Pass the formatteed $mailMessage to the sendMail() function.                 #
#------------------------------------------------------------------------------#

        my $sent = &sendMail($mailMessage);
        &debug("sendMail returned: $sent\n");

#------------------------------------------------------------------------------#
# If the paramter $OUTPUT is a filename, print the contents of $xmlReport out  #
# to the specified file.                                                       #
#------------------------------------------------------------------------------#


    } else {
        &printArray($xmlReport,$OUTPUT);
    }

}

#------------------------------------------------------------------------------#
# End of generateReport() function.                                            #
#------------------------------------------------------------------------------#

#----------------------#
# function: sendMail() #
#----------------------#-------------------------------------------------------#
# Function sends a pre-formatted mail message with body $mailMessage.          #
# At a minimum, the mail message must begin with the following:                #
#                                                                              #
# From: Sender Name <sender\@mcgraw-hill.com>                                  #
# To: Recipient Name <recipient\@mcgraw-hill.com>                              #
# Subject: New TeamSite Account                                                #
#                                                                              #
#------------------------------------------------------------------------------#

sub sendMail {

    my $mailMessage = $_[0];
    my $mailer = "/usr/lib/sendmail -t -odb";
    &debug("mailer: $mailer\n");

#------------------------------------------------------------------------------#
# Open a handle for the email notification where the data is to be written.    #
#------------------------------------------------------------------------------#

    open(MAILER, "| $mailer")
        || &errorHandler("Cannot open $mailer: $!\n",0);
    print MAILER "$mailMessage\n";

#------------------------------------------------------------------------------#
# Close the MAILER filehandle to ensure the data is sent.                      #
#------------------------------------------------------------------------------#

    close(MAILER)
        || &errorHandler("Sendmail did not close properly.\n",1);
    return(1);

}

#------------------------------------------------------------------------------#
# End of sendMail() function.                                                  #
#------------------------------------------------------------------------------#

#----------------------------#
# function: isEmailAddress() #
#----------------------------#-------------------------------------------------#
# This function checks to see if a given string represents a well-formed email #
# address.  If it does, the function returns 1, otherwise, 0.                  #
#------------------------------------------------------------------------------#

sub isEmailAddress {

    return($_[0] =~ /^(([\w\-\_])+(\.)*)+\@([\w\-\_]+\.)+[A-Za-z]{2,}$/i);

}

#------------------------------------------------------------------------------#
# End of isEmailAddress() function.                                            #
#------------------------------------------------------------------------------#

#------------------------------#
# function: validateUsername() #
#------------------------------#-----------------------------------------------#
# This function checks to see if a given username is available for use.  If    #
# username already exists in the /etc/passwd file, it returns 0.  If no such   #
# entry exists, it returns 1.                                                  #
#------------------------------------------------------------------------------#

sub validateUsername {

    my $user = $_[0];
    my $match = getpwnam($user);
    return (1) if (&isWhiteSpace($match));
    &debug("$user exists in /etc/passwd: uid $match\n");
    return(0);

}

#------------------------------------------------------------------------------#
# End of validateUsername() function.                                          #
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
    }
    if (!(-e $inputFile)) {
        &errorHandler("File $inputFile not found.\n",0);
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
# function: getTimeStamp() #
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

#------------------------------------------------------------------------------#
# Read the /etc/passwd file into an array.                                     #
#------------------------------------------------------------------------------#

    my @passwdFile = &readData("/etc/passwd");
    my %shellList = &createShellList(@passwdFile);
    my %userList = &createUserList(@passwdFile);
    my %userData = &getUserDetails(keys %userList);
    &generateReport(%userData);

}

#------------------------------------------------------------------------------#
# End of main() function.                                                      #
#------------------------------------------------------------------------------#

#------------------------------------------------------------------------------#
# End of userReport.pl                                                         #
#------------------------------------------------------------------------------#

