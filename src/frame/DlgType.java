/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgType.java
 *
 * Created on Oct 2, 2010, 8:39:04 PM
 */

package frame;

import action.TypeCallback;
import bean.Note;
import bo.BookBO;
import tree.TypeTree;
import xdg.XdgUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author fxbird
 */
public class DlgType extends javax.swing.JDialog {
   private List<Note> notes;
   private TypeTree tree;
    private BookBO bo;
    private TypeCallback callback;
    /** Creates new form DlgType */
    public DlgType(java.awt.Frame parent, boolean modal, List<Note> notes, BookBO bo) {
        super(parent, modal);
        this.notes=notes;
        this.bo=bo;
        tree=new TypeTree(parent,bo);
        initComponents();
        addTree();
        setButtonListener();
    }

    public DlgType(java.awt.Frame parent, boolean modal, BookBO bo,TypeCallback callback) {
         super(parent, modal);
        this.bo=bo;
        tree=new TypeTree(parent,bo);
        initComponents();
        addTree();
        setButtonListener();
        this.callback=callback;
    }

    private void setButtonListener() {
        btnOK.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (tree.isNotSelecting()){
                    XdgUtil.showMsg(DlgType.this,"You don't select test");
                    return;
                }
                 //todo change parent type modification
                callback.handle(tree.getSelectedType());
                XdgUtil.showMsg(null,"Complete handling");
            }
        });

        btnClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                DlgType.this.dispose();
            }
        });
    }

    private void addTree() {
        jsp.setViewportView(tree);
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jsp = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnOK.setText("Complete");
        jPanel1.add(btnOK);

        btnClose.setText("Close");
        jPanel1.add(btnClose);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(jsp, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOK;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jsp;
    // End of variables declaration//GEN-END:variables

}
