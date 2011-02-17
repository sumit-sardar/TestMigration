package com.ctb.xmlProcessing.assessment;

import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ctb.common.tools.OASConstants;
import com.ctb.common.tools.media.MediaMapper;

/**
 * @author wmli
 */
public class AssessmentType {
    private static Map assessmentTypes = new HashMap();
    private String name;
    private String productType;
    private String mediaPath;

    public static AssessmentType SOFA =
        new AssessmentType(
            "SOFA",
            OASConstants.SCALED_FORMATIVE_PRODUCT_TYPE_CODE,
            MediaMapper.CAB_MEDIA_PATH);

    public static AssessmentType TERRANOVA =
        new AssessmentType(
            "TERRANOVA",
            OASConstants.TERRANOVA_PRODUCT_TYPE_CODE,
            MediaMapper.TERRANOVA_MEDIA_PATH);
    
    public static AssessmentType TERRA1 =
        new AssessmentType(
            "TERRA1",
            OASConstants.TERRA1_PRODUCT_TYPE_CODE,
            MediaMapper.TERRANOVA_MEDIA_PATH);
    
    public static AssessmentType AAS =
        new AssessmentType(
            "AAS",
            OASConstants.TERRA1_PRODUCT_TYPE_CODE,
            MediaMapper.TERRANOVA_MEDIA_PATH);
    
    public static AssessmentType TABE =
        new AssessmentType(
            "TABE",
            OASConstants.TABE_PRODUCT_TYPE_CODE,
            MediaMapper.TABE_MEDIA_PATH);

    public static AssessmentType TEST =
        new AssessmentType(
            "TEST",
            OASConstants.CURRICULUM_FRAMEWORK_PRODUCT_TYPE_CODE,
            MediaMapper.CAB_MEDIA_PATH);

    public AssessmentType(String name, String productType, String mediaPath) {
        this.name = name;
        this.productType = productType;
        this.mediaPath = mediaPath;
        assessmentTypes.put(name, this);
    }

    static public Iterator getAllAssessmentTypes() {
        return assessmentTypes.values().iterator();
    }

    static public AssessmentType getAssessmentType(String name) {
        return (AssessmentType) assessmentTypes.get(name);
    }

    private Object readResolve() throws InvalidObjectException {
        AssessmentType type = getAssessmentType(name);

        if (type != null) {
            return type;
        } else {
            String msg = "Invalid deserialized assessmentType:  name = ";
            throw new InvalidObjectException(msg + name);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof AssessmentType) {
            return this.name.equals(((AssessmentType) obj).getName());
        }
        return false;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public String getName() {
        return name;
    }

    public String getProductType() {
        return productType;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

}
