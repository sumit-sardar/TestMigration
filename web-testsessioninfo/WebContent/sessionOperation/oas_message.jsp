<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle bundlePath="webResources" name="web"/>


<input type="hidden" id="noCompletedTestSessions" name = "noCompletedTestSessions" value="${bundle.web['common.message.user.noCompletedTestSessions.title']}"/>
<input type="hidden" id="noCurrentOrFutureTestSessions" name = "noCurrentOrFutureTestSessions" value="${bundle.web['common.message.user.noCurrentOrFutureTestSessions.title']}"/>
<input type="hidden" id="noTestSessions" name = "noTestSessions" value="${bundle.web['common.message.browse.noTestSessions.message']}"/>
