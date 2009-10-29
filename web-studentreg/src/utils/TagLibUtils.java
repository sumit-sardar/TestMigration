package utils; 

import org.apache.beehive.netui.script.el.NetUIReadVariableResolver;
//import org.apache.beehive.netui.script.el.VariableResolver;
import org.apache.beehive.netui.script.el.util.ParseUtils;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import org.apache.struts.action.ActionForm;

public class TagLibUtils 
{ 
    private ActionForm actionForm;
    private Tag tag;
    private PageContext pageContext;
    
    
    
    public TagLibUtils(HttpServletRequest request, Tag tag, PageContext pageContext) throws JspException {
        Enumeration enum1 = request.getAttributeNames();
        String attributeName;
        Object object;
    
        while( enum1.hasMoreElements() ) {
            attributeName = (String) enum1.nextElement();
            object = request.getAttribute(attributeName);
                
            if( object instanceof ActionForm ) {
                this.actionForm = (ActionForm) object;
                break;
            }
        }
        if( this.actionForm == null ) {
            throw new JspException("Could not instantiate TagLibUtils.  Could not find actionForm off of HttpServletRequest.");
        }
        
        this.tag = tag;
        this.pageContext = pageContext;
    }
    

    
    
    public Object getNetUIDataSourceValue(String dataSource) {
        //VariableResolver vr = new NetUIReadVariableResolver(this.actionForm, this.tag, this.pageContext);
    	NetUIReadVariableResolver vr = new NetUIReadVariableResolver(this.pageContext.getVariableResolver());
        
        return ParseUtils.evaluate(dataSource, vr);
    }
    
    
} 
