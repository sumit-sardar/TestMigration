/*
 * Created on Aug 4, 2004
 *
 */
package com.ctb.lexington.domain.teststructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ctb.lexington.util.Listx;

/**
 * @author arathore
 *
 */
public class Subtests implements Serializable{
	private List subtests;
	
	public Subtests() {
		this(0);
	}
	
	public Subtests(int size) {
		subtests = new ArrayList(size);
	}
	
	public void add(SubtestInfo subtestInfo) {
		subtests.add(subtestInfo);
	}
	
	public void add(int index, SubtestInfo subtestInfo) {
		subtests.add(index, subtestInfo);
	}
	
	public void insertSampleSet(String itemSetId, SubtestInfo sampleSet){
	    subtests = insertBefore(itemSetId, sampleSet, subtests);
	}
	
	private List insertBefore(String itemSetId , SubtestInfo sampleSet, List subtests){
	    if(subtests.size() == 0)
	        return subtests;
        if(((SubtestInfo)Listx.first(subtests)).getId().equals(itemSetId))
            return Listx.buildList(sampleSet, subtests);
	    else
	        return Listx.buildList(Listx.first(subtests), insertBefore(itemSetId, sampleSet, Listx.allButFirst(subtests)));
	}
	
	public boolean isPresent(String subtestName) {
		for (Subtests.SubtestsIterator i = iterator(); i.hasNext();) {
			SubtestInfo subtest = i.next();
			if (subtest.getSubtestName().equals(subtestName))
				return true;
		}
		return false;
	}

	public boolean areAnyPresent(String[] tests) {
		for (int i = 0; i < tests.length; i++)
			if (isPresent(tests[i])) return true;
		return false;
	}

	public Subtests.SubtestsIterator iterator() {
		return new SubtestsIterator(subtests);
	}
	
	public class SubtestsIterator{
		private Iterator iter;
		
		public SubtestsIterator(List list) {
			iter = new ArrayList(list).iterator();
		}
		
		public boolean hasNext() {
			return iter.hasNext();
		}

		public SubtestInfo next() {
			return (SubtestInfo) iter.next();
		}
	}

	public SubtestInfo subtest(String subtestName) {
		for (Subtests.SubtestsIterator i = iterator(); i.hasNext();) {
			SubtestInfo subtest = i.next();
			if (subtest.getSubtestName().equals(subtestName))
				return subtest;
		}
		return null;
	}
	
	//private Subtests 
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("[ ");
		for (Subtests.SubtestsIterator i = iterator(); i.hasNext();)
			result.append(i.next() + ", ");
		result.append(" ]");
		return result.toString();
	}

    public void remove(SubtestInfo subtest) {
        subtests.remove(subtest);
    }

    public boolean containsId(String id) {
        for (SubtestsIterator i = iterator(); i.hasNext();) {
            if (i.next().getId().equals(id))
                return true;
        }
        return false;
    }

	public boolean containsSubtestName(String subtestName) {
        for (SubtestsIterator i = iterator(); i.hasNext();) {
            if (i.next().getSubtestName().equals(subtestName))
                return true;
        }
        return false;
	}

    public void moveUp(String subtestId) {
        subtests = movedUp(subtestId, Listx.list(subtests));
    }

    public void moveDown(String subtestId) {
        subtests = movedDown(subtestId, Listx.list(subtests));
    }

    private List movedUp(String subtestId, List subtests) {
        if (subtests.size() == 1 || subtests.size() == 0) 
            return subtests;
        if (((SubtestInfo) subtests.get(1)).getId().equals(subtestId))
            return swappedFirstTwo(subtests);
        else 
            return Listx.buildList( Listx.first(subtests), movedUp(subtestId, Listx.allButFirst(subtests)));
    }
    
    private List movedDown(String subtestId, List subtests) {
        if (subtests.size() == 1 || subtests.size() == 0) 
            return subtests;
        if (((SubtestInfo) subtests.get(0)).getId().equals(subtestId))
            return swappedFirstTwo(subtests);
        else 
            return Listx.buildList( Listx.first(subtests), movedDown(subtestId, Listx.allButFirst(subtests)));
    }

    private List  swappedFirstTwo(List list) {
        Object first = Listx.shift(list);
        list.add(1, first);
        return list;
    }

    public int size() {
        return subtests.size();
    }

    public SubtestInfo getSubtestAt(int index){
        return (SubtestInfo) subtests.get(index);
    }
    
    public SubtestInfo getSubtestAtOrder(int order) {
    	for (Subtests.SubtestsIterator i = iterator(); i.hasNext();) {
			SubtestInfo info = i.next();
			if (info.getItemOrder() == order)
				return info;
		}
    	return null;
    }

    public SubtestInfo getSubtest(String subtestId) {
		for (Subtests.SubtestsIterator i = iterator(); i.hasNext();) {
			SubtestInfo subtest = i.next();
			if (subtest.getId().equals(subtestId))
				return subtest;
		}
		return null;
    }
    
    public SubtestInfo getSubtestOfName(String name) {
		for (Subtests.SubtestsIterator i = iterator(); i.hasNext();) {
			SubtestInfo subtest = i.next();
			if (subtest.getSubtestName().equals(name))
				return subtest;
		}
		return null;
    }
    
    public SubtestInfo getSubtest(String subtestName, String subtestLevel) {
		for (Subtests.SubtestsIterator i = iterator(); i.hasNext();) {
			SubtestInfo subtest = i.next();
			if (subtest.getSubtestName().equalsIgnoreCase(subtestName) && (subtest.getLevel() != null && subtest.getLevel().equalsIgnoreCase(subtestLevel)))
				return subtest;
		}
		return null;
    }

    public List subtestNames() {
        Set names = new HashSet();
		for (Subtests.SubtestsIterator i = iterator(); i.hasNext();) {
			SubtestInfo subtest = i.next();
			names.add(subtest.getSubtestName());
		}
		return sortedSubtestNames(Listx.list(names));
    }
    
	private List sortedSubtestNames(List names) {
		Collections.sort(names, new Comparator() {
			public int compare(Object subtestName1, Object subtestName2) {
				return Listx.list(SubtestNames.ORDERING).indexOf(subtestName1) - Listx.list(SubtestNames.ORDERING).indexOf(subtestName2); 
			}});
		return names;
	}

	public void sortInOrder() {
    	Collections.sort(subtests, new Comparator() {
			public int compare(Object o1, Object o2) {
				SubtestInfo s1 = (SubtestInfo) o1;
				SubtestInfo s2 = (SubtestInfo) o2;
				return s1.getItemOrder() - s2.getItemOrder();
			}
    	});
    }
    
	public void sortInDisplayOrder() {
    	Collections.sort(subtests, new Comparator() {
			public int compare(Object o1, Object o2) {
				SubtestInfo s1 = (SubtestInfo) o1;
				SubtestInfo s2 = (SubtestInfo) o2;
				return Listx.list(SubtestNames.ORDERING).indexOf(s1.getSubtestName()) - Listx.list(SubtestNames.ORDERING).indexOf(s2.getSubtestName());
			}
    	});
	}

	public Subtests.SubtestsIterator levelIterator(String level) {
		return new SubtestsIterator(testsForLevel(level));
	}
	
	private List testsForLevel(String level) {
		ArrayList result = new ArrayList();
		for (SubtestsIterator i = iterator(); i.hasNext();) {
			SubtestInfo subtest = i.next();
			if (subtest.getLevel() == null || subtest.getLevel().equals(level))
				result.add(subtest);
		}
		return result;
	}

	public void update(SubtestInfo newSubtest) {
		SubtestInfo older = getSubtestOfName(newSubtest.getSubtestName());
		if (older == null) return;
		subtests.add(subtests.indexOf(older), newSubtest);
		subtests.remove(older);
	}
}