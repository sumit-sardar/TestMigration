package formElements;
import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.ctb.util.web.sanitizer.SanitizedFormData;

/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class FormElementsController extends PageFlowController
{
    static final long serialVersionUID = 1L;

    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
	/*
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
		}
	)
	*/
    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success",
                         path = "index.jsp")
    		},
    		validationErrorForward = @Jpf.Forward(name = "failure", path = "/logout.do"))
    protected Forward begin(BeginForm form)
    {
        return new Forward("success");
    }

    @Jpf.Action(forwards = { 
            @Jpf.Forward(name = "success",
                         path = "/error.jsp")
    		}
    )
    protected Forward logout(BeginForm form)
    {
    	System.out.println("logout.do");
        return new Forward("success");
    }
    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class BeginForm extends SanitizedFormData
    {
        private List selectMultiOptionValues;

        private String selectSingleOptionValues;

        private String[] checkBoxVerticalValues = {"Apples", "Plums"};

        private String[] checkBoxHorizontalValues = {"XML", "XHTML"};

        private String radioButtonVerticalValue = "Cake";

        private String radioButtonHorizontalValue = "Red";

        private String passwordFieldValue = "abcdef123";

        private String textAreaValue = "Example Text";

        private String textFieldValue = "Enabled Text";

        private String textFieldDisabledValue = "Disabled Text";

        public BeginForm()
        {
        	
        }
        
        public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
        {            
            return super.validate(mapping, request);
        }
         
        public void setTextFieldValue(String textFieldValue)
        {
            this.textFieldValue = textFieldValue;
        }

        public String getTextFieldValue()
        {
            return this.textFieldValue;
        }

        public void setTextFieldDisabledValue(String textFieldDisabledValue)
        {
            this.textFieldDisabledValue = textFieldDisabledValue;
        }

        public String getTextFieldDisabledValue()
        {
            return this.textFieldDisabledValue;
        }


        public String getTextAreaValue()
        {
            return this.textAreaValue;
        }

        public void setTextAreaValue(String str)
        {
            this.textAreaValue = str;
        }
        
        public void setPasswordFieldValue(String passwordFieldValue)
        {
            this.passwordFieldValue = passwordFieldValue;
        }

        public String getPasswordFieldValue()
        {
            return this.passwordFieldValue;
        }

        public void setRadioButtonHorizontalValue(String radioButtonHorizontalValue)
        {
            this.radioButtonHorizontalValue = radioButtonHorizontalValue;
        }

        public String getRadioButtonHorizontalValue()
        {
            return this.radioButtonHorizontalValue;
        }

        public void setRadioButtonVerticalValue(String radioButtonVerticalValue)
        {
            this.radioButtonVerticalValue = radioButtonVerticalValue;
        }

        public String getRadioButtonVerticalValue()
        {
            return this.radioButtonVerticalValue;
        }

        public void setCheckBoxHorizontalValues(String[] checkBoxHorizontalValues)
        {
            this.checkBoxHorizontalValues = checkBoxHorizontalValues;
        }

        public String[] getCheckBoxHorizontalValues()
        {
            return this.checkBoxHorizontalValues;
        }

        public void setCheckBoxVerticalValues(String[] checkBoxVerticalValues)
        {
            this.checkBoxVerticalValues = checkBoxVerticalValues;
        }

        public String[] getCheckBoxVerticalValues()
        {
            return this.checkBoxVerticalValues;
        }

        public void setSelectSingleOptionValues(String selectSingleOptionValues)
        {
            this.selectSingleOptionValues = selectSingleOptionValues;
        }

        public String getSelectSingleOptionValues()
        {
            return this.selectSingleOptionValues;
        }

        public void setSelectMultiOptionValues(List selectMultiOptionValues)
        {
            this.selectMultiOptionValues = selectMultiOptionValues;
        }

        public List getSelectMultiOptionValues()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.selectMultiOptionValues == null)
            {
                this.selectMultiOptionValues = new ArrayList();
            }

            return this.selectMultiOptionValues;
        }
    }
}
