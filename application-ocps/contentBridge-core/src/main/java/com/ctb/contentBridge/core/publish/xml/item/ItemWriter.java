package com.ctb.contentBridge.core.publish.xml.item;


/**
 * Okay, we have some cases we need to handle
 *
 * Case One:
 *      Validate ready for insert - DONE: ItemWriterOASDatabaseValidateOnly
 * Case Two:
 *      Insert Item without Media - DONE: ItemWriterOASDatabaseNoMedia
 * Case Three:
 *      Insert Item and Media
 *
 *
 * Not a "case" - handle transaction boundaries within the command processors
 */
public interface ItemWriter {
    String write(Item item);
    
    void writeDatapointConditionCode( Item item );
}
