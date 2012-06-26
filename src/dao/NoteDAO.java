package dao;

import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;
import java.util.Map;

import bean.Note;

public class NoteDAO extends AbsBaseIbatisDAO {
    public NoteDAO(String namespace, SqlSessionFactory factory) {
        super(namespace, factory);
    }

    public List<Note> findByTypeTitle(Map params){
      return  selectList("findByTypeTitle",params);
    }

    public List<Note> findByTypeTitleContent(Map params){
        return  selectList("findByTypeTitleContent",params);
    }

    public void deepDeleteByType(int id){
        delete("deepDeleteByType",id);
    }

    public void update(Map params){
        update("update",params);
    }

    public void insert(Note note){
        insert("insert",note);
    }
}
