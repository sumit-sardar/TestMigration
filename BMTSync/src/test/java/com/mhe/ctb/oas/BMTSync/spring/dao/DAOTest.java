package com.mhe.ctb.oas.BMTSync.spring.dao;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mhe.ctb.oas.BMTSync.model.Student;


@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/mhe/ctb/oas/BMTSync/dao/spring-jdbc-qa.xml")
public class DAOTest {

	private SpringStudentDAO _studentDao;
	
	private Logger LOGGER = Logger.getLogger(DAOTest.class);

	@Test
	public void testOrgNodeDAO() throws Exception
	{
		Student student = _studentDao.getStudent(413595);
		LOGGER.info(String.format("Found Student: %s", student));
	}

	public SpringStudentDAO getStudentDao() {
		return _studentDao;
	}

	@Autowired
	public void setStudentDao(SpringStudentDAO studentDao) {
		_studentDao = studentDao;
	}	
	
	
}
