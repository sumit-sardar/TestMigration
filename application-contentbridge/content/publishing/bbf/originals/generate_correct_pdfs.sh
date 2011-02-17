LOG_FILENAME="logs/generate_correct_pdfs.log"
>$LOG_FILENAME
echo "starting at `date`" >>$LOG_FILENAME
for i in `cd xml; ls *.xml`
do
for j in `cd xsl; ls FOP*.xsl`
do
./generate_correct_pdf.sh $i $j >>$LOG_FILENAME
echo "finished at `date`" >>$LOG_FILENAME
done
done
