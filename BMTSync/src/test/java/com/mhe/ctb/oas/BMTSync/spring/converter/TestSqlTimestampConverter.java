package com.mhe.ctb.oas.BMTSync.spring.converter;

import static org.junit.Assert.assertEquals;
import org.springframework.core.convert.ConversionFailedException;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Test;

public class TestSqlTimestampConverter {

	@Test(expected = ConversionFailedException.class)
	public void testSqlTimestampConvert_nullInput() {
		final SqlTimestampConverter converter = new SqlTimestampConverter();
		
		converter.convert(null);
	}
	
	@Test
	public void testDqlDateConvert_normalInput() {
		final Calendar now = Calendar.getInstance();
		final Timestamp timestamp = new Timestamp(now.getTimeInMillis());
		
		final SqlTimestampConverter converter = new SqlTimestampConverter();
		final Calendar output = converter.convert(timestamp);
		assertEquals(now.getTimeInMillis(), output.getTimeInMillis());
	}
}
