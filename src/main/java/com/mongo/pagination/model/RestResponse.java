package com.mongo.pagination.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse {
    private boolean success = true;
    private Object result;

    public RestResponse(boolean success) {
        this.success = success;
    }

    public RestResponse(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }

    public RestResponse(Object body) {
        this.result = body;
    }
}
