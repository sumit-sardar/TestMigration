
/**
 * ScoringServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

    package com.mcgraw_hill.ctb.acuity.scoring;

    /**
     *  ScoringServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ScoringServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ScoringServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ScoringServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for processStudentScore method
            * override this method for handling normal response from processStudentScore operation
            */
           public void receiveResultprocessStudentScore(
                    com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ScoringStatus result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from processStudentScore operation
           */
            public void receiveErrorprocessStudentScore(java.lang.Exception e) {
            }
                


    }
    