package vetoselection;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;


public class TestVetoTreeSelectionModel {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                final JTree tree = new JTree();
                VetoTreeSelectionModel selectionModel = new VetoTreeSelectionModel();
                selectionModel.addTreeSelectionListener(new VetoTreeSelectionAdapter() {
                    @Override
                    public boolean valueChanging(TreeSelectionEvent event) {
                        return JOptionPane.showConfirmDialog(tree, 
                                "Should change selection?\n\n" + eventToString(event),
                                "Tree selection change", JOptionPane.OK_CANCEL_OPTION)
                                    == JOptionPane.OK_OPTION;
                    }
                    
                    @Override
                    public void valueChanged(TreeSelectionEvent event) {
//                        JOptionPane.showMessageDialog(tree, 
//                                "Selection changed.\n\n" + eventToString(event));
                    }

                    private String eventToString(TreeSelectionEvent event) {
                        StringBuilder builder = new StringBuilder();
                        for(TreePath path : event.getPaths()) {
                            if(event.isAddedPath(path)) {
                                builder.append("Added: ");
                            }
                            else {
                                builder.append("Removed: ");
                            }
                            builder.append(path);
                            builder.append('\n');
                        }
                        builder.append("Old lead: ");
                        builder.append(event.getOldLeadSelectionPath());
                        builder.append("\nNew lead: ");
                        builder.append(event.getNewLeadSelectionPath());
                        return builder.toString();
                    }
                });
                
                tree.setSelectionModel(selectionModel);
                
                JFrame frame = new JFrame("Main");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.getContentPane().add(new JScrollPane(tree));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
