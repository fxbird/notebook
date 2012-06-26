package tree;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fxbird
 * Date: Oct 2, 2010
 * Time: 6:03:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyJTreeTransferHandler extends TransferHandler {
    private DoWhenMove dwm;
    public MyJTreeTransferHandler(DoWhenMove dwm) throws ClassNotFoundException {
       this.dwm=dwm;
    }

    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree=(JTree)c;
        TreePath[] paths=tree.getSelectionPaths();
        ArrayList nodes=new ArrayList();
        for (TreePath path:paths){
            nodes.add(path.getLastPathComponent());
        }

        return new JTreeTransferable(nodes);
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
//        if (action!=MOVE) return;
//        try {
//            TreePath[] paths=(TreePath[])data.getTransferData(JTreeTransferable.FLAVOR);
//            JTree tree=(JTree)source;
//            DefaultTreeModel model=(DefaultTreeModel)tree.getModel();
//
//            for (TreePath path:paths){
//
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
////                model.nodesWereRemoved(node.getParent(),new int[]{ 2},new DefaultMutableTreeNode[]{node});
//                model.nodeStructureChanged(node);
//                model.removeNodeFromParent(node);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (support.isDataFlavorSupported(JTreeTransferable.FLAVOR)){
            if (support.getDropAction()==MOVE) return true;
        }

        return false;
    }

    @Override
    public boolean importData(TransferSupport support) {
        JTree tree=(JTree)support.getComponent();
        DefaultTreeModel model=(DefaultTreeModel)tree.getModel();
        Transferable transfer=support.getTransferable();
        try {
            List<DefaultMutableTreeNode> nodes=(List<DefaultMutableTreeNode>) transfer.getTransferData(JTreeTransferable.FLAVOR);

            JTree.DropLocation location= (JTree.DropLocation)support.getDropLocation();
            DefaultMutableTreeNode newParent =(DefaultMutableTreeNode)location.getPath().getLastPathComponent();

            if (dwm.handle(newParent,nodes)==false) return false;

            for (DefaultMutableTreeNode node:nodes){
                model.removeNodeFromParent(node);
                model.insertNodeInto(node,
                        newParent,
                        newParent.getChildCount());
            }

        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }
}

class JTreeTransferable implements Transferable {
        public  static DataFlavor FLAVOR =null;

        private List<DefaultMutableTreeNode> nodes;

        JTreeTransferable( ArrayList<DefaultMutableTreeNode> nodes ) {
            try{
                 FLAVOR= new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
                              ";class=\"" + ArrayList.class.getName() + "\"");
                this.nodes=nodes;
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
             return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{FLAVOR};
        }

        public boolean isDataFlavorSupported(DataFlavor flv) {
            return FLAVOR.equals(flv);
        }
    }

interface DoWhenMove{
    boolean handle(DefaultMutableTreeNode newParent, List<DefaultMutableTreeNode> movedNodes);
}