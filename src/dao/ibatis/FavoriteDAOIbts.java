package dao.ibatis;

import bean.FavoriteNote;
import dao.FavoriteDAO;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavoriteDAOIbts extends BaseIbatisDAO implements FavoriteDAO {
    public FavoriteDAOIbts(String namespace, SqlSessionFactory factory) {
        super(namespace, factory);
    }

    @Override
    public List<FavoriteNote> selectAllItem(){
        return selectList("selectAllItem");
    }

    @Override
    public void deleteItem(Integer no){
        delete("deleteItem",no);
    }

    @Override
    public void insertItem(int noteNo){
        Map params=new HashMap();
        params.put("noteNo",noteNo);
        params.put("dropDate",new Timestamp(System.currentTimeMillis()));
        insert("insertItem", params);
    }

    @Override
    public boolean isExist(int noteNo){
        return selectOne("selectItem",noteNo)!=null;
    }
}
