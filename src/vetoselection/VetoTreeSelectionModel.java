package vetoselection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class VetoTreeSelectionModel extends DefaultTreeSelectionModel {

    // No state to serialize.
    private static final long serialVersionUID = -5970064041289351778L;

//    public void clearSelection() {
//        if (2>1) {
//            super.clearSelection();
//            return;
//        }
//        TreePath[] paths = getSelectionPaths();
//        boolean[] isNew = new boolean[paths.length];
//        TreePath lead = getLeadSelectionPath();
//        if (checkNotVetoed(paths, isNew, lead, null)) {
//            super.clearSelection();
//        }
//    }

    public void addSelectionPaths(TreePath[] paths) {
        if (paths != null && paths.length > 0) {
            if (selectionMode == TreeSelectionModel.SINGLE_TREE_SELECTION) {
                setSelectionPaths(paths);
            } else if (selectionMode == TreeSelectionModel.
                    CONTIGUOUS_TREE_SELECTION && !canPathsBeAdded(paths)) {
                if (arePathsContiguous(paths)) {
                    setSelectionPaths(paths);
                } else {
                    TreePath[] newPaths = new TreePath[1];

                    newPaths[0] = paths[0];
                    setSelectionPaths(newPaths);
                }
            } else {
                List<TreePath> currentPaths = getSelectedPaths();
                Set<TreePath> addedPaths = new LinkedHashSet<TreePath>(Arrays.asList(paths));
                addedPaths.removeAll(currentPaths);

                if (!addedPaths.isEmpty()) {
                    int size = addedPaths.size();
                    boolean[] isNew = new boolean[size];
                    Arrays.fill(isNew, true);
                    TreePath[] added = addedPaths.toArray(new TreePath[size]);
                    TreePath oldLead = getLeadSelectionPath();
                    TreePath newLead = added[added.length - 1];
                    if (checkNotVetoed(added, isNew, oldLead, newLead)) {
                        super.addSelectionPaths(paths);
                    }
                }
            }
        }
    }

    @Override
    public void removeSelectionPaths(TreePath[] paths) {
        if (paths != null && paths.length > 0) {
            List<TreePath> currentPaths = new ArrayList<TreePath>(getSelectedPaths());
            Set<TreePath> removedPaths = new LinkedHashSet<TreePath>(Arrays.asList(paths));
            removedPaths.retainAll(currentPaths);
            currentPaths.removeAll(removedPaths);
            if (!removedPaths.isEmpty()) {
                int size = removedPaths.size();
                boolean[] isNew = new boolean[size];
                TreePath[] removed = removedPaths.toArray(new TreePath[size]);
                TreePath oldLead = getLeadSelectionPath();
                TreePath newLead = oldLead;
                if (!currentPaths.contains(oldLead)) {
                    if (currentPaths.isEmpty()) {
                        newLead = null;
                    } else {
                        newLead = currentPaths.get(currentPaths.size() - 1);
                    }
                }
                if (checkNotVetoed(removed, isNew, oldLead, newLead)) {
                    super.removeSelectionPaths(paths);
                }
            }
        }
    }

    @Override
    public void setSelectionPaths(TreePath[] paths) {
        if (paths == null || paths.length == 0) {
            clearSelection();
        } else {
            List<TreePath> currentPaths = getSelectedPaths();
            if (currentPaths.isEmpty()) {
                addSelectionPaths(paths);
            } else {
                int newCount = paths.length;
                if (selectionMode == TreeSelectionModel.SINGLE_TREE_SELECTION) {
                    // If single selection and more than one path, only allow first.
                    if (newCount > 1) {
                        paths = new TreePath[]{paths[0]};
                        newCount = 1;
                    }
                } else if (selectionMode == TreeSelectionModel.CONTIGUOUS_TREE_SELECTION) {
                    // If contiguous selection and paths aren't contiguous, only select the first path item.
                    if (newCount > 0 && !arePathsContiguous(paths)) {
                        paths = new TreePath[]{paths[0]};
                        newCount = 1;
                    }
                }

                Set<TreePath> changedPaths = new LinkedHashSet<TreePath>(Arrays.asList(paths));
                List<TreePath> removedPaths = new ArrayList<TreePath>(currentPaths);
                removedPaths.removeAll(changedPaths);
                changedPaths.addAll(removedPaths);

                int size = changedPaths.size();
                TreePath[] changed = changedPaths.toArray(new TreePath[size]);
                boolean[] isNew = new boolean[size];
                for (int i = 0; i < size; i++) {
                    isNew[i] = !currentPaths.contains(changed[i]);
                }
                TreePath oldLead = getLeadSelectionPath();
                TreePath newLead = paths[paths.length - 1];
                if (checkNotVetoed(changed, isNew, oldLead, newLead)) {
                    super.setSelectionPaths(paths);
                }
            }
        }
    }

    public List<TreePath> getSelectedPaths() {
        TreePath[] paths = getSelectionPaths();
        if (paths != null) {
            return Arrays.asList(paths);
        } else {
            return Collections.emptyList();
        }
    }

    protected boolean checkNotVetoed(TreePath[] paths, boolean[] isNew, TreePath oldLead, TreePath newLead) {
        return fireValueChanging(new TreeSelectionEvent(this, paths, isNew, oldLead, newLead));
    }

    /**
     * If the listener is an instance of VetoTreeSelectionListener it will be automatically added as such as well.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public void addTreeSelectionListener(TreeSelectionListener listener) {
        if (listener instanceof VetoTreeSelectionListener) {
            addVetoTreeSelectionListener((VetoTreeSelectionListener) listener);
        }
        super.addTreeSelectionListener(listener);
    }

    /**
     * If the listener is an instance of VetoTreeSelectionListener it will be automatically removed as such as well.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public void removeTreeSelectionListener(TreeSelectionListener listener) {
        if (listener instanceof VetoTreeSelectionListener) {
            removeVetoTreeSelectionListener((VetoTreeSelectionListener) listener);
        }
        super.removeTreeSelectionListener(listener);
    }

    public void addVetoTreeSelectionListener(VetoTreeSelectionListener listener) {
        listenerList.add(VetoTreeSelectionListener.class, listener);
    }

    public void removeVetoTreeSelectionListener(VetoTreeSelectionListener listener) {
        listenerList.remove(VetoTreeSelectionListener.class, listener);
    }

    /**
     * Notifies all listeners that are registered for
     * tree veto selection events on this object.
     *
     * @see #addVetoTreeSelectionListener
     */
    protected boolean fireValueChanging(TreeSelectionEvent e) {
        for (VetoTreeSelectionListener listener : listenerList.getListeners(VetoTreeSelectionListener.class)) {
            if (!listener.valueChanging(e)) {
                return false;
            }
        }
        return true;
    }

}
