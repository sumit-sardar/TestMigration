package com.ctb.testSessionInfo.utils; 

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;



public class JsonUtils {
	public static String getJsonString (Object _object, String alias, Class<?> _class) throws Exception{
		
		XStream jsondriver = new XStream(new JettisonMappedXmlDriver());
		jsondriver.alias(alias, _class);
		jsondriver.alias("entry", String.class);
		return jsondriver.toXML(_object);
		
	}

}
