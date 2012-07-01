package dbwin;

import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

public class MultipleMap {
    private HashMap map=new HashMap();
    private int len;

    public  void add(String[] stuff,String alias) throws Exception {
       if (len==0){
           len=stuff.length;
       } else if (len!=stuff.length){
           throw new Exception("The number of element is not equal to existing，please confirm");
       }

       if (map.containsKey(alias)){
           throw new Exception("The element with alias "+alias+" has existed, change it please");
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
