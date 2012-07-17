package dao.ibatis;

import bean.NoteType;
import dao.NoteDAO;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Note;

public class NoteDAOIbts extends BaseIbatisDAO implements NoteDAO {
    public NoteDAOIbts(String namespace, SqlSessionFactory factory) {
        super(namespace, factory);
    }

    public List<Note> findByTypeTitle(final List<NoteType> types, final List<String> keys) {
        HashMap params = new HashMap();
        params.put("types", types);
        params.put("keys", keys);
        return selectList("findByTypeTitle", params);
    }

    public List<Note> findByTypeTitleContent(final List<NoteType> types, final List<String> keys) {
        HashMap params = new HashMap();
        params.put("types", types);
        params.put("keys", keys);
        return selectList("findByTypeTitleContent", params);
    }

    public void deepDeleteByType(final int id) {
        delete("deepDeleteByType", id);
    }

    public void update(Map params) {
        update("update", params);
    }

    public void insert(Note note) {
        insert("insert", note);
    }
}
