package com.reprezen.swaggerparser.model3;

import com.reprezen.swaggerparser.model3.SecurityParameter;
import com.reprezen.swaggerparser.model3.SwaggerObject;
import java.util.Map;
import javax.annotation.Generated;

public interface SecurityRequirement extends SwaggerObject {

    // Requirement
    @Generated("com.reprezen.swaggerparser.jsonoverlay.gen.CodeGenerator")
    Map<String, ? extends SecurityParameter> getRequirements();

    @Generated("com.reprezen.swaggerparser.jsonoverlay.gen.CodeGenerator")
    boolean hasRequirement(String name);

    @Generated("com.reprezen.swaggerparser.jsonoverlay.gen.CodeGenerator")
    SecurityParameter getRequirement(String name);

    @Generated("com.reprezen.swaggerparser.jsonoverlay.gen.CodeGenerator")
    void setRequirements(Map<String, ? extends SecurityParameter> requirements);

    @Generated("com.reprezen.swaggerparser.jsonoverlay.gen.CodeGenerator")
    void setRequirement(String name, SecurityParameter requirement);

    @Generated("com.reprezen.swaggerparser.jsonoverlay.gen.CodeGenerator")
    void removeRequirement(String name);

}