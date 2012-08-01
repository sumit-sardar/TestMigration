package fileUtil;

public class CodeValue {

	private String code;
	private Double value;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		return this.code.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if(!(obj instanceof CodeValue)) {
			return false;
		}
		
		CodeValue obj1 = (CodeValue) obj;
		if(this.getCode().equals(obj1.getCode())) {
			return true;
		}
		return false;
	}
}