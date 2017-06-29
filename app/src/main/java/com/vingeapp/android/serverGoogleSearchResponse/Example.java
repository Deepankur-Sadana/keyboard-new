
package com.vingeapp.android.serverGoogleSearchResponse;

import java.util.HashMap;
import java.util.Map;

public class Example {

    private Boolean error;
    private Result result;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
