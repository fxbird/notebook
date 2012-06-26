package dbwin.render;

import org.apache.commons.beanutils.PropertyUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;

public class TypeRender implements TableCellRenderer {
    private JLabel text=new JLabel();
    private Map mapData = new HashMap();

    public TypeRender(List data,String idPropName,String descPropName){
        try {
            for (int i = 0; i < data.size(); i++) {
               Object elem=data.get(i);
               mapData.put(
                       PropertyUtils.getProperty(elem,idPropName),
                       PropertyUtils.getProperty(elem,descPropName)
                       );
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        text.setOpaque(true);
        text.setText(mapData.get(value).toString());
        text.setHorizontalAlignment(JLabel.CENTER);
        System.out.println("test name: "+mapData.get(value).toString()+" test id: "+value.toString());

//        text.setBackground(Color.red);
        return text;
    }

    public JLabel getComponent(){
        return text;
    }
}
