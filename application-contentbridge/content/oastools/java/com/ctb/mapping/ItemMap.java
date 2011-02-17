package com.ctb.mapping;


import java.io.*;
import java.util.*;


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
            StringTokenizer toker = new StringTokenizer(s, ",\"");

            itemId = toker.nextToken().trim();
            curriculumId = toker.nextToken().trim();
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
