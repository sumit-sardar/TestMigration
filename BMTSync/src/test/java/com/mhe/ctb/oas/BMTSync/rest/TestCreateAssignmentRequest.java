package com.mhe.ctb.oas.BMTSync.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.ctb.oas.BMTSync.model.TestAssignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class TestCreateAssignmentRequest {
	
	public static final String JSON = "{\"testAssignments\":[{\"oasTestAdministrationID\":null,\"oasCustomerId\":null,\"oasTestCatalogId\":null,\"name\":\"NAME\",\"productName\":null,\"deliveryWindow\":null,\"parameters\":null,\"roaster\":null,\"errorCode\":null,\"errorMessage\":null}]}";
	public static final String NAME = "NAME";
	public static final String OTHER_NAME = "OTHER NAME";
	
	@Test
	public void testCreateAssignmentRequest_AddOne() {
		final CreateAssignmentRequest request = new CreateAssignmentRequest();
		
		final TestAssignment assignment = new TestAssignment();
		assignment.setName(NAME);
		request.addTestAssignment(assignment);
		
		final List<TestAssignment> contentsList = request.getTestAssignments();
		assertEquals(1, contentsList.size());
		final TestAssignment contents = contentsList.get(0);
				
		assertEquals(assignment.getName(), contents.getName());
	}

	@Test
	public void testCreateAssignmentRequest_AddList() {
		final CreateAssignmentRequest request = new CreateAssignmentRequest();
		
		final TestAssignment assignmentOne = new TestAssignment();
		assignmentOne.setName(NAME);
		final TestAssignment assignmentTwo = new TestAssignment();
		assignmentTwo.setName(OTHER_NAME);
		final List<TestAssignment> assignmentList = new ArrayList<TestAssignment>();
		assignmentList.add(assignmentOne);
		assignmentList.add(assignmentTwo);
		request.addTestAssignments(assignmentList);
		
		// Now, make a keyset with all the names.
		Set<String> names = new HashSet<String>();
		names.add(NAME);
		names.add(OTHER_NAME);
		
		final List<TestAssignment> contentsList = request.getTestAssignments();
		assertEquals(2, contentsList.size());
		for (final TestAssignment contents : contentsList) {
			if (names.contains(contents.getName())) {
				names.remove(contents.getName());
			} else {
				fail("Name not found in list!");
			}
		}
		
		if (names.size() > 0) {
			fail("Name in list not found!");
		}
	}

	
	@Test
	public void testCreateAssignmentRequest_toJson_success() throws JsonProcessingException {
		final CreateAssignmentRequest request = new CreateAssignmentRequest();
		
		final TestAssignment assignment = new TestAssignment();
		assignment.setName(NAME);
		request.addTestAssignment(assignment);
		
		final String json = request.toJson();
		
		assertEquals(JSON, json);
		
	}
	
	@Test
	public void testCreateAssignmentRequest_toJson_exception() throws JsonProcessingException {
		final ObjectMapper mockMapper = mock(ObjectMapper.class);
		
		final CreateAssignmentRequest request = new CreateAssignmentRequest(mockMapper);
		
		final TestAssignment assignment = new TestAssignment();
		assignment.setName(NAME);
		request.addTestAssignment(assignment);
		
		when(mockMapper.writeValueAsString(any(TestAssignment.class))).thenThrow(new JsonGenerationException("Oopsie"));
		
		final String json = request.toJson();
		
		assertNull(json);
		
	}
}
