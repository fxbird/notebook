package dao;

import org.apache.ibatis.session.SqlSessionFactory;
import other.Constant;
import other.BookException;
import other.Msg;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.RowMapper;
import bean.Type;
import java.sql.PreparedStatement;
import java.util.Map;

public class TypeDAO extends AbsBaseIbatisDAO {
    public TypeDAO(String namespace, SqlSessionFactory factory) {
        super(namespace, factory);
    }

    public void insert(Type bookType) {
        insert("insertBookType", bookType);
    }

    public void update(Type bookType) {
        update("updateBookType", bookType);
    }

    public void logicalDelete(int typeNo) {
        delete("logicalDelete", typeNo);
    }

    public void deepDelete(int typeNo){
        delete("deepDelete",typeNo);
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

    public void hide(int id){
        update("hide",id);
    }

    public void show(int id){
        update("show",id);
    }

    public List<Type> findHidden(){
        return selectList("findHidden");
    }


}
