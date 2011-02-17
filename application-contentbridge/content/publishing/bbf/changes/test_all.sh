OUT_FILENAME="test_all.out"
>$OUT_FILENAME
for i in `cd ../originals/xml; ls *.xml`
do
for j in `cd xsl; ls FOP*.xsl`
do
./test.sh $i $j >>$OUT_FILENAME
done
done
FAILED_COUNT=`grep "FAILED" $OUT_FILENAME | wc -l`
if [ $FAILED_COUNT -gt 0 ]
then
echo "FAILED: $FAILED_COUNT failures (see $OUT_FILENAME)"
else
echo "SUCCESS"
fi
