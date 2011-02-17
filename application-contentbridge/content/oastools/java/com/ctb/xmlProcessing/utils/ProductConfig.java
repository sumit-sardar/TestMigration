/*
 * Created on Aug 22, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ctb.common.tools.Config;
import com.ctb.common.tools.SystemException;

public class ProductConfig extends Config {
    private Map products;

    public ProductConfig() {
        products = new HashMap();
        load(userPropertiesFile);
    }

    public ProductConfig(File file) {
        products = new HashMap();
        load(file);
    }

    protected void readProperties() {
        String scorableFrameworkStr = getRequiredProperty(properties, "scorable.frameworks", file);
        String[] scorableFrameworkList = StringUtils.split(scorableFrameworkStr, ",");

        for (int scorableFrameworkIdx = 0; scorableFrameworkIdx < scorableFrameworkList.length; scorableFrameworkIdx++) {
            String productFrameworkCode = scorableFrameworkList[scorableFrameworkIdx].toUpperCase();

            String productTypeStr = getOptionalProperty(properties, productFrameworkCode
                    .toUpperCase()
                    + ".TYPE", file, null);

            // if the product type info not available, move on to the next product
            if (productTypeStr == null) {
                continue;
            }

            //parse product type
            String[] productTypeList = StringUtils.split(productTypeStr, ",");

            Map productTypes = new HashMap();

            for (int productTypeIdx = 0; productTypeIdx < productTypeList.length; productTypeIdx++) {
                ProductTypeInfo productType = new ProductTypeInfo(StringUtils.split(
                        productTypeList[productTypeIdx], "::")[0].trim(), StringUtils.split(
                        productTypeList[productTypeIdx], "::")[1].trim());

                productTypes.put(productType.getName(), productType);
                productType.setTitle(properties.getProperty(productFrameworkCode + "." + productType.getAbbreviation() +
                        ".TITLE"));
            }

            // retrieve the item core type code for each product
            for (Iterator iter = productTypes.values().iterator(); iter.hasNext();) {
                ProductTypeInfo productType = (ProductTypeInfo) iter.next();

                // -----------------------------------------------------------------------------------------------
                String productScoreTypeStr = getRequiredProperty(properties, productFrameworkCode
                        .toUpperCase()
                        + "." + productType.getAbbreviation() + ".SCORE_TYPE", file);
                if ( productScoreTypeStr != null && productScoreTypeStr.length() > 0 )
                {
	                String[] productScoreTypeList = StringUtils.split(productScoreTypeStr, ",");
	                productType.setScoreTypes(new ArrayList(Arrays.asList(productScoreTypeList)));
                }

                // -----------------------------------------------------------------------------------------------
                String productContentAreaStr = getRequiredProperty(properties, productFrameworkCode
                        .toUpperCase()
                        + "." + productType.getAbbreviation() + ".CONTENT_AREA", file);

                String[] productTypeContentAreaList = StringUtils.split(productContentAreaStr, ",");

                // -----------------------------------------------------------------------------------------------
                Map contentAreas = new HashMap();
                for (int contentAreaIdx = 0; contentAreaIdx < productTypeContentAreaList.length; contentAreaIdx++) {
                    ContentAreaInfo contentArea = new ContentAreaInfo(StringUtils.split(
                            productTypeContentAreaList[contentAreaIdx], "::")[0].trim(),
                            StringUtils.split(productTypeContentAreaList[contentAreaIdx], "::")[1]
                                    .trim(),isScoringEnabled(productFrameworkCode, productType));

                    contentAreas.put(contentArea.getName(), contentArea);
                }

                productType.setContentAreas(contentAreas);

                // -----------------------------------------------------------------------------------------------
                for (Iterator iterator = productType.getContentAreas().values().iterator(); iterator
                        .hasNext();) {
                    ContentAreaInfo contentArea = (ContentAreaInfo) iterator.next();

                    String productContentAreaScoreLookupStr = getRequiredProperty(properties,
                            productFrameworkCode.toUpperCase() + "."
                                    + productType.getAbbreviation() + "."
                                    + contentArea.getAbbreviation() + ".SCORE_LOOKUP", file);
                    if ( productContentAreaScoreLookupStr != null && productContentAreaScoreLookupStr.length() > 0 )
                    {
	                    String[] productContentAreaScoreLookupList = StringUtils.split(
	                            productContentAreaScoreLookupStr, ",");
	                    contentArea.setScoreLookup(new ArrayList(Arrays
	                            .asList(productContentAreaScoreLookupList)));
                    }
                }
            }

            products.put(productFrameworkCode, productTypes);
        }
    }

    private boolean isScoringEnabled(String productFrameworkCode, ProductTypeInfo productType) {
        String scoringEnabledKey = productFrameworkCode.toUpperCase()
                + "." + productType.getAbbreviation() + ".SCORING";
        String scoringEnabled = getOptionalProperty(properties,scoringEnabledKey,file,"true");
        boolean isScoringEnabled = new Boolean(scoringEnabled).booleanValue();
        return isScoringEnabled;
    }

    public String toString() {
        StringBuffer result = new StringBuffer("");

        for (Iterator iter = products.keySet().iterator(); iter.hasNext();) {

            String productFrameworkCode = (String) iter.next();

            result.append("Product FrameworkCode: ");
            result.append(productFrameworkCode);
            result.append("\n");

            for (Iterator iter2 = ((Map) products.get(productFrameworkCode)).values().iterator(); iter2
                    .hasNext();) {
                ProductTypeInfo productType = (ProductTypeInfo) iter2.next();
                result.append(productType.toString());
            }
        }

        return result.toString();

    }

    public Map getProductTypes(String frameworkCode) {
        Map productTypes = (Map) products.get(frameworkCode.toUpperCase());

        if (productTypes != null) {
            return productTypes;
        } else {
            throw new SystemException("Cannot find framework [" + frameworkCode + "]\n");
        }
    }

    /**
     * @param productName
     * @return
     */
    public ProductTypeInfo findProductType(String frameworkCode, String productName) {
        Map productTypes = (Map) getProductTypes(frameworkCode.toUpperCase());

        ProductTypeInfo info = (ProductTypeInfo) productTypes.get(productName.toUpperCase());

        if (info != null) {
            return info;
        } else {
            throw new SystemException("Cannot find product [" + productName + "] in framework ["
                    + frameworkCode + "]\n");
        }
    }
}