import bean.NoteType;
import bo.BookBO;
import dao.ibatis.NoteTypeDAOIbts;
import db.DBConnection;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateParentType {
    private static String path="E:/My Doc/programming/java/projects/idea/book2/types.note";
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BookBO bo=new BookBO();
        NoteTypeDAOIbts typeDAO=new NoteTypeDAOIbts("book.type", DBConnection.createSessionFactory());
        bo.setTypeDAO(typeDAO);

//        generateSerialized(path,bo);
//        updateFromSerialized(path,bo);
        displayAll(path,bo);
    }

    public static void generateSerialized(String path,BookBO bo) throws IOException {
        List<NoteType> noteTypes =bo.getAllTypes();
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(noteTypes);
        oos.close();
    }

    public static void updateFromSerialized(String path,BookBO bo)
            throws ClassNotFoundException, IOException {
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(path));

          List<NoteType> noteTypes =( List<NoteType>)ois.readObject();
         for (NoteType t: noteTypes){
             t.setName(null);
             bo.updateTypeDyn(t);
         }
        ois.close();

    }

    public static void displayAll(String path,BookBO bo)
            throws ClassNotFoundException, IOException {
          ObjectInputStream ois=new ObjectInputStream(new FileInputStream(path));

          List<NoteType> noteTypes =( List<NoteType>)ois.readObject();
        Collections.sort(noteTypes,new Comparator<NoteType>(){
            public int compare(NoteType t1, NoteType t2) {
                return t1.getId()-t2.getId();
            }
        });
         for (NoteType t: noteTypes){
             System.out.printf("id: %d, parent: %d, name : %s\n",t.getId(),t.getParentTypeNo(),t.getName());
         }
        ois.close();
    }
}
