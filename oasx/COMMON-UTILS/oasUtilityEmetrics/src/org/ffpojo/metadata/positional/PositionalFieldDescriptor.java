package org.ffpojo.metadata.positional;

import org.ffpojo.metadata.FieldDecorator;
import org.ffpojo.metadata.FieldDescriptor;

public class PositionalFieldDescriptor extends FieldDescriptor implements Comparable<PositionalFieldDescriptor> {

	private int initialPosition;
	private int finalPosition;
	private PaddingAlign paddingAlign;
	private Justify justify;
	private char paddingCharacter;
	private boolean trimOnRead;
	private FieldDecorator<?> decorator;
	
	public int compareTo(PositionalFieldDescriptor other) {
		if (this.initialPosition - other.initialPosition == 0) {
			if (this.finalPosition - other.finalPosition == 0) {
				return this.getGetter().getName().compareTo(other.getGetter().getName());
			} else {
				return this.finalPosition - other.finalPosition;
			}
		} else {
			return this.initialPosition - other.initialPosition;
		}
	}
	
	// GETTERS AND SETTERS
	
	public int getInitialPosition() {
		return initialPosition;
	}
	public void setInitialPosition(int initialPosition) {
		this.initialPosition = initialPosition;
	}
	public int getFinalPosition() {
		return finalPosition;
	}
	public void setFinalPosition(int finalPosition) {
		this.finalPosition = finalPosition;
	}
	public FieldDecorator<?> getDecorator() {
		return decorator;
	}
	public void setDecorator(FieldDecorator<?> decorator) {
		this.decorator = decorator;
	}
	public PaddingAlign getPaddingAlign() {
		return paddingAlign;
	}
	public void setPaddingAlign(PaddingAlign paddingAlign) {
		this.paddingAlign = paddingAlign;
	}
	public char getPaddingCharacter() {
		return paddingCharacter;
	}
	public void setPaddingCharacter(char paddingCharacter) {
		this.paddingCharacter = paddingCharacter;
	}
	public boolean isTrimOnRead() {
		return trimOnRead;
	}
	public void setTrimOnRead(boolean trimOnRead) {
		this.trimOnRead = trimOnRead;
	}

	public Justify getJustify() {
		return justify;
	}

	public void setJustify(Justify justify) {
		this.justify = justify;
	}
}
