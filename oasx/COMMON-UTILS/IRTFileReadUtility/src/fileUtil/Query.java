package fileUtil;

public class Query {

	public static final String insertScoreLookup = "INSERT INTO score_lookup " +
			"(source_score_type_code, dest_Score_type_code, score_lookup_id," +
			"source_score_value, dest_score_value, test_form, test_level, grade, content_area," +
			"norm_group,norm_year,framework_code,product_internal_display_name) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String insertScoreLookupItemSet = "INSERT INTO SCORE_LOOKUP_ITEM_SET VALUES(?,?)";
	
	public static final String INSERT_ITEM_P_VALUE = "INSERT INTO item_p_value VALUES(?, ?, ?, ?, ?, ?)";
	
	public static final String GET_ALL_ITEM_SET = "SELECT itemset.item_set_id " + 
													"FROM test_catalog tc, item_set_ancestor isa, item_set itemset " +
													"WHERE tc.item_set_id = isa.ancestor_item_set_id " +
													  "AND isa.item_set_type = 'TD' " +
													  "AND itemset.item_set_id = isa.item_set_id " +
													  "AND itemset.subject = ? " + 
													  "AND tc.product_id = ?";
	
	public static String ITEMS_FROM_ITEM_SET = "SELECT isi.item_id, isi.item_sort_order" +
													  " FROM item_set_item isi WHERE isi.item_set_id IN (#)";

}
