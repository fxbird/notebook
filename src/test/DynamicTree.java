package test;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import java.awt.*;


public class DynamicTree extends JPanel{
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit=Toolkit.getDefaultToolkit();

    public DynamicTree() {
        super(new GridLayout(1,0));

        rootNode=new DefaultMutableTreeNode("Root Node");
        treeModel=new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());

        tree=new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        JScrollPane scrollPane=new JScrollPane(tree);
        add(scrollPane);
    }

    public void clear(){
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    public void removeCurrentNode(){
        TreePath currentSelection=tree.getSelectionPath();
        if (currentSelection!=null){
            DefaultMutableTreeNode currentnode=(DefaultMutableTreeNode)currentSelection.getLastPathComponent();
            MutableTreeNode parent=(MutableTreeNode)currentnode.getParent();
            if (parent!=null){
                treeModel.removeNodeFromParent(currentnode);
                return;
            }
        }

        toolkit.beep();
    }

    public DefaultMutableTreeNode addObject(Object child){
        DefaultMutableTreeNode parentNode=null;
        TreePath parentPath=tree.getSelectionPath();

        if (parentPath==null){
            parentNode=rootNode;
        }else{
            parentNode=(DefaultMutableTreeNode)parentPath.getLastPathComponent();
        }

        return addObject(parentNode,child,true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,Object child){
        return addObject(parent,child,false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,Object child,boolean shouldBeVisible){
        DefaultMutableTreeNode childNode=new DefaultMutableTreeNode(child);

        if (parent==null) parent=rootNode;

        treeModel.insertNodeInto(childNode,parent,parent.getChildCount());

        if (shouldBeVisible) tree.scrollPathToVisible(new TreePath(childNode.getPath()));

        return childNode;
    }
}

class MyTreeModelListener implements TreeModelListener{
    public void treeNodesChanged(TreeModelEvent e) {
        DefaultMutableTreeNode node;
        node=(DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
        int index=e.getChildIndices()[0];
        node=(DefaultMutableTreeNode)(node.getChildAt(index));
        System.out.println("The user has finished edition the node.");
        System.out.println("New value: "+node.getUserObject());
    }

    public void treeNodesInserted(TreeModelEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void treeNodesRemoved(TreeModelEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void treeStructureChanged(TreeModelEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
