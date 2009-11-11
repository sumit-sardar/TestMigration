package com.ctb.lexington.db.mapper;

import com.ctb.lexington.db.data.ReportingLevels;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

public class ObjectiveMapper extends AbstractDBMapper {
	public static final String FIND_NAME = "findObjectivesBySubtestIdAndProductId";
    public static final String FIND_BY_LEVEL = "findObjectivesBySubtestIdAndLevel";
    public static final String FIND_LEVELS = "findReportingLevels";
    public static final String FIND_LEVELS_FOR_ROSTER = "findReportingLevelsForRoster";

	/**
	 * @param conn
	 */
	public ObjectiveMapper(Connection conn) {
		super(conn);
	}
	
	public Collection findObjectivesBySubtestIdAndProductId(Long subtestId, Long productId) {
		HashMap map = new HashMap();
		map.put("subtestId", subtestId);
		map.put("productId", productId);
        return findMany(FIND_NAME, map);
	}

    /**
     * This query finds all objective nodes at a specified level for each item in a subtest.
     *
     * @param subtestId: Id of a Subtest Item Set ('TD' type)
     * @param productId: Id of a Product (not a Curriculum Framework!) in the PRODUCT table
     * @param level: The desired hierarchy level (1,2,3...)
     * @return A collection of ObjectiveData records
     */
	public Collection findObjectivesBySubtestIdAndLevel(Long subtestId, Long productId, Integer level) {
		HashMap map = new HashMap();
		map.put("subtestId", subtestId);
        map.put("productId", productId);
		map.put("level", level);
        return findMany(FIND_BY_LEVEL, map);
	}

    /**
     * This query finds the reporting levels (Contant Area, Primary, Secondary) for a product.
     * Use these levels in calls to <code>findObjectivesBySubtestIdAndLevel</code>
     *
     * @param productId: Id of a Product (not a Curriculum Framework!) in the PRODUCT table
     * @return One record of type ReportingLevels
     */
	public ReportingLevels findReportingLevels(Long productId) {
		HashMap map = new HashMap();
        map.put("productId", productId);

        ReportingLevels record = (ReportingLevels)find(FIND_LEVELS, map);

        if (record == null)
            throw new IllegalArgumentException("Product with ID " + productId + " not found in PRODUCT table. Cannot retrieve reporting levels.");

        if (record.getSecondaryScoringItemSetLevel() == null && record.getScoringItemSetLevel() != null)
            record.setSecondaryScoringItemSetLevel(new Integer(record.getScoringItemSetLevel().intValue() + 1));

        return record;
	}

    public ReportingLevels findReportingLevelsForRoster(Long testRosterId) {
        ReportingLevels record = (ReportingLevels)find(FIND_LEVELS_FOR_ROSTER,testRosterId);

        if (record == null)
            throw new IllegalArgumentException("Cannot retrieve reporting levels for TEST_ROSTER: " + testRosterId);

        if (record.getSecondaryScoringItemSetLevel() == null && record.getScoringItemSetLevel() != null)
            record.setSecondaryScoringItemSetLevel(new Integer(record.getScoringItemSetLevel().intValue() + 1));

        return record;

    }
}