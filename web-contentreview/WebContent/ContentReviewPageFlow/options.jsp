<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="selectOptions" name="options"/>
<netui:html>
    <head>
        <title>
            Content Review
        </title>
                
    <script type="text/javascript">
        function openPreviewer() 
        {
            window.open('about:blank', 'previewWindow', 'bgcolor=#6691B4,fullscreen=yes');
        }
        
        function goBack()
        {
            document.forms[0].target = "";
            document.forms[0].submit();
        } 
        
        function stayWithColorSet()
        {
            document.forms[0].target = "";
            document.forms[0].submit();
        }
    </script>    
    </head>
    <body>
    <jsp:include page="header.jsp" />  
    <p>Select options to view your test under various conditions, and click Preview to view the results.
    </p>
        <netui:form target="previewWindow" method="post" action="preview">
        <netui-data:getData resultId="questionBgColor" value="${actionForm.questionBgColor}"/>
        <netui-data:getData resultId="answerBgColor" value="${actionForm.answerBgColor}"/>

            <div><strong>Subtest:</strong></div>
            <netui-data:repeater dataSource="globalApp.SchedulableUnits">
                <netui-data:repeaterHeader>
                    <table class="" border="0" cellpadding="0" cellspacing="0">   
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                    <tr valign="top">
                        <td><netui:span value="${container.item.title}" defaultValue="&nbsp;"/></td>
                        <td>&nbsp;</td>
                        </tr>
                        <tr valign="top">
                        <td>&nbsp;</td>
                        <td>
                        <netui-data:repeater dataSource="container.item.deliverableUnits">
                            <netui-data:repeaterHeader>
                                <table class="" border="0" cellpadding="0" cellspacing="0">
                            </netui-data:repeaterHeader>
                            <netui-data:repeaterItem>
                                <tr valign="top">
                                    <td>
                                        <netui:radioButtonGroup dataSource="pageFlow.subtestTitle">   
                                            <netui:radioButtonOption value="${container.item.title}"/>
                                        </netui:radioButtonGroup>
                                    </td>
                                </tr>
                            </netui-data:repeaterItem>
                            <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
                        </netui-data:repeater>
                        </td>
                    </tr>
                </netui-data:repeaterItem>
                <br/>
                <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
            </netui-data:repeater>
            </td>
            </tr>
            </table>
            <br/>
            <p><strong>Select Accommodations (optional):</strong><br>This section allows you to simulate an accommodated test environment. The options you select in this section will not change test settings for students.</p>
            <table class="">
            <tr valign="top">
                <td>Student First Name:</td>
                <td>
                    <netui:textBox dataSource="actionForm.studentFirstName"/>
                </td>
                </tr>
                <tr valign="top">
                    <td>Student Last Name:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.studentLastName"/>
                    </td>
                </tr>
            </table>
            <br/>
            <div>Accommodation Settings:</div>
            <table class="">
                <tr>
                    <td width="24" height="38"><netui:image src=".././resources/images/transparent.gif" width="24" height="24" border="0"/></td>
                    <td>
                        <table>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="actionForm.screenReader"/>Allow Screen Reader
								</td>
                                <td>&nbsp;</td>
                            </tr>
                             <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="actionForm.speedAdjustment"/>Screen Reader Speed Adjustment
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="actionForm.calculator"/>Calculator
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="actionForm.rest_break"/>Test Pause
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="actionForm.untimed"/>Untimed Test
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="actionForm.highlighter"/>Highlighter
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
                </table>
                <br>
                <br>
                <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>Question Settings:</td>
                    <td width="24" height="38"><netui:image src=".././resources/images/transparent.gif" width="24" height="24" border="0"/></td>
                    <td>Answer Settings:</td>
                </tr>
                    <tr>
                        <td>
                        <table border="1" cellspacing="0" cellpadding="0" bordercolor="#FFCC66"><tr><td>
                        <table border="0" cellspacing="0" cellpadding="0" bgcolor="#CCC980">
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Background Color:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <netui:select nullable="true" dataSource="actionForm.questionBgColor" onChange="stayWithColorSet(); return false;">
                                        <netui:selectOption style="background-color: #CCECFF;" value="${bundle.options['color.lightblue']}">Light Blue     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFCCCC;" value="${bundle.options['color.lightpink']}">Light Pink     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFB0;" value="${bundle.options['color.lightyellow']}">Light Yellow   </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="${bundle.options['color.white']}">White          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="${bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #000000;" value="${bundle.options['color.black']}">Black          </netui:selectOption>
                                    </netui:select>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Font Color:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <c:if test="${questionBgColor == null}"> 
                                    <netui:select nullable="true" dataSource="actionForm.questionFgColor">
                                        <netui:selectOption style="background-color: #00CC00;" value="${bundle.options['color.green']}">Green          </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFF99;" value="${bundle.options['color.yellow']}">Yellow         </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="${bundle.options['color.white']}">White          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000000;" value="${bundle.options['color.black']}">Black          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="${bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #663300;" value="${bundle.options['color.darkbrown']}">Dark Brown      </netui:selectOption>
                                    </netui:select>
                                    </c:if>
                                    <c:if test="${questionBgColor == '0x000000'}"> 
                                    <netui:select nullable="true" dataSource="actionForm.questionFgColor">
                                        <netui:selectOption style="background-color: #00CC00;" value="${bundle.options['color.green']}">Green          </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFF99;" value="${bundle.options['color.yellow']}">Yellow         </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="${bundle.options['color.white']}">White          </netui:selectOption>
                                    </netui:select>
                                    </c:if>
                                    <c:if test="${questionBgColor == '0x000080'}"> 
                                    <netui:select nullable="true" dataSource="actionForm.questionFgColor">
                                        <netui:selectOption style="background-color: #FFFFFF;" value="${bundle.options['color.white']}">White          </netui:selectOption>
                                    </netui:select>
                                    </c:if>
                                    <c:if test="${questionBgColor == '0xFFFFFF' 
                                                || questionBgColor == '0xCCECFF'
                                                || questionBgColor == '0xFFCCCC' 
                                                || questionBgColor == '0xFFFFB0'}"> 
                                    <netui:select nullable="true" dataSource="actionForm.questionFgColor">
                                        <netui:selectOption style="background-color: #000000;" value="${bundle.options['color.black']}">Black          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="${bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #663300;" value="${bundle.options['color.darkbrown']}">Dark Brown      </netui:selectOption>
                                    </netui:select>
                                    </c:if>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Font Size:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <netui:select nullable="true" dataSource="actionForm.questionFontSize">
                                        <netui:selectOption value="${bundle.options['fontsize.standard']}">Standard       </netui:selectOption>
                                        <netui:selectOption value="${bundle.options['fontsize.larger']}">Larger         </netui:selectOption>
                                        <netui:selectOption value="${bundle.options['fontsize.largest']}">Largest        </netui:selectOption>
                                    </netui:select>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="4">&nbsp;</td></tr>
                        </table></td></tr>
                        </table>
                        </td> 
                        <td width="24" height="38"><netui:image src=".././resources/images/transparent.gif" width="24" height="24" border="0"/></td>
                        <td>
                            <table class="" border="1" cellspacing="0" cellpadding="0" bordercolor="#FFCC66"><tr><td>
                            <table class="" border="0" cellspacing="0" cellpadding="0" bgcolor="#CCC980">
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Background Color:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <netui:select nullable="true" dataSource="actionForm.answerBgColor" onChange="stayWithColorSet(); return false;">
                                        <netui:selectOption style="background-color: #CCECFF;" value="${bundle.options['color.lightblue']}">Light Blue     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFCCCC;" value="${bundle.options['color.lightpink']}">Light Pink     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFB0;" value="${bundle.options['color.lightyellow']}">Light Yellow   </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="${bundle.options['color.white']}">White          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="${bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #000000;" value="${bundle.options['color.black']}">Black          </netui:selectOption>
                                    </netui:select>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Font Color:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <c:if test="${answerBgColor == null}"> 
                                    <netui:select nullable="true" dataSource="actionForm.answerFgColor">
                                        <netui:selectOption style="background-color: #00CC00;" value="${bundle.options['color.green']}">Green          </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFF99;" value="${bundle.options['color.yellow']}">Yellow         </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="${bundle.options['color.white']}">White          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000000;" value="${bundle.options['color.black']}">Black          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="${bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #663300;" value="${bundle.options['color.darkbrown']}">Dark Brown      </netui:selectOption>
                                    </netui:select>
                                    </c:if>
                                    <c:if test="${answerBgColor == '0x000000'}"> 
                                    <netui:select nullable="true" dataSource="actionForm.answerFgColor">
                                        <netui:selectOption style="background-color: #00CC00;" value="${bundle.options['color.green']}">Green          </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFF99;" value="${bundle.options['color.yellow']}">Yellow         </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="${bundle.options['color.white']}">White          </netui:selectOption>
                                    </netui:select>
                                    </c:if>
                                    <c:if test="${answerBgColor == '0x000080'}"> 
                                    <netui:select nullable="true" dataSource="actionForm.answerFgColor">
                                        <netui:selectOption style="background-color: #FFFFFF;" value="${bundle.options['color.white']}">White          </netui:selectOption>
                                    </netui:select>
                                    </c:if>
                                    <c:if test="${answerBgColor == '0xFFFFFF' 
                                                || answerBgColor == '0xCCECFF'
                                                || answerBgColor == '0xFFCCCC' 
                                                || answerBgColor == '0xFFFFB0'}"> 
                                    <netui:select nullable="true" dataSource="actionForm.answerFgColor">
                                        <netui:selectOption style="background-color: #000000;" value="${bundle.options['color.black']}">Black          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="${bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #663300;" value="${bundle.options['color.darkbrown']}">Dark Brown      </netui:selectOption>
                                    </netui:select>
                                    </c:if>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Font Size:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <netui:select nullable="true" dataSource="actionForm.answerFontSize">
                                        <netui:selectOption value="${bundle.options['fontsize.standard']}">Standard       </netui:selectOption>
                                        <netui:selectOption value="${bundle.options['fontsize.larger']}">Larger         </netui:selectOption>
                                        <netui:selectOption value="${bundle.options['fontsize.largest']}">Largest        </netui:selectOption>
                                    </netui:select>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="4">&nbsp;</td></tr>
                            </table></td></tr>
                            </table>
                        </td>
                    </tr>
                </table>
                
                <br/>
                <netui:span value="Option Eliminator:   ">Option Eliminator:   </netui:span>
                <netui:radioButtonGroup dataSource="actionForm.eliminatorResource">
                    <netui:radioButtonOption value="/ContentReviewWeb/resources/eliminator.swf">On top of selector &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</netui:radioButtonOption>
                    <netui:radioButtonOption value="/ContentReviewWeb/resources/eliminatorFCAT.swf">To the left of selector</netui:radioButtonOption>
                </netui:radioButtonGroup>
            <br/>    

            <br/>
            <input name="back" type="submit" value="Back" onclick="goBack(); return true;"/>
            <netui:image src=".././resources/images/transparent.gif" width="24" height="24" border="0"/>
            <input name="preview" type="submit" value="Preview" onclick="openPreviewer(); return true;"/>
            <br/>
        </netui:form>
    </body>
</netui:html>