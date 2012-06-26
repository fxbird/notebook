/*
 * Created by JFormDesigner on Sun Jul 20 10:49:37 CST 2008
 */

package frame;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 * @author fxasdf sadfsdaf
 */
public class Check extends JFrame {
    private DefaultTableModel model;
    public Check() {
        initComponents();

         model=new DefaultTableModel(new String[][]{
                {"1a","1b"}
                ,{"2a","2b"}
                ,{"3a","3b"}
        },new String[]{"col1","col2"});

        table1.setModel(model);

        concreteShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0,false),new AbstractAction(){
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null,"hello");
                    }
                });
        
    }

    public void concreteShortcut(KeyStroke ks, Action action) {
        JRootPane rp=  this.getRootPane();
        String mapKey = new Random().nextInt() + "";
        InputMap imap = rp.getInputMap();
//        imap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0,false));
//        imap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0,true));
        ActionMap amap = rp.getActionMap();
        imap.put(ks, mapKey);
        amap.put(mapKey, action);
    }

    public static void main(String[] args) {
        Check frame=new Check();
        frame.setSize(600,400);
        frame.setVisible(true);
    }

    private void button1ActionPerformed(ActionEvent e) {
       model.addRow(new String[]{"",""});
    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - fxasdf sadfsdaf
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        button1 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(table1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(15, 0, 432, 314);

        //---- button1 ----
        button1.setText("text");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
            }
        });
        contentPane.add(button1);
        button1.setBounds(140, 355, 120, button1.getPreferredSize().height);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - fxasdf sadfsdaf
    private JScrollPane scrollPane1;
    private JTable table1;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
