package utils;

import java.util.Date;


/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssetInfo implements java.io.Serializable
{
    public byte[] data;
    public String mimeType;
    public Date createdDateTime;
    
    public AssetInfo()
    {
        super();
    }
    
    public void setExt( String ext )
    {
        mimeType = "image/gif";
        if ( "swf".equals( ext ))
            mimeType = "application/x-shockwave-flash";
        if ( "gif".equals( ext ))
            mimeType = "image/gif";
        if ( "jpg".equals( ext ))
            mimeType = "image/jpg";
    }
    
    public void setData( byte[] data_ )
    {
        data = data_;
    }
    
    public String getMIMEType()
    {
        return mimeType;
    }
    
    public byte[] getData()
    {
        return data;
    }

	/**
	 * @return the createdDateTime
	 */
	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

}

