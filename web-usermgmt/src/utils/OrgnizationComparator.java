package utils;

import java.util.Comparator;

public class OrgnizationComparator implements Comparator<Organization> {

	@Override
	public int compare(Organization org1, Organization org2) {
		if(org1 == null || org2 == null) {
			return 0;
		} else if (org1.getOrgNodeId()== null || org2.getOrgNodeId() == null ) {
			return 0;
		} else if (org1.getOrgNodeId() > org2.getOrgNodeId() ) {
			return 1;
		} else if (org1.getOrgNodeId() < org2.getOrgNodeId() ) {
			return -1;
		}  else {
			return 0;
		}
		
	}

}
