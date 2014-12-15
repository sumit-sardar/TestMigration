package com.mhe.ctb.oas.BMTSync.spring.converter;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

public class CustomConversionService extends DefaultConversionService implements InitializingBean
{
	private Collection<Converter<?,?>> _converters;
	
	public void setConverters(Collection<Converter<?,?>> converters)
	{
		_converters = converters;
	}

	public void afterPropertiesSet() throws Exception {
		
		if (_converters == null)
		{
			return;
		}
		
		for (Converter<?,?> converter : _converters)
		{
			this.addConverter(converter);
		}
		
	}
	
}
