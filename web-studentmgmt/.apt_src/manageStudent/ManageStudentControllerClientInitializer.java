
package manageStudent;

import java.lang.reflect.Field;
import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.Controls;
import org.apache.beehive.controls.api.versioning.VersionRequired;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.runtime.bean.EventAdaptor;
import org.apache.beehive.controls.runtime.bean.AdaptorPersistenceDelegate;

@SuppressWarnings("all")
public class ManageStudentControllerClientInitializer
extends org.apache.beehive.controls.runtime.bean.ClientInitializer
{
    static final Field _studentManagementField;
    static
    {
        try
        {
            _studentManagementField = manageStudent.ManageStudentController.class.getDeclaredField("studentManagement");
            _studentManagementField.setAccessible(true);
        }
        catch (NoSuchFieldException __bc_nsfe)
        {
            throw new ExceptionInInitializerError(__bc_nsfe);
        }
    }
    
    
    private static void initializeFields(ControlBeanContext cbc,
    manageStudent.ManageStudentController client)
    {
        try
        {
            String __bc_id;
            //
            // Initialize any nested controls used by the client
            //
            __bc_id = client.getClass() + "@" + client.hashCode() + ".com.ctb.control.studentManagement.StudentManagement.studentManagement";
            com.ctb.control.studentManagement.StudentManagementBean _studentManagement = (cbc == null ? null : (com.ctb.control.studentManagement.StudentManagementBean)cbc.getBean(__bc_id));
            if (_studentManagement == null)
            _studentManagement = (com.ctb.control.studentManagement.StudentManagementBean) Controls.instantiate(com.ctb.control.studentManagement.StudentManagementBean.class, getAnnotationMap(cbc, _studentManagementField), cbc, __bc_id );
            
            
            _studentManagementField.set(client, _studentManagement);
        }
        catch (RuntimeException __bc_re) { throw __bc_re; }
        catch (Exception __bc_e)
        {
            __bc_e.printStackTrace();
            throw new ControlException("Initializer failure", __bc_e);
        }
    }
    
    public static void initialize(ControlBeanContext cbc, manageStudent.ManageStudentController client)
    {
        
        initializeFields( cbc, client );
    }
}
