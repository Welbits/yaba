package com.pilasvacias.yaba.modules.soap;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

/**
 * Created by pablo on 10/11/13.
 * welvi-android
 */
@Root(name = "soap:Envelope", strict = false)
@Default
@NamespaceList({
        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap", reference = "http://schemas.xmlsoap.org/soap/envelope/")
})
public class Envelope<T> {

    @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/")
    @Element(name = "soap:Body", required = true)
    T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
