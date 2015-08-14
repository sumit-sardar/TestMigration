package com.tcs.dataaccess;

import com.tcs.model.ItemObject;
import com.tcs.model.TestRosterObject;
import java.util.List;
import java.util.Set;

public abstract interface StudentTestSessionDao
{
  public abstract Set<TestRosterObject> getItemIdsFromItemSet(ConnectionManager paramConnectionManager1, ConnectionManager paramConnectionManager2, Integer paramInteger)
    throws Exception;

  public abstract List<ItemObject> getItemListFromADS(ConnectionManager paramConnectionManager, String[] paramArrayOfString)
    throws Exception;

  public abstract String updateAnswerInItem(TestRosterObject paramTestRosterObject, int paramInt)
    throws Exception;

  public abstract String getHTMLWithoutAnswerAppended(TestRosterObject paramTestRosterObject)
    throws Exception;

  public abstract String[] getDistinctTestAdminIdPerProduct(ConnectionManager paramConnectionManager, String paramString1, String paramString2, String paramString3)
    throws Exception;

  public abstract void createReport(TestRosterObject paramTestRosterObject, int paramInt)
    throws Exception;
}

