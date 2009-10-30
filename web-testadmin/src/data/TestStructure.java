package data; 

import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.bean.testAdmin.TestProductData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import utils.FilterSortPageUtils;

public class TestStructure implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private TestProductData testProductData = null;    
    private List productNameList = null;
    private Hashtable productNameToIndexHash = null;
    private List levelList = null;
    private List gradeList = null;    
    private List testList = null;
    private List copyOfTestList = null;
    private List subtests = null;
    private List copyOfSubtests = null;
    private List formList = null;
    
    private String selectedProductName = null;
    private String selectedLevel = null;    
    private String selectedTestId = null;    
    private String testAccessCode = null;
    private Boolean hasBreak = Boolean.FALSE;
    
    public TestStructure()
    {
    }

    public void createProductNameList(TestProduct[] tps)
    {
        this.productNameList = new ArrayList();   
        this.productNameToIndexHash = new Hashtable();
        for (int i=0; i< tps.length; i++) {
            this.productNameList.add(tps[i].getProductName());
            this.productNameToIndexHash.put(tps[i].getProductName(), new Integer(i));
        }  
    }    

    public void createLevelList(String [] levels)
    {
        this.levelList = new ArrayList();
        if (levels.length > 1)
            this.levelList.add(FilterSortPageUtils.FILTERTYPE_SHOWALL);
        for (int i=0; i<levels.length; i++) {
            if (levels[i] != null)
                this.levelList.add(levels[i]);
        }
    }

    public void createGradeList(String [] grades)
    {
        this.gradeList = new ArrayList();
        if (grades.length > 1)
            this.gradeList.add(FilterSortPageUtils.FILTERTYPE_SHOWALL);
        for (int i=0; i<grades.length; i++) {
            if (grades[i] != null)
                this.gradeList.add(grades[i]);
        }
    }

    public HashMap createProductIdNameHash(TestProduct [] tps)
    {
        HashMap result = new HashMap();   
        for (int i=0; i< tps.length; i++) {
            result.put(tps[i].getProductId().toString(),tps[i].getProductName());
        }
        return result;
        
    }      
    
    public String getProductName(int index)
    {
        String name = "";
        if ((index >= 0) && (index < this.productNameList.size())) {
            name = (String)this.productNameList.get(index);
        }
        return name;
    }
    
    public int getProductListIndexByName(String productName) 
    {
        if (productName == null)
            return -1;
        Integer index = (Integer) this.productNameToIndexHash.get(productName);
        if (index == null)
            return -1;
        else
            return index.intValue();        
    }

    public String getLevelName(int index)
    {
        String name = "";
        if ((index >= 0) && (index < this.levelList.size())) {
            name = (String)this.levelList.get(index);
        }
        return name;
    }

    public String getGradeName(int index)
    {
        String name = "";
        if ((index >= 0) && (index < this.gradeList.size())) {
            name = (String)this.gradeList.get(index);
        }
        return name;
    }

    public String getFormName(int index)
    {
        String name = "";
        if ((index >= 0) && (index < this.formList.size())) {
            name = (String)this.formList.get(index);
        }
        return name;
    }

    public TestVO getTestById(String testId) 
    {
        if (this.testList == null)
            return null;
        Iterator it = this.testList.iterator();
        while (it.hasNext()) {
            TestVO testVO= (TestVO)it.next();
            if (testVO.getId().equals(new Integer(testId)))
                return testVO;
        }
        return null;
    } 

    public void copyTestList()
    {
        this.copyOfTestList = null;
        if (this.testList != null) {
            this.copyOfTestList = new ArrayList();
            Iterator it = this.testList.iterator();
            while (it.hasNext()) {
                this.copyOfTestList.add(new TestVO((TestVO)it.next()));  
            }
        }
    }

    public void restoreTestList()
    {
        this.testList = this.copyOfTestList;
    }
            
    public void buildSubTests(TestElement [] testElements) 
    {
        this.subtests = new ArrayList();

        for (int i=0; i<testElements.length; i++) {
            int durationMinutes = testElements[i].getTimeLimit().intValue()/60;
            String duration;
            if (durationMinutes == 0) 
                duration = "Untimed";
            else 
                duration = durationMinutes +" minutes";
            SubtestVO subtestVO = new SubtestVO(testElements[i].getItemSetId(),i+1+"", testElements[i].getItemSetName(), 
                    duration, testElements[i].getAccessCode(), testElements[i].getSessionDefault());
            this.subtests.add(subtestVO);
        }
    }

    public void copySubTests()
    {
        this.copyOfSubtests = null;
        if (this.subtests != null) {
            this.copyOfSubtests = new ArrayList();
            Iterator it = this.subtests.iterator();
            while (it.hasNext()) {
                this.copyOfSubtests.add(new SubtestVO((SubtestVO)it.next()));  
            }
        }
    }

    public void restoreSubtests()
    {
        this.subtests = this.copyOfSubtests;
    }

    public SubtestVO getSubTestByIndex(int index) 
    {
        SubtestVO st = null;
        if ((index >= 0) && (index < this.subtests.size())) {
            st = (SubtestVO)this.subtests.get(index);
        }
        return st;
    } 

    public String [] getTACs() 
    {
        String[] TACs = new String[this.subtests.size()];
        Iterator it = this.subtests.iterator();
        int i=0;
        while (it.hasNext()) {
            SubtestVO subtest =(SubtestVO)it.next();
            subtest.setTestAccessCode(subtest.getTestAccessCode().trim());
            TACs[i++] =subtest.getTestAccessCode();  
        }
        return TACs;
    }

    public boolean hasEmptyTAC(String [] TACs) 
    {
        boolean found = false;
        for (int i=0; i<TACs.length && !found; i++) {
            if ("".equals(TACs[i]))
                found = true;
        }            
        return found;
    }
    
    public boolean hasSpaceInTAC(String [] TACs) 
    {
        boolean found = false;
        for (int i=0; i<TACs.length && !found; i++) {
            if (TACs[i].indexOf(" ")>0)
                found = true;
        }            
        return found;
    }
    

    
    public boolean hasDuplicateTAC(String [] TACs) 
    {
        boolean found = false;
        if (TACs.length <= 1)
            return false;
        for (int i=0; i<TACs.length && !found; i++) {
            for (int j=i+1; j<TACs.length && !found; j++) {
                if (TACs[i]!=null && TACs[i].equalsIgnoreCase((TACs[j])) )
                    found = true;
            }     
        }            
        return found;
    }
    
    public boolean hasInvalidateTACLength(String [] TACs) 
    {
        boolean found = false;
        for (int i=0; i<TACs.length && !found; i++) {
            if (TACs[i]!=null && TACs[i].length() < 6)
                found = true;
        }            
        return found;
    }

    public boolean hasProduct()
    {
        return (this.productNameList.size() > 0);
    }

    public int getProductCount()
    {
        return this.productNameList != null ? this.productNameList.size() : 0;
    }

    public int getLevelCount()
    {
        return this.levelList != null ? this.levelList.size() : 0;
    }

    public int getGradeCount()
    {
        return this.gradeList != null ? this.gradeList.size() : 0;
    }

    public int getFormCount()
    {
        return this.formList != null ? this.formList.size() : 0;
    }

    public int getSubtestCount()
    {
        return this.subtests != null ? this.subtests.size() : 0;
    }
    
    public List getProductNameList()
    {
        return this.productNameList;
    }
    
    public void setProductNameList(List productNameList)
    {
        this.productNameList = productNameList;
    }

    public List getLevelList()
    {
        return this.levelList;
    }
    
    public void setLevelList(List levelList)
    {
        this.levelList = levelList;
    }

    public List getGradeList()
    {
        return this.gradeList;
    }
    
    public void setGradeList(List gradeList)
    {
        this.gradeList = gradeList;
    }

    public List getFormList()
    {
        return this.formList;
    }
    
    public void setFormList(String[] formList)
    {        
        this.formList = new ArrayList();
        for (int i=0 ; i<formList.length ; i++) {
            this.formList.add(formList[i]);    
        }
    }
        
    public List getTestList()
    {
        return this.testList;
    }
    
    public void setTestList(List testList)
    {
        this.testList = testList;
    }
    
    public List getSubtests()
    {
        if (this.subtests == null)
            this.subtests = new ArrayList();
        return this.subtests;
    }
    
    public void setSubtests(List subtests)
    {
        this.subtests = subtests;
    }

    public String getSelectedProductName()
    {
        return this.selectedProductName;
    }
    
    public void setSelectedProductName(String selectedProductName)
    {
        this.selectedProductName = selectedProductName;
    }
    
    public TestProductData getTestProductData()
    {
        return this.testProductData;
    }
    
    public void setTestProductData(TestProductData testProductData)
    {
        this.testProductData = testProductData;
    }

    public String getSelectedLevel()
    {
        return this.selectedLevel;
    }
    
    public void setSelectedLevel(String selectedLevel)
    {
        this.selectedLevel = selectedLevel;
    }
    
    public String getSelectedTestId()
    {
        return this.selectedTestId;
    }
    
    public void setSelectedTestId(String selectedTestId)
    {
        this.selectedTestId = selectedTestId;
    }

    public String getTestAccessCode()
    {
        return this.testAccessCode;
    }
    
    public void setTestAccessCode(String testAccessCode)
    {
        this.testAccessCode = testAccessCode;
    }

    public Boolean getHasBreak()
    {
        return this.hasBreak;
    }
    
    public void setHasBreak(Boolean hasBreak)
    {
        this.hasBreak = hasBreak;
    }
} 
