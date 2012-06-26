package frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

class Testing
{
    private DefaultTableModel model;
    private JTable table;

    public void buildGUI()
  {
    JPanel p = new JPanel();
    JTextField tf1 = new JTextField("has focus, does F1 work OK?");
    JTextField tf2 = new JTextField("see if it works here, as well");
      table = new JTable();
      model = new DefaultTableModel(new String[][]{
                  {"1a","1b"}
                  ,{"2a","2b"}
                  ,{"3a","3b"}
          },new String[]{"col1","col2"});

      table.setModel(model);
    p.add(tf1); p.add(tf2);
     p.add(table);
    JFrame f = new JFrame();
    f.getContentPane().add(p);
    f.setSize(400,300);
    f.setLocationRelativeTo(null);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JButton btn=new JButton("delete");
      btn.addActionListener(new ActionListener(){

          public void actionPerformed(ActionEvent e) {
              model.removeRow(table.getSelectedRow());
          }
      });
     p.add(btn);
    KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
    Action action_F1 = new AbstractAction(){
      public void actionPerformed(ActionEvent e){
            model.removeRow(table.getSelectedRow());
      }
    };
    f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f1,"F1");
    f.getRootPane().getActionMap().put("F1", action_F1);
    f.setVisible(true);
  }
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
        new Testing().buildGUI();
      }
    });
  }
}