
package com.ctb.prism.web.controller;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ctb.prism.web.controller package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LoadStudentData_QNAME = new QName("http://controller.web.prism.ctb.com/", "loadStudentData");
    private final static QName _LoadStudentDataResponse_QNAME = new QName("http://controller.web.prism.ctb.com/", "loadStudentDataResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ctb.prism.web.controller
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CustHierarchyDetailsTO }
     * 
     */
    public CustHierarchyDetailsTO createCustHierarchyDetailsTO() {
        return new CustHierarchyDetailsTO();
    }

    /**
     * Create an instance of {@link StudentSurveyBioTO }
     * 
     */
    public StudentSurveyBioTO createStudentSurveyBioTO() {
        return new StudentSurveyBioTO();
    }

    /**
     * Create an instance of {@link SubtestAccommodationsTO }
     * 
     */
    public SubtestAccommodationsTO createSubtestAccommodationsTO() {
        return new SubtestAccommodationsTO();
    }

    /**
     * Create an instance of {@link ContentScoreDetailsTO }
     * 
     */
    public ContentScoreDetailsTO createContentScoreDetailsTO() {
        return new ContentScoreDetailsTO();
    }

    /**
     * Create an instance of {@link ItemResponseTO }
     * 
     */
    public ItemResponseTO createItemResponseTO() {
        return new ItemResponseTO();
    }

    /**
     * Create an instance of {@link LoadStudentDataResponse }
     * 
     */
    public LoadStudentDataResponse createLoadStudentDataResponse() {
        return new LoadStudentDataResponse();
    }

    /**
     * Create an instance of {@link ContentScoreTO }
     * 
     */
    public ContentScoreTO createContentScoreTO() {
        return new ContentScoreTO();
    }

    /**
     * Create an instance of {@link SubtestAccommodationTO }
     * 
     */
    public SubtestAccommodationTO createSubtestAccommodationTO() {
        return new SubtestAccommodationTO();
    }

    /**
     * Create an instance of {@link DemoTO }
     * 
     */
    public DemoTO createDemoTO() {
        return new DemoTO();
    }

    /**
     * Create an instance of {@link ItemResponsesDetailsTO }
     * 
     */
    public ItemResponsesDetailsTO createItemResponsesDetailsTO() {
        return new ItemResponsesDetailsTO();
    }

    /**
     * Create an instance of {@link ObjectiveScoreDetailsTO }
     * 
     */
    public ObjectiveScoreDetailsTO createObjectiveScoreDetailsTO() {
        return new ObjectiveScoreDetailsTO();
    }

    /**
     * Create an instance of {@link StudentBioTO }
     * 
     */
    public StudentBioTO createStudentBioTO() {
        return new StudentBioTO();
    }

    /**
     * Create an instance of {@link OrgDetailsTO }
     * 
     */
    public OrgDetailsTO createOrgDetailsTO() {
        return new OrgDetailsTO();
    }

    /**
     * Create an instance of {@link StudentListTO }
     * 
     */
    public StudentListTO createStudentListTO() {
        return new StudentListTO();
    }

    /**
     * Create an instance of {@link StudentDetailsTO }
     * 
     */
    public StudentDetailsTO createStudentDetailsTO() {
        return new StudentDetailsTO();
    }

    /**
     * Create an instance of {@link LoadStudentData }
     * 
     */
    public LoadStudentData createLoadStudentData() {
        return new LoadStudentData();
    }

    /**
     * Create an instance of {@link RosterDetailsTO }
     * 
     */
    public RosterDetailsTO createRosterDetailsTO() {
        return new RosterDetailsTO();
    }

    /**
     * Create an instance of {@link ObjectiveScoreTO }
     * 
     */
    public ObjectiveScoreTO createObjectiveScoreTO() {
        return new ObjectiveScoreTO();
    }

    /**
     * Create an instance of {@link StudentDemoTO }
     * 
     */
    public StudentDemoTO createStudentDemoTO() {
        return new StudentDemoTO();
    }

    /**
     * Create an instance of {@link SurveyBioTO }
     * 
     */
    public SurveyBioTO createSurveyBioTO() {
        return new SurveyBioTO();
    }

    /**
     * Create an instance of {@link ContentDetailsTO }
     * 
     */
    public ContentDetailsTO createContentDetailsTO() {
        return new ContentDetailsTO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadStudentData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://controller.web.prism.ctb.com/", name = "loadStudentData")
    public JAXBElement<LoadStudentData> createLoadStudentData(LoadStudentData value) {
        return new JAXBElement<LoadStudentData>(_LoadStudentData_QNAME, LoadStudentData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadStudentDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://controller.web.prism.ctb.com/", name = "loadStudentDataResponse")
    public JAXBElement<LoadStudentDataResponse> createLoadStudentDataResponse(LoadStudentDataResponse value) {
        return new JAXBElement<LoadStudentDataResponse>(_LoadStudentDataResponse_QNAME, LoadStudentDataResponse.class, null, value);
    }

}
