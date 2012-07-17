package bo;

import bean.FavoriteNote;
import bean.NoteType;
import dao.ibatis.FavoriteDAOIbts;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class BookBO {
    private FavoriteDAOIbts favDAO;








    public void addFavoriteItem(int noteNo){
        if (favDAO.isExist(noteNo)) return;
        favDAO.insertItem(noteNo);
    }

    public List<FavoriteNote> getFavoriteItemList(){
        return favDAO.selectAllItem();
    }

    public void deleteFavoriteItem(Integer no){
        favDAO.deleteItem(no);
    }

    //getter and setter




    public List<NoteType> extractTypes(DefaultMutableTreeNode node){
         ArrayList<NoteType> noteTypes =new ArrayList<NoteType>();
        if (node.isLeaf()){
            noteTypes.add((NoteType) node.getUserObject());
        }else{

            Enumeration<DefaultMutableTreeNode> enumTypes=node.breadthFirstEnumeration();
            while (enumTypes.hasMoreElements())
                noteTypes.add((NoteType)enumTypes.nextElement().getUserObject());
        }

        return noteTypes;
    }


}
