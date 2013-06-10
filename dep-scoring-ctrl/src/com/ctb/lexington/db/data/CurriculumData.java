package com.ctb.lexington.db.data;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurriculumData {
    
    public static class Composite {
        private Long compositeId;
        private String compositeName;
        private String compositeType;
        private String subject;
        private Long compositeNumItems;
        private Long compositePointsPossible;
        private Long subjectId;
        private Long assessmentId;

        /**
		 * @return Returns the assessmentId.
		 */
		public Long getAssessmentId() {
			return assessmentId;
		}
		/**
		 * @param assessmentId The assessmentId to set.
		 */
		public void setAssessmentId(Long assessmentId) {
			this.assessmentId = assessmentId;
		}
		/**
		 * @return Returns the subjectId.
		 */
		public Long getSubjectId() {
			return subjectId;
		}
		/**
		 * @param subjectId The subjectId to set.
		 */
		public void setSubjectId(Long subjectId) {
			this.subjectId = subjectId;
		}
		/**
		 * @return Returns the compositeId.
		 */
		public Long getCompositeId() {
			return compositeId;
		}
		/**
		 * @param compositeId The compositeId to set.
		 */
		public void setCompositeId(Long compositeId) {
			this.compositeId = compositeId;
		}
		/**
		 * @return Returns the compositeName.
		 */
		public String getCompositeName() {
			return compositeName;
		}
		/**
		 * @param compositeName The compositeName to set.
		 */
		public void setCompositeName(String compositeName) {
			this.compositeName = compositeName;
		}
		/**
		 * @return Returns the compositeNumItems.
		 */
		public Long getCompositeNumItems() {
			return compositeNumItems;
		}
		/**
		 * @param compositeNumItems The compositeNumItems to set.
		 */
		public void setCompositeNumItems(Long compositeNumItems) {
			this.compositeNumItems = compositeNumItems;
		}
		/**
		 * @return Returns the compositePointsPossible.
		 */
		public Long getCompositePointsPossible() {
			return compositePointsPossible;
		}
		/**
		 * @param compositePointsPossible The compositePointsPossible to set.
		 */
		public void setCompositePointsPossible(Long compositePointsPossible) {
			this.compositePointsPossible = compositePointsPossible;
		}
		/**
		 * @return Returns the compositeType.
		 */
		public String getCompositeType() {
			return compositeType;
		}
		/**
		 * @param compositeType The compositeType to set.
		 */
		public void setCompositeType(String compositeType) {
			this.compositeType = compositeType;
		}
		/**
		 * @return Returns the subject.
		 */
		public String getSubject() {
			return subject;
		}
		/**
		 * @param subject The subject to set.
		 */
		public void setSubject(String subject) {
			this.subject = subject;
		}
    }
    
    public static class ContentArea {
        private Long contentAreaId;
        private String contentAreaName;
        private String contentAreaType;
        private String subject;
        private Long contentAreaNumItems;
        private Long contentAreaPointsPossible;
        private Long subtestId;
        private String subtestForm;
        private String subtestLevel;
        private Long subjectId;
        
        
		public ContentArea() {
			super();
		}
		public ContentArea(ContentArea contentArea) {
			this.contentAreaId = contentArea.getContentAreaId();
		    this.contentAreaName = contentArea.getContentAreaName();
	        this.contentAreaType = contentArea.getContentAreaType();
	        this.subject = contentArea.getSubject();
	        this.contentAreaNumItems = contentArea.getContentAreaNumItems();
	        this.contentAreaPointsPossible = contentArea.getContentAreaPointsPossible();
	        this.subtestId = contentArea.getSubtestId();
	        this.subtestForm = contentArea.getSubtestForm();
	        this.subtestLevel = contentArea.getSubtestLevel();
	        this.subjectId = contentArea.getSubjectId();
		}
		/**
		 * @return Returns the subjectId.
		 */
		public Long getSubjectId() {
			return subjectId;
		}
		/**
		 * @param subjectId The subjectId to set.
		 */
		public void setSubjectId(Long subjectId) {
			this.subjectId = subjectId;
		}
		/**
		 * @return Returns the subtestForm.
		 */
		public String getSubtestForm() {
			return subtestForm;
		}
		/**
		 * @param subtestForm The subtestForm to set.
		 */
		public void setSubtestForm(String subtestForm) {
			this.subtestForm = subtestForm;
		}
		/**
		 * @return Returns the subtestId.
		 */
		public Long getSubtestId() {
			return subtestId;
		}
		/**
		 * @param subtestId The subtestId to set.
		 */
		public void setSubtestId(Long subtestId) {
			this.subtestId = subtestId;
		}
		/**
		 * @return Returns the subtestLevel.
		 */
		public String getSubtestLevel() {
			return subtestLevel;
		}
		/**
		 * @param subtestLevel The subtestLevel to set.
		 */
		public void setSubtestLevel(String subtestLevel) {
			this.subtestLevel = subtestLevel;
		}
		/**
		 * @return Returns the contentAreaId.
		 */
		public Long getContentAreaId() {
			return contentAreaId;
		}
		/**
		 * @param contentAreaId The contentAreaId to set.
		 */
		public void setContentAreaId(Long contentAreaId) {
			this.contentAreaId = contentAreaId;
		}
		/**
		 * @return Returns the contentAreaName.
		 */
		public String getContentAreaName() {
			return contentAreaName;
		}
		/**
		 * @param contentAreaName The contentAreaName to set.
		 */
		public void setContentAreaName(String contentAreaName) {
			this.contentAreaName = contentAreaName;
		}
		/**
		 * @return Returns the contentAreaNumItems.
		 */
		public Long getContentAreaNumItems() {
			return contentAreaNumItems;
		}
		/**
		 * @param contentAreaNumItems The contentAreaNumItems to set.
		 */
		public void setContentAreaNumItems(Long contentAreaNumItems) {
			this.contentAreaNumItems = contentAreaNumItems;
		}
		/**
		 * @return Returns the contentAreaPointsPossible.
		 */
		public Long getContentAreaPointsPossible() {
			return contentAreaPointsPossible;
		}
		/**
		 * @param contentAreaPointsPossible The contentAreaPointsPossible to set.
		 */
		public void setContentAreaPointsPossible(Long contentAreaPointsPossible) {
			this.contentAreaPointsPossible = contentAreaPointsPossible;
		}
		/**
		 * @return Returns the contentAreaType.
		 */
		public String getContentAreaType() {
			return contentAreaType;
		}
		/**
		 * @param contentAreaType The contentAreaType to set.
		 */
		public void setContentAreaType(String contentAreaType) {
			this.contentAreaType = contentAreaType;
		}
		/**
		 * @return Returns the subject.
		 */
		public String getSubject() {
			return subject;
		}
		/**
		 * @param subject The subject to set.
		 */
		public void setSubject(String subject) {
			this.subject = subject;
		}
    }
    
    public static class PrimaryObjective {
        private Long primaryObjectiveId;
        private Long contentAreaId;
        private String primaryObjectiveName;
        private String primaryObjectiveType;
        private Long primaryObjectiveNumItems;
        private Long primaryObjectivePointsPossible;
        private Long subtestId;
        private String subtestForm;
        private String subtestLevel;
        private Long primaryObjectiveIndex;
        private BigDecimal nationalAverage;
        private Long productId;
        private Integer highMasteryRange;
        private Integer lowMasteryRange;
        private String monarchId;
        
        public PrimaryObjective() {
			super();
		}
		public PrimaryObjective(PrimaryObjective primaryObj) {
			this.primaryObjectiveId = primaryObj.getPrimaryObjectiveId();
		    this.contentAreaId = primaryObj.getContentAreaId();
	        this.primaryObjectiveName = primaryObj.getPrimaryObjectiveName();
	        this.primaryObjectiveType = primaryObj.getPrimaryObjectiveType();
	        this.primaryObjectiveNumItems = primaryObj.getPrimaryObjectiveNumItems();
	        this.primaryObjectivePointsPossible = primaryObj.getPrimaryObjectivePointsPossible();
	        this.subtestId = primaryObj.getSubtestId();
	        this.subtestForm = primaryObj.getSubtestForm();
	        this.subtestLevel = primaryObj.getSubtestLevel();
	        this.primaryObjectiveIndex = primaryObj.getPrimaryObjectiveIndex();
	        this.nationalAverage = primaryObj.getNationalAverage();
	        this.productId = primaryObj.getProductId();
	        this.highMasteryRange = primaryObj.getHighMasteryRange();
	        this.lowMasteryRange = primaryObj.getLowMasteryRange();
	        this.monarchId = primaryObj.getMonarchId();
		}
        public Long getProductId() {
			return productId;
		}
		
		public void setProductId(Long productId) {
			this.productId = productId;
		}
        
        /**
		 * @return Returns the nationalAverage.
		 */
		public BigDecimal getNationalAverage() {
			return nationalAverage;
		}
		/**
		 * @param nationalAverage The nationalAverage to set.
		 */
		public void setNationalAverage(BigDecimal nationalAverage) {
			this.nationalAverage = nationalAverage;
		}
        
        /**
		 * @return Returns the primaryObjectiveIndex.
		 */
		public Long getPrimaryObjectiveIndex() {
			return primaryObjectiveIndex;
		}
		/**
		 * @param primaryObjectiveIndex The primaryObjectiveIndex to set.
		 */
		public void setPrimaryObjectiveIndex(Long primaryObjectiveIndex) {
			this.primaryObjectiveIndex = primaryObjectiveIndex;
		}
		/**
		 * @return Returns the subtestForm.
		 */
		public String getSubtestForm() {
			return subtestForm;
		}
		/**
		 * @param subtestForm The subtestForm to set.
		 */
		public void setSubtestForm(String subtestForm) {
			this.subtestForm = subtestForm;
		}
		/**
		 * @return Returns the subtestId.
		 */
		public Long getSubtestId() {
			return subtestId;
		}
		/**
		 * @param subtestId The subtestId to set.
		 */
		public void setSubtestId(Long subtestId) {
			this.subtestId = subtestId;
		}
		/**
		 * @return Returns the subtestLevel.
		 */
		public String getSubtestLevel() {
			return subtestLevel;
		}
		/**
		 * @param subtestLevel The subtestLevel to set.
		 */
		public void setSubtestLevel(String subtestLevel) {
			this.subtestLevel = subtestLevel;
		}
		/**
		 * @return Returns the contentAreaId.
		 */
		public Long getContentAreaId() {
			return contentAreaId;
		}
		/**
		 * @param contentAreaId The contentAreaId to set.
		 */
		public void setContentAreaId(Long contentAreaId) {
			this.contentAreaId = contentAreaId;
		}
		/**
		 * @return Returns the primaryObjectiveId.
		 */
		public Long getPrimaryObjectiveId() {
			return primaryObjectiveId;
		}
		/**
		 * @param primaryObjectiveId The primaryObjectiveId to set.
		 */
		public void setPrimaryObjectiveId(Long primaryObjectiveId) {
			this.primaryObjectiveId = primaryObjectiveId;
		}
		/**
		 * @return Returns the primaryObjectiveName.
		 */
		public String getPrimaryObjectiveName() {
			return primaryObjectiveName;
		}
		/**
		 * @param primaryObjectiveName The primaryObjectiveName to set.
		 */
		public void setPrimaryObjectiveName(String primaryObjectiveName) {
			this.primaryObjectiveName = primaryObjectiveName;
		}
		/**
		 * @return Returns the primaryObjectiveNumItems.
		 */
		public Long getPrimaryObjectiveNumItems() {
			return primaryObjectiveNumItems;
		}
		/**
		 * @param primaryObjectiveNumItems The primaryObjectiveNumItems to set.
		 */
		public void setPrimaryObjectiveNumItems(Long primaryObjectiveNumItems) {
			this.primaryObjectiveNumItems = primaryObjectiveNumItems;
		}
		/**
		 * @return Returns the primaryObjectivePointsPossible.
		 */
		public Long getPrimaryObjectivePointsPossible() {
			return primaryObjectivePointsPossible;
		}
		/**
		 * @param primaryObjectivePointsPossible The primaryObjectivePointsPossible to set.
		 */
		public void setPrimaryObjectivePointsPossible(
				Long primaryObjectivePointsPossible) {
			this.primaryObjectivePointsPossible = primaryObjectivePointsPossible;
		}
		/**
		 * @return Returns the primaryObjectiveType.
		 */
		public String getPrimaryObjectiveType() {
			return primaryObjectiveType;
		}
		/**
		 * @param primaryObjectiveType The primaryObjectiveType to set.
		 */
		public void setPrimaryObjectiveType(String primaryObjectiveType) {
			this.primaryObjectiveType = primaryObjectiveType;
		}
		/**
		 * @return Returns the highMasteryRange.
		 */
		public Integer getHighMasteryRange() {
			return highMasteryRange;
		}
		/**
		 * @param highMasteryRange The highMasteryRange to set.
		 */
		public void setHighMasteryRange(Integer highMasteryRange) {
			this.highMasteryRange = highMasteryRange;
		}
		/**
		 * @return Returns the lowMasteryRange.
		 */
		public Integer getLowMasteryRange() {
			return lowMasteryRange;
		}
		/**
		 * @param lowMasteryRange The lowMasteryRange to set.
		 */
		public void setLowMasteryRange(Integer lowMasteryRange) {
			this.lowMasteryRange = lowMasteryRange;
		}

		public String getMonarchId() {
			return monarchId;
		}

		public void setMonarchId(String monarchId) {
			this.monarchId = monarchId;
		}
    }
    
    public static class SecondaryObjective {
        private Long secondaryObjectiveId;
        private Long primaryObjectiveId;
        private String secondaryObjectiveName;
        private String secondaryObjectiveType;
        private Long secondaryObjectiveNumItems;
        private Long secondaryObjectivePointsPossible;
        private Long subtestId;
        private String subtestForm;
        private String subtestLevel;
        private String subtestName;
        private Long productId;
        private String monarchId;
        
        public Long getProductId() {
			return productId;
		}
		
		public void setProductId(Long productId) {
			this.productId = productId;
		}

        /**
		 * @return Returns the subtestName.
		 */
		public String getSubtestName() {
			return subtestName;
		}
		/**
		 * @param subtestName The subtestName to set.
		 */
		public void setSubtestName(String subtestName) {
			this.subtestName = subtestName;
		}
		/**
		 * @return Returns the subtestForm.
		 */
		public String getSubtestForm() {
			return subtestForm;
		}
		/**
		 * @param subtestForm The subtestForm to set.
		 */
		public void setSubtestForm(String subtestForm) {
			this.subtestForm = subtestForm;
		}
		/**
		 * @return Returns the subtestId.
		 */
		public Long getSubtestId() {
			return subtestId;
		}
		/**
		 * @param subtestId The subtestId to set.
		 */
		public void setSubtestId(Long subtestId) {
			this.subtestId = subtestId;
		}
		/**
		 * @return Returns the subtestLevel.
		 */
		public String getSubtestLevel() {
			return subtestLevel;
		}
		/**
		 * @param subtestLevel The subtestLevel to set.
		 */
		public void setSubtestLevel(String subtestLevel) {
			this.subtestLevel = subtestLevel;
		}
		/**
		 * @return Returns the primaryObjectiveId.
		 */
		public Long getPrimaryObjectiveId() {
			return primaryObjectiveId;
		}
		/**
		 * @param primaryObjectiveId The primaryObjectiveId to set.
		 */
		public void setPrimaryObjectiveId(Long primaryObjectiveId) {
			this.primaryObjectiveId = primaryObjectiveId;
		}
		/**
		 * @return Returns the secondaryObjectiveId.
		 */
		public Long getSecondaryObjectiveId() {
			return secondaryObjectiveId;
		}
		/**
		 * @param secondaryObjectiveId The secondaryObjectiveId to set.
		 */
		public void setSecondaryObjectiveId(Long secondaryObjectiveId) {
			this.secondaryObjectiveId = secondaryObjectiveId;
		}
		/**
		 * @return Returns the secondaryObjectiveName.
		 */
		public String getSecondaryObjectiveName() {
			return secondaryObjectiveName;
		}
		/**
		 * @param secondaryObjectiveName The secondaryObjectiveName to set.
		 */
		public void setSecondaryObjectiveName(String secondaryObjectiveName) {
			this.secondaryObjectiveName = secondaryObjectiveName;
		}
		/**
		 * @return Returns the secondaryObjectiveNumItems.
		 */
		public Long getSecondaryObjectiveNumItems() {
			return secondaryObjectiveNumItems;
		}
		/**
		 * @param secondaryObjectiveNumItems The secondaryObjectiveNumItems to set.
		 */
		public void setSecondaryObjectiveNumItems(Long secondaryObjectiveNumItems) {
			this.secondaryObjectiveNumItems = secondaryObjectiveNumItems;
		}
		/**
		 * @return Returns the secondaryObjectivePointsPossible.
		 */
		public Long getSecondaryObjectivePointsPossible() {
			return secondaryObjectivePointsPossible;
		}
		/**
		 * @param secondaryObjectivePointsPossible The secondaryObjectivePointsPossible to set.
		 */
		public void setSecondaryObjectivePointsPossible(
				Long secondaryObjectivePointsPossible) {
			this.secondaryObjectivePointsPossible = secondaryObjectivePointsPossible;
		}
		/**
		 * @return Returns the secondaryObjectiveType.
		 */
		public String getSecondaryObjectiveType() {
			return secondaryObjectiveType;
		}
		/**
		 * @param secondaryObjectiveType The secondaryObjectiveType to set.
		 */
		public void setSecondaryObjectiveType(String secondaryObjectiveType) {
			this.secondaryObjectiveType = secondaryObjectiveType;
		}

		public String getMonarchId() {
			return monarchId;
		}

		public void setMonarchId(String monarchId) {
			this.monarchId = monarchId;
		}
    }
    
    public static class Item {
        private String oasItemId;
        private Long secondaryObjectiveId;
        private String itemText;
        private Long itemIndex;
        private String itemType;
        private String itemCorrectResponse;
        private Long itemPointsPossible;
        private Long itemId;
        private Long subtestId;
        private String subtestForm;
        private String subtestLevel;
        private String subtestName;
        private HashMap nationalAverage;
        
        public Item () {
            this.nationalAverage = new HashMap(30);
        }
        
        public BigDecimal getNationalAverage(String groupgrade) {
            BigDecimal natAvg = (BigDecimal) this.nationalAverage.get(groupgrade);
            if(natAvg != null) {
                return natAvg;
            } else {
                return new BigDecimal(-1.0);
            }
        }
        
        public void setNationalAverage(String groupgrade, BigDecimal nationalAverage) {
            this.nationalAverage.put(groupgrade, nationalAverage);
        }

        /**
		 * @return Returns the subtestName.
		 */
		public String getSubtestName() {
			return subtestName;
		}
		/**
		 * @param subtestName The subtestName to set.
		 */
		public void setSubtestName(String subtestName) {
			this.subtestName = subtestName;
		}
		/**
		 * @return Returns the subtestForm.
		 */
		public String getSubtestForm() {
			return subtestForm;
		}
		/**
		 * @param subtestForm The subtestForm to set.
		 */
		public void setSubtestForm(String subtestForm) {
			this.subtestForm = subtestForm;
		}
		/**
		 * @return Returns the subtestId.
		 */
		public Long getSubtestId() {
			return subtestId;
		}
		/**
		 * @param subtestId The subtestId to set.
		 */
		public void setSubtestId(Long subtestId) {
			this.subtestId = subtestId;
		}
		/**
		 * @return Returns the subtestLevel.
		 */
		public String getSubtestLevel() {
			return subtestLevel;
		}
		/**
		 * @param subtestLevel The subtestLevel to set.
		 */
		public void setSubtestLevel(String subtestLevel) {
			this.subtestLevel = subtestLevel;
		}
		/**
		 * @return Returns the itemCorrectResponse.
		 */
		public String getItemCorrectResponse() {
			return itemCorrectResponse;
		}
		/**
		 * @param itemCorrectResponse The itemCorrectResponse to set.
		 */
		public void setItemCorrectResponse(String itemCorrectResponse) {
			this.itemCorrectResponse = itemCorrectResponse;
		}
		/**
		 * @return Returns the itemIndex.
		 */
		public Long getItemIndex() {
			return itemIndex;
		}
		/**
		 * @param itemIndex The itemIndex to set.
		 */
		public void setItemIndex(Long itemIndex) {
			this.itemIndex = itemIndex;
		}
		/**
		 * @return Returns the itemPointsPossible.
		 */
		public Long getItemPointsPossible() {
			return itemPointsPossible;
		}
		/**
		 * @param itemPointsPossible The itemPointsPossible to set.
		 */
		public void setItemPointsPossible(Long itemPointsPossible) {
			this.itemPointsPossible = itemPointsPossible;
		}
		/**
		 * @return Returns the itemText.
		 */
		public String getItemText() {
			return itemText;
		}
		/**
		 * @param itemText The itemText to set.
		 */
		public void setItemText(String itemText) {
			this.itemText = itemText;
		}
		/**
		 * @return Returns the itemType.
		 */
		public String getItemType() {
			return itemType;
		}
		/**
		 * @param itemType The itemType to set.
		 */
		public void setItemType(String itemType) {
			this.itemType = itemType;
		}
		/**
		 * @return Returns the oasItemId.
		 */
		public String getOasItemId() {
			return oasItemId;
		}
		/**
		 * @param oasItemId The oasItemId to set.
		 */
		public void setOasItemId(String oasItemId) {
			this.oasItemId = oasItemId;
		}
        /**
		 * @return Returns the itemId.
		 */
		public Long getItemId() {
			return itemId;
		}
		/**
		 * @param itemId The itemId to set.
		 */
		public void setItemId(Long itemId) {
			this.itemId = itemId;
		}
		/**
		 * @return Returns the secondaryObjectiveId.
		 */
		public Long getSecondaryObjectiveId() {
			return secondaryObjectiveId;
		}
		/**
		 * @param secondaryObjectiveId The secondaryObjectiveId to set.
		 */
		public void setSecondaryObjectiveId(Long secondaryObjectiveId) {
			this.secondaryObjectiveId = secondaryObjectiveId;
		}
    }
    
    private Composite [] composites;
    private ContentArea [] contentAreas;
    private ContentArea [] allContentAreas;
    private PrimaryObjective [] primaryObjectives;
    private SecondaryObjective [] secondaryObjectives;
    private Item [] items;
    
    public Item getItemForOASItemId(String oasItemId) {
        for(int i=0;i<items.length;i++) {
            if(oasItemId.equals(items[i].getOasItemId())) {
                return items[i];
            }
        }
        return null;
    }
    
    public SecondaryObjective getSecObjById(Long secObjId) {
        for(int i=0;i<secondaryObjectives.length;i++) {
            if(secObjId.equals(secondaryObjectives[i].getSecondaryObjectiveId())) {
                return secondaryObjectives[i];
            }
        }
        return null;
    }
    
    public PrimaryObjective getPrimObjById(Long primObjId) {
        for(int i=0;i<primaryObjectives.length;i++) {
            if(primObjId.equals(primaryObjectives[i].getPrimaryObjectiveId())) {
                return primaryObjectives[i];
            }
        }
        return null;
    }
    
    public List getContentAreasByName(String name) {
        ArrayList matches = new ArrayList();
        for(int i=0;i<contentAreas.length;i++) {
            if(name.equals(contentAreas[i].getContentAreaName())) {
                matches.add(contentAreas[i]);
            }
        }
        return matches;
    }
    
	/**
	 * @return Returns the composites.
	 */
	public Composite[] getComposites() {
		return composites;
	}
	/**
	 * @param composites The composites to set.
	 */
	public void setComposites(Composite[] composites) {
		this.composites = composites;
	}
	/**
	 * @return Returns the contentAreas.
	 */
	public ContentArea[] getContentAreas() {
		return contentAreas;
	}
	/**
	 * @param contentAreas The contentAreas to set.
	 */
	public void setContentAreas(ContentArea[] contentAreas) {
		this.contentAreas = contentAreas;
	}
	/**
	 * @return Returns the contentAreas.
	 */
	public ContentArea[] getAllContentAreas() {
		return allContentAreas;
	}
	/**
	 * @param contentAreas The contentAreas to set.
	 */
	public void setAllContentAreas(ContentArea[] allContentAreas) {
		this.allContentAreas = allContentAreas;
	}
	/**
	 * @return Returns the items.
	 */
	public Item[] getItems() {
		return items;
	}
	/**
	 * @param items The items to set.
	 */
	public void setItems(Item[] items) {
		this.items = items;
	}
	/**
	 * @return Returns the primaryObjectives.
	 */
	public PrimaryObjective[] getPrimaryObjectives() {
		return primaryObjectives;
	}
	/**
	 * @param primaryObjectives The primaryObjectives to set.
	 */
	public void setPrimaryObjectives(PrimaryObjective[] primaryObjectives) {
		this.primaryObjectives = primaryObjectives;
	}
	/**
	 * @return Returns the secondaryObjectives.
	 */
	public SecondaryObjective[] getSecondaryObjectives() {
		return secondaryObjectives;
	}
	/**
	 * @param secondaryObjectives The secondaryObjectives to set.
	 */
	public void setSecondaryObjectives(SecondaryObjective[] secondaryObjectives) {
		this.secondaryObjectives = secondaryObjectives;
	}
}