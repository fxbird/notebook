package db;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-7-2
 * Time: 11:23:32
 * To change this template use File | Settings | File Templates.
 */
public class DBUtil {
    public static ResultSet query(Connection cnn,String sql) throws SQLException {
        return cnn.prepareStatement(sql).executeQuery();
    }
}
