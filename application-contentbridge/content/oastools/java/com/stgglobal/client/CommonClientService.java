/**
 * CommonClientService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.stgglobal.client;

public interface CommonClientService extends javax.xml.rpc.Service {
    public java.lang.String getCommonClientAddress();

    public com.stgglobal.client.CommonClient getCommonClient() throws javax.xml.rpc.ServiceException;

    public com.stgglobal.client.CommonClient getCommonClient(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
