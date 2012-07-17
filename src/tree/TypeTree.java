package tree;

import bean.NoteType;
import bo.BookBO;
import other.Msg;
import xdg.XdgUtil;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class TypeTree extends JTree{
//    private JTree tree;
    private BookBO bo;
    private Component parentComp;
    private DefaultTreeModel model;
    private DefaultMutableTreeNode root;

    public TypeTree(Component parentComp,BookBO bo) {
        this.bo = bo;
        this.parentComp=parentComp;
        NoteType t = new NoteType();
        t.setId(0);
        t.setName("All");
        List<NoteType> noteTypes = bo.getAllTypes();
        root = new DefaultMutableTreeNode(t);
        generateTree(root, noteTypes);

        model=new DefaultTreeModel(root);
        setModel(model);
        setEditable(true);
        setCellEditor(new TypeNodeEditor(new JTextField(12)));
        setDragEnabled(true);
        setMoveHandling();
        setPopupMenu();
//        expandAll();

       
    }

    public void expandAll(){
        for (int i = 0; i < this.getRowCount(); i++) {
            this.expandRow(i);
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



       this.addKeyListener(new KeyAdapter(){
           public void keyPressed(KeyEvent e) {
               if (isNotSelecting()) return ;
               if (e.getKeyCode()==KeyEvent.VK_DELETE){
                   deepDelete();
               }
           }
       });

       this.addMouseListener(new MouseAdapter(){
           public void mouseReleased(MouseEvent e) {
               if (!e.isPopupTrigger()) return;
               if (isNotSelecting()) return;
               pop.show(TypeTree.this,e.getX(),e.getY());
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
            this.setTransferHandler(new MyJTreeTransferHandler(dwm));
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
        this.makeVisible(path);//note it
        this.setSelectionPath(path);
        this.scrollPathToVisible(path);
        this.startEditingAtPath(path);
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
         DefaultMutableTreeNode node=(DefaultMutableTreeNode)this.getLastSelectedPathComponent();
         NoteType t=(NoteType)node.getUserObject();
        return t;
    }

    public DefaultMutableTreeNode getSelectedNode(){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
        return node;

    }

    public void selectNodeByType(Integer typeNo){
       Enumeration<DefaultMutableTreeNode> e=root.breadthFirstEnumeration();
        while (e.hasMoreElements()){
           DefaultMutableTreeNode node=(DefaultMutableTreeNode)e.nextElement();
           NoteType noteType =(NoteType)node.getUserObject();
           if (noteType.getId().equals(typeNo)){
               clearSelection();
               this.addSelectionPath(new TreePath(node.getPath()));
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

