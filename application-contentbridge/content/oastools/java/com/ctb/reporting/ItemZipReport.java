package com.ctb.reporting;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class ItemZipReport implements Report {
	public boolean isSuccess() {
		return true;
	}
	public String toString(boolean isSubReport) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        FormatterFactory.create(this).print(
            new PrintWriter(new PrintStream(o), true),
            isSubReport);
        return o.toString();
    }

    public String toString() {
        return this.toString(false);
    }
}
