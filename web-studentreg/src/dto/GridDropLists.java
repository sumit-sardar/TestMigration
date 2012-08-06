package dto;

public class GridDropLists {
	
	private String[] gradeOptions;
	private String[] testCatalogOptions;
	private String useRole;

	public String[] getGradeOptions() {
		return gradeOptions;
	}

	public void setGradeOptions(String[] gradeOptions) {
		this.gradeOptions = gradeOptions;
	}

	public String[] getTestCatalogOptions() {
		return testCatalogOptions;
	}

	public void setTestCatalogOptions(String[] testCatalogOptions) {
		this.testCatalogOptions = testCatalogOptions;
	}

	public String getUseRole() {
		return useRole;
	}

	public void setUseRole(String useRole) {
		this.useRole = useRole;
	}

}
