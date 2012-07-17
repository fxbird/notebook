package bo;

import bean.Note;
import bean.NoteType;
import dao.hbn.NoteDAOHbn;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class NoteBOImpl implements NoteBO {
    private NoteDAOHbn noteDAO;

    @Override
    public void updateNoteType(Note note, NoteType noteType) {
        note.setNoteType(noteType);
        noteDAO.update(note);
    }

    @Override
    public List<Note> findNoteByTypeTitle(DefaultMutableTreeNode node, String keys) {
        ArrayList<NoteType> types = new ArrayList<NoteType>();
        if (node != null) {
            NoteType t = (NoteType) node.getUserObject();

            if (t.getId() > 0) {
                types.addAll(extractTypes(node));
            }
        }

        String[] arrKey = keys.split(" | ");

        List<Note> list = noteDAO.findByTypeTitle(types, Arrays.asList(arrKey));
        return list;
    }

    @Override
    public List<Note> findNoteByTypeTitleContent(DefaultMutableTreeNode node, String keys) {
        ArrayList<NoteType> types = new ArrayList<NoteType>();
        if (node != null) {
            NoteType t = (NoteType) node.getUserObject();

            if (t.getId() > 0) {
                types.addAll(extractTypes(node));
            }
        }

        String[] arrKey = keys.split(" | ");


        List<Note> list = noteDAO.findByTypeTitleContent(types, Arrays.asList(arrKey));
        return list;
    }

    @Override
    public void addNote(Note note) {
        noteDAO.insert(note);
    }

    @Override
    public void update(Note note) {
        noteDAO.update(note);
    }

    private List<NoteType> extractTypes(DefaultMutableTreeNode node) {
        ArrayList<NoteType> noteTypes = new ArrayList<NoteType>();
        if (node.isLeaf()) {
            noteTypes.add((NoteType) node.getUserObject());
        } else {

            Enumeration<DefaultMutableTreeNode> enumTypes = node.breadthFirstEnumeration();
            while (enumTypes.hasMoreElements())
                noteTypes.add((NoteType) enumTypes.nextElement().getUserObject());
        }

        return noteTypes;
    }

    public NoteDAOHbn getNoteDAO() {
        return noteDAO;
    }

    public void setNoteDAO(NoteDAOHbn noteDAO) {
        this.noteDAO = noteDAO;
    }
}
