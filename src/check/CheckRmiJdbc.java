package check;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-6-21
 * Time: 11:07:09
 * To change this template use File | Settings | File Templates.
 */
public class CheckRmiJdbc {
    public static void main(String[] args) throws Exception {
        Class.forName("org.objectweb.rmijdbc.Driver").newInstance();
    String url ="jdbc:rmi://127.0.0.1:1234/jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=f:\\My Documents\\program\\pb\\�ʼǱ�\\asp_book_ac.mdb";
//myDBΪ���ݿ���
    Connection conn= DriverManager.getConnection(url); //������û���Ҳ������Ϊ����

        PreparedStatement ps=conn.prepareStatement("select * from book_type");
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            System.out.println(rs.getString("type_dsc"));
        }

        conn.close();

         BasicDataSource ds=new BasicDataSource();

    }
}
