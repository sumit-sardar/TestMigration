package utils;

public class TestSessionUtils
{
    public static final String GENERIC_PRODUCT_TYPE = "genericProductType";
    public static final String TABE_BATTERY_SURVEY_PRODUCT_TYPE = "tabeBatterySurveyProductType";
    public static final String TABE_LOCATOR_PRODUCT_TYPE = "tabeLocatorProductType";
    public static final String TABE_ADAPTIVE_PRODUCT_TYPE = "tabeAdaptiveProductType";
    public static final String LASLINKS_PRODUCT_TYPE = "lasLinksProductType";
	
    /**
     * getProductType
     */
    public static String getProductType(String productType)
    {
        if (productType.equals("TB"))
            return TABE_BATTERY_SURVEY_PRODUCT_TYPE;
        else if (productType.equals("TL"))
            return TABE_LOCATOR_PRODUCT_TYPE;
        else if (productType.equals("TA"))
        	return TABE_ADAPTIVE_PRODUCT_TYPE;
        else if (productType.equals("LL"))
        	return LASLINKS_PRODUCT_TYPE;
        else
            return GENERIC_PRODUCT_TYPE;
    }

    /**
     * isTabeProduct
     */
    public static Boolean isTabeProduct(String productType)
    {
        return new Boolean(	(! productType.equals(GENERIC_PRODUCT_TYPE)) && 
        					(! productType.equals(TABE_ADAPTIVE_PRODUCT_TYPE)) &&
							(! productType.equals(LASLINKS_PRODUCT_TYPE)));
    }
    
    /**
     * isTabeAdaptiveProduct
     */
    public static Boolean isTabeAdaptiveProduct(String productType)
    {
        return new Boolean(productType.equals(TABE_ADAPTIVE_PRODUCT_TYPE));
    }
    
    /**
     * isTabeOrTabeAdaptiveProduct
     */
    public static Boolean isTabeOrTabeAdaptiveProduct(String productType)
    {
        return new Boolean(	productType.equals(TABE_BATTERY_SURVEY_PRODUCT_TYPE) ||
        					productType.equals(TABE_LOCATOR_PRODUCT_TYPE) ||
        					productType.equals(TABE_ADAPTIVE_PRODUCT_TYPE));
    }
    
    /**
     * isTabeBatterySurveyProduct
     */
    public static Boolean isTabeBatterySurveyProduct(String productType)
    {
        return new Boolean(productType.equals(TABE_BATTERY_SURVEY_PRODUCT_TYPE));
    }

    /**
     * isTabeLocatorProduct
     */
    public static Boolean isTabeLocatorProduct(String productType)
    {
        return new Boolean(productType.equals(TABE_LOCATOR_PRODUCT_TYPE));
    }

    /**
     * isLasLinksProduct
     */
    public static Boolean isLasLinksProduct(String productType)
    {
        return new Boolean(productType.equals(LASLINKS_PRODUCT_TYPE));
    }
	

}