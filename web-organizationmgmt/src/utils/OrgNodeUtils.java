package utils; 



import dto.PathNode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrgNodeUtils 
{ 
    private static class PathNodeComparator implements Comparator {
        boolean ascending = true;
        public PathNodeComparator(boolean ascending) {
            this.ascending = ascending;
        }
        public int compare(Object o1, Object o2) {
            int result = 0;
            PathNode po1 = (PathNode) o1;
            //case sensitive sorting
            String name1 = po1.getName();
            PathNode po2 = (PathNode) o2;
            String name2 = po2.getName();
            if ( ascending ) {

                result = name1.compareTo(name2);
                
            } else {
                                
                result = name2.compareTo(name1);
                
            }    
            return result;
        }
    }
        
	public static List sortList(List srcList, String sort) {
        boolean ascending = sort.equals("asc");
		Collections.sort(srcList, new PathNodeComparator(ascending));
    	return srcList;
	}
    
} 
