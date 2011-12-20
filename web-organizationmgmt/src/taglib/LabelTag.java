package taglib;


import java.util.HashMap;
import java.util.Properties;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This class is responsible to retrieve the value from properties 
 * file by passing the key and print the value into the browser.
 * @author TCS
 *
 */

public class LabelTag extends TagSupport {


	private String key="";
	private String prefix="";
	private String suffix="";

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int doStartTag() {
		
		try {
						
			StringBuilder sb = new StringBuilder();
			Properties prop = null;
			ServletRequest req = pageContext.getRequest();
			HashMap<String,Properties> map = (HashMap<String,Properties>)req.getAttribute("localeMap");
			String ccode = (String)pageContext.getRequest().getAttribute("ccode");

			if (!map.containsKey(ccode)) {
				throw new RuntimeException("Invalid Country Code...");
			}
			
			prop = map.get(ccode);
			String value = prop.getProperty(this.key);
			
			if (this.prefix != null && !"".equals(this.prefix)) {
				sb.append(this.prefix);
			}
			
			sb.append(value);
			
			if (this.suffix != null && !"".equals(this.suffix)) {
				sb.append(this.suffix);
			}
						
			printContent (sb.toString());

		} catch (Exception ex) {
			throw new Error(ex.getMessage());
		}
		
		return SKIP_BODY;
	}
	
	private void printContent (String content) throws Exception{
		
		JspWriter out = pageContext.getOut();
		out.print(content);
	}

}


