package misc;
import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
 
/**
 * @jpf:controller
 *  */
@Jpf.Controller()
public class MiscController extends PageFlowController
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
     * @jpf:forward name="success" path="viewToolTips.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "viewToolTips.do")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="toolTips.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "toolTips.jsp")
    })
    protected Forward viewToolTips()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="calendar.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "calendar.jsp")
    })
    protected Forward viewCalendar(ViewCalendarForm form)
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="orderedList.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "orderedList.jsp")
    })
    protected Forward viewOrderedList()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="sortList.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "sortList.jsp")
    })
    protected Forward sortList()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="sectionClient.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "sectionClient.do")
    })
    protected Forward showSectionClient()
    {
        SectionForm form = new SectionForm();
        
        form.setSectionVisible(Boolean.TRUE);
        form.setSecondSectionVisible(Boolean.TRUE);
        
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="sectionServer.do"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "sectionServer.do")
    })
    protected Forward showSectionServer()
    {
        SectionForm form = new SectionForm();
        
        form.setSectionVisible(Boolean.TRUE);
        form.setSecondSectionVisible(Boolean.TRUE);
        
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="sectionClient.jsp"
     */
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "sectionClient.jsp")
    })
    protected Forward sectionClient(SectionForm form)
    {    
        return new Forward("success", form);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="sectionServer.jsp"
     */
	@Jpf.Action(
		forwards = { 
			@Jpf.Forward(name = "success", path = "sectionServer.jsp")
		}
	)
    protected Forward sectionServer(SectionForm form)
    {    
        String currentAction = form.getCurrentAction();
        if (currentAction.equals("sectionOne")) {
            Boolean visible = form.getSectionVisible();
            form.setSectionVisible( new Boolean( ! visible.booleanValue() ));
        }
        if (currentAction.equals("sectionTwo")) {
            Boolean visible = form.getSecondSectionVisible();
            form.setSecondSectionVisible( new Boolean( ! visible.booleanValue() ));
        }
        
        
        return new Forward("success", form);
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class SectionForm extends FormData
    {
        private String actionElement;
        private String currentAction;        
        private Boolean sectionVisible;
        private Boolean secondSectionVisible;

        public SectionForm() {
            this.actionElement = "";
            this.currentAction = "";
            this.sectionVisible = Boolean.TRUE;
            this.secondSectionVisible = Boolean.TRUE;
        }
        public void setActionElement(String actionElement)
        {
            this.actionElement = actionElement;
        }
        public String getActionElement()
        {
            return this.actionElement;
        }
        public void setCurrentAction(String currentAction)
        {
            this.currentAction = currentAction;
        }
        public String getCurrentAction()
        {
            return this.currentAction;
        }        
        public void setSectionVisible(Boolean sectionVisible)
        {
            this.sectionVisible = sectionVisible;
        }
        public Boolean getSectionVisible()
        {
            return this.sectionVisible;
        }        
        public void setSecondSectionVisible(Boolean secondSectionVisible)
        {
            this.secondSectionVisible = secondSectionVisible;
        }
        public Boolean getSecondSectionVisible()
        {
            return this.secondSectionVisible;
        }        
        
    }
    
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class ViewCalendarForm extends FormData
    {
        private String noEarlierDate;

        private String startDate2;

        private String startDate;

        public ViewCalendarForm() {
            SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
            sdf.applyPattern("MM/dd/yy");
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add( Calendar.DATE, 1);
            this.startDate = sdf.format( tomorrow.getTime() );

            sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
            sdf.applyPattern("MM/dd/yy");
            Calendar nextWeek = Calendar.getInstance();
            nextWeek.add( Calendar.DATE, 7);
            this.startDate2 = sdf.format( nextWeek.getTime() );

            sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
            sdf.applyPattern("MM/dd/yy");
            Calendar nextDay = Calendar.getInstance();
            nextDay.add( Calendar.DATE, 2);
            this.noEarlierDate = sdf.format( nextDay.getTime() );
        }

        public void setStartDate(String startDate)
        {
            this.startDate = startDate;
        }

        public String getStartDate()
        {
            return this.startDate;
        }

        public void setStartDate2(String startDate2)
        {
            this.startDate2 = startDate2;
        }

        public String getStartDate2()
        {
            return this.startDate2;
        }

        public void setNoEarlierDate(String noEarlierDate)
        {
            this.noEarlierDate = noEarlierDate;
        }

        public String getNoEarlierDate()
        {
            return this.noEarlierDate;
        }
    }
}
