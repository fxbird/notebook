package dbwin;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-6-29
 * Time: 21:46:10
 * To change this template use File | Settings | File Templates.
 */
public class DbWinException extends Exception{
    private Exception exc;
    private String msg;

    public DbWinException(String msg) {
        this.msg = msg;
    }


    public DbWinException(Exception exc, String msg) {
        this.exc = exc;
        this.msg = msg;
    }


    public Exception getExc() {
        return exc;
    }

    public void setExc(Exception exc) {
        this.exc = exc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
