
package com.ctb.contentBridge.core.publish.report;

import java.io.PrintWriter;

public interface Formatter {

    void print(PrintWriter writer, boolean isSubReport);
}
