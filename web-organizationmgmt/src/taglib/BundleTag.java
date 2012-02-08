package taglib;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

public class BundleTag extends TagSupport{
	
	private String baseName = "";
	private String ccode = "";
	public String getBaseName() {
		
		if (ccode != null && !"".equals(ccode)) {
			
			return baseName+"_"+ccode+".properties";
		}
		
		return baseName+".properties";
	}
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	public String getCcode() {
		return ccode;
	}
	public void setCcode(String ccode) {
		this.ccode = ccode;
	}
	
	public int doStartTag() {
		try {
			
			boolean requiredToLoadResource = false;
			ServletRequest req = pageContext.getRequest();
			HashMap<String,Properties> map = null;
			//HashMap<String,Properties> map = (HashMap<String,Properties>)session.getAttribute("localeMap");
			if(req.getAttribute("localeMap")== null ) {
				requiredToLoadResource = true;
			} else if (  req.getAttribute("bunble") == null  ||  ! req.getAttribute("bunble").equals(this.getBaseName())){
				requiredToLoadResource = true;
			} else {
				HashMap<String,Properties> mval = (HashMap<String,Properties>)req.getAttribute("localeMap");
				if(mval.get(getCountryCode()) == null) {
					requiredToLoadResource = true;
				}
			}
			
			if(requiredToLoadResource){
				req.removeAttribute("localeMap");
				req.removeAttribute("bunble");
				map = new HashMap<String,Properties> ();
				map.put(getCountryCode(), getProperties ());
				req.setAttribute("localeMap", map);
				req.setAttribute("bunble", this.getBaseName());
			} else {
				//System.out.println("Resource ["+baseName+"] Already Loaded...");
			}

		} catch (Exception ex) {
			throw new Error("Key not found...");
		}
		return SKIP_BODY;
	}
	
	private String getCountryCode () {

		if (this.ccode != null && !"".equals(this.ccode)) {
			
			pageContext.getRequest().setAttribute("ccode", this.ccode);
			return this.ccode;
		} else {
			
			pageContext.getRequest().setAttribute("ccode", "en");
			return "en";
		}
		//String ccode = "en";
		/*String prop = this.getBaseName();
		prop = prop.substring(0, prop.indexOf("."));
		if (prop.contains("_")) {

			ccode = prop.substring(prop.indexOf("_")+1,prop.length());
		}*/
		//return ccode;
	}

	private Properties getProperties () throws Exception {

		String propName = this.getBaseName();
		Properties prop = new Properties ();
		InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propName); 
		System.out.println("inStream::"+inStream);		
		prop.load(inStream);
		return prop;
	}

}
