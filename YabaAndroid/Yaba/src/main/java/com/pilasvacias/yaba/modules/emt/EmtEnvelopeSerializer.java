package com.pilasvacias.yaba.modules.emt;

import com.pilasvacias.yaba.modules.emt.models.EmtBody;

import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import java.io.ByteArrayOutputStream;

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

    public String toXML(EmtBody body) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            SERIALIZER.write(body, out);
            String xml = new String(out.toByteArray());
            // Nazismo time, fuck Soap y sus mierdas anidadas que no sirven
            // para absolutamente nada y que junto con EMT meten 4 PUTOS NIVLES
            // DE ETIQUETAS XML VACÍAS QUE HACE IMPOSIBLE METERLAS EN UN
            // PARSER QUE LLEVA AÑOS EN DESARROLLO Y TIENE TODAS LAS
            // FUNCIONALIDADES INIMAGINABLES MENOS ESA PORQUE NO ES FUNCIONALIDAD, ES
            // PURO RETRASO MENTAL.
            String action = body.getSoapAction();
            xml = String.format(xml, action, action);
            return String.format(ENVELOPE_TEMPLATE, xml);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T fromXML(String xml, Class<T> bodyClass) {

        // Nazismo time 2x, se quitan las 2 capas inútiles
        // de Soap + las 2 de EMT parseando el XML a mano.
        final String targetTag = bodyClass.getSimpleName();
        final String openTag = "<" + targetTag + ">";
        final String closeTag = "</" + targetTag + ">";
        int first = xml.indexOf(openTag);
        int end = xml.indexOf(closeTag);
        xml = xml.substring(first, end + closeTag.length());

        try {
            if (first == -1 || end == -1)
                throw new ClassNotFoundException("body class is not in the EMT response, " +
                        "make sure the name of the class" +
                        "and the name of the response are exactly the same (eg: GetGroupsResult)");
            return SERIALIZER.read(bodyClass, xml, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static class EmtVisitor implements Visitor {

        @Override public void read(Type type, NodeMap<InputNode> node) throws Exception {
        }

        @Override public void write(Type type, NodeMap<OutputNode> node) throws Exception {
            //The SOAPAction and the class name must Match, so force it in the root element.
            //Introduce two format strings that latter will be replaced
            if (node.getNode().isRoot()) {
                node.getNode().setName("%s");
            }
            //To avoid com.pack.Something attributes for List<Something>
            node.getNode().getAttributes().remove("class");
        }
    }
}
