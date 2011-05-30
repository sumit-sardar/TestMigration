package utils;

import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;



public class JsonStudentUtils {
	public static String getJson (Object _object,String alias,Class<?> _class) throws Exception{
		
		XStream jsondriver = new XStream(new JettisonMappedXmlDriver());
		jsondriver.alias(alias,_class);
		jsondriver.alias("entry",StudentSessionStatus.class);
		return jsondriver.toXML(_object);
		
	}

}
