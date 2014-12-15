package com.mhe.ctb.oas.BMTSync.spring.converter;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

public class SqlDateConverter implements Converter<java.sql.Date, java.util.Calendar>
{

	public Calendar convert(Date date) 
	{
		if (date == null)
		{
			throw new ConversionFailedException(TypeDescriptor.valueOf(Date.class), TypeDescriptor.valueOf(Calendar.class), date, null);
		}
		
		Calendar returnCal = Calendar.getInstance();
		returnCal.setTimeInMillis(date.getTime());
		return returnCal;
	}



}
