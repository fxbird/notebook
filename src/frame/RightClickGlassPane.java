package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RightClickGlassPane extends JComponent implements MouseListener, MouseMotionListener {

    private JPopupMenu popup;
    private Container contentPane;
    private static JTextField tf;

    public RightClickGlassPane(Container contentPane, JPopupMenu menu) {
        addMouseListener(this);
        addMouseMotionListener(this);
        this.contentPane = contentPane;
        popup = menu;
    }

    // draw some text just so we know the glass pane
    // is installed and visible
    public void paint(Graphics g) {
        //g.drawString("I'm a glass pane",50,50);
    }


    // catch all mouse events and redispatch them
    public void mouseMoved(MouseEvent e) {
        redispatchMouseEvent(e, false);
    }
    public void mouseDragged(MouseEvent e) {
        redispatchMouseEvent(e, false);
    }
    public void mouseClicked(MouseEvent e) {
        Point containerPoint = SwingUtilities.convertPoint(this,
                e.getPoint(),contentPane);

            // find the component that under this point
            Component component = SwingUtilities.getDeepestComponentAt(
                        contentPane,
                        containerPoint.x,
                        containerPoint.y);
            if (component==tf){
                JOptionPane.showConfirmDialog(null,"u clicked textfield");
                return;
            }
    }
    public void mouseEntered(MouseEvent e) {
        redispatchMouseEvent(e, false);
    }
    public void mouseExited(MouseEvent e) {
        redispatchMouseEvent(e, false);
    }
    public void mousePressed(MouseEvent e) {
        redispatchMouseEvent(e, false);
    }
    public void mouseReleased(MouseEvent e) {
        redispatchMouseEvent(e, false);
    }


    private void redispatchMouseEvent(MouseEvent e, boolean repaint) {

        // if it's a popup
        if(e.isPopupTrigger()) {
            // show the popup and return
            popup.show(e.getComponent(), e.getX(), e.getY());
        } else {
            // since it's not a popup we need to redispatch it.

            // getMappedType the mouse click point relative to the content pane
            Point containerPoint = SwingUtilities.convertPoint(this,
                e.getPoint(),contentPane);

            // find the component that under this point
            Component component = SwingUtilities.getDeepestComponentAt(
                        contentPane,
                        containerPoint.x,
                        containerPoint.y);
//            if (component==tf){
//                JOptionPane.showConfirmDialog(null,"u clicked textfield");
//                return;
//            }

            // return if nothing was found
            if (component == null) {
                return;
            }

            // convert point relative to the target component
            Point componentPoint = SwingUtilities.convertPoint(
                this,
                e.getPoint(),
                component);

            // redispatch the event
            component.dispatchEvent(new MouseEvent(component,
                e.getID(),
                e.getWhen(),
                e.getModifiers(),
                componentPoint.x,
                componentPoint.y,
                e.getClickCount(),
                e.isPopupTrigger()));
        }
    }



    public static void main(String[] args) {
        // create a frame with some components in it
        JFrame frame = new JFrame("Right Click ImportFromAcc2Fb");
        JButton button = new JButton("this is a button");
        tf = new JTextField("this is a textfield");
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(tf);
        frame.getContentPane().add(panel);


        JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem("Dogs"));
        popup.add(new JMenuItem("Cats"));
        popup.add(new JMenuItem("Mass Hysteria"));

        // create the right click glass pane.
        Component rc = new RightClickGlassPane(frame.getContentPane(),popup);
        // set as glasspane and make it visible
frame.setGlassPane(rc);
        rc.setVisible(true);

        // pack and show the frame
        frame.pack();
        frame.setSize(400,200);
        frame.show();
    }

    // utiltity function
    public static void p(String str) {
        System.out.println(str);
    }

}

