package other;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-6-19
 * Time: 22:25:32
 * To change this template use File | Settings | File Templates.
 */
public class BookException extends Exception{
    private Exception exc;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BookException(String msg) {
    this.msg = msg;
}

    public Exception getExc() {
        return exc;
    }

    public void setExc(Exception exc) {
        this.exc = exc;
    }

    public BookException(Exception ex) {
        this.exc=ex;
    }

    public String getDetaildMsg(){
        return exc==null?msg: exc.getMessage();
    }
}
