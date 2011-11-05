package com.ctb.tms.nosql.coherence.push;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.oracle.coherence.patterns.pushreplication.EntryOperation;
import com.oracle.coherence.patterns.pushreplication.publishers.cache.ConflictResolution;
import com.oracle.coherence.patterns.pushreplication.publishers.cache.ConflictResolver;
import com.tangosol.util.BinaryEntry;

public class TMSConflictResolver implements ConflictResolver {

	public TMSConflictResolver() {
    }

    public ConflictResolution resolve(EntryOperation entryOperation, BinaryEntry localEntry) {
        ConflictResolution resolution = new ConflictResolution();

        if (!(localEntry.isPresent())) {
            switch (entryOperation.getOperation())
            {
                case Delete:
                    resolution.useLocalValue();
                    break;
                case Insert:
                	resolution.useInComingValue();
                    break;
                case Update:
                	resolution.useLocalValue();
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
                	if("OASRosterCache".equals(entryOperation.getCacheName())) {
                		RosterData incoming = (RosterData) localEntry.getContext().getValueFromInternalConverter().convert(entryOperation.getPublishableEntry().getBinaryValue());
                		RosterData current = (RosterData) localEntry.getValue();
                		if(incoming.getAuthData().getLastMseq() > current.getAuthData().getLastMseq()) {
                			resolution.useInComingValue();
                		} else {
                			resolution.useLocalValue();
                		}
                	} else if("OASManifestCache".equals(entryOperation.getCacheName())) {
                		Manifest[] incoming = (Manifest[]) localEntry.getContext().getValueFromInternalConverter().convert(entryOperation.getPublishableEntry().getBinaryValue());
                		Manifest[] current = (Manifest[]) localEntry.getValue();
                		if(incoming == null || incoming.length <= 0) {
                			resolution.useLocalValue();
                		} else if(current == null || current.length <= 0) {
                			resolution.useInComingValue();
                		} else {
	                		if(incoming[0].getRosterLastMseq() > current[0].getRosterLastMseq()) {
	                			resolution.useInComingValue();
	                		} else {
	                			resolution.useLocalValue();
	                		}
                		}
                	} else {
                		resolution.useInComingValue();
                	}
                    break;
                }
            }
        }
        return resolution;
    }
}
