/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dbwin.render;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.Box;

/**
 *
 * @author fxbird
 */
//public class CheckBoxRender implements TableCellRenderer{
//    private int valueTrue;
//    private int valueFalse;
//    public CheckBoxRender(int vluTrue,int vluFalse){
//        this.valueTrue=vluTrue;
//        this.valueFalse=vluFalse;
//    }
//
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//      JCheckBox cb=new JCheckBox();
//      cb.setSelected(value.toString().equals(valueTrue+""));
//      JPanel in=new JPanel();
//      in.setLayout(new BoxLayout(in, BoxLayout.Y_AXIS));
//      in.add(Box.createGlue());
//      in.add(cb);
//      in.add(Box.createGlue());
//      JPanel out=new JPanel();
//      out.add(in,BorderLayout.CENTER);
////      return cb;
//      return out;
//    }
//}


public class CheckBoxRender implements TableCellRenderer{
    private Integer valueTrue;
    private Integer valueFalse;
    TableCellRenderer render;
    public CheckBoxRender(int vluTrue,int vluFalse,JTable table){
        this.valueTrue=vluTrue;
        this.valueFalse=vluFalse;
        render = table.getDefaultRenderer(Boolean.class);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp= render.getTableCellRendererComponent(table,valueTrue.equals(value), isSelected,hasFocus,row,column);
        return comp;
        
    }
}

