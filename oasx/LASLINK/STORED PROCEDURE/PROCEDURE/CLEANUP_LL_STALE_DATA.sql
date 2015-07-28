CREATE OR REPLACE PROCEDURE CLEANUP_LL_STALE_DATA AS

  V_FF_PRODUCT_ID INTEGER := 0;

  /**
  * Fetch all re level item sets where there is no parent child relationship present.
  * Do not consider top level re level item set.
  */
  CURSOR CUR_STALE_RE_ITEM_SETS(IN_FF_PRODUCT_ID INTEGER) IS
    select iset.item_set_id            AS item_set_id,
           isc.Item_Set_category_level AS Item_Set_category_level
      from item_set iset, Item_Set_Category isc
     where isc.framework_product_id in (IN_FF_PRODUCT_ID)
       and isc.item_set_category_id = iset.item_set_category_id
       and isc.Item_Set_category_level <> 1
       and iset.item_set_type = 'RE'
       and not exists (select 1
              from Item_Set_Parent
             where item_set_id = iset.item_Set_id)
     order by isc.Item_Set_category_level desc;

BEGIN

  FOR I IN 1 .. 2 LOOP
    IF I = 1 THEN
      V_FF_PRODUCT_ID := 7000;
    ELSE
      V_FF_PRODUCT_ID := 7500;
    END IF;
  
    FOR CUR_DATA IN CUR_STALE_RE_ITEM_SETS(V_FF_PRODUCT_ID) LOOP
    
      -- delete from DATAPOINT_CONDITION_CODE
      DELETE FROM DATAPOINT_CONDITION_CODE
       WHERE DATAPOINT_ID IN
             (SELECT DATAPOINT_ID
                FROM DATAPOINT
               WHERE EXISTS
               (SELECT 1
                        FROM ITEM_SET_ITEM
                       WHERE ITEM_SET_ITEM.ITEM_SET_ID = CUR_DATA.ITEM_SET_ID
                         AND ITEM_SET_ITEM.ITEM_ID = DATAPOINT.ITEM_ID));
    
      -- delete from DATAPOINT
      DELETE FROM DATAPOINT
       WHERE EXISTS
       (SELECT 1
                FROM ITEM_SET_ITEM
               WHERE ITEM_SET_ITEM.ITEM_SET_ID = CUR_DATA.ITEM_SET_ID
                 AND ITEM_SET_ITEM.ITEM_ID = DATAPOINT.ITEM_ID);
    
      -- delete from ITEM_SET_ITEM  
      DELETE FROM ITEM_SET_ITEM WHERE ITEM_SET_ID = CUR_DATA.ITEM_SET_ID;
    
      -- delete from ITEM_SET_PRODUCT
      DELETE FROM ITEM_SET_PRODUCT
       WHERE ITEM_SET_ID = CUR_DATA.ITEM_SET_ID;
    
      -- delete from ITEM_SET_PARENT
      DELETE FROM ITEM_SET_PARENT WHERE ITEM_SET_ID = CUR_DATA.ITEM_SET_ID;
    
      -- delete from ITEM_SET_ANCESTOR
      DELETE FROM ITEM_SET_ANCESTOR
       WHERE ITEM_SET_ID = CUR_DATA.ITEM_SET_ID;
    
      -- delete from ITEM_SET
      DELETE FROM ITEM_SET WHERE ITEM_SET_ID = CUR_DATA.ITEM_SET_ID;
    
    END LOOP; --loop for each re stale data
  
  END LOOP; -- loop for each parent product
  COMMIT;
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
  
END CLEANUP_LL_STALE_DATA;
/
