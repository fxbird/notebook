/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package frame;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ButtonTest extends JFrame {
 
	public ButtonTest() {
		JButton button1 = new JButton("Hello");
		JButton button2 = new JButton("World");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
//		panel.add(Box.createGlue());
		panel.add(button1);
//		panel.add(button2);
//		panel.add(Box.createGlue());
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		JFrame frame = new ButtonTest();
		frame.setSize(500, 500);
		frame.setVisible(true);
	}

}
