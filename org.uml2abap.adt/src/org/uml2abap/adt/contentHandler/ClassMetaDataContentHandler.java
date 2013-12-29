package org.uml2abap.adt.contentHandler;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.uml2abap.adt.data.ClassMetaData;

import com.sap.adt.communication.content.IContentHandler;
import com.sap.adt.communication.message.ByteArrayMessageBody;
import com.sap.adt.communication.message.IMessageBody;

public class ClassMetaDataContentHandler implements
		IContentHandler<ClassMetaData> {
	private static final String ADTCORE_URI = "http://www.sap.com/adt/core";
	private static final String ADTCORE_PREFIX = "adtcore";

	@Override
	public ClassMetaData deserialize(IMessageBody arg0,
			Class<? extends ClassMetaData> arg1) {
		// TODO Needed?
		return null;
	}

	@Override
	public String getSupportedContentType() {
		return "application/vnd.sap.adt.oo.classes+xml";
	}

	@Override
	public Class<ClassMetaData> getSupportedDataType() {
		return ClassMetaData.class;
	}

	@Override
	public IMessageBody serialize(ClassMetaData arg0, Charset arg1) {
		XMLStreamWriter streamWriter = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		XMLOutputFactory factory = XMLOutputFactory.newFactory();
		try {
			streamWriter = factory.createXMLStreamWriter(byteArrayOutputStream,
					"UTF-8");

			streamWriter.writeStartElement("class:abapClass");
			streamWriter.writeNamespace("adtcore",
					"http://www.sap.com/adt/core");
			streamWriter.writeNamespace("class",
					"http://www.sap.com/adt/oo/classes");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI,
					"description", "test description");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI,
					"language", "EN");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI, "name",
					arg0.getClassName().toUpperCase());
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI,
					"masterLanguage", "EN");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI,
					"masterSystem", "R6T");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI,
					"responsible", "C7123512");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI,
					"visibility", "public");

			streamWriter.writeStartElement("adtcore:packageRef");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI, "name",
					"$TMP");
			streamWriter.writeEndElement();

			streamWriter.writeStartElement("class:include");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI, "name",
					"CLAS");
			streamWriter.writeAttribute(ADTCORE_PREFIX, ADTCORE_URI, "type",
					"CLAS");
			streamWriter.writeAttribute("class",
					"http://www.sap.com/adt/oo/classes", "includetype",
					"testclasses");
			streamWriter.writeEndElement();

			streamWriter.writeStartElement("class:superClassRef");
			streamWriter.writeEndElement();

			streamWriter.writeEndElement();
			streamWriter.flush();
			return new ByteArrayMessageBody(getSupportedContentType(),
					byteArrayOutputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return null;
	}

}
