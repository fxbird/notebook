package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Test extends JFrame{
    public Test(){
        Object[][] data={
            {1,1},
            {2,1},
            {3,0}
        };
        JTable table=new JTable(new DefaultTableModel(data,new String[]{"No.","T/F"}));
        JScrollPane sp=new JScrollPane(table);
        add(sp,BorderLayout.CENTER);


        table.getColumnModel().getColumn(1).setCellRenderer(new MyCheckBoxRender(1, 0));
    }
    
    public static void main(String[] args) {
        Test t=new  Test();
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        t.pack();
        t.setVisible(true);
    }
}

 /**
  * my customized cell render
  * when value=1,checkbox is selected, when 0,not selected
  * @author fxbird
  */
 class MyCheckBoxRender extends JPanel implements TableCellRenderer{
    private int valueTrue;
    private int valueFalse;
    JCheckBox cb=new JCheckBox();
    public MyCheckBoxRender(int vluTrue,int vluFalse){
        this.valueTrue=vluTrue;
        this.valueFalse=vluFalse;

//        FlowLayout flow=new FlowLayout();
//        setLayout(flow );
        add(cb);
//         JPanel in=new JPanel();
//      in.setLayout(new BoxLayout(in, BoxLayout.Y_AXIS));
//      in.add(Box.createGlue());
//      in.add(cb);
//      in.add(Box.createGlue());
//
//      add(in,BorderLayout.CENTER);
      setBackground(Color.red);

      setBorder(javax.swing.BorderFactory.createEtchedBorder(new Color(51, 51, 255), new Color(0, 51, 255)));

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
     
      cb.setSelected(value.toString().equals(valueTrue+""));
        final JButton btn = new JButton("hello");
        btn.setPreferredSize(new Dimension(20,20));

     
       
      return this;
//      return out;
    }
}
