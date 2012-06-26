package dao;

import org.apache.ibatis.session.*;

import java.sql.Connection;
import java.util.List;

public abstract class AbsBaseIbatisDAO {
    private SqlSessionFactory factory;
    private SqlSession session;
    protected String namespace;

    protected AbsBaseIbatisDAO(String namespace, SqlSessionFactory factory) {
        this.namespace = namespace;
        this.factory = factory;
        session=factory.openSession(true);

    }

    public SqlSessionFactory getFactory() {
        return factory;
    }

    public void setFactory(SqlSessionFactory factory) {
        this.factory = factory;
        session=factory.openSession();
    }

    protected void clearCache() {
        session.clearCache();
    }

    protected void close() {
       session.close();
    }

    protected void commit() {
        session.commit();
    }

    protected void commit(boolean b) {
      session.commit(b);
    }

    protected int delete(String s) {
        return session.delete(namespace+"." +s);
    }

    protected int delete(String s, Object o) {
       return session.delete(namespace+"." +s,o);
    }

    protected Configuration getConfiguration() {
        return null;
    }

    protected Connection getConnection() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected <T> T getMapper(Class<T> tClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected int insert(String s) {
      return session.insert(namespace+"." +s);
    }

    protected int insert(String s, Object o) {
       int rst=session.insert(namespace+"." +s,o);
      return rst;
    }

    protected void rollback() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void rollback(boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void select(String s, Object o, ResultHandler resultHandler) {
        session.select(namespace+"." +s,o,resultHandler);
    }

    protected void select(String s, Object o, RowBounds rowBounds, ResultHandler resultHandler) {
        session.select(namespace+"." +s,o,rowBounds,resultHandler);
    }

    protected List selectList(String s) {
       return session.selectList(namespace+"." +s);
    }

    protected List selectList(String s, Object o) {
        session.clearCache();
        return session.selectList(namespace+"." +s,o);
    }

    protected List selectList(String s, Object o, RowBounds rowBounds) {
       return session.selectList(namespace+"." +s,o,rowBounds);
    }

    protected Object selectOne(String s) {
       return session.selectOne(namespace+"." +s);
    }

    protected Object selectOne(String s, Object o) {
        return session.selectOne(namespace+"." +s,o);
    }

    protected int update(String s) {
       return session.update(namespace+"." +s);
    }

    protected int update(String s, Object o) {
       return session.update(namespace+"." +s,o);
    }

    public SqlSession getSession() {
        return session;
    }

    protected String getNamespace() {
        return namespace;
    }

    protected void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
