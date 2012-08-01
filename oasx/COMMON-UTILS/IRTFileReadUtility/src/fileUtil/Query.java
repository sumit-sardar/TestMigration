package fileUtil;

public class Query {

	public static final String insertScoreLookup = "INSERT INTO score_lookup " +
			"(source_score_type_code, dest_Score_type_code, score_lookup_id," +
			"source_score_value, dest_score_value, test_form, test_level, grade, content_area," +
			"norm_group,norm_year,framework_code,product_internal_display_name) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String insertScoreLookupItemSet = "INSERT INTO SCORE_LOOKUP_ITEM_SET VALUES(?,?)";
}
