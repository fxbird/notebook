/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package other;

import java.util.ArrayList;

/**
 *
 * @author XuDaoG
 */
public class DoubleLinkedList<E> extends ArrayList<E>{
    private int curIdx=-1;
    public DoubleLinkedList() {
       super() ;
    }
    
    public boolean hasPrevious(){
        return curIdx>0;
    }
    
    public boolean hasNext(){
        return curIdx<size()-1;
    }
    
    public E previous(){
        return (E)get(--curIdx);
    }
    
    public E next(){
        return (E)get(++curIdx);
    }

    public boolean add(E obj){
       if (size()>0 && get(size()-1)==obj){
           return  false;
       }
        
       super.add(obj);
       curIdx=size()-1;
       return  true;
    }

    public void gotoLast(){
        curIdx = size() - 1;
    }
}
