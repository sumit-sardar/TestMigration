package com.ctb.common.teamsite;


import java.util.*;

import org.jdom.*;

import com.ctb.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Oct 1, 2003
 * Time: 9:33:20 AM
 * State specific items end in a state code number. this filter ensures that only items with the appropriate
 * state code are processed
 */
public class StateNumberCodeFilter implements JDOMNodeFilter {

    private String numberCode;

    public StateNumberCodeFilter(String numberCode) {
        this.numberCode = numberCode;
    }

    public Collection filterNodes(Collection nodes) {
        List filteredList = filterForStateNumberCode(nodes);

        return filteredList;
    }

    /**
     * Filters any Items whose ID's do not end in the numberCode from the element list
     * @param nodes list of Objects returned from a query
     * @return list of JDOM Elements
     */
    private List filterForStateNumberCode(Collection nodes) {
        List elements = new ArrayList();

        for (Iterator iter = nodes.iterator(); iter.hasNext();) {
            Object node = iter.next();

            if (node instanceof Element
                    && ((Element) node).getAttributeValue("ID").endsWith(numberCode)) {
                elements.add(node);
            }
        }
        return elements;
    }
}
