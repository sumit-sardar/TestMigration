package fileUtil;

import java.util.List;

public class PVALFileData implements Cloneable {

	private String level;
	private String grade;
	private String form;
	private String other;
	private String content;
	private String normsGroup;
	private CodeValue codeValue;
	private List<CodeValue> dataList;
	
	public String getNormsGroup() {
		return normsGroup;
	}
	public void setNormsGroup(String normsGroup) {
		this.normsGroup = normsGroup;
	}
	public CodeValue getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(CodeValue codeValue) {
		this.codeValue = codeValue;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<CodeValue> getDataList() {
		return dataList;
	}
	public void setDataList(List<CodeValue> dataList) {
		this.dataList = dataList;
	}
	
	@Override
	public int hashCode() {
		return this.level.hashCode() * this.grade.hashCode() * 
			   this.form.hashCode() * this.other.hashCode() * 
			   this.content.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if(!(obj instanceof PVALFileData)) {
			return false;
		}
		PVALFileData obj1 = (PVALFileData) obj;
		if(this.getLevel().equals(obj1.getLevel()) 
				&& this.getGrade().equals(obj1.getGrade())
				&& this.getForm().equals(obj1.getForm())
				&& this.getOther().equals(obj1.getOther())
				&& this.getNonGroup().equals(obj1.getNonGroup())
				&& this.getContent().equals(obj1.getContent())) {
			
			return true;
		}
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}