package utils;

public class Row {
	
	private String id;
	
	private Object []cell;
	
	public Row(String id) {
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object[] getCell() {
		return cell;
	}

	public void setCell(Object[] cell) {
		this.cell = cell;
	}
}
