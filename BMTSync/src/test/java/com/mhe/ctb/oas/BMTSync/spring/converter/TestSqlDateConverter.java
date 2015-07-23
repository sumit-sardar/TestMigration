package com.mhe.ctb.oas.BMTSync.spring.converter;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Test;
import org.springframework.core.convert.ConversionFailedException;

public class TestSqlDateConverter {

	@Test(expected = ConversionFailedException.class)
	public void testSqlDateConvert_nullInput() {
		final SqlDateConverter converter = new SqlDateConverter();
		
		converter.convert(null);
	}
	
	@Test
	public void testDqlDateConvert_normalInput() {
		final Calendar now = Calendar.getInstance();
		final Date date = new Date(now.getTimeInMillis());
		
		final SqlDateConverter converter = new SqlDateConverter();
		final Calendar output = converter.convert(date);
		assertEquals(now.getTimeInMillis(), output.getTimeInMillis());
	}
}
