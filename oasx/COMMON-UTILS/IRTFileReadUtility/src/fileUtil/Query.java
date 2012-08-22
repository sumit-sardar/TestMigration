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
													  "AND tc.product_id = ? " +
													  "AND itemset.item_set_level = ? " +
													  "AND itemset.sample = 'F'";
	
	public static String ITEMS_FROM_ITEM_SET = " SELECT ISITD.ITEM_ID AS ITEM_ID, "
			+ "ISITD.ITEM_SORT_ORDER AS ITEM_SORT_ORDER, "
			+ "ISET.ITEM_SET_ID AS OBJECTIVE_ID, "
			+ "ISET.ITEM_SET_NAME AS OBJECTIVE_NAME "
			+ "FROM ITEM_SET_ITEM     ISI, "
			+ "ITEM_SET_ANCESTOR ISA, "
			+ "ITEM_SET          ISET, "
			+ "ITEM_SET_CATEGORY ISC, "
			+ "PRODUCT           PROD, "
			+ "ITEM_SET_ITEM     ISITD "
			+ "WHERE ISITD.ITEM_SET_ID IN (#) "
			+ "AND ISI.ITEM_ID = ISITD.ITEM_ID "
			+ "AND ISI.ITEM_SET_ID = ISA.ITEM_SET_ID "
			+ "AND ISA.ANCESTOR_ITEM_SET_ID = ISET.ITEM_SET_ID "
			+ "AND ISET.ITEM_SET_TYPE = 'RE' "
			+ "AND ISET.ITEM_SET_CATEGORY_ID = ISC.ITEM_SET_CATEGORY_ID "
			+ "AND ITEM_SET_CATEGORY_LEVEL = PROD.SCORING_ITEM_SET_LEVEL "
			+ "AND PROD.PRODUCT_ID = 3720 " + "ORDER BY ISITD.ITEM_SORT_ORDER";

}
