package tree;

import bean.NoteType;
import bo.BookBO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import other.Msg;
import xdg.XdgUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class TypeTreeWrapper {
    private JTree tree;
    private JPanel panel;
    private JTextField tfKey;
    private JButton btnClear;
    private BookBO bo;
    private Component parentComp;
    private DefaultTreeModel model;
    private DefaultMutableTreeNode root;
    private final static Log log = LogFactory.getLog(TypeTreeWrapper.class);

    public TypeTreeWrapper(Component parentComp,BookBO bo) {
        this.bo = bo;
        this.parentComp=parentComp;

        NoteType t = new NoteType();
        t.setId(0);
        t.setName("All");
        List<NoteType> noteTypes = bo.getAllTypes();
        root = new DefaultMutableTreeNode(t);
        generateTree(root, noteTypes);

        //create tree
        model=new DefaultTreeModel(root);
        tree=new JTree();
        tree.setModel(model);
        tree.setEditable(true);
        tree.setCellEditor(new TypeNodeEditor(new JTextField(12)));
        tree.setDragEnabled(true);
        final MutuableBGColorRender render = new MutuableBGColorRender();
        tree.setCellRenderer(render);
        setMoveHandling();
        setPopupMenu();

        btnClear=new JButton("*");
        btnClear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                tfKey.setText("");
            }
        });

        tfKey=new JTextField(20);
        tfKey.getDocument().addDocumentListener(new DocumentListener(){
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            public void insertUpdate(DocumentEvent e) {
                search();
            }

            public void changedUpdate(DocumentEvent e) {
                search();
            }

            public void search(){
                String key=tfKey.getText();
                render.key=key;
                tree.repaint();
                if (key.length()==0) return;
                Enumeration nodes=root.breadthFirstEnumeration();
                while (nodes.hasMoreElements()){
                    DefaultMutableTreeNode node=(DefaultMutableTreeNode)nodes.nextElement();
                    DefaultMutableTreeNode parent=(DefaultMutableTreeNode)node.getParent();
                    NoteType t=(NoteType)node.getUserObject();
                    if (t.getName().toLowerCase().startsWith(key.toLowerCase()))
                        tree.expandPath(new TreePath(parent.getPath()));
                }
            }
        });
//        tfKey.setPreferredSize(new Dimension(300,20));

        JPanel pnlTop=new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.add(tfKey);
        pnlTop.add(btnClear);
        pnlTop.setBackground(Color.LIGHT_GRAY);
        
        panel=new JPanel(new BorderLayout());
        panel.add(pnlTop,BorderLayout.NORTH);
        panel.add(tree,BorderLayout.CENTER);
//        expandAll();

       
    }

    public void expandAll(){
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    public static void generateTree(DefaultMutableTreeNode node, List<NoteType> noteTypes) {

        NoteType parent = (NoteType) node.getUserObject();
        List<DefaultMutableTreeNode> sonNodeList = new ArrayList<DefaultMutableTreeNode>();
        for (NoteType t : noteTypes) {
            if (parent.getId().equals(t.getParentTypeNo())) {
                DefaultMutableTreeNode sonNode = new DefaultMutableTreeNode(t);
                node.add(sonNode);
                sonNodeList.add(sonNode);
            }
        }

        for (DefaultMutableTreeNode n : sonNodeList) {
            generateTree(n, noteTypes);
        }
    }

    public void setPopupMenu(){
       final JPopupMenu pop=new JPopupMenu();
         pop.add(new AbstractAction("Add"){
             public void actionPerformed(ActionEvent e) {
                 add();
             }
         });

         pop.add(new AbstractAction("Deep Delete") {
            public void actionPerformed(ActionEvent e) {
                deepDelete();
            }
        });

        pop.add(new AbstractAction("Logical Delete") {
            public void actionPerformed(ActionEvent e) {
                logicalDelete();
            }
        });



       tree.addKeyListener(new KeyAdapter(){
           public void keyPressed(KeyEvent e) {
               if (isNotSelecting()) return ;
               if (e.getKeyCode()==KeyEvent.VK_DELETE){
                   deepDelete();
               }
           }
       });

       tree.addMouseListener(new MouseAdapter(){
           public void mouseReleased(MouseEvent e) {
               if (!e.isPopupTrigger()) return;
               if (isNotSelecting()) return;
               pop.show(tree,e.getX(),e.getY());
           }
       });
    }

    public void setMoveHandling(){
        DoWhenMove dwm=new DoWhenMove(){
            public boolean handle(DefaultMutableTreeNode newParent, List<DefaultMutableTreeNode> movedNodes) {
               if (XdgUtil.showConfirm(parentComp,"Really change the parent test?")==1){
                   HashMap map=new HashMap();
                   map.put("parent",newParent);
                   map.put("nodes",movedNodes);
                   bo.updateParentType(map);
                   return true;
               }else{
                   return false;
               }
            }
        };

        try {
            tree.setTransferHandler(new MyJTreeTransferHandler(dwm));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void add() {
        NoteType parentNoteType =getSelectedType();
        DefaultMutableTreeNode parentNode=getSelectedNode();
        NoteType t=new NoteType();
        t.setParentTypeNo(parentNoteType.getId()==0?null: parentNoteType.getId());
        t.setName("");
        DefaultMutableTreeNode newNode=new DefaultMutableTreeNode(t);
        model.insertNodeInto(newNode,parentNode,parentNode.getChildCount());//note it
        getSelectedNode().add(newNode);

        TreePath path = new TreePath(newNode.getPath());
        tree.makeVisible(path);//note it
        tree.setSelectionPath(path);
        tree.scrollPathToVisible(path);
        tree.startEditingAtPath(path);
    }

    private void deepDelete() {
        NoteType noteType = getSelectedType();

        if (Msg.showConfirm(parentComp, "Really delete \"" + noteType + "\" and its descendant ?") == 1) {
            try {
               DefaultMutableTreeNode node=getSelectedNode();
                model.removeNodeFromParent(node);
                Enumeration e= node.breadthFirstEnumeration();
                while (e.hasMoreElements()){
                    DefaultMutableTreeNode n=(DefaultMutableTreeNode)e.nextElement();
                    NoteType t=(NoteType)n.getUserObject();
                    bo.deepDeleteType(t);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Msg.showError(parentComp, ex);
            }
        }
    }

    private void logicalDelete(){
       NoteType noteType = getSelectedType();

        if (Msg.showConfirm(parentComp, "Really delete \"" + noteType + "\" and its descendant logically ?") == 1) {
            try {
               DefaultMutableTreeNode node=getSelectedNode();
                model.removeNodeFromParent(node);
                Enumeration e= node.breadthFirstEnumeration();
                while (e.hasMoreElements()){
                    DefaultMutableTreeNode n=(DefaultMutableTreeNode)e.nextElement();
                    NoteType t=(NoteType)n.getUserObject();
                    bo.logicalDeleteType(t);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Msg.showError(parentComp, ex);
            }
        }
    }

    public NoteType getSelectedType(){
         DefaultMutableTreeNode node=(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
         NoteType t=(NoteType)node.getUserObject();
        return t;
    }

    public DefaultMutableTreeNode getSelectedNode(){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        return node;

    }

    public void selectNodeByType(Integer typeNo){
       Enumeration<DefaultMutableTreeNode> e=root.breadthFirstEnumeration();
        log.debug("type id wanted to highlight : "+typeNo);
        while (e.hasMoreElements()){
           DefaultMutableTreeNode node=(DefaultMutableTreeNode)e.nextElement();
           NoteType noteType =(NoteType)node.getUserObject();
           log.debug("noteType : id = "+ noteType.getId()+", name = "+ noteType.getName());
           if (noteType.getId().equals(typeNo)){
               tree.clearSelection();
               tree.addSelectionPath(new TreePath(node.getPath()));
               break;
           }
        }
    }

    public boolean isSelecting(){
        return getSelectedNode()!=null;
    }

    public boolean isNotSelecting(){
        return !isSelecting();
    }

    public void removeNode(DefaultMutableTreeNode node){
        model.removeNodeFromParent(node);
    }

    public void removeSelectedNode(){
        if (isNotSelecting()) return;
        model.removeNodeFromParent(getSelectedNode());
        
    }

    public JTree getTree() {
        return tree;
    }

    public JPanel getPanel() {
        return panel;
    }

    class TypeTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {

        }

        public void treeNodesInserted(TreeModelEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void treeNodesRemoved(TreeModelEvent e) {

        }

        public void treeStructureChanged(TreeModelEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class TypeNodeEditor extends DefaultCellEditor{
        private JTextField tf;
        private NoteType actualObj;
        TypeNodeEditor(JTextField tf){
           super(tf);
           this.tf=tf;
        }

        public boolean stopCellEditing() {
           if (XdgUtil.isEmpty(tf.getText())) return false;
           NoteType t=getSelectedType();
           String oldName=t.getName();
           t.setName(tf.getText().trim());
           if (bo.isTypeExist(t)){
              XdgUtil.showMsg(parentComp, String.format("%s has existed, maybe it's not visiable!",tf.getText()));
               t.setName(oldName);
               super.cancelCellEditing();

              return false; 
           }

           t.setName(tf.getText());
           if (t.getId()==null){
               bo.createType(t);
               t.setId(bo.getMaxTypeNo());
           }else{
               bo.editType(t);
           }

           actualObj=t;
           return super.stopCellEditing();
        }

        public Object getCellEditorValue() {
            return actualObj;
        }
    }
}

class MutuableBGColorRender extends DefaultTreeCellRenderer{
    public String key;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JComponent c= (JComponent)super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);    //To change body of overridden methods use File | Settings | File Templates.

        if (isSelected) {
            c.setOpaque(false);
            c.setForeground(getTextSelectionColor());
        } else {
            c.setOpaque(true);
            if (key != null && !key.isEmpty()
                    && value.toString().toLowerCase().contains(key.toLowerCase())) {
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

