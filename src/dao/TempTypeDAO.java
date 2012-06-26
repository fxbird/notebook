package dao;

import bean.Type;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class TempTypeDAO extends AbsBaseIbatisDAO {
    public TempTypeDAO(String namespace, SqlSessionFactory factory) {
        super(namespace, factory);
    }

    public void insert(Type bookType) {
        insert("insertBookType", bookType);
    }

    public void update(Type bookType) {
        update("updateBookType", bookType);
    }

    public void delete(int typeNo) {
        delete("deleteBookType", typeNo);
    }

    public List<Type> selectAll() {
        return (List<Type>) selectList("selectAll");
    }

    public List<Type> selectSameTypeWhenInsert(Type type) {
        return (List<Type>) selectList("selectSameTypeNameWhenInsert", type);
    }

    public List<Type> selectSameTypeWhenEdit(Type type) {
        return (List<Type>) selectList("selectSameTypeNameWhenEdit", type);
    }

    public Integer selectMaxTypeNo(){
        return (Integer)selectOne("selectMaxTypeNo");
    }

    public void updateParentType(Map map){
        update("updateParentType",map);
    }

}
