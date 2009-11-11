package com.ctb.lexington.util;

public class SQLStatements
{
    public static final String GET_USERS_LINKED_NODES =
        "SELECT ur.org_node_id " +
        "FROM user_role ur " +
        "WHERE ur.user_id = ? " +
        "AND ur.activation_status = 'AC' ";

    public static final String GET_USERS_TOP_NODES =                         
        "SELECT unique o.org_node_id " +
        "FROM user_role u, org_node_parent p, org_node o " +
        "WHERE u.user_id = ? " +
        "AND u.activation_status = 'AC' " +
        "AND p.org_node_id = u.org_node_id " +
        "AND o.org_node_id = p.org_node_id " +
        "AND p.parent_org_node_id not in ( " +
            GET_USERS_LINKED_NODES +
        ") ";

    public static final String GET_USERS_VIEWABLE_NODES =
        "SELECT unique p.org_node_id FROM org_node_parent p " +
        "START WITH p.parent_org_node_id IN ( " +
            GET_USERS_TOP_NODES +
        ") " +
        "CONNECT BY PRIOR p.org_node_id = p.parent_org_node_id ";

    public static final String GET_USERS_VIEWABLE_STUDENTS =
        "SELECT unique sn.student_id " + 
        "FROM org_node_student sn, student s " +
        "WHERE (" +
          "sn.org_node_id IN ( " +
          GET_USERS_VIEWABLE_NODES +
          ") " +
          "OR sn.org_node_id IN ( " +
            GET_USERS_TOP_NODES +
          ")" +
        ") " +
        "AND sn.student_id = s.student_id " +
        "AND s.activation_status = 'AC'" +
        "AND sn.activation_status = 'AC'";
}
