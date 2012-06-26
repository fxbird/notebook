package frame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class FrmCheck {
    public static void main(String[] args) {
        JFrame frame = new SplitPaneFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}

class SplitPaneFrame extends JFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private Planet[] planets = {
            new Planet("Mercury", 2440, 0),
            new Planet("Venus", 6378, 0),
            new Planet("Earth", 6378, 1)
    };

    public SplitPaneFrame() {
        setSize(WIDTH, HEIGHT);

        final JList planetList = new JList(planets);
        final JLabel planetImage = new JLabel();
        final JTextArea description = new JTextArea();

        planetList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Planet value = (Planet) planetList.getSelectedValue();
                planetImage.setIcon(value.getImage());
                description.setText(value.getDescription());
            }
        });

        JSplitPane innerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, planetList, planetImage);
        innerPane.setContinuousLayout(true);
        innerPane.setOneTouchExpandable(true);

        JSplitPane outerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, innerPane, description);

        getContentPane().add(outerPane, BorderLayout.CENTER);
    }
}

class Planet {
    private String name;
    private double radius;
    private int moons;
    private ImageIcon image;

    public Planet(String n, double r, int m) {
        name = n;
        radius = r;
        moons = m;
        image = new ImageIcon("H:\\My Documents\\program\\java\\projects\\idea\\book2\\out\\production\\book2\\frame"+name + ".gif");
    }

    @Override
    public String toString() {
        return name;
    }

    public String getDescription() {
        return "Radius:" + radius + "\nMoons: " + moons + "\n";
    }

    public ImageIcon getImage() {
        return image;
    }
}
