package dao.ibatis;

import bean.NoteType;
import dao.NoteTypeDAO;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class NoteTypeDAOIbts extends BaseIbatisDAO implements NoteTypeDAO {
    public NoteTypeDAOIbts(String namespace, SqlSessionFactory factory) {
        super(namespace, factory);
    }

    public void insert(NoteType bookNoteType) {
        insert("insertBookType", bookNoteType);
    }

    public void update(NoteType bookNoteType) {
        update("updateBookType", bookNoteType);
    }

    public void logicalDelete(int typeNo) {
        delete("logicalDelete", typeNo);
    }

    public void deepDelete(int typeNo){
        delete("delete",typeNo);
    }

    public List<NoteType> selectAll() {
        return (List<NoteType>) selectList("selectAll");
    }

    public List<NoteType> selectByNameParentType(NoteType noteType) {
        return (List<NoteType>) selectList("selectSameTypeNameWhenInsert", noteType);
    }

    public List<NoteType> selectByParentTypeNameDiffId(NoteType noteType) {
        return (List<NoteType>) selectList("selectSameTypeNameWhenEdit", noteType);
    }

    public Integer selectMaxTypeNo(){
        return (Integer)selectOne("selectMaxTypeNo");
    }

    public void updateParentType(List<NoteType> noteTypes){
        update("updateParentType",map);
    }

    public void hide(int id){
        update("hide",id);
    }

    public void show(int id){
        update("show",id);
    }

    public List<NoteType> selectHidden(){
        return selectList("selectHidden");
    }


}
