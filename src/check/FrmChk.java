/*
 * Created by JFormDesigner on Wed Feb 17 16:20:15 CST 2010
 */

package check;

import java.awt.*;
import javax.swing.*;


/**
 * @author Kurt Xu
 */
public class FrmChk extends JFrame {
    public FrmChk() {
        initComponents();
    }

    public static void main(String[] args) {
        JFrame frame=new FrmChk();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Kurt Xu
        splitPane1 = new JSplitPane();
        scrollPane1 = new JScrollPane();
        list1 = new JList();
        scrollPane2 = new JScrollPane();
        textArea1 = new JTextArea();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== splitPane1 ========
        {
            splitPane1.setOneTouchExpandable(true);

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(list1);
            }
            splitPane1.setLeftComponent(scrollPane1);

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(textArea1);
            }
            splitPane1.setRightComponent(scrollPane2);
        }
        contentPane.add(splitPane1);
        splitPane1.setBounds(5, 100, 370, 275);

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
    // Generated using JFormDesigner Evaluation license - Kurt Xu
    private JSplitPane splitPane1;
    private JScrollPane scrollPane1;
    private JList list1;
    private JScrollPane scrollPane2;
    private JTextArea textArea1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
