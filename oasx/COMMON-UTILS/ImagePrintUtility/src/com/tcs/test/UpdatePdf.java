/*     */ package com.tcs.test;
/*     */ 
/*     */ import com.lowagie.text.BadElementException;
/*     */ import com.lowagie.text.Chunk;
/*     */ import com.lowagie.text.DocumentException;
/*     */ import com.lowagie.text.Paragraph;
/*     */ import com.lowagie.text.Rectangle;
/*     */ import com.lowagie.text.pdf.PdfContentByte;
/*     */ import com.lowagie.text.pdf.PdfDocument;
/*     */ import com.lowagie.text.pdf.PdfPCell;
/*     */ import com.lowagie.text.pdf.PdfPTable;
/*     */ import com.lowagie.text.pdf.PdfReader;
/*     */ import com.lowagie.text.pdf.PdfStamper;
/*     */ import com.tcs.conversion.B64ImgReplacedElementFactory;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import org.xhtmlrenderer.extend.TextRenderer;
/*     */ import org.xhtmlrenderer.layout.SharedContext;
/*     */ import org.xhtmlrenderer.pdf.ITextRenderer;
/*     */ 
/*     */ public class UpdatePdf
/*     */ {
/*     */   public static void main(String[] args)
/*     */     throws DocumentException, IOException
/*     */   {
/*  29 */     createPDFFromHTML("OASCC0016338238", "c:\\reports");
/*     */   }
/*     */   public static void createPDFFromHTML(String pdfFileName, String outputFolderName) throws DocumentException, IOException {
/*  32 */     String finalPDFFileName = outputFolderName + System.getProperty("file.separator") + pdfFileName + ".pdf";
/*  33 */     String finalPDFFileTempName = outputFolderName + System.getProperty("file.separator") + pdfFileName + "_temp.pdf";
/*  34 */     File outputDirectory = new File(outputFolderName);
/*  35 */     if ((!outputDirectory.exists()) || (!outputDirectory.isDirectory())) {
/*  36 */       outputDirectory.mkdir();
/*     */     }
/*  38 */     File tempHtmlFile = new File("c:\\reports" + System.getProperty("file.separator") + "temp" + 
/*  39 */       System.getProperty("file.separator") + "\\" + pdfFileName + ".html");
/*     */ 
/*  41 */     File finalPDFTemp = new File(finalPDFFileTempName);
/*  42 */     if (finalPDFTemp.exists())
/*  43 */       finalPDFTemp.delete();
/*  44 */     File finalPDF = new File(finalPDFFileName);
/*  45 */     if (finalPDF.exists()) {
/*  46 */       finalPDF.delete();
/*     */     }
/*     */ 
/*  49 */     OutputStream os = new FileOutputStream(new File(finalPDFFileTempName));
/*     */ 
/*  51 */     ITextRenderer renderer = new ITextRenderer();
/*     */ 
/*  53 */     SharedContext sharedContext = renderer.getSharedContext();
/*  54 */     sharedContext.setPrint(true);
/*  55 */     sharedContext.setInteractive(false);
/*     */ 
/*  57 */     sharedContext.setReplacedElementFactory(new B64ImgReplacedElementFactory());
/*  58 */     sharedContext.getTextRenderer().setSmoothingThreshold(0.0F);
/*  59 */     renderer.setDocument(tempHtmlFile);
/*     */ 
/*  61 */     renderer.layout();
/*  62 */     renderer.createPDF(os);
/*     */ 
/*  64 */     os.close();
/*     */ 
/*  66 */     PdfReader reader = new PdfReader(finalPDFFileTempName);
/*  67 */     os = new FileOutputStream(new File(finalPDFFileName));
/*  68 */     PdfStamper stamper = new PdfStamper(reader, os);
/*  69 */     modifyPdf(stamper);
/*  70 */     os.close();
/*  71 */     System.out.println("Generated PDF :: " + finalPDFFileName);
/*     */   }
/*     */ 
/*     */   public static void modifyPdf(PdfStamper stamper) throws BadElementException {
/*  75 */     PdfReader reader = stamper.getReader();
/*     */ 
/*  77 */     PdfContentByte under = null;
/*     */ 
/*  79 */     PdfPTable header = null;
/*  80 */     PdfPTable footer = null;
/*     */ 
/*  82 */     int total = reader.getNumberOfPages();
/*  83 */     for (int page = 1; page <= total; page++) {
/*  84 */       under = stamper.getUnderContent(page);
/*     */ 
/*  86 */       PdfDocument doc = under.getPdfDocument();
/*  87 */       Rectangle rect = reader.getPageSizeWithRotation(page);
/*     */ 
/*  89 */       header = new PdfPTable(1);
/*  90 */       header.addCell(getTableCell("Header"));
/*  91 */       header.setTotalWidth(200.0F);
/*     */ 
/*  93 */       footer = new PdfPTable(1);
/*  94 */       header.addCell(getTableCell("Footer"));
/*  95 */       footer.setTotalWidth(200.0F);
/*     */ 
/*  97 */       float x = 0.0F;
/*     */ 
/* 100 */       if (header != null) {
/* 101 */         float y = rect.getTop() - 0.0F;
/* 102 */         header.writeSelectedRows(0, -1, 0.0F, y, under);
/*     */       }
/*     */ 
/* 106 */       if (footer != null) {
/* 107 */         float y = rect.getBottom() + 20.0F;
/* 108 */         footer.writeSelectedRows(0, -1, 0.0F, y, under);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static PdfPCell getTableCell(String text) throws BadElementException {
/* 113 */     if (text == null)
/* 114 */       text = "";
/* 115 */     Chunk chunk = new Chunk(text);
/*     */ 
/* 117 */     Paragraph paragraph = new Paragraph();
/* 118 */     paragraph.add(chunk);
/* 119 */     PdfPCell result = new PdfPCell(paragraph);
/*     */ 
/* 121 */     result.setBorder(0);
/*     */ 
/* 124 */     result.setPaddingLeft(10.0F);
/* 125 */     result.setPaddingBottom(1.0F);
/*     */ 
/* 127 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Users\522912.INDIA\Desktop\reports.jar
 * Qualified Name:     com.tcs.test.UpdatePdf
 * JD-Core Version:    0.6.0
 */