UPDATE CUSTOMER_CONFIGURATION cc
   SET cc.CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription_Disabled_OAS1944'
 WHERE cc.CUSTOMER_CONFIGURATION_NAME = 'Allow_Subscription'
   and EXISTS
 (SELECT 1
          FROM CUSTOMER_CONFIGURATION LMCC
         where LMCC.customer_id = cc.customer_id
           AND LMCC.CUSTOMER_CONFIGURATION_NAME = 'TABE_Customer')
/
commit;