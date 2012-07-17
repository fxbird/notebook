package dao.hbn;

import org.hibernate.Session;

public abstract class BaseHbnDAO {
    protected Session session;

    protected BaseHbnDAO(Session session) {
        this.session = session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

}
