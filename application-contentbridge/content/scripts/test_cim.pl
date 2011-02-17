# run me like this: perl -I ../teamsite/iw-home/iw-perl/lib/perl5/site_perl/5.005 test_cim.pl

use strict;

push (@INC, '../teamsite/iw-home/iw-perl/lib/perl5/site_perl/5.005');
print "foo\n";

use CTB::CIM;

# not a unit test -- just prints it for sanity

print &CTB::CIM::logline("123", "4R.1.1.1.01", "FOP", "0");
print &CTB::CIM::logline("123", "4R.1.1.1.01", "CIM", "256", "Bad body odor");
print &CTB::CIM::logline("123", "4R.1.1.1.02", "FOP", "0");
print &CTB::CIM::logline("123", "4R.1.1.1.02", "CIM", "0");
