CREATE OR REPLACE PROCEDURE "SP_INTERRUPTER" is

CURSOR C_ROSTERS IS

       SELECT TR.TEST_ROSTER_ID FROM TEST_ROSTER TR,
              (SELECT TEST_ROSTER_ID, MAX(CREATED_DATE_TIME) MAX_TIME FROM ITEM_RESPONSE
              GROUP BY TEST_ROSTER_ID ) IR
       WHERE TR.TEST_ROSTER_ID = IR.TEST_ROSTER_ID
       AND (TR.UPDATED_DATE_TIME < SYSDATE - 2.5/1440 AND TR.TEST_COMPLETION_STATUS in ('IP', 'SP'))
       AND MAX_TIME < SYSDATE - 2.5/1440
       and ((TR.updated_date_time > sysdate - 30 and tr.test_admin_id not in (81821,81822,81823,81824,81831,81832,81833))
       or (TR.updated_date_time > sysdate - 0.5 and tr.test_admin_id in (81821,81822,81823,81824,81831,81832,81833)))
       UNION ALL
       SELECT TR.TEST_ROSTER_ID
       FROM TEST_ROSTER TR
       WHERE TR.UPDATED_DATE_TIME < SYSDATE - 2.5/1440 AND TR.TEST_COMPLETION_STATUS in ('IP', 'SP')
       AND NOT EXISTS (select * from item_response where test_roster_id = tr.test_roster_id)
       and TR.updated_date_time > sysdate - 30
       and tr.test_admin_id not in (81821,81822,81823,81824,81831,81832,81833);

cursor rosterCursor is

select to_char(test_roster_id) from test_roster tr WHERE TEST_COMPLETION_STATUS = 'IN'
                AND not exists (SELECT * FROM STUDENT_ITEM_SET_STATUS
                                WHERE completion_status <> 'CO'
                                and test_roster_id = tr.test_roster_id)
                                and tr.test_admin_id not in (81821,81822,81823,81824,81831,81832,81833)
                                and TR.updated_date_time > sysdate - 30;

rosterId varchar2(32);

BEGIN

     FOR V_ROSTERS IN C_ROSTERS LOOP

         UPDATE STUDENT_ITEM_SET_STATUS SET COMPLETION_STATUS = 'IN'
         WHERE COMPLETION_STATUS in ('IP', 'SP')
         AND TEST_ROSTER_ID = V_ROSTERS.TEST_ROSTER_ID;

         UPDATE TEST_ROSTER SET TEST_COMPLETION_STATUS = 'IN', UPDATED_DATE_TIME = sysdate
         WHERE TEST_COMPLETION_STATUS in ('IP', 'SP')
         AND TEST_ROSTER_ID = V_ROSTERS.TEST_ROSTER_ID;

         COMMIT;

      END LOOP;

     commit;

    ------ invoke scoring for interrupted rosters marked complete

            open rosterCursor;

            LOOP

                fetch rosterCursor into rosterId;

                EXIT when rosterCursor%NOTFOUND;

                update TEST_ROSTER TR SET TEST_COMPLETION_STATUS = 'CO' where test_Roster_id = rosterId;

                commit;

            end loop;

            close rosterCursor;

    ------

    UPDATE TEST_ADMIN set test_admin_status = 'CU'
    where to_date((to_char(login_start_date, 'MM/DD/YYYY ')||to_char(daily_login_start_time, 'HH24:MI:SS')), 'MM/DD/YYYY HH24:MI:SS') <= sysdate
    and test_admin_status = 'FU';

    UPDATE TEST_ADMIN set test_admin_status = 'PA'
    where to_date((to_char(login_end_date, 'MM/DD/YYYY ')||to_char(daily_login_end_time, 'HH24:MI:SS')), 'MM/DD/YYYY HH24:MI:SS') <= sysdate
    and test_admin_status = 'CU';

    COMMIT;

    UPDATE TEST_ROSTER SET test_completion_status = 'IC'
    where test_completion_status in ('IN', 'IS')
    and test_admin_id in
        ( select test_Admin_id from test_admin where test_Admin_status = 'PA')
    and test_admin_id not in (81821,81822,81823,81824,81831,81832,81833);

    UPDATE TEST_ROSTER SET test_completion_status = 'NT'
    where test_completion_status = 'SC'
    and test_admin_id in
        ( select test_Admin_id from test_admin where test_Admin_status = 'PA')
    and test_admin_id not in (81821,81822,81823,81824,81831,81832,81833);

    COMMIT;
 
    UPDATE test_roster
    SET test_completion_status = 'SC'
    where test_completion_status = 'NT'
       and test_admin_id in
           (select distinct test_admin_id
              from test_admin
             where test_admin_status = 'CU'
               and product_id in (3710, 3720))
       and test_admin_id not in
           (81821, 81822, 81823, 81824, 81831, 81832, 81833);
    
    COMMIT;


end SP_INTERRUPTER;
/
