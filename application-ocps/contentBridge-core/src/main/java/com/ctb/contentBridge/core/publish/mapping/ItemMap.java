package com.ctb.contentBridge.core.publish.mapping;


import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class ItemMap extends Loader {

    public static class Entry {

        private String itemId;
        private String curriculumId;

        public Entry(String itemId, String curriculumId) {
            this.itemId = itemId;
            this.curriculumId = curriculumId;
        }

        public Entry(String inputLine) {
            tokenize(inputLine);
        }

        private void tokenize(String s) {
        	try {
	            StringTokenizer toker = new StringTokenizer(s, ",\"");
	
	            itemId = toker.nextToken().trim();
	            curriculumId = toker.nextToken().trim();
        	} catch (Exception e) {
        		String message = "*****  Problem tokenizing item_map entry: " + s;
        		System.out.println(message);
        		throw new java.util.NoSuchElementException(message);
        	}
        }

        public String getItemId() {
            return itemId;
        }

        public String getCurriculumId() {
            return curriculumId;
        }
    }

    protected SortedMap map = new TreeMap();
    protected Set duplicates = new HashSet();

    public ItemMap() {}

    public ItemMap(File file) {
        load(file);
    }

    public ItemMap(Reader in) {
        load(in);
    }

    public ItemMap(String contents) {
        load(new StringReader(contents));
    }

    protected void addLine(String line) {
        Entry entry = new Entry(line);

        add(entry);
    }

    public void put(String itemId,String curriculumId) {
        add(new Entry(itemId,curriculumId));
    }
    public void add(Entry entry) {
        if (invalidEntry(entry)) {
                duplicates.add(entry.itemId);
        }
        map.put(entry.getItemId(), entry.getCurriculumId());
    }

    public String curriculumId(String itemId) {
        if (map.containsKey(itemId)) {
            return (String) map.get(itemId);
        } else {
            return null;
        }
    }

    public Iterator getAllItemIDs() {
        return map.keySet().iterator();
    }

    public Iterator getAllCurriculumIDs() {
        return map.values().iterator();
    }

    public Set getDuplicateItemIDs() {
        return duplicates;
    }
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    private boolean invalidEntry(Entry entry) {

        boolean invalid = map.containsKey(entry.getItemId());
        if (invalid) {
            String existingObjective = (String) map.get(entry.getItemId());
            invalid = !(entry.getCurriculumId().equals(existingObjective));
        }
        return invalid;
    }
    public void addAll(ItemMap fromMap) {
        map.putAll(fromMap.map);
    }
    public void remove(String id) {
        map.remove(id);
    }
    public long size() {
        return map.size();
    }
}
