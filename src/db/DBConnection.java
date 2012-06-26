package db;

import java.io.*;
import java.net.URISyntaxException;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
   private static Connection cnn;
   private static  String dbpath;
   private static SqlSessionFactory factory;
   public static DataSource getDataSource() {
       BasicDataSource ds=new BasicDataSource();
        ds.setDriverClassName("sun.jdbc.odbc.JdbcOdbcDriver");
        ds.setUrl("jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};UID=;PWD=;DBQ="+dbpath);
        return ds;
   }

   public static DataSource getDataSourceRMI(int port) {
       BasicDataSource ds=new BasicDataSource();
        ds.setDriverClassName("org.objectweb.rmijdbc.Driver");
        ds.setUrl("jdbc:rmi://127.0.0.1:"+port+"/jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="+dbpath);
        return ds;
   }

   public static Connection getConnection() throws ClassNotFoundException, SQLException, IOException, URISyntaxException {
       if (cnn!=null && !cnn.isClosed()){
           return cnn;
       }

       dbpath=System.getProperties().getProperty("dbpath");

       Properties  prop=new Properties();
       prop.load(DBConnection.class.getResourceAsStream("/config.properties"));


       Class.forName("org.firebirdsql.jdbc.FBDriver");
       cnn = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:" + dbpath + "?lc_ctype=UTF8",
               prop.getProperty("username"), prop.getProperty("password"));
       
       return  cnn;

   }
   
   

    public static SqlSessionFactory createSessionFactory() throws IOException {
        if (factory!=null) return factory;
        
        Properties prop = new Properties();
        prop.load(DBConnection.class.getResourceAsStream("/config.properties"));
        Reader reader = Resources.getResourceAsReader("dao/dao.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader, prop);

        return factory;
    }
}
