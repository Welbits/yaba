package com.pilasvacias.yaba.modules.soap.emt;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by pablo on 10/13/13.
 * welvi-android
 */
@Root(strict = false)
@Default
public class EmtResult {
    @Element(name = "cReturn")
    EmtRequestInfo requestInfo;

    public EmtRequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(EmtRequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
