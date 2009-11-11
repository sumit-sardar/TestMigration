package com.ctb.lexington.domain.teststructure;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class StringConstant implements Serializable {
    private final String code;
    private final String description;

    protected StringConstant(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public final String getCode() {
        return code;
    }

    public final String getDescription() {
        return description;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final StringConstant constant = (StringConstant) obj;

        if (code != null ? (!code.equals(constant.code)) : (constant.code != null)) {
            return false;
        }

        return true;
    }

    public final int hashCode() {
        return getCode().hashCode();
    }

    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}