
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
 
public class SplitPaneDemo extends JFrame
{
    JTextField jtf = new JTextField(15);
   
    String cols[] = {"asdfasdfasdf","asdfasdfsadf","asdfasdf","aaaaaaaaaaaa","aaaaaaaaaaaaaaaaa"};
    JPanel jp = new JPanel();
    JPanel jp1 = new JPanel();
    JPanel jp2 = new JPanel(new BorderLayout());
    JTable tbl = new JTable(10,10);
    JScrollPane jsp = new JScrollPane(tbl);
    
    public SplitPaneDemo() {
    
        
        JButton jb = new JButton("sdf");
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        jsp.setAutoscrolls(true);
        jb.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(jp1.isVisible())
                    jp1.setVisible(false);
                else
                    jp1.setVisible(true);
            }
        });
        jp.setEnabled(true);
        
        jp.add(jsp, "Center");
        jp.setBorder(BorderFactory.createBevelBorder(1));
        jp1.add(new JLabel("asdfeeeeeeeeeeeeeeeeee"));
        
        jp2.add(jsp, "Center");
        jp2.add(jp1, "West");
        
        getContentPane().add(new JPanel().add(jb),"West");
        getContentPane().add(jp2, "Center");
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    public static void main(String[] args)
    {
        new SplitPaneDemo();
    }
}