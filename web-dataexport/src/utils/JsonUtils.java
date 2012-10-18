package utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.ctb.bean.testAdmin.RubricViewData;

public class JsonUtils {

	
	
public static String getJson (Object _object,String alias,Class<?> _class) throws Exception{
		
		XStream jsondriver = new XStream(
				new JettisonMappedXmlDriver());
		jsondriver.alias(alias,_class);
		jsondriver.alias("entry",RubricViewData.class);
		return jsondriver.toXML(_object);
		
	}
}
