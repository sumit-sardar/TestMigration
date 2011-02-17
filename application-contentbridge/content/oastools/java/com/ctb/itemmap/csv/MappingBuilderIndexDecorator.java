package com.ctb.itemmap.csv;

/**
 * @author wmli
 */
public class MappingBuilderIndexDecorator implements MappingBuilder {
	MappingBuilder builder;
	int index = 1;
	
	public MappingBuilderIndexDecorator(MappingBuilder builder) {
		this.builder = builder;
	}

    public MappingEntry build(String mapping) {
    	MappingEntry entry = builder.build(mapping);
    	entry.setIndex(new Integer(index).toString());
    	index++;
    	return entry;
    }
}
