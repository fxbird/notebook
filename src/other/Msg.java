/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package other;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author xdg
 */
public class Msg {
    public static void showMsg(Component parent,String msg){
        JOptionPane.showMessageDialog(parent, msg);
    }

    public static void showInConsole(Object str){
       if (Constant.showInConsole ){
           System.out.println(str.toString());
       }
    }

    public static void showInConsole(String title, Object str){
        if (Constant.showInConsole ){
           System.out.println(title+":"+str.toString());
        }
    }

    public static void showError(Component parent,Exception e){
        StringBuffer s=new StringBuffer(e.toString()+"\n");
        StackTraceElement[] trace = e.getStackTrace();
            for (int i=0; i < trace.length; i++)
                s.append("\tat " + trace[i]+"\n");
        JOptionPane.showMessageDialog(parent, s.toString());
    }

    public static int showConfirm(Component parent,String prompt){
       int  rst= JOptionPane.showConfirmDialog(parent, prompt, "提示", JOptionPane.YES_NO_CANCEL_OPTION);
        switch (rst){
            case JOptionPane.YES_OPTION:
                return 1;
            case JOptionPane.NO_OPTION:
                return 2;
            default:
                return 3;
        }
    }
}
