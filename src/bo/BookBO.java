package bo;

import bean.FavoriteItem;
import bean.Note;
import bean.Type;
import dao.FavoriteDAO;
import dao.NoteDAO;
import dao.TempTypeDAO;
import dao.TypeDAO;

import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.Timestamp;
import java.util.*;

public class BookBO {
    private TypeDAO typeDAO;
    private NoteDAO noteDAO;
    private FavoriteDAO favDAO;

    public void createType(String name, Type parentType) {
        Type t = new Type();
        t.setName(name);
        if (parentType != null) {
            t.setParentTypeNo(parentType.getId());
        }
        typeDAO.insert(t);
    }

    public void createType(Type t) {
        typeDAO.insert(t);
    }

    public void editType(Type type) {
        typeDAO.update(type);
    }

    public void deepDeleteType(Type type) {
        typeDAO.deepDelete(type.getId());
        noteDAO.deepDeleteByType(type.getId());
    }

    public void logicalDeleteType(Type type){
        typeDAO.logicalDelete(type.getId());
    }

    public List<Type> getAllTypes() {
        return (List<Type>) typeDAO.selectAll();
    }

    public boolean isTypeExist(Type type) {
        if (type.getId() == null) {
            return !typeDAO.selectSameTypeWhenInsert(type).isEmpty();
        } else {
            return !typeDAO.selectSameTypeWhenEdit(type).isEmpty();
        }
    }

    public Integer getNextTypeNo() {
        Integer maxNow = typeDAO.selectMaxTypeNo();
        return (maxNow != null ? maxNow + 1 : 1);
    }

    public Integer getMaxTypeNo() {
        Integer maxNow = typeDAO.selectMaxTypeNo();
        return (maxNow != null ? maxNow : 0);
    }

    public void updateParentType(Map map) {
        typeDAO.updateParentType(map);
    }

    public void updateNoteType(int noteId,int typeId){
        Map params=new HashMap();
        params.put("id",noteId);
        params.put("typeNo",typeId);

        noteDAO.update(params);
    }

    public List<Note> findNoteByTypeTitle(DefaultMutableTreeNode node, String keys) {
        Map params = new HashMap();
        if (node==null){
            params.put("typeNos",null);
        }else  {
            Type t=(Type)node.getUserObject();
            if (t.getId()<1) params.put("typeNos",null);
            else   params.put("typeNos", extractTypes(node));
        }

        String[] arrKey = keys.split(" | ");
        for (int i = 0; i < arrKey.length; i++) {
           arrKey[i]="%"+arrKey[i]+"%";

        }
        params.put("keys", arrKey);

        List<Note> list = noteDAO.findByTypeTitle(params);
        return list;
    }

    public List<Note> findNoteByTypeTitleContent(DefaultMutableTreeNode node, String keys) {
        Map params = new HashMap();
       if (node==null){
            params.put("typeNos",null);
        }else  {
            Type t=(Type)node.getUserObject();
            if (t.getId()<1) params.put("typeNos",null);
            else   params.put("typeNos", extractTypes(node));
        }

        String[] arrKey = keys.split(" | ");
        for (int i = 0; i < arrKey.length; i++) {
           arrKey[i]="%"+arrKey[i]+"%";
        }

        params.put("keys",arrKey);

        List<Note> list = noteDAO.findByTypeTitleContent(params);
        return list;
    }

    public void updateTypeDyn(Type type){
        typeDAO.update(type);
    }

    public void hideType(int id){
        typeDAO.hide(id);
    }

    public void showType(int id){
        typeDAO.show(id);
    }

    public List<Type> findHiddenType(){
        return typeDAO.findHidden();
    }

    public void addFavoriteItem(int noteNo){
        if (favDAO.isExist(noteNo)) return;
        favDAO.insertItem(noteNo);
    }

    public List<FavoriteItem> getFavoriteItemList(){
        return favDAO.selectAllItem();
    }

    public void deleteFavoriteItem(Integer no){
        favDAO.deleteItem(no);
    }

    //getter and setter


    public TypeDAO getTypeDAO() {
        return typeDAO;
    }

    public void setTypeDAO(TypeDAO typeDAO) {
        this.typeDAO = typeDAO;
    }

    public NoteDAO getNoteDAO() {
        return noteDAO;
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public void setFavDAO(FavoriteDAO favDAO) {
        this.favDAO = favDAO;
    }

    public List<Type> extractTypes(DefaultMutableTreeNode node){
         ArrayList<Type> types=new ArrayList<Type>();
        if (node.isLeaf()){
            types.add((Type) node.getUserObject());
        }else{

            Enumeration<DefaultMutableTreeNode> enumTypes=node.breadthFirstEnumeration();
            while (enumTypes.hasMoreElements())
                types.add((Type)enumTypes.nextElement().getUserObject());
        }

        return types;
    }

    public void addNote(Note note){
        noteDAO.insert(note);
    }
}
