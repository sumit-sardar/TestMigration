package com.ctb.lexington.domain.teststructure;

import java.util.HashMap;

import com.ctb.lexington.util.SafeHashMap;

public final class ProductType extends StringConstant {
    private static final HashMap ALL_LEVELS = new SafeHashMap(String.class, ProductType.class);

    public static final ProductType SOFA = new ProductType("SF", "Sofa");
    public static final ProductType SCALED_SUMMATIVE = new ProductType("SS", "Scaled Summative");
    public static final ProductType CAB_IBS = new ProductType("ST", "CAB IBS");
    public static final ProductType TABE = new ProductType("TB", "TABE");
    public static final ProductType TERRANOVA = new ProductType("TV", "TerraNova");
    public static final ProductType RESEARCH_STUDY = new ProductType("RS", "ResearchStudy");
    public static final ProductType TERRANOVA_RESEARCH = new ProductType("TN", "TerraNova");
    public static final ProductType TABE_LOCATOR = new ProductType("TL", "TABE Locator");
    public static final ProductType LASLINK = new ProductType("LL", "LasLink");
    
    //Need to add one entry for Laslink
    
    private ProductType(final String code, final String description) {
        super(code, description);
        ALL_LEVELS.put(code, this);
    }

    public static ProductType getByCode(final String code) {
        if (!ALL_LEVELS.containsKey(code)) {
            System.out.println("No ProductType found for: " + code);
            return null;
        }
        return (ProductType) ALL_LEVELS.get(code);
    }

    public static boolean isTabe(final String productTypeCode) {
        return TABE.getCode().equalsIgnoreCase(productTypeCode);
    }

    public static boolean isSofa(final String productTypeCode) {
        return SOFA.getCode().equalsIgnoreCase(productTypeCode);
    }

    public static boolean isTerraNova(final String productTypeCode) {
        return TERRANOVA.getCode().equalsIgnoreCase(productTypeCode);
    }
}