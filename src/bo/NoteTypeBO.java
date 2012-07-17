package bo;

import bean.NoteType;

import java.util.List;

public interface NoteTypeBO {
    void createType(String name, NoteType parentNoteType);

    void createType(NoteType t);

    void updateType(NoteType noteType);

    void deepDeleteType(NoteType noteType);

    void logicalDeleteType(NoteType noteType);

    List<NoteType> getAllTypes();

    boolean isTypeExist(NoteType noteType);

    Integer getNextTypeNo();

    Integer getMaxTypeNo();

    void updateParentType(List<NoteType> noteTypes, int newParentTypeId);

    void updateTypeDyn(NoteType noteType);

    void hideType(NoteType noteType);

    void showType(NoteType noteType);

    List<NoteType> findHiddenType();
}
