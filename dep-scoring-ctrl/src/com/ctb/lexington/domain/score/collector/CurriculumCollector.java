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
            if("G".equalsIgnoreCase(form)) {
            	if("TERRA3 Survey".equals(assessmentType)) { // for product_id 3710
            		if(level != null && level.indexOf("22") > 0) {
    	                composites[0].setAssessmentId(new Long(87640));
    	                composites[0].setCompositeId(new Long(57));
    	                composites[0].setCompositeNumItems(new Long(85));
    	                composites[0].setCompositePointsPossible(new Long(85));
    	            } else if(level != null && "20".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87638));
    	                composites[0].setCompositeId(new Long(56));
    	                composites[0].setCompositeNumItems(new Long(85));
    	                composites[0].setCompositePointsPossible(new Long(85));
    	            } else if(level != null && "19".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87636));
    	                composites[0].setCompositeId(new Long(55));
    	                composites[0].setCompositeNumItems(new Long(85));
    	                composites[0].setCompositePointsPossible(new Long(85));
    	            } else if(level != null && "18".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87634));
    	                composites[0].setCompositeId(new Long(54));
    	                composites[0].setCompositeNumItems(new Long(91));
    	                composites[0].setCompositePointsPossible(new Long(91));
    	            } else if(level != null && "17".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87628));
    	                composites[0].setCompositeId(new Long(53));
    	                composites[0].setCompositeNumItems(new Long(92));
    	                composites[0].setCompositePointsPossible(new Long(92));
    	            } else if(level != null && "16".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87626));
    	                composites[0].setCompositeId(new Long(52));
    	                composites[0].setCompositeNumItems(new Long(91));
    	                composites[0].setCompositePointsPossible(new Long(91));
    	            } else if(level != null && "15".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87624));
    	                composites[0].setCompositeId(new Long(51));
    	                composites[0].setCompositeNumItems(new Long(92));
    	                composites[0].setCompositePointsPossible(new Long(92));
    	            } else if(level != null && "14".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87606));
    	                composites[0].setCompositeId(new Long(50));
    	                composites[0].setCompositeNumItems(new Long(92));
    	                composites[0].setCompositePointsPossible(new Long(92));
    	            } else if(level != null && "13".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87604));
    	                composites[0].setCompositeId(new Long(49));
    	                composites[0].setCompositeNumItems(new Long(80));
    	                composites[0].setCompositePointsPossible(new Long(80));
    	            }
                } else { // For product_id 3720
                	if(level != null && "12".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87641));
    	                composites[0].setCompositeId(new Long(48));
    	                composites[0].setCompositeNumItems(new Long(107));
    	                composites[0].setCompositePointsPossible(new Long(107));
    	            } else if(level != null && level.indexOf("22") > 0) {
    	                composites[0].setAssessmentId(new Long(87639));
    	                composites[0].setCompositeId(new Long(47));
    	                composites[0].setCompositeNumItems(new Long(126));
    	                composites[0].setCompositePointsPossible(new Long(126));
    	            } else if(level != null && "20".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87637));
    	                composites[0].setCompositeId(new Long(46));
    	                composites[0].setCompositeNumItems(new Long(126));
    	                composites[0].setCompositePointsPossible(new Long(126));
    	            } else if(level != null && "19".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87635));
    	                composites[0].setCompositeId(new Long(45));
    	                composites[0].setCompositeNumItems(new Long(126));
    	                composites[0].setCompositePointsPossible(new Long(126));
    	            } else if(level != null && "18".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87633));
    	                composites[0].setCompositeId(new Long(44));
    	                composites[0].setCompositeNumItems(new Long(136));
    	                composites[0].setCompositePointsPossible(new Long(136));
    	            } else if(level != null && "17".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87627));
    	                composites[0].setCompositeId(new Long(43));
    	                composites[0].setCompositeNumItems(new Long(137));
    	                composites[0].setCompositePointsPossible(new Long(137));
    	            } else if(level != null && "16".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87625));
    	                composites[0].setCompositeId(new Long(42));
    	                composites[0].setCompositeNumItems(new Long(136));
    	                composites[0].setCompositePointsPossible(new Long(136));
    	            } else if(level != null && "15".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87623));
    	                composites[0].setCompositeId(new Long(41));
    	                composites[0].setCompositeNumItems(new Long(137));
    	                composites[0].setCompositePointsPossible(new Long(137));
    	            } else if(level != null && "14".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87605));
    	                composites[0].setCompositeId(new Long(40));
    	                composites[0].setCompositeNumItems(new Long(137));
    	                composites[0].setCompositePointsPossible(new Long(137));
    	            } else if(level != null && "13".equals(level)) {
    	                composites[0].setAssessmentId(new Long(87603));
    	                composites[0].setCompositeId(new Long(39));
    	                composites[0].setCompositeNumItems(new Long(120));
    	                composites[0].setCompositePointsPossible(new Long(120));
    	            }
                }
            	
            } else {
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
           } else if("ESP B".equals(form)){	
        	   if(level != null && "K-1".equals(level)) {
	           	composites[0].setAssessmentId(new Long(13337));
	            composites[0].setCompositeId(new Long(39));
	            composites[0].setCompositeNumItems(new Long(100));
	            composites[0].setCompositePointsPossible(new Long(315));
	          }else if("9-12".equals(level)) {
	            composites[0].setAssessmentId(new Long(13341));
	            composites[0].setCompositeId(new Long(40));
	            composites[0].setCompositeNumItems(new Long(100));
	            composites[0].setCompositePointsPossible(new Long(315));
	          } else if("6-8".equals(level)) {
	            composites[0].setAssessmentId(new Long(13340));
	            composites[0].setCompositeId(new Long(41));
	            composites[0].setCompositeNumItems(new Long(100));
	            composites[0].setCompositePointsPossible(new Long(315));
	          }else if("4-5".equals(level)) {
	            composites[0].setAssessmentId(new Long(13217));
	            composites[0].setCompositeId(new Long(42));
	            composites[0].setCompositeNumItems(new Long(100));
	            composites[0].setCompositePointsPossible(new Long(315));
	          }else if("2-3".equals(level)) {
	            composites[0].setAssessmentId(new Long(13339));
	            composites[0].setCompositeId(new Long(43));
	            composites[0].setCompositeNumItems(new Long(100));
	            composites[0].setCompositePointsPossible(new Long(315));
	          }
           }else if("C".equals(form)){

        	   if(level != null && "K-1".equals(level)) {
               	composites[0].setAssessmentId(new Long(29126));
                   composites[0].setCompositeId(new Long(44));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 }else if("9-12".equals(level)) {
                   composites[0].setAssessmentId(new Long(29355));
                   composites[0].setCompositeId(new Long(45));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 } else if("6-8".equals(level)) {
                   composites[0].setAssessmentId(new Long(29346));
                   composites[0].setCompositeId(new Long(46));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 }else if("4-5".equals(level)) {
                   composites[0].setAssessmentId(new Long(29077));
                   composites[0].setCompositeId(new Long(47));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 }else if("2-3".equals(level)) {
                   composites[0].setAssessmentId(new Long(29304));
                   composites[0].setCompositeId(new Long(48));
                   composites[0].setCompositeNumItems(new Long(100));
                   composites[0].setCompositePointsPossible(new Long(315));
                 }
           }/*else if("D".equals(form)){

        	   if(level != null && "K-1".equals(level)) {
            	composites[0].setAssessmentId(new Long(29364));
                composites[0].setCompositeId(new Long(49));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("9-12".equals(level)) {
                composites[0].setAssessmentId(new Long(29418));
                composites[0].setCompositeId(new Long(50));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              } else if("6-8".equals(level)) {
                composites[0].setAssessmentId(new Long(29502));
                composites[0].setCompositeId(new Long(51));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("4-5".equals(level)) {
                composites[0].setAssessmentId(new Long(29086));
                composites[0].setCompositeId(new Long(52));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }else if("2-3".equals(level)) {
                composites[0].setAssessmentId(new Long(29382));
                composites[0].setCompositeId(new Long(53));
                composites[0].setCompositeNumItems(new Long(100));
                composites[0].setCompositePointsPossible(new Long(315));
              }
           } */
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
    	Integer productId =null;
    	String subtestLevel="", subtestForm="";
        HashMap caMap = new HashMap();
        ArrayList<ContentArea> contentAreas = new ArrayList<ContentArea>();
        final String casql = 
        	"select distinct " +
        	"  	prod.product_id as productId,"+
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
            "   prod.product_id, "+
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
            	productId =  rs.getInt("productId");
            	subtestLevel = rs.getString("subtestLevel");
            	subtestForm = rs.getString("subtestForm");
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
        if(productId == 7501 || productId == 7502){
        	contentAreas = getVirtualContentAreaForLaslinkSecEdition(contentAreas, productId, subtestLevel, subtestForm);
        }
            
        return (ContentArea []) contentAreas.toArray(new ContentArea[0]);
    }
    
    private ArrayList getVirtualContentAreaForLaslinkSecEdition(
			ArrayList<ContentArea> contentAreas, Integer productId, String subtestLevel, String subtestForm) throws SQLException{
    	 ArrayList<ContentArea> finalCAList = new ArrayList<ContentArea>();
    	
    	 final String casql = "select prod.product_id || ca.item_Set_id as contentAreaId, " + 
         "	ca.item_set_name as contentAreaName, " + 
         "  prod.product_type || ' CONTENT AREA' as contentAreaType, " + 
         "  prod.product_type || ' ' || ca.item_Set_name as subject " +
         "  from item_set ca," +
         "  item_set_category isc," +
         "  product prod" +
         "	where ca.ext_cms_item_set_id like ?" +
         "	and ca.item_set_category_id = isc.item_set_category_id" +
         "	and isc.item_set_category_level = prod.content_area_level" +
         "	and isc.framework_product_id = prod.parent_product_id" +
         "	and prod.product_id = ?";

    	 PreparedStatement ps = null;
         ResultSet rs = null;
         try {
             ps = conn.prepareStatement(casql);
             String likeSubtestLevel = "%"+subtestLevel+"%";
             ps.setString(1, likeSubtestLevel);
             ps.setInt(2, productId);
             rs = ps.executeQuery();
             while (rs.next()) {
             ContentArea contentArea = new ContentArea();
             contentArea.setContentAreaId(rs.getLong("contentAreaId"));
             contentArea.setContentAreaName(rs.getString("contentAreaName"));
             contentArea.setContentAreaType(rs.getString("contentAreaType"));
             contentArea.setSubject(rs.getString("subject"));
             contentArea.setSubtestLevel(subtestLevel);
             contentArea.setSubtestForm(subtestForm);
             
             if ("Comprehension".equals(contentArea.getContentAreaName())){
            	 Long contentAreaPointsPossible = new Long(0);
                 Long contentAreaNumItems = new Long(0);
                 for(ContentArea ca : contentAreas){
            		 if("Listening".equals(ca.getContentAreaName()) || "Reading".equals(ca.getContentAreaName())){
            			 contentAreaPointsPossible += ca.getContentAreaPointsPossible();
            			 contentAreaNumItems += ca.getContentAreaNumItems();
            			 contentArea.setContentAreaPointsPossible(contentAreaPointsPossible);
            			 contentArea.setContentAreaNumItems(contentAreaNumItems);
            		 }
            	 }
            	 for(ContentArea ca : contentAreas){
            		 if("Listening".equals(ca.getContentAreaName())){
            			 ContentArea newCA = new ContentArea(contentArea);
            			 newCA.setSubtestId(ca.getSubtestId());
            			 finalCAList.add(newCA);
            		 }
            		 if("Reading".equals(ca.getContentAreaName())){
            			 ContentArea newCA = new ContentArea(contentArea);
            			 newCA.setSubtestId(ca.getSubtestId());
            			 finalCAList.add(newCA);
            		 }
            	 }
            	 
             	}
             else if("Oral".equals(contentArea.getContentAreaName())){
            	 Long contentAreaPointsPossible = new Long(0);
                 Long contentAreaNumItems = new Long(0);
            	 for(ContentArea ca : contentAreas){
            		 if("Listening".equals(ca.getContentAreaName()) || "Speaking".equals(ca.getContentAreaName())){
            			 contentAreaPointsPossible += ca.getContentAreaPointsPossible();
            			 contentAreaNumItems += ca.getContentAreaNumItems();
            			 contentArea.setContentAreaPointsPossible(contentAreaPointsPossible);
            			 contentArea.setContentAreaNumItems(contentAreaNumItems);
            		 }
            	 }
            	 for(ContentArea ca : contentAreas){
            		 if("Listening".equals(ca.getContentAreaName())){
            			 ContentArea newCA = new ContentArea(contentArea);
            			 newCA.setSubtestId(ca.getSubtestId());
            			 finalCAList.add(newCA);
            		 }
            		 if("Speaking".equals(ca.getContentAreaName())){
            			 ContentArea newCA = new ContentArea(contentArea);
            			 newCA.setSubtestId(ca.getSubtestId());
            			 finalCAList.add(newCA);
            		 }
            	 }
             }
             else if("Productive".equals(contentArea.getContentAreaName())){
	           	 Long contentAreaPointsPossible = new Long(0);
	             Long contentAreaNumItems = new Long(0);
	           	 for(ContentArea ca : contentAreas){
	           		 if("Writing".equals(ca.getContentAreaName()) || "Speaking".equals(ca.getContentAreaName())){
	           			 contentAreaPointsPossible += ca.getContentAreaPointsPossible();
	           			 contentAreaNumItems += ca.getContentAreaNumItems();
	           			 contentArea.setContentAreaPointsPossible(contentAreaPointsPossible);
	           			 contentArea.setContentAreaNumItems(contentAreaNumItems);
	           		 }
	           	 }
	           	 for(ContentArea ca : contentAreas){
	           		 if("Writing".equals(ca.getContentAreaName())){
	           			ContentArea newCA = new ContentArea(contentArea);
	           			newCA.setSubtestId(ca.getSubtestId());
	           			finalCAList.add(newCA);
	           		 }
	           		 if("Speaking".equals(ca.getContentAreaName())){
	           			ContentArea newCA = new ContentArea(contentArea);
	           			newCA.setSubtestId(ca.getSubtestId());
	           			finalCAList.add(newCA);
	           		 }
	           	 }
             }
             else if("Literacy".equals(contentArea.getContentAreaName())){
	           	 Long contentAreaPointsPossible = new Long(0);
	             Long contentAreaNumItems = new Long(0);
	           	 for(ContentArea ca : contentAreas){
	           		 if("Reading".equals(ca.getContentAreaName()) || "Writing".equals(ca.getContentAreaName())){
	           			 contentAreaPointsPossible += ca.getContentAreaPointsPossible();
	           			 contentAreaNumItems += ca.getContentAreaNumItems();
	           			 contentArea.setContentAreaPointsPossible(contentAreaPointsPossible);
	           			 contentArea.setContentAreaNumItems(contentAreaNumItems);
	           		 }
	           	 }
	           	 for(ContentArea ca : contentAreas){
	           		 if("Writing".equals(ca.getContentAreaName())){
	           			ContentArea newCA = new ContentArea(contentArea);
	           			newCA.setSubtestId(ca.getSubtestId());
	           			finalCAList.add(newCA);
	           		 }
	           		 if("Reading".equals(ca.getContentAreaName())){
	           			ContentArea newCA = new ContentArea(contentArea);
	           			newCA.setSubtestId(ca.getSubtestId());
	           			finalCAList.add(newCA);
	           		 }
	           	 }
             }
             }
             contentAreas.addAll(finalCAList);
         }finally {
             SQLUtil.close(rs);
             ConnectionFactory.getInstance().release(ps);
         }
		
		return contentAreas;
	}

	public PrimaryObjective [] getPrimaryObjectives(Long oasRosterId) throws SQLException {
        ArrayList primaryObjectives = new ArrayList();
        HashMap poMap = new HashMap();
        Integer productId = null;
        String subtestLevel ="", subtestForm ="";
        Long objectiveIndex = new Long(0);
        
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
            "  productId, " +
            "  monarchId " +  
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
            "   prod.product_id as productId, " +
            "	prim.ext_cms_item_set_id as monarchId " + 
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
            "   prod.product_id, " +
            "	prim.ext_cms_item_set_id)";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
            	productId = rs.getInt("productId");
            	subtestLevel = rs.getString("subtestLevel");
            	subtestForm = rs.getString("subtestForm");
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
                primaryObjective.setMonarchId(rs.getString("monarchId"));
                
                String key = primaryObjective.getPrimaryObjectiveName() + "||" + primaryObjective.getProductId() + "||" + primaryObjective.getContentAreaId() + "||" + primaryObjective.getSubtestLevel();
                
                if(poMap.containsKey(key)) {
                    PrimaryObjective po1 = (PrimaryObjective) poMap.get(key);
                    primaryObjective.setPrimaryObjectiveNumItems(new Long(po1.getPrimaryObjectiveNumItems().longValue() + primaryObjective.getPrimaryObjectiveNumItems().longValue()));
                    primaryObjective.setPrimaryObjectivePointsPossible(new Long(po1.getPrimaryObjectivePointsPossible().longValue() + primaryObjective.getPrimaryObjectivePointsPossible().longValue()));
                }   
                poMap.put(key, primaryObjective);    
                primaryObjectives.add(primaryObjective);
                objectiveIndex = primaryObjective.getPrimaryObjectiveIndex();
            }
            if(productId == 7501 || productId == 7502){
            	primaryObjectives = getVirtualPrimaryObjectiveForLaslinkSecEdition(primaryObjectives, productId, subtestLevel, subtestForm, ++objectiveIndex);
            }
        } finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        return (PrimaryObjective []) primaryObjectives.toArray(new PrimaryObjective[0]);
    }
	
	
	private ArrayList getVirtualPrimaryObjectiveForLaslinkSecEdition(
			ArrayList<PrimaryObjective> primaryObjectives, Integer productId, String subtestLevel, String subtestForm, Long objectiveIndex) throws SQLException{
    	 ArrayList<PrimaryObjective> tempPrimList = new ArrayList<PrimaryObjective>();
    	
    	 final String casql = "select prim.item_Set_id as primaryObjectiveId," +
    	 		"	prod.product_id || prim.item_Set_id as contentAreaId," +
    	 		"	prim.item_set_name as primaryObjectiveName," +
    	 		"	primcat.ITEM_SET_CATEGORY_NAME as primaryObjectiveType," +
    	 		"	prim.ext_cms_item_set_id as monarchId" +
    	 		"	from item_set prim, item_set_category primcat, product prod" +
    	 		"	where prim.ext_cms_item_set_id like ?" +
    	 		"	and prim.item_set_category_id = primcat.item_set_category_id" +
    	 		"	and primcat.item_set_category_level = prod.content_area_level" +
    	 		"	and primcat.framework_product_id = prod.parent_product_id" +
    	 		"	and prod.product_id = ?";

    	 PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             ps = conn.prepareStatement(casql);
             String likeSubtestLevel = "%"+subtestLevel+"%";
             ps.setString(1, likeSubtestLevel);
             ps.setInt(2, productId);
             rs = ps.executeQuery();
             while (rs.next()) {
            	 PrimaryObjective primaryObjective = new PrimaryObjective();
                 primaryObjective.setPrimaryObjectiveId(new Long(rs.getLong("primaryObjectiveId")));
                 primaryObjective.setContentAreaId(new Long(rs.getLong("contentAreaId")));
                 primaryObjective.setPrimaryObjectiveName(rs.getString("primaryObjectiveName"));
                 primaryObjective.setPrimaryObjectiveType(rs.getString("primaryObjectiveType"));
                 primaryObjective.setSubtestForm(subtestForm);
                 primaryObjective.setSubtestLevel(subtestLevel);
//                 primaryObjective.setPrimaryObjectiveIndex(objectiveIndex);
                 primaryObjective.setProductId(new Long(productId));
                 primaryObjective.setMonarchId(rs.getString("monarchId"));
             	 
             //finalCAList.add(contentArea);
                 if ("Comprehension".equals(primaryObjective.getPrimaryObjectiveName())){
                	 Long primaryObjectivePointsPossible = new Long(0);
                     Long primaryObjectiveNumItems = new Long(0);
                	 for(PrimaryObjective prim : primaryObjectives){
                		 if("Listening".equals(prim.getPrimaryObjectiveName()) || "Reading".equals(prim.getPrimaryObjectiveName())){
                			 primaryObjectivePointsPossible += prim.getPrimaryObjectivePointsPossible();
                			 primaryObjectiveNumItems += prim.getPrimaryObjectiveNumItems();
                			 primaryObjective.setPrimaryObjectivePointsPossible(primaryObjectivePointsPossible);
                			 primaryObjective.setPrimaryObjectiveNumItems(primaryObjectiveNumItems);
                		 }
                	 }
                	 for(PrimaryObjective prim : primaryObjectives){
    	           		 if("Writing".equals(prim.getPrimaryObjectiveName())){
    	           			PrimaryObjective tempPO = new PrimaryObjective(primaryObjective);
    	           			tempPO.setSubtestId(prim.getSubtestId());
    	           			tempPO.setPrimaryObjectiveIndex(objectiveIndex);
    	           			tempPrimList.add(tempPO);
    	           			objectiveIndex++;
    	           		 }
    	           		 if("Reading".equals(prim.getPrimaryObjectiveName())){
    	           			PrimaryObjective tempPO = new PrimaryObjective(primaryObjective);
    	           			tempPO.setSubtestId(prim.getSubtestId());
    	           			tempPO.setPrimaryObjectiveIndex(objectiveIndex);
    	           			tempPrimList.add(tempPO);
    	           			objectiveIndex++;
    	           		 }
    	           	 }
                	 //primaryObjectives.add(primaryObjective);
                 	}
                 else if ("Oral".equals(primaryObjective.getPrimaryObjectiveName())){
                	 Long primaryObjectivePointsPossible = new Long(0);
                     Long primaryObjectiveNumItems = new Long(0);
                	 for(PrimaryObjective prim : primaryObjectives){
                		 if("Listening".equals(prim.getPrimaryObjectiveName()) || "Speaking".equals(prim.getPrimaryObjectiveName())){
                			 primaryObjectivePointsPossible += prim.getPrimaryObjectivePointsPossible();
                			 primaryObjectiveNumItems += prim.getPrimaryObjectiveNumItems();
                			 primaryObjective.setPrimaryObjectivePointsPossible(primaryObjectivePointsPossible);
                			 primaryObjective.setPrimaryObjectiveNumItems(primaryObjectiveNumItems);
                		 }
                	 }
                	 for(PrimaryObjective prim : primaryObjectives){
    	           		 if("Listening".equals(prim.getPrimaryObjectiveName())){
    	           			PrimaryObjective tempPO = new PrimaryObjective(primaryObjective);
    	           			tempPO.setSubtestId(prim.getSubtestId());
    	           			tempPO.setPrimaryObjectiveIndex(objectiveIndex);
    	           			tempPrimList.add(tempPO);
    	           			objectiveIndex++;
    	           		 }
    	           		 if("Speaking".equals(prim.getPrimaryObjectiveName())){
    	           			PrimaryObjective tempPO = new PrimaryObjective(primaryObjective);
    	           			tempPO.setSubtestId(prim.getSubtestId());
    	           			tempPO.setPrimaryObjectiveIndex(objectiveIndex);
    	           			tempPrimList.add(tempPO);
    	           			objectiveIndex++;
    	           		 }
    	           	 }
                	 //primaryObjectives.add(primaryObjective);
                	 //objectiveIndex++;
                 	}
                 else if ("Productive".equals(primaryObjective.getPrimaryObjectiveName())){
                	 Long primaryObjectivePointsPossible = new Long(0);
                     Long primaryObjectiveNumItems = new Long(0);
                	 for(PrimaryObjective prim : primaryObjectives){
                		 if("Speaking".equals(prim.getPrimaryObjectiveName()) || "Writing".equals(prim.getPrimaryObjectiveName())){
                			 primaryObjectivePointsPossible += prim.getPrimaryObjectivePointsPossible();
                			 primaryObjectiveNumItems += prim.getPrimaryObjectiveNumItems();
                			 primaryObjective.setPrimaryObjectivePointsPossible(primaryObjectivePointsPossible);
                			 primaryObjective.setPrimaryObjectiveNumItems(primaryObjectiveNumItems);
                		 }
                	 }
                	 for(PrimaryObjective prim : primaryObjectives){
    	           		 if("Speaking".equals(prim.getPrimaryObjectiveName())){
    	           			PrimaryObjective tempPO = new PrimaryObjective(primaryObjective);
    	           			tempPO.setSubtestId(prim.getSubtestId());
    	           			tempPO.setPrimaryObjectiveIndex(objectiveIndex);
    	           			tempPrimList.add(tempPO);
    	           			objectiveIndex++;
    	           		 }
    	           		 if("Writing".equals(prim.getPrimaryObjectiveName())){
    	           			PrimaryObjective tempPO = new PrimaryObjective(primaryObjective);
    	           			tempPO.setSubtestId(prim.getSubtestId());
    	           			tempPO.setPrimaryObjectiveIndex(objectiveIndex);
    	           			tempPrimList.add(tempPO);
    	           			objectiveIndex++;
    	           		 }
    	           	 }
                	 //primaryObjectives.add(primaryObjective);
                	 //objectiveIndex++;
                 	}
                 else if ("Literacy".equals(primaryObjective.getPrimaryObjectiveName())){
                	 Long primaryObjectivePointsPossible = new Long(0);
                     Long primaryObjectiveNumItems = new Long(0);
                	 for(PrimaryObjective prim : primaryObjectives){
                		 if("Reading".equals(prim.getPrimaryObjectiveName()) || "Writing".equals(prim.getPrimaryObjectiveName())){
                			 primaryObjectivePointsPossible += prim.getPrimaryObjectivePointsPossible();
                			 primaryObjectiveNumItems += prim.getPrimaryObjectiveNumItems();
                			 primaryObjective.setPrimaryObjectivePointsPossible(primaryObjectivePointsPossible);
                			 primaryObjective.setPrimaryObjectiveNumItems(primaryObjectiveNumItems);
                		 }
                	 }
                	 for(PrimaryObjective prim : primaryObjectives){
    	           		 if("Reading".equals(prim.getPrimaryObjectiveName())){
    	           			PrimaryObjective tempPO = new PrimaryObjective(primaryObjective);
    	           			tempPO.setSubtestId(prim.getSubtestId());
    	           			tempPO.setPrimaryObjectiveIndex(objectiveIndex);
    	           			tempPrimList.add(tempPO);
    	           			objectiveIndex++;
    	           		 }
    	           		 if("Writing".equals(prim.getPrimaryObjectiveName())){
    	           			PrimaryObjective tempPO = new PrimaryObjective(primaryObjective);
    	           			tempPO.setSubtestId(prim.getSubtestId());
    	           			tempPO.setPrimaryObjectiveIndex(objectiveIndex);
    	           			tempPrimList.add(tempPO);
    	           			objectiveIndex++;
    	           		 }
    	           	 }
                	 //primaryObjectives.add(primaryObjective);
                	 //objectiveIndex++;
                 	}
                 primaryObjectives.addAll(tempPrimList);
             }
         }finally {
             SQLUtil.close(rs);
             ConnectionFactory.getInstance().release(ps);
         }
		
		return primaryObjectives;
	}
    public SecondaryObjective [] getSecondaryObjectives(Long oasRosterId) throws SQLException {
        ArrayList<SecondaryObjective> secondaryObjectives = new ArrayList<SecondaryObjective>();
        Integer productId = null;
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
            "   prod.product_id as productId, " +
            "	sec.ext_cms_item_set_id as monarchId " + 
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
            "   prod.product_id, " +
            "	sec.ext_cms_item_set_id";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(casql);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
            	productId = rs.getInt("productId");
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
                secondaryObjective.setMonarchId(rs.getString("monarchId"));
                
                secondaryObjectives.add(secondaryObjective);
            }
            if(productId == 7501 || productId == 7502){
            	secondaryObjectives = calculateAcademicScore((SecondaryObjective []) secondaryObjectives.toArray(new SecondaryObjective[0]), oasRosterId);
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
        		"	item_set_ancestor isa, " +
        		"	test_roster ros, " +
        		"	test_admin adm, " +
        		"	test_catalog tc " +
        		"where " +
        		"	ros.test_roster_id = ? " +
        		"	and adm.test_admin_id = ros.test_admin_id " +
        		"	and tc.test_catalog_id = adm.test_catalog_id " +
        		"	and prod.product_id = tc.product_id " +
        		"	and tc.item_set_id = isa.ancestor_item_set_id " +
        		"	and isa.item_set_id = ca.item_set_id " +
        		"	and isa.item_set_type = 'TD' " +
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
        	"	iset.item_set_level as subtestLevel, " +
        	"	prod.product_id as productId " +
        	"from " +
        	"	test_roster ros, " +
        	"	test_admin adm, " +
        	"	product prod, " +
        	"	tabe_cat_objective tco, " +
        	"	item_set iset " +
        	"where " +
        	"	ros.test_roster_id = ? " +
        	"	and adm.test_admin_id = ros.test_admin_id " +
        	"	and adm.product_id = prod.product_id " +
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
                primaryObjective.setSubtestLevel(rs.getString("subtestLevel"));
                primaryObjective.setPrimaryObjectiveIndex(new Long(rs.getLong("primaryObjectiveIndex")));
                primaryObjective.setProductId(new Long(rs.getLong("productId")));
                primaryObjective.setSubtestForm("Adaptive");
                
                String level = rs.getString("subtestLevel");
                System.out.println("*** OAS ROSTER ID: " + oasRosterId.toString());
                /*if(null != level && level.contains(primaryObjective.getPrimaryObjectiveId().toString())) {
                	//System.out.println("*** Obj ID: " + primaryObjective.getPrimaryObjectiveId().toString());
                	//System.out.println("*** level: " + level);
                	 String[] objectiveScores = level.split("\\|");
                     for (int i = 0; i < objectiveScores.length; i++) {
                     	String[] individualObj = objectiveScores[i].split(",");
                     	long objId = Long.parseLong(individualObj[0]);
                     	//System.out.println("*** objId: " + objId);
                     	if(primaryObjective.getPrimaryObjectiveId() == objId) {
                     		primaryObjective.setSubtestLevel(individualObj[5]);
                     		//System.out.println("*** Obj Level: " + individualObj[5]);
                     	}
                     }
                } else {
                	primaryObjective.setSubtestLevel(rs.getString("subtestLevel"));
                }*/
               
                
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
    
    public ArrayList<SecondaryObjective> calculateAcademicScore(SecondaryObjective[] secondaryObjectives, Long oasRosterId) throws SQLException{
    	ArrayList<SecondaryObjective> secObjList = new ArrayList<SecondaryObjective>();
    	HashMap<String, SecondaryObjective> academicMap = new HashMap<String, SecondaryObjective>();
    	SecondaryObjective readingAcademic = new SecondaryObjective();
    	SecondaryObjective writingAcademic = new SecondaryObjective();
    	SecondaryObjective speakingAcademic = new SecondaryObjective();
    	SecondaryObjective listeningAcademic = new SecondaryObjective();
    	SecondaryObjective comprehensionAcademic = new SecondaryObjective();
    	SecondaryObjective oralAcademic = new SecondaryObjective();
    	SecondaryObjective productiveAcademic = new SecondaryObjective();
    	SecondaryObjective literacyAcademic = new SecondaryObjective();
    	SecondaryObjective overallAcademic = new SecondaryObjective();
    	Long readingPointPossible = new Long(0);
    	Long readingNumItems = new Long(0);
    	Long writingPointPossible = new Long(0);
    	Long writingNumItems = new Long(0);
    	Long speakingPointPossible = new Long(0);
    	Long speakingNumItems = new Long(0);
    	Long listeningPointPossible = new Long(0);
    	Long listeningNumItems = new Long(0);
    	Long comprehensionPointPossible = new Long(0);
    	Long comprehensionNumItems = new Long(0);
    	Long oralPointPossible = new Long(0);
    	Long oralNumItems = new Long(0);
    	Long productivePointPossible = new Long(0);
    	Long productiveNumItems = new Long(0);
    	Long literacyPointPossible = new Long(0);
    	Long literacyNumItems = new Long(0);
    	Long overallPointPossible = new Long(0);
    	Long overallNumItems = new Long(0);
    	
    	final String SQL = 
        	"SELECT LCO.OBJECTIVE_ID    AS secondaryObjectiveId, " +
        	"	ISET1.ITEM_SET_ID       AS primaryObjectiveId, " +
        	"	LCO.OBJECTIVE_NAME      AS secondaryObjectiveName," +
        	"	LCO.EXT_CMS_ITEM_SET_ID AS monarchId," +
        	"	LCO.TEST_LEVEL          AS subtestLevel, " +
        	"	LCO.SUBJECT             AS subtestName " +
        	"	FROM LASLINK_CD_OBJECTIVE LCO, " +
        	"	TEST_ADMIN           TA, " +
        	"	ITEM_SET             ISET, " +
        	"	TEST_ROSTER          TR, " +
        	"	ITEM_SET             ISET1, " +
        	"	PRODUCT              PRO, " +
        	"	ITEM_SET_CATEGORY    ISC " +
        	"	WHERE TR.TEST_ROSTER_ID = ? " +
        	"	AND TR.TEST_ADMIN_ID = TA.TEST_ADMIN_ID " +
        	"	AND TA.ITEM_SET_ID = ISET.ITEM_SET_ID " +
        	"	AND SUBSTR(ISET.ITEM_SET_LEVEL, 3, LENGTH(ISET.ITEM_SET_LEVEL)) = LCO.TEST_LEVEL " +
        	"	AND LCO.SUBJECT = ISET1.ITEM_SET_NAME AND ISET1.ITEM_SET_LEVEL = LCO.TEST_LEVEL " +
        	"	AND TA.PRODUCT_ID = PRO.PRODUCT_ID AND PRO.PARENT_PRODUCT_ID = ISC.FRAMEWORK_PRODUCT_ID " +
        	"	AND ISC.ITEM_SET_CATEGORY_ID = ISET1.ITEM_SET_CATEGORY_ID";
    	
    	
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(SQL);
            ps.setLong(1, oasRosterId.longValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                SecondaryObjective secObjective = new SecondaryObjective();
                secObjective.setSecondaryObjectiveId(new Long(rs.getLong("secondaryObjectiveId")));
                secObjective.setPrimaryObjectiveId(new Long(rs.getLong("primaryObjectiveId")));
                secObjective.setSecondaryObjectiveName(rs.getString("secondaryObjectiveName"));
                secObjective.setSubtestLevel(rs.getString("subtestLevel"));
                secObjective.setSubtestName(rs.getString("subtestName"));
                secObjective.setMonarchId(rs.getString("monarchId"));
                
                academicMap.put(rs.getString("subtestName").toUpperCase(), secObjective);
            }
        }finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        
    	for(int ii=0; ii<secondaryObjectives.length; ii++){
    		secObjList.add(secondaryObjectives[ii]);
	    		if(secondaryObjectives[ii].getSecondaryObjectiveName().contains("Language Arts / Social Studies / History")|| 
	    				secondaryObjectives[ii].getSecondaryObjectiveName().contains("Mathematics / Science / Technical Subjects") || 
	    					secondaryObjectives[ii].getSecondaryObjectiveName().contains("Foundational Skills")){
	    			
	    			if("READING".equals(secondaryObjectives[ii].getSubtestName())){
	    				readingPointPossible = readingPointPossible + ((Long) secondaryObjectives[ii].getSecondaryObjectivePointsPossible());
	    				readingNumItems = readingNumItems + ((Long) secondaryObjectives[ii].getSecondaryObjectiveNumItems());
	    				if(readingAcademic.getSubtestName() == null){
		    				readingAcademic.setPrimaryObjectiveId(secondaryObjectives[ii].getPrimaryObjectiveId());
		    				readingAcademic.setSecondaryObjectiveName(academicMap.get(secondaryObjectives[ii].getSubtestName()).getSecondaryObjectiveName());
		    				readingAcademic.setSubtestName(secondaryObjectives[ii].getSubtestName());
		    				readingAcademic.setSecondaryObjectiveType(secondaryObjectives[ii].getSecondaryObjectiveType());
		    				readingAcademic.setSubtestForm(secondaryObjectives[ii].getSubtestForm());
		    				readingAcademic.setSubtestLevel(secondaryObjectives[ii].getSubtestLevel());
		    				readingAcademic.setSubtestId(secondaryObjectives[ii].getSubtestId());
		    				readingAcademic.setProductId(secondaryObjectives[ii].getProductId());
		    				readingAcademic.setMonarchId(academicMap.get(secondaryObjectives[ii].getSubtestName()).getMonarchId());
		    				readingAcademic.setSecondaryObjectiveId(new Long(academicMap.get(secondaryObjectives[ii].getSubtestName()).getSecondaryObjectiveId()));
	    				}
		    			readingAcademic.setSecondaryObjectivePointsPossible(readingPointPossible);
		    			readingAcademic.setSecondaryObjectiveNumItems(readingNumItems);
	    			}else if("LISTENING".equals(secondaryObjectives[ii].getSubtestName())){
	    				listeningPointPossible = listeningPointPossible + ((Long) secondaryObjectives[ii].getSecondaryObjectivePointsPossible());
	    				listeningNumItems = listeningNumItems + ((Long) secondaryObjectives[ii].getSecondaryObjectiveNumItems());
	    				if(listeningAcademic.getSubtestName() == null){
		    				listeningAcademic.setPrimaryObjectiveId(secondaryObjectives[ii].getPrimaryObjectiveId());
		    				listeningAcademic.setSecondaryObjectiveName(academicMap.get(secondaryObjectives[ii].getSubtestName()).getSecondaryObjectiveName());
		    				listeningAcademic.setSubtestName(secondaryObjectives[ii].getSubtestName());
		    				listeningAcademic.setSecondaryObjectiveType(secondaryObjectives[ii].getSecondaryObjectiveType());
		    				listeningAcademic.setSubtestForm(secondaryObjectives[ii].getSubtestForm());
		    				listeningAcademic.setSubtestLevel(secondaryObjectives[ii].getSubtestLevel());
		    				listeningAcademic.setSubtestId(secondaryObjectives[ii].getSubtestId());
		    				listeningAcademic.setProductId(secondaryObjectives[ii].getProductId());
		    				listeningAcademic.setMonarchId(academicMap.get(secondaryObjectives[ii].getSubtestName()).getMonarchId());
		    				listeningAcademic.setSecondaryObjectiveId(new Long(academicMap.get(secondaryObjectives[ii].getSubtestName()).getSecondaryObjectiveId()));
	    				}
	    				listeningAcademic.setSecondaryObjectivePointsPossible(listeningPointPossible);
	    				listeningAcademic.setSecondaryObjectiveNumItems(listeningNumItems);
	    			}else if("SPEAKING".equals(secondaryObjectives[ii].getSubtestName())){
	    				speakingPointPossible = speakingPointPossible + ((Long) secondaryObjectives[ii].getSecondaryObjectivePointsPossible());
	    				speakingNumItems = speakingNumItems + ((Long) secondaryObjectives[ii].getSecondaryObjectiveNumItems());
	    				if(speakingAcademic.getSubtestName() == null){
		    				speakingAcademic.setPrimaryObjectiveId(secondaryObjectives[ii].getPrimaryObjectiveId());
		    				speakingAcademic.setSecondaryObjectiveName(academicMap.get(secondaryObjectives[ii].getSubtestName()).getSecondaryObjectiveName());
		    				speakingAcademic.setSubtestName(secondaryObjectives[ii].getSubtestName());
		    				speakingAcademic.setSecondaryObjectiveType(secondaryObjectives[ii].getSecondaryObjectiveType());
		    				speakingAcademic.setSubtestForm(secondaryObjectives[ii].getSubtestForm());
		    				speakingAcademic.setSubtestLevel(secondaryObjectives[ii].getSubtestLevel());
		    				speakingAcademic.setSubtestId(secondaryObjectives[ii].getSubtestId());
		    				speakingAcademic.setProductId(secondaryObjectives[ii].getProductId());
		    				speakingAcademic.setMonarchId(academicMap.get(secondaryObjectives[ii].getSubtestName()).getMonarchId());
		    				speakingAcademic.setSecondaryObjectiveId(new Long(academicMap.get(secondaryObjectives[ii].getSubtestName()).getSecondaryObjectiveId()));
	    				}
	    				speakingAcademic.setSecondaryObjectivePointsPossible(speakingPointPossible);
	    				speakingAcademic.setSecondaryObjectiveNumItems(speakingNumItems);
	    			}else if("WRITING".equals(secondaryObjectives[ii].getSubtestName())){
	    				writingPointPossible = writingPointPossible + ((Long) secondaryObjectives[ii].getSecondaryObjectivePointsPossible());
	    				writingNumItems = writingNumItems + ((Long) secondaryObjectives[ii].getSecondaryObjectiveNumItems());
	    				if(writingAcademic.getSubtestName() == null){
		    				writingAcademic.setPrimaryObjectiveId(secondaryObjectives[ii].getPrimaryObjectiveId());
		    				writingAcademic.setSecondaryObjectiveName(academicMap.get(secondaryObjectives[ii].getSubtestName()).getSecondaryObjectiveName());
		    				writingAcademic.setSubtestName(secondaryObjectives[ii].getSubtestName());
		    				writingAcademic.setSecondaryObjectiveType(secondaryObjectives[ii].getSecondaryObjectiveType());
		    				writingAcademic.setSubtestForm(secondaryObjectives[ii].getSubtestForm());
		    				writingAcademic.setSubtestLevel(secondaryObjectives[ii].getSubtestLevel());
		    				writingAcademic.setSubtestId(secondaryObjectives[ii].getSubtestId());
		    				writingAcademic.setProductId(secondaryObjectives[ii].getProductId());
		    				writingAcademic.setMonarchId(academicMap.get(secondaryObjectives[ii].getSubtestName()).getMonarchId());
		    				writingAcademic.setSecondaryObjectiveId(new Long(academicMap.get(secondaryObjectives[ii].getSubtestName()).getSecondaryObjectiveId()));
	    				}
	    				writingAcademic.setSecondaryObjectivePointsPossible(writingPointPossible);
	    				writingAcademic.setSecondaryObjectiveNumItems(writingNumItems);
	    			}
	    		}
    	}
    	if(readingAcademic != null && listeningAcademic != null && speakingAcademic != null && writingAcademic != null){
	    	secObjList.add(readingAcademic);
	    	secObjList.add(listeningAcademic);
	    	secObjList.add(speakingAcademic);
	    	secObjList.add(writingAcademic);
	    	
	    	for(SecondaryObjective secObj : secObjList){
	    		if(secObj.getSecondaryObjectiveName().contains("Academic")){
	    			if(secObj.getSecondaryObjectiveName().toUpperCase().contains("LISTENING") || secObj.getSecondaryObjectiveName().toUpperCase().contains("READING")){
	    				comprehensionPointPossible = comprehensionPointPossible + ((Long) secObj.getSecondaryObjectivePointsPossible());
	    				comprehensionNumItems = comprehensionNumItems + ((Long) secObj.getSecondaryObjectiveNumItems());
	    				if(comprehensionAcademic.getSecondaryObjectiveName() == null){
	    					comprehensionAcademic.setPrimaryObjectiveId(academicMap.get("COMPREHENSION").getPrimaryObjectiveId());
	    					comprehensionAcademic.setSecondaryObjectiveName(academicMap.get("COMPREHENSION").getSecondaryObjectiveName());
	    					comprehensionAcademic.setSecondaryObjectiveType(secObj.getSecondaryObjectiveType());
	    					comprehensionAcademic.setSubtestForm(secObj.getSubtestForm());
	    					comprehensionAcademic.setSubtestLevel(secObj.getSubtestLevel());
	    					comprehensionAcademic.setProductId(secObj.getProductId());
	    					comprehensionAcademic.setSecondaryObjectiveId(new Long(academicMap.get("COMPREHENSION").getSecondaryObjectiveId()));
	    				}
	    				comprehensionAcademic.setSecondaryObjectivePointsPossible(comprehensionPointPossible);
	    				comprehensionAcademic.setSecondaryObjectiveNumItems(comprehensionNumItems);
	    			}if(secObj.getSecondaryObjectiveName().toUpperCase().contains("LISTENING") || secObj.getSecondaryObjectiveName().toUpperCase().contains("SPEAKING")){
	    				oralPointPossible = oralPointPossible + ((Long) secObj.getSecondaryObjectivePointsPossible());
	    				oralNumItems = oralNumItems + ((Long) secObj.getSecondaryObjectiveNumItems());
	    				if(oralAcademic.getSecondaryObjectiveName() == null){
	    					oralAcademic.setPrimaryObjectiveId(academicMap.get("ORAL").getPrimaryObjectiveId());
	    					oralAcademic.setSecondaryObjectiveName(academicMap.get("ORAL").getSecondaryObjectiveName());
	    					oralAcademic.setSecondaryObjectiveType(secObj.getSecondaryObjectiveType());
	    					oralAcademic.setSubtestForm(secObj.getSubtestForm());
	    					oralAcademic.setSubtestLevel(secObj.getSubtestLevel());
	    					oralAcademic.setProductId(secObj.getProductId());
	    					oralAcademic.setSecondaryObjectiveId(new Long(academicMap.get("ORAL").getSecondaryObjectiveId()));
	    				}
	    				oralAcademic.setSecondaryObjectivePointsPossible(oralPointPossible);
	    				oralAcademic.setSecondaryObjectiveNumItems(oralNumItems);
	    			}if(secObj.getSecondaryObjectiveName().toUpperCase().contains("WRITING") || secObj.getSecondaryObjectiveName().toUpperCase().contains("SPEAKING")){
	    				productivePointPossible = productivePointPossible + ((Long) secObj.getSecondaryObjectivePointsPossible());
	    				productiveNumItems = productiveNumItems + ((Long) secObj.getSecondaryObjectiveNumItems());
	    				if(productiveAcademic.getSecondaryObjectiveName() == null){
	    					productiveAcademic.setPrimaryObjectiveId(academicMap.get("PRODUCTIVE").getPrimaryObjectiveId());
	    					productiveAcademic.setSecondaryObjectiveName(academicMap.get("PRODUCTIVE").getSecondaryObjectiveName());
	    					productiveAcademic.setSecondaryObjectiveType(secObj.getSecondaryObjectiveType());
	    					productiveAcademic.setSubtestForm(secObj.getSubtestForm());
	    					productiveAcademic.setSubtestLevel(secObj.getSubtestLevel());
	    					productiveAcademic.setProductId(secObj.getProductId());
	    					productiveAcademic.setSecondaryObjectiveId(new Long(academicMap.get("PRODUCTIVE").getSecondaryObjectiveId()));
	    				}
	    				productiveAcademic.setSecondaryObjectivePointsPossible(productivePointPossible);
	    				productiveAcademic.setSecondaryObjectiveNumItems(productiveNumItems);
	    			}if(secObj.getSecondaryObjectiveName().toUpperCase().contains("READING") || secObj.getSecondaryObjectiveName().toUpperCase().contains("WRITING")){
	    				literacyPointPossible = literacyPointPossible + ((Long) secObj.getSecondaryObjectivePointsPossible());
	    				literacyNumItems = literacyNumItems + ((Long) secObj.getSecondaryObjectiveNumItems());
	    				if(literacyAcademic.getSecondaryObjectiveName() == null){
	    					literacyAcademic.setPrimaryObjectiveId(academicMap.get("LITERACY").getPrimaryObjectiveId());
	    					literacyAcademic.setSecondaryObjectiveName(academicMap.get("LITERACY").getSecondaryObjectiveName());
	    					literacyAcademic.setSecondaryObjectiveType(secObj.getSecondaryObjectiveType());
	    					literacyAcademic.setSubtestForm(secObj.getSubtestForm());
	    					literacyAcademic.setSubtestLevel(secObj.getSubtestLevel());
	    					literacyAcademic.setProductId(secObj.getProductId());
	    					literacyAcademic.setSecondaryObjectiveId(new Long(academicMap.get("LITERACY").getSecondaryObjectiveId()));
	    				}
	    				literacyAcademic.setSecondaryObjectivePointsPossible(literacyPointPossible);
	    				literacyAcademic.setSecondaryObjectiveNumItems(literacyNumItems);
	    			}
	    		}
	    	}
	    	secObjList.add(comprehensionAcademic);
	    	secObjList.add(oralAcademic);
	    	secObjList.add(productiveAcademic);
	    	secObjList.add(literacyAcademic);
	    	//secObjList.add(overallAcademic);
    	}
    	return secObjList;
    }
    
}