package com.ctb.control.db;

//import com.bea.control.*;
import java.sql.SQLException;
import java.util.Date;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerConfigurationValue;
import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.bean.testAdmin.FindUser;
import com.ctb.bean.testAdmin.PasswordHintQuestion;
import com.ctb.bean.testAdmin.PasswordHistory;
import com.ctb.bean.testAdmin.Role;
import com.ctb.bean.testAdmin.TimeZones;
import com.ctb.bean.testAdmin.User;

/** 
 * Defines a new database control. 
 * 
 * The @jc:connection tag indicates which WebLogic data source will be used by 
 * this database control. Please change this to suit your needs. You can see a 
 * list of available data sources by going to the WebLogic console in a browser 
 * (typically http://localhost:7001/console) and clicking Services, JDBC, 
 * Data Sources. 
 * 
 * @jc:connection data-source-jndi-name="oasDataSource" 
 */ 
@ControlExtension()
@JdbcControl.ConnectionDataSource(jndiName = "oasDataSource")
public interface Users extends JdbcControl
{
    /**
     * @jc:sql statement::
     * select
     * 	   decode(count(ona.org_node_id), 0, 'false', 'true') as visible
     * from
     * 	org_node_ancestor ona,
     * 	user_role urole,
     * 	users,
     * 	user_role orole,
     * 	users ousers
     * where
     * 	 users.user_name = {userName}
     * 	 and urole.user_id = users.user_id
     * 	 and ona.ancestor_org_node_id = urole.org_node_id
     * 	 and ona.org_node_id = orole.org_node_id
     * 	 and orole.user_id = ousers.user_id
     * 	 and ousers.user_name = {otherName}::
     */
    @JdbcControl.SQL(statement = "select  \t  decode(count(ona.org_node_id), 0, 'false', 'true') as visible from \torg_node_ancestor ona, \tuser_role urole, \tusers, \tuser_role orole, \tusers ousers where \t users.user_name = {userName} \t and urole.user_id = users.user_id \t and ona.ancestor_org_node_id = urole.org_node_id \t and ona.org_node_id = orole.org_node_id \t and orole.user_id = ousers.user_id \t and ousers.user_name = {otherName}")
    String checkVisibility(String userName, String otherName) throws SQLException;
	//attempt to improve performance
	@JdbcControl.SQL(statement = "select  \t  decode(count(ona.org_node_id), 0, 'false', 'true') as visible from \torg_node_ancestor ona, \tuser_role urole, \tusers where \t users.user_name = {userName} \t and urole.user_id = users.user_id \t and ona.ancestor_org_node_id = urole.org_node_id")
    String checkVisibilitySame(String userName, String otherName) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      count(distinct usr.user_id)
     * from
     *      users usr,
     *      user_role onu,
     *      org_node_ancestor ona,
     *      user_role urole,
     *      users
     * where
     *      usr.user_id = {userId}
     *      and onu.user_id = usr.user_id
     *      and ona.org_node_id = onu.org_node_id
     *      and urole.org_node_id = ona.ancestor_org_node_id
     *      and users.user_id = urole.user_id
     *      and users.user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select  count(distinct usr.user_id) from  users usr,  user_role onu,  org_node_ancestor ona,  user_role urole,  users where  usr.user_id = {userId}  and onu.user_id = usr.user_id  and ona.org_node_id = onu.org_node_id  and urole.org_node_id = ona.ancestor_org_node_id  and users.user_id = urole.user_id  and users.user_name = {userName}")
    Integer isUserEditableByUser(String userName, Integer userId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      count(distinct usr.user_id)
     * from
     *      users usr,
     *      user_role onu,
     *      org_node_ancestor ona,
     *      user_role urole,
     *      users,
     *      test_admin admin
     * where
     *      usr.user_id = {userId}
     *      and onu.user_id = usr.user_id
     *      and ona.org_node_id = onu.org_node_id
     *      and onu.activation_status = 'AC'
     *      and urole.org_node_id = ona.ancestor_org_node_id
     *      and urole.activation_status = 'AC'
     *      and users.user_id = urole.user_id
     *      and users.user_name = {userName}
     *      and admin.test_admin_id = {testAdminId}
     * ::
     */
    @JdbcControl.SQL(statement = "select  count(distinct usr.user_id) from  users usr,  user_role onu,  org_node_ancestor ona,  user_role urole,  users,  test_admin admin where  usr.user_id = {userId}  and onu.user_id = usr.user_id  and ona.org_node_id = onu.org_node_id  and onu.activation_status = 'AC'  and urole.org_node_id = ona.ancestor_org_node_id  and urole.activation_status = 'AC'  and users.user_id = urole.user_id  and users.user_name = {userName}  and admin.test_admin_id = {testAdminId}")
    Integer isUserEditableByUserForAdmin(String userName, Integer userId, Integer testAdminId) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
     *      users.user_id as userId,
     *      users.user_name as userName,
     *      users.display_user_name as displayUserName,
     *      users.password,
     *      users.first_name as firstName,
     *      users.middle_name as middleName,
     *      users.last_name as lastName,
     *      users.preferred_name as preferredName,
     *      users.prefix,
     *      users.suffix,
     *      users.time_zone as timeZone,
     *      users.email,
     *      users.password_hint_question_id as passwordHintQuestionId,
     *      users.password_expiration_date as passwordExpirationDate,
     *      users.password_hint_answer as passwordHintAnswer,
     *      users.address_id as addressId,
     *      users.active_session as activeSession,
     *      users.reset_password as resetPassword,
     *      users.last_login_date_time as lastLoginDateTime,
     *      users.ext_pin1 as extPin1,
     *      users.ext_pin2 as extPin2,
     *      users.ext_pin3 as extPin3,
     *      users.ext_school_id as extSchoolId,
     *      users.created_by as createdBy,
     *      users.created_date_time as CreatedDateTime,
     *      users.updated_by as updatedBy,
     *      users.updated_date_time as updatedDateTime,
     *      users.activation_status as activationStatus,
     *      users.data_import_history_id as dataImportHistoryId,
     *      users.display_new_message as displayNewMessage
     * from
	 *      user_role urole,
     *      users
     * where
     *      urole.user_id = users.user_id
     *      and urole.org_node_id = {orgNodeId}
     *      and users.activation_status = 'AC'
     *      and urole.activation_status = 'AC'::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  users.user_id as userId,  users.user_name as userName,  users.display_user_name as displayUserName,  users.password,  users.first_name as firstName,  users.middle_name as middleName,  users.last_name as lastName,  users.preferred_name as preferredName,  users.prefix,  users.suffix,  users.time_zone as timeZone,  users.email,  users.password_hint_question_id as passwordHintQuestionId,  users.password_expiration_date as passwordExpirationDate,  users.password_hint_answer as passwordHintAnswer,  users.address_id as addressId,  users.active_session as activeSession,  users.reset_password as resetPassword,  users.last_login_date_time as lastLoginDateTime,  users.ext_pin1 as extPin1,  users.ext_pin2 as extPin2,  users.ext_pin3 as extPin3,  users.ext_school_id as extSchoolId,  users.created_by as createdBy,  users.created_date_time as CreatedDateTime,  users.updated_by as updatedBy,  users.updated_date_time as updatedDateTime,  users.activation_status as activationStatus,  users.data_import_history_id as dataImportHistoryId,  users.display_new_message as displayNewMessage from  user_role urole,  users where  urole.user_id = users.user_id  and urole.org_node_id = {orgNodeId}  and users.activation_status = 'AC'  and urole.activation_status = 'AC'",
                     arrayMaxLength = 100000)
    User [] getUsersForOrgNode(Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      users.user_id as userId,
     *      users.user_name as userName,
     *      users.display_user_name as displayUserName,
     *      users.password,
     *      users.first_name as firstName,
     *      users.middle_name as middleName,
     *      users.last_name as lastName,
     *      users.preferred_name as preferredName,
     *      users.prefix,
     *      users.suffix,
     *      users.time_zone as timeZone,
     *      users.email,
     *      users.password_hint_question_id as passwordHintQuestionId,
     *      users.password_expiration_date as passwordExpirationDate,
     *      users.password_hint_answer as passwordHintAnswer,
     *      users.address_id as addressId,
     *      users.active_session as activeSession,
     *      users.reset_password as resetPassword,
     *      users.last_login_date_time as lastLoginDateTime,
     *      users.ext_pin1 as extPin1,
     *      users.ext_pin2 as extPin2,
     *      users.ext_pin3 as extPin3,
     *      users.ext_school_id as extSchoolId,
     *      users.created_by as createdBy,
     *      users.created_date_time as CreatedDateTime,
     *      users.updated_by as updatedBy,
     *      users.updated_date_time as updatedDateTime,
     *      users.activation_status as activationStatus,
     *      users.data_import_history_id as dataImportHistoryId,
     *      users.display_new_message as displayNewMessage
     * from
	 *      test_admin_user_role taur,
     *      users
     * where
     *      taur.user_id = users.user_id
     *      and taur.test_admin_id = {testAdminId}
     *      and users.activation_status = 'AC'
     * order by
     *      users.last_name,
     *      users.first_name::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  users.user_id as userId,  users.user_name as userName,  users.display_user_name as displayUserName,  users.password,  users.first_name as firstName,  users.middle_name as middleName,  users.last_name as lastName,  users.preferred_name as preferredName,  users.prefix,  users.suffix,  users.time_zone as timeZone,  users.email,  users.password_hint_question_id as passwordHintQuestionId,  users.password_expiration_date as passwordExpirationDate,  users.password_hint_answer as passwordHintAnswer,  users.address_id as addressId,  users.active_session as activeSession,  users.reset_password as resetPassword,  users.last_login_date_time as lastLoginDateTime,  users.ext_pin1 as extPin1,  users.ext_pin2 as extPin2,  users.ext_pin3 as extPin3,  users.ext_school_id as extSchoolId,  users.created_by as createdBy,  users.created_date_time as CreatedDateTime,  users.updated_by as updatedBy,  users.updated_date_time as updatedDateTime,  users.activation_status as activationStatus,  users.data_import_history_id as dataImportHistoryId,  users.display_new_message as displayNewMessage from  test_admin_user_role taur,  users where  taur.user_id = users.user_id  and taur.test_admin_id = {testAdminId}  and users.activation_status = 'AC' order by  users.last_name,  users.first_name",
                     arrayMaxLength = 100000)
    User [] getProctorUsersForAdmin(Integer testAdminId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      users.user_id as userId,
     *      users.user_name as userName,
     *      users.display_user_name as displayUserName,
     *      users.password,
     *      users.first_name as firstName,
     *      users.middle_name as middleName,
     *      users.last_name as lastName,
     *      users.preferred_name as preferredName,
     *      users.prefix,
     *      users.suffix,
     *      users.time_zone as timeZone,
     *      users.email,
     *      users.password_hint_question_id as passwordHintQuestionId,
     *      phq.password_hint_question as passwordHintQuestion,
     *      users.password_expiration_date as passwordExpirationDate,
     *      users.password_hint_answer as passwordHintAnswer,
     *      users.address_id as addressId,
     *      users.active_session as activeSession,
     *      users.reset_password as resetPassword,
     *      users.last_login_date_time as lastLoginDateTime,
     *      users.ext_pin1 as extPin1,
     *      users.ext_pin2 as extPin2,
     *      users.ext_pin3 as extPin3,
     *      users.ext_school_id as extSchoolId,
     *      users.created_by as createdBy,
     *      users.created_date_time as CreatedDateTime,
     *      users.updated_by as updatedBy,
     *      users.updated_date_time as updatedDateTime,
     *      users.activation_status as activationStatus,
     *      users.data_import_history_id as dataImportHistoryId,
     *      users.display_new_message as displayNewMessage
     * from
	 *      users, password_hint_question phq
     * where
     *      phq.password_hint_question_id (+) = users.password_hint_question_id
     *      and users.user_name = {userName}
     *      and users.activation_status = 'AC'::
     */
    @JdbcControl.SQL(statement = "select  users.user_id as userId,  users.user_name as userName,  users.display_user_name as displayUserName,  users.password,  users.first_name as firstName,  users.middle_name as middleName,  users.last_name as lastName,  users.preferred_name as preferredName,  users.prefix,  users.suffix,  users.time_zone as timeZone,  users.email,  users.password_hint_question_id as passwordHintQuestionId,  phq.password_hint_question as passwordHintQuestion,  users.password_expiration_date as passwordExpirationDate,  users.password_hint_answer as passwordHintAnswer,  users.address_id as addressId,  users.active_session as activeSession,  users.reset_password as resetPassword,  users.last_login_date_time as lastLoginDateTime,  users.ext_pin1 as extPin1,  users.ext_pin2 as extPin2,  users.ext_pin3 as extPin3,  users.ext_school_id as extSchoolId,  users.created_by as createdBy,  users.created_date_time as CreatedDateTime,  users.updated_by as updatedBy,  users.updated_date_time as updatedDateTime,  users.activation_status as activationStatus,  users.data_import_history_id as dataImportHistoryId,  users.display_new_message as displayNewMessage from  users, password_hint_question phq where  phq.password_hint_question_id (+) = users.password_hint_question_id  and users.user_name = {userName}  and users.activation_status = 'AC'")
    User getUserDetails(String userName) throws SQLException;
    
    //attempt to improve performance
    @JdbcControl.SQL(statement = "select  users.user_id as userId,  users.user_name as userName,  users.display_user_name as displayUserName,  users.password,  users.first_name as firstName,  users.middle_name as middleName,  users.last_name as lastName,  users.preferred_name as preferredName,  users.prefix,  users.suffix,  users.time_zone as timeZone,  users.email,  users.password_hint_question_id as passwordHintQuestionId,  users.password_expiration_date as passwordExpirationDate,  users.password_hint_answer as passwordHintAnswer,  users.address_id as addressId,  users.active_session as activeSession,  users.reset_password as resetPassword,  users.last_login_date_time as lastLoginDateTime,  users.ext_pin1 as extPin1,  users.ext_pin2 as extPin2,  users.ext_pin3 as extPin3,  users.ext_school_id as extSchoolId,  users.created_by as createdBy,  users.created_date_time as CreatedDateTime,  users.updated_by as updatedBy,  users.updated_date_time as updatedDateTime,  users.activation_status as activationStatus,  users.data_import_history_id as dataImportHistoryId,  users.display_new_message as displayNewMessage from  users where users.user_name = {userName}  and users.activation_status = 'AC'")
    User getUserDetailsWithoutPassword(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *      customer.customer_id as customerId,
     *      customer.customer_name as customerName,
     *      customer.override_hide_accommodations as hideAccommodations,
     *      customer.import_student_editable as importStudentEditable,
     *      customer.demographic_visible as demographicVisible
     * from
     *      users,
     *      user_role urole,
     *      org_node node,
     *      customer
     * where
     *      node.org_node_id = urole.org_node_id
     *      and urole.user_id = users.user_id
     *      and customer.customer_id = node.customer_id
     *      and urole.activation_status = 'AC'
     *      and users.user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select distinct  customer.customer_id as customerId,  customer.customer_name as customerName,  customer.override_hide_accommodations as hideAccommodations,  customer.import_student_editable as importStudentEditable,  customer.demographic_visible as demographicVisible from  users,  user_role urole,  org_node node,  customer where  node.org_node_id = urole.org_node_id  and urole.user_id = users.user_id  and customer.customer_id = node.customer_id  and urole.activation_status = 'AC'  and users.user_name = {userName}")
    Customer getCustomer(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select user_id from users where user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select user_id from users where user_name = {userName}")
    Integer getUserIdForName(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      max(node.customer_id)
     * from
     *      users, org_node node, user_role urole
     * where
     *      users.user_name = {userName}
     *      and urole.user_id = users.user_id
     *      and node.org_node_id = urole.org_node_id
     *      and urole.activation_status = 'AC'::
     */
    @JdbcControl.SQL(statement = "select  max(node.customer_id) from  users, org_node node, user_role urole where  users.user_name = {userName}  and urole.user_id = users.user_id  and node.org_node_id = urole.org_node_id  and urole.activation_status = 'AC'")
    Integer getCustomerIdForName(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *      max(urole.org_node_id)
     * from
     *      users, user_role urole
     * where
     *      users.user_name = {userName}
     *      and urole.activation_status = 'AC'
     *      and urole.user_id = users.user_id::
     */
    @JdbcControl.SQL(statement = "select  max(urole.org_node_id)  from  users, user_role urole where  users.user_name = {userName}  and urole.activation_status = 'AC'  and urole.user_id = users.user_id")
    Integer getFirstTopNodeForUser(String userName) throws SQLException;

    static final long serialVersionUID = 1L;

    /**
     * @jc:sql statement::
     * select distinct
     *   r.role_id as roleId,
     *   r.role_name as roleName,
     *   r.created_by as createdBy,
     *   r.created_date_time as createdDateTime,
     *   r.updated_by as updatedBy,
     *   r.updated_date_time as updatedDateTime,
     *   r.activation_status as activationStatus,
     *   r.role_type_id as roleTypeId
     * from role r, user_role ur, users u
     * where r.role_id = ur.role_id
     *    and ur.user_id = u.user_id
     *    and ur.activation_status = 'AC'
     *    and u.user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select distinct  r.role_id as roleId,  r.role_name as roleName,  r.created_by as createdBy,  r.created_date_time as createdDateTime,  r.updated_by as updatedBy,  r.updated_date_time as updatedDateTime,  r.activation_status as activationStatus,  r.role_type_id as roleTypeId from role r, user_role ur, users u where r.role_id = ur.role_id  and ur.user_id = u.user_id  and ur.activation_status = 'AC'  and u.user_name = {userName}")
    Role getRole(String userName);

    /**
     * @jc:sql statement::
     * select tz.time_zone as timeZone,
     * INITCAP(tz.time_zone_desc) as timeZoneDesc
     * from time_zone_code tz
     * ::
     */
    @JdbcControl.SQL(statement = "select tz.time_zone as timeZone, INITCAP(tz.time_zone_desc) as timeZoneDesc from time_zone_code tz")
    TimeZones[] getTimeZones() throws SQLException;

    /**
     * @jc:sql statement::
     * insert into
     *     users (
     * 		user_id,
     * 		user_name,
     * 		password,
     * 		first_name,
     * 		middle_name,
     * 		last_name,
     * 		email,
     * 		password_expiration_date,
     * 		reset_password,
     * 		address_id,
     * 		active_session,
     * 		time_zone,
     * 		created_date_time,
     *   	created_by,
     * 		updated_date_time,
     * 	    updated_by,
     * 		last_login_date_time,
     * 		preferred_name,
     * 		activation_status,
     * 		display_user_name,
     * 		display_new_message,
     * 		ext_pin1
     * 		) values (
     * 		seq_user_id.nextval,
     * 		{user.userName},
     * 		{user.password},
     * 		{user.firstName},
     * 		{user.middleName},
     * 		{user.lastName},
     * 		{user.email},
     * 		{user.passwordExpirationDate},
     * 		{user.resetPassword},
     * 		{user.addressId},
     * 		{user.activeSession},
     * 		{user.timeZone},
     * 		{user.CreatedDateTime},
     * 		{user.createdBy},
     *      {user.updatedDateTime},
     * 		{user.updatedBy},
     *      {user.lastLoginDateTime},
     * 		{user.preferredName},
     *      {user.activationStatus},
     * 		{user.displayUserName},
     * 		{user.displayNewMessage},
     * 		{user.extPin1}
     * 		)::
     */


    @JdbcControl.SQL(statement = "insert into  users ( \t\tuser_id, \t\tuser_name, \t\tpassword, \t\tfirst_name, \t\tmiddle_name, \t\tlast_name, \t\temail, \t\tpassword_expiration_date, \t\treset_password, \t\taddress_id, \t\tactive_session, \t\ttime_zone, \t\tcreated_date_time,  \tcreated_by, \t\tupdated_date_time, \t  updated_by, \t\tlast_login_date_time, \t\tpreferred_name, \t\tactivation_status, \t\tdisplay_user_name, \t\tdisplay_new_message, \t\text_pin1 \t\t) values ( \t\tseq_user_id.nextval, \t\t{user.userName}, \t\t{user.password}, \t\t{user.firstName}, \t\t{user.middleName}, \t\t{user.lastName}, \t\t{user.email}, \t\t{user.passwordExpirationDate}, \t\t{user.resetPassword}, \t\t{user.addressId}, \t\t{user.activeSession}, \t\t{user.timeZone}, \t\t{user.CreatedDateTime}, \t\t{user.createdBy},  {user.updatedDateTime}, \t\t{user.updatedBy},  {user.lastLoginDateTime}, \t\t{user.preferredName},  {user.activationStatus}, \t\t{user.displayUserName}, \t\t{user.displayNewMessage}, \t\t{user.extPin1} \t\t)")
    void createUser(User user) throws SQLException;



    /**
     * @jc:sql statement::
     * select seq_user_id.nextval
     * from dual ::
     */
    @JdbcControl.SQL(statement = "select seq_user_id.nextval  from dual ")
    Integer getNextPK() throws SQLException;

    /**
     * @jc:sql statement::
     * update users
     * set first_name = {user.firstName},
     * 	middle_name = {user.middleName},
     * 	last_name = {user.lastName},
     * 	email = {user.email},
     * 	ext_pin1 = {user.extPin1},
     * 	time_zone = {user.timeZone},
     * 	address_id = {user.addressId},
     * 	updated_By = {user.updatedBy},
     * 	updated_Date_Time = {user.updatedDateTime}
     * where user_id = {user.userId}::
     */
    @JdbcControl.SQL(statement = "update users set first_name = {user.firstName}, \tmiddle_name = {user.middleName}, \tlast_name = {user.lastName}, \temail = {user.email}, \text_pin1 = {user.extPin1}, \ttime_zone = {user.timeZone}, \taddress_id = {user.addressId}, \tupdated_By = {user.updatedBy}, \tupdated_Date_Time = {user.updatedDateTime} where user_id = {user.userId}")
    void updateUser(User user) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *      users.user_id as userId,
     *      users.user_name as userName,
     *      concat(concat(users.last_name, ', '), concat(users.first_name, concat(' ', users.MIDDLE_NAME))) as displayUserName,
     *      users.password,
     *      users.first_name as firstName,
     *      users.middle_name as middleName,
     *      users.last_name as lastName,
     *      users.preferred_name as preferredName,
     *      users.prefix,
     *      users.suffix,
     *      users.time_zone as timeZone,
     *      users.email,
     *      users.password_hint_question_id as passwordHintQuestionId,
     *      users.password_expiration_date as passwordExpirationDate,
     *      users.password_hint_answer as passwordHintAnswer,
     *      users.address_id as addressId,
     *      users.active_session as activeSession,
     *      users.reset_password as resetPassword,
     *      users.last_login_date_time as lastLoginDateTime,
     *      users.ext_pin1 as extPin1,
     *      users.ext_pin2 as extPin2,
     *      users.ext_pin3 as extPin3,
     *      users.ext_school_id as extSchoolId,
     *      users.created_by as createdBy,
     *      users.created_date_time as CreatedDateTime,
     *      users.updated_by as updatedBy,
     *      users.updated_date_time as updatedDateTime,
     *      users.activation_status as activationStatus,
     *      users.data_import_history_id as dataImportHistoryId,
     *      users.display_new_message as displayNewMessage,
     *      role.role_id as roleId,
     *      INITCAP(role.role_name) as roleName
     * from
     *      user_role urole,
     *      users,
     *      role role
     * where
     *      urole.user_id = users.user_id
     *      and urole.org_node_id = {orgNodeId}
     *      and users.activation_status = 'AC'
     *      and urole.activation_status = 'AC'
     *      and urole.role_id = role.role_id
     * ::
     */
    @JdbcControl.SQL(statement = "select distinct  users.user_id as userId,  users.user_name as userName,  concat(concat(users.last_name, ', '), concat(users.first_name, concat(' ', users.MIDDLE_NAME))) as displayUserName,  users.password,  users.first_name as firstName,  users.middle_name as middleName,  users.last_name as lastName,  users.preferred_name as preferredName,  users.prefix,  users.suffix,  users.time_zone as timeZone,  users.email,  users.password_hint_question_id as passwordHintQuestionId,  users.password_expiration_date as passwordExpirationDate,  users.password_hint_answer as passwordHintAnswer,  users.address_id as addressId,  users.active_session as activeSession,  users.reset_password as resetPassword,  users.last_login_date_time as lastLoginDateTime,  users.ext_pin1 as extPin1,  users.ext_pin2 as extPin2,  users.ext_pin3 as extPin3,  users.ext_school_id as extSchoolId,  users.created_by as createdBy,  users.created_date_time as CreatedDateTime,  users.updated_by as updatedBy,  users.updated_date_time as updatedDateTime,  users.activation_status as activationStatus,  users.data_import_history_id as dataImportHistoryId,  users.display_new_message as displayNewMessage,  role.role_id as roleId,  INITCAP(role.role_name) as roleName from  user_role urole,  users,  role role where  urole.user_id = users.user_id  and urole.org_node_id = {orgNodeId}  and users.activation_status = 'AC'  and urole.activation_status = 'AC'  and urole.role_id = role.role_id")
    FindUser[] getUsersAtNode(Integer orgNodeId) throws SQLException;

    /**
     * @jc:sql statement::
     * update users u
     * set u.activation_status = 'IN'
     * , u.updated_by = {user.updatedBy}
     * , u.updated_date_time = {user.updatedDateTime}
     * where u.user_id = {user.userId}::
     */
    @JdbcControl.SQL(statement = "update users u set u.activation_status = 'IN' , u.updated_by = {user.updatedBy} , u.updated_date_time = {user.updatedDateTime} where u.user_id = {user.userId}")
    void inactivateUser(User user) throws SQLException;

    /**
     * @jc:sql statement::
     * SELECT phq.password_hint_question_id passwordHintQuestionId,
     * phq.password_hint_question passwordHintQuestion
     * FROM password_hint_question phq
     * where phq.activation_status = 'AC'::
     */
    @JdbcControl.SQL(statement = "SELECT phq.password_hint_question_id passwordHintQuestionId,  phq.password_hint_question passwordHintQuestion FROM password_hint_question phq where phq.activation_status = 'AC'")
    PasswordHintQuestion[] getHintQuestions() throws SQLException;

    /**
     * @jc:sql statement::
     * update users set
     * password = {user.password},
     * password_expiration_date = {user.passwordExpirationDate},
     * reset_password = {user.resetPassword},
     * display_new_message = {user.displayNewMessage},
     * last_login_date_time = null,
     * updated_by = (select u.user_id from users u  where u.user_name = {userName}),
     * updated_date_time = {updatedDate}
     * where user_name = {user.userName}::
     */
    @JdbcControl.SQL(statement = "update users set  password = {user.password}, password_expiration_date = {user.passwordExpirationDate}, reset_password = {user.resetPassword}, display_new_message = {user.displayNewMessage}, last_login_date_time = null, updated_by = (select u.user_id from users u  where u.user_name = {userName}), updated_date_time = {updatedDate}  where user_name = {user.userName}")
    void updatePassword(String userName, User user, Date updatedDate) throws SQLException;

    /**
     * @jc:sql statement::
     * update users set
     * password = {user.password},
     * password_expiration_date = {user.passwordExpirationDate},
     * password_hint_answer = {user.passwordHintAnswer},
     * password_hint_question_id = {user.passwordHintQuestionId},
     * reset_password = {user.resetPassword},
     * display_new_message = {user.displayNewMessage},
     * updated_by = (select u.user_id from users u  where u.user_name = {userName}),
     * updated_date_time = {updatedDate}
     * where user_name = {user.userName}::
     */
    @JdbcControl.SQL(statement = "update users set  password = {user.password}, password_expiration_date = {user.passwordExpirationDate}, password_hint_answer = {user.passwordHintAnswer}, password_hint_question_id = {user.passwordHintQuestionId}, reset_password = {user.resetPassword}, display_new_message = {user.displayNewMessage}, updated_by = (select u.user_id from users u  where u.user_name = {userName}), updated_date_time = {updatedDate}  where user_name = {user.userName}")
    void updateOwnPassword(String userName, User user, Date updatedDate) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     * passHist.user_id as userId,
     * passHist.password as password,
     * passHist.created_date_time as createdDate
     * from password_history passHist
     * where passHist.User_Id = (select u.user_id from users u  where u.user_name = {selectedUserName})::
     */
    @JdbcControl.SQL(statement = "select  passHist.user_id as userId, passHist.password as password, passHist.created_date_time as createdDate from password_history passHist  where passHist.User_Id = (select u.user_id from users u  where u.user_name = {selectedUserName})")
    PasswordHistory[] getPasswordHistory(String selectedUserName) throws SQLException;

    /**
     * @jc:sql statement::
     * delete
     * from
     * password_history passHist
     * where passHist.user_id = (select u.user_id from users u where u.user_name = {selectedUserName})
     * and
     * passHist.password = {password}::
    */
    @JdbcControl.SQL(statement = "delete  from password_history passHist  where passHist.user_id = (select u.user_id from users u where u.user_name = {selectedUserName})  and  passHist.password = {password}")
    void deletePasswordHistory(String selectedUserName, String password);

    /**
     * @jc:sql statement::
     * insert into
     *      password_history (
     *      user_id,
     *      password,
     *      created_date_time
     *      ) values (
     *      (select u.user_id from users  u where u.user_name = {selectedUserName}),
     *      {passHistDetail.password},
     *      {passHistDetail.createdDate}
     *      )::
     */
    @JdbcControl.SQL(statement = "insert into  password_history (  user_id,  password,  created_date_time  ) values (  (select u.user_id from users  u where u.user_name = {selectedUserName}),  {passHistDetail.password},  {passHistDetail.createdDate}  )")
    void addPasswordHistory(String selectedUserName, PasswordHistory passHistDetail) throws SQLException;



    /**
     * @jc:sql statement::
     * select u.password as password
     *    from users u
     *    where u.user_name = {selectedUserName}::
     */
    @JdbcControl.SQL(statement = "select u.password as password  from users u  where u.user_name = {selectedUserName}")
    String getPasswordForUser(String selectedUserName) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *    decode (count(passHist.password), 0 ,0,1)
     *    from password_history passHist
     *    where passHist.user_id = (select u.user_id from users u.user_name = {selectedUserName})
     *    and
     *    passHist.password = {newPassword}::
     */
  //  boolean isRepeatedPassword(String selectedUserName, String newPassword) throws SQLException;

    /**
     * @jc:sql statement::
     * SELECT DISTINCT u.user_id as userId,
     * u.user_name as userName,
     * u.first_name as firstName,
     * u.middle_name as middleName,
     * u.last_name as lastName,
     * concat(concat(u.last_name, ', '), concat(u.first_name, concat(' ', u.MIDDLE_NAME))) as displayUserName,
     * u.email as email,
     * r2.role_id as roleId,
     * INITCAP(r2.role_name) as roleName
     * FROM users u,
     * users u1,
     * user_role ur1,
     * user_role ur2,
     * role r2,
     * org_node_ancestor ona,
     * org_node node1,
     * org_node node2
     * WHERE u1.user_name = {userName_}
     * AND ur1.user_id = u1.user_id
     * AND ur1.activation_status = 'AC'
     * AND ur1.org_node_id = node1.org_node_id
     * AND node1.activation_status = 'AC'
     * AND ur1.org_node_id = ona.ancestor_org_node_id
     * AND ona.number_of_levels >= {number_of_levels}
     * AND ona.org_node_id = ur2.org_node_id
     * AND ur2.org_node_id = node2.org_node_id
     * And node2.activation_status = 'AC'
     * AND ur2.activation_status = 'AC'
     * AND ur2.user_id = u.user_id
     * AND u.activation_status = 'AC'
     * AND ur2.role_id = r2.role_id
     * AND ( ( ur2.role_id LIKE {sRoleId1_} ) OR ( ur2.role_id LIKE {sRoleId2_} ) )
     * {sql: searchCriteria}::
     */
    //FindUser [] searchUsers(String userName_, Integer number_of_levels, String sRoleId1_, String sRoleId2_, String searchCriteria);

    /**
     * @jc:sql statement::
     * select decode (count(itemSet.item_set_id),0,0,1) result
     *    from item_set itemSet where
     *    itemSet.Activation_Status = 'AC'
     *    and (itemSet.Created_By = {userId}
     *    or itemSet.Updated_By = {userId})::
     */
    @JdbcControl.SQL(statement = "select decode (count(itemSet.item_set_id),0,0,1) result  from item_set itemSet where  itemSet.Activation_Status = 'AC'  and (itemSet.Created_By = {userId}  or itemSet.Updated_By = {userId})")
    boolean hasUserCreatedTest(Integer userId) throws SQLException;

    /**
     * @jc:sql statement::
     * select decode (count(i.item_id),0,0,1)
     * from item i
     * where created_by = {userId}::
     */
    @JdbcControl.SQL(statement = "select decode (count(i.item_id),0,0,1)  from item i  where created_by = {userId}")
    boolean isAnyItemsLinkToUser(Integer userId) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *       decode(count(testAdmin.test_admin_id),0,0,1)
     * from test_admin testAdmin
     * where testAdmin.created_By = {createdBy}
     * and testAdmin.activation_status = 'AC'
     * ::
     */
    @JdbcControl.SQL(statement = "select  decode(count(testAdmin.test_admin_id),0,0,1)  from test_admin testAdmin  where testAdmin.created_By = {createdBy} and testAdmin.activation_status = 'AC'")
    boolean isTestAdmin(Integer createdBy) throws SQLException;

    /**
     * @jc:sql statement::
     * select decode (count(testAdminUserRole.User_Id),0,0,1)
     *  from test_admin_user_role testAdminUserRole
     *  where testAdminUserRole.user_id = {selectedUserId}::
     */
    @JdbcControl.SQL(statement = "select decode (count(testAdminUserRole.User_Id),0,0,1)  from test_admin_user_role testAdminUserRole  where testAdminUserRole.user_id = {selectedUserId}")
    boolean isTestProctor(Integer selectedUserId) throws SQLException;

    /**
     * @jc:sql statement::
     *  SELECT DISTINCT {sql: selectClause}
     *  FROM users u,
     *  users u1,
     *  user_role ur1,
     *  user_role ur2,
     *  role r2,
     *  org_node_ancestor ona,
     *  org_node node1,
     *  org_node node2
     *  WHERE u1.user_name = {userName}
     *  AND ur1.user_id = u1.user_id
     *  AND ur1.activation_status = 'AC'
     *  AND ur1.org_node_id = node1.org_node_id
     *  AND node1.activation_status = 'AC'
     *  AND ur1.org_node_id = ona.ancestor_org_node_id
     *  AND ona.number_of_levels >= 0
     *  AND ona.org_node_id = ur2.org_node_id
     *  AND ur2.org_node_id = node2.org_node_id
     *  And node2.activation_status = 'AC'
     *  AND ur2.activation_status = 'AC'
     *  AND ur2.user_id = u.user_id
     *  AND u.activation_status = 'AC'
     *  AND ur2.role_id = r2.role_id
     *  {sql: whereClause}::
     */
    @JdbcControl.SQL(statement = "SELECT DISTINCT {sql: selectClause} FROM users u, users u1, user_role ur1, user_role ur2, role r2, org_node_ancestor ona, org_node node1, org_node node2 WHERE u1.user_name = {userName} AND ur1.user_id = u1.user_id AND ur1.activation_status = 'AC' AND ur1.org_node_id = node1.org_node_id AND node1.activation_status = 'AC' AND ur1.org_node_id = ona.ancestor_org_node_id AND ona.number_of_levels >= 0 AND ona.org_node_id = ur2.org_node_id AND ur2.org_node_id = node2.org_node_id And node2.activation_status = 'AC' AND ur2.activation_status = 'AC' AND ur2.user_id = u.user_id AND u.activation_status = 'AC' AND ur2.role_id = r2.role_id {sql: whereClause}")
    String [] findUserSuggestions(String userName, String selectClause, String whereClause) throws SQLException;

    /**
     * @jc:sql statement::
     * select count(user_name)
     *  from users
     *  where user_name like {formatedUserName} ESCAPE '\'
     *  or user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select count(user_name)  from users  where user_name like {formatedUserName} ESCAPE '\\'  or user_name = {userName}")
    int findExistingUserNameCount(String formatedUserName, String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select max(to_number(nvl(regexp_replace(u.user_name, {selectRegExp}, {replaceStr}), 0)))
     * from (SELECT USER_NAME FROM users WHERE  user_name LIKE {userNameescape} || '%' ESCAPE '\') u
     * where regexp_like(u.user_name, {whereRegExp})
     * or u.user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select max(to_number(nvl(regexp_replace(u.user_name, {selectRegExp}, {replaceStr}), 0))) from (SELECT USER_NAME FROM users WHERE  user_name LIKE {userNameescape} || '%' ESCAPE '\\') u where regexp_like(u.user_name, {whereRegExp}) or u.user_name = {userName}")
    Integer findExistingUserName(String userName, String userNameescape, String whereRegExp, String selectRegExp, String replaceStr) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
     *      u.user_id as userId,
     *      u.user_name as userName,
     *      u.first_name as firstName,
     *      u.middle_name as middleName,
     *      u.last_name as lastName,
     * 	    concat(concat(u.last_name, ', '), concat(u.first_name, concat(' ', u.MIDDLE_NAME))) as displayUserName,
     * 	    u.email as email,
     *      INITCAP(r.role_name) as roleName,
     *      r.role_id as roleId,
     *      u.ext_pin1 as extPin1,
     *      u.ext_pin2 as extPin2,
     *      u.address_id as addressId
     * from
     *      users u,
     *      user_role ur,
     *      role r,
     *      org_node node
     * where
     *      u.user_id = ur.user_id
     *    and ur.org_node_id = node.org_node_id
     *    and ur.role_id = r.role_id
     *    and node.activation_status= 'AC'
     *    and u.activation_status= 'AC'
     *    and ur.activation_status = 'AC'
     *    and ur.org_node_id in  (select distinct ona.org_node_id
     *    from org_node_ancestor ona where {sql:fn in(ona.ancestor_org_node_id,{orgNodeId})}
     *    )::
     *    array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select distinct  u.user_id as userId,  u.user_name as userName,  u.first_name as firstName,  u.middle_name as middleName,  u.last_name as lastName, \t  concat(concat(u.last_name, ', '), concat(u.first_name, concat(' ', u.MIDDLE_NAME))) as displayUserName, \t  u.email as email,  INITCAP(r.role_name) as roleName,  r.role_id as roleId,  u.ext_pin1 as extPin1,  u.ext_pin2 as extPin2,  u.address_id as addressId from  users u,  user_role ur,  role r,  org_node node where  u.user_id = ur.user_id  and ur.org_node_id = node.org_node_id  and ur.role_id = r.role_id  and node.activation_status= 'AC'  and u.activation_status= 'AC'  and ur.activation_status = 'AC'  and ur.org_node_id in  (select distinct ona.org_node_id  from org_node_ancestor ona where {sql:fn in(ona.ancestor_org_node_id,{orgNodeId})}  )",
                     arrayMaxLength = 100000)
    FindUser[] getUsersAtOrBelowNodes(String orgNodeId) throws SQLException;


    /**
     * @jc:sql statement::
     * select distinct
     *      u.user_id as userId,
     *      u.user_name as userName,
     *      u.first_name as firstName,
     *      u.middle_name as middleName,
     *      u.last_name as lastName,
     * 	    concat(concat(u.last_name, ', '), concat(u.first_name, concat(' ', u.MIDDLE_NAME))) as displayUserName,
     * 	    u.email as email,
     *      INITCAP(r.role_name) as roleName,
     *      r.role_id as roleId,
     *      u.ext_pin1 as extPin1,
     *      u.ext_pin2 as extPin2,
     *      u.address_id as addressId
     * from
     *      users u,
     *      user_role ur,
     *      role r,
     *      org_node node
     * where
     *      u.user_id = ur.user_id
     *    and ur.org_node_id = node.org_node_id
     *    and ur.role_id = r.role_id
     *    and node.activation_status= 'AC'
     *    and u.activation_status= 'AC'
     *    and ur.activation_status = 'AC'
     *    and ur.org_node_id in  (select distinct ona.org_node_id
     *    from org_node_ancestor ona where {sql:fn in(ona.ancestor_org_node_id,{orgNodeId})})
     *    {sql: searchCriteria}::
     *    array-max-length="all"
     *    {sql:fn in(ona.ancestor_org_node_id,{orgNodeId})})  {sql: searchCriteria}
     */

    @JdbcControl.SQL(statement = "select distinct  u.user_id as userId,  u.user_name as userName,  u.first_name as firstName,  u.middle_name as middleName,  u.last_name as lastName, \t  concat(concat(u.last_name, ', '), concat(u.first_name, concat(' ', u.MIDDLE_NAME))) as displayUserName, \t  u.email as email,  INITCAP(r.role_name) as roleName,  r.role_id as roleId,  u.ext_pin1 as extPin1,  u.ext_pin2 as extPin2,  u.address_id as addressId from  users u,  user_role ur,  role r,  org_node node where  u.user_id = ur.user_id  and ur.org_node_id = node.org_node_id  and ur.role_id = r.role_id  and node.activation_status= 'AC'  and u.activation_status= 'AC'  and ur.activation_status = 'AC'  and ur.org_node_id in  (select distinct ona.org_node_id  from org_node_ancestor ona where {sql: searchCriteriaTemp} {sql: searchCriteria})",
                     arrayMaxLength = 10000)
    FindUser[] getUsersAtOrBelowNodesForActManager(String searchCriteriaTemp, String searchCriteria) throws SQLException;

    /**
     * @jc:sql statement::
     * select c.customer_id as customerId
     * , c.email_type as emailType
     * , c.reply_to as replyTo
     * , c.subject as subject
     * , c.email_body as emailBody
     * from customer_email_config c
     * where c.customer_id = (select distinct node.customer_id
     *                          from users u
     *                          , user_role role
     *                          , org_node node
     *                          where u.user_name = {userName}
     *                          and u.user_id = role.user_id
     *                          and role.org_node_id = node.org_node_id)
     * and c.email_type = {emailType}::
     */
    @JdbcControl.SQL(statement = "select c.customer_id as customerId , c.email_type as emailType , c.reply_to as replyTo , c.subject as subject , c.email_body as emailBody from customer_email_config c where c.customer_id = (select distinct node.customer_id  from users u  , user_role role  , org_node node  where u.user_name = {userName}  and u.user_id = role.user_id  and role.org_node_id = node.org_node_id) and c.email_type = {emailType}")
    CustomerEmail getCustomerEmailByUserName(String userName, Integer emailType) throws SQLException;

    /**
     * @jc:sql statement::
     * select c.customer_id as customerId
     * , c.email_type as emailType
     * , c.reply_to as replyTo
     * , c.subject as subject
     * , c.email_body as emailBody
     * from customer_email_config c
     * where c.customer_id = (select distinct node.customer_id
     *                          from org_node node
     *                          where node.org_node_id = {orgNodeId})
     * and c.email_type = {emailType}::
     */
    @JdbcControl.SQL(statement = "select c.customer_id as customerId , c.email_type as emailType , c.reply_to as replyTo , c.subject as subject , c.email_body as emailBody from customer_email_config c where c.customer_id = (select distinct node.customer_id  from org_node node  where node.org_node_id = {orgNodeId}) and c.email_type = {emailType}")
    CustomerEmail getCustomerEmailByOrgId(Integer orgNodeId, Integer emailType) throws SQLException;


    /**
     * @jc:sql statement::
     * select
     *      users.user_id as userId
     * from
     *      users
     * where
     *  users.user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select  users.user_id as userId from  users where  users.user_name = {userName}")
    Integer getUserIdFromUserName(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *   decode( count(cc.customer_id),0,0,1)
     *
     *       from customer_configuration cc
     *      where cc.customer_id = {customerId}
     *      and cc.customer_configuration_name = {customerConfigurationName}::
     */
    @JdbcControl.SQL(statement = "select  decode( count(cc.customer_id),0,0,1)  from customer_configuration cc  where cc.customer_id = {customerId}  and cc.customer_configuration_name = {customerConfigurationName}")
    boolean isDexCustomer(String customerConfigurationName, Integer customerId) throws SQLException;



    /**
     * @jc:sql statement="select lpad(dex.dex_user_sequence.nextval,4,0) from dual"
     */
    @JdbcControl.SQL(statement = "select lpad(dex.dex_user_sequence.nextval,4,0) from dual")
    String getDexSequence() throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *   decode( count(u.user_id),0,0,1)
     * from users u
     * where u.user_name = {userName}
     * ::
     */
    @JdbcControl.SQL(statement = "select  decode( count(u.user_id),0,0,1)  from users u where u.user_name = {userName}")
    boolean isUserNameAlreadyExists(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * insert into dex.dex_user_password
     *   (user_id,
     *    user_name,
     *    user_password_new)
     * values (
     *    {userId},
     *    {userName},
     *    {newPassword}
     *   )::
     */
    @JdbcControl.SQL(statement = "insert into dex.dex_user_password  (user_id,  user_name,  user_password_new) values (  {userId},  {userName},  {newPassword}  )")
    void addDexPassword(Integer userId, String userName, String newPassword) throws SQLException;

    /**
     * @jc:sql statement::
     *  update dex.dex_user_password d
     *     set d.user_password_old = (select dd.user_password_new
     * 							   from dex.dex_user_password dd
     * 							   where dd.user_id = {userId}),
     * 		d.user_password_new = {newPassword}
     *   where d.user_id = {userId}::
     */
    @JdbcControl.SQL(statement = "update dex.dex_user_password d  set d.user_password_old = (select dd.user_password_new  \t\t\t\t\t\t\t  from dex.dex_user_password dd  \t\t\t\t\t\t\t  where dd.user_id = {userId}),  \t\td.user_password_new = {newPassword}  where d.user_id = {userId}")
    void updateDexPassword(Integer userId, String newPassword) throws SQLException;
    
    
    @JdbcControl.SQL(statement = "select  customer_configuration_id as id,  customer_configuration_name as customerConfigurationName,  customer_id as customerId,  editable as editable,  default_value as defaultValue from customer_configuration where customer_id = {customerId}")
    CustomerConfiguration [] getCustomerConfigurations(int customerId) throws SQLException;
    
    @JdbcControl.SQL(statement = "select  customer_configuration_value as customerConfigurationValue,  customer_configuration_id as customerConfigurationId,  sort_order sortOrder from customer_configuration_value where customer_configuration_id = {customerConfigurationId} order by sort_order, customer_configuration_value")
    CustomerConfigurationValue [] getCustomerConfigurationValues(int customerConfigurationId) throws SQLException;

}