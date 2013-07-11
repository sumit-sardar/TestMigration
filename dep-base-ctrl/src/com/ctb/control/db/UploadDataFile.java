package com.ctb.control.db; 

import com.bea.control.*;
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import com.ctb.bean.testAdmin.CustomerDemographicValue; 
import java.sql.SQLException; 
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.FindUser;
import com.ctb.bean.testAdmin.StudentDemoGraphics;
import com.ctb.bean.testAdmin.StudentDemoGraphicsData;
import com.ctb.bean.testAdmin.StudentFileRow;
import com.ctb.bean.testAdmin.UserFileRow;
import com.ctb.bean.testAdmin.DataFileAudit;
import com.ctb.bean.testAdmin.DataFileTemp;
import com.ctb.bean.testAdmin.UserNode;
import com.ctb.bean.testAdmin.Role;
import java.sql.Blob;
import com.ctb.bean.testAdmin.Student;
import org.apache.beehive.controls.api.bean.ControlExtension;

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
public interface UploadDataFile extends JdbcControl
{ 
    // Sample database function.  Uncomment to use 

    // static public class Customer 
    // { 
    //   public int id; 
    //   public String name; 
    // } 
    // 
    // /** 
    //  * @jc:sql statement="SELECT ID, NAME FROM CUSTOMERS WHERE ID = {id}" 
    //  */ 
    // Customer findCustomer(int id);

    // Add "throws SQLException" to request that SQLExeptions be thrown on errors.

    static final long serialVersionUID = 1L;

    /**
     * @jc:sql statement::
     * select distinct
     *     parent.parent_org_node_id as parentorgNodeId,
     *     descendant_node.org_node_id as orgNodeId,
     *     descendant_node.customer_id as customerId,
     *     descendant_node.org_node_category_id as orgNodeCategoryId,
     *     descendant_node.org_node_name as orgNodeName,
     *     descendant_node.activation_status as activationStatus,
     *     descendant_node.org_node_code as orgNodeCode,
     *     cat.category_name as orgNodeCategoryName,
     *     cat.category_level as categoryLevel
     * from 
     *      org_node node, 
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node,
     *      user_role uor,
     *      users,
     *      user_role tur,
     *      org_node_parent parent,
     *      users tu
     * where 
     *      cat.org_node_category_id = descendant_node.org_node_category_id
     *      and node.org_node_id = tur.org_node_id
     *      and tur.user_id = tu.user_id
     *      and tu.user_name = {userName}
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and parent.org_node_id = descendant_node.org_node_id
     *      and descendant_node.activation_status = 'AC'
     *      and uor.org_node_id (+) = descendant_node.org_node_id
     *      and users.user_id (+) = uor.user_id
     *      
     *      order by cat.category_level, parent.parent_org_node_id::
     */
    @JdbcControl.SQL(statement = "select distinct  parent.parent_org_node_id as parentorgNodeId,  descendant_node.org_node_id as orgNodeId,  descendant_node.customer_id as customerId,  descendant_node.org_node_category_id as orgNodeCategoryId,  descendant_node.org_node_name as orgNodeName,  descendant_node.activation_status as activationStatus,  descendant_node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  cat.category_level as categoryLevel from  org_node node,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node,  user_role uor,  users,  user_role tur,  org_node_parent parent,  users tu where  cat.org_node_category_id = descendant_node.org_node_category_id  and node.org_node_id = tur.org_node_id  and tur.user_id = tu.user_id  and tu.user_name = {userName}  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and parent.org_node_id = descendant_node.org_node_id  and descendant_node.activation_status = 'AC'  and uor.org_node_id (+) = descendant_node.org_node_id  and users.user_id (+) = uor.user_id  order by cat.category_level, parent.parent_org_node_id")
    Node[] getUserDataTemplate(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select parent.parent_org_node_id as parentOrgNodeId,    
     *     node.org_node_id as orgNodeId,
     *     node.customer_id as customerId,
     *     node.org_node_category_id as orgNodeCategoryId,
     *     node.org_node_name as orgNodeName,
     *     node.ext_qed_pin as extQedPin,
     *     node.ext_elm_id as extElmId,
     *     node.ext_org_node_type as extOrgNodeType,
     *     node.org_node_description as orgNodeDescription,
     *     node.created_by as createdBy,
     *     node.created_date_time as createdDateTime,
     *     node.updated_by as updatedBy,
     *     node.updated_date_time as updatedDateTime,
     *     node.activation_status as activationStatus,
     *     node.data_import_history_id as dataImportHistoryId,
     *     node.parent_state as parentState,
     *     node.parent_region as parentRegion,
     *     node.parent_county as parentCounty,
     *     node.parent_district as parentDistrict,
     *     node.org_node_code as orgNodeCode,
     *     cat.category_name as orgNodeCategoryName
     *
     * 
     * from 
     * org_node_parent parent ,org_node node, org_node_category cat  
     * 
     * where node.customer_id = {customerId}
     * and node.org_node_id = parent.org_node_id 
     * and node.activation_status = 'AC'
     * and cat.activation_status = 'AC'
     * and node.org_node_category_id = cat.org_node_category_id
     * start with parent .org_node_id in
     * 	(select org.org_node_id 
     *      from org_node org, org_node_parent op 
     *      where org.customer_id = {customerId} and org.activation_status = 'AC'
     *      and op.org_node_id = org.org_node_id and op.parent_org_node_id = 2)
     * connect by prior node.org_node_id = parent.parent_org_node_id::
     */
    @JdbcControl.SQL(statement = "select parent.parent_org_node_id as parentOrgNodeId,  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName  from  org_node_parent parent ,org_node node, org_node_category cat  where node.customer_id = {customerId} and node.org_node_id = parent.org_node_id  and node.activation_status = 'AC' and cat.activation_status = 'AC' and node.org_node_category_id = cat.org_node_category_id start with parent .org_node_id in \t(select org.org_node_id  from org_node org, org_node_parent op  where org.customer_id = {customerId} and org.activation_status = 'AC'  and op.org_node_id = org.org_node_id and op.parent_org_node_id = 2) connect by prior node.org_node_id = parent.parent_org_node_id",
    		arrayMaxLength = 100000)
    Node[] getOrgPathFromCustomerToLoginUser(Integer customerId) throws SQLException;
    
    /**
     * @jc:sql statement::
     *   select pt.parent_org_node_id    as parentorgNodeId,
     *             pt.org_node_id           as orgNodeId,
     *             pt.customer_id           as customerId,
     *             org.org_node_category_id as orgNodeCategoryId,
     *             org.org_node_name        as orgNodeName,
     *             org.org_node_code        as orgNodeCode,
     *             oca.category_name        as orgNodeCategoryName,
     *             oca.category_level       as categoryLevel
     *      
     *        from org_node_parent pt, org_node org, org_node_category oca
     *       where pt.org_node_id = org.org_node_id
     *       and org..activation_status = 'AC'
     *       and oca.activation_status = 'AC'
     *         and org.org_node_category_id = oca.org_node_category_id
     *       start with pt.parent_org_node_id = {customerTopNodeId}
     *      connect by prior pt.org_node_id = pt.parent_org_node_id::
     */
    @JdbcControl.SQL(statement = "select pt.parent_org_node_id  as parentorgNodeId,  pt.org_node_id  as orgNodeId,  pt.customer_id  as customerId,  org.org_node_category_id as orgNodeCategoryId,  org.org_node_name  as orgNodeName,  org.org_node_code  as orgNodeCode,  oca.category_name  as orgNodeCategoryName,  oca.category_level  as categoryLevel  from org_node_parent pt, org_node org, org_node_category oca  where pt.org_node_id = org.org_node_id  and org..activation_status = 'AC'  and oca.activation_status = 'AC'  and org.org_node_category_id = oca.org_node_category_id  start with pt.parent_org_node_id = {customerTopNodeId}  connect by prior pt.org_node_id = pt.parent_org_node_id",
    			arrayMaxLength = 100000)
    Node[] getOrgPathsFromCustomerToLoginUser(Integer customerTopNodeId) throws SQLException;


    /**
     * @jc:sql array-max-length="all" statement::
     * select u.user_id             as userId,
     *        u.user_name           as userName,
     *        u.first_name          as firstName,
     *        u.middle_name         as middleName,
     *        u.last_name           as lastName,
     *        concat(concat(u.last_name, ', '),
     *               concat(u.first_name, concat(' ', u.MIDDLE_NAME))) as displayUserName,
     *        u.email               as email,
     *        INITCAP(r.role_name)  as roleName,
     *        r.role_id             as roleId,
     *        u.ext_pin1            as extPin1,
     *        u.ext_pin2            as extPin2,
     *        u.address_id          as addressId,
     *        node.org_node_id      as orgNodeId,
     *        t.time_zone_desc      as timeZoneDese
     *        
     *   from users u, user_role ur, role r, org_node node, time_zone_code t
     *    where u.user_id = ur.user_id
     *    and ur.org_node_id = node.org_node_id
     *    and ur.role_id = r.role_id
     *    and u.time_zone = t.time_zone
     *    and node.activation_status = 'AC'
     *    and u.activation_status = 'AC'
     *    and ur.activation_status = 'AC'
     *    and ur.org_node_id in
     *        (select distinct ona.org_node_id
     *           from org_node_ancestor ona
     *          where ona.ancestor_org_node_id in
     *                (select org_node_id
     *                   from user_role
     *                  where user_id = (select user_id
     *                                     from users
     *                                    where user_name = {userName})))
     *  order by u.user_name::
     */
    @JdbcControl.SQL(arrayMaxLength = 100000,
                     statement = "select u.user_id  as userId,  u.user_name  as userName,  u.first_name  as firstName,  u.middle_name  as middleName,  u.last_name  as lastName,  concat(concat(u.last_name, ', '),  concat(u.first_name, concat(' ', u.MIDDLE_NAME))) as displayUserName,  u.email  as email,  INITCAP(r.role_name)  as roleName,  r.role_id  as roleId,  u.ext_pin1  as extPin1,  u.ext_pin2  as extPin2,  u.address_id  as addressId,  node.org_node_id  as orgNodeId,  t.time_zone_desc  as timeZoneDese  from users u, user_role ur, role r, org_node node, time_zone_code t  where u.user_id = ur.user_id  and ur.org_node_id = node.org_node_id  and ur.role_id = r.role_id  and u.time_zone = t.time_zone  and node.activation_status = 'AC'  and u.activation_status = 'AC'  and ur.activation_status = 'AC'  and ur.org_node_id in  (select distinct ona.org_node_id  from org_node_ancestor ona  where ona.ancestor_org_node_id in  (select org_node_id  from user_role  where user_id = (select user_id  from users  where user_name = {userName})))  order by u.user_name")
    UserFileRow[] getUserData(String userName) throws SQLException;

    /**
     * @jc:sql array-max-length="all" statement::
     * select distinct
     *        stu.student_id                   as studentId,
     *        stu.user_Name                    as userName,
     *        stu.password                     as password,
     *        stu.first_Name                   as firstName,
     *        stu.middle_Name                  as middleName,
     *        stu.last_Name                    as lastName,
     *        concat(concat(stu.last_name, ', '),
     *               concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as displayStudentName,
     * 
     * 	   stu.preferred_Name               as preferredName,
     *        stu.prefix                       as prefix,
     *        stu.suffix                       as suffix,
     *        stu.birthdate                    as birthdate,
     *        stu.gender                       as gender,
     *        stu.ethnicity                    as ethnicity,
     *        stu.email                        as email,
     *        stu.grade                        as grade,
     *        stu.ext_Elm_Id                   as extElmId,
     *        stu.ext_Pin1                     as extPin1,
     *        stu.ext_Pin2                     as extPin2,
     *        stu.ext_Pin3                     as extPin3,
     *        stu.ext_School_Id                as extSchoolId,
     *        stu.active_Session               as activeSession,
     *        stu.potential_Duplicated_Student as potentialDuplicatedStudent,
     *        stu.created_By                   as createdBy,
     *        stu.created_Date_Time            as createdDateTime,
     *        stu.updated_By                   as updatedBy,
     *        stu.updated_Date_Time            as updatedDateTime,
     *        stu.activation_Status            as activationStatus,
     *        stu.data_import_history_id       as dataImportHistoryId,
     *        stu.grade                        as studentGrade,
     *        accom.SCREEN_MAGNIFIER           as screenMagnifier,
     *        accom.SCREEN_READER              as screenReader,
     *        accom.CALCULATOR                 as calculator,
     *        accom.TEST_PAUSE                 as testPause,
     *        accom.UNTIMED_TEST               as untimedTest,
     *        accom.HIGHLIGHTER                as highlighter,
     *        accom.QUESTION_BACKGROUND_COLOR  as questionBackgroundColor,
     *        accom.QUESTION_FONT_COLOR        as questionFontColor,
     *        accom.QUESTION_FONT_SIZE         as questionFontSize,
     *        accom.ANSWER_BACKGROUND_COLOR    as answerBackgroundColor,
     *        accom.ANSWER_FONT_COLOR          as answerFontColor,
     *        accom.ANSWER_FONT_SIZE           as answerFontSize,
     *        accom.highlighter                as highlighter,
     *        ons.org_node_id                  as orgNodeId,
     *        stucon.contact_name              as contactName,
     *        stucon.street_line1              as address1,
     *        stucon.street_line2              as address2,
     *        stucon.street_line3              as address3,
     *        stucon.city                      as city,
     *        st.statepr_desc                  as state,
     *        stucon.zipcode                   as zip,
     *        stucon.primary_phone             as primaryPhone,
     *        stucon.secondary_phone           as secondaryPhone,
     *        stucon.fax                       as faxNumber
     *        
     *        
     *   from org_node_student      ons,
     *        student               stu,
     *        org_node_ancestor     ona,
     *        student_accommodation accom,
     *        student_contact       stucon,
     *        statepr_code          st,
     *        country_code          cc
     *     
     *        
     *  where accom.student_id(+) = stu.student_id
     *    and stucon.student_id(+) = stu.student_id
     *    and stucon.statepr = st.statepr(+)
     *    and stucon.country = cc.country(+)
     *    and ons.student_id = stu.student_id
     *    and stu.activation_status = 'AC'
     *    and ons.activation_status = 'AC'
     *    and ons.org_node_id = ona.org_node_id
     *  
     *    and ona.ancestor_org_node_id in
     *        (select org_node_id
     *           from user_role
     *          where user_id =
     *                (select user_id from users where user_name = {userName}))::
     */
    @JdbcControl.SQL(arrayMaxLength = 100000,
                     statement = "select distinct  stu.student_id  as studentId,  stu.user_Name  as userName,  stu.password  as password,  stu.first_Name  as firstName,  stu.middle_Name  as middleName,  stu.last_Name  as lastName,  concat(concat(stu.last_name, ', '),  concat(stu.first_name, concat(' ', stu.MIDDLE_NAME))) as displayStudentName,  \t  stu.preferred_Name  as preferredName,  stu.prefix  as prefix,  stu.suffix  as suffix,  stu.birthdate  as birthdate,  stu.gender  as gender,  stu.ethnicity  as ethnicity,  stu.email  as email,  stu.grade  as grade,  stu.ext_Elm_Id  as extElmId,  stu.ext_Pin1  as extPin1,  stu.ext_Pin2  as extPin2,  stu.ext_Pin3  as extPin3,  stu.ext_School_Id  as extSchoolId,  stu.active_Session  as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By  as createdBy,  stu.created_Date_Time  as createdDateTime,  stu.updated_By  as updatedBy,  stu.updated_Date_Time  as updatedDateTime,  stu.activation_Status  as activationStatus,  stu.data_import_history_id  as dataImportHistoryId,  stu.grade  as studentGrade,  accom.SCREEN_MAGNIFIER  as screenMagnifier,  accom.SCREEN_READER  as screenReader,  accom.CALCULATOR  as calculator,  accom.TEST_PAUSE  as testPause,  accom.UNTIMED_TEST  as untimedTest,  accom.HIGHLIGHTER  as highlighter,  accom.QUESTION_BACKGROUND_COLOR  as questionBackgroundColor,  accom.QUESTION_FONT_COLOR  as questionFontColor,  accom.QUESTION_FONT_SIZE  as questionFontSize,  accom.ANSWER_BACKGROUND_COLOR  as answerBackgroundColor,  accom.ANSWER_FONT_COLOR  as answerFontColor,  accom.ANSWER_FONT_SIZE  as answerFontSize,  accom.highlighter  as highlighter,  ons.org_node_id  as orgNodeId,  stucon.contact_name  as contactName,  stucon.street_line1  as address1,  stucon.street_line2  as address2,  stucon.street_line3  as address3,  stucon.city  as city,  st.statepr_desc  as state,  stucon.zipcode  as zip,  stucon.primary_phone  as primaryPhone,  stucon.secondary_phone  as secondaryPhone,  stucon.fax  as faxNumber  from org_node_student  ons,  student  stu,  org_node_ancestor  ona,  student_accommodation accom,  student_contact  stucon,  statepr_code  st,  country_code  cc  where accom.student_id(+) = stu.student_id  and stucon.student_id(+) = stu.student_id  and stucon.statepr = st.statepr(+)  and stucon.country = cc.country(+)  and ons.student_id = stu.student_id  and stu.activation_status = 'AC'  and ons.activation_status = 'AC'  and ons.org_node_id = ona.org_node_id  and ona.ancestor_org_node_id in  (select org_node_id  from user_role  where user_id =  (select user_id from users where user_name = {userName}))")
    StudentFileRow[] getStudentData(String userName) throws SQLException;

    /**
     * @jc:sql statement::
     * select
     *     node.org_node_id as orgNodeId,
     *     node.customer_id as customerId,
     *     node.org_node_category_id as orgNodeCategoryId,
     *     node.org_node_name as orgNodeName,
     *     node.ext_qed_pin as extQedPin,
     *     node.ext_elm_id as extElmId,
     *     node.ext_org_node_type as extOrgNodeType,
     *     node.org_node_description as orgNodeDescription,
     *     node.created_by as createdBy,
     *     node.created_date_time as createdDateTime,
     *     node.updated_by as updatedBy,
     *     node.updated_date_time as updatedDateTime,
     *     node.activation_status as activationStatus,
     *     node.data_import_history_id as dataImportHistoryId,
     *     node.parent_state as parentState,
     *     node.parent_region as parentRegion,
     *     node.parent_county as parentCounty,
     *     node.parent_district as parentDistrict,
     *     node.org_node_code as orgNodeCode
    
    
     *   from org_node node 
    
     *  where  node.activation_status = 'AC'
     *      and node.org_node_id = (select min(orgnode.org_node_id) 
     *                              from org_node orgnode  
     *                              where orgnode.activation_status = 'AC'
     *                              and orgnode.customer_id={customerId})
     * ::
     */
    @JdbcControl.SQL(statement = "select  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode  from org_node node  where  node.activation_status = 'AC'  and node.org_node_id = (select min(orgnode.org_node_id)  from org_node orgnode  where orgnode.activation_status = 'AC'  and orgnode.customer_id={customerId})")
    Node getTopOrgNodeForCustomer(Integer customerId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     *      cusDemo.Customer_Demographic_Id as customerDemographicId,
     *      cusDemo.Customer_Id             as customerId,
     *      cusDemo.Label_Name              as labelName,
     *      cusDemo.Label_Code              as labelCode,
     *      cusDemo.Value_Cardinality       as valueCardinality,
     *      cusDemo.Sort_Order              as sortOrder,
     *      cusDemo.Import_Editable         as importEditable,
     *      cusDemo.Visible                 as visible,
     *      cusDemo.Created_Date_Time       as createdDateTime
     * 
     * from customer_demographic cusDemo 
     * where cusDemo.customer_id ={customerId}::
     */
    @JdbcControl.SQL(statement = "select  cusDemo.Customer_Demographic_Id as customerDemographicId,  cusDemo.Customer_Id  as customerId,  cusDemo.Label_Name  as labelName,  cusDemo.Label_Code  as labelCode,  cusDemo.Value_Cardinality  as valueCardinality,  cusDemo.Sort_Order  as sortOrder,  cusDemo.Import_Editable  as importEditable,  cusDemo.Visible  as visible,  cusDemo.Created_Date_Time  as createdDateTime  from customer_demographic cusDemo  where cusDemo.customer_id ={customerId}")
    StudentDemoGraphics[] getStudentDemoGraphics(Integer customerId) throws SQLException;

    /**
     * @jc:sql statement::
     *  select 
     * 	sdd.student_demographic_data_id   as studentDemographicId,
     *  sdd.student_id               as studentId,
     *  sdd.customer_demographic_id  as customerDemographicId,
     *  sdd.value_name               as valueName,
     *  sdd.value                    as value,
     *  sdd.created_by               as createdBy,
     *  sdd.created_date_time        as createdDateTime,
     *  sdd.updated_by               as updatedBy,
     *  sdd.updated_date_time        as updatedDateTime
     *   
     *  from 	student_demographic_data sdd
     *  where 	sdd.student_id ={studentId}::
     */
    @JdbcControl.SQL(statement = "select  \tsdd.student_demographic_data_id  as studentDemographicId, sdd.student_id  as studentId, sdd.customer_demographic_id  as customerDemographicId, sdd.value_name  as valueName, sdd.value  as value, sdd.created_by  as createdBy, sdd.created_date_time  as createdDateTime, sdd.updated_by  as updatedBy, sdd.updated_date_time  as updatedDateTime  from \tstudent_demographic_data sdd where \tsdd.student_id ={studentId}")
    StudentDemoGraphicsData[] getStudentDemoGraphicsData(Integer studentId) throws SQLException;
     
     
    /**
     * @jc:sql statement::
     * select
     *     node.org_node_id as orgNodeId,
     *     node.customer_id as customerId,
     *     node.org_node_category_id as orgNodeCategoryId,
     *     node.org_node_name as orgNodeName,
     *     node.activation_status as activationStatus,
     *     node.org_node_code as orgNodeCode
     * 
     *   from org_node node , org_node_category cat 
     * where
     * node.org_node_category_id =  cat.org_node_category_id
     * and node.activation_status = 'AC'
     * and cat.activation_status = 'AC'
     * and cat.customer_id = node.customer_id
     * and cat.customer_id = {customerId}
     * and cat.category_name = {categoryName}
     * and node.org_node_name={orgNodeName}::
     */
    @JdbcControl.SQL(statement = "select  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.activation_status as activationStatus,  node.org_node_code as orgNodeCode  from org_node node , org_node_category cat  where node.org_node_category_id =  cat.org_node_category_id and node.activation_status = 'AC' and cat.activation_status = 'AC' and cat.customer_id = node.customer_id and cat.customer_id = {customerId} and cat.category_name = {categoryName} and node.org_node_name={orgNodeName}")
    Node[] isOrgNameExists(String orgNodeName, String categoryName, Integer customerId) throws SQLException;

    /**
     * @jc:sql statement::
     * select orgNodeCat.org_node_category_id as orgNodeCategoryId,
     *    orgNodeCat.customer_id as customerId,
     *    orgNodeCat.is_group as isGroup,
     *    orgNodeCat.category_name as categoryName,
     *    orgNodeCat.category_level as categoryLevel,
     *    orgNodeCat.activation_status as activationStatus,    
     *    orgNodeCat.created_by as createdBy,
     *    orgNodeCat.created_date_time as createdDateTime,
     *    orgNodeCat.updated_by as updatedBy,
     *    orgNodeCat.updated_date_time as updatedDateTime
     *    from org_node_category orgNodeCat
     *    where orgNodeCat.customer_id = {customerId}
     *    and orgNodeCat.activation_status = 'AC'
     *    and orgNodeCat.category_name = {categoryName}
     *    and orgNodeCat.category_level > 0
     *    order by orgNodeCat.category_level::
     */
    @JdbcControl.SQL(statement = "select orgNodeCat.org_node_category_id as orgNodeCategoryId,  orgNodeCat.customer_id as customerId,  orgNodeCat.is_group as isGroup,  orgNodeCat.category_name as categoryName,  orgNodeCat.category_level as categoryLevel,  orgNodeCat.activation_status as activationStatus,  orgNodeCat.created_by as createdBy,  orgNodeCat.created_date_time as createdDateTime,  orgNodeCat.updated_by as updatedBy,  orgNodeCat.updated_date_time as updatedDateTime  from org_node_category orgNodeCat  where orgNodeCat.customer_id = {customerId}  and orgNodeCat.activation_status = 'AC'  and orgNodeCat.category_name = {categoryName}  and orgNodeCat.category_level > 0  order by orgNodeCat.category_level")
    com.ctb.bean.testAdmin.OrgNodeCategory getOrgNodeCategoryId(Integer customerId, String categoryName) throws SQLException;

    /**
     * @jc:sql statement::
     * select node.org_node_id as orgNodeId,
     *     node.customer_id as customerId,
     *     node.org_node_category_id as orgNodeCategoryId,
     *     node.org_node_name as orgNodeName,
     *     node.ext_qed_pin as extQedPin,
     *     node.ext_elm_id as extElmId,
     *     node.ext_org_node_type as extOrgNodeType,
     *     node.org_node_description as orgNodeDescription,
     *     node.created_by as createdBy,
     *     node.created_date_time as createdDateTime,
     *     node.updated_by as updatedBy,
     *     node.updated_date_time as updatedDateTime,
     *     node.activation_status as activationStatus,
     *     node.data_import_history_id as dataImportHistoryId,
     *     node.parent_state as parentState,
     *     node.parent_region as parentRegion,
     *     node.parent_county as parentCounty,
     *     node.parent_district as parentDistrict,
     *     node.org_node_code as orgNodeCode
     *    
     *  from org_node node
     * where 
     * node.org_node_code = {orgCode}
     * and node.activation_status = 'AC'
     * and node.customer_id = {customerId}
     * and node.org_node_name = {orgNodeName}::
     */
    @JdbcControl.SQL(statement = "select node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode  from org_node node where  node.org_node_code = {orgCode} and node.activation_status = 'AC' and node.customer_id = {customerId} and node.org_node_name = {orgNodeName}")
    Node[] isOrgNameWithCodeExists(String orgNodeName, String orgCode, Integer customerId) throws SQLException;

    /**
     * @jc:sql statement::
     * select distinct
     *     node.org_node_id as orgNodeId,
     *     node.customer_id as customerId,
     *     node.org_node_category_id as orgNodeCategoryId,
     *     node.org_node_name as orgNodeName,
     *     node.ext_qed_pin as extQedPin,
     *     node.ext_elm_id as extElmId,
     *     node.ext_org_node_type as extOrgNodeType,
     *     node.org_node_description as orgNodeDescription,
     *     node.created_by as createdBy,
     *     node.created_date_time as createdDateTime,
     *     node.updated_by as updatedBy,
     *     node.updated_date_time as updatedDateTime,
     *     node.activation_status as activationStatus,
     *     node.data_import_history_id as dataImportHistoryId,
     *     node.parent_state as parentState,
     *     node.parent_region as parentRegion,
     *     node.parent_county as parentCounty,
     *     node.parent_district as parentDistrict,
     *     node.org_node_code as orgNodeCode,
     *     cat.category_name as orgNodeCategoryName,
     *     count(distinct uor.user_id) as userCount,
     *     count(distinct descendants.org_node_id) - 1 as childNodeCount
     * from 
     *      org_node node, 
     *      org_node_category cat,
     *      org_node_ancestor descendants,
     *      org_node descendant_node,
     *      user_role uor,
     *      users,
     *      user_role tur,
     *      users tu
     * where 
     *      cat.org_node_category_id = node.org_node_category_id
     *      and node.org_node_id = tur.org_node_id
     *      and tur.activation_status = 'AC'
     *      and tur.user_id = tu.user_id
     *      and tu.user_name = {userName}
     *      and node.org_node_id = descendants.ancestor_org_node_id
     *      and descendants.org_node_id = descendant_node.org_node_id
     *      and descendant_node.activation_status = 'AC'
     *      and uor.org_node_id (+) = descendant_node.org_node_id
     *      and NVL(uor.activation_status, 'AC') = 'AC'
     *      and users.user_id (+) = uor.user_id
     *      and NVL(users.activation_status, 'AC') = 'AC'
     *      and node.customer_id = {customerId}
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name
     *      
     *      
     *      order by node.org_node_category_id::
     */
    @JdbcControl.SQL(statement = "select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName,  count(distinct uor.user_id) as userCount,  count(distinct descendants.org_node_id) - 1 as childNodeCount from  org_node node,  org_node_category cat,  org_node_ancestor descendants,  org_node descendant_node,  user_role uor,  users,  user_role tur,  users tu where  cat.org_node_category_id = node.org_node_category_id  and node.org_node_id = tur.org_node_id  and tur.activation_status = 'AC'  and tur.user_id = tu.user_id  and tu.user_name = {userName}  and node.org_node_id = descendants.ancestor_org_node_id  and descendants.org_node_id = descendant_node.org_node_id  and descendant_node.activation_status = 'AC'  and uor.org_node_id (+) = descendant_node.org_node_id  and NVL(uor.activation_status, 'AC') = 'AC'  and users.user_id (+) = uor.user_id  and NVL(users.activation_status, 'AC') = 'AC'  and node.customer_id = {customerId} group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name  order by node.org_node_category_id")
    UserNode[] getTopUserNodesForUser(String userName, Integer customerId) throws SQLException;

  
    /**
     * @jc:sql statement::
     * select 
     * SEQ_DATA_FILE_AUDIT_ID.NEXTVAL 
     * from 
     * dual::
     */
    @JdbcControl.SQL(statement = "select  SEQ_DATA_FILE_AUDIT_ID.NEXTVAL  from  dual")
    Integer getNextPK() throws SQLException;
    
    /**
     * @jc:sql statement::
     * insert into DATA_FILE_AUDIT 
     * (
     * DATA_FILE_AUDIT_ID,
     * USER_ID,
     * CUSTOMER_ID,
     * UPLOAD_FILE_NAME,   
     * CREATED_BY,
     * CREATED_DATE_TIME,
     * STATUS
     * )
     * 
     * values (
     * {dataFileAudit.dataFileAuditId},
     * {dataFileAudit.userId},
     * {dataFileAudit.customerId},
     * {dataFileAudit.uploadFileName},    
     * {dataFileAudit.createdBy},
     * sysdate,
     * {dataFileAudit.status}
     * )
     * ::
     */
    @JdbcControl.SQL(statement = "insert into DATA_FILE_AUDIT  ( DATA_FILE_AUDIT_ID, USER_ID, CUSTOMER_ID, UPLOAD_FILE_NAME,  CREATED_BY, CREATED_DATE_TIME, STATUS )  values ( {dataFileAudit.dataFileAuditId}, {dataFileAudit.userId}, {dataFileAudit.customerId}, {dataFileAudit.uploadFileName},  {dataFileAudit.createdBy}, sysdate, {dataFileAudit.status} )")
    void createDataFileAudit(DataFileAudit dataFileAudit);


    /**
     * @jc:sql statement::
     * select distinct
     *     node.org_node_id as orgNodeId,
     *     node.customer_id as customerId,
     *     node.org_node_category_id as orgNodeCategoryId,
     *     node.org_node_name as orgNodeName,
     *     node.ext_qed_pin as extQedPin,
     *     node.ext_elm_id as extElmId,
     *     node.ext_org_node_type as extOrgNodeType,
     *     node.org_node_description as orgNodeDescription,
     *     node.created_by as createdBy,
     *     node.created_date_time as createdDateTime,
     *     node.updated_by as updatedBy,
     *     node.updated_date_time as updatedDateTime,
     *     node.activation_status as activationStatus,
     *     node.data_import_history_id as dataImportHistoryId,
     *     node.parent_state as parentState,
     *     node.parent_region as parentRegion,
     *     node.parent_county as parentCounty,
     *     node.parent_district as parentDistrict,
     *     node.org_node_code as orgNodeCode,
     *     cat.category_name as orgNodeCategoryName
     *    
     * from 
     *      org_node node, 
     *      org_node_category cat,
     *      user_role uor,
     *      users,
     *      user_role tur,
     *      users tu
     * where 
     *      cat.org_node_category_id = node.org_node_category_id
     *      and node.org_node_id = tur.org_node_id
     *      and tur.activation_status = 'AC'
     *      and tur.user_id = tu.user_id
     *      and tu.user_id= {userId}
     *      and tur.role_id={roleId}
     *      and uor.activation_status = 'AC'
     *      and users.user_id  = uor.user_id
     *      and users.activation_status = 'AC'
     *      and node.customer_id = {customerId}
     * group by
     *      node.org_node_id,
     *      node.customer_id,
     *      node.org_node_category_id,
     *      node.org_node_name,
     *      node.ext_qed_pin,
     *      node.ext_elm_id,
     *      node.ext_org_node_type,
     *      node.org_node_description,
     *      node.created_by,
     *      node.created_date_time,
     *      node.updated_by,
     *      node.updated_date_time,
     *      node.activation_status,
     *      node.data_import_history_id,
     *      node.parent_state,
     *      node.parent_region,
     *      node.parent_county,
     *      node.parent_district,
     *      node.org_node_code,
     *      cat.category_name
     *      
     *      
     *      order by node.org_node_category_id::
     */
    @JdbcControl.SQL(statement = "select distinct  node.org_node_id as orgNodeId,  node.customer_id as customerId,  node.org_node_category_id as orgNodeCategoryId,  node.org_node_name as orgNodeName,  node.ext_qed_pin as extQedPin,  node.ext_elm_id as extElmId,  node.ext_org_node_type as extOrgNodeType,  node.org_node_description as orgNodeDescription,  node.created_by as createdBy,  node.created_date_time as createdDateTime,  node.updated_by as updatedBy,  node.updated_date_time as updatedDateTime,  node.activation_status as activationStatus,  node.data_import_history_id as dataImportHistoryId,  node.parent_state as parentState,  node.parent_region as parentRegion,  node.parent_county as parentCounty,  node.parent_district as parentDistrict,  node.org_node_code as orgNodeCode,  cat.category_name as orgNodeCategoryName  from  org_node node,  org_node_category cat,  user_role uor,  users,  user_role tur,  users tu where  cat.org_node_category_id = node.org_node_category_id  and node.org_node_id = tur.org_node_id  and tur.activation_status = 'AC'  and tur.user_id = tu.user_id  and tu.user_id= {userId}  and tur.role_id={roleId}  and uor.activation_status = 'AC'  and users.user_id  = uor.user_id  and users.activation_status = 'AC'  and node.customer_id = {customerId} group by  node.org_node_id,  node.customer_id,  node.org_node_category_id,  node.org_node_name,  node.ext_qed_pin,  node.ext_elm_id,  node.ext_org_node_type,  node.org_node_description,  node.created_by,  node.created_date_time,  node.updated_by,  node.updated_date_time,  node.activation_status,  node.data_import_history_id,  node.parent_state,  node.parent_region,  node.parent_county,  node.parent_district,  node.org_node_code,  cat.category_name  order by node.org_node_category_id")
    Node[] getNodeIdFromUserRole(Integer roleId, Integer userId, Integer customerId) throws SQLException;


    /**
     * @jc:sql statement::
     * update  DATA_FILE_AUDIT 
     * set error_file = {dataFileAudit.faildRec},
     * FAILED_RECORD_COUNT =  {dataFileAudit.failedRecordCount},
     * UPLOADED_RECORD_COUNT = {dataFileAudit.uploadFileRecordCount},
     * STATUS = {dataFileAudit.status}
     * 
     * where DATA_FILE_AUDIT_ID = {dataFileAudit.dataFileAuditId}::
     */
    @JdbcControl.SQL(statement = "update  DATA_FILE_AUDIT  set error_file = {dataFileAudit.faildRec}, FAILED_RECORD_COUNT =  {dataFileAudit.failedRecordCount}, UPLOADED_RECORD_COUNT = {dataFileAudit.uploadFileRecordCount}, STATUS = {dataFileAudit.status}  where DATA_FILE_AUDIT_ID = {dataFileAudit.dataFileAuditId}")
    public void upDateAuditTable(DataFileAudit dataFileAudit) throws SQLException;

    /**
     * @jc:sql statement::
     * update  DATA_FILE_AUDIT 
     * set STATUS = {dataFileAudit.status}
     * 
     * where DATA_FILE_AUDIT_ID = {dataFileAudit.dataFileAuditId}::
     */
    @JdbcControl.SQL(statement = "update  DATA_FILE_AUDIT  set STATUS = {dataFileAudit.status}  where DATA_FILE_AUDIT_ID = {dataFileAudit.dataFileAuditId}")
    public void updateAuditFileStatus(DataFileAudit dataFileAudit) throws SQLException;


    /**
     * @jc:sql statement::
     * SELECT role.role_id as roleId,
     * INITCAP(role.role_name) as roleName,
     * role.activation_status as activationStatus,
     * role.role_type_id as roleTypeId
     * FROM Role role
     * WHERE role.activation_status = 'AC'
    
     * order by role.role_name::
     */
    @JdbcControl.SQL(statement = "SELECT role.role_id as roleId, INITCAP(role.role_name) as roleName, role.activation_status as activationStatus, role.role_type_id as roleTypeId FROM Role role WHERE role.activation_status = 'AC'  order by role.role_name")
    Role[] getRole() throws SQLException;
    
    
    /**
     * @jc:sql statement::
     * delete from data_file_audit where 
     * data_file_audit_id = {auditId}::
    */
    @JdbcControl.SQL(statement = "delete from data_file_audit where  data_file_audit_id = {auditId}")
    void deleteErrorDataFile(Integer auditId) throws SQLException;
    
    /**
     * @jc:sql statement::
     *        select dfa.DATA_FILE_AUDIT_ID    as dataFileAuditId,
     *               dfa.user_id               as userId,
     *               dfa.customer_id           as customerId,
     *               dfa.upload_file_name      as uploadFileName,
     *               dfa.uploaded_record_count as uploadFileRecordCount,
     *               dfa.failed_record_count   as failedRecordCount,
     *               dfa.status                as status,
     *               dfa.created_by            as createdBy,
     *               dfa.created_date_time     as createdDateTime
     *          from DATA_FILE_AUDIT dfa, users usr
     *         where usr.user_id = dfa.user_id
     *         and usr.user_name = {userName}::
     */
    @JdbcControl.SQL(statement = "select dfa.DATA_FILE_AUDIT_ID  as dataFileAuditId,  dfa.user_id  as userId,  dfa.customer_id  as customerId,  dfa.upload_file_name  as uploadFileName,  dfa.uploaded_record_count as uploadFileRecordCount,  dfa.failed_record_count  as failedRecordCount,  dfa.status  as status,  dfa.created_by  as createdBy,  dfa.created_date_time  as createdDateTime  from DATA_FILE_AUDIT dfa, users usr  where usr.user_id = dfa.user_id  and usr.user_name = {userName}")
    DataFileAudit[] getUploadHistory(String userName) throws SQLException;


    /**
     * @jc:sql statement::
     *        select dfa.DATA_FILE_AUDIT_ID    as dataFileAuditId,
     *               dfa.user_id               as userId,
     *               dfa.customer_id           as customerId,
     *               dfa.upload_file_name      as uploadFileName,
     *               dfa.uploaded_record_count as uploadFileRecordCount,
     *               dfa.failed_record_count   as failedRecordCount,
     *               dfa.status                as status,
     *               dfa.created_by            as createdBy,
     *               dfa.created_date_time     as createdDateTime
     *          from DATA_FILE_AUDIT dfa
     *         where dfa.data_file_audit_id = {uploadDataFileId}::
     */
    @JdbcControl.SQL(statement = "select dfa.DATA_FILE_AUDIT_ID  as dataFileAuditId,  dfa.user_id  as userId,  dfa.customer_id  as customerId,  dfa.upload_file_name  as uploadFileName,  dfa.uploaded_record_count as uploadFileRecordCount,  dfa.failed_record_count  as failedRecordCount,  dfa.status  as status,  dfa.created_by  as createdBy,  dfa.created_date_time  as createdDateTime  from DATA_FILE_AUDIT dfa  where dfa.data_file_audit_id = {uploadDataFileId}")
    DataFileAudit getUploadFile(Integer uploadDataFileId) throws SQLException;


    /**
     * @jc:sql statement::
     * select ERROR_FILE from DATA_FILE_AUDIT 
     * where data_file_audit_id ={auditFileId}::
   */
    @JdbcControl.SQL(statement = "select ERROR_FILE from DATA_FILE_AUDIT  where data_file_audit_id ={auditFileId}")
    Blob getErrorDataFile(Integer auditFileId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * delete from student_contact stcontact
     * 	where stcontact.student_id = {studentId}::
     */
    @JdbcControl.SQL(statement = "delete from student_contact stcontact \twhere stcontact.student_id = {studentId}")
    public void deleteStudentContact(Integer studentId) throws SQLException;

    /**
     * @jc:sql statement::
     * select 
     * SEQ_STUDENT_CONTACT_ID.NEXTVAL 
     * from 
     * dual::
     */
    @JdbcControl.SQL(statement = "select  SEQ_STUDENT_CONTACT_ID.NEXTVAL  from  dual")
    public void getNextContact();
    
    /**
     * @jc:sql statement::
     * select 
     *      stu.student_id as studentId,
     *      stu.user_Name as userName,
     *      stu.password as password,
     *      stu.first_Name as firstName,
     *      stu.middle_Name as middleName,
     *      stu.last_Name as lastName,
     *      stu.preferred_Name as preferredName,
     *      stu.prefix as prefix,
     *      stu.suffix as suffix,
     *      stu.birthdate as birthdate,
     *      stu.gender as gender,
     *      stu.ethnicity as ethnicity,
     *      stu.email as email,
     *      stu.grade as grade,
     *      stu.ext_Elm_Id as extElmId,
     *      stu.ext_Pin1 as extPin1,
     *      stu.ext_Pin2 as extPin2,
     *      stu.ext_Pin3 as extPin3,
     *      stu.ext_School_Id as extSchoolId,
     *      stu.active_Session as activeSession,
     *      stu.potential_Duplicated_Student as potentialDuplicatedStudent,
     *      stu.created_By as createdBy,
     *      stu.created_Date_Time as createdDateTime,
     *      stu.updated_By as updatedBy,
     *      stu.updated_Date_Time as updatedDateTime,
     *      stu.activation_Status as activationStatus,
     *      stu.data_import_history_id as dataImportHistoryId
     * from
     *      org_node_student ons,
     *      student stu
     * where
     *      ons.student_id = stu.student_id
     * 	 and stu.student_id = {studentId}
     *      and ons.org_node_id = {orgNodeId}
     *      and ons.activation_status = 'AC'
     *      and stu.activation_status = 'AC'::
     */
    @JdbcControl.SQL(statement = "select  stu.student_id as studentId,  stu.user_Name as userName,  stu.password as password,  stu.first_Name as firstName,  stu.middle_Name as middleName,  stu.last_Name as lastName,  stu.preferred_Name as preferredName,  stu.prefix as prefix,  stu.suffix as suffix,  stu.birthdate as birthdate,  stu.gender as gender,  stu.ethnicity as ethnicity,  stu.email as email,  stu.grade as grade,  stu.ext_Elm_Id as extElmId,  stu.ext_Pin1 as extPin1,  stu.ext_Pin2 as extPin2,  stu.ext_Pin3 as extPin3,  stu.ext_School_Id as extSchoolId,  stu.active_Session as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By as createdBy,  stu.created_Date_Time as createdDateTime,  stu.updated_By as updatedBy,  stu.updated_Date_Time as updatedDateTime,  stu.activation_Status as activationStatus,  stu.data_import_history_id as dataImportHistoryId from  org_node_student ons,  student stu where  ons.student_id = stu.student_id \t and stu.student_id = {studentId}  and ons.org_node_id = {orgNodeId}  and ons.activation_status = 'AC'  and stu.activation_status = 'AC'")
    public Student getStudentID(Integer orgNodeId, Integer studentId) throws SQLException;


        
    /**
 * @jc:sql statement::
 * select    cc.customer_configuration_id   as customerConfigurationId,
 *           cc.customer_configuration_name as customerConfigurationName,
 *           cc.editable                    as editable,
 *           cc.default_value               as defaultValue
 *           
 *      from customer_configuration cc
 *     where cc.customer_id = {customerId}
 *     and cc.customer_configuration_name in 
 *     ('Highlighter',
 *      'Session_Details_Show_Scores',
 *      'Calculator', 
 *      'Test_Pause', 
 *      'Screen_Reader', 
 *      'Untimed_Test')::
 */
    @JdbcControl.SQL(statement = "select  cc.customer_configuration_id  as customerConfigurationId,  cc.customer_configuration_name as customerConfigurationName,  cc.editable  as editable,  cc.default_value  as defaultValue  from customer_configuration cc  where cc.customer_id = {customerId}  and cc.customer_configuration_name in  ('Highlighter',  'Session_Details_Show_Scores',  'Calculator',  'Test_Pause',  'Screen_Reader',  'Untimed_Test')")
    com.ctb.bean.testAdmin.CustomerConfig[] getCustomerConfig(Integer customerId) throws SQLException;

    /**
     * @jc:sql statement::
     * SELECT
     *       cdv.customer_demographic_id as customerDemographicId,
     *       cdv.value_name              as valueName,
     *       cdv.value_code              as valueCode,
     *       cdv.sort_order              as sortOrder,
     *       cdv.visible                 as visible,
     *       cdv.created_by              as createdBy,
     *       cdv.created_date_time       as createdDateTime
    
     * FROM customer_demographic_value cdv
    
     * WHERE cdv.customer_demographic_id={customerDemographicId} ::
     */
    @JdbcControl.SQL(statement = "SELECT  cdv.customer_demographic_id as customerDemographicId,  cdv.value_name  as valueName,  cdv.value_code  as valueCode,  cdv.sort_order  as sortOrder,  cdv.visible  as visible,  cdv.created_by  as createdBy,  cdv.created_date_time  as createdDateTime  FROM customer_demographic_value cdv  WHERE cdv.customer_demographic_id={customerDemographicId} ")
    CustomerDemographicValue[] getCustomerDemographicValue(Integer customerDemographicId) throws SQLException;
    
    
    /**
     * @jc:sql statement::
     * select    cc.customer_configuration_id   as customerConfigurationId,
     *           cc.customer_configuration_name as customerConfigurationName,
     *           cc.editable                    as editable,
     *           cc.default_value               as defaultValue,
     *           cc.created_by                  as createdBy,
     *           cc.created_date_time           as createdDateTime
     *      from customer_configuration cc
     *     where cc.customer_id = {customerId}
     *    ::
     */
    @JdbcControl.SQL(statement = "select  cc.customer_configuration_id  as customerConfigurationId,  cc.customer_configuration_name as customerConfigurationName,  cc.editable  as editable,  cc.default_value  as defaultValue,  cc.created_by  as createdBy,  cc.created_date_time  as createdDateTime  from customer_configuration cc  where cc.customer_id = {customerId}")
    com.ctb.bean.testAdmin.CustomerConfig[] getCustomerConfigurationEntries(Integer customerId) throws SQLException;
    //Changes for GA2011CR003 OAS Student Bulk Upload using Unique Student ID
    /**
     * @jc:sql statement::
        select    
     *          decode( count(cc.customer_id),0,0,1) 
     *                                                                                          
     *      from customer_configuration cc
     *     where cc.customer_id = {customerId}
     *     and cc.customer_configuration_name = {customerConfigurationName}::
     *     and cc.default_value='T'
     */
    @JdbcControl.SQL(statement = "select  decode( count(cc.customer_id),0,0,1)  from customer_configuration cc  where cc.customer_id = {customerId}  and cc.customer_configuration_name = {customerConfigurationName} and cc.default_value='T'")
    boolean checkCustomerConfigurationEntries(Integer customerId, String customerConfigurationName) throws SQLException;


    /**
     * @jc:sql statement::
     * select 
     *      stu.student_id as studentId,
     *      stu.user_Name as userName,
     *      stu.password as password,
     *      stu.first_Name as firstName,
     *      stu.middle_Name as middleName,
     *      stu.last_Name as lastName,
     *      stu.preferred_Name as preferredName,
     *      stu.prefix as prefix,
     *      stu.suffix as suffix,
     *      stu.birthdate as birthdate,
     *      stu.gender as gender,
     *      stu.ethnicity as ethnicity,
     *      stu.email as email,
     *      stu.grade as grade,
     *      stu.ext_Elm_Id as extElmId,
     *      stu.ext_Pin1 as extPin1,
     *      stu.ext_Pin2 as extPin2,
     *      stu.ext_Pin3 as extPin3,
     *      stu.ext_School_Id as extSchoolId,
     *      stu.active_Session as activeSession,
     *      stu.potential_Duplicated_Student as potentialDuplicatedStudent,
     *      stu.created_By as createdBy,
     *      stu.created_Date_Time as createdDateTime,
     *      stu.updated_By as updatedBy,
     *      stu.updated_Date_Time as updatedDateTime,
     *      stu.activation_Status as activationStatus,
     *      stu.data_import_history_id as dataImportHistoryId
     * from
     *      org_node_student ons,
     *      student stu
     * where
     *      
     *       
     *      ons.student_id = stu.student_id
     *      and ons.org_node_id = {orgNodeId}
     *      and stu.ext_Pin1 = {studentId}
     *      and ons.activation_status = 'AC'
     *      and stu.activation_status = 'AC'
     *      ::
     */
    @JdbcControl.SQL(statement = "select  stu.student_id as studentId,  stu.user_Name as userName,  stu.password as password,  stu.first_Name as firstName,  stu.middle_Name as middleName,  stu.last_Name as lastName,  stu.preferred_Name as preferredName,  stu.prefix as prefix,  stu.suffix as suffix,  stu.birthdate as birthdate,  stu.gender as gender,  stu.ethnicity as ethnicity,  stu.email as email,  stu.grade as grade,  stu.ext_Elm_Id as extElmId,  stu.ext_Pin1 as extPin1,  stu.ext_Pin2 as extPin2,  stu.ext_Pin3 as extPin3,  stu.ext_School_Id as extSchoolId,  stu.active_Session as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By as createdBy,  stu.created_Date_Time as createdDateTime,  stu.updated_By as updatedBy,  stu.updated_Date_Time as updatedDateTime,  stu.activation_Status as activationStatus,  stu.data_import_history_id as dataImportHistoryId from  org_node_student ons,  student stu where  ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and stu.ext_Pin1 = {studentId}  and ons.activation_status = 'AC'  and stu.activation_status = 'AC'")
    Student[] getStudentFromExtPin1(Integer orgNodeId, String studentId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select 
     *      stu.student_id as studentId,
     *      stu.user_Name as userName,
     *      stu.password as password,
     *      stu.first_Name as firstName,
     *      stu.middle_Name as middleName,
     *      stu.last_Name as lastName,
     *      stu.preferred_Name as preferredName,
     *      stu.prefix as prefix,
     *      stu.suffix as suffix,
     *      stu.birthdate as birthdate,
     *      stu.gender as gender,
     *      stu.ethnicity as ethnicity,
     *      stu.email as email,
     *      stu.grade as grade,
     *      stu.ext_Elm_Id as extElmId,
     *      stu.ext_Pin1 as extPin1,
     *      stu.ext_Pin2 as extPin2,
     *      stu.ext_Pin3 as extPin3,
     *      stu.ext_School_Id as extSchoolId,
     *      stu.active_Session as activeSession,
     *      stu.potential_Duplicated_Student as potentialDuplicatedStudent,
     *      stu.created_By as createdBy,
     *      stu.created_Date_Time as createdDateTime,
     *      stu.updated_By as updatedBy,
     *      stu.updated_Date_Time as updatedDateTime,
     *      stu.activation_Status as activationStatus,
     *      stu.data_import_history_id as dataImportHistoryId
     * from
	 *      org_node_student ons,
     *      student stu
     * where
     *      ons.student_id = stu.student_id
     *      and ons.org_node_id = {orgNodeId}
     *      and ons.activation_status = 'AC'
     *      and stu.activation_status = 'AC'::
     * array-max-length="all"
     */
    @JdbcControl.SQL(statement = "select  stu.student_id as studentId,  stu.user_Name as userName,  stu.password as password,  stu.first_Name as firstName,  stu.middle_Name as middleName,  stu.last_Name as lastName,  stu.preferred_Name as preferredName,  stu.prefix as prefix,  stu.suffix as suffix,  stu.birthdate as birthdate,  stu.gender as gender,  stu.ethnicity as ethnicity,  stu.email as email,  stu.grade as grade,  stu.ext_Elm_Id as extElmId,  stu.ext_Pin1 as extPin1,  stu.ext_Pin2 as extPin2,  stu.ext_Pin3 as extPin3,  stu.ext_School_Id as extSchoolId,  stu.active_Session as activeSession,  stu.potential_Duplicated_Student as potentialDuplicatedStudent,  stu.created_By as createdBy,  stu.created_Date_Time as createdDateTime,  stu.updated_By as updatedBy,  stu.updated_Date_Time as updatedDateTime,  stu.activation_Status as activationStatus,  stu.data_import_history_id as dataImportHistoryId from  org_node_student ons,  student stu where  ons.student_id = stu.student_id  and ons.org_node_id = {orgNodeId}  and ons.activation_status = 'AC'  and stu.activation_status = 'AC'",
                     arrayMaxLength = 100000)
    Student [] getStudentsForLeafOrgNode(Integer orgNodeId) throws SQLException;
    

    /**
     * @jc:sql statement::
     * select    cc.customer_configuration_id   as customerConfigurationId,
     *           cc.customer_configuration_name as customerConfigurationName,
     *           cc.editable                    as editable,
     *           cc.default_value               as defaultValue,
     *           cc.created_by                  as createdBy,
     *           cc.created_date_time           as createdDateTime
     *      from customer_configuration cc
     *      where cc.customer_configuration_name in ('Screen_Reader'
     *     ,'Calculator','Test_Pause'
     *     ,'Untimed_Test','Highlighter') 
     *     and cc.customer_id = {customerId}::
     */
    @JdbcControl.SQL(statement = "select  cc.customer_configuration_id  as customerConfigurationId,  cc.customer_configuration_name as customerConfigurationName,  cc.editable  as editable,  cc.default_value  as defaultValue,  cc.created_by  as createdBy,  cc.created_date_time  as createdDateTime  from customer_configuration cc  where cc.customer_configuration_name in ('Screen_Reader'  ,'Calculator','Test_Pause'  ,'Untimed_Test','Highlighter')  and cc.customer_id = {customerId}")
    com.ctb.bean.testAdmin.CustomerConfig[] getCustomerConfigurationForAccommodation(Integer customerId) throws SQLException;

    /**
     * @jc:sql statement::
     *    
     *    select distinct
     *        stu.student_id                   as studentId,
     *        stu.user_Name                    as userName,
     *        stu.first_Name                   as firstName,
     *        stu.middle_Name                  as middleName,
     *        stu.last_Name                    as lastName,
     *        stu.birthdate                    as birthdate,
     *        stu.gender                       as gender,
     *        stu.grade                        as grade,
     *        stu.ext_Elm_Id                   as extElmId,
     *        stu.ext_Pin1                     as extPin1,
     *        stu.ext_Pin2                     as extPin2
     *        
     *   from org_node_student      ons,
     *        student               stu,
     *        org_node_ancestor     ona
     *        
     *  where ons.student_id = stu.student_id
     *    and stu.activation_status = 'AC'
     *    and ons.activation_status = 'AC'
     *    and ons.org_node_id = ona.org_node_id
     *  
     *    and ona.ancestor_org_node_id in
     *        (select org_node_id
     *           from user_role
     *          where user_id =
     *                (select user_id from users where user_name = {userName}))::
     * array-max-length="all"               
     */
    @JdbcControl.SQL(statement = " select distinct  stu.student_id  as studentId,  stu.user_Name  as userName,  stu.first_Name  as firstName,  stu.middle_Name  as middleName,  stu.last_Name  as lastName,  stu.birthdate  as birthdate,  stu.gender  as gender,  stu.grade  as grade,  stu.ext_Elm_Id  as extElmId,  stu.ext_Pin1  as extPin1,  stu.ext_Pin2  as extPin2  from org_node_student  ons,  student  stu,  org_node_ancestor  ona  where ons.student_id = stu.student_id and stu.activation_status = 'AC' and ons.activation_status = 'AC' and ons.org_node_id = ona.org_node_id  and ona.ancestor_org_node_id in  (select org_node_id  from user_role  where user_id =  (select user_id from users where user_name = {userName}))",
                     arrayMaxLength = 100000)
    StudentFileRow[] getExistStudentData(String userName) throws SQLException;
    
    /**
     * @jc:sql statement::
     * insert into DATA_FILE_TEMP 
     * (
     * DATA_FILE_AUDIT_ID,
     * DATA_FILE
     * )
     * 
     * values (
     * {dataFileTemp.dataFileAuditId},
     * {dataFileTemp.dataFile}
     * )
     * ::
     */
    @JdbcControl.SQL(statement = "insert into DATA_FILE_TEMP  ( DATA_FILE_AUDIT_ID, DATA_FILE )  values ( {dataFileTemp.dataFileAuditId}, {dataFileTemp.dataFile} )")
    void createDataFileTemp(DataFileTemp dataFileTemp) throws SQLException;
    
    /**
     * @jc:sql statement::
     *    
     * select DT.DATA_FILE
     * from DATA_FILE_TEMP DT
     * where DT.DATA_FILE_AUDIT_ID = {dataFileAuditId}
     * ::               
     */
	@JdbcControl.SQL(statement = "  select DT.DATA_FILE  from DATA_FILE_TEMP DT  where DT.DATA_FILE_AUDIT_ID = {dataFileAuditId}")
    Blob getDataFileTemp(Integer dataFileAuditId) throws SQLException;
	
	/**
     * @jc:sql statement::
     * select cc.default_value
     * from customer_configuration cc
     * where cc.customer_id = {customerId}
     * and cc.customer_configuration_name = {customerConfigurationName}::
     *
     */
	@JdbcControl.SQL(statement = "select cc.default_value from customer_configuration cc  where cc.customer_id = {customerId}  and cc.customer_configuration_name = {customerConfigurationName}")
	String checkCustomerConfiguration(Integer customerId,String customerConfigurationName) throws SQLException;
    

}