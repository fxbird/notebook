package test;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class HighlightTreeNodeTest {
    private final JTree tree = new JTree();
    private final JTextField field = new JTextField("foo");
    private final MyTreeCellRenderer renderer =
            new MyTreeCellRenderer(tree.getCellRenderer());

    public JComponent makeUI() {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            public void removeUpdate(DocumentEvent e) {
                search();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        tree.setCellRenderer(renderer);
        renderer.key = field.getText();
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        p.add(field, BorderLayout.NORTH);
        p.add(new JScrollPane(tree));
        return p;
    }

    private void search() {
        String key = field.getText();
        renderer.key = key;
        collapseAll();
        if (key.isEmpty()) return;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration nodes = root.breadthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            if (node.getUserObject().toString().toLowerCase().startsWith(key.toLowerCase()))
                if (parent!=null) tree.expandPath(new TreePath(parent.getPath()));
        }

        tree.repaint();
    }

    private void collapseAll() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration nodes = root.breadthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
            if (!node.isLeaf()) tree.collapsePath(new TreePath(node.getPath()));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.getContentPane().add(new HighlightTreeNodeTest().makeUI());
        f.setSize(320, 240);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

class MyTreeCellRenderer extends DefaultTreeCellRenderer {
    private final TreeCellRenderer renderer;
    public String key;

    public MyTreeCellRenderer(TreeCellRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean isSelected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        JComponent c = (JComponent) renderer.getTreeCellRendererComponent(
                tree, value, isSelected, expanded, leaf, row, hasFocus);
        if (isSelected) {
            c.setOpaque(false);
            c.setForeground(getTextSelectionColor());
        } else {
            c.setOpaque(true);
            if (key!=null && !key.isEmpty() &&
                    value.toString().toLowerCase().startsWith(key.toLowerCase())) {
                c.setForeground(getTextNonSelectionColor());
                c.setBackground(Color.YELLOW);
            } else {
                c.setForeground(getTextNonSelectionColor());
                c.setBackground(getBackgroundNonSelectionColor());
            }
        }
        return c;
    }
}
