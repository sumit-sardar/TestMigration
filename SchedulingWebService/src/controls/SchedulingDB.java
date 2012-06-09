package controls;

import model.*;

import org.apache.beehive.controls.api.bean.*;
import org.apache.beehive.controls.system.jdbc.*;

@ControlExtension
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface SchedulingDB extends JdbcControl {
 
	static final long serialVersionUID = 1L;

	@JdbcControl.SQL(statement = "select ta.test_admin_id as sessionId, ta.test_admin_name as sessionName, ta.access_code accessCode, st.student_id as studentId, st.user_name as loginId, tr.password as password from student st, test_roster tr, test_admin ta  where st.student_id = tr.student_id and tr.test_admin_id = ta.test_admin_id and st.student_id = (select student_id from student where upper(first_name) like upper({firstName}) and upper(last_name) like upper({lastName}))")
	public SchedulingData scheduleSession(String testLevel, String firstName, String lastName);
}
