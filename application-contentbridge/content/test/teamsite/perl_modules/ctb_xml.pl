 #!C:\iw-home\bin\perl -Tw
    use Test;
    BEGIN { plan tests => 159 };
    
    use CTB::XML;
    
    my $file;
    my @files = qw(
    	Job_Documents/5sc01.xml
	Job_Documents/4sc02.xml
	Job_Documents/5sc02.xml
	Job_Documents/5sc03.xml
	Job_Documents/5sc04.xml
	Job_Documents/4sc03.xml
	Job_Documents/7sc01.xml
	Job_Documents/7sc02.xml
	Job_Documents/6sc01.xml
	Job_Documents/7sc03.xml
	Job_Documents/7sc04.xml
	Job_Documents/6sc02.xml
	Job_Documents/8sc01.xml
	Job_Documents/8sc02.xml
	Job_Documents/6sc03.xml
	Job_Documents/8sc03.xml
	Job_Documents/3sc01.xml
	Job_Documents/3sc02.xml
	Job_Documents/3sc03.xml
	Job_Documents/3sc04.xml
	Job_Documents/5sc05.xml
	Job_Documents/BAD10781.xml
	Job_Documents/4sc01.xml
	Job_Documents/4sc05_CR_CAB_OAS.xml
	Job_Documents/4sc06_CR_CAB_OAS.xml
	Job_Documents/10sc02_SR_WV_OAS.xml
	Job_Documents/10sc03_SR_WV_OAS.xml
	Job_Documents/3sc05_CR_CAB_OAS.xml
	Job_Documents/5sc06_CR_CAB_OAS.xml
	Job_Documents/5sc07_CR_CAB_OAS.xml
	Job_Documents/7sc05_CR_CAB_OAS.xml
	Job_Documents/7sc07_CR_CAB_OAS.xml
	Job_Documents/8sc04_CR_CAB_OAS.xml
	Job_Documents/8sc05_CR_CAB_OAS.xml
	Job_Documents/8sc06_CR_CAB_OAS.xml
	Job_Documents/10sc01_SR_WV_OAS.xml
	Job_Documents/5sc08_SR_WV_OAS.xml
	Job_Documents/5sc09_SR_WV_OAS.xml
	Job_Documents/8sc07_SR_WV_OAS.xml
	Job_Documents/8sc08_SR_WV_OAS.xml
	Job_Documents/7sc08_SR_W_OAS.xml
	Job_Documents/7sc09_SR_WV_OAS.xml
	Job_Documents/4sc07_SR_WV_OAS.xml
	Job_Documents/4sc08_SR_WV_OAS.xml
	Job_Documents/4sc09_SR_WV_OAS.xml
	Job_Documents/10sc04_SR_WV_OAS.xml
	Job_Documents/6sc04_SR_WV_OAS.xml
	Job_Documents/6sc05_SR_WV_OAS.xml
	Job_Documents/3sc06_SR_WV_OAS.xml
	Job_Documents/3sc07_SR_WV_OAS.xml
	Job_Documents/3sc08_SR_WV_OAS.xml
	Job_Documents/4sc04.xml);
    	
    foreach $file (@files) {
    	my ($gi, $grade, %items) = CTB::XML::get_items("$file", "/default/main/CAB/SCIENCE/WORKAREA/load");
    	my $num_keys = keys %items;
    	
    	ok( $num_keys != 0 );
    	ok( $gi ne "####");
    	ok( $grade ne "-1" );
    	
    	my ($k, $v);
    	while ( ($k,$v) = each %items ) {
        print "$k => $v\n";
    	}
    }

