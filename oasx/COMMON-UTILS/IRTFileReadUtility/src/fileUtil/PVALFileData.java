package fileUtil;

import java.util.ArrayList;
import java.util.List;

public class PVALFileData implements Cloneable {

	private String level;
	private String grade;
	private String form;
	private String other;
	private String content;
	private String normsGroup;
	private List<CodeValue> dataList = new ArrayList<CodeValue>();
	
	public String getNormsGroup() {
		return normsGroup;
	}
	public void setNormsGroup(String normsGroup) {
		this.normsGroup = normsGroup;
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
		PVALFileData pval = (PVALFileData) obj;
		if(this.getLevel().equals(pval.getLevel()) 
			&& this.getForm().equals(pval.getForm())
			&& this.getOther().equals(pval.getOther())
			&& this.getNormsGroup().equals(pval.getNormsGroup())
			&& this.getContent().equals(pval.getContent())) {
			
			return true;
		}
		return false;
	}
	
	@Override
	public PVALFileData clone() throws CloneNotSupportedException {
		
		PVALFileData pvalFileData = (PVALFileData) super.clone();
		List<CodeValue> valueList = new ArrayList<CodeValue>();
		if(pvalFileData.getDataList() != null) {
			for(CodeValue val : pvalFileData.getDataList()) {
				CodeValue codeVal = new CodeValue();
				codeVal.setCode(val.getCode());
				valueList.add(codeVal);
			}
			pvalFileData.setDataList(valueList);
		}
		return pvalFileData;
	}
}