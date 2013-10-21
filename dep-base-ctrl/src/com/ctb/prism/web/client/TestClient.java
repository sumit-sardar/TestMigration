package com.ctb.prism.web.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.ctb.prism.web.controller.RosterDetailsTO;
import com.ctb.prism.web.controller.SampleWebservice;
import com.ctb.prism.web.controller.StudentListTO;

public class TestClient {
	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL("http://10.160.23.12:8080/tasc/StudentDataloadService?wsdl");
		QName qname = new QName("http://controller.web.prism.ctb.com/", "StudentDataloadService");
		
		Service service = Service.create(url, qname) ;
		
		SampleWebservice prxy = service.getPort(SampleWebservice.class);
		
		StudentListTO studentListTO  = new StudentListTO();
		List<RosterDetailsTO> rosterDetailsLst = studentListTO.getRosterDetailsTO();
		
		
		
		System.out.println(prxy.loadStudentData(studentListTO));
		
	}
}
