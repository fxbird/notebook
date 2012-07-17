package dbwin.editor;

import bean.NoteType;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.List;
import java.awt.Component;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;

public class TypeEditor extends AbstractCellEditor implements  TableCellEditor , ItemListener{
    private JComboBox cb;
    private List types;
    private String idPropName;

    public TypeEditor(List types,String idPropName){
        cb = new JComboBox(types.toArray());
        this.types=types;
        this.idPropName=idPropName;
        cb.addItemListener(this);
    }

    public Object getCellEditorValue() {
        try {
            return PropertyUtils.getProperty(cb.getSelectedItem(),idPropName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
             return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
             return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }


    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        for (int i = 0; i < types.size(); i++) {
             NoteType noteType =(NoteType)types.get(i);
             if (noteType.getId().equals(value)){
                 cb.setSelectedIndex(i);
                 break;
             }
        }

        cb.setMaximumRowCount(25);

        return cb;
    }

    public void itemStateChanged(ItemEvent e) {
        stopCellEditing();
    }

    public JComboBox getCB(){
        return cb;
    }
}
