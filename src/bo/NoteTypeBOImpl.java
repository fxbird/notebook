package bo;

import bean.NoteType;
import bo.NoteTypeBO;
import dao.NoteDAO;
import dao.NoteTypeDAO;
import dao.hbn.NoteTypeDAOHbn;

import java.util.List;

public class NoteTypeBOImpl implements NoteTypeBO {
    private NoteTypeDAO typeDAO;
    private NoteDAO noteDAO;

    @Override
    public void createType(String name, NoteType parentNoteType) {
        NoteType t = new NoteType();
        t.setName(name);
        t.setParentType(parentNoteType);
        typeDAO.insert(t);
    }

    @Override
    public void createType(NoteType t) {
        typeDAO.insert(t);
    }

    @Override
    public void updateType(NoteType noteType) {
        typeDAO.update(noteType);
    }

    @Override
    public void deepDeleteType(NoteType noteType) {
        typeDAO.delete(noteType);
        noteDAO.deepDeleteByType(noteType.getId());
    }

    @Override
    public void logicalDeleteType(NoteType noteType) {
        noteType.setDel(1);
        typeDAO.update(noteType);
    }

    @Override
    public List<NoteType> getAllTypes() {
        return typeDAO.selectAll();
    }

    @Override
    public boolean isTypeExist(NoteType noteType) {
        if (noteType.getId() == null) {
            return !typeDAO.selectByNameParentType(noteType).isEmpty();
        } else {
            return !typeDAO.selectByParentTypeNameDiffId(noteType).isEmpty();
        }
    }

    @Override
    public Integer getNextTypeNo() {
        Integer maxNow = typeDAO.selectMaxTypeNo();
        return (maxNow != null ? maxNow + 1 : 1);
    }

    @Override
    public Integer getMaxTypeNo() {
        Integer maxNow = typeDAO.selectMaxTypeNo();
        return (maxNow != null ? maxNow : 0);
    }

    @Override
    public void updateParentType(List<NoteType> noteTypes, int newParentTypeId) {
        typeDAO.updateParentType(noteTypes, newParentTypeId);
    }

    @Override
    public void updateTypeDyn(NoteType noteType) {
        typeDAO.update(noteType);
    }

    @Override
    public void hideType(NoteType noteType) {
        noteType.setShow(0);
        typeDAO.update(noteType);
    }

    @Override
    public void showType(NoteType noteType) {
        noteType.setShow(0);
        typeDAO.update(noteType);
    }

    @Override
    public List<NoteType> findHiddenType() {
        return typeDAO.selectHidden();
    }

    public NoteDAO getNoteDAO() {
        return noteDAO;
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public NoteTypeDAO getTypeDAO() {
        return typeDAO;
    }

    public void setTypeDAO(NoteTypeDAO typeDAO) {
        this.typeDAO = typeDAO;
    }
}
