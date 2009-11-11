package com.ctb.lexington.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tai Truong
 */

/*
 * There are 8 templates for SR items = SR0, SR1, SR2, SR4, SR8, SR9, SR12, SR26
 * SR0, SR1			= don’t use a stimulus.
 * SR4 				= used by passage stimulus.
 * SR8 				= used by passage and image stimulus
 * SR2, SR9, SR12, SR26	= used by image stimulus.
 * There are 6 templates for CR items = CR0, CR2, CR4, CR8, CR9, CR12
 * CR0 				= don’t use a stimulus.
 * CR4 				= used by passage stimulus.
 * CR8 				= used by passage and image stimulus
 * CR2, CR9, CR12 	= used by image stimulus. 
 */
public class TemplateDef implements java.io.Serializable 
{
    private static final int NUMBER_TEMPLATES = 14;
	private static final String[] templateIds 	= {"SR0", "SR1", "SR2", "SR4", "SR8", "SR9", "SR12", "SR26", 
	        							   		   "CR0", "CR2", "CR4", "CR8", "CR9", "CR12" };
	private static final String[] templateNames = {"Template SR0", "Template SR1", "Template SR2", "Template SR4", "Template SR8", "Template SR9", "Template SR12", "Template SR26", 
	        									   "Template CR0", "Template CR2", "Template CR4", "Template CR8", "Template CR9", "Template CR12" };
	private static final String[] templateImages = {"template_med_0.gif", "template_med_1.gif", "template_med_2.gif", "template_med_4.gif", "template_med_8.gif", "template_med_9.gif", "template_med_12.gif", "template_med_26.gif",
	        								 	    "template_med_cr2.gif", "template_med_cr2.gif", "template_med_cr2.gif", "template_med_cr8.gif", "template_med_cr9.gif", "template_med_cr12.gif" };
	private static final String[] templatePreviewImages = {"template_large_0.gif", "template_large_1.gif", "template_large_2.gif", "template_large_4.gif", "template_large_8.gif", "template_large_9.gif", "template_large_12.gif", "template_large_26.gif",
	        									   "template_large_cr2.gif", "template_large_cr2.gif", "template_large_cr2.gif", "template_large_cr8.gif", "template_large_cr9.gif", "template_large_cr12.gif" };
	private static final String[] stimulusTypes	= {"", "", "I1", "PA", "PI", "I2", "I3", "I3", 
	        								   	   "", "I1", "PA", "PI", "I2", "I3" };
	 
    public static List getTemplates_SR_Stimulus_None() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(0));		// "0"
        result.add(getTemplate(1));		// "1"
        return result;
    }

    public static List getTemplates_SR_Stimulus_Passage() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(3));		// "4"
        return result;
    }

    public static List getTemplates_SR_Stimulus_Passage_Art() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(4));		// "8"
        return result;
    }

    public static List getTemplates_SR_Stimulus_Art() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(2));		// "2"
        result.add(getTemplate(5));		// "9"
        result.add(getTemplate(6));		// "12"
        result.add(getTemplate(7));		// "26"
        return result;
    }
    
    public static List getTemplates_SR_Stimulus_Image1() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(2));		// "2"
        return result;
    }

    public static List getTemplates_SR_Stimulus_Image2() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(5));		// "9"
        return result;
    }

    public static List getTemplates_SR_Stimulus_Image3() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(6));		// "12"
        result.add(getTemplate(7));		// "26"
        return result;
    }

    public static List getTemplates_CR_Stimulus_None() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(8));		// "CR"
        return result;
    }

    public static List getTemplates_CR_Stimulus_Passage() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(10));		// "CR4"
        return result;
    }

    public static List getTemplates_CR_Stimulus_Passage_Art() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(11));		// "CR8"
        return result;
    }

    public static List getTemplates_CR_Stimulus_Art() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(9));		// "CR2"
        result.add(getTemplate(12));	// "CR9"
        result.add(getTemplate(13));	// "CR12"
        return result;
    }
    
    public static List getTemplates_CR_Stimulus_Image1() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(9));		// "CR2"
        return result;
    }

    public static List getTemplates_CR_Stimulus_Image2() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(12));	// "CR9"
        return result;
    }

    public static List getTemplates_CR_Stimulus_Image3() 
    { 
        List result = new ArrayList();
        result.add(getTemplate(13));		// "CR12"
        return result;
    }

    ///////////////////////////////////////////////// helper methods ///////////////////////////////////////////////
    public static String getTemplateId(int index) 
    { 
        index = validate(index);
        return templateIds[index];
    }

    public static String getTemplateName(int index) 
    { 
        index = validate(index);
        return templateNames[index];
    }

    public static String getTemplateImage(int index) 
    { 
        index = validate(index);
        return templateImages[index];
    }

    public static String getTemplatePreviewImage(int index) 
    { 
        index = validate(index);
        return templatePreviewImages[index];
    }

    public static String getStimulusType(int index) 
    { 
        index = validate(index);
        return stimulusTypes[index];
    }
    
    public static TemplateVO getTemplate(int index) 
    { 
        index = validate(index);
        TemplateVO template = new TemplateVO();
        template.setId(getTemplateId(index));
        template.setDisplayName(getTemplateName(index));
        template.setImageName(getTemplateImage(index));
        template.setImageNamePreview(getTemplatePreviewImage(index));
        template.setStimulusType(getStimulusType(index));
        return template;
    }

    public static String getStimulusType(String templateId) 
    { 
        String result = "";
        if (templateId != null) {
	        for (int i = 0; i < templateIds.length; i++) {
	            if (templateId.equals(templateIds[i])) {
	                result = stimulusTypes[i];
	                break;
	            }
	        }
        }
        return result;
    }
    
    private static int validate(int index) 
    { 
        if (index < 0)
            index = 0;
        if (index >= NUMBER_TEMPLATES)
            index = NUMBER_TEMPLATES - 1;
        return index;
    }
    
}

