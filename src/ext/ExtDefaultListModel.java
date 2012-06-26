/*
 * 重写DefaultListModel类，添加方便的构造函数
 * 
 */

package ext;

import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author fxbird
 */
public class ExtDefaultListModel extends DefaultListModel {
	private List items;
    public ExtDefaultListModel(){
        super();
    }
    
     public ExtDefaultListModel(Object [] data){
         this();
         for (int i = 0; i < data.length; i++) {
             Object object = data[i];
             addElement(object);
         }
     }
     
      public ExtDefaultListModel(List list){
          this() ;
          items=list;
          for (Iterator it = list.iterator(); it.hasNext();) {
              Object object = it.next();
              addElement(object);
          }
      }

	public List getItems() {
		return items;
	}
      
      
}
