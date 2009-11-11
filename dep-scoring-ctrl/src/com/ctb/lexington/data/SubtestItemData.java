package com.ctb.lexington.data;

import java.io.Serializable;

public class SubtestItemData implements Serializable {
	// -- private variables -- //
	private Integer id = null;
	private String name = null;
	private int itemCount_SR = 0; 
	private int itemCount_CR = 0; 
	private int itemCount_UnScored = 0; 
	
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public int getItemCount_CR() {
        return itemCount_CR;
    }
    public void setItemCount_CR(int itemCount_CR) {
        this.itemCount_CR = itemCount_CR;
    }
    public int getItemCount_SR() {
        return itemCount_SR;
    }
    public void setItemCount_SR(int itemCount_SR) {
        this.itemCount_SR = itemCount_SR;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getItemCount_UnScored() {
        return itemCount_UnScored;
    }
    public void setItemCount_UnScored(int itemCount_UnScored) {
        this.itemCount_UnScored = itemCount_UnScored;
    }
    public int getTotalItemCount() {
        return itemCount_SR + itemCount_CR;
    }
}
