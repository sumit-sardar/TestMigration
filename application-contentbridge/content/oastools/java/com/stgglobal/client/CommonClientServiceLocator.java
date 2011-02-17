/**
 * CommonClientServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.stgglobal.client;

public class CommonClientServiceLocator extends org.apache.axis.client.Service implements com.stgglobal.client.CommonClientService {

//	 Use to get a proxy class for CommonClient
    private java.lang.String CommonClient_address = "";
	
    public CommonClientServiceLocator(String wsClient) {
    	CommonClient_address = wsClient;
    }


    public CommonClientServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CommonClientServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    public java.lang.String getCommonClientAddress() {
        return CommonClient_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CommonClientWSDDServiceName = "CommonClient";

    public java.lang.String getCommonClientWSDDServiceName() {
        return CommonClientWSDDServiceName;
    }

    public void setCommonClientWSDDServiceName(java.lang.String name) {
        CommonClientWSDDServiceName = name;
    }

    public com.stgglobal.client.CommonClient getCommonClient() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CommonClient_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCommonClient(endpoint);
    }

    public com.stgglobal.client.CommonClient getCommonClient(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.stgglobal.client.CommonClientSoapBindingStub _stub = new com.stgglobal.client.CommonClientSoapBindingStub(portAddress, this);
            _stub.setPortName(getCommonClientWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCommonClientEndpointAddress(java.lang.String address) {
        CommonClient_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.stgglobal.client.CommonClient.class.isAssignableFrom(serviceEndpointInterface)) {
                com.stgglobal.client.CommonClientSoapBindingStub _stub = new com.stgglobal.client.CommonClientSoapBindingStub(new java.net.URL(CommonClient_address), this);
                _stub.setPortName(getCommonClientWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CommonClient".equals(inputPortName)) {
            return getCommonClient();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://client.stgglobal.com", "CommonClientService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://client.stgglobal.com", "CommonClient"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CommonClient".equals(portName)) {
            setCommonClientEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
