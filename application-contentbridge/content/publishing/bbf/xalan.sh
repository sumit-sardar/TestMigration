CLASSPATH=/usr/java/jre/lib:/usr/java/jre/lib/rt.jar:/usr/java/jre/lib/ext:/usr/java/jre/lib/servlet.jar:/usr/java/jre/lib/ext/xerces.jar:/export/fop/fop-0.20.4/lib/avalon-framework-cvs-20020315.jar:/export/fop/fop-0.20.4/lib/batik.jar:/export/fop/fop-0.20.4/lib/bsf.jar:/export/fop/fop-0.20.4/lib/buildtools.jar:/export/fop/fop-0.20.4/lib/stylebook.jar:/export/fop/fop-0.20.4/lib/xalan-2.3.1.jar:/export/fop/fop-0.20.4/lib/xercesImpl-2.0.1.jar:/export/fop/fop-0.20.4/lib/xml-apis.jar:/export/fop/fop-0.20.4/build/fop.jar:$CLASSPATH
export CLASSPATH
../../hyphenspace.pl $1

TMPFILE=`echo $1.tmp | /usr/bin/sed -e 's/.xml.tmp/.tmp/'`
mv $1.tmp $TMPFILE

FOFILE=`echo $TMPFILE | /usr/bin/sed -e 's/.tmp/.fo/'`
echo "Transforming $TMPFILE with $2 to get $3"
java org.apache.xalan.xslt.Process -in $TMPFILE -xsl $2 -out $3 $4
rm $TMPFILE
