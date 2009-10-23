package com.ctb.testSessionInfo.dto; 

import com.ctb.bean.testAdmin.Program;
import com.ctb.bean.testAdmin.ProgramData;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.UserNodeData;
import java.util.ArrayList;
import java.util.List;

public class ReportManager implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;

    private List programs = null;
    private int programIndex = 0;
    public List organizations = null;
    private int organizationIndex = 0;
    
    public ReportManager() {}
    
    public List getPrograms() {
        return this.programs;
    }
    public void setPrograms(List programs) {
        this.programs = programs;
    }
    public int getProgramIndex() {
        return this.programIndex;
    }
    public int getOrganizationIndex() {
        return this.organizationIndex;
    }
    
    public List initPrograms(ProgramData userPrograms) 
    {
        this.programs = new ArrayList();
        Program[] programs = userPrograms.getPrograms();
        
        for (int i=0 ; i<programs.length ; i++) {
            Program program = programs[i];
            Node node = new Node(program.getProgramId(), program.getProgramName());         
            this.programs.add(node);
        }
        
        this.programIndex = 0;
        
        return this.programs;
    }

    public List initOrganizations(UserNodeData userTopNodes) 
    {
        this.organizations = new ArrayList();
        UserNode[] userNodes = userTopNodes.getUserNodes();   
     
        for (int i=0 ; i<userNodes.length ; i++) {
            UserNode userNode = userNodes[i];
            Node node = new Node(userNode.getOrgNodeId(), userNode.getOrgNodeName());         
            this.organizations.add(node);
        }

        this.organizationIndex = 0;
        
        return this.organizations;
    }
    
    public String[] getProgramNames() 
    {
        List names = new ArrayList();
        for (int i=0 ; i<this.programs.size() ; i++) {
            Node program = (Node)this.programs.get(i);
            names.add(program.getName());
        }        
        return (String[])names.toArray(new String[0]);
    }

    public String getSelectedProgramName() 
    {
        Node program = (Node)this.programs.get(this.programIndex);
        return program.getName();
    }

    public Integer getSelectedProgramId() 
    {
        Node program = (Node)this.programs.get(this.programIndex);
        return program.getId();
    }

    public Boolean isMultiplePrograms() 
    {
        return new Boolean(this.programs.size() > 1);
    }

    public Boolean isSingleProgramAndOrganization() 
    {
        return new Boolean((this.programs.size() <= 1) && (this.organizations.size() <= 1));
    }
    
    public String[] getOrganizationNames() 
    {
        List names = new ArrayList();
        for (int i=0 ; i<this.organizations.size() ; i++) {
            Node org = (Node)this.organizations.get(i);
            names.add(org.getName());
        }        
        return (String[])names.toArray(new String[0]);
    }
    
    public String getSelectedOrganizationName() 
    {
        Node org = (Node)this.organizations.get(this.organizationIndex);
        return org.getName();
    }

    public Integer getSelectedOrganizationId() 
    {
        Node org = (Node)this.organizations.get(this.organizationIndex);
        return org.getId();
    }

    public Boolean isMultipleOrganizations() 
    {
        return new Boolean(this.organizations.size() > 1);
    }
    
    public Integer setSelectedProgram(String index)
    {
        this.programIndex = 0;
        if (index != null) {
            this.programIndex = Integer.parseInt(index);
        }
        return getSelectedProgramId();
    }
    
    public Integer setSelectedOrganization(String index)
    {
        this.organizationIndex = 0;
        if (index != null) {
            this.organizationIndex = Integer.parseInt(index);
        }
        return getSelectedOrganizationId();
    }
} 
