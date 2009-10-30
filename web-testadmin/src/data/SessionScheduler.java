package data; 

import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.User;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SessionScheduler implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private Integer testAdminId = null;
    private String currentSelectedTestId = null;
    
    private User user = null;
    private User scheduler = null;
    private User originalScheduler = null; 
    private List selectedStudents = null;
    private List selectedProctors = null;
    private int addedStudentsCount = 0;
    private int addedProctorsCount = 0;

    public SessionScheduler() 
    {
        this.currentSelectedTestId = "";
        this.addedStudentsCount = 0;
        this.addedProctorsCount = 0;
    }
    
    public Integer getTestAdminId() { return this.testAdminId; }
    public void setTestAdminId(Integer testAdminId) { this.testAdminId = testAdminId; }

    public String getCurrentSelectedTestId() { return this.currentSelectedTestId; }
    public void setCurrentSelectedTestId(String currentSelectedTestId) { this.currentSelectedTestId = currentSelectedTestId; }
    
    public User getUser() { return this.user; }
    public void setUser(User user) { this.user = user; }
        
    public User getScheduler() { return this.scheduler; }
    public void setScheduler(User scheduler) { this.scheduler = scheduler; }

    public User getOriginalScheduler() { return this.originalScheduler; }
    public void setOriginalScheduler(User originalScheduler) { this.originalScheduler = originalScheduler; }

    public List getSelectedStudents() { return this.selectedStudents; }
    public void setSelectedStudents(List selectedStudents) { this.selectedStudents = selectedStudents; }
    public int getSelectedStudentCount() { return this.selectedStudents != null ? this.selectedStudents.size() : 0; }

    public List getSelectedProctors() { return this.selectedProctors; }
    public void setSelectedProctors(List selectedProctors) { this.selectedProctors = selectedProctors; }
    public int getSelectedProctorCount() { return this.selectedProctors != null ? this.selectedProctors.size() : 0; }
    
    public void setAddedStudentsCount(int addedStudentsCount) { this.addedStudentsCount = addedStudentsCount; }
    public int getAddedStudentsCount() { return this.addedStudentsCount; }

    public void setAddedProctorsCount(int addedProctorsCount) { this.addedProctorsCount = addedProctorsCount; }
    public int getAddedProctorsCount() { return this.addedProctorsCount; }
    
    public String getUserName()
    {
        String name = this.user.getFirstName() + " " + this.user.getLastName();
        return name;        
    } 
    public String getSchedulerName()
    {
        String name = this.scheduler.getFirstName() + " " + this.scheduler.getLastName();
        return name;        
    } 

    public Boolean getSupportAccommodations()
    {
        Boolean supportAccommodations = Boolean.TRUE;
        Customer customer = this.user.getCustomer();
        String hideAccommodations = customer.getHideAccommodations();
        if ((hideAccommodations != null) && hideAccommodations.equalsIgnoreCase("T")) {
            supportAccommodations = Boolean.FALSE;
        }
        return supportAccommodations;
    }
    
    public int removeSelectedStudents(HashMap hashMap)
    {
        int count=0;
        if (this.selectedStudents != null) {
            Iterator it = this.selectedStudents.iterator();
            while (it.hasNext()) {
                SessionStudent ss = (SessionStudent) it.next();
                if ((ss != null) && (hashMap.containsKey(ss.getStudentId()))) {
                    it.remove();
                    count++;
                }
            }          
        }
        return count;
    }
    
    public void setItemSetFormForSelectedStudents(String form)
    {
        if (this.selectedStudents != null) {
            Iterator it = this.selectedStudents.iterator();
            while (it.hasNext()) {
                SessionStudent ss = (SessionStudent) it.next();
                if (ss != null) {
                    if (form == null) {
                        ss.setItemSetForm(form);
                    }
                    else {
                        if ((ss.getItemSetForm() == null || ss.getItemSetForm().trim().equals(""))) {
                            ss.setItemSetForm(form);
                        }
                    }
                }
            }
        }
    }

    public SessionStudent [] getSessionStudentsfromSelectedStudents()
    {                    
        SessionStudent [] sessionStudents = new SessionStudent[this.selectedStudents.size()];
        Iterator it = this.selectedStudents.iterator();
        int i=0;
        while (it.hasNext()) {
            SessionStudent ss = (SessionStudent) it.next();
            sessionStudents[i++] = ss;
        }
        return sessionStudents;
    }

    public Integer getstudentWithAccommodationsCount()
    {                    
        Iterator it = this.selectedStudents.iterator();
        int studentWithAccommodationsCount = 0;
        int i = 0;
        while (it.hasNext()) {
            SessionStudent ss = (SessionStudent) it.next();
            if (ss.getHasAccommodations().equalsIgnoreCase("true"))
                studentWithAccommodationsCount++;
        }
        return new Integer(studentWithAccommodationsCount);
    }

    public int removeSelectedProctors(HashMap hashMap)
    {
        int count=0;
        if (this.selectedProctors != null) {
            Iterator it = this.selectedProctors.iterator();
            while (it.hasNext()) {
                User user = (User) it.next();
                if (hashMap.containsKey(user.getUserId())){
                    it.remove();
                    count++;
                }
            }          
        }
        return count;
    }

    public int removeAllEditableProctors()
    {
        int count=0;
        Iterator it = this.selectedProctors.iterator();
        while (it.hasNext()) {
            User user = (User) it.next();
            if ("T".equals(user.getEditable())){
                it.remove();
                count++;
            }
        }
        return count;
    }
} 
