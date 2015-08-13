package com.ctb.utils.datamodel;

import java.io.Serializable;
import java.util.List;

public class CorrectAnswerDragDrop implements Serializable{

	private static final long serialVersionUID = 1L;
	private CorrectAnswerDropArea droparea;
	private List<CorrectAnswerDragArea> dragarea;
	
	public CorrectAnswerDragDrop(){
		
	}

	public CorrectAnswerDropArea getDroparea() {
		return droparea;
	}

	public void setDroparea(CorrectAnswerDropArea droparea) {
		this.droparea = droparea;
	}

	public List<CorrectAnswerDragArea> getDragarea() {
		return dragarea;
	}

	public void setDragarea(List<CorrectAnswerDragArea> dragarea) {
		this.dragarea = dragarea;
	}
	
}
