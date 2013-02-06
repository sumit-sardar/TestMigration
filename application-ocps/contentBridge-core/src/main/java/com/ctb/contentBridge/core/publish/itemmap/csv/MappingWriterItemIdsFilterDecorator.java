/*
 * Created on Nov 19, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ctb.contentBridge.core.publish.itemmap.csv;

import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author wmli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MappingWriterItemIdsFilterDecorator implements MappingWriter {
    MappingWriter writer;
    Set filterItemIds;

    public MappingWriterItemIdsFilterDecorator(
        MappingWriter writer,
        Set filterItemIds) {
        this.writer = writer;
        this.filterItemIds = filterItemIds;
    }

    public void writeLine(String line) {
        // extract the item id from the line        
        if (line.trim().equals("")) return;
        
        StringTokenizer toker = new StringTokenizer(line, ",\"");
        String itemId = toker.nextToken().trim();
        String curriculumId = toker.nextToken().trim();

        if (filterItemIds == null || !filterItemIds.contains(itemId)) {
            writer.writeLine(line);
        }
    }

    public void close() {
        writer.close();
    }

}
