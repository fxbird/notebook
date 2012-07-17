package dao.hbn;

import bean.NoteType;
import com.xdg.util.hibernate.HbnExeUtil;
import com.xdg.util.hibernate.UpdateLogic;
import dao.NoteTypeDAO;
import org.hibernate.Session;

import java.util.List;

public class NoteTypeDAOHbn extends BaseHbnDAO implements NoteTypeDAO {
    protected NoteTypeDAOHbn(Session session) {
        super(session);
    }

    @Override
    public void insert(final NoteType bookNoteType) {
        HbnExeUtil.executeUpdateLogic(getSession(), new UpdateLogic() {
            @Override
            public void execute(Session session) throws Exception {
                getSession().save(bookNoteType);
            }
        });
    }

    @Override
    public void update(final NoteType bookNoteType) {
        HbnExeUtil.executeUpdateLogic(getSession(), new UpdateLogic() {
            @Override
            public void execute(Session session) throws Exception {
                getSession().update(bookNoteType);
            }
        });
    }

    @Override
    public void delete(final NoteType noteType) {
        HbnExeUtil.executeUpdateLogic(getSession(),new UpdateLogic() {
            @Override
            public void execute(Session session) throws Exception {
                session.delete(noteType);
            }
        });
    }

    @Override
    public void logicalDelete(NoteType noteType) {
        noteType.setDel(1);
        update(noteType);
    }

    @Override
    public List<NoteType> selectAll() {
        return HbnExeUtil.executeQueryHQL(getSession(),"from NoteType as nt order by lower(nt.name)");
    }

    @Override
    public List<NoteType> selectByNameParentType(NoteType noteType) {
        return HbnExeUtil.executeQueryHQL(getSession(),"from NoteType as nt where nt.id="+noteType.getParentType().getId()+
                " and nt.name='"+noteType.getName()+"'");
    }

    @Override
    public List<NoteType> selectByParentTypeNameDiffId(NoteType noteType) {
        return HbnExeUtil.executeQueryHQL(getSession(),"from NoteType as nt where nt.id="+noteType.getParentType().getId()+
                        " and nt.name='"+noteType.getName()+"' and nt.id<>"+noteType.getId());
    }

    @Override
    public Integer selectMaxTypeNo() {
        return null;
    }

    @Override
    public void updateParentType(List<NoteType> noteTypes, int newParentTypeId) {

    }

    @Override
    public List<NoteType> selectHidden() {
        return HbnExeUtil.executeQueryHQL(getSession(),"from NoteType as nt where nt.show=1 and nt.del=1 order by nt.name");
    }

    @Override
    public void updateShowFlag(int id, int state) {

    }
}
