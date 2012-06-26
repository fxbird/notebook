package test;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;


public class ClassTree  {
    public static void main(String[] args) {
       EventQueue.invokeLater(new Runnable(){
           public void run() {
               JFrame frame=new ClassTreeFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
           }
       });
    }
}

class ClassTreeFrame extends JFrame{
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private JTree tree;
    private JTextField textField;
    private JTextArea textArea;
    private static final int DEFAULT_WIDTH=400;
    private static final int DEFAULT_HEIGHT=300;
    ClassTreeFrame() throws HeadlessException {
        setTitle("class Tree");
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);

        root=new DefaultMutableTreeNode(Object.class);
        model=new DefaultTreeModel(root);
        tree=new JTree(model);

        addClass(getClass());

        ClassNameTreeCellRenderer render=new ClassNameTreeCellRenderer();
        String path="h:\\My Documents\\program\\source\\Core Java -8th\\v2ch06\\ClassTree\\";
        render.setClosedIcon(new ImageIcon(path+"red-ball.gif"));
        render.setOpenIcon(new ImageIcon(path+"yellow-ball.gif"));
        render.setLeafIcon(new ImageIcon(path+"blue-ball.gif"));
        tree.setCellRenderer(render);

        tree.addTreeSelectionListener(new TreeSelectionListener(){
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path=tree.getSelectionPath();
                if (path==null)return;
                DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode)path.getLastPathComponent();
                Class<?> c=(Class<?>)selectedNode.getUserObject();
                String description=getFieldDescription(c);
                textArea.setText(description);
            }
        });

        textArea=new JTextArea();

        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(1,2));
        panel.add(new JScrollPane(tree));
        panel.add(new JScrollPane(textArea));

        add(panel,BorderLayout.CENTER);

        addTextField() ;
    }

    public void addTextField(){
        JPanel panel=new JPanel();
        ActionListener addListener=new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    String text=textField.getText();
                    addClass(Class.forName(text));
                    textField.setText("");
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };

        textField=new JTextField(20);
        textField.addActionListener(addListener);
        panel.add(textField);

        JButton addButton=new JButton("Add");
        addButton.addActionListener(addListener);
        panel.add(addButton);

        add(panel,BorderLayout.SOUTH);
    }

    public DefaultMutableTreeNode findUserObject(Object obj){
        Enumeration<TreeNode> e=(Enumeration<TreeNode>)root.breadthFirstEnumeration();
        while (e.hasMoreElements()){
            DefaultMutableTreeNode node=(DefaultMutableTreeNode)e.nextElement();
            if (node.getUserObject().equals(obj)) return node;
        }

        return null;
    }

    public DefaultMutableTreeNode addClass(Class<?> c){
        if (c.isInterface() || c.isPrimitive()) return null;
        DefaultMutableTreeNode node=findUserObject(c);

        if (node!=null) return node;

        Class<?> s=c.getSuperclass();

        DefaultMutableTreeNode parent;
        if (s==null) parent=root;
        else parent=addClass(s);

        DefaultMutableTreeNode newNode=new DefaultMutableTreeNode(c);
        model.insertNodeInto(newNode,parent,parent.getChildCount());

        TreePath path=new TreePath(model.getPathToRoot(newNode));
        tree.makeVisible(path);

        return newNode;
    }

    public static String getFieldDescription(Class<?> c){
        StringBuilder r=new StringBuilder();
        Field[] fields=c.getDeclaredFields();
        for(Field f:fields){
            if ((f.getModifiers() & Modifier.STATIC)!=0) r.append("static ");
            r.append(f.getType().getName());
            r.append(" ");
            r.append(f.getName());
            r.append("\n");
        }

        return r.toString();
    }
}

class ClassNameTreeCellRenderer extends DefaultTreeCellRenderer{
    private Font plainFont;
    private Font italicFont;
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);

        DefaultMutableTreeNode node=(DefaultMutableTreeNode)value;
        Class<?> c=(Class<?>)node.getUserObject();

        if (plainFont==null){
            plainFont=getFont();
            if (plainFont!=null) italicFont=plainFont.deriveFont(Font.ITALIC);
        }

        if ((c.getModifiers() & Modifier.ABSTRACT)==0) setFont(plainFont);
        else setFont(italicFont);

        return this;
    }
}
