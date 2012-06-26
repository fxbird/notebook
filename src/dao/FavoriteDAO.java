package dao;

import bean.FavoriteItem;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavoriteDAO extends AbsBaseIbatisDAO {
    public FavoriteDAO(String namespace, SqlSessionFactory factory) {
        super(namespace, factory);
    }

    public List<FavoriteItem> selectAllItem(){
        return selectList("selectAllItem");
    }

    public void deleteItem(Integer no){
        delete("deleteItem",no);
    }

    public void insertItem(int noteNo){
        Map params=new HashMap();
        params.put("noteNo",noteNo);
        params.put("dropDate",new Timestamp(System.currentTimeMillis()));
        insert("insertItem", params);
    }

    public boolean isExist(int noteNo){
        return selectOne("selectItem",noteNo)!=null;
    }
}
