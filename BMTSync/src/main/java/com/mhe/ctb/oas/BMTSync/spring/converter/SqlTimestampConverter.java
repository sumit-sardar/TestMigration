package com.mhe.ctb.oas.BMTSync.spring.converter;

import java.sql.Timestamp;
import java.util.Calendar;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;


public class SqlTimestampConverter implements Converter<Timestamp, Calendar>
{
	
	public Calendar convert(Timestamp ts)
	{
		/*
		if (ts == null)
		{
			throw new ConversionFailedException(TypeDescriptor.valueOf(Timestamp.class), TypeDescriptor.valueOf(Calendar.class), ts, null);
		}
		*/
		
		Calendar returnCal = Calendar.getInstance();
		returnCal.setTimeInMillis(ts.getTime());
		return returnCal;
	}
	
}
