package utils;

public class Row {
	
	private int id;
	
	private Object []cell;
	
	public Row(int id){
		this.id=id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object[] getCell() {
		return cell;
	}

	public void setCell(Object[] cell) {
		this.cell = cell;
	}

	

	

	
}
