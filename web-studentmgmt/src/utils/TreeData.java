package utils; 

import java.util.ArrayList;
import java.util.List;

public class TreeData {
	
	private String data;
	private Attribute attr = new Attribute();
	private List<TreeData> children = new ArrayList<TreeData> ();
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Attribute getAttr() {
		return attr;
	}
	public void setAttr(Attribute attr) {
		this.attr = attr;
	}
	public List<TreeData> getChildren() {
		return children;
	}
	public void setChildren(List<TreeData> children) {
		this.children = children;
	}
	
	

}
