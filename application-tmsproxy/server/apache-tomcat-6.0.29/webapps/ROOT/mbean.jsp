<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.management.*" %>
<%@ page import="javax.management.*" %>

<!--
  A little JMX Server inspection tool. This JSP allows you to list all attributes on all mbeans
  in all mbean servers in your JVM. We make a reasonable attempt to also print the value of each
  attribute, although we don't dig into the more complex structures. JConsole is a much better
  tool for that.

  And yes, the resulting list is *huge*. :-)

  Written by Kees Jan Koster <kjkoster@kjkoster.org>
-->

<%!
    private static void spillTheBeans(final Writer out) throws Exception {
        final PrintWriter os = new PrintWriter(out);
        os.println("<table>");

        final List<MBeanServer> servers = new LinkedList<MBeanServer>();
        servers.add(ManagementFactory.getPlatformMBeanServer());
        servers.addAll(MBeanServerFactory.findMBeanServer(null));
        for (final MBeanServer server : servers) {
            os.println("  <tr><td colspan='4'>&nbsp;</td></tr>");
            os.println("  <tr><td>Server:</td><td colspan='3'>"
                    + server.getClass().getName() + "</td></tr>");

            final Set<ObjectName> mbeans = new HashSet<ObjectName>();
            mbeans.addAll(server.queryNames(null, null));
            for (final ObjectName mbean : mbeans) {
                os.println("  <tr><td colspan='4'>&nbsp;</td></tr>");
                os.println("  <tr><td>MBean:</td><td colspan='3'>" + mbean
                        + "</td></tr>");

                final MBeanAttributeInfo[] attributes = server.getMBeanInfo(
                        mbean).getAttributes();
                for (final MBeanAttributeInfo attribute : attributes) {
                    os.print("  <tr><td>&nbsp;</td><td>" + attribute.getName()
                            + "</td><td>" + attribute.getType() + "</td><td>");

                    try {
                        final Object value = server.getAttribute(mbean,
                                attribute.getName());
                        if (value == null) {
                            os.print("<font color='#660000'>null</font>");
                        } else {
                            os.print(value.toString());
                        }
                    } catch (Exception e) {
                        os.print("<font color='#990000'>" + e.getMessage()
                                + "</font>");
                    }

                    os.println("</td></tr>");
                }
            }
        }

        os.println("</table>");
        os.flush();
    }
%>

<html>
  <head><title>Spill the Beans -- JMX Mbean Listing</title></head>
  <body>
    <% spillTheBeans(out); %>
  </body>
</body>