package com.ctb.tms.nosql.coherence.push;

import org.apache.log4j.Logger;
import org.gridkit.coherence.utils.pof.ReflectionPofExtractor;

import com.ctb.tms.bean.login.ReplicationObject;
import com.oracle.coherence.patterns.pushreplication.EntryOperation;
import com.oracle.coherence.patterns.pushreplication.PublishableEntry;
import com.oracle.coherence.patterns.pushreplication.publishers.cache.ConflictResolution;
import com.oracle.coherence.patterns.pushreplication.publishers.cache.ConflictResolver;
import com.tangosol.util.BinaryEntry;

public class TMSConflictResolver implements ConflictResolver {

	static Logger logger = Logger.getLogger(TMSConflictResolver.class);
	
	static ReflectionPofExtractor extractor = new ReflectionPofExtractor("cacheTime");
	
	public TMSConflictResolver() {
    }

    public ConflictResolution resolve(EntryOperation entryOperation, BinaryEntry localEntry) {
        ConflictResolution resolution = new ConflictResolution();
        
        ReplicationObject merged = null;

        if (!(localEntry.isPresent())) {
            switch (entryOperation.getOperation())
            {
                case Delete:
                    merged = (ReplicationObject) localEntry.getValue();
                    merged.setReplicate(false);
                    resolution.useMergedValue(merged);
                    break;
                case Insert:
                	merged = (ReplicationObject) entryOperation.getPublishableEntry().getValue();
                	merged.setReplicate(false);
                	resolution.useMergedValue(merged);
                    break;
                case Update:
                	merged = (ReplicationObject) entryOperation.getPublishableEntry().getValue();
                	merged.setReplicate(false);
                	resolution.useMergedValue(merged);
                    break;
            }
        }
        else {
            switch (entryOperation.getOperation())
            {
                case Delete:
                	resolution.remove();
                    break;
                case Insert:
                case Update:
                { 
                	long localTime = (Long) extractor.extractFromEntry(localEntry);
                	long inTime = (Long) extractor.extractFromEntry(entryOperation.getPublishableEntry());
                	if(inTime > localTime) {
                		logger.debug("Using newer INCOMING message.");
                		merged = (ReplicationObject) entryOperation.getPublishableEntry().getValue();
                	} else {
                		logger.debug("Using newer LOCAL message.");
                		merged = (ReplicationObject) localEntry.getValue();
                	}
                	merged.setReplicate(false);
                    resolution.useMergedValue(merged);
                    break;
                }
            }
        }
        
        return resolution;
    }
}
