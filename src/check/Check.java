package check;

import db.DBConnection;

import java.sql.*;

import bean.Note;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-6-17
 * Time: 22:44:22
 * To change this template use File | Settings | File Templates.
 */
public class Check {
    private static PreparedStatement ps;
    private static Connection cnn;

    public static void main(String[] args) throws Exception {
        cnn = DBConnection.getConnection();
        ps=cnn.prepareStatement("insert into book(title,content) values(?,?)");
        ps.setString(1,"111");
        ps.setString(2,null);
        ps.execute();
        ps.close();
        ps=null;
        cnn.close();

        cnn=null;
      Connection  cnn2 = DBConnection.getConnection();
        PreparedStatement ps2 = cnn2.prepareStatement("select * ,len(content) as  nel from book order by id desc "
//                +"//where len(content)=0"
        );
        ResultSet rs=ps2.executeQuery();
        while (rs.next()){
            System.out.println("id="+rs.getString("id")+",len="+rs.getString("nel"));
        }
        cnn2.close();
    }
}
