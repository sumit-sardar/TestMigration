package com.tcs.conversion;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.tcs.Main;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.extend.TextRenderer;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class HTMLToPDFConversion
{
	private static Logger slf4jLogger = LoggerFactory.getLogger(Main.class);

	public static void createPDFFromHTML(String htmlString, String pdfFileName, String outputFolderName) throws DocumentException, IOException { 
	String finalPDFFileName = outputFolderName + System.getProperty("file.separator") + pdfFileName + ".pdf";
	String finalPDFFileTempName = outputFolderName + System.getProperty("file.separator") + pdfFileName + "_temp.pdf";
	File outputDirectory = new File(outputFolderName);
	if ((!outputDirectory.exists()) || (!outputDirectory.isDirectory())) {
		outputDirectory.mkdir();
	}
	File tempHtmlFile = new File(Main.BASE_DIR + System.getProperty("file.separator") + "temp" + 
			System.getProperty("file.separator") + pdfFileName + ".html");

	File finalPDFTemp = new File(finalPDFFileTempName);
	if (finalPDFTemp.exists())
		finalPDFTemp.delete();
	File finalPDF = new File(finalPDFFileName);
	if (finalPDF.exists()) {
		finalPDF.delete();
	}
	FileOutputStream stream = new FileOutputStream(tempHtmlFile);
	stream.write(htmlString.getBytes());

	OutputStream os = new FileOutputStream(finalPDFTemp);

	ITextRenderer renderer = new ITextRenderer();
	SharedContext sharedContext = renderer.getSharedContext();
	sharedContext.setPrint(true);
	sharedContext.setInteractive(false);

	sharedContext.setReplacedElementFactory(new B64ImgReplacedElementFactory());
	sharedContext.getTextRenderer().setSmoothingThreshold(0.0F);
	renderer.setDocument(tempHtmlFile);

	renderer.getFontResolver().addFont("StaticData/fonts/verdana.ttf", "Identity-H", false);
	renderer.getFontResolver().addFont("StaticData/fonts/verdanab.ttf", "Identity-H", false);
	renderer.getFontResolver().addFont("StaticData/fonts/verdanai.ttf", "Identity-H", false);
	renderer.getFontResolver().addFont("StaticData/fonts/verdanaz.ttf", "Identity-H", false);

	renderer.layout();
	renderer.createPDF(os);

	os.close();

	removeBlankPdfPages(finalPDFFileTempName, finalPDFFileName);
	slf4jLogger.info("Generated PDF :: " + finalPDFFileName);
	if (tempHtmlFile.exists())
		tempHtmlFile.delete();
	if (finalPDFTemp.exists())
		finalPDFTemp.delete(); }

	public static void removeBlankPdfPages(String pdfSourceFile, String pdfDestinationFile)
	{
		int blankPdfSize = 510;
		try
		{
			PdfReader r = new PdfReader(pdfSourceFile);
			RandomAccessFileOrArray raf = new RandomAccessFileOrArray(pdfSourceFile);
			Document document = new Document(r.getPageSizeWithRotation(1));

			PdfCopy writer = new PdfCopy(document, new FileOutputStream(pdfDestinationFile));

			document.open();

			PdfImportedPage page = null;

			for (int i = 1; i <= r.getNumberOfPages(); i++)
			{
				byte[] bContent = r.getPageContent(i, raf);
				ByteArrayOutputStream bs = new ByteArrayOutputStream();

				bs.write(bContent);

				if (bs.size() > blankPdfSize)
				{
					page = writer.getImportedPage(r, i);
					writer.addPage(page);
				}
				bs.close();
			}

			document.close();
			writer.close();
			raf.close();
			r.close();
		}
		catch (Exception localException)
		{
		}
	}

	public static void modifyPdf(PdfStamper stamper) throws BadElementException {
		PdfReader reader = stamper.getReader();

		PdfContentByte under = null;

		PdfPTable header = null;
		PdfPTable footer = null;

		int total = reader.getNumberOfPages();
		for (int page = 1; page <= total; page++) {
			under = stamper.getUnderContent(page);

			PdfDocument doc = under.getPdfDocument();
			Rectangle rect = reader.getPageSizeWithRotation(page);

			header = new PdfPTable(1);
			header.addCell(getTableCell("Header"));
			header.setTotalWidth(200.0F);

			footer = new PdfPTable(1);
			header.addCell(getTableCell("Footer"));
			footer.setTotalWidth(200.0F);

			float x = 0.0F;

			if (header != null) {
				float y = rect.getTop() - 0.0F;
				header.writeSelectedRows(0, -1, 0.0F, y, under);
			}

			if (footer != null) {
				float y = rect.getBottom() + 20.0F;
				footer.writeSelectedRows(0, -1, 0.0F, y, under);
			}
		}
	}

	public static PdfPCell getTableCell(String text) throws BadElementException {
		if (text == null)
			text = "";
		Chunk chunk = new Chunk(text);

		Paragraph paragraph = new Paragraph();
		paragraph.add(chunk);
		PdfPCell result = new PdfPCell(paragraph);

		result.setBorder(0);

		result.setPaddingLeft(10.0F);
		result.setPaddingBottom(1.0F);

		return result;
	}
}

