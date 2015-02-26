package com.ctb.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.ctb.CreateFile;
import com.ctb.dto.Accomodations;
import com.ctb.dto.CustomerDemographic;
import com.ctb.dto.OrderFile;
import com.ctb.dto.Student;
import com.ctb.dto.TestRoster;
import com.ctb.dto.Tfil;

public class ProcessRosters implements Runnable{
	
	static Logger logger = Logger.getLogger(ProcessRosters.class.getName());
	
	private TestRoster roster;
	private String customerModelLevelValue;
	private String customerState;
	private String customerCity;
	private Map<Integer, String> customerDemographic;
	private Set<CustomerDemographic> setAccomodation;
	private OrderFile orderFile;
	private static List<Tfil> tfilList = new ArrayList<Tfil>();
	CreateFile cFile = new CreateFile();
	
	public ProcessRosters(TestRoster roster, String customerModelLevelValue,
			String customerState, String customerCity,
			Map<Integer, String> customerDemographic,
			Set<CustomerDemographic> setAccomodation, OrderFile orderFile,
			CreateFile cFile) {
		
		
		this.roster = roster;
		this.customerModelLevelValue = customerModelLevelValue;
		this.customerState = customerState;
		this.customerCity = customerCity;
		this.customerDemographic = customerDemographic;
		this.setAccomodation = setAccomodation;
		this.orderFile = orderFile;
		this.cFile = cFile;
	}

	@Override
	public void run() {
		logger.info("Processing roster :: " + roster.getTestRosterId());
		List<String> invalidSubtestList = new ArrayList<String>();
		Tfil tfil = new Tfil();
		tfil.setTestRosterId(roster.getTestRosterId().toString());
		
		String isClassNodeRequiredDummyName = "";
		isClassNodeRequiredDummyName = Configuration.getIsClassNodeRequiredDummyName();
		
		/**
		 * Checking for customer model level value
		 */
		if(null != isClassNodeRequiredDummyName && !"".equalsIgnoreCase(isClassNodeRequiredDummyName)
				&& Integer.valueOf(this.customerModelLevelValue).intValue() < 7){
			int value = Integer.valueOf(this.customerModelLevelValue).intValue();
			value++;
			tfil.setModelLevel(new Integer(value).toString());
		}
		else{
			tfil.setModelLevel(this.customerModelLevelValue);
		}
		
		tfil.setState(this.customerState);
		tfil.setCity(this.customerCity);
		
		try {
			Student student = roster.getStudent();
			//Student Information
			cFile.setStudentList(tfil, student, roster.getTestRosterId());
			
			// Accommodations
			Accomodations accomodations = cFile.createAccomodations(student.getStudentDemographic(), setAccomodation,	customerDemographic, tfil);
			tfil.setAccomodations(accomodations);
			
			// Organization
			cFile.createOrganization(tfil, roster, orderFile);
			
			// create test Session
			cFile.createTestSessionDetails(tfil, roster, orderFile);
			
			// create studentItemSetstatus
			cFile.createStudentItemStatusDetails(tfil, roster);

			// Skill Area Information : Scale Score, Proficiency Level, NCE, PR, Point Obtained, Percent Obtained,
			cFile.createSkillAreaScoreInformation(tfil, roster, invalidSubtestList);

			//Sub Skill Area Information 
			cFile.createSubSkillAreaScoreInformation(tfil, roster, invalidSubtestList);
			
			//cFile.createItemResponseInformation(oascon, roster,tfil); // not needed for LAS Links Form A/B/Espanol
			
			// sub test indicator flag for roster
			tfil.setSubtestIndicatorFlag((roster.getSubtestModel()== null)?"":roster.getSubtestModel());
			
			addToArrayList(tfil);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * populate a collection of flat file object
	 * @param tfil
	 */
	private static synchronized void addToArrayList(Tfil tfil) {
		ProcessRosters.tfilList.add(tfil);
	}

	/**
	 * get populated collection object of flat file 
	 * @return
	 */
	public static List<Tfil> getFinalList(){
		return ProcessRosters.tfilList;
	}
}
