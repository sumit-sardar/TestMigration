package com.mhe.ctb.oas.BMTSync.rest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestBaseResponse {
	
	public static final String ERROR_CODE = "ERRCD";
	public static final String ERROR_MESSAGE = "ERROR MESSAGE";
	
	@Test
	public void testBaseResponse_ReadWrite() {
		final BaseResponse response = new BaseResponse();
		
		response.setErrorCode(ERROR_CODE);
		response.setErrorMessage(ERROR_MESSAGE);
		response.setSuccessful(false);
		
		assertEquals(ERROR_CODE, response.getServiceErrorCode());
		assertEquals(ERROR_MESSAGE, response.getServiceErrorMessage());
		assertEquals(false, response.isSuccessful());
	}
}
