package com.tcs.conversion;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MergerPDFApp
{
	public static void main(String[] args)
	{
		List list = new ArrayList();
		try
		{
			list.add(new FileInputStream(new File("StaticData/pdf/1.pdf")));
			list.add(new FileInputStream(new File("StaticData/pdf/2.pdf")));

			OutputStream out = new FileOutputStream(new File("StaticData/pdf/result.pdf"));

			doMerge(list, out);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void doMerge(List<InputStream> list, OutputStream outputStream)
			throws DocumentException, IOException
			{
		Rectangle a4 = PageSize.A4;
		Rectangle a4LandScape = a4.rotate();

		Document document = new Document();
		document.setPageSize(a4LandScape);
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();
		PdfContentByte cb = writer.getDirectContent();

		for (InputStream in : list) {
			PdfReader reader = new PdfReader(in);
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				document.newPage();

				PdfImportedPage page = writer.getImportedPage(reader, i);

				cb.addTemplate(page, 0.0F, 0.0F);
			}
		}

		outputStream.flush();
		document.close();
		outputStream.close();
			}
}

