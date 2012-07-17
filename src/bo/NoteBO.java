package bo;

import bean.Note;
import bean.NoteType;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public interface NoteBO {
    void updateNoteType(Note note, NoteType noteType);

    List<Note> findNoteByTypeTitle(DefaultMutableTreeNode node, String keys);

    List<Note> findNoteByTypeTitleContent(DefaultMutableTreeNode node, String keys);

    void addNote(Note note);

    void update(Note note);
}
