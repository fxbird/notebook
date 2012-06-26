package test;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * @author helloworld922
 *         <p/>
 * @version 1.0
 *          <p/>
 *          copyright 2010 <br>
 *          <p/>
 *          You are welcome to use/modify this code for any purposes you want so
 *          long as credit is given to me.
 */
public class JTreeTransferHandler extends TransferHandler {
    /**
     * Using model models allows us to add/remove nodes from a model and pass the
     * appropriate messages.
     */
    protected DefaultTreeModel model;
    /**
     *
     */
    private static final long serialVersionUID = -6851440217837011463L;

    /**
     * Creates a JTreeTransferHandler to handle a certain model. Note that this
     * constructor does NOT set this transfer handler to be that model's transfer
     * handler, you must still add it manually.
     *
     * @param tree
     */
    public JTreeTransferHandler(DnDJTree tree) {
        super();
        this.model = (DefaultTreeModel) tree.getModel();
    }

    /**
     * @param c
     * @return
     */
    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    /**
     * @param c
     * @return null if no nodes were selected, or this transfer handler was not
     *         added to a DnDJTree. I don't think it's possible because of the
     *         constructor layout, but one more layer of safety doesn't matter.
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof DnDJTree) {
            return new DnDTreeList(((DnDJTree) c).getSelection());
        } else {
            return null;
        }
    }

    /**
     * @param c
     * @param t
     * @param action
     */
    @Override
    protected void exportDone(JComponent c, Transferable t, int action) {
        System.out.println("in exportDone");
        if (action == TransferHandler.MOVE) {
            // we need to remove items imported from the appropriate source.
            try {
                // get back the list of items that were transfered
                ArrayList<TreePath> list = ((DnDTreeList) t
                        .getTransferData(DnDTreeList.DnDTreeList_FLAVOR)).getNodes();
                for (int i = 0; i < list.size(); i++) {
                    // remove them
                    DnDNode oldNode = (DnDNode) list.get(i).getLastPathComponent();
                    this.model.removeNodeFromParent(oldNode);
                }
            }
            catch (UnsupportedFlavorException exception) {
                // for debugging purposes (and to make the compiler happy). In
                // theory, this shouldn't be reached.
                exception.printStackTrace();
            }
            catch (IOException exception) {
                // for debugging purposes (and to make the compiler happy). In
                // theory, this shouldn't be reached.
                exception.printStackTrace();
            }
        }
    }

    /**
     * @param supp
     * @return
     */
    @Override
    public boolean canImport(TransferSupport supp) {
        // Setup so we can always see what it is we are dropping onto.
        supp.setShowDropLocation(true);
        if (supp.isDataFlavorSupported(DnDTreeList.DnDTreeList_FLAVOR)) {
            // at the moment, only allow us to import list of DnDNodes

            // Fetch the drop path
            TreePath dropPath = ((JTree.DropLocation) supp.getDropLocation()).getPath();
            if (dropPath == null) {
                // Debugging a few anomalies with dropPath being null. In the
                // future hopefully this will get removed.
                System.out.println("Drop path somehow came out null");
                return false;
            }
            // Determine whether we accept the location
            if (dropPath.getLastPathComponent() instanceof DnDNode) {
                // only allow us to drop onto a DnDNode
                try {
                    // using the node-defined checker, see if that node will
                    // accept
                    // every selected node as a child.
                    DnDNode parent = (DnDNode) dropPath.getLastPathComponent();
                    ArrayList<TreePath> list = ((DnDTreeList) supp.getTransferable()
                            .getTransferData(DnDTreeList.DnDTreeList_FLAVOR)).getNodes();
                    for (int i = 0; i < list.size(); i++) {
                        if (parent.getAddIndex((DnDNode) list.get(i).getLastPathComponent()) < 0) {
                            return false;
                        }
                    }

                    return true;
                }
                catch (UnsupportedFlavorException exception) {
                    // Don't allow dropping of other data types. As of right
                    // now,
                    // only DnDNode_FLAVOR and DnDTreeList_FLAVOR are supported.
                    exception.printStackTrace();
                }
                catch (IOException exception) {
                    // to make the compiler happy.
                    exception.printStackTrace();
                }
            }
        }
        // something prevented this import from going forward
        return false;
    }

    /**
     * @param supp
     * @return
     */
    @Override
    public boolean importData(TransferSupport supp) {
        System.out.println("in importData");
        if (this.canImport(supp)) {

            try {
                // Fetch the data to transfer
                Transferable t = supp.getTransferable();
                ArrayList<TreePath> list = ((DnDTreeList) t
                        .getTransferData(DnDTreeList.DnDTreeList_FLAVOR)).getNodes();
                // Fetch the drop location
                TreePath loc = ((JTree.DropLocation) supp.getDropLocation()).getPath();
                // Insert the data at this location
                for (int i = 0; i < list.size(); i++) {
                    DnDNode newNode = (DnDNode) list.get(i).getLastPathComponent();
                    this.model.insertNodeInto(newNode,
                            (DnDNode) loc.getLastPathComponent(), ((DnDNode) loc
                                    .getLastPathComponent()).getAddIndex((DnDNode) list.get(i)
                                    .getLastPathComponent()));
                }
                // success!
                return true;
            }
            catch (UnsupportedFlavorException e) {
                // In theory, this shouldn't be reached because we already
                // checked to make sure imports were valid.
                e.printStackTrace();
            }
            catch (IOException e) {
                // In theory, this shouldn't be reached because we already
                // checked to make sure imports were valid.
                e.printStackTrace();
            }
        }
        // import isn't allowed at this time.
		return false;
	}
}
