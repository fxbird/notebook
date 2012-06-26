package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fxbird
 * Date: Sep 24, 2010
 * Time: 4:07:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CloneTest {
    public static void main(String[] args) {
//        Integer[] ori={1,2,3};
//        Integer[] clone=ori.clone();

       ArrayList<Integer> ori= new ArrayList();
        ori.add(1);
        ori.add(2);
       ArrayList<Integer> clone=(ArrayList<Integer>)ori.clone(); 
        System.out.println(clone);
    }
}
