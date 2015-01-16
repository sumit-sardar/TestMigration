package util;


public interface ImmediateReport {

	public static final float LEFT_X = 30f;
	public static final float TITLE_VALUE_WIDTH = 500f;
	public static final float SCORE_TEXT_VALUE_WIDTH = 250f;
	public static final float TITLE_VALUE_X = 24f;
	public static final float TITLE_Y = 760f;
	public static final float SCORE_BORDER = 0.4f;
	
	public static final String QUES_NO = "Question Number";
	public static final String ITEM_TYPE = "Item Type";
	public static final String RESPONSE = "Examinee’s\nResponse";
	public static final String CR_RESPONSE = "Examinee’s Response";
	public static final String RAW_SCORE = "Points Earned";
	public static final String CONTENT_DOMAIN = "Content Domain";
	public static final String SCORE_SHEET = " Score Sheet";
	public static final String EXAMINEE_NAME = "Examinee Name";
	public static final String SESSION_NAME = "Session Name";
	public static final String TASC_ID = "TASC ID";
	
	public static final float TOTAL_TABLE_WIDTH = 552f;
	public static final float[] FIVE_COLUMN_TABLE_WIDTHS = new float[]{60f, 37f, 75f, 60f, 320f};
	public static final float[] FIVE_COLUMN_TABLE_WIDTHS_CR = new float[]{60f, 37f, 335f, 60f, 60f};
	public static final float[] TWO_COLUMN_TABLE_WIDTHS = new float[]{90f, 462f};
	
	public static final int TOTAL_ITEM_IN_PAGE = 35;
	public static final int TOTAL_ITEM_IN_FIRST_PAGE = 25;
	public static final int CHAR_PER_PDF_PAGE = 2000;
}
