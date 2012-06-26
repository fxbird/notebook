package vetoselection;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


public interface VetoTreeSelectionListener extends TreeSelectionListener {
    /**
     * If any listener returns <code>false</code> the selection change is vetoed and not changed.
     * 
     * @param event The tree event of the selection change
     * @return <code>false</code> if the selection change should be vetoed, <code>true</code> otherwise.
     */
    boolean valueChanging(TreeSelectionEvent event);
}
