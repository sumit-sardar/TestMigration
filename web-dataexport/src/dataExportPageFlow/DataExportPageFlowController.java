package dataExportPageFlow;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.internal.BaseActionForm;

import com.ctb.bean.testAdmin.User;
import com.ctb.control.dataExportManagement.DataExportManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.jmsutils.ExportDataJMSUtil;
import com.ctb.util.web.sanitizer.SanitizedFormData;

@Jpf.Controller()
public class DataExportPageFlowController extends PageFlowController {
	private static final long serialVersionUID = 1L;

	private User user = null;
	private String userName = null;
	//private Integer customerId = null;

	public String pageTitle = null;
	public String pageMessage = null;

	//CustomerConfiguration[] customerConfigurations = null;
	//CustomerConfigurationValue[] customerConfigurationsValue = null;

	private static final String ACTION_DEFAULT = "defaultAction";
	private static final String ACTION_EXPORT_DATA = "export_data";
	public String action = ACTION_EXPORT_DATA;

	@Control
	private DataExportManagement dataExportManagement;

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "index.jsp") })
	protected Forward begin() {
		// retrieveInfoFromSession();
		return new Forward("success");
	}

	
		
	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "submitDataExportTask.jsp") })
	public Forward generateReport(DataExportForm form) {
		// this.hasReport = (String)getRequest().getParameter("hasReport");
		// this.bulkAcc = (String)getRequest().getParameter("bulkAcc");

		init(form);
		Integer customerId = this.user.getCustomer().getCustomerId();
		String userName = this.user.getUserName();

		 System.out.println("userName"+userName);
		 System.out.println("customerId"+customerId);
		 
		ExportDataJMSUtil exportDataJMSUtil = new ExportDataJMSUtil ();
		 try {
			//dataExportManagement.initGenerateReportTask(userName, customerId);
			 exportDataJMSUtil.initGenerateReportTask (userName, customerId);
		} catch (CTBBusinessException e) {
			e.printStackTrace();
		}
		 Forward forward = new Forward("success");
		 return forward;
	}

	@Jpf.Action(forwards = { @Jpf.Forward(name = "success", path = "submitDataExportTask.jsp") })
	public Forward beginGenerateReport(DataExportForm form) {
		// this.hasReport = (String)getRequest().getParameter("hasReport");
		// this.bulkAcc = (String)getRequest().getParameter("bulkAcc");
		 init(form);
		Forward forward = new Forward("success");
		return forward;
	}

	private void init(DataExportForm form) {
		java.security.Principal principal = getRequest().getUserPrincipal();
		this.userName = principal.toString();
		getSession().setAttribute("userName", this.userName);
		form.setCurrentAction(ACTION_DEFAULT);
		form.setActionElement(ACTION_DEFAULT);
		this.action = ACTION_DEFAULT;

		if (this.user == null) {
			try {
				this.user = this.dataExportManagement.getUserDetails(this.userName,	this.userName);
			} catch (CTBBusinessException be) {
				be.printStackTrace();
			}

		}
	}

	public static class DataExportForm extends BaseActionForm {
		private static final long serialVersionUID = 1L;
		private String actionElement;
		private String currentAction;
		

		

		public void init() {
			this.actionElement = ACTION_DEFAULT;
			this.currentAction = ACTION_DEFAULT;
			
		}

		public DataExportForm createClone() {
			DataExportForm copied = new DataExportForm();

			copied.setActionElement(this.actionElement);
			copied.setCurrentAction(this.currentAction);

			

			return copied;
		}

		public void validateValues() {
			

		}

		

		/**
		 * @return the actionElement
		 */
		public String getActionElement() {
			return actionElement;
		}

		/**
		 * @param actionElement
		 *            the actionElement to set
		 */
		public void setActionElement(String actionElement) {
			this.actionElement = actionElement;
		}

		/**
		 * @return the currentAction
		 */
		public String getCurrentAction() {
			return currentAction;
		}

		/**
		 * @param currentAction
		 *            the currentAction to set
		 */
		public void setCurrentAction(String currentAction) {
			this.currentAction = currentAction;
		}

		/**
		 * @return the message
		 */
		

		public void resetValuesForAction(String actionElement) {


		}

		

	}

	public String getAction() {
		return action;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}