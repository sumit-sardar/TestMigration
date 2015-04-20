#!/usr/bin/perl -w

use strict;
use Switch;
use Time::Local;
use Getopt::Long;

use constant {
	TAS_LOG_PART_1 => '/local/apps/oas/tas/servers/tas',
	TAS_LOG_PART_2 => '/logs/tas',
	TAS_LOG_PART_3 => '.out',
	LOG_ALL => 0,
	LOG_TRACE => 1,
	LOG_DEBUG => 2,
	LOG_INFO => 3,
	LOG_WARN => 4,
	LOG_ERROR => 5,
	LOG_FATAL => 6,
	LOG_OFF => 7,
	LOG_HEADERS => ['=== ALL', '--- TRACE', '~~~ DEBUG', "... INFO", "*** WARN", "+++ ERROR", "!!! FATAL"],
};

use constant LOG_LEVEL => LOG_DEBUG;

use constant MINUTES_OF_HISTORY => 60;

# --- MAIN BODY GOES HERE
my $scriptStart = time; # Used strictly to script execution time and logs to load.
my @report;

# Identify the path to this server's TAS log.
my $logfile = &findLogfilePath();
writeLog(LOG_INFO, "Identified '" . $logfile . "' for parsing.");

# Read the log entries from the last sixty minutes.
my @logEntries = &readLog($logfile, $scriptStart, MINUTES_OF_HISTORY);
writeLog(LOG_INFO, "Loaded most recent " . MINUTES_OF_HISTORY . " minutes of history.");

my $sinceLogsLoaded = time; # Used for all time calculations to determine how recently something happened relative to "now."
my $timeToLoad = $sinceLogsLoaded - $scriptStart;
push(@report, "Time to read BMTSync log entries into memory for parsing: $timeToLoad seconds");

# Run the monitoring scripts on the log entries to be analyzed:
push(@report, monitorLatestThreads($sinceLogsLoaded, @logEntries));
push(@report, monitorTimeToSync(@logEntries));
push(@report, monitorTestStatusCalls(@logEntries));

my $scriptEnd = time;
#push (@report, monitorScriptTime($scriptStart, $scriptEnd);

writeReport(@report);
exit(0);

# --- SUBROUTINES GO BELOW
# --- SUBROUTINES GO BELOW

sub findLogfilePath {
	my $hostname = `hostname`;
	my $filepath = "no-such-host";
	chomp $hostname;
	switch ($hostname) {
		case "nj09mhe5252" {$filepath = &assembleLogfilePath("1");}
		case "nj09mhe5253" {$filepath = &assembleLogfilePath("2");}
		case "nj09mhe5254" {$filepath = &assembleLogfilePath("3");}
		case "nj09mhe5255" {$filepath = &assembleLogfilePath("4");}
		case "ew1ctgl6343" {$filepath = &assembleLogfilePath("5");}
		case "ew1ctgl6344" {$filepath = &assembleLogfilePath("6");}
		case "ew1ctgl6349" {$filepath = &assembleLogfilePath("7");}
		case "ew1ctgl6350" {$filepath = &assembleLogfilePath("8");}
		case "ew1ctgl6351" {$filepath = &assembleLogfilePath("9");}
		case "ew1ctgl6352" {$filepath = &assembleLogfilePath("10");}
		case "ew1ctgl6370" {$filepath = &assembleLogfilePath("11");}
		case "ew1ctgl6371" {$filepath = &assembleLogfilePath("12");}
	}
	# Now, test that the file exists. 
	if (-f $filepath) {
		writeLog(LOG_DEBUG, "File '" . $filepath . "' found. Continuing.");
	} else {
		writeLog(LOG_ERROR, "File '" . $filepath . "' not found! Aborting.");
		exit(-1);
	}
	return $filepath;
}

sub assembleLogfilePath {
	my $pathNumber = shift @_;
	my $fullPath = TAS_LOG_PART_1 . $pathNumber . TAS_LOG_PART_2 . $pathNumber . TAS_LOG_PART_3;
	return $fullPath;
}

sub readLog {
	my $logfile = shift @_;
	my $now = shift @_;
	my $minutesToRead = shift @_;
	my $linesRead = 0;
	my $linesAdded = 0;
	my @linesToParse;
	my $fh; # file handle

	my $since = $now - ($minutesToRead * 60);
	my ($sinceSc, $sinceMn, $sinceHr, $sinceDy, $sinceMo, $sinceYr) = (gmtime($since))[0,1,2,3,4,5];
	my $sinceString = sprintf("%04d-%02d-%02d %02d:%02d:%02d", $sinceYr+1900, $sinceMo+1, $sinceDy, $sinceHr, $sinceMn, $sinceSc);
	writeLog(LOG_DEBUG, "Since: " . $sinceString);

	unless (open($fh, $logfile)) {
		writeLog(LOG_ERROR, "Can't open log file '$logfile' for opening! Aborting.");
		exit(-2);
	}

	# Currently, we don't know which log entries are ours. For now, if the timezone is written in ISO-8601 International,
	# It's probably a BMTSync log entry.
	while (my $line = <$fh>) {
		chomp($line);
		$linesRead++;
		if ($line =~ /^- (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.\d{3} - /) {
			# If it's a line we care about, add it.
			my $logDateString = $1;
			my $dateDiff = dateDifference($sinceString, $logDateString);
			writeLog(LOG_TRACE, $sinceString . " - " . $logDateString . " = " . $dateDiff);
			if ($dateDiff <= 0) {
				$linesAdded++;
				push(@linesToParse, $line);
			}
		}
	}
	writeLog(LOG_DEBUG, "Lines counted: " . $linesRead);
	writeLog(LOG_DEBUG, "Lines added: " . $linesAdded);
	return @linesToParse;
}

sub dateDifference {
	my $minuendRaw = shift @_;
	my $subtrahendRaw = shift @_;
	# http://stackoverflow.com/questions/95492/how-do-i-convert-a-date-time-to-epoch-time-aka-unix-time-seconds-since-1970

	writeLog(LOG_TRACE, "$minuendRaw - $subtrahendRaw");
	$minuendRaw =~ /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})$/;
	my ($minYr, $minMo, $minDy, $minHr, $minMn, $minSc) = ($1, $2, $3, $4, $5, $6);
	$minHr |= 0;  $minMn |= 0;  $minSc |= 0;  # defaults.
	$minYr = ($minYr<100 ? ($minYr<70 ? 2000+$minYr : 1900+$minYr) : $minYr);
	my $minuend = Time::Local::timelocal($minSc,$minMn,$minHr,$minDy,$minMo-1,$minYr);
	$subtrahendRaw =~ /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})$/;
	my ($subYr, $subMo, $subDy, $subHr, $subMn, $subSc) = ($1, $2, $3, $4, $5, $6);
	$subHr |= 0;  $subMn |= 0;  $subSc |= 0;  # defaults.
	$subYr = ($subYr<100 ? ($subYr<70 ? 2000+$subYr : 1900+$subYr) : $subYr);
	my $subtrahend = Time::Local::timelocal($subSc,$subMn,$subHr,$subDy,$subMo-1,$subYr);
	my $difference = $minuend - $subtrahend;
	return $difference;
}

sub monitorLatestThreads {
	my $now = shift @_;
	my @logEntries = @_;
	my @report;
	my $threadMap;

	my ($nowSc, $nowMn, $nowHr, $nowDy, $nowMo, $nowYr) = (gmtime($now))[0,1,2,3,4,5];
	my $nowString = sprintf("%04d-%02d-%02d %02d:%02d:%02d", $nowYr+1900, $nowMo+1, $nowDy, $nowHr, $nowMn, $nowSc);
	foreach my $logEntry (@logEntries) {
		writeLog(LOG_TRACE, "Log entry: " . $logEntry);
		if ($logEntry =~ /^- (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.\d{3} - ([^ ]+) - DEBUG - Starting polling for (.*) messages to post to BMT\.\.\.\.$/) {
			writeLog(LOG_TRACE, "Entry matches!");
			my ($timestamp, $thread, $type) = ($1, $2, $3);
			if (defined $threadMap->{$thread}->{"type"} && ($threadMap->{$thread}->{"type"} ne $type)) {
				writeLog(LOG_WARN, "Thread '$thread' changes type at $timestamp! Please validate!");
			}
			$threadMap->{$thread}->{"type"} = $type;
			$threadMap->{$thread}->{"lastPolled"} = $timestamp;
		}
	}
	push(@report, "");
	push(@report, "BMTSync Process Monitoring:");
	foreach my $key (sort keys %$threadMap) {
		writeLog(LOG_DEBUG, "Adding to report for key $key");
		push(@report, "    $key: " . $threadMap->{$key}->{"type"} . " poller last executed " . $threadMap->{$key}->{"lastPolled"} . " ("
			. dateDifference($nowString, $threadMap->{$key}->{"lastPolled"}) . " seconds)");
	}
	return @report;
}

sub monitorTimeToSync {
	my @logEntries = @_;
	my @report;
	my $threadMap;
	my $timestampMap;
	my $timeMarker;
	my ($timestamp, $millis, $thread, $type, $error);

	foreach my $logEntry (@logEntries) {
		writeLog(LOG_TRACE, "Log entry: " . $logEntry);
		if ($logEntry =~ /^- (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.(\d{3}) - ([^ ]+) - INFO - \[(.*)\] Request json to BMT/) {
			writeLog(LOG_TRACE, "Entry matches Request!");
			($timestamp, $millis, $thread, $type) = ($1, $2, $3, $4);
			if (defined $threadMap->{$thread}) {
				writeLog(LOG_ERROR, "Thread '$thread' has a request without a response!");
			}
			$threadMap->{$thread}->{"type"} = $type;
			$threadMap->{$thread}->{"startedTime"} = $timestamp;
			$threadMap->{$thread}->{"startedMillis"} = $millis; # Milliseconds cound in this operation.
			$threadMap->{$thread}->{"timestamp"} = $timestamp . "." . $millis;
		}
		if ($logEntry =~ /^- (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.(\d{3}) - ([^ ]+) - ERROR - \[(.*)\] (.*)$/) {
			writeLog(LOG_DEBUG, "Entry matches ERROR!");
			($timestamp, $millis, $thread, $type, $error) = ($1, $2, $3, $4, $5);
			$timeMarker = $timestamp . "." . $millis;
			$error =~ s/ \[.*id=\d+,.*id=\d+\]$//i;
			$timestampMap->{$type}->{$timeMarker}->{"errorMessage"} = $error;
		}
			
		if ($logEntry =~ /^- (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.(\d{3}) - ([^ ]+) - INFO - \[(.*)\] Response json from BMT/) {
			writeLog(LOG_TRACE, "Entry matches Response!");
			($timestamp, $millis, $thread, $type) = ($1, $2, $3, $4);
			
			if (not defined $threadMap->{$thread}) {
				writeLog(LOG_WARN, "Thread '$thread' has response with no call. Possibly crosses a time boundary.");
				next;
			}
			if (defined $threadMap->{$thread}->{"type"} && ($threadMap->{$thread}->{"type"} ne $type)) {
				writeLog(LOG_WARN, "Thread '$thread' changes type at $timestamp! Please validate!");
			}
			$timeMarker = $threadMap->{$thread}->{"timestamp"};
			$timestampMap->{$type}->{$timeMarker}->{"startedTime"} = $threadMap->{$thread}->{"startedTime"};
			$timestampMap->{$type}->{$timeMarker}->{"startedMillis"} = $threadMap->{$thread}->{"startedMillis"};
			$timestampMap->{$type}->{$timeMarker}->{"endedTime"} = $timestamp;
			$timestampMap->{$type}->{$timeMarker}->{"endedMillis"} = $millis;
			$logEntry =~ /successCount":(\d+)[^0-9]/;
			$timestampMap->{$type}->{$timeMarker}->{"successCount"} = $1;
			$logEntry =~ /failureCount":(\d+)[^0-9]/;
			$timestampMap->{$type}->{$timeMarker}->{"failureCount"} = $1;
			if ($logEntry =~ /errorMessage":"([^"]+)"/) {
				$timestampMap->{$type}->{$timeMarker}->{"errorMessage"} = $1;
			}
			
			delete $threadMap->{$thread};
		}
	}

	foreach my $type (sort keys %$timestampMap) {
		writeLog(LOG_DEBUG, "Adding report for $type Poller");
		my $timeMarkerMap = $timestampMap->{$type};
		my $timeDifference = 0;
		my $totalServiceCallTime = 0;
		my $totalServiceCalls = 0;
		my $totalServiceCallRecordSuccesses = 0;
		my $totalServiceCallRecordFailures = 0;
		my $totalServiceCallRecords = 0;
		my $totalServiceCallTimePerRecord = 0;
		my $errorTypes = undef;
		foreach my $timeMarker (sort keys %$timeMarkerMap) {
			if (defined $timeMarkerMap->{$timeMarker}->{"errorMessage"}) {
				$errorTypes->{$timeMarkerMap->{$timeMarker}->{"errorMessage"}}++;
			}

			if (defined $timeMarkerMap->{$timeMarker}->{"endedTime"} &&
					defined $timeMarkerMap->{$timeMarker}->{"endedMillis"} &&
					defined $timeMarkerMap->{$timeMarker}->{"startedMillis"} &&
					defined $timeMarkerMap->{$timeMarker}->{"startedTime"}) {
				$timeDifference = dateDifference($timeMarkerMap->{$timeMarker}->{"endedTime"}, $timeMarkerMap->{$timeMarker}->{"startedTime"}) * 1000
						+ $timeMarkerMap->{$timeMarker}->{"endedMillis"} - $timeMarkerMap->{$timeMarker}->{"startedMillis"};
				$totalServiceCallTime += $timeDifference;
				$totalServiceCalls++;
			}

			if (defined $timeMarkerMap->{$timeMarker}->{"successCount"} && defined $timeMarkerMap->{$timeMarker}->{"failureCount"}) {
				$totalServiceCallRecordSuccesses += $timeMarkerMap->{$timeMarker}->{"successCount"};
				$totalServiceCallRecordFailures += $timeMarkerMap->{$timeMarker}->{"failureCount"};
				$totalServiceCallRecords += $timeMarkerMap->{$timeMarker}->{"successCount"} + $timeMarkerMap->{$timeMarker}->{"failureCount"};
				$totalServiceCallTimePerRecord += $timeDifference /
						 ($timeMarkerMap->{$timeMarker}->{"successCount"} + $timeMarkerMap->{$timeMarker}->{"failureCount"});
			}
		}
		my $totalServiceMeanCallTime;
		my $totalServiceMeanCallTimePerRecord;
		if ($totalServiceCalls) {
			$totalServiceMeanCallTime = $totalServiceCallTime / $totalServiceCalls;
			$totalServiceMeanCallTimePerRecord = $totalServiceCallTimePerRecord / $totalServiceCalls;
		} else {
			$totalServiceMeanCallTime = 0;
			$totalServiceMeanCallTimePerRecord = 0;
		}
		push(@report, "");
		push(@report, "Statistics for $type Poller:");
		push(@report, "\tTotal Service Calls: $totalServiceCalls");
		push(@report, "\tTotal Service Records: $totalServiceCallRecords");
		push(@report, "\tTotal Service Successes: $totalServiceCallRecordSuccesses");
		push(@report, "\tTotal Service Failures: $totalServiceCallRecordFailures");
		my $successRate;
		if ($totalServiceCallRecords) {
			$successRate = ($totalServiceCallRecordSuccesses / $totalServiceCallRecords) * 100;
		} else {
			$successRate = 0;
		}
		push(@report, sprintf("\tTotal Service Success Rate: %.2f%%", $successRate));
		push(@report, "\tTotal Service Call Time: ${totalServiceCallTime}ms");
		push(@report, sprintf("\tTotal Service Mean Call Time: %.2fms", $totalServiceMeanCallTime));
		push(@report, sprintf("\tTotal Service Mean Call Time Per Record: %.2fms", $totalServiceMeanCallTimePerRecord));
		if (defined $errorTypes && (scalar keys %$errorTypes > 0)) {
			push(@report, "\tErrors encountered during processing:");
			foreach my $errorMessage (sort keys %$errorTypes) {
				push(@report, "\t\t$errorMessage: " . $errorTypes->{$errorMessage});
			}
		}
	}
	return @report;
}

sub monitorTestStatusCalls {
	my @logEntries = @_;
	my @report;
	my $threadMap = undef;
	my $timestampMap;
	my $timeMarker;
	my ($timestamp, $millis, $thread, $type, $error);

	foreach my $logEntry (@logEntries) {
		if ($logEntry =~ /^- (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.(\d{3}) - .* - INFO - \[TestStatus\] Request From BMT/) {
			($timestamp, $millis) = ($1, $2);
			writeLog(LOG_DEBUG, "Entry matches Test Status Request!");
			$threadMap->{"startedTime"} = $timestamp;
			$threadMap->{"startedMillis"} = $millis; # Milliseconds cound in this operation.
			$threadMap->{"timestamp"} = $timestamp . "." . $millis;
		}

		if ($logEntry =~ /^- (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.(\d{3}) - .* - INFO - \[TestStatus\] Response to BMT/) {
			($timestamp, $millis) = ($1, $2);
			writeLog(LOG_TRACE, "Entry matches Test Status Response!");
			if (not defined $threadMap) {
				writeLog(LOG_WARN, "Thread has response with no call. Possibly crosses a time boundary.");
				next;
			}

			$timeMarker = $threadMap->{"timestamp"};
			$timestampMap->{$timeMarker}->{"startedTime"} = $threadMap->{"startedTime"};
			$timestampMap->{$timeMarker}->{"startedMillis"} = $threadMap->{"startedMillis"};
			$timestampMap->{$timeMarker}->{"endedTime"} = $timestamp;
			$timestampMap->{$timeMarker}->{"endedMillis"} = $millis;
			$logEntry =~ /successCount":(\d+)[^0-9]/;
			$timestampMap->{$timeMarker}->{"successCount"} = $1;
			$logEntry =~ /failureCount":(\d+)[^0-9]/;
			$timestampMap->{$timeMarker}->{"failureCount"} = $1;
			if ($logEntry =~ /errorMessage":"([^"]+)"/) {
				$timestampMap->{$timeMarker}->{"errorMessage"} = $1;
				if ($timestampMap->{$timeMarker}->{"errorMessage"} eq "Roster Id - OasTest ID  does not exist in OAS") {
					$logEntry =~ /oasRosterId":(\d+)[^0-9]/;
					$timestampMap->{$timeMarker}->{"failedRosterId"} = $1;
					$logEntry =~ /oasTestId":"([^"]+)"/;
					$timestampMap->{$timeMarker}->{"failedTestId"} = $1;
				}
			}
			
			$threadMap = undef;
		}
	}
	
	push(@report, "");
	push(@report, "Statistics for BMTSync TimeStatus API:");

	my $errorTypes;
	my $timeDifference;
	my $totalServiceCallTime;
	my $totalServiceCalls;
	my $totalServiceCallRecords;
	my $totalServiceCallRecordSuccesses;
	my $totalServiceCallRecordFailures;
	my $totalServiceCallTimePerRecord;
	my @missingRosterTests;
	foreach $timeMarker (sort keys %$timestampMap) {
		$totalServiceCalls++;
		if (defined $timestampMap->{$timeMarker}->{"errorMessage"}) {
			$errorTypes->{$timestampMap->{$timeMarker}->{"errorMessage"}}++;
		}
		$timeDifference = dateDifference($timestampMap->{$timeMarker}->{"endedTime"}, $timestampMap->{$timeMarker}->{"startedTime"}) * 1000
			+ $timestampMap->{$timeMarker}->{"endedMillis"} - $timestampMap->{$timeMarker}->{"startedMillis"};
		$totalServiceCallTime += $timeDifference;
		$totalServiceCallRecordSuccesses += $timestampMap->{$timeMarker}->{"successCount"};
		$totalServiceCallRecordFailures += $timestampMap->{$timeMarker}->{"failureCount"};
		$totalServiceCallRecords += $timestampMap->{$timeMarker}->{"successCount"} + $timestampMap->{$timeMarker}->{"failureCount"};
		$totalServiceCallTimePerRecord += $timeDifference /
			($timestampMap->{$timeMarker}->{"successCount"} + $timestampMap->{$timeMarker}->{"failureCount"});
		if (defined $timestampMap->{$timeMarker}->{"failedRosterId"} && defined $timestampMap->{$timeMarker}->{"failedTestId"}) {
			push(@missingRosterTests,$timestampMap->{$timeMarker}->{"failedRosterId"} . "," . $timestampMap->{$timeMarker}->{"failedTestId"}); 
		}
	}
	
	my $totalServiceMeanCallTime;
	my $totalServiceMeanCallTimePerRecord;
	if ($totalServiceCalls) {
		$totalServiceMeanCallTime = $totalServiceCallTime / $totalServiceCalls;
		$totalServiceMeanCallTimePerRecord = $totalServiceCallTimePerRecord / $totalServiceCalls;
	} else {
		$totalServiceMeanCallTime = 0;
		$totalServiceMeanCallTimePerRecord = 0;
	}

	push(@report, "\tTotal Calls from BMT: $totalServiceCalls");
	push(@report, "\tTotal Records from BMT: $totalServiceCallRecords");
	push(@report, "\tTotal Successes for BMT: $totalServiceCallRecordSuccesses");
	push(@report, "\tTotal Failures for BMT: $totalServiceCallRecordFailures");
	my $successRate;
	if ($totalServiceCallRecords) {
		$successRate = ($totalServiceCallRecordSuccesses / $totalServiceCallRecords) * 100;
	} else {
		$successRate = 0;
	}
	push(@report, sprintf("\tTotal Service Success Rate: %.2f%%", $successRate));
	push(@report, "\tTotal Service Call Time: ${totalServiceCallTime}ms");
	push(@report, sprintf("\tTotal Service Mean Call Time: %.2fms", $totalServiceMeanCallTime));
	push(@report, sprintf("\tTotal Service Mean Call Time Per Record: %.2fms", $totalServiceMeanCallTimePerRecord));
	if (scalar keys %$errorTypes > 0) {
		push(@report, "\tErrors encountered during processing:");
		foreach my $errorMessage (sort keys %$errorTypes) {
			push(@report, "\t\t$errorMessage: " . $errorTypes->{$errorMessage});
		}
	}
	if (scalar @missingRosterTests > 0)  {
		push(@report, "\tMissing Roster/Tests in OAS according to BMT:");
		for my $missing (@missingRosterTests) {
			push(@report, "\t\t$missing");
		}
	}

	return @report;
}

sub writeReport {
	my @report = @_;
	print STDOUT "\n";
	print STDOUT "--- BMTSYNC MONITORING REPORT ---\n";
	foreach my $reportLine (@report) {
		print STDOUT "$reportLine\n";
	}
}

# Logging functions. DO NOT CHANGE.

sub writeLog {
	my $level = shift @_;
	if ($level < LOG_LEVEL) {
		return;
	}

	my $message = shift @_;
	my $parent = (caller(1))[3];
	if (not defined $parent) {
		$parent = $0;
	}
	print STDERR LOG_HEADERS->[$level] . ": $parent: $message\n";
}
