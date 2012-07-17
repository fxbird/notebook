package dbwin.sql;

public enum OperateType {
    New(1),Update(2),Delete(3),No_Change(4);
    private int operType;

    private OperateType(int operType) {
        this.operType = operType;
    }
}
