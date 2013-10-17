package com.pilasvacias.yaba.modules.emt;

import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.util.L;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public class EmtEnvelopeSerializer {

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

    public static String extractXmlElement(String xml, String open, String close, boolean keepParent) {
        final String openTag = "<" + open + ">";
        final String closeTag = "</" + close + ">";
        int begin = xml.indexOf(openTag);
        int end = xml.lastIndexOf(closeTag);
        if (begin == -1 || end == -1)
            return "";
        if (keepParent)
            return xml.substring(begin, end + closeTag.length());
        else
            return xml.substring(begin + open.length() + 2, end);

    }

    /**
     * @param xml format <Tag></Tag><Tag></Tag>...
     * @return Array with [<Tag></Tag>, <Tag></Tag>...]
     */
    public static String[] extractXmlArray(String xml, String tag) {
        final String openTag = tag + ">";
        final String closeTag = "</" + tag;
        String pattern = openTag + "(\\n|\\r\\n|\\s)*" + closeTag;
        String replacement = openTag + "________" + closeTag;
        return xml.replaceAll(pattern, replacement).split("________");
    }

    private static void closeStream(InputStream stream) {
        if (stream != null)
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public String toXML(EmtBody body) {
        try {
            // Nazismo time, fuck Soap y sus mierdas anidadas que no sirven
            // para absolutamente nada y que junto con EMT meten 4 PUTOS NIVLES
            // DE ETIQUETAS XML VACÍAS QUE HACE IMPOSIBLE METERLAS EN UN
            // PARSER QUE LLEVA AÑOS EN DESARROLLO Y TIENE TODAS LAS
            // FUNCIONALIDADES INIMAGINABLES MENOS ESA PORQUE NO ES FUNCIONALIDAD, ES
            // PURO RETRASO MENTAL.
            XStream xStream = new XStream();
            xStream.alias(body.getSoapAction(), body.getClass());

            xStream.autodetectAnnotations(true);
            String xml;
            xml = xStream.toXML(body);
            //xml = String.format(xml, action, action);
            return String.format(ENVELOPE_TEMPLATE, xml);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> EmtData<T> fromXML(String xml, Class<T> responseType, EmtBody body) {

        XStream xStream = new XStream();
        xStream.alias("DESCRIPCION", String.class);
        xStream.alias("RESULTADO", Integer.class);
        xStream.alias("REG", responseType);
        String tag = body.getSoapAction() + "Result";
        xml = extractXmlElement(xml, tag, tag, false);
        EmtData<T> result = new EmtData<T>();

        ObjectInputStream stream = null;
        try {
            stream = xStream.createObjectInputStream(new ByteArrayInputStream(xml.getBytes()));

            Object object = null;
            do {
                object = stream.readObject();

                if (object instanceof String)
                    result.getEmtInfo().setDescription((String) object);
                else if (object instanceof Integer)
                    result.getEmtInfo().setResultCode((Integer) object);
                else
                    result.getPayload().add((T) object);
            } while (object != null);

        } catch (EOFException e){
            //everything is read
        } catch (IOException e) {
            e.printStackTrace();
            L.og.e("Unable to parser response for %s", body.getSoapAction());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            L.og.e("Unable to read object for %s", body.getSoapAction());
        } finally {
            closeStream(stream);
        }

        return result;
    }

    @XStreamAlias(value = "soap:Body")
    public static class Body {

    }

    private static class EnvelopeReader implements Converter {

        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            return null;
        }

        @Override public boolean canConvert(Class type) {
            return type == Body.class;
        }
    }
}
