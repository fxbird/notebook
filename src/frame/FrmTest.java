/*
 * Created by JFormDesigner on Sat Oct 03 12:22:04 CST 2009
 */

package frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import other.Msg;

/**
 * @author Kurt Xu
 */
public class FrmTest extends JPanel {
    public FrmTest() {
        initComponents();
        cb.addItem("");
    }

    private void button1ActionPerformed(ActionEvent e) {
        Msg.showMsg(FrmTest.this, cb.getSelectedItem().toString());
    }

    public static void main(String[] args) {
        JFrame frame=new JFrame();
        frame.add(new FrmTest());
        frame.setSize(600,400);
        frame.setVisible(true);
    }

  

    private void cbMouseClicked(MouseEvent e) {
        System.out.println("clieck");
    }

  

    private void cbActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Kurt Xu
        cb = new JComboBox();
        button1 = new JButton();

        //======== this ========

        // JFormDesigner evaluation mark
        setBorder(new javax.swing.border.CompoundBorder(
            new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BOTTOM, new Font("Dialog", Font.BOLD, 12),
                Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

        setLayout(null);

        //---- cb ----
        cb.setModel(new DefaultComboBoxModel(new String[] {
            "1,1",
            "2,2",
            "3,3"
        }));
        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cbActionPerformed(e);
            }
        });
        cb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cbMouseClicked(e);
                cbMouseClicked(e);
            }
        });
        cb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            }
        });
        add(cb);
        cb.setBounds(125, 26, 205, cb.getPreferredSize().height);

        //---- button1 ----
        button1.setText("text");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
            }
        });
        add(button1);
        button1.setBounds(195, 175, 195, button1.getPreferredSize().height);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < getComponentCount(); i++) {
                Rectangle bounds = getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            setMinimumSize(preferredSize);
            setPreferredSize(preferredSize);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Kurt Xu
    private JComboBox cb;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
