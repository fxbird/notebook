package dao;


import bean.Note;
import bean.NoteType;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public interface NoteDAO {
       public List<Note> findByTypeTitle(final List<NoteType> types,final List<String> keys);

       public List<Note> findByTypeTitleContent(final List<NoteType> types,final List<String> keys);

       public void deepDeleteByType(final int id);

       public void update(Note note);

       public void insert(Note note);
}
