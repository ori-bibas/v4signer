package org.obbs.enums;

public enum ContextAttributes {

    CONTEXT_ATTRIBUTE_AWS_SERVICE_NAME("aws-service-name"),
    CONTEXT_ATTRIBUTE_AWS_REGION("aws-region");

    private final String contextAttribute;

    ContextAttributes(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    public String getContextAttribute() {
        return contextAttribute;
    }

}
