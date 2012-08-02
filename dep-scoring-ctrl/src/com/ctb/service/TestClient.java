package com.ctb.service;

import java.util.ResourceBundle;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.AuthenticatedUser;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ProcessStudentScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ProcessStudentScoreResponse;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ScoringStatus;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.StudentScore;

public class TestClient {
	public static void main(String [] args) {
		      try {
		    	  ResourceBundle rb = ResourceBundle.getBundle("webServiceUrls");
		    	  String endPointUrl = rb.getString("url");
		    	  StudentScore stuScore = new StudentScore();
		    	  stuScore.setStudentId(1644566);
		    	  stuScore.setSessionId(234672);
		    	  stuScore.setFormId("G");
		    	  stuScore.setLevelId(13);
		    	  ProcessStudentScore pss = new ProcessStudentScore();
		    	  pss.setScore(stuScore);
		    	  AuthenticatedUser  user = new AuthenticatedUser();
		    	  user.setUsername("");
		    	  user.setUsername("");
		    	  
		    	  pss.setUser(user);
		    	  String url = "http://192.168.14.136:8080/host/services/ScoringService";
		    	  //ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContext("?wsdl", url);
		  		  final ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem("./repo",null);
		    	  ScoringServiceStub stub = new ScoringServiceStub(ctx, url);
		  		  stub._getServiceClient().engageModule("logging");
		    	  ScoringStatus status = stub.processStudentScore(user, stuScore);
		    	  System.out.println("status.getStudentId() -> " + status.getStudentId());
		    	  System.out.println("status.getSessionId() -> " + status.getSessionId());
		    	  System.out.println("status.getErrorMsg() -> " + status.getErrorMsg());
		    	  System.out.println("status.getStatus() -> " + status.getStatus());
		        } catch (Exception e) {
		        System.err.println(e.toString());
		        e.printStackTrace();
		      }
		    }

}
