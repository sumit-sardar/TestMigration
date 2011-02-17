package CTB::CIM;

#------------------------------------------------------------------------------
# Copyright 2003 Interwoven, Inc.   All rights reserved.
#
# Author    Bobby Magee <bobby@interwoven.com>
#
# File      CTB::CIM.pm
# Use       Executing OAS's CIM 
#
#------------------------------------------------------------------------------
use CTB::RegEx;
use File::Copy;
use File::Basename;

sub run_jgen {

	my $swf = $_[0];
	my $ret_str;
	
	#my $date = localtime();
	#print "$date: Running JGen command\n";
	# `/appl/publishing/jgenerator/2.1/bin/jgenerate -log4j -swf $swf cab_iak.swt`;
	my $cmd = "/appl/publishing/jgenerator/2.1/bin/jgenerate -log -swf $swf cab_iak.swt";			
	
	#print "$cmd\n";
	
	open(PIPE, "$cmd 2>&1 |");
	my(@text) = <PIPE>;
	close(PIPE);

	foreach $line (@text) {
	   next if ($line =~ /\/bin\/java/ && $line =~ /too\smany\sopen\sfiles/);
	   $ret_str .= $line;
	   if ($line =~ /Exception/ && !(defined $error)) {
	       $error = $line;
	   } 
	}
	$error = "\n" if (!(defined $error));
	$ret_str = " " if (!(defined $ret_str));
        return ($?, $error . $ret_str);
}

sub fulldoc_jgen {

	my $swf = $_[0]; 
	
	#my $date = localtime();
	#print "$date: Running JGen command\n";
	# `/appl/publishing/jgenerator/2.1/bin/jgenerate -log4j -swf $swf cab_ak_subtest.swt`;
	my $cmd = "/appl/publishing/jgenerator/2.1/bin/jgenerate -log -swf $swf cab_ak_subtest.swt";			
	
	#print "$cmd\n";
	
	open(PIPE, "$cmd 2>&1 |");
		my(@text) = <PIPE>;
	close(PIPE);
	
	#print join('', @text);
	
	return 0;
}


sub run_cim {

	# OUTDATED PLEASE SEE DEPLOY.PM
	
	my $file_string;
	my ($environment, $xml, $swf, $pdf,$user_dir) = @_;
	#my $date = localtime();
        my  $ret_str = "";
        my $error;

	my $class_path = get_classpath($user_dir);
	#print "$date: Running CIM command\n";
	#my $cmd =  "java -jar /appl/publishing/cim/XML2DBImport.jar interwoven cab item $environment $xml $swf $pdf";
	my $cmd =  "java -classpath $class_path com.ctb.common.tools.ContentImportModule interwoven cab item $environment $xml $swf $pdf";
	print $cmd;
        open(PIPE, "$cmd 2>&1 |");
                my(@text) = <PIPE>;
        close(PIPE);

        foreach $line (@text) {
	   next if ($line =~ /\/bin\/java/ && $line =~ /too\smany\sopen\sfiles/);
           $ret_str .= $line;
           if ($line =~ /Exception/ && !(defined $error)) {
	       $error = $line;
           } 
        }
         $error = "\n" if (!(defined $error));
         $ret_str = " " if (!(defined $ret_str));
         return ($?, $error . $ret_str, $error);
	# return 0;
}

sub logline {
    my $jobid = shift;
    my $itemid = shift;
    my $callee = shift;
    my $exit_code = shift;
    my $error = shift;

    $error = "" if (!defined($error));

    my $date = localtime();
    my $delim = "\t";
    return $date . $delim . $jobid . $delim . $itemid . $delim . $callee . $delim . $exit_code . $delim . $error . "\n";
}

sub run_fop {

	$xml = basename($_[0]);
	$dir = dirname($_[0]);
	my $results = 0;
	my @rubric;
	my $log;
	
	my $cmd = "/appl/publishing/cim/fopit $_[0] /export/cim/FOP_Interface.xsl";
	
	open(PIPE, "$cmd 2>&1 |");
	my(@text) = <PIPE>;
	close(PIPE);
	
	$results = $?;
	foreach $line (@text) {
	   next if ($line =~ /\/bin\/java/ && $line =~ /too\smany\sopen\sfiles/);
	   $log .= $line;
	} 
	
	if ($results != 0) {
		return ($results, $log);
	} else {
	
		my $type = CTB::RegEx::item_type("$dir/$xml");
	
		if ($type eq "CR") {
			copy("$dir/$xml", "$dir/RUBRIC_$xml");
			@rubric = rubric_fop("$dir/RUBRIC_$xml");
			$results = $rubric[0];
			$log = $rubric[1];
		  	
		}
	
		return ($results, $log);
	}
	
}

sub rubric_fop {
	my $log;
	
	$xml = basename($_[0]);
	$dir = dirname($_[0]);
	
	copy("/appl/publishing/cim/FOP_Interface_AK.xsl", "FOP_Interface_AK.xsl");
	
	
	my $cmd = "/appl/publishing/cim/fopit $_[0] /export/cim/FOP_Interface_AK.xsl";
	
	open(PIPE, "$cmd 2>&1 |");
	    	my(@text) = <PIPE>;
	    	close(PIPE);
	
	foreach $line (@text) {
		   next if ($line =~ /\/bin\/java/ && $line =~ /too\smany\sopen\sfiles/);
		   $log .= $line;
	}
    	unlink "FOP_Interface_AK.xsl";
    	
	return ($?, $log);
    	
}

sub image_check {
	my $file = $_[0];
	my @error;
	
	open(XML, $file);
	while (<XML>) {
		if (/FileName="([^"]+)"/) { # "
			push @error, $1 unless -e $1;
		}
	}
	close XML;
	
	my $count = @error;
	return $count, join("\n", @error);
}

sub get_fop_subs {
	print "THIS HAS NOT BEEN IMPLEMENTED YET\n";
}

sub get_jgen_subs {
	print "THIS HAS NOT BEEN IMPLEMENTED YET\n";
}

sub get_classpath {

    my $userdir = $_[0];
    my $path = ".:";
    my $libdir = "/export/home/" . $userdir . "/interfaces/lib/";
    my $classesdir = "/export/home/" . $userdir . "/interfaces/build/classes:";

    opendir(DIR, $libdir) or die "cant open $libdir: $!";
    my @files = readdir(DIR);
    closedir(DIR);
    foreach(@files){
        next unless ($_ =~ /jar$/);
        $path = $path . $libdir . $_ . ":";
    }
    $path = $path . $classesdir;
    return $path;
}


1;
