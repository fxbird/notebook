package dao;


import bean.NoteType;

import java.util.List;
import java.util.Map;

public interface NoteTypeDAO {
    public void insert(NoteType bookNoteType);
       public void update(NoteType bookNoteType) ;

       public void logicalDelete(NoteType noteType) ;

       public void delete(NoteType noteType);

       public List<NoteType> selectAll() ;

       public List<NoteType> selectByNameParentType(NoteType noteType);

       public List<NoteType> selectByParentTypeNameDiffId(NoteType noteType) ;

       public Integer selectMaxTypeNo();

       public void updateParentType(List<NoteType> noteTypes, int newParentTypeId);

       public void updateShowFlag(int id,int state);

       public List<NoteType> selectHidden();

}
