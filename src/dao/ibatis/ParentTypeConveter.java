/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dao.ibatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 *
 * @author fxbird
 */
public class ParentTypeConveter  implements TypeHandler {

    public Object getResult(ResultSet rs, String name) throws SQLException {
        if (rs.getObject(name)==null){
            return new Integer(0);
        }else {
            return rs.getInt(name);
        }
    }

    public Object getResult(CallableStatement cs, int i) throws SQLException {
        if (cs.getObject(i)==null){
            return new Integer(0);
        }else {
            return cs.getInt(i);
        }
    }

    public void setParameter(PreparedStatement ps, int i, Object o, JdbcType jt) throws SQLException {
       
    }

}
