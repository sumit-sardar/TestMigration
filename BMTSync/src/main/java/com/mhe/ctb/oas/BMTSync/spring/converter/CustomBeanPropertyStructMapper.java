package com.mhe.ctb.oas.BMTSync.spring.converter;

import org.springframework.beans.BeanWrapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jdbc.support.oracle.BeanPropertyStructMapper;

public class CustomBeanPropertyStructMapper<T> extends BeanPropertyStructMapper<T>
{
	private ConversionService _conversionService;
	
	
	public CustomBeanPropertyStructMapper() {
		super();
	}

	/**
	 * Create a new BeanPropertyRowMapper.
	 * @param mappedClass the class that each row should be mapped to.
	 */
	public CustomBeanPropertyStructMapper(Class<T> mappedClass) {
		super(mappedClass);
	}
	
	public ConversionService getConversionService() {
		return _conversionService;
	}


	public void setConversionService(ConversionService conversionService) {
		_conversionService = conversionService;
	}


	@Override
	protected void initBeanWrapper(BeanWrapper bw) 
	{
		bw.setConversionService(_conversionService);
	}

}
