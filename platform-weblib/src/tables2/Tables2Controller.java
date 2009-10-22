package tables2;
import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import com.ctb.oas.dto.OrgNode;
import com.ctb.oas.dto.Student;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class Tables2Controller extends PageFlowController
{
    static final long serialVersionUID = 1L;
    
    public ArrayList students;
    public ArrayList testHarnessStudents;
    public ArrayList orgNodes;
    public ArrayList testHarnessOrgNodes;
    
    public static final String TH_DISPLAY_ALL         = "Display everything";
    public static final String TH_DISPLAY_NONE        = "Display no results";
    public static final String TH_DISPLAY_FILTERED    = "Display filtered results";
    public static final String TH_DISPLAY_SINGLE_PAGE = "Display results on single page";
    public String[] testHarnessResultDisplay;
    
    

    public Tables2Controller()
    {
        
        students = new ArrayList();
        
        // Used to test the display of results on the screen.
        testHarnessStudents = new ArrayList();

        Student student = new Student();
        student.setStudentId(new Integer(499));
        student.setFirstName("Zachary");
        student.setMiddleName(null);
        student.setLastName("Applegate");
        student.setStudentNumber("111222333444");
        testHarnessStudents.add(student);

        Student student2 = new Student();
        student2.setStudentId(new Integer(500));
        student2.setFirstName("Adam");
        student2.setMiddleName(null);
        student2.setLastName("Barkely");
        student2.setStudentNumber("246813579");
        testHarnessStudents.add(student2);

        Student student3 = new Student();
        student3.setStudentId(new Integer(501));
        student3.setFirstName("Beth");
        student3.setMiddleName("Marie");
        student3.setLastName("Charleston");
        student3.setStudentNumber("123498765");
        testHarnessStudents.add(student3);

        Student student4 = new Student();
        student4.setStudentId(new Integer(502));
        student4.setFirstName("Christopher");
        student4.setMiddleName("Michael");
        student4.setLastName("Dowry");
        student4.setStudentNumber("986532741");
        testHarnessStudents.add(student4);

        Student student5 = new Student();
        student5.setStudentId(new Integer(503));
        student5.setFirstName("Drew");
        student5.setMiddleName(null);
        student5.setLastName("Everyman");
        student5.setStudentNumber("7539518264");
        testHarnessStudents.add(student5);

        Student student6 = new Student();
        student6.setStudentId(new Integer(504));
        student6.setFirstName("Emily");
        student6.setMiddleName("Lillian");
        student6.setLastName("Freedman");
        student6.setStudentNumber("741852963");
        testHarnessStudents.add(student6);

        Student student7 = new Student();
        student7.setStudentId(new Integer(505));
        student7.setFirstName("Frederick");
        student7.setMiddleName("George");
        student7.setLastName("Halloway");
        student7.setStudentNumber("963852741");
        testHarnessStudents.add(student7);

        Student student8 = new Student();
        student8.setStudentId(new Integer(506));
        student8.setFirstName("John");
        student8.setMiddleName("Jacob");
        student8.setLastName("Smith");
        student8.setStudentNumber("123456789");
        testHarnessStudents.add(student8);
        
        
        orgNodes = new ArrayList();
        
        testHarnessOrgNodes = new ArrayList();
        
        OrgNode orgNode1 = new OrgNode();
        orgNode1.setOrgNodeId(new Integer(1000));
        orgNode1.setOrgNodeName("Algebra I");
        orgNode1.setTotalStudentsWithin(new Integer(33));
        orgNode1.setTotalStudentsAssigned(new Integer(10));
        testHarnessOrgNodes.add(orgNode1);

        OrgNode orgNode2 = new OrgNode();
        orgNode2.setOrgNodeId(new Integer(1234));
        orgNode2.setOrgNodeName("Biology");
        orgNode2.setTotalStudentsWithin(new Integer(25));
        orgNode2.setTotalStudentsAssigned(new Integer(0));
        testHarnessOrgNodes.add(orgNode2);

        OrgNode orgNode3 = new OrgNode();
        orgNode3.setOrgNodeId(new Integer(2345));
        orgNode3.setOrgNodeName("Chemistry");
        orgNode3.setTotalStudentsWithin(new Integer(18));
        orgNode3.setTotalStudentsAssigned(new Integer(5));
        testHarnessOrgNodes.add(orgNode3);

        OrgNode orgNode4 = new OrgNode();
        orgNode4.setOrgNodeId(new Integer(3456));
        orgNode4.setOrgNodeName("Economics");
        orgNode4.setTotalStudentsWithin(new Integer(24));
        orgNode4.setTotalStudentsAssigned(new Integer(7));
        testHarnessOrgNodes.add(orgNode4);

        OrgNode orgNode5 = new OrgNode();
        orgNode5.setOrgNodeId(new Integer(4567));
        orgNode5.setOrgNodeName("English Literature");
        orgNode5.setTotalStudentsWithin(new Integer(29));
        orgNode5.setTotalStudentsAssigned(new Integer(5));
        testHarnessOrgNodes.add(orgNode5);

        OrgNode orgNode6 = new OrgNode();
        orgNode6.setOrgNodeId(new Integer(6789));
        orgNode6.setOrgNodeName("Physics");
        orgNode6.setTotalStudentsWithin(new Integer(12));
        orgNode6.setTotalStudentsAssigned(new Integer(0));
        testHarnessOrgNodes.add(orgNode6);

        OrgNode orgNode7 = new OrgNode();
        orgNode7.setOrgNodeId(new Integer(7890));
        orgNode7.setOrgNodeName("Spanish I");
        orgNode7.setTotalStudentsWithin(new Integer(17));
        orgNode7.setTotalStudentsAssigned(new Integer(8));
        testHarnessOrgNodes.add(orgNode7);
        
        
        
        
        
        // Add test harness options for displayed results
        testHarnessResultDisplay = new String[4];
        testHarnessResultDisplay[0] = TH_DISPLAY_ALL;
        testHarnessResultDisplay[1] = TH_DISPLAY_NONE;
        testHarnessResultDisplay[2] = TH_DISPLAY_FILTERED;
        testHarnessResultDisplay[3] = TH_DISPLAY_SINGLE_PAGE;
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
     * @jpf:forward name="success" path="viewComplexTable.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "viewComplexTable.do")
    })
    protected Forward begin()
    {
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
        Integer totalFound;
        Integer totalFiltered;
        Integer totalPages;
        
        if (form.testHarnessDisplayResults.equals(TH_DISPLAY_ALL))
        {
            if (students.isEmpty())
            {
                students.addAll(testHarnessStudents);
            }
    
            totalFound = new Integer(students.size());
            totalFiltered = new Integer(0);
            totalPages = new Integer(5);    

        }
        else if (form.testHarnessDisplayResults.equals(TH_DISPLAY_FILTERED))
        {
            if (students.isEmpty())
            {
                students.addAll(testHarnessStudents);
            }
    
            totalFound = new Integer(students.size());
            totalFiltered = new Integer(5);
            totalPages = new Integer(3);    

        }
        else if (form.testHarnessDisplayResults.equals(TH_DISPLAY_SINGLE_PAGE))
        {
            if (students.isEmpty())
            {
                students.addAll(testHarnessStudents);
            }
    
            totalFound = new Integer(students.size());
            totalFiltered = new Integer(0);
            totalPages = new Integer(1);    

        }
        else
        {
            students.clear();

            totalFound = new Integer(0);
            totalFiltered = new Integer(0);
            totalPages = new Integer(0);    
        }

        HttpServletRequest request = (HttpServletRequest)getRequest();
        request.setAttribute("totalFound", totalFound);
        request.setAttribute("totalFiltered", totalFiltered);
        request.setAttribute("totalPages", totalPages);

        if (form.pageRequested == null || form.pageRequested.intValue() < 1)
            form.pageRequested = new Integer(1);
        else if (form.pageRequested.intValue() > totalPages.intValue())
            form.pageRequested = totalPages;
        
    
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewComplexTableForm extends FormData
    {
        private String filterLastName;

        private Boolean visibleFilter = Boolean.FALSE;

        private String sortOrder = "asc";

        private String sortColumn = "lastName";

        private String testHarnessDisplayResults = TH_DISPLAY_ALL;

        private Integer pageRequested = new Integer(1);

        private Integer studentId;

        public void setStudentId(Integer studentId)
        {
            this.studentId = studentId;
        }

        public Integer getStudentId()
        {
            return this.studentId;
        }

        public void setPageRequested(Integer pageRequested)
        {
            this.pageRequested = pageRequested;
        }

        public Integer getPageRequested()
        {
            return this.pageRequested;
        }

        public void setTestHarnessDisplayResults(String testHarnessDisplayResults)
        {
            this.testHarnessDisplayResults = testHarnessDisplayResults;
        }

        public String getTestHarnessDisplayResults()
        {
            return this.testHarnessDisplayResults;
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

        public void setVisibleFilter(Boolean visibleFilter)
        {
            this.visibleFilter = visibleFilter;
        }

        public Boolean getVisibleFilter()
        {
            return this.visibleFilter;
        }

        public void setFilterLastName(String filterLastName)
        {
            this.filterLastName = filterLastName;
        }

        public String getFilterLastName()
        {
            return this.filterLastName;
        }
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="pathListTable.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "pathListTable.jsp")
		}
	)
    protected Forward viewPathListTable(ViewPathListTableForm form)
    {
        Integer totalFound;
        Integer totalFiltered;
        Integer totalPages;
        
        if( form.testHarnessDisplayResults.equals(TH_DISPLAY_ALL) ) {
            if( orgNodes.isEmpty() ) {
                orgNodes.addAll( testHarnessOrgNodes );
            }
    
            totalFound = new Integer(orgNodes.size());
            totalFiltered = new Integer(0);
            totalPages = new Integer(4);    

        } else if( form.testHarnessDisplayResults.equals(TH_DISPLAY_FILTERED) ) {
            if( orgNodes.isEmpty() ) {
                orgNodes.addAll( testHarnessOrgNodes );
            }
    
            totalFound = new Integer(orgNodes.size());
            totalFiltered = new Integer(2);
            totalPages = new Integer(3);    

        } else if( form.testHarnessDisplayResults.equals(TH_DISPLAY_SINGLE_PAGE) ) {
            if( orgNodes.isEmpty() ) {
                orgNodes.addAll( testHarnessOrgNodes );
            }
    
            totalFound = new Integer(orgNodes.size());
            totalFiltered = new Integer(0);
            totalPages = new Integer(1);    

        } else {
            orgNodes.clear();

            totalFound = new Integer(0);
            totalFiltered = new Integer(0);
            totalPages = new Integer(0);    
        }

        HttpServletRequest request = (HttpServletRequest) getRequest();
        request.setAttribute("totalFound", totalFound);
        request.setAttribute("totalFiltered", totalFiltered);
        request.setAttribute("totalPages", totalPages );

        if( form.pageRequested == null || form.pageRequested.intValue() < 1 )
            form.pageRequested = new Integer(1);
        else if( form.pageRequested.intValue() > totalPages.intValue() )
            form.pageRequested = totalPages;
        
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewPathListTableForm extends FormData
    {
        private String orgNodeId;

        private String testHarnessDisplayResults = TH_DISPLAY_ALL;

        private String sortColumn = "orgNodeName";

        private String sortOrder = "asc";

        private Integer pageRequested;

        public void setPageRequested(Integer pageRequested)
        {
            this.pageRequested = pageRequested;
        }

        public Integer getPageRequested()
        {
            return this.pageRequested;
        }

        public void setSortOrder(String sortOrder)
        {
            this.sortOrder = sortOrder;
        }

        public String getSortOrder()
        {
            return this.sortOrder;
        }

        public void setSortColumn(String sortColumn)
        {
            this.sortColumn = sortColumn;
        }

        public String getSortColumn()
        {
            return this.sortColumn;
        }

        public void setTestHarnessDisplayResults(String testHarnessDisplayResults)
        {
            this.testHarnessDisplayResults = testHarnessDisplayResults;
        }

        public String getTestHarnessDisplayResults()
        {
            return this.testHarnessDisplayResults;
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
