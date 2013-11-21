package com.tcs.util;

import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.Contects;
import com.ctb.bean.Network;
import com.ctb.bean.WorkStation;

final public class DataFormater {

	public DataFormater() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Contects> getValueMap(List list) {
		List<Contects> csvFileOkhlahamas = new ArrayList<Contects>();
		for (int i = 1; i < list.size(); i++) {

			String record[] = (String[]) list.get(i);
			Contects okhlahama = new Contects();
			okhlahama.setDistrictNumber(record[0]);
			okhlahama.setDistrictName(record[1]);
			okhlahama.setSchoolNumber(record[2]);
			okhlahama.setSchoolName(record[3]);
			okhlahama.setSiteType(record[4]);
			okhlahama.setTestCoordinatorFirstName(record[5]);
			okhlahama.setTestCoordinatorLastName(record[6]);
			okhlahama.setTestCoordinatorEmail(record[7]);
			okhlahama.setTestPhoneNumber(record[8]);
			okhlahama.setTechCoordinatorFirstName(record[9]);
			okhlahama.setTechCoordinatorLastName(record[10]);
			okhlahama.setTechCoordinatorEmail(record[11]);
			okhlahama.setTechPhoneNumber(record[12]);
			csvFileOkhlahamas.add(okhlahama);
		}// end of loop
		return csvFileOkhlahamas;
	}// end of method
	
	public List<Network> getNetworkList(List list) {
		List<Network> networkList = new ArrayList<Network>();
		for (int i = 1; i < list.size(); i++) {
			String record[] = (String[]) list.get(i);
			Network network=new Network();
			network.setDistNumber(record[0]);
			network.setSchoolNumber(record[1]);	
			network.setInternetConnectionType(record[2]);
			network.setDownSpeed(record[3]);
			network.setUpSpeed(record[4]);			
			networkList.add(network);			
		}// end of loop
		return networkList;
	}// end of method

	public List<WorkStation> getWorkStationList(List list) {
		List<WorkStation> WorkStationList = new ArrayList<WorkStation>();
		for (int i = 1; i < list.size(); i++) {
			String record[] = (String[]) list.get(i);
			WorkStation network=new WorkStation();
			network.setDistNumber(record[0]);
			network.setSchoolNumber(record[1]);
			network.setTypeOfWorkstation(record[2]);
			network.setNumberOfWorkstation(record[3]);
			network.setOperatingSystem(record[4]);
			network.setProcessorSpeed(record[5]);
			network.setMemory(record[6]);		
			WorkStationList.add(network);			
		}// end of loop
		return WorkStationList;
	}// end of method
	
}// end of class
