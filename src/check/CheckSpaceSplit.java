package check;

import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-6-24
 * Time: 21:48:43
 * To change this template use File | Settings | File Templates.
 */
public class CheckSpaceSplit {


    public static void main(String[] args) {
        String str = "1  2 3";
        StringTokenizer token = new StringTokenizer(str," ");
        while (token.hasMoreTokens()) {
            System.out.println(token.nextElement());
        }
    }


}
