<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>

<netui-data:declareBundle bundlePath="selectOptions" name="options" />
<% 
    String testTitle = (String) request.getAttribute( "testTitle" );
    testTitle = testTitle.replaceAll( "/", " " );
%>
<netui:html>
    <head>
        <title>
            Content Preview
        </title>
        
    <script type="text/javascript">
      function openPreviewer() {
        //document.open('about:blank', 'previewWindow', 'fullscreen=yes');
        window.open('about:blank', 'previewWindow', 'bgcolor=#6691B4,fullscreen=yes');
        setTimeout("document.forms[0].submit();", 50);
      }
      
      function getTestName()
      {
            name = prompt( 'Please enter test name.  This can take a while.', document.getElementById("testname").value );
            if ( null == name || null === name || 'null' == name )
            {
                return false;
            }
            else
            {
                document.getElementById("testname").value = name;
                return true;
            }
      }
      
      function getTestName1()
      {
            name = prompt( 'Please enter test name. This can take a while.', document.getElementById("testname1").value );
            if ( null == name || null === name || 'null' == name )
            {
                return false;
            }
            else
            {
                document.getElementById("testname1").value = name;
                return true;
            }
      }
      
      function getTestName2()
      {
            name = prompt( 'Please enter test name. This can take a while.', document.getElementById("testname2").value );
            if ( null == name || null === name || 'null' == name )
            {
                return false;
            }
            else
            {
                document.getElementById("testname2").value = name;
                return true;
            }
      }
    </script>    
    </head>
    <body>
        <netui:form target="previewWindow" method="post" action="preview">
            <p> Choose subtest: </p>
            <netui:label value="{pageFlow.assessmentTitle}" defaultValue="&nbsp;"></netui:label>            
            <table class="tablebody" border="0" cellpadding="0" cellspacing="0"> 
            <tr valign="top">
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>
            <netui-data:repeater dataSource="{pageFlow.schedulableUnits}">
                <netui-data:repeaterHeader>
                    <table class="tablebody" border="0" cellpadding="0" cellspacing="0">   
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                    <tr valign="top">
                        <td><netui:label value="{container.item.title}" defaultValue="&nbsp;"></netui:label></td>
                        <td>&nbsp;</td>
                        </tr>
                        <tr valign="top">
                        <td>&nbsp;</td>
                        <td>
                        <netui-data:repeater dataSource="{container.item.deliverableUnits}">
                            <netui-data:repeaterHeader>
                                <table class="tablebody" border="0" cellpadding="0" cellspacing="0">
                            </netui-data:repeaterHeader>
                            <netui-data:repeaterItem>
                                <tr valign="top">
                                    <td>
                                        <netui:radioButtonGroup dataSource="{actionForm.deliverableUnit}">                                    
                                            <netui:radioButtonOption value="{container.item.index}"></netui:radioButtonOption>
                                        </netui:radioButtonGroup>
                                    </td>
                                    <td>
                                        <netui:label value="{container.item.title}" defaultValue="&nbsp;"></netui:label>
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
            <table>
            <tr valign="top">
                <td>Student First Name:</td>
                <td>
                    <netui:textBox dataSource="{actionForm.studentFirstName}"/>
                </td>
                </tr>
                <tr valign="top">
                    <td>Student Last Name:</td>
                    <td>
                    <netui:textBox dataSource="{actionForm.studentLastName}"/>
                    </td>
                </tr>
            </table>
            <br/>
            <div>Accommodation Settings:</div>
            <table>
                <tr>
                    <td width="24" height="38"><netui:image src="./resources/images/transparent.gif" width="24" height="24" border="0"/></td>
                    <td>
                        <table>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="{actionForm.screenReader}"/>Allow Screen Reader
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="{actionForm.calculator}"/>Calculator
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="{actionForm.rest_break}"/>Test Pause
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="{actionForm.untimed}"/>Untimed Test
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                             <tr valign="top">
                                <td>
                                <netui:checkBox dataSource="{actionForm.highlighter}"/>Highlighter
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
                    <td width="24" height="38"><netui:image src="./resources/images/transparent.gif" width="24" height="24" border="0"/></td>
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
                                    <netui:select nullable="true" dataSource="{actionForm.questionBgColor}">
                                        <netui:selectOption style="background-color: #CCECFF;" value="{bundle.options['color.lightblue']}">Light Blue     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFCCCC;" value="{bundle.options['color.lightpink']}">Light Pink     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFB0;" value="{bundle.options['color.lightyellow']}">Light Yellow   </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="{bundle.options['color.white']}">White          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="{bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #000000;" value="{bundle.options['color.black']}">Black          </netui:selectOption>
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
                                    <netui:select nullable="true" dataSource="{actionForm.questionFgColor}">
                                        <netui:selectOption style="background-color: #00CC00;" value="{bundle.options['color.green']}">Green          </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFF99;" value="{bundle.options['color.yellow']}">Yellow         </netui:selectOption>
                                        <netui:selectOption style="background-color: #663300;" value="{bundle.options['color.darkbrown']}">Dark Brown     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="{bundle.options['color.white']}">White          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="{bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #000000;" value="{bundle.options['color.black']}">Black          </netui:selectOption>
                                    </netui:select>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Font Size:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <netui:select nullable="true" dataSource="{actionForm.questionFontSize}">
                                        <netui:selectOption  value="{bundle.options['fontsize.standard']}">Standard       </netui:selectOption>
                                        <netui:selectOption  value="{bundle.options['fontsize.larger']}">Larger         </netui:selectOption>
                                        <netui:selectOption  value="{bundle.options['fontsize.largest']}">Largest        </netui:selectOption>
                                    </netui:select>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="4">&nbsp;</td></tr>
                        </table></td></tr>
                        </table>
                        </td> 
                        <td width="24" height="38"><netui:image src="./resources/images/transparent.gif" width="24" height="24" border="0"/></td>
                        <td>
                            <table border="1" cellspacing="0" cellpadding="0" bordercolor="#FFCC66"><tr><td>
                            <table border="0" cellspacing="0" cellpadding="0" bgcolor="#CCC980">
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Background Color:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <netui:select nullable="true" dataSource="{actionForm.answerBgColor}">
                                        <netui:selectOption style="background-color: #CCECFF;" value="{bundle.options['color.lightblue']}">Light Blue     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFCCCC;" value="{bundle.options['color.lightpink']}">Light Pink     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFB0;" value="{bundle.options['color.lightyellow']}">Light Yellow   </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="{bundle.options['color.white']}">White          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="{bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #000000;" value="{bundle.options['color.black']}">Black          </netui:selectOption>
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
                                    <netui:select nullable="true" dataSource="{actionForm.answerFgColor}">
                                        <netui:selectOption style="background-color: #00CC00;" value="{bundle.options['color.green']}">Green          </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFF99;" value="{bundle.options['color.yellow']}">Yellow         </netui:selectOption>
                                        <netui:selectOption style="background-color: #663300;" value="{bundle.options['color.darkbrown']}">Dark Brown     </netui:selectOption>
                                        <netui:selectOption style="background-color: #FFFFFF;" value="{bundle.options['color.white']}">White          </netui:selectOption>
                                        <netui:selectOption style="background-color: #000080;" value="{bundle.options['color.darkblue']}">Dark Blue      </netui:selectOption>
                                        <netui:selectOption style="background-color: #000000;" value="{bundle.options['color.black']}">Black          </netui:selectOption>
                                    </netui:select>
                                </td>
                                <td width="10">&nbsp;</td>
                            </tr>
                            <tr><td height="20" colspan="5">&nbsp;</td></tr>
                            <tr valign="top">
                                <td width="10">&nbsp;</td>
                                <td>Font Size:</td>
                                <td width="20">&nbsp;</td>
                                <td>
                                    <netui:select nullable="true" dataSource="{actionForm.answerFontSize}">
                                        <netui:selectOption  value="{bundle.options['fontsize.standard']}">Standard       </netui:selectOption>
                                        <netui:selectOption  value="{bundle.options['fontsize.larger']}">Larger         </netui:selectOption>
                                        <netui:selectOption  value="{bundle.options['fontsize.largest']}">Largest        </netui:selectOption>
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
                <netui:label value="Option Eliminator:   ">Option Eliminator:   </netui:label>
                <netui:radioButtonGroup dataSource="{actionForm.eliminatorResource}">
                    <netui:radioButtonOption value="resources/eliminator.swf">On top of selector &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</netui:radioButtonOption>
                    <netui:radioButtonOption value="resources/eliminatorFCAT.swf">To the left of selector</netui:radioButtonOption>
                </netui:radioButtonGroup>
            <br/>    
            <br/>
            <netui:button value="preview" type="button" onClick="openPreviewer(); return false;"/>
            <br/>
        </netui:form>
        <netui:anchor action="viewAnswerKey">Answer Key</netui:anchor>
        <br/>
        <br/>
        <netui:anchor action="viewAcknowledgements">Acknowledgements</netui:anchor>
        <br/>
        <br/>
        <br/>
        <br/>
        <netui:form method="get" action="doEditorReveiw">
        <netui:button value="Submit for Editor Reveiw" type="submit" action="doEditorReveiw"/>
        <netui:label value="{pageFlow.editorReviewURL}" defaultValue="&nbsp;"></netui:label>
        </netui:form>
        <br/>
        <br/>
        <table class="tablebody" border="0" cellpadding="0" cellspacing="0"> 
        <tr valign="top">
        <td>
        <netui:form method="get" action="doCQAReview">
        <input type="radio" name="state" value="DEMO" >Demo</input><br/>
        <input type="radio" name="state" value="ISTEP" >Indiana</input><br/>
        <input type="radio" name="state" value="FL" >Florida</input><br/>
        <input type="radio" name="state" value="KY" checked >Kentuchy</input><br/>
        <input type="radio" name="state" value="OK" >Oklahoma</input><br/>
        <input type="radio" name="state" value="CTB" >CTB</input><br/>
        <netui:button value="Submit for CQA Demo" type="submit" action="doCQAReview" onClick="return getTestName();"/>
        <netui:label value="{pageFlow.CQAReviewMessage}" defaultValue="&nbsp;"></netui:label>
        <input type="hidden" name="testname" id="testname" value="<%=testTitle%>" >
        </netui:form>
        <td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td>
        <netui:form method="get" action="doQAReview">
        <input type="radio" name="state1" value="DEMO" >Demo</input><br/>
        <input type="radio" name="state1" value="ISTEP" >Indiana</input><br/>
        <input type="radio" name="state1" value="FL" >Florida</input><br/>
        <input type="radio" name="state1" value="KY" checked >Kentuchy</input><br/>
        <input type="radio" name="state1" value="OK" >Oklahoma</input><br/>
        <netui:button value="Submit for QA Demo" type="submit" action="doQAReview" onClick="return getTestName1();"/>
        <netui:label value="{pageFlow.QAReviewMessage}" defaultValue="&nbsp;"></netui:label>
        <input type="hidden" name="testname1" id="testname1" value="<%=testTitle%>" >
        </netui:form>
        </td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td>
        <netui:form method="get" action="doDEVReview">
        <input type="radio" name="state2" value="DEMO" >Demo</input><br/>
        <input type="radio" name="state2" value="ISTEP" >Indiana</input><br/>
        <input type="radio" name="state2" value="FL" >Florida</input><br/>
        <input type="radio" name="state2" value="KY" checked >Kentuchy</input><br/>
        <input type="radio" name="state2" value="OK" >Oklahoma</input><br/>
        <netui:button value="Submit for DEV Demo" type="submit" action="doDEVReview" onClick="return getTestName2();"/>
        <netui:label value="{pageFlow.DEVReviewMessage}" defaultValue="&nbsp;"></netui:label>
        <input type="hidden" name="testname2" id="testname2" value="<%=testTitle%>" >
        </netui:form>
        </td>
        </tr></table>
    </body>
</netui:html>