package com.ctb.util.request; 

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.request.FilterParams.FilterType;
import com.ctb.exception.request.InvalidFilterFieldException;

/**
 * Filters an array of CTBBeans according to a FilterParam.
 * Output list contains only beans matching the filter spec. 
 * Currently only Integer-, String-, or Date-typed bean 
 * fields may be used for filtering.
 * 
 * @author Nate_Cohen
 */
public class Filterer 
{
	static final long serialVersionUID = 1L;

	static final String OR_SEPARATOR = ",";

	/**
	 * Filters a list according to the FilterParam
	 * 
	 * @param list - the data to filter
	 * @param filter - specifies filter criteria
	 * @throws InvalidFilterFieldException
	 * @throws Exception
	 */
	public static void filter(List list, FilterParam filter) throws InvalidFilterFieldException, Exception{
		String field = filter.getField();
		FilterType type = filter.getType();
		Object [] argument = filter.getArgument();
		Object value = null;
		Iterator iter = list.iterator();
		while(iter.hasNext()) {
			Object o1 = iter.next();
			boolean result = false;
			try {
				value = o1.getClass().getMethod("get" + field, null).invoke(o1, null);
				if(value instanceof String) {
					// ignore case when filtering strings
					value = ((String) value).toUpperCase();
					argument[0] = ((String) argument[0]).toUpperCase();
				}
			} catch (NoSuchMethodException nsme) {
				throw new InvalidFilterFieldException("Filterer: filter: invalid field: " + field);
			}
			if(value == null && !(argument[0] == null)) {
				if(type.equals(FilterType.NOTEQUAL)) result = true;
				else result = false;
			} else if(type.equals(FilterType.CONTAINS)) {
				if(value instanceof String) {
					if(((String) argument[0]).indexOf(OR_SEPARATOR) >= 0) {
						// do an OR
						result = false;
						StringTokenizer st = new StringTokenizer(((String) argument[0]), OR_SEPARATOR);
						while (st.hasMoreTokens() && !result) {
							String token = st.nextToken().trim();
							result = ((String) value).indexOf(token) >= 0;
						}
					} else
						result = ((String) value).indexOf((String) argument[0]) >= 0;
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else if(type.equals(FilterType.EQUALS)) {
				if(value instanceof String) {
					if(((String) argument[0]).indexOf(OR_SEPARATOR) >= 0) {
						// do an OR
						result = false;
						StringTokenizer st = new StringTokenizer(((String) argument[0]), OR_SEPARATOR);
						while (st.hasMoreTokens() && !result) {
							String token = st.nextToken().trim();
							result = ((String) value).equals(token);
						}
					} else 
						result = ((String) value).equals((String) argument[0]);
				} else if(value instanceof Integer) {
					result = ((Integer) value).equals((Integer) argument[0]);
				} else if(value instanceof Date) {
					result = ((Date) value).equals((Date) argument[0]);
				} else if (value == null && argument[0] == null) {
					result = true;
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else if(type.equals(FilterType.NOTEQUAL)) {
				if(value instanceof String) {
					if(((String) argument[0]).indexOf(OR_SEPARATOR) >= 0) {
						// do an OR
						result = false;
						StringTokenizer st = new StringTokenizer(((String) argument[0]), OR_SEPARATOR);
						while (st.hasMoreTokens() && !result) {
							String token = st.nextToken().trim();
							result = !((String) value).equals(token);
						}
					} else
						result = !((String) value).equals((String) argument[0]);
				} else if(value instanceof Integer) {
					result = !((Integer) value).equals((Integer) argument[0]);
				} else if(value instanceof Date) {
					result = !((Date) value).equals((Date) argument[0]);
				} else if (value == null && argument[0] == null) {
					result = false;
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			}else if(type.equals(FilterType.STARTSWITH)) {
				if(value instanceof String) {
					if(((String) argument[0]).indexOf(OR_SEPARATOR) >= 0) {
						// do an OR
						result = false;
						StringTokenizer st = new StringTokenizer(((String) argument[0]), OR_SEPARATOR);
						while (st.hasMoreTokens() && !result) {
							String token = st.nextToken().trim();
							result = ((String) value).startsWith(token);
						}
					} else
						result = ((String) value).startsWith((String) argument[0]);
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else if(type.equals(FilterType.ENDSWITH)) {
				if(value instanceof String) {
					if(((String) argument[0]).indexOf(OR_SEPARATOR) >= 0) {
						// do an OR
						result = false;
						StringTokenizer st = new StringTokenizer(((String) argument[0]), OR_SEPARATOR);
						while (st.hasMoreTokens() && !result) {
							String token = st.nextToken().trim();
							result = ((String) value).endsWith(token);
						}
					} else
						result = ((String) value).endsWith((String) argument[0]);
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else if(type.equals(FilterType.BEFORE)) {
				if(value instanceof Date) {
					result = ((Date) value).before((Date) argument[0]);
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else if(type.equals(FilterType.AFTER)) {
				if(value instanceof Date) {
					result = ((Date) value).after((Date) argument[0]);
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else if(type.equals(FilterType.BETWEEN)) {
				if(value instanceof Date) {
					result = ((Date) value).after((Date) argument[0]) &&
					((Date) value).before((Date) argument[1]);
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else if(type.equals(FilterType.GREATERTHAN)) {
				if(value instanceof String) {
					result = ((String) value).compareTo((String) argument[0]) > 0;
				} else if(value instanceof Integer) {
					result = ((Integer) value).compareTo((Integer) argument[0]) > 0;
				} else if(value instanceof Date) {
					result = ((Date) value).compareTo((Date) argument[0]) > 0;
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else if(type.equals(FilterType.LESSTHAN)) {
				if(value instanceof String) {
					result = ((String) value).compareTo((String) argument[0]) < 0;
				} else if(value instanceof Integer) {
					result = ((Integer) value).compareTo((Integer) argument[0]) < 0;
				} else if(value instanceof Date) {
					result = ((Date) value).compareTo((Date) argument[0]) < 0;
				} else {
					throw new RuntimeException("Filterer: compare: invalid filter argument: " + argument);
				}
			} else {
				throw new RuntimeException("Filterer: compare: invalid filter type: " + type);
			}
			if(!result) {
				iter.remove();

			}
		}

	}

	/**
	 * Filters a list according to the FilterParam
	 * 
	 * @param list - the data to filter
	 * @param filter - specifies filter criteria
	 * @throws InvalidFilterFieldException
	 * @throws Exception
	 */
	public static void demoFilter(List list, FilterParam filter) throws InvalidFilterFieldException, Exception{
		String field = filter.getField();
		FilterType type = filter.getType();
		Object [] argument = filter.getArgument();
		Object value = null;
		Iterator iter = list.iterator();
		while(iter.hasNext()) {
			Object o1 = iter.next();
			boolean result = false;
			try {
				argument[0] = ((String) argument[0]).toUpperCase();

				String valueMapField = "ValueHashMap";

				List getValueMap = (List) o1.getClass().getMethod("get" + valueMapField, null).invoke(o1, null);
				
				if(getValueMap != null && getValueMap.size() > 0) {
					if(field.equals("LabelName")){

						Iterator iter1 = getValueMap.iterator();
						while(iter1.hasNext()) {
							String[] objValue=(String[])iter1.next();
							if(objValue[0]==null) {
								result = false;
								break;
							} else
							if(argument[0].equals(objValue[0].toUpperCase())) {
								result = true;
								break;
							}
						}
					}
					
					if(field.equals("ValueName")){

						Iterator iter1 = getValueMap.iterator();
						while(iter1.hasNext()) {
							String[] objValue=(String[])iter1.next();
							if(objValue[1]==null) {
								result = false;
								break;
							} else
								
							if(argument[0].equals(objValue[1].toUpperCase())) {
								result = true;	
								break;
							}
						}
					}

				} else {
					result = false;
				}
			

			} catch (NoSuchMethodException nsme) {
				throw new InvalidFilterFieldException("Filterer: filter: invalid field: " + field);
			}
			if(!result) {
				iter.remove();

			}
		} 


	} 
} 
