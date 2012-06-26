package dbwin;

import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xdg
 * Date: 2008-7-5
 * Time: 21:50:24
 * To change this template use File | Settings | File Templates.
 */
public class MultipleMap {
    private HashMap map=new HashMap();
    private int len;

    public  void add(String[] stuff,String alias) throws Exception {
       if (len==0){
           len=stuff.length;
       } else if (len!=stuff.length){
           throw new Exception("元素个数和已有的不一致，请确认");
       }

       if (map.containsKey(alias)){
           throw new Exception("有别名"+alias+"的元素已经存在了，不能重复指定");
       }

       map.put(alias, Arrays.asList(stuff));
    }

    public String getMappedType(String value,String souAlias,String desAlias){
        int index = ((List)map.get(souAlias)).indexOf(value);
        return  ((List)map.get(desAlias)).get(index).toString();
    }

    public List getOneSeries(String typeAlias){
       return (List)map.get(typeAlias);
    }
}
