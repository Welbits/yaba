package com.pilasvacias.yaba.modules.soap;

import com.pilasvacias.yaba.modules.util.L;

import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public class EmtEnvelopeSerializer {

    private static final Strategy STRATEGY = new VisitorStrategy(new EmtVisitor());
    private static final Persister SERIALIZER = new Persister(STRATEGY);
    private static final EmtEnvelopeSerializer instance = new EmtEnvelopeSerializer();
    private static final String ENVELOPE_TEMPLATE =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                    "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "<soap:Body>\n" +
                    "%s\n" +
                    "</soap:Body>\n" +
                    "</soap:Envelope>";

    public static EmtEnvelopeSerializer getInstance() {
        return instance;
    }

    public String toXML(Object body) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            SERIALIZER.write(body, out);
            String xml = new String(out.toByteArray());
            return String.format(ENVELOPE_TEMPLATE, xml);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromXML(String xml, Class<T> bodyClass) {
        try {
            InputStream inStream = new ByteArrayInputStream(xml.getBytes("utf-8"));
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inStream, "utf-8");
            while (!parser.getName().equals(bodyClass.getSimpleName())) {
                parser.nextTag();
                L.og.d("tag => %s", parser.getName());
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class EmtVisitor implements Visitor {

        @Override public void read(Type type, NodeMap<InputNode> node) throws Exception {

        }

        @Override public void write(Type type, NodeMap<OutputNode> node) throws Exception {
            if (node.getNode().isRoot()) {
                node.getNode().setName(type.getType().getSimpleName());
            }
            node.getNode().getAttributes().remove("class");
        }
    }
}
