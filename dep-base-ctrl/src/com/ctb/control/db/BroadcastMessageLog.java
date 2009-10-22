package com.ctb.control.db; 
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import org.apache.beehive.controls.api.bean.ControlExtension;

import com.ctb.bean.testAdmin.BroadcastMessage; 
import java.sql.SQLException; 


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
public interface BroadcastMessageLog extends JdbcControl
{ 
    static final long serialVersionUID = 1L;
    
    /**
     * @jc:sql statement::
     * select
     *     message.DISPLAY_DATE_TIME as displayDateTime,
     *     message.CREATED_DATE_TIME as createdDateTime,
     *     message.MESSAGE as message,
     *     message.PRIORITY_VALUE as priorityValue,
     *     message.CREATED_BY as createdBy,
     *     message.START_DATE as startDate,
     *     message.END_DATE as endDate,
     *     message.ACTIVATION_STATUS as activationStatus,
     *     message.BROADCAST_MESSAGE_LOG_ID as broadcastMessageLogId,
     *     message.APPLICATION as application,
     *     message.MESSAGE_TYPE as messageType
     * from 
     *      broadcast_message_log message
     * where 
     *      message.application in ('OAS', 'ALL')
     *      and message.activation_status = 'AC'
     *      and sysdate > message.start_date and sysdate < message.end_date
     *      order by priorityValue, displayDateTime desc::
     *      array-max-length="100000"
     */
    @JdbcControl.SQL(statement = "select  message.DISPLAY_DATE_TIME as displayDateTime,  message.CREATED_DATE_TIME as createdDateTime,  message.MESSAGE as message,  message.PRIORITY_VALUE as priorityValue,  message.CREATED_BY as createdBy,  message.START_DATE as startDate,  message.END_DATE as endDate,  message.ACTIVATION_STATUS as activationStatus,  message.BROADCAST_MESSAGE_LOG_ID as broadcastMessageLogId,  message.APPLICATION as application,  message.MESSAGE_TYPE as messageType from  broadcast_message_log message where  message.application in ('OAS', 'ALL')  and message.activation_status = 'AC'  and sysdate > message.start_date and sysdate < message.end_date  order by priorityValue, displayDateTime desc",
                     arrayMaxLength = 100000)
    BroadcastMessage [] getBroadcastMessages() throws SQLException;
    
    /**
     * @jc:sql statement::
     * (select
     *      message.DISPLAY_DATE_TIME as displayDateTime,
     *      message.CREATED_DATE_TIME as createdDateTime,
     *      message.MESSAGE as message,
     *      message.PRIORITY_VALUE as priorityValue,
     *      message.CREATED_BY as createdBy,
     *      message.START_DATE as startDate,
     *      message.END_DATE as endDate,
     *      message.ACTIVATION_STATUS as activationStatus,
     *      message.BROADCAST_MESSAGE_LOG_ID as broadcastMessageLogId,
     *      message.APPLICATION as application,
     *      message.MESSAGE_TYPE as messageType
     *  from 
     *       broadcast_message_log message
     *  where 
     *       message.application in ('OAS', 'ALL')
     *       and message.activation_status = 'AC'
     *       and sysdate > message.start_date and sysdate < message.end_date
     *       and message.product_id is null)
     *       
     * union         
     *       (select
     *      message.DISPLAY_DATE_TIME as displayDateTime,
     *      message.CREATED_DATE_TIME as createdDateTime,
     *      message.MESSAGE as message,
     *      message.PRIORITY_VALUE as priorityValue,
     *      message.CREATED_BY as createdBy,
     *      message.START_DATE as startDate,
     *      message.END_DATE as endDate,
     *      message.ACTIVATION_STATUS as activationStatus,
     *      message.BROADCAST_MESSAGE_LOG_ID as broadcastMessageLogId,
     *      message.APPLICATION as application,
     *      message.MESSAGE_TYPE as messageType
     *  from 
     *       broadcast_message_log message
     *  where 
     *       message.application in ('OAS', 'ALL')
     *       and message.activation_status = 'AC'
     *       and sysdate > message.start_date and sysdate < message.end_date
     *       and {sql:fn in(message.product_id,{productId})})
     *  order by priorityValue, displayDateTime desc::
     *      array-max-length="all"
     */
    @JdbcControl.SQL(statement = "(select  message.DISPLAY_DATE_TIME as displayDateTime,  message.CREATED_DATE_TIME as createdDateTime,  message.MESSAGE as message,  message.PRIORITY_VALUE as priorityValue,  message.CREATED_BY as createdBy,  message.START_DATE as startDate,  message.END_DATE as endDate,  message.ACTIVATION_STATUS as activationStatus,  message.BROADCAST_MESSAGE_LOG_ID as broadcastMessageLogId,  message.APPLICATION as application,  message.MESSAGE_TYPE as messageType  from  broadcast_message_log message  where  message.application in ('OAS', 'ALL')  and message.activation_status = 'AC'  and sysdate > message.start_date and sysdate < message.end_date  and message.product_id is null)  union  (select  message.DISPLAY_DATE_TIME as displayDateTime,  message.CREATED_DATE_TIME as createdDateTime,  message.MESSAGE as message,  message.PRIORITY_VALUE as priorityValue,  message.CREATED_BY as createdBy,  message.START_DATE as startDate,  message.END_DATE as endDate,  message.ACTIVATION_STATUS as activationStatus,  message.BROADCAST_MESSAGE_LOG_ID as broadcastMessageLogId,  message.APPLICATION as application,  message.MESSAGE_TYPE as messageType  from  broadcast_message_log message  where  message.application in ('OAS', 'ALL')  and message.activation_status = 'AC'  and sysdate > message.start_date and sysdate < message.end_date  and {sql:fn in(message.product_id,{productId})})  order by priorityValue, displayDateTime desc",
                     arrayMaxLength = 100000)
    BroadcastMessage [] getProductSpecificBroadcastMsg(Integer[] productId) throws SQLException;
    
    /**
     * @jc:sql statement::
     * select distinct prod.parent_product_id 
     * from users usr, user_role url, org_node_test_catalog ontc, product prod
     * where usr.user_name = {userName}
     * and usr.user_id = url.user_id
     * and url.org_node_id = ontc.org_node_id
     * and ontc.product_id = prod.product_id::
     * 
     */
    @JdbcControl.SQL(statement = "select distinct prod.parent_product_id  from users usr, user_role url, org_node_test_catalog ontc, product prod where usr.user_name = {userName} and usr.user_id = url.user_id and url.org_node_id = ontc.org_node_id and ontc.product_id = prod.product_id")
    Integer [] getFrameworkProductForUser(String userName) throws SQLException;

}