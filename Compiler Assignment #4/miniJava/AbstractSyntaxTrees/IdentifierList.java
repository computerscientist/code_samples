/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import java.util.*;

public class IdentifierList implements Iterable<Identifier>
{
    public IdentifierList() {
        ilist = new ArrayList<Identifier>();
    }
    
    public void add(Identifier s){
        ilist.add(s);
    }
    
    public Identifier get(int i){
        return ilist.get(i);
    }
    
    public int size() {
        return ilist.size();
    }
    
    public Iterator<Identifier> iterator() {
    	return ilist.iterator();
    }
    
    private List<Identifier> ilist;
}
