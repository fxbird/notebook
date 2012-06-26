package other;

import db.DBConnection;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;


public class MyUtil {

	 public static boolean isEmpty(String str) {
	        if (str == null) {
	            return true;
	        }

	        if (str.toString().trim().length() == 0) {
	            return true;
	        }

	        return false;
	    }

	     public static String judgeEmpOrNull(String str) {
	        if (str == null) {
	            return "";
	        } else {
	            return str;
	        }
	    }
	
    public static String getComboBoxValue(JComboBox cb) {
        if (cb.getEditor().getItem() == null) {
            return "";
        }
        return cb.getEditor().getItem().toString();
    }

    public static void enableTabIndent(final JTextArea ta) {
        ta.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "none");
        ta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int end = ta.getSelectionEnd();
                int start = ta.getSelectionStart();
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    String selText = ta.getSelectedText();
                    e.isControlDown();

                    if (!isEmpty(selText)) {

                        int rows = (selText.split("\n").length == 0 ? 1 : selText.split("\n").length);
                        selText = "\t" + selText.replaceAll("\n", "\n\t");
                        ta.replaceSelection(selText);
                        ta.select(start, end + rows);


//                        ta.replaceSelection(selText.replaceAll("^|\\n(?!$)", "$0"));
                    } else {
                        int curPos = ta.getCaretPosition();
                        ta.insert("\t", curPos);
                    }
                }
            }
        });
    }

    public static void setEnglishInput(Component f) {
        f.getInputContext().selectInputMethod(Locale.US);
    }

    public static void setChineseInput(Component f) {
        f.getInputContext().selectInputMethod(Locale.CHINA);
    }


    public static void addShortcut(JComponent comp, KeyStroke key, Action action) {
        InputMap imap = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap amap = comp.getActionMap();
        String mapKey = "sc-" + new Random().nextInt();
        imap.put(key, mapKey);
        amap.put(mapKey, action);
    }

    public static void addShortcut(JComponent comp, String key, Action action) {
        addShortcut(comp, KeyStroke.getKeyStroke(key), action);
    }

    public static void addShortcutFocus(JComponent comp, KeyStroke key, Action action) {
        InputMap imap = comp.getInputMap();
        ActionMap amap = comp.getActionMap();
        String mapKey = "sc-" + new Random().nextInt();
        imap.put(key, mapKey);
        amap.put(mapKey, action);
    }

    public static void addShortcutFocus(JComponent comp, String key, Action action) {
        addShortcutFocus(comp, KeyStroke.getKeyStroke(key), action);
    }

    public static void enableInsertTabOnNewLine(final JTextArea ta) {
        addShortcutFocus(ta, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK), new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ta.insert("\n\t", ta.getCaretPosition());
//                    taMeaning.setCaretPosition(taMeaning.getCaretPosition()+2);

            }
        });
    }

    public static boolean writeToFile(String path, String str) {
        BufferedWriter writer = null;
        boolean suc = true;
        try {

            writer = new BufferedWriter(new FileWriter(path));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(MyUtil.class.getName()).log(Level.SEVERE, null, ex);
            suc = false;
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(MyUtil.class.getName()).log(Level.SEVERE, null, ex);
//                showError(ex);
                suc = false;
            }
            return suc;
        }
    }

    public String readPath() {
        String path = "";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(this.getClass().getResourceAsStream("/db.xml"));

            String os=System.getProperties().getProperty("os.name");

            path = doc.getDocumentElement().getElementsByTagName(os.toLowerCase().contains("window")?"win":"non_win").item(0).getFirstChild().getNodeValue();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return path;
    }

    public  static void importFromAcc2FB(String access, String firebird) throws Exception {
        System.out.println("Importing will drop down existing data of firebird, continue?( y or n):");
        Scanner scan = new Scanner(System.in);
        if (!"y".equals(scan.nextLine())) {
            return;
        }

        Connection connA=getAccessConnection(access);
        Connection connF=getFirebirdConnection(firebird);
        exeSql(connF,"drop table book;");
        exeSql(connF,"drop table book_type;");
        String sql="create table book(id int ,title varchar(100) character set UTF8," +
                "content BLOB SUB_TYPE TEXT character set UTF8,tim Timestamp,type_no int);" ;
        exeSql(connF,sql);
        sql="create table book_type(type_no int,type_dsc varchar(50) character set UTF8);";
        exeSql(connF,sql);

        ResultSet rs=query(connA,"select * from book ");
        PreparedStatement ps=connF.prepareStatement("insert into book(id,title,content,tim,type_no) values (?,?,?,?,?)");
        while (rs.next()){
            String content=rs.getString("content");
            if (content!=null && content.length()>64000){

            } else {
                ps.setInt(1,rs.getInt("id"));
                ps.setString(2,rs.getString("title"));
                ps.setString(3,content);
                ps.setTimestamp(4,rs.getTimestamp("tim"));
                ps.setInt(5,rs.getInt("type_no"));
                ps.execute();
            }
        }

       rs=query(connA,"select * from book_type");
        while (rs.next()){
            exeSql(connF,"insert into book_type(type_no,type_dsc) values ("+rs.getInt("type_no")+",'"+rs.getString("type_dsc")+"')");
        }


        rs=query(connF,"select * from book");
        while (rs.next()){
            System.out.println(rs.getString("content"));
        }
        connA.close();
        connF.close();
    }

    public static void checkLength() throws Exception {
        Connection conn = DBConnection.getConnection();
        ResultSet rs = conn.prepareStatement("select * from book").executeQuery();
        while (rs.next()) {
//            System.out.println(rs.getInt("id"));
            String content = rs.getString("content");
            if (content != null && content.length() > 32767) {
                System.out.println(rs.getString("title") + ":" + content.length());
            }
        }
    }

    public static Connection getAccessConnection(String path) throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        Connection cnn = DriverManager.getConnection("jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=" + path, "", "");
//       cnn= DriverManager.getConnection("jdbc:odbc:java_note","","");
        return cnn;
    }

    public static Connection getFirebirdConnection(String path)throws Exception {
        Class.forName("org.firebirdsql.jdbc.FBDriver");
        Connection connF = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:" + path + "?lc_ctype=UTF8", "sysdba", "sysdba");
        return connF;
    }

    public static void exeSql(Connection conn,String sql) throws SQLException{
        conn.prepareStatement(sql).execute();
    }

    public static ResultSet query(Connection conn,String sql) throws SQLException{
       return conn.prepareStatement(sql).executeQuery();
    }

    public static void serialize(String path, Object obj) throws IOException{
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(new File(path)));
        oos.writeObject(obj);
    }

    public static Object deserialize(String path) throws IOException,ClassNotFoundException{
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(new File(path)));
        return ois.readObject();
    }

    public static String getDesktopPath(){
        FileSystemView fsv=FileSystemView.getFileSystemView();
        return fsv.getHomeDirectory().toString();
    }


}
