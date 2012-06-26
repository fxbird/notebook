import bean.Type;
import bo.BookBO;
import dao.TypeDAO;
import db.DBConnection;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpdateParentType {
    private static String path="E:/My Doc/programming/java/projects/idea/book2/types.note";
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BookBO bo=new BookBO();
        TypeDAO typeDAO=new TypeDAO("book.type", DBConnection.createSessionFactory());
        bo.setTypeDAO(typeDAO);

//        generateSerialized(path,bo);
//        updateFromSerialized(path,bo);
        displayAll(path,bo);
    }

    public static void generateSerialized(String path,BookBO bo) throws IOException {
        List<Type> types=bo.getAllTypes();
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(types);
        oos.close();
    }

    public static void updateFromSerialized(String path,BookBO bo)
            throws ClassNotFoundException, IOException {
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(path));

          List<Type> types=( List<Type>)ois.readObject();
         for (Type t:types){
             t.setName(null);
             bo.updateTypeDyn(t);
         }
        ois.close();

    }

    public static void displayAll(String path,BookBO bo)
            throws ClassNotFoundException, IOException {
          ObjectInputStream ois=new ObjectInputStream(new FileInputStream(path));

          List<Type> types=( List<Type>)ois.readObject();
        Collections.sort(types,new Comparator<Type>(){
            public int compare(Type t1, Type t2) {
                return t1.getId()-t2.getId();
            }
        });
         for (Type t:types){
             System.out.printf("id: %d, parent: %d, name : %s\n",t.getId(),t.getParentTypeNo(),t.getName());
         }
        ois.close();
    }
}
