package com.tuannguyen.framework.util;


import org.json.JSONObject;

public class SafeJSONObject extends JSONObject {
    private JSONObject mJSONObject;
    public SafeJSONObject(JSONObject jsonObject) {
        this.mJSONObject = jsonObject;
    }


}
