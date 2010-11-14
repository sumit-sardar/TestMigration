package com.ctb.bean; 

import java.io.Serializable;

/**
 * The base CTB bean type, all data collection objects returned
 * by platform calls extend this type
 * 
 * @author Nate_Cohen
 */
public abstract class CTBBean implements Serializable
{
    static final long serialVersionUID = 1L;
} 
