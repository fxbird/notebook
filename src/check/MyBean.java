package check;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-7-3
 * Time: 10:49:33
 * To change this template use File | Settings | File Templates.
 */
public class MyBean {
     Integer userNo;
    Date birth;
    String title;

     public String getTitle() {
         return title;
     }

     public void setTitle(String title) {
         this.title = title;
     }

     public MyBean() {
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }


    public Integer getUserNo() {
        return userNo;
    }

    public void setUserNo(Integer userNo) {
        this.userNo = userNo;
    }
}
