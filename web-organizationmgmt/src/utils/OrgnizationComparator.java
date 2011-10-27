package utils;

import java.util.Comparator;

public class OrgnizationComparator implements Comparator<Organization> {

	public int compare(Organization org1, Organization org2) {
		if(org1 == null || org2 == null) {
			return 0;
		} else if (org1.getOrgCategoryId() == null || org2.getOrgCategoryId() == null ) {
			return 0;
		} else if (org1.getOrgCategoryId() > org2.getOrgCategoryId() ) {
			return 1;
		} else if (org1.getOrgCategoryId() < org2.getOrgCategoryId() ) {
			return -1;
		}  else {
			return 0;
		}
		
	}

}
