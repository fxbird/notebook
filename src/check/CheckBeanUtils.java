package check;

import bean.Note;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-6-29
 * Time: 22:13:47
 * To change this template use File | Settings | File Templates.
 */
public class CheckBeanUtils {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        MyBean bean=new MyBean();
        bean.setUserNo(new Integer(24));
        bean.setTitle("this is title");
        bean.setBirth(new Date(System.currentTimeMillis()));

        System.out.println("id:"+ PropertyUtils.getProperty(bean,"userNo"));


        System.out.println("birth:"+ BeanUtils.getProperty(bean,"birth"));
        System.out.println("title:"+ PropertyUtils.getProperty(bean,"title"));
    }


}


