package com.pilasvacias.yaba.modules.soap;

import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayOutputStream;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public class EnvelopeSerializer {

    private static final SoapVisitorStrategy STRATEGY =
            new SoapVisitorStrategy(new SoapVisitorStrategy.SoapVisitor(null));
    private static final Persister SERIALIZER = new Persister(STRATEGY);

    public String toXML(Object body) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Envelope<Object> envelope = new Envelope<Object>();
        envelope.setBody(body);

        try {
            SERIALIZER.write(envelope, out);
            return new String(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromXML(String xml, Class<T> bodyClass) {
        synchronized (this) {
            STRATEGY.setVisitor(new SoapVisitorStrategy.SoapVisitor(bodyClass));
            try {
                return (T) SERIALIZER.read(Envelope.class, xml).getBody();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
