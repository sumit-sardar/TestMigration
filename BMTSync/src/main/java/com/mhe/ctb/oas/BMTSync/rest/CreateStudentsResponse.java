package com.mhe.ctb.oas.BMTSync.rest;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.StudentResponse;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"isSuccessful", "successCount", "failureCount", "failures"})
@JsonInclude(Include.NON_EMPTY)
public class CreateStudentsResponse extends BaseResponse {
	
	// The logger shouldn't be included in the JSON.
	private static final Logger logger = Logger.getLogger(CreateStudentsResponse.class);

	boolean _isSuccessful;
	int _successCount;
	int _failureCount;
	List<StudentResponse> _failures;
	//int _serviceErrorCode;
	//String _serviceErrorMessage;
	
	public boolean getIsSuccessful() {
		return _isSuccessful;
	}
	
	@JsonProperty("isSuccessful")
	public void setIsSuccessful(final boolean isSuccessful) {
		_isSuccessful = isSuccessful;
	}

	public int getSuccessCount() {
		return _successCount;
	}

	@JsonProperty("successCount")
	public void setSuccessCount(int successCount) {
		_successCount = successCount;
	}

	public int getFailureCount() {
		return _failureCount;
	}

	@JsonProperty("failureCount")
	public void setFailureCount(int failureCount) {
		_failureCount = failureCount;
	}

	public List<StudentResponse> getFailures() {
		return _failures;
	}

	
	@JsonProperty("failures")
	public void setFailures(List<StudentResponse> failures) {
		_failures = failures;
	}
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			logger.info("Failure mapping CreateStudentsResponse to JSON.");
			return null;
		}
	}
	

    /*
	@JsonProperty("failures")
	public void setFailuresFromObjects(Collection<Object> failures) {
		_failures = null;

		if ((failures == null) || (failures.size() == 0)) 
		{
			return;
		}
		
		
		List<Student> failedStudents = new LinkedList<Student>();
		for (Object o : failures)
		{
			if (o instanceof Student)
			{
				failedStudents.add((Student)o);
			}
		}
		
		if (failedStudents.size() > 0)
		{
			_failures = failedStudents;
		}
	}

	
	public int getServiceErrorCode() {
		return _serviceErrorCode;
	}

	@JsonProperty("serviceErrorCode")
	public void setServiceErrorCode(int serviceErrorCode) {
		_serviceErrorCode = serviceErrorCode;
	}

	public String getServiceErrorMessage() {
		return _serviceErrorMessage;
	}

	@JsonProperty("serviceErrorMessage")
	public void setServiceErrorMessage(String serviceErrorMessage) {
		_serviceErrorMessage = serviceErrorMessage;
	}
	*/
	
}
