create or replace and compile java source named labelname as
import java.io.*;
 import java.sql.*;
 import oracle.sql.*;
 import oracle.jdbc.driver.*;
 import java.util.*;
 
 
class LabelName{
     
  static HashMap map = new HashMap();
	static {
		
		map.put(new Integer(11)	,"Cantonese");
		map.put(new Integer(12)	,"Cebuano (Visayan)");
		map.put(new Integer(13)	,"Chaldean");
		map.put(new Integer(14)	,"Chamorro (Guamanian)");
		map.put(new Integer(15)	,"Chaozhou (Chaochow)");
		map.put(new Integer(16)	,"Croatian");
		map.put(new Integer(18)	,"Dutch");
		map.put(new Integer(21)	,"Farsi (Persian)");
		map.put(new Integer(22)	,"Filipino (Pilipino or Tagalog)");
		map.put(new Integer(23)	,"French");
		map.put(new Integer(24)	,"French Creole");
		map.put(new Integer(26)	,"German");
		map.put(new Integer(27)	,"Greek");
		map.put(new Integer(28)	,"Gujarati");
		map.put(new Integer(30)	,"Hebrew");
		map.put(new Integer(31)	,"Hindi");
		map.put(new Integer(32)	,"Hmong");
		map.put(new Integer(33)	,"Hungarian");
		map.put(new Integer(35)	,"Ilocano");
		map.put(new Integer(36)	,"Indonesian");
		map.put(new Integer(37)	,"Italian");
		map.put(new Integer(39)	,"Japanese");
		map.put(new Integer(41)	,"Khmer (Cambodian)");
		map.put(new Integer(42)	,"Khmu");
		map.put(new Integer(43)	,"Korean");
		map.put(new Integer(44)	,"Kurdish");
		map.put(new Integer(46)	,"Lahu");
		map.put(new Integer(47)	,"Lao");
		map.put(new Integer(49)	,"Mai Mai");
		map.put(new Integer(50)	,"Mandarin (Putonghua)");
		map.put(new Integer(51)	,"Marshallese");
		map.put(new Integer(52)	,"Mien (Yao)");
		map.put(new Integer(53)	,"Mixteco");
		map.put(new Integer(59)	,"Pashto");
		map.put(new Integer(60)	,"Polish");
		map.put(new Integer(61)	,"Portuguese");
		map.put(new Integer(62)	,"Punjabi");
		map.put(new Integer(65)	,"Rumanian");
		map.put(new Integer(66)	,"Russian");
		map.put(new Integer(68)	,"Samoan");
		map.put(new Integer(69)	,"Serbo-Croatian (Serbian)");
		map.put(new Integer(70)	,"Somali");
		map.put(new Integer(71)	,"Spanish");
		map.put(new Integer(73)	,"Taiwanese");
		map.put(new Integer(74)	,"Thai");
		map.put(new Integer(75)	,"Tigrinya");
		map.put(new Integer(76)	,"Toishanese");
		map.put(new Integer(77)	,"Tongan");
		map.put(new Integer(78)	,"Turkish");
		map.put(new Integer(80)	,"Ukrainian");
		map.put(new Integer(81)	,"Urdu");
		map.put(new Integer(83),"Vietnamese");
		map.put(new Integer(99),"All Other Languages Not Listed");
	}
  
	public static String getLanguage(Integer code)
	  {
	    return getLang(code);
	  }
    
    private static String getLang(Integer code) {
 
	
            return (String)map.get(code);
    
    }

}




