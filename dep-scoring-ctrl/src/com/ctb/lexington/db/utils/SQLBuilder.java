package com.ctb.lexington.db.utils;

import java.util.ArrayList;
import java.util.List;


public class SQLBuilder {
    
    StringBuffer sql;
    List pairs = new ArrayList();
    

    public void addWhereClause(String name, String value) {
        
        if(value == null){
            pairs.add(name + " is null");
        }else{
            int index =value.indexOf("'");
            if(index!=-1){
                String preStr = value.substring(0, index);
                preStr = preStr+"'";
                value = preStr + value.substring(index, value.length());
            }

            pairs.add(name + "='" + value+"'");
        }
    }

    public String buildSQL(String sqlString) {
        // build the sql
        this.sql = new StringBuffer(sqlString);
        for(int i =0;i<pairs.size(); ++i){
            if(i==0){
                sql.append(" where ");
            }else{
                sql.append(" and ");
            }
            sql.append(pairs.get(i));
        }
        return sql.toString();
    }

}
