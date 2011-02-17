/*
 * Created on Aug 3, 2005
 * @author Earl_Waller
 *
 * Class to support testing of objective tree structure
 */

import java.io.Serializable;
import java.lang.Comparable;

public class Objective implements Comparable, Serializable {
    String  Id;
    Integer Level;

    public Objective () {}

    public Objective (String id, int level) {
        Id = new String (id);
        Level = new Integer (level);
    } // public Objective (String id, int level)

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo (Object o) {
        return compareTo ((Objective) o);
    } // public int compareTo (Object o)

    public int compareTo (Objective o) {
        int result = Id.compareTo (o.Id);
        if (result != 0)
            return result;
        return Level.compareTo (o.Level);
    } // public int compareTo (Objective o)

    public boolean equals (Object o) {
        return equals ((Objective) o);
    } // boolean equals (Object o)

    public boolean equals (Objective o) {
        if (Id.compareTo (o.Id) != 0)
            return false;
        return Level.compareTo (o.Level) == 0;
    } // boolean equals (Objective o)
} // public class Objective
