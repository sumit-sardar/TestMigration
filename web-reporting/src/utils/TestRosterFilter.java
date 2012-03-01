package utils; 

public class TestRosterFilter implements java.io.Serializable  
{ 
    private String lastName = null;
    private String lastNameFilterType = null;
    private String firstName = null;
    private String firstNameFilterType = null;
    private String accommodationFilterType = null;
    private String [] selectedAccommodations = null;
    private String organization = null;
    private String organizationFilterType = null;
    private String formFilterType = null;
    
    private Boolean showAccommodations=Boolean.FALSE;        

    
    public TestRosterFilter() 
    {
        this.lastName = "";
        this.lastNameFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.firstName = "";
        this.firstNameFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.accommodationFilterType = FilterSortPageUtils.STUDENTS_WITH_AND_WITHOUT_ACCOMMODATIONS;
        this.selectedAccommodations = new String[0];
        this.organization = "";
        this.organizationFilterType = FilterSortPageUtils.FILTERTYPE_CONTAINS;
        this.formFilterType = FilterSortPageUtils.FILTERTYPE_SHOWALL;
        this.showAccommodations=Boolean.FALSE;
    }

    public void copyValues(TestRosterFilter src)
    {
        this.lastName = src.getLastName();
        this.lastNameFilterType = src.getLastNameFilterType();
        this.firstName = src.getFirstName();
        this.firstNameFilterType = src.getFirstNameFilterType();
        this.accommodationFilterType = src.getAccommodationFilterType();
        this.selectedAccommodations = src.getSelectedAccommodations();
        this.organization = src.getOrganization();
        this.organizationFilterType = src.getOrganizationFilterType();
        this.formFilterType = src.getFormFilterType();
        this.showAccommodations = src.getShowAccommodations();
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    public String getLastName()
    {
        return this.lastName;
    }    
    public void setLastNameFilterType(String lastNameFilterType)
    {
        this.lastNameFilterType = lastNameFilterType;
    }
    public String getLastNameFilterType()
    {
        return this.lastNameFilterType;
    }    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public String getFirstName()
    {
        return this.firstName;
    }    
    public void setFirstNameFilterType(String firstNameFilterType)
    {
        this.firstNameFilterType = firstNameFilterType;
    }
    public String getFirstNameFilterType()
    {
        return this.firstNameFilterType;
    }    
    public void setAccommodationFilterType(String accommodationFilterType)
    {
        this.accommodationFilterType = accommodationFilterType;
    }
    public String getAccommodationFilterType()
    {
        return this.accommodationFilterType;
    }    
    public void setSelectedAccommodations(String [] selectedAccommodations)
    {
        this.selectedAccommodations = selectedAccommodations;
    }
    public String [] getSelectedAccommodations()
    {
        return this.selectedAccommodations;
    }        
    public void setOrganization(String organization)
    {
        this.organization = organization;
    }
    public String getOrganization()
    {
        return this.organization;
    }    
    public void setOrganizationFilterType(String organizationFilterType)
    {
        this.organizationFilterType = organizationFilterType;
    }
    public String getOrganizationFilterType()
    {
        return this.organizationFilterType;
    }    
    public void setFormFilterType(String formFilterType)
    {
        this.formFilterType = formFilterType;
    }
    public String getFormFilterType()
    {
        return this.formFilterType;
    }   
   
   public void setShowAccommodations(Boolean showAccommodations)
    {
        this.showAccommodations = showAccommodations;
    }

/*
    public Boolean getShowAccommodations()
    {
        return this.showAccommodations;
    }   
 */      
 
    
     public Boolean getShowAccommodations()
    {
        if (this.accommodationFilterType.equals(FilterSortPageUtils.STUDENTS_WITH_ACCOMMODATIONS))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;        
    }   
         
} 
