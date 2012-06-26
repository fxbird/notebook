package vetoselection;

import javax.swing.event.TreeSelectionEvent;

public class VetoTreeSelectionAdapter implements VetoTreeSelectionListener {

    /**
     * By default allows any selection change.
     * 
     * {@inheritDoc}
     */
    public boolean valueChanging(TreeSelectionEvent event) {
        return true;
    }

    /**
     * By default does nothing when the selection changes.
     */
    public void valueChanged(TreeSelectionEvent event) {
        // do nothing
    }
}