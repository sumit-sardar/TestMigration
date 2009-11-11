package com.ctb.lexington.db.irsdata;

import com.ctb.lexington.db.record.Persistent;
/**
 * @author Rama_Rao
 *
 */
public class IrsOrgNodeDimData implements Persistent{

    private Long orgNodeid;
    private Long numLevels;
    private Long level1Id;
    private String level1Type;
    private String level1Name;
    private Long level2Id;
    private String level2Type;
    private String level2Name;
    private Long level3Id;
    private String level3Type;
    private String level3Name;
    private Long level4Id;
    private String level4Type;
    private String level4Name;
    private Long level5Id;
    private String level5Type;
    private String level5Name;
    private Long level6Id;
    private String level6Type;
    private String level6Name;
    private Long level7Id;
    private String level7Type;
    private String level7Name;
    private Long customerid;


    public Long getOrgNodeid() {
        return orgNodeid;
    }

    public void setOrgNodeid(Long orgNodeid) {
        this.orgNodeid = orgNodeid;
    }

    public Long getNumLevels() {
        return numLevels;
    }

    public void setNumLevels(Long numLevels) {
        this.numLevels = numLevels;
    }

    public Long getLevel1Id() {
        return level1Id;
    }

    public void setLevel1Id(Long level1Id) {
        this.level1Id = level1Id;
    }

    public String getLevel1Type() {
        return level1Type;
    }

    public void setLevel1Type(String level1Type) {
        this.level1Type = level1Type;
    }

    public String getLevel1Name() {
        return level1Name;
    }

    public void setLevel1Name(String level1Name) {
        this.level1Name = level1Name;
    }

    public Long getLevel2Id() {
        return level2Id;
    }

    public void setLevel2Id(Long level2Id) {
        this.level2Id = level2Id;
    }

    public String getLevel2Type() {
        return level2Type;
    }

    public void setLevel2Type(String level2Type) {
        this.level2Type = level2Type;
    }

    public String getLevel2Name() {
        return level2Name;
    }

    public void setLevel2Name(String level2Name) {
        this.level2Name = level2Name;
    }

    public Long getLevel3Id() {
        return level3Id;
    }

    public void setLevel3Id(Long level3Id) {
        this.level3Id = level3Id;
    }

    public String getLevel3Type() {
        return level3Type;
    }

    public void setLevel3Type(String level3Type) {
        this.level3Type = level3Type;
    }

    public String getLevel3Name() {
        return level3Name;
    }

    public void setLevel3Name(String level3Name) {
        this.level3Name = level3Name;
    }

    public Long getLevel4Id() {
        return level4Id;
    }

    public void setLevel4Id(Long level4Id) {
        this.level4Id = level4Id;
    }

    public String getLevel4Type() {
        return level4Type;
    }

    public void setLevel4Type(String level4Type) {
        this.level4Type = level4Type;
    }

    public String getLevel4Name() {
        return level4Name;
    }

    public void setLevel4Name(String level4Name) {
        this.level4Name = level4Name;
    }

    public Long getLevel5Id() {
        return level5Id;
    }

    public void setLevel5Id(Long level5Id) {
        this.level5Id = level5Id;
    }

    public String getLevel5Type() {
        return level5Type;
    }

    public void setLevel5Type(String level5Type) {
        this.level5Type = level5Type;
    }

    public String getLevel5Name() {
        return level5Name;
    }

    public void setLevel5Name(String level5Name) {
        this.level5Name = level5Name;
    }

    public Long getLevel6Id() {
        return level6Id;
    }

    public void setLevel6Id(Long level6Id) {
        this.level6Id = level6Id;
    }

    public String getLevel6Type() {
        return level6Type;
    }

    public void setLevel6Type(String level6Type) {
        this.level6Type = level6Type;
    }

    public String getLevel6Name() {
        return level6Name;
    }

    public void setLevel6Name(String level6Name) {
        this.level6Name = level6Name;
    }

    public Long getLevel7Id() {
        return level7Id;
    }

    public void setLevel7Id(Long level7Id) {
        this.level7Id = level7Id;
    }

    public String getLevel7Type() {
        return level7Type;
    }

    public void setLevel7Type(String level7Type) {
        this.level7Type = level7Type;
    }

    public String getLevel7Name() {
        return level7Name;
    }

    public void setLevel7Name(String level7Name) {
        this.level7Name = level7Name;
    }
    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }
    
    public boolean equals(Object other) {
            return
                this.getCustomerid().equals(((IrsOrgNodeDimData) other).getCustomerid()) &&
                (this.getLevel1Id() == null || (
                    //this.getLevel1Id().equals(((IrsOrgNodeDimData) other).getLevel1Id()) &&
                    this.getLevel1Name().equals(((IrsOrgNodeDimData) other).getLevel1Name()) &&
                    this.getLevel1Type().equals(((IrsOrgNodeDimData) other).getLevel1Type()))
                ) &&
                (this.getLevel2Id() == null || (
                    //this.getLevel2Id().equals(((IrsOrgNodeDimData) other).getLevel2Id()) &&
                    this.getLevel2Name().equals(((IrsOrgNodeDimData) other).getLevel2Name()) &&
                    this.getLevel2Type().equals(((IrsOrgNodeDimData) other).getLevel2Type()))
                ) &&
                (this.getLevel3Id() == null || (
                    //this.getLevel3Id().equals(((IrsOrgNodeDimData) other).getLevel3Id()) &&
                    this.getLevel3Name().equals(((IrsOrgNodeDimData) other).getLevel3Name()) &&
                    this.getLevel3Type().equals(((IrsOrgNodeDimData) other).getLevel3Type()))
                ) &&
                (this.getLevel4Id() == null || (
                    //this.getLevel4Id().equals(((IrsOrgNodeDimData) other).getLevel4Id()) &&
                    this.getLevel4Name().equals(((IrsOrgNodeDimData) other).getLevel4Name()) &&
                    this.getLevel4Type().equals(((IrsOrgNodeDimData) other).getLevel4Type()))
                ) &&
                (this.getLevel5Id() == null || (
                    //this.getLevel5Id().equals(((IrsOrgNodeDimData) other).getLevel5Id()) &&
                    this.getLevel5Name().equals(((IrsOrgNodeDimData) other).getLevel5Name()) &&
                    this.getLevel5Type().equals(((IrsOrgNodeDimData) other).getLevel5Type()))
                ) &&
                (this.getLevel6Id() == null || (
                    //this.getLevel6Id().equals(((IrsOrgNodeDimData) other).getLevel6Id()) &&
                    this.getLevel6Name().equals(((IrsOrgNodeDimData) other).getLevel6Name()) &&
                    this.getLevel6Type().equals(((IrsOrgNodeDimData) other).getLevel6Type()))
                ) &&
                (this.getLevel7Id() == null || (
                    //this.getLevel7Id().equals(((IrsOrgNodeDimData) other).getLevel7Id()) &&
                    this.getLevel7Name().equals(((IrsOrgNodeDimData) other).getLevel7Name()) &&
                    this.getLevel7Type().equals(((IrsOrgNodeDimData) other).getLevel7Type())));
        }

        public int hashCode() {
            if(orgNodeid != null) {
                return (int) orgNodeid.longValue();
            } else {
                return super.hashCode();
            }
        }
}
