package utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdom.input.SAXBuilder;


public enum MemoryCache {
	INSTANCE;
    static final long serialVersionUID = 1L;

    private static HashMap itemMap = new HashMap();
    private HashMap assetMap = new HashMap();
    public SAXBuilder saxBuilder =  new SAXBuilder();
    private HashMap imageMap = new HashMap();
 
    private MemoryCache() {
    	System.out.println("MemoryCache");
        clearContent();
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
    	System.out.println("clear Content");
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

