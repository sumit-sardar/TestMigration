package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.CurriculumData.Item;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.util.SQLUtil;
import java.math.BigDecimal;
import java.util.HashMap;

public class CurriculumCollector {
    private final Connection conn;

    public CurriculumCollector(Connection conn) {
        this.conn = conn;
    }

    public CurriculumData collectCurriculumData(Long oasRosterId, String productType) throws SQLException {
        CurriculumData data = new CurriculumData();
        data.setComposites(getComposites(oasRosterId, productType));
        if (productType.equalsIgnoreCase("TA")) {
        	data.setContentAreas(getContentAreasForAdaptive(oasRosterId));
        	data.setPrimaryObjectives(getPrimaryObjectivesForTabeCat(oasRosterId));   
        } else {       
        	data.setContentAreas(getContentAreas(oasRosterId));	  
        	data.setPrimaryObjectives(getPrimaryObjectives(oasRosterId));
        }
        
        data.setSecondaryObjectives(getSecondaryObjectives(oasRosterId));
        data.setItems(getItems(oasRosterId));
        
        return data;
    }
    
    public Composite [] getComposites(Long oasRosterId, String productType) throws SQLException {
        String assessmentType = "Battery";
        String level = "0";
        String form = "A";  // For Laslink Scoring
        
        final String casql = 
            "select distinct " +
            "   prod.internal_display_name as assessmentType, "+
            "   iset.item_Set_level as itemSetLevel, " +
            "   iset.item_Set_form as itemSetForm " +
            "from " +  
            "   test_roster ros, " + 
            "   test_Admin adm, " + 
            "   product prod,  " +
            "   student_item_Set_status siss, " +
            "   item_Set iset " + 
            "where " + 
            "   ros.test_roster_id = ? " +
            "   and adm.test_admin_id = ros.test_admin_id " +
            "   and prod.product_id = adm.product_id " +
            "   and siss.test_roster_id = ros.test_roster_id " +
            "   and iset.item_set_id = siss.item_Set_id";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                assessmentType = (rs.getString("assessmentType"));
                level = (rs.getString("itemSetLevel"));
                form = (rs.getString("itemSetForm")); // For Laslink Scoring
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
            
        if("TB".equals(productType)) {
            Composite [] composites = new Composite[2];
            
            if("TABE 9 Battery".equals(assessmentType)) {
                composites[0] = new Composite();
                composites[0].setCompositeId(new Long(1));
                composites[0].setCompositeName("Total Mathematics");
                composites[0].setCompositeType("TABE COMPOSITE");
                composites[0].setSubject("TB Total Mathematics");
                composites[0].setCompositePointsPossible(new Long(90));
                composites[0].setCompositeNumItems(new Long(90));
                composites[1] = new Composite();
                composites[1].setCompositeId(new Long(2));
                composites[1].setCompositeName("Total Battery");
                composites[1].setCompositeType("TABE COMPOSITE");
                composites[1].setSubject("TB Total Battery");
                composites[1].setCompositePointsPossible(new Long(195));
                composites[1].setCompositeNumItems(new Long(195));
            } else if("TABE 9 Survey".equals(assessmentType)) {
                composites[0] = new Composite();
                composites[0].setCompositeId(new Long(6));
                composites[0].setCompositeName("Total Mathematics");
                composites[0].setCompositeType("TABE COMPOSITE");
                composites[0].setSubject("TB Total Mathematics");
                composites[0].setCompositePointsPossible(new Long(50));
                composites[0].setCompositeNumItems(new Long(50));
                composites[1] = new Composite();
                composites[1].setCompositeId(new Long(7));
                composites[1].setCompositeName("Total Battery");
                composites[1].setCompositeType("TABE COMPOSITE");
                composites[1].setSubject("TB Total Battery");
                composites[1].setCompositePointsPossible(new Long(100));
                composites[1].setCompositeNumItems(new Long(100));
            } else if("TABE 10 Battery".equals(assessmentType)) {
                composites[0] = new Composite();
                composites[0].setCompositeId(new Long(8));
                composites[0].setCompositeName("Total Mathematics");
                composites[0].setCompositeType("TABE COMPOSITE");
                composites[0].setSubject("TB Total Mathematics");
                composites[0].setCompositePointsPossible(new Long(90));
                composites[0].setCompositeNumItems(new Long(90));
                composites[1] = new Composite();
                composites[1].setCompositeId(new Long(9));
                composites[1].setCompositeName("Total Battery");
                composites[1].setCompositeType("TABE COMPOSITE");
                composites[1].setSubject("TB Total Battery");
                composites[1].setCompositePointsPossible(new Long(195));
                composites[1].setCompositeNumItems(new Long(195));
            } else if("TABE 10 Survey".equals(assessmentType)) {
                composites[0] = new Composite();
                composites[0].setCompositeId(new Long(10));
                composites[0].setCompositeName("Total Mathematics");
                composites[0].setCompositeType("TABE COMPOSITE");
                composites[0].setSubject("TB Total Mathematics");
                composites[0].setCompositePointsPossible(new Long(50));
                composites[0].setCompositeNumItems(new Long(50));
                composites[1] = new Composite();
                composites[1].setCompositeId(new Long(11));
                composites[1].setCompositeName("Total Battery");
                composites[1].setCompositeType("TABE COMPOSITE");
                composites[1].setSubject("TB Total Battery");
                composites[1].setCompositePointsPossible(new Long(100));
                composites[1].setCompositeNumItems(new Long(100));
            }
            
            return composites;
        } else if ("TL".equals(productType)) {
            Composite [] composites = new Composite[3];
            composites[0] = new Composite();
            composites[0].setCompositeId(new Long(3));
            composites[0].setCompositeName("Reading");
            composites[0].setCompositeType("TABE LOCATOR COMPOSITE");
            composites[0].setSubject("TL Reading");
            composites[0].setCompositeNumItems(new Long(12));
            composites[0].setCompositePointsPossible(new Long(12));
            composites[1] = new Composite();
            composites[1].setCompositeId(new Long(4));
            composites[1].setCompositeName("Language");
            composites[1].setCompositeType("TABE LOCATOR COMPOSITE");
            composites[1].setSubject("TL Language");
            composites[1].setCompositeNumItems(new Long(12));
            composites[1].setCompositePointsPossible(new Long(12));
            composites[2] = new Composite();
            composites[2].setCompositeId(new Long(5));
            composites[2].setCompositeName("Mathematics");
            composites[2].setCompositeType("TABE LOCATOR COMPOSITE");
            composites[2].setSubject("TL Mathematics");
            composites[2].setCompositeNumItems(new Long(16));
            composites[2].setCompositePointsPossible(new Long(16));
            return composites;
        } else if ("TV".equals(productType)) {
            Composite [] composites = new Composite[1];
            composites[0] = new Composite();
            composites[0].setCompositeName("Total Score");
            composites[0].setCompositeType("TERRANOVA COMPOSITE");
            composites[0].setSubject("TV Total Score");
            if(level != null && level.indexOf("19") >= 0) {
                composites[0].setAssessmentId(new Long(72153));
                composites[0].setCompositeId(new Long(12));
                composites[0].setCompositeNumItems(new Long(80));
                composites[0].setCompositePointsPossible(new Long(80));
            } else if("18".equals(level)) {
                composites[0].setAssessmentId(new Long(72272));
                composites[0].setCompositeId(new Long(13));
                composites[0].setCompositeNumItems(new Long(86));
                composites[0].setCompositePointsPossible(new Long(86));
            } else if("17".equals(level)) {
                composites[0].setAssessmentId(new Long(72252));
                composites[0].setCompositeId(new Long(14));
                composites[0].setCompositeNumItems(new Long(87));
                composites[0].setCompositePointsPossible(new Long(87));
            } else if("16".equals(level)) {
                composites[0].setAssessmentId(new Long(72232));
                composites[0].setCompositeId(new Long(15));
                composites[0].setCompositeNumItems(new Long(86));
                composites[0].setCompositePointsPossible(new Long(86));
            } else if("15".equals(level)) {
                composites[0].setAssessmentId(new Long(72152));
                composites[0].setCompositeId(new Long(16));
                composites[0].setCompositeNumItems(new Long(87));
                composites[0].setCompositePointsPossible(new Long(87));
            } else if("14".equals(level)) {
                composites[0].setAssessmentId(new Long(72212));
                composites[0].setCompositeId(new Long(17));
                composites[0].setCompositeNumItems(new Long(87));
                composites[0].setCompositePointsPossible(new Long(87));
            } else if("13".equals(level)) {
                composites[0].setAssessmentId(new Long(72192));
                composites[0].setCompositeId(new Long(18));
                composites[0].setCompositeNumItems(new Long(80));
                composites[0].setCompositePointsPossible(new Long(80));
            }
            return composites;
        } 
        // For Laslink Scoring
        else if ("LL".equals(productType)) {
        	Composite [] composites = new Composite[1];
            composites[0] = new Composite();
            composites[0].setCompositeName("Overall Score");
            composites[0].setCompositeType("LL OVERALL COMPOSITE");
            composites[0].setSubject("LL Overall Score");

            if("Espa?ol".equals(form) || "Espanol".equals(form) || "Español".equals(form))
            {
              if(level != null && "K".equals(level)) {
            	composites[0].setAssessmentId(new Long(13337));
                composites[0].setCompositeId(new Long(19));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("9-12".equals(level)) {
                composites[0].setAssessmentId(new Long(13341));
                composites[0].setCompositeId(new Long(20));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              } else if("6-8".equals(level)) {
                composites[0].setAssessmentId(new Long(13340));
                composites[0].setCompositeId(new Long(21));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("4-5".equals(level)) {
                composites[0].setAssessmentId(new Long(13217));
                composites[0].setCompositeId(new Long(22));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("2-3".equals(level)) {
                composites[0].setAssessmentId(new Long(13339));
                composites[0].setCompositeId(new Long(23));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("1".equals(level)) {
                composites[0].setAssessmentId(new Long(13338));
                composites[0].setCompositeId(new Long(24));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
            }
           }else if("A".equals(form)){

        	   if(level != null && "K".equals(level)) {
               	composites[0].setAssessmentId(new Long(29126));
                   composites[0].setCompositeId(new Long(25));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 }else if("9-12".equals(level)) {
                   composites[0].setAssessmentId(new Long(29355));
                   composites[0].setCompositeId(new Long(26));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 } else if("6-8".equals(level)) {
                   composites[0].setAssessmentId(new Long(29346));
                   composites[0].setCompositeId(new Long(27));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 }else if("4-5".equals(level)) {
                   composites[0].setAssessmentId(new Long(29077));
                   composites[0].setCompositeId(new Long(28));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 }else if("2-3".equals(level)) {
                   composites[0].setAssessmentId(new Long(29304));
                   composites[0].setCompositeId(new Long(29));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 }else if("1".equals(level)) {
                   composites[0].setAssessmentId(new Long(29255));
                   composites[0].setCompositeId(new Long(30));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
               }

           }else if("B".equals(form)){

        	   if(level != null && "K".equals(level)) {
            	composites[0].setAssessmentId(new Long(29364));
                composites[0].setCompositeId(new Long(31));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("9-12".equals(level)) {
                composites[0].setAssessmentId(new Long(29418));
                composites[0].setCompositeId(new Long(32));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              } else if("6-8".equals(level)) {
                composites[0].setAssessmentId(new Long(29502));
                composites[0].setCompositeId(new Long(33));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("4-5".equals(level)) {
                composites[0].setAssessmentId(new Long(29086));
                composites[0].setCompositeId(new Long(34));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("2-3".equals(level)) {
                composites[0].setAssessmentId(new Long(29382));
                composites[0].setCompositeId(new Long(35));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("1".equals(level)) {
                composites[0].setAssessmentId(new Long(29373));
                composites[0].setCompositeId(new Long(36));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
            }
           } 
        	return composites;
        } else if ("TA".equals(productType)) {
        	Composite [] composites = new Composite[2];
        	 composites[0] = new Composite();
             composites[0].setCompositeId(new Long(37));
             composites[0].setCompositeName("Total Mathematics");
             composites[0].setCompositeType("TABE ADAPTIVE COMPOSITE");
             composites[0].setSubject("TA Total Mathematics");
           //  composites[0].setCompositePointsPossible(new Long(90));
          //   composites[0].setCompositeNumItems(new Long(90));
             composites[1] = new Composite();
             composites[1].setCompositeId(new Long(38));
             composites[1].setCompositeName("Total Battery");
             composites[1].setCompositeType("TABE ADAPTIVE COMPOSITE");
             composites[1].setSubject("TA Total Battery");
           //  composites[1].setCompositePointsPossible(new Long(195));
          //   composites[1].setCompositeNumItems(new Long(195));
     	   
     	   
     	   
     	  return composites;
        }
        return new Composite[0];
    }
    
    public ContentArea [] getContentAreas(Long oasRosterId) throws SQLException {
        HashMap caMap = new HashMap();
        ArrayList contentAreas = new ArrayList();
        final String casql = 
        	"select distinct " +
            "   prod.product_id || ca.item_Set_id as contentAreaId, " + 
            "   ca.item_set_name as contentAreaName, " + 
            "   prod.product_type || ' CONTENT AREA' as contentAreaType, " + 
            "   prod.product_type || ' ' || ca.item_Set_name as subject, " +
            "   sum(dp.max_points) as contentAreaPointsPossible, " +
            "   count(distinct item.item_id) as contentAreaNumItems, " +
            "   decode(prod.internal_display_name, 'TABE 9 Survey', '9', 'TABE 9 Battery', '9', 'TABE 10 Survey', '10', 'TABE 10 Battery', '10', td.item_set_form) as subtestForm, " +
            "   td.item_set_level as subtestLevel, " +
            "   td.item_set_id as subtestId " + 
            "from " +  
            "   item, " +
            "   item_set ca, " +
            "   item_Set_category cacat, " +
            "   item_Set_ancestor caisa, " +
            "   item_set_item caisi, " +
            "   item_Set_ancestor tcisa, " +
            "   item_set_item tcisi, " + 
            "   datapoint dp, " +
            "   test_roster ros, " + 
            "   test_Admin adm, " + 
            "   test_catalog tc, " + 
            "   product prod, " +
            "   item_set td " + 
            "where " + 
            "   ros.test_roster_id = ? " +
            "   and adm.test_admin_id = ros.test_admin_id " +
            "   and tc.test_catalog_id = adm.test_catalog_id " +
            "   and prod.product_id = tc.product_id " +
            "   and item.ACTIVATION_STATUS = 'AC' " + 
            "   and tc.ACTIVATION_STATUS = 'AC' " + 
            "   and ca.item_Set_id = caisa.ancestor_item_Set_id " +
            "   and ca.item_set_type = 'RE' " + 
            "   and caisa.item_set_id = caisi.item_Set_id " +
            "   and item.item_id = caisi.item_id " +
            "   and tcisi.item_id = item.item_id " +
            "   and tcisa.item_set_id = tcisi.item_set_id " +
            "   and adm.item_set_id = tcisa.ancestor_item_set_id " +
            "   and cacat.item_Set_category_id = ca.item_set_category_id " + 
            "   and cacat.item_set_category_level = prod.content_area_level " +
            "   and dp.item_id = item.item_id " +
            "   and dp.item_set_id = caisi.item_Set_id " +
            "   and td.item_set_id = tcisi.item_set_id " +
            "   and td.sample = 'F' " +
            "   AND (td.item_set_level != 'L' OR PROD.PRODUCT_TYPE = 'TL') " + 
            "   and cacat.framework_product_id = prod.PARENT_PRODUCT_ID " +
            " group by " +
            "   prod.product_id || ca.item_Set_id, " + 
            "   ca.item_set_name, " + 
            "   prod.product_type || ' CONTENT AREA', " + 
            "   prod.product_type || ' ' || ca.item_Set_name, " + 
            "   decode(prod.internal_display_name, 'TABE 9 Survey', '9', 'TABE 9 Battery', '9', 'TABE 10 Survey', '10', 'TABE 10 Battery', '10', td.item_set_form), " + 
            "   td.item_Set_level, " + 
            "   td.item_Set_id";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                ContentArea contentArea = new ContentArea();
                contentArea.setContentAreaId(new Long(rs.getLong("contentAreaId")));
                contentArea.setContentAreaName(rs.getString("contentAreaName"));
                contentArea.setContentAreaType(rs.getString("contentAreaType"));
                contentArea.setSubject(rs.getString("subject"));
                contentArea.setContentAreaNumItems(new Long(rs.getLong("contentAreaNumItems")));
                contentArea.setContentAreaPointsPossible(new Long(rs.getLong("contentAreaPointsPossible")));
                contentArea.setSubtestId(new Long(rs.getLong("subtestId")));
                contentArea.setSubtestForm(rs.getString("subtestForm"));
                contentArea.setSubtestLevel(rs.getString("subtestLevel"));

                String key = contentArea.getContentAreaName() + "||" + contentArea.getSubtestLevel();

                if(caMap.containsKey(key)) {
                    ContentArea ca1 = (ContentArea) caMap.get(key);
                    Long numItems = new Long(ca1.getContentAreaNumItems().longValue() + contentArea.getContentAreaNumItems().longValue());
                    Long points = new Long(ca1.getContentAreaPointsPossible().longValue() + contentArea.getContentAreaPointsPossible().longValue());
                    contentArea.setContentAreaNumItems(numItems);
                    contentArea.setContentAreaPointsPossible(points);
                    ca1.setContentAreaNumItems(numItems);
                    ca1.setContentAreaPointsPossible(points);
                }   
                caMap.put(key, contentArea);    
                contentAreas.add(contentArea);
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        return (ContentArea []) contentAreas.toArray(new ContentArea[0]);
    }
    
    public PrimaryObjective [] getPrimaryObjectives(Long oasRosterId) throws SQLException {
        ArrayList primaryObjectives = new ArrayList();
        HashMap poMap = new HashMap();
        
        final String casql = 
        	"select primaryObjectiveId, " + 
            "  rownum as primaryObjectiveIndex, " + 
            "  contentAreaId, " + 
            "  primaryObjectiveName, " +  
            "  primaryObjectiveType, " + 
            "  primaryPointsPossible, " + 
            "  primaryNumItems, " +  
            "  subtestForm, " + 
            "  subtestLevel, " + 
            "  subtestId, " +
            "  productId " +  
            " from (select distinct " +
            "   prim.item_Set_id as primaryObjectiveId, " +
            "   prod.product_id || ca.item_Set_id as contentAreaId, " +
            "   prim.item_set_name as primaryObjectiveName, " + 
            "   primcat.ITEM_SET_CATEGORY_NAME as primaryObjectiveType, " +
            "   sum(dp.max_points) as primaryPointsPossible, " +
            "   count(distinct item.item_id) as primaryNumItems, " + 
            "   decode(prod.internal_display_name, 'TABE 9 Survey', '9', 'TABE 9 Battery', '9', 'TABE 10 Survey', '10', 'TABE 10 Battery', '10', td.item_set_form) as subtestForm, " +
            "   td.item_set_level as subtestLevel, " +
            "   td.item_set_id as subtestId, " +
            "   prod.product_id as productId " + 
            "from " +  
            "   item, " +
            "   item_set prim, " +
            "   item_Set_category primcat, " +
            "   item_Set_ancestor primisa, " +
            "   item_set ca, " +
            "   item_Set_category cacat, " +
            "   item_Set_ancestor caisa, " +
            "   item_set_item primisi, " +
            "   item_Set_ancestor tcisa, " +
            "   item_set_item tcisi, " + 
            "   datapoint dp, " + 
            "   test_roster ros, " + 
            "   test_Admin adm, " + 
            "   test_catalog tc, " + 
            "   product prod, " +
            "   item_set td " +
            "where " + 
            "   ros.test_roster_id = ? " +
            "   and adm.test_admin_id = ros.test_admin_id " +
            "   and tc.test_catalog_id = adm.test_catalog_id " +
            "   and prod.product_id = tc.product_id " +
            "   and item.ACTIVATION_STATUS = 'AC' " + 
            "   and tc.ACTIVATION_STATUS = 'AC' " + 
            "   and prim.item_Set_id = primisa.ancestor_item_Set_id " +
            "   and prim.item_set_type = 'RE' " + 
            "   and primisa.item_set_id = primisi.item_Set_id " +
            "   and item.item_id = primisi.item_id " +
            "   and tcisi.item_id = item.item_id " +
            "   and tcisa.item_set_id = tcisi.item_set_id " +
            "   and adm.item_set_id = tcisa.ancestor_item_set_id " +
            "   and primcat.item_Set_category_id = prim.item_set_category_id " + 
            "   and primcat.item_set_category_level = prod.scoring_item_Set_level " +
            "   and dp.item_id = item.item_id " +
            "   and dp.item_set_id = primisi.item_Set_id " +
            "   and caisa.item_Set_id = prim.item_Set_id " +
            "   and ca.item_set_id = caisa.ancestor_item_Set_id " +
            "   and cacat.item_Set_category_id = ca.item_set_category_id " + 
            "   and cacat.item_set_category_level = prod.content_area_level " +
            "   and td.item_set_id = tcisi.item_set_id " +
            "   and td.sample = 'F' " +
            "   AND (td.item_set_level != 'L' OR PROD.PRODUCT_TYPE = 'TL') " +
            "   and primcat.framework_product_id = prod.PARENT_PRODUCT_ID " +
            " group by " +
            "   prim.item_Set_id, " +
            "   prod.product_id || ca.item_set_id, " +
            "   prim.item_set_name, " + 
            "   primcat.ITEM_SET_CATEGORY_NAME, " + 
            "   decode(prod.internal_display_name, 'TABE 9 Survey', '9', 'TABE 9 Battery', '9', 'TABE 10 Survey', '10', 'TABE 10 Battery', '10', td.item_set_form), " +
            "   td.item_Set_level, " + 
            "   td.item_Set_id, " +
            "   prod.product_id)";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                PrimaryObjective primaryObjective = new PrimaryObjective();
                primaryObjective.setPrimaryObjectiveId(new Long(rs.getLong("primaryObjectiveId")));
                primaryObjective.setContentAreaId(new Long(rs.getLong("contentAreaId")));
                primaryObjective.setPrimaryObjectiveName(rs.getString("primaryObjectiveName"));
                primaryObjective.setPrimaryObjectiveType(rs.getString("primaryObjectiveType"));
                primaryObjective.setPrimaryObjectiveNumItems(new Long(rs.getLong("primaryNumItems")));
                primaryObjective.setPrimaryObjectivePointsPossible(new Long(rs.getLong("primaryPointsPossible")));
                primaryObjective.setSubtestId(new Long(rs.getLong("subtestId")));
                primaryObjective.setSubtestForm(rs.getString("subtestForm"));
                primaryObjective.setSubtestLevel(rs.getString("subtestLevel"));
                primaryObjective.setPrimaryObjectiveIndex(new Long(rs.getLong("primaryObjectiveIndex")));
                primaryObjective.setProductId(new Long(rs.getLong("productId")));
                
                String key = primaryObjective.getPrimaryObjectiveName() + "||" + primaryObjective.getProductId() + "||" + primaryObjective.getContentAreaId() + "||" + primaryObjective.getSubtestLevel();
                
                if(poMap.containsKey(key)) {
                    PrimaryObjective po1 = (PrimaryObjective) poMap.get(key);
                    primaryObjective.setPrimaryObjectiveNumItems(new Long(po1.getPrimaryObjectiveNumItems().longValue() + primaryObjective.getPrimaryObjectiveNumItems().longValue()));
                    primaryObjective.setPrimaryObjectivePointsPossible(new Long(po1.getPrimaryObjectivePointsPossible().longValue() + primaryObjective.getPrimaryObjectivePointsPossible().longValue()));
                }   
                poMap.put(key, primaryObjective);    
                primaryObjectives.add(primaryObjective);
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        return (PrimaryObjective []) primaryObjectives.toArray(new PrimaryObjective[0]);
    }
    
    public SecondaryObjective [] getSecondaryObjectives(Long oasRosterId) throws SQLException {
        ArrayList secondaryObjectives = new ArrayList();
        final String casql = 
        	"select distinct " +
            "   sec.item_Set_id as secondaryObjectiveId, " +
            "   prim.item_set_id as primaryObjectiveId, " + 
            "   sec.item_set_name as secondaryObjectiveName, " + 
            "   seccat.ITEM_SET_CATEGORY_NAME as secondaryObjectiveType, " +
            "   sum(dp.max_points) as secondaryPointsPossible, " +
            "   count(distinct item.item_id) as secondaryNumItems, " + 
            "   decode(prod.internal_display_name, 'TABE 9 Survey', '9', 'TABE 9 Battery', '9', 'TABE 10 Survey', '10', 'TABE 10 Battery', '10', td.item_set_form) as subtestForm, " +
            "   td.item_set_level as subtestLevel, " +
            "   td.item_set_id as subtestId, " +
            "   td.item_set_name as subtestName, " +
            "   prod.product_id as productId " + 
            "from " +  
            "   item, " +
            "   item_set sec, " +
            "   item_Set_category seccat, " +
            "   item_Set_ancestor secisa, " +
            "   item_set prim, " +
            "   item_Set_category primcat, " +
            "   item_Set_ancestor primisa, " +
            "   item_set_item secisi, " +
            "   item_Set_ancestor tcisa, " +
            "   item_set_item tcisi, " + 
            "   datapoint dp, " + 
            "   test_roster ros, " + 
            "   test_Admin adm, " + 
            "   test_catalog tc, " + 
            "   product prod, " +
            "   item_set td " + 
            "where " + 
            "   ros.test_roster_id = ? " +
            "   and adm.test_admin_id = ros.test_admin_id " +
            "   and tc.test_catalog_id = adm.test_catalog_id " +
            "   and prod.product_id = tc.product_id " +
            "   and item.ACTIVATION_STATUS = 'AC' " + 
            "   and tc.ACTIVATION_STATUS = 'AC' " + 
            "   and sec.item_Set_id = secisa.ancestor_item_Set_id " +
            "   and sec.item_set_type = 'RE' " + 
            "   and secisa.item_set_id = secisi.item_Set_id " +
            "   and item.item_id = secisi.item_id " +
            "   and tcisi.item_id = item.item_id " +
            "   and tcisa.item_set_id = tcisi.item_set_id " +
            "   and adm.item_set_id = tcisa.ancestor_item_set_id " +
            "   and seccat.item_Set_category_id = sec.item_set_category_id " + 
            "   and seccat.item_set_category_level = prod.sec_scoring_item_Set_level " +
            "   and dp.item_id = item.item_id " +
            "   and primisa.item_Set_id = sec.item_Set_id " +
            "   and prim.item_set_id = primisa.ancestor_item_Set_id " +
            "   and primcat.item_Set_category_id = prim.item_set_category_id " + 
            "   and primcat.item_set_category_level = prod.scoring_item_Set_level " +
            "   and td.item_set_id = tcisi.item_set_id " +
            "   and td.sample = 'F' " +
            "   AND (td.item_set_level != 'L' OR PROD.PRODUCT_TYPE = 'TL') " +
            "   and seccat.framework_product_id = prod.PARENT_PRODUCT_ID " +
            " group by " +
            "   sec.item_Set_id, " +
            "   prim.item_set_id, " + 
            "   sec.item_set_name, " + 
            "   seccat.ITEM_SET_CATEGORY_NAME, " + 
            "   decode(prod.internal_display_name, 'TABE 9 Survey', '9', 'TABE 9 Battery', '9', 'TABE 10 Survey', '10', 'TABE 10 Battery', '10', td.item_set_form), " +
            "   td.item_Set_level, " + 
            "   td.item_Set_id, " +
            "   td.item_set_name, " +
            "   prod.product_id";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                SecondaryObjective secondaryObjective = new SecondaryObjective();
                secondaryObjective.setSecondaryObjectiveId(new Long(rs.getLong("secondaryObjectiveId")));
                secondaryObjective.setPrimaryObjectiveId(new Long(rs.getLong("primaryObjectiveId")));
                secondaryObjective.setSecondaryObjectiveName(rs.getString("secondaryObjectiveName"));
                secondaryObjective.setSecondaryObjectiveType(rs.getString("secondaryObjectiveType"));
                secondaryObjective.setSecondaryObjectiveNumItems(new Long(rs.getLong("secondaryNumItems")));
                secondaryObjective.setSecondaryObjectivePointsPossible(new Long(rs.getLong("secondaryPointsPossible")));
                secondaryObjective.setSubtestId(new Long(rs.getLong("subtestId")));
                secondaryObjective.setSubtestForm(rs.getString("subtestForm"));
                secondaryObjective.setSubtestLevel(rs.getString("subtestLevel"));
                secondaryObjective.setSubtestName(rs.getString("subtestName"));
                secondaryObjective.setProductId(new Long(rs.getLong("productId")));
                
                secondaryObjectives.add(secondaryObjective);
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        return (SecondaryObjective []) secondaryObjectives.toArray(new SecondaryObjective[0]);
    }
    
    public Item [] getItems(Long oasRosterId) throws SQLException {
        HashMap items = new HashMap();
        final String casql = 
        	"select distinct " + 
               "sec.item_set_id as secondaryObjectiveId, " + 
               "item.item_id as oasItemId, " +  
               "item.description as itemText, " +  
               "tdisi.item_sort_order as itemIndex, " +  
               "item.item_type as itemType, " +  
               "item.correct_answer as itemCorrectResponse, " +  
               "dp.max_points as itemPointsPossible, " +  
               "td.item_set_form as subtestForm, " + 
               "td.item_set_level as subtestLevel, " + 
               "td.item_set_id as subtestId, " + 
               "td.item_set_name as subtestName, " +
               "pval.grade as pvalGrade, " +
               "pval.norm_group as pvalNormGroup, " +
               "round(pval.p_value * 100, 1) as nationalAverage " +
            "from " +   
               "item, " +  
               "item_set sec, " +  
               "item_Set_category seccat, " +  
               "item_set_ancestor secisa, " + 
               "item_Set_item secisi, " + 
               "item_set td, " + 
               "item_set_ancestor tdisa, " + 
               "item_set_item tdisi, " +   
               "datapoint dp, " +  
               "test_roster ros, " +  
               "test_Admin adm, " +  
               "test_catalog tc, " +  
               "product prod, " +
               "program prog, " +
               "item_p_value pval " +
            "where " +  
               "ros.test_roster_id = ? " + 
               "and adm.test_admin_id = ros.test_admin_id " + 
               "and tc.test_catalog_id = adm.test_catalog_id " + 
               "and prod.product_id = tc.product_id " + 
               "and item.ACTIVATION_STATUS = 'AC' " +  
               "and tc.ACTIVATION_STATUS = 'AC' " +  
               "and sec.item_Set_id = secisa.ancestor_item_Set_id " + 
               "and sec.item_set_type = 'RE' " +  
               "and secisa.item_set_id = secisi.item_Set_id " + 
               "and item.item_id = secisi.item_id " + 
               "and tdisi.item_id = item.item_id " + 
               "and td.item_set_id = tdisi.item_set_id " + 
               "and td.item_set_type = 'TD' " + 
               "and tdisa.item_set_id = td.item_set_id " + 
               "and adm.item_set_id = tdisa.ancestor_item_set_id " + 
               "and seccat.item_Set_category_id = sec.item_set_category_id " +  
               "and seccat.item_set_category_level = prod.sec_scoring_item_Set_level " + 
               "and dp.item_id = item.item_id " + 
               "and td.sample = 'F' " + 
               "AND (td.item_set_level != 'L' OR PROD.PRODUCT_TYPE = 'TL') " + 
               "and seccat.framework_product_id = prod.PARENT_PRODUCT_ID " +
               "and prog.program_id = adm.program_id " +
               "and pval.item_display_name (+) = item.item_id " +
            "group by " +
               "sec.item_set_id, " + 
               "item.item_id, " +  
               "item.description, " +  
               "tdisi.item_sort_order, " +  
               "item.item_type, " +  
               "item.correct_answer, " +  
               "dp.max_points, " +  
               "td.item_set_form, " + 
               "td.item_set_level, " + 
               "td.item_set_id, " + 
               "td.item_set_name, " +
               "pval.grade, " +
               "pval.norm_group, " +
               "pval.p_value " +
           "order by " +  
               "tdisi.item_sort_order";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                Item item = (Item) items.get(rs.getString("oasItemId"));
                if(item == null) item = new Item();
                item.setOasItemId(rs.getString("oasItemId"));
                item.setSecondaryObjectiveId(new Long(rs.getLong("secondaryObjectiveId")));
                item.setItemCorrectResponse(rs.getString("itemCorrectResponse"));
                item.setItemIndex(new Long(rs.getLong("itemIndex")));
                item.setItemPointsPossible(new Long(rs.getLong("itemPointsPossible")));          
                item.setItemText(rs.getString("itemIndex"));
                item.setItemType(rs.getString("itemType"));
                item.setSubtestId(new Long(rs.getLong("subtestId")));
                item.setSubtestForm(rs.getString("subtestForm"));
                item.setSubtestLevel(rs.getString("subtestLevel"));
                item.setSubtestName(rs.getString("subtestName"));
                if(rs.getBigDecimal("nationalAverage") != null) {
                    item.setNationalAverage(rs.getString("pvalNormGroup") + String.valueOf(rs.getInt("pvalGrade")), rs.getBigDecimal("nationalAverage"));
                }      
                items.put(rs.getString("oasItemId"), item);
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        return (Item []) items.values().toArray(new Item[0]);
    }
    
    
    //Changes for TABE Adaptive
    
    public ContentArea [] getContentAreasForAdaptive(Long oasRosterId) throws SQLException {
    	//System.out.println("!!!!!!getContentAreasForAdaptive!!!!!!");
        HashMap caMap = new HashMap();
        ArrayList contentAreas = new ArrayList();
        final String casql = "select distinct " +
        		"	prod.product_id || ca.item_Set_id as contentAreaId, " +
        		"	ca.item_set_name as contentAreaName, " +
        		"	prod.product_type || ' CONTENT AREA' as contentAreaType, " +
        		"	prod.product_type || ' ' || ca.item_Set_name as subject, " +
        		"	decode(prod.internal_display_name, 'TABE 9 Survey', '9', 'TABE 9 Battery', '9', 'TABE 10 Survey', '10', 'TABE 10 Battery', '10', ca.item_set_form) as subtestForm, " +
        		"	ca.item_set_level as subtestLevel, " +
        		"	ca.item_set_id as subtestId " +
        		"from " +
        		"	product prod, " +
        		"	item_set ca, " +
        		"	item_Set_category cacat, " +
        		"	student_item_set_status siss, " +
        		"	test_roster ros, " +
        		"	test_admin adm, " +
        		"	test_catalog tc " +
        		"where " +
        		"	ros.test_roster_id = ? " +
        		"	and adm.test_admin_id = ros.test_admin_id " +
        		"	and tc.test_catalog_id = adm.test_catalog_id " +
        		"	and prod.product_id = tc.product_id " +
        		"	and siss.test_roster_id = ros.test_roster_id " +
        		"	and ca.item_set_id = siss.item_set_id " +
        		"	and ca.sample = 'F' " +
        		"	and tc.ACTIVATION_STATUS = 'AC' " +
        		"	and (ca.item_set_level != 'L' OR PROD.PRODUCT_TYPE = 'TL') " +
        		"	and cacat.framework_product_id = prod.PARENT_PRODUCT_ID " +
        		"group by " +
        		"	prod.product_id || ca.item_Set_id, " +
        		"	ca.item_set_name, " +
        		"	prod.product_type || ' CONTENT AREA', " +
        		"	prod.product_type || ' ' || ca.item_Set_name, " +
        		"	decode(prod.internal_display_name, 'TABE 9 Survey', '9', 'TABE 9 Battery', '9', 'TABE 10 Survey', '10', 'TABE 10 Battery', '10', ca.item_set_form), " +
        		"	ca.item_Set_level, " +
        		"	ca.item_Set_id";
        	      
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                ContentArea contentArea = new ContentArea();
                contentArea.setContentAreaId(new Long(rs.getLong("contentAreaId")));
                contentArea.setContentAreaName(rs.getString("contentAreaName"));
                contentArea.setContentAreaType(rs.getString("contentAreaType"));
                contentArea.setSubject(rs.getString("subject"));
         //       contentArea.setContentAreaNumItems(new Long(0));
         //       contentArea.setContentAreaPointsPossible(new Long(0));
                contentArea.setSubtestId(new Long(rs.getLong("subtestId")));
                contentArea.setSubtestForm(rs.getString("subtestForm"));
                contentArea.setSubtestLevel(rs.getString("subtestLevel"));

                String key = contentArea.getContentAreaName() + "||" + contentArea.getSubtestLevel();

               /* if(caMap.containsKey(key)) {
                    ContentArea ca1 = (ContentArea) caMap.get(key);
                    Long numItems = new Long(0);
                    Long points = new Long(0);
                    contentArea.setContentAreaNumItems(numItems);
                    contentArea.setContentAreaPointsPossible(points);
                    ca1.setContentAreaNumItems(numItems);
                    ca1.setContentAreaPointsPossible(points);
                } */  
                caMap.put(key, contentArea);    
                contentAreas.add(contentArea);
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        return (ContentArea []) contentAreas.toArray(new ContentArea[0]);
    }
    
    // Added for tabe adaptive
    public PrimaryObjective [] getPrimaryObjectivesForTabeCat(Long oasRosterId) throws SQLException {
        ArrayList primaryObjectives = new ArrayList();
        HashMap poMap = new HashMap();
        
        final String casql = 
        	"select distinct " +
        	"	tco.objective_id as primaryObjectiveId, " +
        	"	rownum as primaryObjectiveIndex, " +
        	"	prod.product_id || tco.content_area_id as contentAreaId, " +
        	"	tco.objective_name as primaryObjectiveName, " +
        	"	tco.items as primaryNumItems, " +
        	"	iset.item_set_id as subtestId, " +
        	"	siss.objective_score as subtestLevel, " +
        	"	prod.product_id as productId " +
        	"from " +
        	"	test_roster ros, " +
        	"	test_admin adm, " +
        	"	product prod, " +
        	"	tabe_cat_objective tco, " +
        	"	student_item_set_status siss, " +
        	"	item_set iset " +
        	"where " +
        	"	ros.test_roster_id = ? " +
        	"	and adm.test_admin_id = ros.test_admin_id " +
        	"	and adm.product_id = prod.product_id " +
        	"	and siss.test_roster_id = ros.test_roster_id " +
        	"	and siss.item_set_id = iset.item_set_id " +
        	"	and iset.item_set_id = tco.content_area_id";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                PrimaryObjective primaryObjective = new PrimaryObjective();
                primaryObjective.setPrimaryObjectiveId(new Long(rs.getLong("primaryObjectiveId")));
                primaryObjective.setContentAreaId(new Long(rs.getLong("contentAreaId")));
                primaryObjective.setPrimaryObjectiveName(rs.getString("primaryObjectiveName"));
                primaryObjective.setPrimaryObjectiveType("Objective");
                primaryObjective.setPrimaryObjectiveNumItems(new Long(rs.getLong("primaryNumItems")));
                primaryObjective.setPrimaryObjectivePointsPossible(new Long(0));
                primaryObjective.setSubtestId(new Long(rs.getLong("subtestId")));
              //  primaryObjective.setSubtestLevel(rs.getString("subtestLevel"));
                primaryObjective.setPrimaryObjectiveIndex(new Long(rs.getLong("primaryObjectiveIndex")));
                primaryObjective.setProductId(new Long(rs.getLong("productId")));
                
                String level = rs.getString("subtestLevel");
                System.out.println("*** OAS ROSTER ID: " + oasRosterId.toString());
                if(null != level && level.contains(primaryObjective.getPrimaryObjectiveId().toString())) {
                	System.out.println("*** Obj ID: " + primaryObjective.getPrimaryObjectiveId().toString());
                	System.out.println("*** level: " + level);
                	 String[] objectiveScores = level.split("\\|");
                     for (int i = 0; i < objectiveScores.length; i++) {
                     	String[] individualObj = objectiveScores[i].split(",");
                     	long objId = Long.parseLong(individualObj[0]);
                     	System.out.println("*** objId: " + objId);
                     	if(primaryObjective.getPrimaryObjectiveId() == objId) {
                     		primaryObjective.setSubtestLevel(individualObj[5]);
                     		System.out.println("*** Obj Level: " + individualObj[5]);
                     	}
                     }
                } else {
                	primaryObjective.setSubtestLevel(rs.getString("subtestLevel"));
                }
               
                
                String key = primaryObjective.getPrimaryObjectiveName() + "||" + primaryObjective.getProductId() + "||" + primaryObjective.getContentAreaId() + "||" + primaryObjective.getSubtestLevel();
               // System.out.println(key);
                
                if(poMap.containsKey(key)) {
                    PrimaryObjective po1 = (PrimaryObjective) poMap.get(key);
                    primaryObjective.setPrimaryObjectiveNumItems(new Long(po1.getPrimaryObjectiveNumItems().longValue() + primaryObjective.getPrimaryObjectiveNumItems().longValue()));
                    primaryObjective.setPrimaryObjectivePointsPossible(new Long(po1.getPrimaryObjectivePointsPossible().longValue() + primaryObjective.getPrimaryObjectivePointsPossible().longValue()));
                }   
                poMap.put(key, primaryObjective);    
                primaryObjectives.add(primaryObjective);
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        return (PrimaryObjective []) primaryObjectives.toArray(new PrimaryObjective[0]);
    }
    
    
}