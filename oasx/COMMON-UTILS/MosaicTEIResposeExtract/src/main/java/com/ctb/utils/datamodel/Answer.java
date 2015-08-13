package com.ctb.utils.datamodel;

import java.io.Serializable;
import java.util.List;

public class Answer implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String id;
	private String format;
	private String response;	
	//private List<CorrectAnswerDragDrop> dragDropAnswer;
	private DropArea droparea;
	private List<DragArea> dragarea;
	
	public Answer(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public DropArea getDroparea() {
		return droparea;
	}

	public void setDroparea(DropArea droparea) {
		this.droparea = droparea;
	}

	public List<DragArea> getDragarea() {
		return dragarea;
	}

	public void setDragarea(List<DragArea> dragarea) {
		this.dragarea = dragarea;
	}

//	public List<CorrectAnswerDragDrop> getDragDropAnswer() {
//		return dragDropAnswer;
//	}
//
//	public void setDragDropAnswer(List<CorrectAnswerDragDrop> dragDropAnswer) {
//		this.dragDropAnswer = dragDropAnswer;
//	}
	
	
}
