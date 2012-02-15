<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="label.tld" prefix="lb" %>
<lb:bundle baseName="testsessionApplicationResource" />

<netui-data:declareBundle bundlePath="webResources" name="web"/>


<input type="hidden" id="noCompletedTestSessions" name = "noCompletedTestSessions" value="${bundle.web['common.message.user.noCompletedTestSessions.title']}"/>
<input type="hidden" id="noCurrentOrFutureTestSessions" name = "noCurrentOrFutureTestSessions" value="${bundle.web['common.message.user.noCurrentOrFutureTestSessions.title']}"/>
<input type="hidden" id="noTestSessions" name = "noTestSessions" value="${bundle.web['common.message.browse.noTestSessions.message']}"/>
<input type="hidden" id="noStudentTitle" name = "noStudentTitle" value="${bundle.web['sessionList.studentTab.noStuSelected.title']}"/>
<input type="hidden" id="noStudentMsg" name = "noStudentMsg" value="${bundle.web['sessionList.studentTab.noStuSelected.message']}"/>
<input type="hidden" id="noTestMsg" name = "noStudentMsg" value="${bundle.web['sessionList.selectTest.noTestSelected.message']}"/>
<input type="hidden" id="noProctorTitle" name = "noProctorTitle" value="${bundle.web['sessionList.studentTab.noProctorTitle.title']}"/>
<input type="hidden" id="noProctorMsg" name = "noProctorMsg" value="${bundle.web['sessionList.studentTab.noProctorSelected.message']}"/>

<input type="hidden" id="fieldDisabled" name="fieldDisabled" value=<lb:label key="session.edit.fieldDisable" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuLogged" name="stuLogged" value=<lb:label key="session.edit.stuLogged" prefix="'" suffix="'"/>/>
<input type="hidden" id="sessionEnd" name="sessionEnd" value=<lb:label key="session.edit.sessionEnded" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentLogged" name="noStudentLogged" value=<lb:label key="session.edit.noStudentLogged" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentLogged2" name="noStudentLogged2" value=<lb:label key="session.edit.noStudentLoggedMsg" prefix="'" suffix="'"/>/>
<input type="hidden" id="noPermission" name="noPermission" value=<lb:label key="session.edit.noPermission" prefix="'" suffix="'"/>/>
<input type="hidden" id="sessionList" name="sessionList" value=<lb:label key="homePage.grid.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="sessionName" name="sessionName" value=<lb:label key="homepage.grid.sessionName" prefix="'" suffix="'"/>/>
<input type="hidden" id="testName" name="testName" value=<lb:label key="homepage.grid.testName" prefix="'" suffix="'"/>/>
<input type="hidden" id="organization" name="organization" value=<lb:label key="homepage.grid.org" prefix="'" suffix="'"/>/>
<input type="hidden" id="myRole" name="myRole" value=<lb:label key="homepage.grid.myRole" prefix="'" suffix="'"/>/>
<input type="hidden" id="startDateGrid" name="startDateGrid" value=<lb:label key="homepage.grid.startDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="endDateGrid" name="endDateGrid" value=<lb:label key="homepage.grid.endDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="procListGrid" name="procListGrid" value=<lb:label key="proc.grid.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuListGrid" name="stuListGrid" value=<lb:label key="session.menu.studentList" prefix="'" suffix="'"/>/>
<input type="hidden" id="dupStuResolve" name="dupStuResolve" value=<lb:label key="stu.dupStudent.resolve" prefix="'" suffix="'"/>/>
<input type="hidden" id="editTestSn" name="editTestSn" value=<lb:label key="sessionList.editTest.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="printTT" name="printTT" value=<lb:label key="sessionList.testTicket.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuFN" name="testStuFN" value=<lb:label key="stu.info.firstName" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuLN" name="testStuLN" value=<lb:label key="stu.info.lastName" prefix="'" suffix="'"/>/>
<input type="hidden" id="searchSessionID" name = "searchSessionID" value=<lb:label key="testSession.label.searchSessID" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuMI" name="testStuMI" value=<lb:label key="stu.info.mi" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuAccom" name="testStuAccom" value=<lb:label key="stu.info.accom" prefix="'" suffix="'"/>/>
<input type="hidden" id="testStuForm" name="testStuForm" value=<lb:label key="stu.info.form" prefix="'" suffix="'"/>/>
<input type="hidden" id="testListGrid" name="testListGrid" value=<lb:label key="testList.grid.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="testDetLevel" name="testDetLevel" value=<lb:label key="testSession.grid.level" prefix="'" suffix="'"/>/>
<input type="hidden" id="testDetGrade" name="testDetGrade" value=<lb:label key="testSession.grid.grade" prefix="'" suffix="'"/>/>
<input type="hidden" id="testDetsubTest" name="testDetsubTest" value=<lb:label key="testSession.grid.subtest" prefix="'" suffix="'"/>/>
<input type="hidden" id="testDetDuration" name="testDetDuration" value=<lb:label key="selectTest.label.duration" prefix="'" suffix="'"/>/>
<input type="hidden" id="confirmAlrt" name="confirmAlrt" value=<lb:label key="common.alert.confirmAlert" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuRos" name="stuRos" value=<lb:label key="viewStatus.dialog.stuRos" prefix="'" suffix="'"/>/>
<input type="hidden" id="schSession" name="schSession" value=<lb:label key="homepage.button.scheduleSession" prefix="'" suffix="'"/>/>
<input type="hidden" id="schViewSts" name="schViewSts" value=<lb:label key="homepage.button.viewStatus" prefix="'" suffix="'"/>/>
<input type="hidden" id="reqTextmsg" name="reqTextmsg" value=<lb:label key="common.requiredText.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="reqTextMultimsg" name="reqTextMultimsg" value=<lb:label key="common.requiredTextMulti.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="invDateMsg" name="invDateMsg" value=<lb:label key="common.invalidDates.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacSixCharsMsg" name="tacSixCharsMsg" value=<lb:label key="common.tacSixChars.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacSpCharHdrMsg" name="tacSpCharHdrMsg" value=<lb:label key="common.tacSpCharHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacSpCharNAMsg" name="tacSpCharNAMsg" value=<lb:label key="common.tacSpCharNA.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacIdentTacHdrMsg" name="tacIdentTacHdrMsg" value=<lb:label key="common.tacIdentTacHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacIdentTac1Msg" name="tacIdentTac1Msg" value=<lb:label key="common.tacIdentTac1.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tacIdentTac2Msg" name="tacIdentTac2Msg" value=<lb:label key="common.tacIdentTac2.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTacHdrMsg" name="missTacHdrMsg" value=<lb:label key="common.missTacHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTacMsg" name="missTacMsg" value=<lb:label key="common.missTac.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTacHdrMulMsg" name="missTacHdrMulMsg" value=<lb:label key="common.missTacHdrMulti.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTac1Msg" name="missTac1Msg" value=<lb:label key="common.missTac1.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="missTac2Msg" name="missTac2Msg" value=<lb:label key="common.missTac2.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuDelMsg" name="stuDelMsg" value=<lb:label key="common.stuDel.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuAddedMsg" name="stuAddedMsg" value=<lb:label key="common.stuAdded.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="stuDelAllMsg" name="stuDelAllMsg" value=<lb:label key="common.stuDelAll.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="procDelMsg" name="procDelMsg" value=<lb:label key="common.procDel.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="procAddMsg" name="procAddMsg" value=<lb:label key="common.procAdded.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="procDelAllMsg" name="procDelAllMsg" value=<lb:label key="common.procDelAll.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="snNmInChHdrMsg" name="snNmInChHdrMsg" value=<lb:label key="common.snNmInChHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="snNmInChBdyMsg" name="snNmInChBdyMsg" value=<lb:label key="common.snNmInChBdy.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tstLcnInChHdrMsg" name="tstLcnInChHdrMsg" value=<lb:label key="common.tstLcnInChHdr.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="tstLcnInChBdyMsg" name="tstLcnInChBdyMsg" value=<lb:label key="common.tstLcnInChBdy.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="monitorStsValidMsg" name="monitorStsValidMsg" value=<lb:label key="monitorSts.snValid.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="doNotScoreMsg" name="doNotScoreMsg" value=<lb:label key="donotScore.snValid.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="delSessionTitle" name="delSessionTitle" value=<lb:label key="sessionList.deleteTest.title" prefix="'" suffix="'"/>/>	
<input type="hidden" id="deleteSuccessMsg" name="deleteSuccessMsg" value=<lb:label key="session.msg.deleteSuccess" prefix="'" suffix="'"/>/>
<input type="hidden" id="deleteFailureMsg" name="deleteFailureMsg" value=<lb:label key="session.msg.deleteFailure" prefix="'" suffix="'"/>/>
<input type="hidden" id="hashDisplay" name="hashDisplay" value=<lb:label key="selectTest.label.hash" prefix="'" suffix="'"/>/>
<input type="hidden" id="subtestNameDisplay" name="subtestNameDisplay" value=<lb:label key="selectTest.label.subtestName" prefix="'" suffix="'"/>/>
<input type="hidden" id="acsCodeDisplay" name="acsCodeDisplay" value=<lb:label key="selectTest.label.tstAccessCode" prefix="'" suffix="'"/>/>
<input type="hidden" id="durationDisplay" name="durationDisplay" value=<lb:label key="selectTest.label.duration" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentValidMsg" name="noStudentValidMsg" value=<lb:label key="noStudents.snValid.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="noTestSelected" name="noTestSelected" value=<lb:label key="scheduleTest.noTestSelected" prefix="'" suffix="'"/>/>
<input type="hidden" id="restrictedTitle" name="restrictedTitle" value=<lb:label key="showrestrictedstudents.window.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="resSessionName" name="resSessionName" value=<lb:label key="restricted.testSesionName" prefix="'" suffix="'"/>/>
<input type="hidden" id="resStartDate" name="resStartDate" value=<lb:label key="restricted.startDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="resEndDate" name="resEndDate" value=<lb:label key="restricted.endDate" prefix="'" suffix="'"/>/>
<input type="hidden" id="resGridTitle" name="resGridTitle" value=<lb:label key="resStu.grid.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentTitleGrd" name = "noStudentTitleGrd" value=<lb:label key="session.noStuSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noStudentMsgGrd" name = "noStudentMsgGrd" value=<lb:label key="session.noStuSelected.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="noProctorTitleGrd" name = "noProctorTitleGrd" value=<lb:label key="session.noProcSelected.title" prefix="'" suffix="'"/>/>
<input type="hidden" id="noProctorMsgGrd" name = "noProctorMsgGrd" value=<lb:label key="session.noProcSelected.message" prefix="'" suffix="'"/>/>




<input type="hidden" id="noSubtestMsg" name = "noSubtestMsg" value=<lb:label key="session.noSubtest.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="languageDependencyMsg" name = "languageDependencyMsg" value=<lb:label key="session.languageDependency.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="languageLevelMsg" name = "languageLevelMsg" value=<lb:label key="session.languageLevel.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="readingDependencyMsg" name = "readingDependencyMsg" value=<lb:label key="session.readingDependency.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="readingLevelMsg" name = "readingLevelMsg" value=<lb:label key="session.readingLevel.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="mathSubtestsMsg" name = "mathSubtestsMsg" value=<lb:label key="session.mathSubtests.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="scoreCalulatableMsg" name = "scoreCalulatableMsg" value=<lb:label key="session.scoreCalulatable.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="mathLevelMsg" name = "mathLevelMsg" value=<lb:label key="session.mathLevel.message" prefix="'" suffix="'"/>/>
<input type="hidden" id="subtestValidationFailedMsg" name = "subtestValidationFailedMsg" value=<lb:label key="session.subtestValidationFailed.message" prefix="'" suffix="'"/>/>


