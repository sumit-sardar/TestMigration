package com.ctb.util;


import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Oct 1, 2003
 * Time: 9:33:20 AM
 * To change this template use Options | File Templates.
 */
public class NodeFilterChain implements JDOMNodeFilter {

    private ArrayList filters = new ArrayList();

    public void addFilter(JDOMNodeFilter filter) {
        filters.add(filter);
    }

    public Collection filterNodes(Collection nodes) {
        Collection filteredList = nodes;

        for (Iterator iter = filters.iterator(); iter.hasNext();) {
            filteredList = ((JDOMNodeFilter) iter.next()).filterNodes(filteredList);
        }
        return filteredList;
    }
}
