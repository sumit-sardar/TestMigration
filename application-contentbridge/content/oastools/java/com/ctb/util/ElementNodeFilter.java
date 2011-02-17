package com.ctb.util;


import java.util.*;

import org.jdom.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Oct 1, 2003
 * Time: 9:33:20 AM
 * To change this template use Options | File Templates.
 */
public class ElementNodeFilter implements JDOMNodeFilter {

    public Collection filterNodes(Collection nodes) {
        Collection filteredNodes = filterNonElements(nodes);

        return filteredNodes;
    }

    /**
     * Filters any non element nodes from the element list used in creating the partitions
     * @param nodes list of Objects returned from a query
     * @return list of JDOM Elements
     */
    private Collection filterNonElements(Collection nodes) {
        List elements = new ArrayList();

        for (Iterator iter = nodes.iterator(); iter.hasNext();) {
            Object node = iter.next();

            if (node instanceof Element) {
                elements.add(node);
            }
        }
        return elements;
    }

}
