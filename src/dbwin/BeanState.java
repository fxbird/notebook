package dbwin;


public enum BeanState {
    New(1), Modified(2),Delete(3),No_Change(4);
    private int state;

    private BeanState(int state) {
        this.state = state;
    }
}
