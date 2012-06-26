package test;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


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
