package com.ctb.utils;

public class SQLUtils {

	public static String GET_ALL_MAPPING_ITEM_DETAILS = "SELECT tr.test_roster_id, "
			+ "        odim.oas_item_id, "
			+ "        odim.das_item_id, "
			+ "        odim.peid, "
			+ "        odim.item_order "
			+ "   FROM oas_das_item_mapping_mss odim, "
			+ "        product                  prod, "
			+ "        test_admin               ta, "
			+ "        test_roster              tr, "
			+ "        student_item_set_status  siss, "
			+ "        item_set_item            isi "
			+ "  WHERE  odim.oas_item_id = isi.item_id "
			+ "    and isi.item_set_id = siss.item_set_id "
			+ "    and siss.test_roster_id = tr.test_roster_id "
			+ "    AND tr.customer_id = ta.customer_id "
			+ "    AND tr.test_admin_id = ta.test_admin_id "
			+ "    AND ta.product_id = prod.product_id "
			+ "    AND prod.parent_product_id = ? "
			+ "    AND ta.activation_status = 'AC' "
			+ "    AND tr.activation_status = 'AC' "
			+ "    AND odim.activation_status = 'AC' "
			+ "    AND EXISTS (SELECT 1 "
			+ "           FROM item_response_cr irc "
			+ "          WHERE irc.test_roster_id = tr.test_roster_id "
			+ "            AND irc.item_set_id = isi.item_Set_id "
			+ "            AND irc.item_id = odim.oas_item_id) "
			+ "  ORDER BY tr.test_roster_id ";

	public static String GET_CR_RESPONSE_BY_ROSTER_ITEM = "SELECT irc.test_roster_id||irc.item_id||odim.das_item_id as key, "
			+ " 	  irc.constructed_response "
			+ "  FROM item_response_cr 			irc, "
			+ "       oas_das_item_mapping_mss  odim "
			+ " WHERE odim.oas_item_id  = irc.item_id "
			+ "   AND odim.activation_status = 'AC' "
			+ "   AND (irc.test_roster_id, irc.item_id) IN ( ";
}
