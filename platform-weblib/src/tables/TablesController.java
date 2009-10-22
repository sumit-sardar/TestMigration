package tables;
import java.util.ArrayList;

import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import com.ctb.oas.dto.OrgNode;
import com.ctb.oas.dto.Student;
import com.ctb.widgets.bean.ColumnSortEntry;
import com.ctb.widgets.bean.PagerSummary;
import com.ctb.widgets.bean.PathListEntry;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class TablesController extends PageFlowController
{
    static final long serialVersionUID = 1L;
    private ArrayList students;
    private ArrayList noStudents;
    private OrgNode[] orgNodes;
    private ArrayList proctors;


 	public TablesController()
    {

        this.noStudents = new ArrayList();

        this.students = new ArrayList();

        Student student = new Student();
        student.setStudentId(new Integer(499));
        student.setFirstName("Zachary");
        student.setMiddleName(null);
        student.setLastName("Applegate");
        student.setStudentNumber("111222333444");
        this.students.add( student );

        Student student2 = new Student();
        student2.setStudentId(new Integer(500));
        student2.setFirstName("Adam");
        student2.setMiddleName(null);
        student2.setLastName("Barkely");
        student2.setStudentNumber("246813579");
        this.students.add( student2 );

        Student student3 = new Student();
        student3.setStudentId(new Integer(501));
        student3.setFirstName("Beth");
        student3.setMiddleName("Marie");
        student3.setLastName("Charleston");
        student3.setStudentNumber("123498765");
        this.students.add( student3 );

        Student student4 = new Student();
        student4.setStudentId(new Integer(502));
        student4.setFirstName("Christopher");
        student4.setMiddleName("Michael");
        student4.setLastName("Dowry");
        student4.setStudentNumber("986532741");
        this.students.add( student4 );

        Student student5 = new Student();
        student5.setStudentId(new Integer(503));
        student5.setFirstName("Drew");
        student5.setMiddleName(null);
        student5.setLastName("Everyman");
        student5.setStudentNumber("7539518264");
        this.students.add( student5 );

        Student student6 = new Student();
        student6.setStudentId(new Integer(504));
        student6.setFirstName("Emily");
        student6.setMiddleName("Lillian");
        student6.setLastName("Freedman");
        student6.setStudentNumber("741852963");
        this.students.add( student6 );

        Student student7 = new Student();
        student7.setStudentId(new Integer(505));
        student7.setFirstName("Frederick");
        student7.setMiddleName("George");
        student7.setLastName("Halloway");
        student7.setStudentNumber("963852741");
        this.students.add( student7 );

        Student student8 = new Student();
        student8.setStudentId(new Integer(506));
        student8.setFirstName("John");
        student8.setMiddleName("Jacob");
        student8.setLastName("Smith");
        student8.setStudentNumber("123456789");
        this.students.add( student8 );
        

        
        this.orgNodes = new OrgNode[7];
        
        this.orgNodes[0] = new OrgNode();
        this.orgNodes[0].setOrgNodeId( new Integer(1000) );
        this.orgNodes[0].setOrgNodeName("Algebra I");
        this.orgNodes[0].setTotalStudentsWithin( new Integer(33) );
        this.orgNodes[0].setTotalStudentsAssigned( new Integer(10) );

        this.orgNodes[1] = new OrgNode();
        this.orgNodes[1].setOrgNodeId( new Integer(1234) );
        this.orgNodes[1].setOrgNodeName("Biology");
        this.orgNodes[1].setTotalStudentsWithin( new Integer(25) );
        this.orgNodes[1].setTotalStudentsAssigned( new Integer(0) );

        this.orgNodes[2] = new OrgNode();
        this.orgNodes[2].setOrgNodeId( new Integer(2345) );
        this.orgNodes[2].setOrgNodeName("Chemistry");
        this.orgNodes[2].setTotalStudentsWithin( new Integer(18) );
        this.orgNodes[2].setTotalStudentsAssigned( new Integer(5) );

        this.orgNodes[3] = new OrgNode();
        this.orgNodes[3].setOrgNodeId( new Integer(3456) );
        this.orgNodes[3].setOrgNodeName("Economics");
        this.orgNodes[3].setTotalStudentsWithin( new Integer(24) );
        this.orgNodes[3].setTotalStudentsAssigned( new Integer(7) );

        this.orgNodes[4] = new OrgNode();
        this.orgNodes[4].setOrgNodeId( new Integer(4567) );
        this.orgNodes[4].setOrgNodeName("English Lit'rature");
        this.orgNodes[4].setTotalStudentsWithin( new Integer(29) );
        this.orgNodes[4].setTotalStudentsAssigned( new Integer(5) );

        this.orgNodes[5] = new OrgNode();
        this.orgNodes[5].setOrgNodeId( new Integer(6789) );
        this.orgNodes[5].setOrgNodeName("Physics");
        this.orgNodes[5].setTotalStudentsWithin( new Integer(12) );
        this.orgNodes[5].setTotalStudentsAssigned( new Integer(0) );

        this.orgNodes[6] = new OrgNode();
        this.orgNodes[6].setOrgNodeId( new Integer(7890) );
        this.orgNodes[6].setOrgNodeName("Spanish I");
        this.orgNodes[6].setTotalStudentsWithin( new Integer(17) );
        this.orgNodes[6].setTotalStudentsAssigned( new Integer(8) );


        this.proctors = new ArrayList();

    }


    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="viewSimpleTable.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "viewSimpleTable.do")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }

    public ArrayList getNoStudents() {
		return noStudents;
	}


	public void setNoStudents(ArrayList noStudents) {
		this.noStudents = noStudents;
	}


	public OrgNode[] getOrgNodes() {
		return orgNodes;
	}


	public void setOrgNodes(OrgNode[] orgNodes) {
		this.orgNodes = orgNodes;
	}


	public ArrayList getProctors() {
		return proctors;
	}


	public void setProctors(ArrayList proctors) {
		this.proctors = proctors;
	}


	public ArrayList getStudents() {
		return students;
	}


	public void setStudents(ArrayList students) {
		this.students = students;
	}



    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class BeginForm extends FormData
    {
        private String textFieldValue;

        private String radioButtonValue;

        public void setRadioButtonValue(String radioButtonValue)
        {
            this.radioButtonValue = radioButtonValue;
        }

        public String getRadioButtonValue()
        {
            return this.radioButtonValue;
        }

        public void setTextFieldValue(String textFieldValue)
        {
            this.textFieldValue = textFieldValue;
        }

        public String getTextFieldValue()
        {
            return this.textFieldValue;
        }
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="pathListTable.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "pathListTable.jsp")
    })
    protected Forward viewPathListTable(ViewPathListTableForm form)
    {
    	form.setPageRequested(new Integer(1));
    	
        // Label for objects being displayed
        this.getRequest().setAttribute("nodeLabel", "Classes");


        // PagerSummary information for objects being displayed
        PagerSummary pathPagerSummary = new PagerSummary();
        pathPagerSummary.setCurrentPage(form.getPageRequested());
        pathPagerSummary.setTotalPages(new Integer(1));
        pathPagerSummary.setTotalObjects(new Integer(7));
        this.getRequest().setAttribute("pathPagerSummary", pathPagerSummary);


        // PathListEntry information for objects being displayed
        PathListEntry stateEntry = new PathListEntry();
        stateEntry.setLabel("California");
        stateEntry.setValue(new Integer(123));

        PathListEntry countyEntry = new PathListEntry();
        countyEntry.setLabel("Monterey County");
        countyEntry.setValue(new Integer(456));

        PathListEntry schoolEntry = new PathListEntry();
        schoolEntry.setLabel("Acme High School");
        schoolEntry.setValue(new Integer(789));

        ArrayList entries = new ArrayList();
        entries.add(stateEntry);
        entries.add(countyEntry);
        entries.add(schoolEntry);
        this.getRequest().setAttribute("orgNodePath", entries);

        this.getRequest().setAttribute("noStudents", this.noStudents);
        
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="complexTable.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "complexTable.jsp")
    })
    protected Forward viewComplexTable(ViewComplexTableForm form)
    {
        int totalPages = 3;
        int totalObjects = 25;
        int totalFiltered = 12;
        
        if (form.getPageRequested() == null || form.getPageRequested().intValue() < 1)
            form.setPageRequested(new Integer(1));
        else if (form.getPageRequested().intValue() > totalPages)
            form.setPageRequested(new Integer(totalPages));

        PagerSummary somePager = new PagerSummary();
        somePager.setCurrentPage(form.getPageRequested());
        somePager.setTotalPages(new Integer(totalPages));
        somePager.setTotalObjects(new Integer(totalObjects));
        somePager.setTotalFilteredObjects(new Integer(totalFiltered));
        this.getRequest().setAttribute("somePagerSummary", somePager);

        ArrayList dummyList = new ArrayList();
        this.getRequest().setAttribute("showNoResultSection", dummyList);
        
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="simpleTable.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "simpleTable.jsp")
    })
    protected Forward viewSimpleTable(ViewSimpleTableForm form)
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="groupingTable.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "groupingTable.jsp")
    })
    protected Forward viewGroupingTable(ViewGroupingTableForm form)
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="transparentTable.jsp"
     */
    @Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "transparentTable.jsp")
		}
	)
    protected Forward viewTransparentTable()
    {
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewGroupingTableForm extends FormData
    {
        private String radioButtonValue;

        private String textFieldValue;

        public void setTextFieldValue(String textFieldValue)
        {
            this.textFieldValue = textFieldValue;
        }

        public String getTextFieldValue()
        {
            return this.textFieldValue;
        }

        public void setRadioButtonValue(String radioButtonValue)
        {
            this.radioButtonValue = radioButtonValue;
        }

        public String getRadioButtonValue()
        {
            return this.radioButtonValue;
        }
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewSimpleTableForm extends FormData
    {
        private String sortOrderBy = ColumnSortEntry.ASCENDING;

        private String sortColumn = "studentId";

        private String radioButtonValue;

        public void setRadioButtonValue(String radioButtonValue)
        {
            this.radioButtonValue = radioButtonValue;
        }

        public String getRadioButtonValue()
        {
            return this.radioButtonValue;
        }

        public void setSortColumn(String sortColumn)
        {
            this.sortColumn = sortColumn;
        }

        public String getSortColumn()
        {
            return this.sortColumn;
        }

        public void setSortOrderBy(String sortOrderBy)
        {
            this.sortOrderBy = sortOrderBy;
        }

        public String getSortOrderBy()
        {
            return this.sortOrderBy;
        }
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewComplexTableForm extends FormData
    {

        private String sortOrderBy = ColumnSortEntry.ASCENDING;
        private String sortColumn = "lastName";
        private Integer pageRequested = new Integer(1);
        private String filter_tab = "tab1";
        private Boolean filterVisible = Boolean.FALSE;



        public void setFilter_tab(String filter_tab)
        {
            this.filter_tab = filter_tab;
        }

        public String getFilter_tab()
        {
            return this.filter_tab;
        }

        public void setPageRequested(Integer pageRequested)
        {
            this.pageRequested = pageRequested;
        }

        public Integer getPageRequested()
        {
            return this.pageRequested;
        }

        public void setSortColumn(String sortColumn)
        {
            this.sortColumn = sortColumn;
        }

        public String getSortColumn()
        {
            return this.sortColumn;
        }

        public void setSortOrderBy(String sortOrderBy)
        {
            this.sortOrderBy = sortOrderBy;
        }

        public String getSortOrderBy()
        {
            return this.sortOrderBy;
        }

        public void setFilterVisible(Boolean filterVisible)
        {
            this.filterVisible = filterVisible;
        }

        public Boolean getFilterVisible()
        {
            return this.filterVisible;
        }
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewPathListTableForm extends FormData
    {
        private String orgNodeName;

        private Integer orgNodeId = new Integer(12345);
        private Integer childOrgNodeId =  new Integer(0);
        private String sortOrderBy = ColumnSortEntry.ASCENDING;
        private String sortColumn = "something";
        private Integer pageRequested = new Integer(1);

        

        public void setPageRequested(Integer pageRequested)
        {
            this.pageRequested = pageRequested;
        }

        public Integer getPageRequested()
        {
            return this.pageRequested;
        }

        public void setOrgNodeId(Integer orgNodeId)
        {
            this.orgNodeId = orgNodeId;
        }

        public Integer getOrgNodeId()
        {
            return this.orgNodeId;
        }

        public void setSortColumn(String sortColumn)
        {
            this.sortColumn = sortColumn;
        }

        public String getSortColumn()
        {
            return this.sortColumn;
        }

        public void setSortOrderBy(String sortOrderBy)
        {
            this.sortOrderBy = sortOrderBy;
        }

        public String getSortOrderBy()
        {
            return this.sortOrderBy;
        }

        public void setChildOrgNodeId(Integer childOrgNodeId)
        {
            this.childOrgNodeId = childOrgNodeId;
        }

        public Integer getChildOrgNodeId()
        {
            return this.childOrgNodeId;
        }

        public void setOrgNodeName(String orgNodeName)
        {
            this.orgNodeName = orgNodeName;
        }

        public String getOrgNodeName()
        {
            return this.orgNodeName;
        }
    }

    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewContainerTableForm extends FormData
    {
        private String orgNodeId;

        private String filterSomething;

        private String filterVisible = "false";

        private String filterTab = "CU";

        private String sortOrder = "ASC";

        private String sortColumn = "middleName";

        private Integer pageRequested = new Integer(1);

        public void setPageRequested(Integer pageRequested)
        {
            this.pageRequested = pageRequested;
        }

        public Integer getPageRequested()
        {
            return this.pageRequested;
        }

        public void setSortColumn(String sortColumn)
        {
            this.sortColumn = sortColumn;
        }

        public String getSortColumn()
        {
            return this.sortColumn;
        }

        public void setSortOrder(String sortOrder)
        {
            this.sortOrder = sortOrder;
        }

        public String getSortOrder()
        {
            return this.sortOrder;
        }

        public void setFilterTab(String filterTab)
        {
            this.filterTab = filterTab;
        }

        public String getFilterTab()
        {
            return this.filterTab;
        }

        public void setFilterVisible(String filterVisible)
        {
            this.filterVisible = filterVisible;
        }

        public String getFilterVisible()
        {
            return this.filterVisible;
        }

        public void setFilterSomething(String filterSomething)
        {
            this.filterSomething = filterSomething;
        }

        public String getFilterSomething()
        {
            return this.filterSomething;
        }

        public void setOrgNodeId(String orgNodeId)
        {
            this.orgNodeId = orgNodeId;
        }

        public String getOrgNodeId()
        {
            return this.orgNodeId;
        }
    }
}
