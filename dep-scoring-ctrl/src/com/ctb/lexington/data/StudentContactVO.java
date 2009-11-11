/*
 * StudentContactVO.java
 *
 * Created on February 27, 2002, 2:26 PM
 */

package com.ctb.lexington.data;

// GRNDS imports
import java.text.MessageFormat;

import com.ctb.lexington.util.FormatUtils;


/**
 *
 * @author  rmariott
 * @version 
 */
public class StudentContactVO extends Object implements java.io.Serializable {

    public static final String VO_LABEL = "com.ctb.lexington.data.StudentContactVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.StudentContactVO.array";
    private static final String TRACE_TAG = "com.ctb.lexington.data.StudentContactVO";
    
    private MessageFormat phoneFormat =
                                 new MessageFormat( FormatUtils.PHONE_FORMAT );
    
    private MessageFormat phoneFormatShort = new MessageFormat( FormatUtils.PHONE_FORMAT_SHORT );                        
    
    private MessageFormat faxFormat = new MessageFormat( FormatUtils.FAX_FORMAT );
    
    private MessageFormat zipcodeFormat =
                               new MessageFormat( FormatUtils.ZIPCODE_FORMAT );

    //private String studentContactID;
    private String contactName;
    private String contactType;
    private String contactEmail;
    private String streetLine1;
    private String streetLine2;
    private String streetLine3;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String zipcodeExtension;
    private String primaryPhoneAreaCode;
    private String primaryPhoneCOC;
    private String primaryPhoneLineNumber;
    private String primaryPhoneExtension;
    private String secondaryPhoneAreaCode;
    private String secondaryPhoneCOC;
    private String secondaryPhoneLineNumber;
    private String secondaryPhoneExtension;
    private String m_faxAreaCode;
    private String m_faxCOC;
    private String m_faxLineNumber;

    /** Creates new StudentContactVO */
    public StudentContactVO() {
        this.contactName = "";
        this.contactType = "";
        this.contactEmail = "";
        
        this.streetLine1 = "";
        this.streetLine2 = "";
        this.streetLine3 = "";
        this.city = "";
        this.state = "";
        this.country = "";
        this.zipcode = "";
        this.zipcodeExtension = "";
        
        this.primaryPhoneAreaCode = "";
        this.primaryPhoneCOC = "";
        this.primaryPhoneLineNumber = "";
        this.primaryPhoneExtension = "";
        this.secondaryPhoneAreaCode = "";
        this.secondaryPhoneCOC = "";
        this.secondaryPhoneLineNumber = "";
        this.secondaryPhoneExtension = "";
        
        this.m_faxAreaCode = "";
        this.m_faxCOC = "";
        this.m_faxLineNumber = "";
    }


     /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getContactName()
    {
        return this.contactName;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactName_ The value to set the property to.
     * @return void
     */
    public void setContactName(String contactName_)
    {
        if (contactName_ != null) this.contactName = contactName_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getContactType()
    {
        return this.contactType;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactType_ The value to set the property to.
     * @return void
     */
    public void setContactType(String contactType_)
    {
        if (contactType_ != null) this.contactType = contactType_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getContactEmail()
    {
        return this.contactEmail;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactEmail_ The value to set the property to.
     * @return void
     */
    public void setContactEmail(String contactEmail_)
    {
        if (contactEmail_ != null) this.contactEmail = contactEmail_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getStreetLine1()
    {
        return this.streetLine1;
    }
    
    /**
     * Set the value of this property.
     *
     * @param streeLine1_ The value to set the property to.
     * @return void
     */
    public void setStreetLine1(String streetLine1_)
    {
        if (streetLine1_ != null) this.streetLine1 = streetLine1_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getStreetLine2()
    {
        return this.streetLine2;
    }
    
    /**
     * Set the value of this property.
     *
     * @param streetLine2_ The value to set the property to.
     * @return void
     */
    public void setStreetLine2(String streetLine2_)
    {
        if (streetLine2_ != null) this.streetLine2 = streetLine2_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getStreetLine3()
    {
        return this.streetLine3;
    }
    
    /**
     * Set the value of this property.
     *
     * @param streetLine3_ The value to set the property to.
     * @return void
     */
    public void setStreetLine3(String streetLine3_)
    {
        if (streetLine3_ != null) this.streetLine3 = streetLine3_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getCity()
    {
        return this.city;
    }
    
    /**
     * Set the value of this property.
     *
     * @param city_ The value to set the property to.
     * @return void
     */
    public void setCity(String city_)
    {
        if (city_ != null) this.city = city_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getStatepr()
    {
        return this.state;
    }
    
    /**
     * Set the value of this property.
     *
     * @param state_ The value to set the property to.
     * @return void
     */
    public void setStatepr(String state_)
    {
        if (state_ != null) this.state = state_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getCountry()
    {
        return this.country;
    }
    
    /**
     * Set the value of this property.
     *
     * @param country_ The value to set the property to.
     * @return void
     */
    public void setCountry(String country_)
    {
        if (country_ != null) this.country = country_.trim();
    }

    /*public String getZipCode()
    {
        return zipcode + zipcodeExtension;
    }

    public void setZipCode( String zipCode_ )
    {
        if (zipCode_ != null) zipCode_ = zipCode_.trim();
        if ( zipCode_.length() > 5 )
        {
            zipcode = zipCode_.substring( 0, 5 );
            zipcodeExtension = zipCode_.substring( 5, zipCode_.length() );
        }
        else
        {
            zipcode = zipCode_;
        }
    }*/
    /**
     * Zipcode is the {@link #zipcode} and the {@link #zipcodeExtension}
     * separated with "-".
     */
    public String getZipcode()
    {
        final String SUB_TRACE = TRACE_TAG + ".getZipcode()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        if( getPostalCode() != null
            && ! getPostalCode().equals( "" )
            && getZipcodeExtension() != null
            && ! getZipcodeExtension().equals( "" ) )
        {
            try
            {
                Object[] args =
                {
                    this.getPostalCode(),
                    this.getZipcodeExtension()
                };
                String zipcode = this.zipcodeFormat.format( args );
                //GrndsTrace.exitScope();
                return zipcode;
            }
            catch( Exception e )
            {
                //GrndsTrace.exitScope( "Error getting zipcode: "
                  //                    + e.getMessage() );
                return "";
            }
        }
        else if ( getPostalCode() != null
            && ! getPostalCode().equals( "" ) )
        {
            String zipcode = this.getPostalCode();
            
            //GrndsTrace.exitScope( "5 digit zipcode" );
            return zipcode;
        }
        else
        {
            //GrndsTrace.exitScope( "zipcode = empty" );
            return "";
        }    
        
    }
    
    public void setZipcode( String zipcode_ )
    {
        final String SUB_TRACE = TRACE_TAG + ".setZipcode()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        // check zipcode
        if( zipcode_ == null || zipcode_.equals( "" ) )
        {
            this.setPostalCode( "" );
            this.setZipcodeExtension( "" );
            //GrndsTrace.exitScope( "Set parts to empty" );
        }
        else
        {
            // check that - is in string
            if( zipcode_.indexOf( '-' ) < 0 )
            {
                zipcode_ = zipcode_ + "-";
            }

            try
            {
                Object[] parts = this.zipcodeFormat.parse( zipcode_ );
                this.setPostalCode( (String)parts[0] );
                this.setZipcodeExtension( (String)parts[1] );
                //GrndsTrace.exitScope( "Set parts" );
            }
            catch( Exception e )
            {
                //GrndsTrace.exitScope( "Error setting zipcode: "
                 //                     + e.getMessage() );
            }
        }
    }


    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getPostalCode()
    {
        return this.zipcode;
    }
    
    /**
     * Set the value of this property.
     *
     * @param postalCode_ The value to set the property to.
     * @return void
     */
    public void setPostalCode(String postalCode_)
    {
        if (postalCode_ != null) this.zipcode = postalCode_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getZipcodeExtension()
    {
        return this.zipcodeExtension;
    }
    
    /**
     * Set the value of this property.
     *
     * @param postalCodeExtension_ The value to set the property to.
     * @return void
     */
    public void setZipcodeExtension(String postalCodeExtension_)
    {
        if (postalCodeExtension_ != null) this.zipcodeExtension = postalCodeExtension_.trim();
    }

    /**
     * Get value of the phone number property from this bean instance.
     * Used by the backend to populate the database.
     *
     * @return String The entire phone number, with no paretheses, spaces or dashes.
     */
    /*public String getContactPhoneNumberP()
    {
        //GrndsTrace.enterScope(TRACE_TAG + ".getContactPhoneNumberP()");
        String fullNumber = new StringBuffer().append(this.primaryPhoneAreaCode).append(this.primaryPhoneCOC).append(this.primaryPhoneLineNumber).append(this.primaryPhoneExtension).toString();
        //GrndsTrace.exitScope(fullNumber);
        return fullNumber;
    }*/
    
    /**
     * Set the value of the phone number.  Used by the back end to populate
     * the bean directly from the database.
     *
     * @param contactPhoneNumberP_ The entire phone number, with no paretheses, spaces or dashes.
     * @return void
     */
    /*public void setContactPhoneNumberP(String contactPhoneNumberP_)
    {
        //GrndsTrace.enterScope(TRACE_TAG + ".setContactPhoneNumberP()");
        if (contactPhoneNumberP_.length() >= 10){
            this.setContactPhoneAreaCodeP(contactPhoneNumberP_.substring(0,3));
            this.setContactPhoneCOCP(contactPhoneNumberP_.substring(3,6));
            this.setContactPhoneLineNumberP(contactPhoneNumberP_.substring(6,10));
            this.setContactPhoneExtensionP(contactPhoneNumberP_.substring(10, contactPhoneNumberP_.length()));
        }
        //GrndsTrace.exitScope("contactPhoneNumberP_(input Param) was:" + contactPhoneNumberP_);
    }*/
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */    
    public String getPrimaryPhone()
    {
        final String SUB_TRACE = TRACE_TAG + ".getContactPhoneNumberP()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        // all required fields must be set
        if( this.getPrimaryPhoneAreaCode() != null
            && ! this.getPrimaryPhoneAreaCode().equals( "" )
            && this.getPrimaryPhoneCOC() != null
            && ! this.getPrimaryPhoneCOC().equals( "" )
            && this.getPrimaryPhoneLineNumber() != null
            && ! this.getPrimaryPhoneLineNumber().equals( "" ) )
        {           
            try
            {
                String number = formatPhone(this.getPrimaryPhoneAreaCode(), 
                                            this.getPrimaryPhoneCOC(),
                                            this.getPrimaryPhoneLineNumber(),
                                            this.getPrimaryPhoneExtension());
                
                return number;
            }
            catch( Exception e )
            {
                //GrndsTrace.exitScope( "Error getting phone number 1: "
                //                      + e.getMessage() );
                return "";
            }
        }
        else
        {
            //GrndsTrace.exitScope( "Empty String" );
            return "";
        }
        
    }
    /**
     * Set the value of this property. Phone numbers are stored in the database
     * as strings of the form (123)456-7890x1234. International numbers can
     * be stored as 123-456789010, i.e., CountryCode-OtherNumbers. This method
     * does not currently support the international format.
     *
     * @param timezoneID_ The value to set the property to.
     * @return void
     */    
    public void setPrimaryPhone(String contactPhoneNumberP_)
    {
        final String SUB_TRACE = TRACE_TAG + ".setContactPhoneLineNumberP()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        if( contactPhoneNumberP_ == null || contactPhoneNumberP_.equals( "" ) )
        {
            this.setPrimaryPhoneAreaCode( "" );
            this.setPrimaryPhoneCOC( "" );
            this.setPrimaryPhoneLineNumber( "" );
            this.setPrimaryPhoneExtension( "" );
            //GrndsTrace.exitScope( "Set all parts to empty" );
        }
        else
        {
            try
            {
                if ( contactPhoneNumberP_.length() <= 13 )
                {
                    Object[] parts = this.phoneFormatShort.parse( contactPhoneNumberP_ );
                    this.setPrimaryPhoneAreaCode( (String)parts[0] );
                    this.setPrimaryPhoneCOC( (String)parts[1] );
                    this.setPrimaryPhoneLineNumber( (String)parts[2] );
                    this.setPrimaryPhoneExtension( "" );
                    //GrndsTrace.exitScope( "Set parts");
                }
                else
                {
                    Object[] parts = this.phoneFormat.parse( contactPhoneNumberP_ );
                    this.setPrimaryPhoneAreaCode( (String)parts[0] );
                    this.setPrimaryPhoneCOC( (String)parts[1] );
                    this.setPrimaryPhoneLineNumber( (String)parts[2] );
                    this.setPrimaryPhoneExtension( (String)parts[3] );
                    //GrndsTrace.exitScope( "Set parts");
                }
            }
            catch( Exception e )
            {
                //GrndsTrace.exitScope( "Error setting phone number 1: "
                //                      + e.getMessage() );
            }
        }
    }
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getPrimaryPhoneAreaCode()
    {
        return this.primaryPhoneAreaCode;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneAreaCodeP_ The value to set the property to.
     * @return void
     */
    public void setPrimaryPhoneAreaCode(String contactPhoneAreaCodeP_)
    {
        if (contactPhoneAreaCodeP_ != null) this.primaryPhoneAreaCode = contactPhoneAreaCodeP_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getPrimaryPhoneCOC()
    {
        return this.primaryPhoneCOC;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneCOCP_ The value to set the property to.
     * @return void
     */
    public void setPrimaryPhoneCOC(String contactPhoneCOCP_)
    {
        if (contactPhoneCOCP_ != null) this.primaryPhoneCOC = contactPhoneCOCP_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getPrimaryPhoneLineNumber()
    {
        return this.primaryPhoneLineNumber;
    }
   
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneLineNumberP_ The value to set the property to.
     * @return void
     */
    public void setPrimaryPhoneLineNumber(String contactPhoneLineNumberP_)
    {
        if (contactPhoneLineNumberP_ != null) this.primaryPhoneLineNumber = contactPhoneLineNumberP_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getPrimaryPhoneExtension()
    {
        return this.primaryPhoneExtension;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneExtensionP_ The value to set the property to.
     * @return void
     */
    public void setPrimaryPhoneExtension(String contactPhoneExtensionP_)
    {
        if (contactPhoneExtensionP_ != null) this.primaryPhoneExtension = contactPhoneExtensionP_.trim();
    }
    
    /*  SECONDARY PHONE NUMBER GETTER SETTERS
     *
     *
     *
     *
     */

    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */    
    public String getSecondaryPhone()
    {
        final String SUB_TRACE = TRACE_TAG + ".getSecondaryPhone()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        // all required fields must be set
        if( this.getSecondaryPhoneAreaCode() != null
            && ! this.getSecondaryPhoneAreaCode().equals( "" )
            && this.getSecondaryPhoneCOC() != null
            && ! this.getSecondaryPhoneCOC().equals( "" )
            && this.getSecondaryPhoneLineNumber() != null
            && ! this.getSecondaryPhoneLineNumber().equals( "" ) )
        {
            try
            {
                String number = formatPhone(this.getSecondaryPhoneAreaCode(), 
                                            this.getSecondaryPhoneCOC(),
                                            this.getSecondaryPhoneLineNumber(),
                                            this.getSecondaryPhoneExtension());

                //GrndsTrace.exitScope( number );
                return number;
            }
            catch( Exception e )
            {
                //GrndsTrace.exitScope( "Error getting phone number 1: "
                      //                + e.getMessage() );
                return "";
            }
        }
        else
        {
            //GrndsTrace.exitScope( "Empty String" );
            return "";
        }
        
    }
    /**
     * Set the value of this property. Phone numbers are stored in the database
     * as strings of the form (123)456-7890x1234. International numbers can
     * be stored as 123-456789010, i.e., CountryCode-OtherNumbers. This method
     * does not currently support the international format.
     *
     * @param timezoneID_ The value to set the property to.
     * @return void
     */    
    public void setSecondaryPhone(String contactPhoneNumberS_)
    {
        final String SUB_TRACE = TRACE_TAG + ".setContactPhoneLineNumberS()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        if( contactPhoneNumberS_ == null || contactPhoneNumberS_.equals( "" ) )
        {
            this.setSecondaryPhoneAreaCode( "" );
            this.setSecondaryPhoneCOC( "" );
            this.setSecondaryPhoneLineNumber( "" );
            this.setSecondaryPhoneExtension( "" );
            //GrndsTrace.exitScope( "Set all parts to empty" );
        }
        else
        {
            try
            {
                if ( contactPhoneNumberS_.length() <= 13 )
                {
                    Object[] parts = this.phoneFormatShort.parse( contactPhoneNumberS_ );
                    this.setSecondaryPhoneAreaCode( (String)parts[0] );
                    this.setSecondaryPhoneCOC( (String)parts[1] );
                    this.setSecondaryPhoneLineNumber( (String)parts[2] );
                    this.setSecondaryPhoneExtension( "" );
                    //GrndsTrace.exitScope( "Set parts");
                    
                }
                else
                {
                    Object[] parts = this.phoneFormat.parse( contactPhoneNumberS_ );
                    this.setSecondaryPhoneAreaCode( (String)parts[0] );
                    this.setSecondaryPhoneCOC( (String)parts[1] );
                    this.setSecondaryPhoneLineNumber( (String)parts[2] );
                    this.setSecondaryPhoneExtension( (String)parts[3] );
                    //GrndsTrace.exitScope( "Set parts");
                }
            }
            catch( Exception e )
            {
                //GrndsTrace.exitScope( "Error setting phone number 1: "
                           //           + e.getMessage() );
            }
        }
    }
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getSecondaryPhoneAreaCode()
    {
        return this.secondaryPhoneAreaCode;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneAreaCodeS_ The value to set the property to.
     * @return void
     */
    public void setSecondaryPhoneAreaCode(String contactPhoneAreaCodeS_)
    {
        if (contactPhoneAreaCodeS_ != null) this.secondaryPhoneAreaCode = contactPhoneAreaCodeS_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getSecondaryPhoneCOC()
    {
        return this.secondaryPhoneCOC;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneCOCS_ The value to set the property to.
     * @return void
     */
    public void setSecondaryPhoneCOC(String contactPhoneCOCS_)
    {
        if (contactPhoneCOCS_ != null) this.secondaryPhoneCOC = contactPhoneCOCS_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getSecondaryPhoneLineNumber()
    {
        return this.secondaryPhoneLineNumber;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneLineNumberS_ The value to set the property to.
     * @return void
     */
    public void setSecondaryPhoneLineNumber(String contactPhoneLineNumberS_)
    {
        if (contactPhoneLineNumberS_ != null) this.secondaryPhoneLineNumber = contactPhoneLineNumberS_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getSecondaryPhoneExtension()
    {
        return this.secondaryPhoneExtension;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneExtensionS_ The value to set the property to.
     * @return void
     */
    public void setSecondaryPhoneExtension(String contactPhoneExtensionS_)
    {
        if (contactPhoneExtensionS_ != null) this.secondaryPhoneExtension = contactPhoneExtensionS_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    /*public String getFaxNumber()
    {
        return this.fax;
    }*/
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneExtensionS_ The value to set the property to.
     * @return void
     */
    /*public void setFaxNumber(String faxNumber_)
    {
        if (faxNumber_ != null) this.fax = faxNumber_.trim();
    }*/
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */    
    public String getFax()
    {
        final String SUB_TRACE = TRACE_TAG + ".getFax()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        // required must be set
        if( this.getFaxAreaCode() != null
            && ! this.getFaxAreaCode().equals( "" )
            && this.getFaxCOC() != null
            && ! this.getFaxCOC().equals( "" )
            && this.getFaxLineNumber() != null
            && ! this.getFaxLineNumber().equals( "" ) )
        {
            try
            {
                Object[] args =
                {
                    this.getFaxAreaCode(),
                    this.getFaxCOC(),
                    this.getFaxLineNumber(),
                };
                String number = this.faxFormat.format( args );
                //GrndsTrace.exitScope( number );
                return number;
            }
            catch( Exception e )
            {
                //GrndsTrace.exitScope( "Error getting fax number: "
                //                      + e.getMessage() );
                return "";
            }
        }
        else
        {
            //GrndsTrace.exitScope( "Empty String" );
            return "";
        }
        
    }
    /**
     * Set the value of this property.
     *
     * @param timezoneID_ The value to set the property to.
     * @return void
     */    
    public void setFax(String faxNumber_)
    {
        final String SUB_TRACE = TRACE_TAG + ".setFax()";
        //GrndsTrace.enterScope( SUB_TRACE );
        
        if( faxNumber_ == null || faxNumber_.equals( "" ) )
        {
            this.setFaxAreaCode( "" );
            this.setFaxCOC( "" );
            this.setFaxLineNumber( "" );
            //GrndsTrace.exitScope( "Set all parts to empty" );
        }
        else
        {
            try
            {
                Object[] parts = this.faxFormat.parse( faxNumber_ );
                this.setFaxAreaCode( (String)parts[0] );
                this.setFaxCOC( (String)parts[1] );
                this.setFaxLineNumber( (String)parts[2] );
                //GrndsTrace.exitScope( "Set parts");
            }
            catch( Exception e )
            {
                //GrndsTrace.exitScope( "Error setting fax number: "
                //                      + e.getMessage() );
            }
        }
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getFaxAreaCode()
    {
        return this.m_faxAreaCode;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneExtensionS_ The value to set the property to.
     * @return void
     */
    public void setFaxAreaCode(String faxAreaCode_)
    {
        if (faxAreaCode_ != null) this.m_faxAreaCode = faxAreaCode_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getFaxCOC()
    {
        return this.m_faxCOC;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneExtensionS_ The value to set the property to.
     * @return void
     */
    public void setFaxCOC(String faxCOC_)
    {
        if (faxCOC_ != null) this.m_faxCOC = faxCOC_.trim();
    }
    
    /**
     * Get this property from this bean instance.
     *
     * @return String The value of the property.
     */
    public String getFaxLineNumber()
    {
        return this.m_faxLineNumber;
    }
    
    /**
     * Set the value of this property.
     *
     * @param contactPhoneExtensionS_ The value to set the property to.
     * @return void
     */
    public void setFaxLineNumber(String faxLineNumber_)
    {
        if (faxLineNumber_ != null) this.m_faxLineNumber = faxLineNumber_.trim();
    }    
    
    public static String formatPhone( String areaCode_,
                                      String coc_,
                                      String line_,
                                      String extension_ )
    {        
        String formattedPhone;
        
        if (extension_.equals(""))
        {
            Object[] args = new Object[]
            {
                areaCode_,
                coc_,
                line_,
            };
            
            formattedPhone = MessageFormat.format( FormatUtils.PHONE_FORMAT_SHORT, args );
        }
        else
        {
            
            // Create argument array
            Object[] args = new Object[]
            {
                areaCode_,
                coc_,
                line_,
                extension_
            };
            
            formattedPhone = MessageFormat.format( FormatUtils.PHONE_FORMAT, args );
        }

        return formattedPhone;
    }

}
