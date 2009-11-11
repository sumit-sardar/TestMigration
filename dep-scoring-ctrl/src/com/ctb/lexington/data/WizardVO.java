package com.ctb.lexington.data;

import java.util.List;



/**
 * @author Tai Truong
 * @created Jan 31, 2005
 */

////////////////////////////////////////////////////////////////////////////////////////////////
// This a transfer data object used to display the wizard bar and wizard buttons  
// It contains a list of AbstractWizardStep objects, current selected step, image of 'Back' and
// 'Next' buttons.
/////////////////////////////////////////////////////////////////////////////////////////////////
public class WizardVO implements java.io.Serializable
{
    private List wizardSteps = null;
    private int selectedStepIndex = 0;
    private ImageButtonVO backButton = null;
    private ImageButtonVO nextButton = null;
    
    /**
     * Creates new WizardVO
     */
    public WizardVO() {}
    /**
     * @return Returns the backButton.
     */
    public ImageButtonVO getBackButton() {
        return this.backButton;
    }
    /**
     * @param backButton The backButton to set.
     */
    public void setBackButton(ImageButtonVO backButton) {
        this.backButton = backButton;
    }
    /**
     * @return Returns the nextButton.
     */
    public ImageButtonVO getNextButton() {
        return this.nextButton;
    }
    /**
     * @param nextButton The nextButton to set.
     */
    public void setNextButton(ImageButtonVO nextButton) {
        this.nextButton = nextButton;
    }
    /**
     * @return Returns the selectedStep.
     */
    public int getSelectedStepIndex() {
        return this.selectedStepIndex;
    }
    /**
     * @param selectedStep The selectedStep to set.
     */
    public void setSelectedStepIndex(int selectedStepIndex) {
        this.selectedStepIndex = selectedStepIndex;
    }
    /**
     * @return Returns the wizardSteps.
     */
    public List getWizardSteps() {
        return this.wizardSteps;
    }
    /**
     * @param wizardSteps The wizardSteps to set.
     */
    public void setWizardSteps(List wizardSteps) {
        this.wizardSteps = wizardSteps;
    }
 }
