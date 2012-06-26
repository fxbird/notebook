/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbwin.editor;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class CheckBoxEditor extends AbstractCellEditor
        implements TableCellEditor {

    private JCheckBox cb;
    private int valueTrue;
    private int valueFalse;
	private TableCellEditor editor;

    public CheckBoxEditor(int vluTrue, int vluFalse,JTable table) {
        this.valueTrue=vluTrue;
        this.valueFalse=vluFalse;
        editor = table.getDefaultEditor(Boolean.class);
    }

    public Object getCellEditorValue() {
        
        if (editor.getCellEditorValue().equals(Boolean.TRUE)){
        	return valueTrue;
        } else {
        	return valueFalse;
        }
        
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      return editor.getTableCellEditorComponent(table,(value.equals(valueTrue)? new Boolean(true):new Boolean(false)),isSelected,row,column);
    }
}
