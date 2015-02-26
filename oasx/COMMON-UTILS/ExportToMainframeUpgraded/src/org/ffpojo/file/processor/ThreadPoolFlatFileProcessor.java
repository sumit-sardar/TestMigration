package org.ffpojo.file.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ffpojo.exception.RecordProcessorException;
import org.ffpojo.file.processor.record.RecordProcessor;
import org.ffpojo.file.processor.record.event.DefaultRecordEvent;
import org.ffpojo.file.processor.record.event.RecordEvent;
import org.ffpojo.file.reader.FlatFileReader;
import org.ffpojo.file.reader.RecordType;


public class ThreadPoolFlatFileProcessor extends BaseFlatFileProcessor implements FlatFileProcessor {

	private ExecutorService executor;
	
	public ThreadPoolFlatFileProcessor(FlatFileReader flatFileReader, int threadPoolSize) {
		super(flatFileReader);
		this.executor = Executors.newFixedThreadPool(threadPoolSize);
	}
	
	public void processFlatFile(final RecordProcessor processor) {
		for(final Object record : flatFileReader) {
			final RecordType recordType = flatFileReader.getRecordType();
			final RecordEvent event = new DefaultRecordEvent(record, flatFileReader.getRecordText(), flatFileReader.getRecordIndex());
			executor.execute(new Runnable() {
				public void run() {
					try {
						if (recordType == RecordType.HEADER) {
							processor.processHeader(event);
						} else if (recordType == RecordType.BODY) {
							processor.processBody(event);
						} else if (recordType == RecordType.TRAILER) {
							processor.processTrailer(event);
						}
					} catch (RecordProcessorException e) {
						try {
							errorHandler.error(e);
						} catch (RecordProcessorException exThrownByErrorHandler) {
							exThrownByErrorHandler.printStackTrace();
						}
					}
				}
			});
		}
		executor.shutdown();
		while(!executor.isTerminated()) {
			Thread.yield();
		}
	}
	
}
