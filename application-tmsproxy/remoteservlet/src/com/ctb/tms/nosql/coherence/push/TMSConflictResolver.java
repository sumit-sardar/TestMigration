package com.ctb.tms.nosql.coherence.push;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ctb.tms.bean.login.Manifest;
import com.ctb.tms.bean.login.RosterData;
import com.oracle.coherence.patterns.pushreplication.EntryOperation;
import com.oracle.coherence.patterns.pushreplication.publishers.cache.ConflictResolution;
import com.oracle.coherence.patterns.pushreplication.publishers.cache.ConflictResolver;
import com.tangosol.util.BinaryEntry;

public class TMSConflictResolver implements ConflictResolver {

	static Logger logger = Logger.getLogger(TMSConflictResolver.class);
	
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
                	resolution.useInComingValue();
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
                			logger.warn("Replicated roster message has lower mseq than current local value - ignoring.");
                		}
                	} else if("OASManifestCache".equals(entryOperation.getCacheName())) {
                		Manifest[] incoming = (Manifest[]) localEntry.getContext().getValueFromInternalConverter().convert(entryOperation.getPublishableEntry().getBinaryValue());
                		Manifest[] current = (Manifest[]) localEntry.getValue();
                		HashMap<String, Manifest> manifestMap = new HashMap<String, Manifest>(incoming.length + current.length);	
                		for(int i=0;i<current.length;i++) {
                			String key = current[i].getAccessCode();
                			manifestMap.put(key, current[i]);
                		}
                		for(int i=0;i<incoming.length;i++) {
                			String key = incoming[i].getAccessCode();
                			Manifest manifest = manifestMap.get(key);
                			if(manifest == null) {
                				manifestMap.put(key, incoming[i]);
                			} else if(incoming[i].getRosterLastMseq() > manifest.getRosterLastMseq()) {
                				manifestMap.put(key, incoming[i]);
                			} else if (incoming[i].doReplicate()) {
                				incoming[i].setReplicate(false);
                				manifestMap.put(key, incoming[i]);
                			} else {
                				logger.warn("Replicated manifest message has lower mseq than current local value - ignoring.");
                			}
                		}
                		Manifest [] newManifest = manifestMap.values().toArray(new Manifest[0]);
                		resolution.useMergedValue(newManifest);
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
