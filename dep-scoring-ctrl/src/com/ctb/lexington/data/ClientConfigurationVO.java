package com.ctb.lexington.data;

/**
 * ClientConfigurationVO.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Bean used to hold information associated with the
 * <code>CLIENT_CONFIGURATION</code> table.
 * </p>
 * <p>
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 * <p>
 *
 * @author  Giuseppe Gennaro
 * @version $Id$
 */
public class ClientConfigurationVO implements Serializable
{
  /**
   * This beans's static label to be used for identification.
   */
  public static final String BEAN_LABEL
                             = "com.ctb.lexington.bean.ClientConfigurationBean";

  /**
   * This beans's static array label to be used for identification.
   */
  public static final String BEAN_ARRAY_LABEL = BEAN_LABEL + ".array";

  /**
   * The version attribute of the bean.
   */
  private String version;

  /**
   * The customer ID attribute of the bean.
   */
  private Integer customerId;

  /**
   * The protocol attribute of the bean.
   */
  private String protocol;

  /**
   * The server attribute of the bean.
   */
  private String server;

  /**
   * The command path attribute of the bean.
   */
  private String commandPath;

  /**
   * The filename attribute of the bean.
   */
  private String filename;

  /**
   * The media path attribute of the bean.
   */
  private String mediaPath;

  /**
   * The session length attribute of the bean.
   */
  private Integer sessionLength;

  /**
   * The hearbeat interval attribute of the bean.
   */
  private Integer heartbeatInterval;

  /**
   * The response retry attribute of the bean.
   */
  private Integer responseRetry;

  /**
   * The response queue length attribute of the bean.
   */
  private Integer responseQueueLength;

  /**
   * The creation by attribute of the bean.
   */
  private Integer createdBy;

  /**
   * The creation date and time attribute of the bean.
   */
    private Date createdDateTime;

  /**
   * The updated by attribute of the bean.
   */
  private Integer updatedBy;

  /**
   * The updated date and time attribute of the bean.
   */
  private Date updatedDateTime;

  /**
   * The activation status attribute of the bean.
   */
  private String activationStatus;




  /**
   * Constructor.
   */
  public ClientConfigurationVO()
  {
  }

  /**
   * Gets this bean's version.
   *
   * @return  This bean's version.
   */
  public String getVersion()
  {
    return this.version;
  }

  /**
   * Sets this bean's version.
   *
   * @param  version  The version to assign to this bean.
   */
  public void setVersion(String version)
  {
    this.version = version;
  }

  /**
   * Gets this bean's customer ID.
   *
   * return  This bean's customer ID.
   */
  public Integer getCustomerId()
  {
    return this.customerId;
  }

  /**
   * Sets this bean's customer ID.
   *
   * @param  customerID  The customer ID to assign to this bean.
   */
  public void setCustomerId(Integer customerId)
  {
    this.customerId = customerId;
  }

  /**
   * Gets this bean's protocol.
   *
   * return  This bean's protocol.
   */
  public String getProtocol()
  {
    return this.protocol;
  }

  /**
   * Sets this bean's protocol.
   *
   * @param  protocol  The protocol to assign to this bean.
   */
  public void setProtocol(String protocol)
  {
    this.protocol = protocol;
  }

  /**
   * Gets this bean's server.
   *
   * return  This bean's server.
   */
  public String getServer()
  {
    return this.server;
  }

  /**
   * Sets this bean's server.
   *
   * @param  server  The server to assign to this bean.
   */
  public void setServer(String server)
  {
    this.server = server;
  }

  /**
   * Gets this bean's command path.
   *
   * return  This bean's command path.
   */
  public String getCommandPath()
  {
    return this.commandPath;
  }

  /**
   * Sets this bean's command path.
   *
   * @param  commandPath  The command path to assign to this bean.
   */
  public void setCommandPath(String commandPath)
  {
    this.commandPath = commandPath;
  }

  /**
   * Gets this bean's media path.
   *
   * return  This bean's media path.
   */
  public String getMediaPath()
  {
    return this.mediaPath;
  }

  /**
   * Sets this bean's media path.
   *
   * @param  mediaPath  The media path to assign to this bean.
   */
  public void setMediaPath(String mediaPath)
  {
    this.mediaPath = mediaPath;
  }

  /**
   * Gets this bean's filename method.
   *
   * return  This bean's filename method.
   */
  public String getFilename()
  {
    return this.filename;
  }

  /**
   * Sets this bean's filename method.
   *
   * @param  filename  The filename method to assign to this bean
   */
  public void setFilename(String filename)
  {
    this.filename = filename;
  }

  /**
   * Gets this bean's session length.
   *
   * return  This bean's session length.
   */
  public Integer getSessionLength()
  {
    return this.sessionLength;
  }

  /**
   * Sets this bean's session length.
   *
   * @param  sessionLength  The session length to assign to this bean.
   */
  public void setSessionLength(Integer sessionLength)
  {
    this.sessionLength = sessionLength;
  }

  /**
   * Gets this bean's heartbeat interval.
   *
   * return  This bean's heartbeat interval.
   */
  public Integer getHeartbeatInterval()
  {
    return this.heartbeatInterval;
  }

  /**
   * Sets this bean's heartbeat interval.
   *
   * @param  heartbeatInterval  The heartbeat interval to assign to this bean.
   */
  public void setHeartbeatInterval(Integer heartbeatInterval)
  {
    this.heartbeatInterval = heartbeatInterval;
  }

  /**
   * Gets this bean's response queue length.
   *
   * return  This bean's response queue length.
   */
  public Integer getResponseQueueLength()
  {
    return this.responseQueueLength;
  }

  /**
   * Sets this bean's response queue length.
   *
   * @param  responseQueueLength  The response queue length to assign to this
   *                              bean.
   */
  public void setResponseQueueLength(Integer responseQueueLength)
  {
    this.responseQueueLength = responseQueueLength;
  }

  /**
   * Gets this bean's response retry.
   *
   * return  This bean's response retry.
   */
  public Integer getResponseRetry()
  {
    return this.responseRetry;
  }

  /**
   * Sets this bean's response retry.
   *
   * @param  responseRetry  The response retry to assign to this bean
   */
  public void setResponseRetry(Integer responseRetry)
  {
    this.responseRetry = responseRetry;
  }

  /**
   * Gets this bean's "created by".
   *
   * return  This bean's "created by".
   */
  public Integer getCreatedBy()
  {
    return this.createdBy;
  }

  /**
   * Sets this bean's "created by".
   *
   * @param  createdBy  The "created by" to assign to this bean.
   */
  public void setCreatedBy(Integer createdBy)
  {
    this.createdBy = createdBy;
  }

  /**
   * Gets this bean's creation date and time.
   *
   * return  This bean's creation date and time.
   */
  public Date getCreatedDateTime()
  {
    return this.createdDateTime;
  }

  /**
   * Sets this bean's creation date and time.
   *
   * @param  createdDateTime  The creation date and time to assign to this
   *                          bean.
   */
  public void setCreatedDateTime(Date createdDateTime)
  {
    this.createdDateTime = createdDateTime;
  }

  /**
   * Gets this bean's "updated by".
   *
   * return  This bean's "updated by".
   */
  public Integer getUpdatedBy()
  {
    return this.updatedBy;
  }

  /**
   * Sets this bean's "updated by".
   *
   * @param  updatedBy  The "updated by" to assign to this bean.
   */
  public void setUpdatedBy(Integer updatedBy)
  {
    this.updatedBy = updatedBy;
  }

  /**
   * Gets this bean's updated date and time.
   *
   * return  This bean's updated date and time.
   */
  public Date getUpdatedDateTime()
  {
    return this.updatedDateTime;
  }

  /**
   * Sets this bean's updated date and time.
   *
   * @param  updatedDateTime  The updated date and time to assign to this bean.
   */
  public void setUpdatedDateTime(Date updatedDateTime)
  {
    this.updatedDateTime = updatedDateTime;
  }

  /**
   * Gets this bean's activation status.
   *
   * return  This bean's activation status.
   */
  public String getActivationStatus()
  {
    return this.activationStatus;
  }

  /**
   * Sets this bean's activation status.
   *
   * @param  activationStatus  The activation status to assign to this bean.
   */
  public void setActivationStatus(String activationStatus)
  {
    this.activationStatus = activationStatus;
  }

}
