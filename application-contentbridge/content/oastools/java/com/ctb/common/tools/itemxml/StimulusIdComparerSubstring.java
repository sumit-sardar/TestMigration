/*
 * Created on Dec 9, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.util.Set;

public class StimulusIdComparerSubstring extends AbstractStimulusIdComparer {

    public StimulusIdComparerSubstring(Set set) {
        super(set);
    }

    protected boolean matches(String element, String string) {
        return (
            ((element != null) && (element.length() != 0))
                && ((string != null) && (string.length() != 0))
                && ((element.startsWith(string)) || (string.startsWith(element))));
    }

}
