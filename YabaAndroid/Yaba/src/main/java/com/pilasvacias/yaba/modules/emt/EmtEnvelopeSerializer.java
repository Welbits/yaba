package com.pilasvacias.yaba.modules.emt;

import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.util.L;
import com.thoughtworks.xstream.XStream;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by Pablo Orgaz - 10/21/13 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * This class is responsible for writing and reading Emt responses. It uses SOAP format
 * data (envelopes) for communicating.
 */
public class EmtEnvelopeSerializer {

    private static final EmtEnvelopeSerializer instance = new EmtEnvelopeSerializer();
    /**
     * Since the template for a envelope is always the same we can speed things a little
     * using a template and {@link String#format(String, Object...)}
     */
    private static final String ENVELOPE_TEMPLATE =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                    "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "<soap:Body>\n" +
                    "%s\n" + // <----- Data goes here
                    "</soap:Body>\n" +
                    "</soap:Envelope>";

    public static EmtEnvelopeSerializer getInstance() {
        return instance;
    }

    /**
     * Finds and extracts and XML string between the two tags specified.
     * <p/>
     * Calling this method with {@code tag = "tag"}
     * <p/>
     * {@code
     * <p/>
     * <tag>
     * <data>...</data>
     * </tag>
     * <p/>
     * =>  <data>...</data>
     * }
     *
     * @param xml   the xml
     * @param open  the open tag <tag>
     * @param close </tag>
     * @return
     */
    public static String extractXmlElement(String xml, String open, String close) {
        final String openTag = "<" + open + ">";
        final String closeTag = "</" + close + ">";
        int begin = xml.indexOf(openTag);
        int end = xml.lastIndexOf(closeTag);
        if (begin == -1 || end == -1)
            return ""; //Tag not found
        return xml.substring(begin + open.length() + 2, end);

    }

    private static void closeStream(InputStream stream) {
        if (stream != null)
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Writes the body as XML into an envelope. It uses the soap action as the name
     * of the root tag.
     *
     * @param body
     * @return
     * @see com.pilasvacias.yaba.modules.emt.models.EmtBody
     */
    public String toXML(EmtBody body) {
        try {
            XStream xStream = new XStream();
            xStream.processAnnotations(body.getClass());
            xStream.alias(body.getSoapAction(), body.getClass());
            String xml;
            xml = xStream.toXML(body);
            return String.format(ENVELOPE_TEMPLATE, xml);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads ands parse data from an EMT SOAP response. The format of the data is
     * ALWAYS the same. An envelope, a body, SOAPAction + Response, SOAPAction + Result
     * and then actual data.
     * So we extract the SOAPAction+Result with {@link #extractXmlElement(String, String, String)}
     * and create an stream.
     * <p/>
     * The real data is always formatted the same way.
     * {@code DESCRIPCION} as a string
     * {@code RESULTADO} as an int
     * {@code REG} as a list of objects
     *
     * @param xml          SOAP xml
     * @param responseType the type of REG
     * @param body         the body sent, needed to obtain the SOAPAction
     * @return {@link com.pilasvacias.yaba.modules.emt.models.EmtData}
     */
    public <T> EmtData<T> fromXML(String xml, Class<T> responseType, EmtBody body) {

        XStream xStream = new XStream();
        xStream.alias("DESCRIPCION", String.class);
        xStream.alias("RESULTADO", Integer.class);
        xStream.alias("REG", responseType);
        xStream.processAnnotations(responseType);

        String tag = body.getSoapAction() + "Result";
        xml = extractXmlElement(xml, tag, tag);
        EmtData<T> result = new EmtData<T>();

        ObjectInputStream stream = null;
        try {
            stream = xStream.createObjectInputStream(new ByteArrayInputStream(xml.getBytes()));
            Object object = null;
            do {
                object = stream.readObject();

                if (object instanceof String) {
                    result.getEmtInfo().setDescription((String) object);
                } else if (object instanceof Integer) {
                    result.getEmtInfo().setResultCode((Integer) object);
                } else {
                    result.getPayload().add((T) object);
                }
            } while (object != null);

        } catch (EOFException e) {
            //everything is read, time to return
            return result;
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
}
