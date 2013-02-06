/*
 * Created on Nov 2, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ctb.contentBridge.core.publish.roundtrip;

import com.ctb.contentBridge.core.publish.media.Media;

/**
 * @author wmli
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface MediaReader {
	public Media readMedia(String itemId);
}
