
package com.ctb.reporting;

import java.io.PrintWriter;

public interface Formatter {

    void print(PrintWriter writer, boolean isSubReport);
}
