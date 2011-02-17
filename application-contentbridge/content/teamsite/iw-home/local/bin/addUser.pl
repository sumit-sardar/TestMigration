#!/export/interwoven/TeamSite/iw-perl/bin/iwperl
#-----------------------------------------#
# Script: addUser.pl                      #
# Language: Perl 5                        #
# Author: Cruz deWilde                    #
# Version: 1.4                            #
# Last Modified: 08/30/2004               #
#-----------------------------------------#------------------------------------#
#                                                                              #
# This script adds a user to the TeamSite system by creating a unique username #
# (8 characters or less) based on the first and last name, creating an entry   #
# the /etc/passwd file, adding the user to the appropriate groups and TeamSite #
# roles files, creating a unique password for the user, and generating an      #
# email message notifying the user that the account has been created.          #
#                                                                              #
# USAGE:                                                                       #
#                                                                              #
# addUser.pl -n "<first> <last>" -g "<groups>" -r "<roles>" [-d] [-s]          #
#                                                                              #
#      Parameters (mandatory):                                                 #
#            -n      Name ("<First Name> <Last Name>")                         #
#            -g      Groups ("<primary> <secondary 1> <secondary 2> ...")      #
#            -r      Roles ("author editor admin master")                      #
#      Parameters (optional):                                                  #
#            -e      Email Address (default: "first_last@ctb.com")             #
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
    getopts('dsn:g:r:e:');

#------------------------------------------------------------------------------#
# Set the email address of the account administrator.                          #
#------------------------------------------------------------------------------#

    $ADMIN_EMAIL = "Cruz deWilde <cruz_dewilde\@ctb.com>";
    ($SCRIPT_NAME, $SCRIPT_DIR, $SCRIPT_TYPE) = fileparse($0); 

    $NAME = $opt_n || &usage("Please supply a first and last name");
    my $emailBase = $NAME;
    $emailBase =~ s/\s/_/gi;
    $GROUPS = $opt_g || &usage("Please supply a valid primary group");
    $ROLES = $opt_r || &usage("Please supply one or more TeamSite roles");
    $EMAIL = $opt_e || lc("$emailBase\@ctb.com");
    

#------------------------------------------------------------------------------#
# With $DEBUG set to "1", the script will print output to the command line.    #
# With $DEBUG set to "0", the script will operate silently.                    #
#------------------------------------------------------------------------------#

    $DEBUG = $opt_d || 0;

#------------------------------------------------------------------------------#
# With $SIMULATE set to "1", the script will not make any system changes.      #
# With $SIMULATE set to "0", the script will operate normally.                 #
#------------------------------------------------------------------------------#

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

 $SCRIPT_NAME -n "<first> <last>" -g "<groups>" -r "<roles>" [-d] [-s]

      Parameters (mandatory):
            -n      Name ("<First Name> <Last Name>")
            -g      Groups ("<primary> <secondary 1> <secondary 2> ...")
            -r      Roles ("author editor admin master")
      Parameters (optional):
            -e      Email Address (default: "first_last@ctb.com")
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

#----------------------------#
# function: createUsername() #
#----------------------------#-------------------------------------------------#
# Routine creates and validates a unique user id based on the first, last,     #
# and possibly middle name of the new user.  The first choice is based on      #
# taking the first letter of the preferred or first name, concatenated with    #
# the first seven letters of the last name.                                    #
#------------------------------------------------------------------------------#

sub createUsername () {

    my $name = $_[0];
    my $user = "";
    my $userFound = 0;

    my @names = split(/\s+/,$name);
    my $first = shift(@names);
    my $last = pop(@names);
    &debug("First Name: $first, Last Name: $last\n");
    &usage("Please supply a first and last name") if (&isWhiteSpace($last));
    $last =~ s/\s+//g;
    $last =~ s/\'//g;

#------------------------------------------------------------------------------#
# If the last name is exactly 8 characters long, then we'll try that first in  #
# order to avoid needlessly truncating the user's name.                        #
#------------------------------------------------------------------------------#

    if (length($last) == 8) {
        $user = lc("$last");
        &debug("$last has 8 chars... Trying username $user\n");
        return($user) if (&validateUsername($user));
    }

    $first = substr($first,0,8);
    my $fullLast = $last;
    $last = substr($last,0,7);
    my $fInitial = substr($first,0,1);
    my $lInitial = substr($last,0,1);

#------------------------------------------------------------------------------#
# If we didn't return $user above, then we must once again continue            #
# formulating usernames until we find one that isn't taken. We start with the  #
# first initial of the first name and the first seven letters of the last.     #
#------------------------------------------------------------------------------#

    &debug("Trying first initial and 7 chars of last name\n");
    $user = lc("$fInitial$last");
    &debug("Username: $user for Name: $name...\n");
    return($user) if (&validateUsername($user));

        
#------------------------------------------------------------------------------#
# If we didn't return $user above, then we must once again continue            #
# formulating usernames until we find one that isn't taken.  Next we try the   #
# first seven letters of the last name concatenated with the first initial     #
# of the first name.                                                           #
#------------------------------------------------------------------------------#

    &debug("Trying 7 chars of last name and first initial \n");
    $user = lc("$last$fInitial");
    &debug("Username: $user for Name: $name...\n");
    return($user) if (&validateUsername($user));

#------------------------------------------------------------------------------#
# If we didn't return $user above, then we must once again continue            #
# formulating usernames until we find one that isn't taken.  Next we try the   #
# first eight letters of the first name.                                       #
#------------------------------------------------------------------------------#

    &debug("Trying first 8 charatcers of first name\n");
    $user = lc("$first");
    &debug("Username: $user for Name: $name...\n");
    return($user) if (&validateUsername($user));

#------------------------------------------------------------------------------#
# If we didn't return $user above, then we must once again continue            #
# formulating usernames until we find one that isn't taken.  Next we try the   #
# first seven letters of the first name and the first character of the last.   #
#------------------------------------------------------------------------------#

    &debug("Trying 7 chars of first name and last initial\n");
    $user = lc("$first$lInitial");
    &debug("Username: $user for Name: $name...\n");
    return($user) if (&validateUsername($user));

#------------------------------------------------------------------------------#
# If we didn't return $user above, then we must once again continue            #
# formulating usernames until we find one that isn't taken.  Next we try the   #
# first eight letters of the last name.                                        #
#------------------------------------------------------------------------------#

    &debug("Trying first 8 charatcers of last name\n");
    $last = substr($fullLast,0,8);
    $user = lc("$last");
    &debug("Username: $user for Name: $name...\n");
    return($user) if (&validateUsername($user));

#------------------------------------------------------------------------------#
# If, after all that, we still weren't able to generate an available username, #
# then we simply return a mournful failure message.  If this ever happens,     #
# then I'll build in some logic which cycles through additional substring      #
# combinations and just shuffles letters around till we can give the user      #
# *something*.                                                                 #
#------------------------------------------------------------------------------#

    &errorHandler("Unable to generate unique userID\n",0);

}

#------------------------------------------------------------------------------#
# End of createUsername() function.                                            #
#------------------------------------------------------------------------------#

#----------------------------#
# function: createPassword() #
#----------------------------#-------------------------------------------------#
# Routine generates a secure, random password between 6 and 8 characters long  #
# by taking a random word out of the system's /usr/dict/words file, switching  #
# a few letters with numbers, and truncating it to 8 characters or less.  The  #
# resulting words are more or less recognizable (and therefore easier to       #
# remember), but difficult to crack due to the truncation and letter-to-number #
# conversion.  To ensure that potentially offensive passwords are not blindly  #
# generated and sent out to customers, the function requires that the password #
# be confirmed with a "y" or "n" keystroke before being finalized.             #
#------------------------------------------------------------------------------#

sub createPassword () {

    &debug("Generating secure random password...\n");
    my $wordFile = "/usr/dict/words";
    my @words = &readData($wordFile);
    my $length = scalar(@words);
    &debug("Found $length seed words in $wordFile...\n");
    my $pw = "";
    while(&isWhiteSpace($pw)) {

#------------------------------------------------------------------------------#
# Take a random word from the /usr/dict/words file containing 6 or more        #
# characters.                                                                  #
#------------------------------------------------------------------------------#

        my $rand = int(rand($length-1));
        my $try = $words[$rand];
        &debug("Trying word \#$rand: $try\n");
        if (length($try) > 5) {
            $pw = $try;
            $pw =~ s/ate/8/gi;
            $pw =~ s/ait/8/gi;
            $pw =~ s/e/3/gi;
            $pw =~ s/i/1/gi;
            &debug("Converted word: $try -> $pw\n");
            $pw = substr($pw,0,8);
            if (length($pw) < 6) {
                &debug("Converted word \'$pw\' too short. Trying again...\n");
                $pw = "";
                next;
            }

#------------------------------------------------------------------------------#
# Request confirmation from the executor that the password is suitable.        #
#------------------------------------------------------------------------------#

            print "Is the password \'$pw\' okay? (y/n): ";
            $| = 1;
            my $key = getone();
            print "--> $key\n";
            while (($key ne "y") && ($key ne "n")) {
                print "Please answer \'y\' or \'n\': ";
                $key = getone();
                print "--> $key\n";
            }

#------------------------------------------------------------------------------#
# The following block of code is borrowed from the Perl FAQ #5.  Essentially,  #
# it saves the executor of the script a keystroke by accepting a "y" or "n"    #
# response to the question above without requiring a carriage return.          #
#------------------------------------------------------------------------------#

            BEGIN {
                use POSIX qw(:termios_h);
                my ($term, $oterm, $echo, $noecho, $fd_stdin);
                $fd_stdin = fileno(STDIN);
                $term     = POSIX::Termios->new();
                $term->getattr($fd_stdin);
                $oterm     = $term->getlflag();
                $echo     = ECHO | ECHOK | ICANON;
                $noecho   = $oterm & ~$echo;
                sub cbreak {
                    $term->setlflag($noecho);
                    $term->setcc(VTIME, 1);
                    $term->setattr($fd_stdin, TCSANOW);
                }
                sub cooked {
                    $term->setlflag($oterm);
                    $term->setcc(VTIME, 0);
                    $term->setattr($fd_stdin, TCSANOW);
                }
                sub getone {
                    my $key = '';
                    cbreak();
                    sysread(STDIN, $key, 1);
                    cooked();
                    return $key;
                }
            }
            END { cooked() }

#------------------------------------------------------------------------------#
# End of borrowed code block.                                                  #
#------------------------------------------------------------------------------#

            if ($key =~ /^(n|N)/) {
                &debug("Password \'$pw\' rejected.  Trying again...\n");
                $pw = "";
            } else {
                &debug("Password \'$pw\' accepted...\n");
            }
        } else {
            &debug("\'$try\' is too short.  Let\'s try again...\n");
        }
    }
    &debug("Password successfully generated: $pw\n");
    return($pw);

}

#------------------------------------------------------------------------------#
# End of createPassword() function.                                            #
#------------------------------------------------------------------------------#

#-------------------------#
# function: setPassword() #
#-------------------------#----------------------------------------------------#
# This function runs the UNIX passwd command for the newly created username.   #
# Many thanks to the folks at O'Reilly for some helpful coding tips.           #
#------------------------------------------------------------------------------#

sub setPassword {

    my $user = $_[0];   
    my $passwd = $_[1];   

#------------------------------------------------------------------------------#
# Check to make sure that the given username has an entry in /etc/passwd.      #
#------------------------------------------------------------------------------#

    if ((&validateUsername($user)) && (!($SIMULATE))) {
        &errorHandler("$user not found in /etc/passwd. Password not set.\n",1);
        return(0);
    }
    my $pwCmd = "/usr/bin/passwd";
    &debug("Running $pwCmd $user...\n");

#------------------------------------------------------------------------------#
# Simply return if running in simulation mode.                                 #
#------------------------------------------------------------------------------#

    return(1) if ($SIMULATE);

#------------------------------------------------------------------------------#
# The Expect.pm module is not standard issue with Perl, and requires IO::Tty.  #
# The TeamSite instance of Perl does come with Expect installed.               #
#------------------------------------------------------------------------------#

    use Expect;

#------------------------------------------------------------------------------#
# Create a process object using the $pwCmd command and $user username.         #
#------------------------------------------------------------------------------#

    my $pwObj = Expect->spawn($pwCmd, $user);
    if (!(defined $pwObj)) {
        &errorHandler("Cannot run $pwCmd:$! Please set password manually.\n",1);
        return(0);
    }

#------------------------------------------------------------------------------#
# Do not log to stdout (i.e. be silent)                                        #
#------------------------------------------------------------------------------#

    $pwObj->log_stdout($DEBUG);

#------------------------------------------------------------------------------#
# Wait for password & password re-enter prompts, answering appropriately.      #
#------------------------------------------------------------------------------#

    $pwObj->expect(10,"New password: ");
    print $pwObj "$passwd\r";
    $pwObj->expect(10, "Re-enter new password: ");
    print $pwObj "$passwd\r";

#------------------------------------------------------------------------------#
# Check the result, reading it into a variable.                                #
#------------------------------------------------------------------------------#

    my $result = (defined ($pwObj->expect(10, "successfully changed")) ? 
                                          "" : "password change failed");

#------------------------------------------------------------------------------#
# Close the process object, waiting up to 15 secs for the process to exit.     #
#------------------------------------------------------------------------------#

    &debug("Closing passwd command object $pwCmd $user. $result\n");
    if ($pwObj->soft_close(  )) { 
        &errorHandler("Failed to close passwd command\n",1);
        return(0);
    }
    
    &debug("Successfully set password for $user\n");
    return(1);
   
}

#------------------------------------------------------------------------------#
# End of setPassword() function.                                               #
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

#------------------------------#
# function: setTeamSiteRoles() #
#------------------------------#-----------------------------------------------#
# This function adds a given username to the specified roles file(s).  For     #
# each role specified, this function reads in the contents of the file,        #
# creating an array of usernames, sorts the array (removing duplicates), and   #
# prints the array back out to the originating file, setting the permissions   #
# appropriately on the way out.  This keeps the roles file clean and healthy.  #
#------------------------------------------------------------------------------#

sub setTeamSiteRoles {

    my $user = $_[0];
    my $role = $_[1];

#------------------------------------------------------------------------------#
# Check to make sure that the given username has an entry in /etc/passwd.      #
#------------------------------------------------------------------------------#

    if ((&validateUsername($user)) && (!($SIMULATE))) {
        &errorHandler("$user not found in /etc/passwd file\n",0);
    }

#------------------------------------------------------------------------------#
# Ensure a clean read of the roles list whether delimited by spaces or commas. #
#------------------------------------------------------------------------------#

    @roles = split(/\,\s*|\s+/,$role);
    &debug("username is $user, roles include: $role\n");

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
        my $initialcount = scalar(@userList);
        push (@userList, $user);
        @userList = &uniqSort(@userList);
        my $count = scalar(@userList);
        &printArray(@userList, $roleFile) unless ($SIMULATE);
        &debug("$user added to $roleFile ($count names)\n");
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
# End of setTeamSiteRoles() function.                                          #
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

#------------------------#
# function: runUserAdd() #
#------------------------#-----------------------------------------------------#
# This function runs the UNIX userAdd command, using the username, real name   #
# and primary group.                                                           #
#------------------------------------------------------------------------------#

sub runUserAdd {

    my $user = $_[0];
    my $name = $_[1];
    my $groups = $_[2];
    my $dir = $_[3] || "/export/home/$user";
    my $sh = $_[4] || "/bin/false";
    my @groupList = split(/\,\s*|\s+/,$groups);
    foreach my $grp (@groupList) {
        my $gid = getgrnam($grp);
        if (&isWhiteSpace($gid)) {
            &errorHandler("Group \'$grp\' not found in /etc/group file.\n",0);
        } else {
            &debug("Group \'$grp\' found in /etc/group file: gid=$gid\n");
        }
    }
        
    my ($primary, $secondary) = split(/\,\s*|\s+/,$groups,2);
    $secondary =~ s/\,\s*|\s+/,/g;
    if (&isWhiteSpace($user)) {
        &errorHandler("Missing username.\n",0);
    }
    if (&isWhiteSpace($name)) {
        &errorHandler("Missing real name.\n",0);
    }
    if (&isWhiteSpace($primary)) {
        &errorHandler("Missing primary group name.\n",0);
    }
    my $useraddCmd = "/usr/sbin/useradd -c \"$name\" -d $dir -g $primary";
    if (!(&isWhiteSpace($secondary))) {
        $useraddCmd = "$useraddCmd -G $secondary";
    }
    $useraddCmd = "$useraddCmd -m -s $sh $user";
    &debug("$useraddCmd\n"); 
    return(1) if ($SIMULATE);
    open(USERADD, "$useraddCmd |") 
        || &errorHandler("Cannot execute $useraddCmd: $!\n");
    while(<USERADD>) {
        chomp($_);
        &debug("$_\n");
    }
    close(USERADD);
    &debug("Successfully created username ($user) for $NAME\n");
    return(1);

}

#------------------------------------------------------------------------------#
# End of runUserAdd() function.                                                #
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

#------------------------#
# function: notifyUser() #
#------------------------#-----------------------------------------------------#
# This function generates an email message to the email address:               #
#      <first>_<last>@mcgraw-hill.com                                          #
# notifying the user about their account creation and logon credentials.       #
# This message is formatted to be passed to the sendMail() function for        #
# delivery.                                                                    #
#------------------------------------------------------------------------------#

sub notifyUser {

    my $name = $_[0];
    my ($fName, $lName) = split(/\s+/,$name,2);
    my $user = $_[1];
    my $pw = $_[2];
    my $groups = $_[3];
    my $roles = $_[4];
    my $adminEmail = $_[5];
    my $email = $_[6];

#------------------------------------------------------------------------------#
# Determine which TeamSite server we are running on, and set variables         #
# accordingly.                                                                 #
#------------------------------------------------------------------------------#

    my $accountType = "";
    my $hostname = `/bin/hostname`;
    if ($?) {
        &errorHandler("Failed to run hostname. Please check your path.\n",0);
    }
    chomp($hostname);
    my $loginURL = "http://$hostname/iw/webdesk/login?name\=$user";
    my $contentCenterMsg = "";
    if ($hostname eq "troy.eppg.com") {
        $loginURL = "https://$hostname/iw-cc/teamsite/common/start.jsp";
        $contentCenterMsg =<<EOB;

On the login screen, you should see a dropdown list labeled "ContentCenter"
(if you do not see it, click the "Options >>" button).  This allows you to
choose your preferred layout based on your needs and familiarity with the
TeamSite interface:

   Standard -     Designed for those new to the updated interface,
                  this layout is accessible and rife with helpful
                  tips.  It is somewhat tailored to managing and
                  previewing web content, however, which may not
                  suit your needs.

   Professional - Designed for those more familiar with TeamSite's
                  updated interface, this layout is robust and highly
                  intuitive.

(If you decide once you've logged in that you would like to switch layouts,
simply click the "Logout" link in the upper right-hand corner, and log back
in using the preferred selection.) 

EOB
    }
    &debug("Hostname is $hostname\n");
    &debug("LoginURL is $loginURL\n");

#------------------------------------------------------------------------------#
# Generate a formatted email message to be passed to the sendMail() function.  #
#------------------------------------------------------------------------------#

    my $mailMessage =<<EOB;
From: $adminEmail
To: $name <$email>
Bcc: $adminEmail
Subject: New TeamSite Account for $fName $lName

Hi $fName,

I have just created a new TeamSite account for you!
Your access credentials are:

Login URL:         $loginURL
Username:          $user
Password:          $pw
TeamSite Role(s):  $roles
Unix Group(s):     $groups
$contentCenterMsg
Please let me know if you have any questions.

Cheers,

$adminEmail

EOB
    &debug($mailMessage);

#------------------------------------------------------------------------------#
# Call the sendMail() function unless running in SIMULATE mode.                #
#------------------------------------------------------------------------------#

    my $sent = &sendMail($mailMessage) unless ($SIMULATE);
    &debug("sendMail returned: $sent\n");
    return($sent);

}

#------------------------------------------------------------------------------#
# End of notifyUser() function.                                                #
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
    my $user = &createUsername($NAME);
    my $pass = &createPassword();
    my $addUser = &runUserAdd($user,$NAME,$GROUPS)
        || &errorHandler("Failed to create Unix account for $user\n",0);
    my $setPass = setPassword($user,$pass) 
        || &errorHandler("Failed to set password for $user\n",1);
    &setTeamSiteRoles($user,$ROLES)
        || &errorHandler("Failed to set TeamSite Roles for $user\n",1);
    &notifyUser($NAME,$user,$pass,$GROUPS,$ROLES,$ADMIN_EMAIL,$EMAIL);

}

#------------------------------------------------------------------------------#
# End of main() function.                                                      #
#------------------------------------------------------------------------------#

#------------------------------------------------------------------------------#
# End of addUser.pl                                                            #
#------------------------------------------------------------------------------#

