
set termout on
spool oas92_deploy.log

conn oas/oas7pr8@oasr5p_ewdc

@'OAS_DDL_LL.sql'
@'OAS_DML_LL.sql'
@'OAS_assign_common_demographics_LL.sql'
@'RESET_ROSTER_LL.sql'
@'setup_customer_base_LL.sql'
@'setup_espanol_customer_LL.sql'
@'setup_laslinkforma_customer_LL.sql'
@'setup_laslinkformb_customer_LL.sql'
@'SETUP_LASLINKFORMC_CUSTOMER_LL.sql'
@'SP_LAS_LM_PO_EXPIRY_UPDATE_LL.sql'
@'INSERTLICENSEDATAFORLASLINK_LL.sql'
@'UPDATELICENSEDATAFORLASLINK_LL.sql'
@'PKG_LAS_LM_TRG_LL.sql'
@'OAS_UTILS_LL.sql'
@'LIC_CREATE_SESSION_ORGNODE_LL.sql'
@'LIC_CREATE_SUBTEST_ORGNODE_LL.sql'
@'LIC_DELETE_SESSION_ORGNODE_LL.sql'
@'LIC_DELETE_SUBTEST_ORGNODE_LL.sql'
@'LIC_UPDATE_SESSION_ORGNODE_LL.sql'
@'LIC_UPDATE_SUBTEST_ORGNODE_LL.sql'
@'LICENSE_MAINTAINABILITY_JOB_LL.sql'

commit;
disconnect

conn irs/irs7prd9@irsr5p_ewdc

@'IRS_DDL_LL.sql'
@'IRS_DML_LL.sql'
@'wipeout_scoring_after_reset_LL.sql'

commit;
disconnect


conn ads/ads7pr8@oasr5p_ewdc
@'ADS_Prod_DDL_Part1_LL.sql'
commit;
disconnect

conn ads/ads#123@oasr51d
@'ADS_Dev_DDL_LL.sql'
@'Copy_Content_Prod_LL.sql'
@'Copy_Content_Prod_execute_LL.sql'

commit;
disconnect

conn ads/ads7pr8@oasr5p_ewdc
@'ADS_Prod_DDL_Part2_LL.sql'
commit;
disconnect



set termout off
spool off