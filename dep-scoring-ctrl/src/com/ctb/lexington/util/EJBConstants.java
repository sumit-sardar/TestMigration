package com.ctb.lexington.util;


/*
 * EJBConstants.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 *
 */


/**
 * <code>EJBConstants</code> defines constants used to access enterprise java beans in the application.
 *
 * @author  <a href="mailto:christopher.p.ulbrich@accenture.com">Chris Ulbrich</a>
 *
 */
public final class EJBConstants
{
    // stateful session bean jndi names
    static public final String HIERARCHY_MANAGER = "ejb/HierarchyManagerEJB";

    // stateless session bean jndi names
    static public final String TEST_CATALOG         = "ejb/TestCatalogEJB";
    static public final String TEST_CATALOG_ASSIGNMENT = "ejb/TestCatalogAssignmentEJB";
    static public final String MANAGE_GROUPS        = "ejb/ManageGroupsEJB";
    static public final String GROUP_MANAGER        = "ejb/GroupManagerEJB";
    static public final String TEST_ADMINISTRATION  = "ejb/TestAdministrationEJB";
    static public final String USER_MANAGER         = "ejb/UserManagerEJB";
    static public final String STUDENT_MANAGER      = "ejb/StudentManagerEJB";
    static public final String ADMINISTRATION_HOME  = "ejb/AdministrationHomeEJB";
    static public final String SUBSCRIPTION_MANAGER = "ejb/SubscriptionManagerEJB";
    static public final String ORGANIZATION_MANAGER = "ejb/OrganizationManagerEJB";
    static public final String HELP                 = "ejb/HelpEJB";
    static public final String REPORTS              = "ejb/ReportsEJB";
    static public final String USER_LOGIN           = "ejb/UserLoginEJB";
    static public final String CREATE_ORGANIZATION  = "ejb/CreateOrganizationEJB";

    // entity bean jndi names
    static public final String ADDRESS              = "ejb/AddressEJB";
    static public final String CLIENT_CONFIGURATION = "ejb/ClientConfigurationEJB";
    static public final String CUSTOMER             = "ejb/CustomerEJB";
    static public final String ITEM_SET             = "ejb/ItemSetEJB";
    static public final String NODE_CATEGORY        = "ejb/NodeCategoryEJB";
    static public final String NODE                 = "ejb/NodeEJB";
    static public final String ORG_NODE_PARENT      = "ejb/OrgNodeParentEJB";
    static public final String ORG_NODE_STUDENT     = "ejb/OrgNodeStudentEJB";
    static public final String ORG_NODE_TEST_CATALOG= "ejb/OrgNodeTestCatalogEJB";
    static public final String PASSWORD_HINT_QUESTION="ejb/PasswordHintQuestionEJB";
    static public final String PASSWORD_HISTORY     = "ejb/PasswordHistoryEJB";
    static public final String PERMISSION           = "ejb/PermissionEJB";
    static public final String PRODUCT              = "ejb/ProductEJB";
    static public final String ROLE                 = "ejb/RoleEJB";
    static public final String ROLE_PERMISSION      = "ejb/RolePermissionEJB";
    static public final String STUDENT_CONTACT      = "ejb/StudentContactEJB";
    static public final String STUDENT              = "ejb/StudentEJB";
    static public final String STUDENT_TUTORIAL_STATUS="ejb/StudentTutorialStatusEJB";
    static public final String SUBSCRIPTION         = "ejb/SubscriptionEJB";
    static public final String TEST_ADMIN           = "ejb/TestAdminEJB";
    static public final String TEST                 = "ejb/TestEJB";
    static public final String TEST_ROSTER          = "ejb/TestRosterEJB";
    static public final String TEST_ADMIN_PROCTOR   = "ejb/TestAdminProctorEJB";
    static public final String TUTORIAL             = "ejb/TutorialEJB";
    static public final String USER                 = "ejb/UserEJB";
    static public final String USER_LOGIN_ATTEMPT   = "ejb/UserLoginAttemptEJB";
    static public final String USER_ROLE            = "ejb/UserRoleEJB";
    static public final String BROADCAST_MESSAGE    = "ejb/BroadcastMessageLogEJB";
    static public final String STATE_PR_CODE        = "ejb/StatePrCodeEJB";

}
/**
 * $Log$
 * Revision 1.1  2007/01/30 01:31:45  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:28  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.5  2002/09/20 00:50:36  kgawetsk
 * Added TEST_ADMIN_PROCTOR.
 *
 */
