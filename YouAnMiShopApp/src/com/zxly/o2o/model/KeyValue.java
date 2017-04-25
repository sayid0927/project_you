package com.zxly.o2o.model;

/**
 * Created by dsnx on 2015/12/22.
 */
public class KeyValue {
    private String key;
    private String value;

    public KeyValue(String key,String value)
    {
        this.key=key;
        this.value=value;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
