package crud.base;

import java.util.Dictionary;
import java.util.HashMap;

public abstract class AbstractRepository{

    protected String findAllQuery(String tableName){
    return "Select * from" + tableName + " Where deleted = false";
    }

    protected String softDeleteQuery(String tableName, String id){
        return "Update " + tableName + " set deleted = true, deleted_at = ?, deleted_by = ? where id = "+ id + " and deleted = false ";
    }

    protected String hardDeleteQuery(String tableName, String id){
        return "Delete from " + tableName + " where id =" + id +  " and deleted = false";
    }


    protected String findByColumnsQuery(String tableName, HashMap<String, String> columnsAndValues){
        StringBuilder query = new StringBuilder("SELECT * from " + tableName + " Where ");
        for( String key : columnsAndValues.keySet()){
            query.append(key).append(" = ").append(columnsAndValues.get(key)).append(" AND ");

        }

        query.delete(query.length()-5, query.length());
        return query.toString();
    }
}
