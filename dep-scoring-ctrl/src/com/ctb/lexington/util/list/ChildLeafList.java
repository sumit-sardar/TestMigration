/*
 * childLeafList.java
 *
 * Created on March 13, 2002, 1:48 PM
 */

package com.ctb.lexington.util.list;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
 
/**
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 * @author  Mary Phelps
 * @version 
 */
public class ChildLeafList implements java.util.List {

    private java.util.List childList = new ArrayList();
    
    private boolean childrenChecked = false;
    
    private Vector childNodes = new Vector();
    
    /** Creates new childLeafList */
    public ChildLeafList() {
    }

    public List getChildList() { return childList; }
    public void setChildList(List list_) { childList.addAll(list_); }
    public boolean getChildrenChecked() { return childrenChecked; }
    public void setChildrenChecked(boolean checked_) {  childrenChecked = checked_; }
    public Vector getChildNodes() { return childNodes; }
    public void setChildNodes(Vector arg1_) { childNodes = arg1_; }
    
    public boolean add(java.lang.Object obj)
    {
        return childList.add(obj);
    }
      
    public void add(int index, Object element) 
    {
        childList.add(index,element);
    }
    
    public boolean addAll(int index, Collection c) 
    {
        return childList.addAll(index,c);
    }
    
    public boolean addAll(java.util.Collection collection)
    {
        return childList.addAll(collection);
    }
        
    public void clear()
    {
        childList.clear();
    }
    
    public boolean contains(java.lang.Object obj)
    {
        return childList.contains(obj);
    }
    
    public boolean containsAll(Collection c) 
    {
        return childList.containsAll(c);
    }
    
    public boolean equals(Object o) 
    {
        return childList.equals(o);
    }
    
    public java.lang.Object get(int param)
    {
        return childList.get(param);
    }
    
    public int hashCode() 
    {
        return childList.hashCode();
    }
    
    public int indexOf(Object o) 
    {
        return childList.indexOf(o);
    }
    
    public java.util.Iterator iterator()
    {
        return childList.iterator();
    }
    
    public int lastIndexOf(Object o) 
    {
     return childList.lastIndexOf(o);
    }
    
    public ListIterator listIterator()
    {
        return childList.listIterator();
    }
    public ListIterator listIterator(int index) 
    {
        return childList.listIterator(index);
    }
    public boolean remove(java.lang.Object obj)
    {
        return childList.remove(obj);
    }
    public Object remove(int index)
    {
        return childList.remove(index);
    } 
   public boolean removeAll(Collection c)
    {
        return childList.removeAll(c);
    }
    
   public boolean retainAll(Collection c)
    {
        return childList.retainAll(c);
    }
    public int size()
    {
        return childList.size();
    }
    
    public java.lang.Object set(int param, java.lang.Object obj)
    {
        return childList.set(param,obj);
    }
    
    public List subList(int fromIndex, int toIndex) 
    {
        return childList.subList(fromIndex,toIndex);
    }
    public boolean isEmpty()
    {
        return childList.isEmpty();
    }
    
    public Object[] toArray()
    { 
        return childList.toArray();
    }
    
    public Object[] toArray(Object[] a) 
    { 
        return childList.toArray(a);
    }
    
}
