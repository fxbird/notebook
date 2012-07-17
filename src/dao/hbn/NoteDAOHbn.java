package dao.hbn;

import bean.Note;
import bean.NoteType;
import com.sun.xml.internal.bind.v2.TODO;
import com.xdg.util.hibernate.HbnExeUtil;
import com.xdg.util.hibernate.QueryLogic;
import com.xdg.util.hibernate.UpdateLogic;
import dao.NoteDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

public class NoteDAOHbn extends BaseHbnDAO implements NoteDAO {
    public NoteDAOHbn(Session session, String className) {
        super(session);
    }

    @Override
    public void deepDeleteByType(int id) {
        HbnExeUtil.executeUpdateHQL(session, "delete from  Note as note where note.noteType.id=" + id);
    }

    @Override
    public List<Note> findByTypeTitle(final List<NoteType> types, final List<String> keys) {

        return HbnExeUtil.executeQueryLogic(getSession(), new QueryLogic() {
            @Override
            public List execute(Session session) {
                Criteria criteria = getSession().createCriteria(Note.class);
                criteria.add(Restrictions.in("id", types));
                for (String key : keys) {
                    criteria.add(Restrictions.like("title", key, MatchMode.ANYWHERE));
                }
                return criteria.list();
            }
        });
    }

    @Override
    public List<Note> findByTypeTitleContent(final List<NoteType> types, final List<String> keys) {
        return HbnExeUtil.executeQueryLogic(getSession(), new QueryLogic() {
            @Override
            public List execute(Session session) {
                Criteria criteria = getSession().createCriteria(Note.class);
                criteria.add(Restrictions.in("id", types));
                Conjunction titleLike = Restrictions.conjunction();
                Conjunction contentLike = Restrictions.conjunction();

                for (String key : keys) {
                    titleLike.add(Restrictions.like("title", key, MatchMode.ANYWHERE));
                    contentLike.add(Restrictions.like("content", key, MatchMode.ANYWHERE));
                }

                criteria.add(Restrictions.or(titleLike, contentLike));

                return criteria.list();
            }
        });
    }

    @Override
    public void insert(final Note note) {
        HbnExeUtil.executeUpdateLogic(getSession(), new UpdateLogic() {
            @Override
            public void execute(Session session) throws Exception {
                session.save(note);
            }
        });
    }

    @Override
    public void update(final Note note) {
        HbnExeUtil.executeUpdateLogic(getSession(), new UpdateLogic() {
            @Override
            public void execute(Session session) throws Exception {
                session.update(note);
            }
        });
    }


}
