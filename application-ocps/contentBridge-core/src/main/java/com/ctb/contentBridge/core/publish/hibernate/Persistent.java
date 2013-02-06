package com.ctb.contentBridge.core.publish.hibernate;

/**
 * @author wmli
 */
public class Persistent {
    private boolean _saved = false;
    
    public void onSave() {
        _saved = true;
    }
    
    public void onLoad() {
        _saved = true;
    }
    
    public boolean isSaved() {
        return _saved;
    }
}