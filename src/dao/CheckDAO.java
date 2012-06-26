package dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-6-19
 * Time: 21:51:04
 * To change this template use File | Settings | File Templates.
 */
public class CheckDAO extends JdbcDaoSupport{
    public void query(){
        String sql="select * from book_type order by type_no desc";
        getJdbcTemplate().query(sql,new RowCallbackHandler(){
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println(rs.getString("type_dsc"));
            }
            });
    }

    public void insert(){
        String sql="insert into book_type(type_no,type_dsc) values(122,'fuck')";
        getJdbcTemplate().update(sql);
    }
}
