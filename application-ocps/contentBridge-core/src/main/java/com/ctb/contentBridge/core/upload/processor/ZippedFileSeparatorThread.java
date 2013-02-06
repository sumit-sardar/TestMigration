package com.ctb.contentBridge.core.upload.processor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.ItemSet;
import com.ctb.contentBridge.core.util.ClosableHelper;
import com.ctb.contentBridge.core.util.FileUtil;


public class ZippedFileSeparatorThread extends StopableThread {
	private ItemSet itemSetTD;
	private FtpProcessorThread ftpProcessorThread ;
	private CTBQueue queue;
	private Map trackerIDMap = new TreeMap();
	private Configuration configuration ;
	public ZippedFileSeparatorThread(Configuration configuration,
			ItemSet itemSetTD) {
		super("ZippedFileSeparatorThread-" + itemSetTD.getAdsid());
		this.itemSetTD = itemSetTD;
		queue = new CTBQueue();
		this.configuration = configuration;
		ftpProcessorThread = new FtpProcessorThread(configuration, itemSetTD, queue );

	}

	public void run() {
		String zippedFileName = itemSetTD.getAdsid() + "$" + itemSetTD.getHash() ;
		String trackerFileName = itemSetTD.getAdsid() + "$"	+ itemSetTD.getHash() + ".xml";
		try {
			ftpProcessorThread.start();
			String xml = separateZippedFile(zippedFileName);
			FileUtil.saveToFile(configuration.getLocalFilePath(), trackerFileName, xml.getBytes());
			FileUtil.deleteFile(configuration.getLocalFilePath(), zippedFileName+ ".zip");
			queue.put( trackerFileName);
			ftpProcessorThread.setFinish(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			StopableThread.foreStopped = true;
		}
	}

	private String separateZippedFile(String zippedFileName) throws IOException {
		byte[] buffer = new byte[8 * 1024];
		int minFileLength = 524285;
		String subtestidValue = zippedFileName;
		String compressedFile = subtestidValue+ ".zip";
		Map trackerMap = new TreeMap();
		
		BufferedInputStream input = new BufferedInputStream(
				new FileInputStream(new File(configuration.getLocalFilePath(), compressedFile)));
		int indx = 0;
		String fileName= getFilename(subtestidValue, ++indx);
		FileOutputStream output = FileUtil.getFileOutputStream(configuration.getLocalFilePath(), fileName);
		long count = 0;
		boolean isfileTracked = false;
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			if(foreStopped) {
				break;
			}
				
			if (count > minFileLength) {
				ClosableHelper.close(output);
				queue.put( fileName);
				
				if (!isfileTracked) {
					//fileName = getFilename(subtestidValue, indx);
					trackerMap.put(new Integer(indx), fileName );
					
				}
				trackerIDMap.put(new Integer(indx), new Integer(indx + 1));
				fileName = getFilename(subtestidValue, ++indx);
				output = FileUtil.getFileOutputStream(configuration.getLocalFilePath(), fileName);
				trackerMap.put(new Integer(indx), fileName);
				trackerIDMap.put(new Integer(indx), null);

				count = 0;
				isfileTracked = true;
			}
			count += bytesRead;
			output.write(buffer, 0, bytesRead);
		}
		ClosableHelper.close(output);
		ClosableHelper.close(input);
		queue.put( fileName);

		if (!isfileTracked) {

			trackerMap
					.put(new Integer(indx), getFilename(subtestidValue, indx));
			trackerIDMap.put(new Integer(indx), null);
		}

		return generateTracker(subtestidValue, trackerMap, trackerIDMap);
	}

	private String getFilename(String subtestidValue, int i)
			throws FileNotFoundException {
		return subtestidValue + ".part"+  "." +i ;
	}


	private String generateTracker(String subtestidValue, Map trackerMap,
			Map trackerIDMap) {
		SimpleDateFormat format =  new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss zzz");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xml += "<content_tracker>";
		xml += "<created_date>" + format.format(new Date()) + "</created_date>";
		Integer start = new Integer(1);

		do {
			
			xml += "<tracker ";
			xml += "sequence_number = \"" + start.intValue()+ "\" ";
			xml += " value = \"" + trackerMap.get(start)+ "\" ";
			start = (Integer) trackerIDMap.get(start);
			xml +=  " next = \"" + ((start == null) ? "NULL" : String.valueOf(start)) + "\" ";;
			xml += ">"	;
				
			xml += "</tracker>";
		} while (start != null);
		xml += "</content_tracker>";

		return xml;
	}

}
