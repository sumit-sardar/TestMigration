package utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom.input.SAXBuilder;


public enum MemoryCache {
	INSTANCE;
    static final long serialVersionUID = 1L;

    private HashMap itemMap;
    private HashMap assetMap;
    public SAXBuilder saxBuilder;
    private HashMap imageMap;
    
 
	private MemoryCache() {
    
        clearContent();
        saxBuilder = new SAXBuilder();
        this.imageMap = new HashMap();
    }

    public static MemoryCache getInstance() {
    	synchronized(MemoryCache.class) {
    		return MemoryCache.INSTANCE;
    	}
    }


    public HashMap getImageMap() {
    	synchronized(this) {
    		return imageMap;
    	}
    }

    public void setImageMap(HashMap imageMap) {
    	synchronized(this) {
    		this.imageMap = imageMap;
    	}
    }
    
     
    public void clearContent()
    {
    	synchronized(this) {
    		itemMap = new HashMap();
    		assetMap = new HashMap();
    	}
    }
    
    public HashMap getAssetMap()
    {
    	synchronized(this) {
    		return assetMap;
    	}
    }
    
    public HashMap getItemMap()
    {
    	synchronized(this) {
    		return itemMap;
    	}
    }
    
    
}

