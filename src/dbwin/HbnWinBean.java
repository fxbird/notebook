package dbwin;

import dbwin.sql.OperateType;

public interface HbnWinBean {
    void setState(BeanState state);
    BeanState getState();
    public String getClassName();

}
