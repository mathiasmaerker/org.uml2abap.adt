package org.uml2abap.adt.contentHandler;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.uml2abap.adt.data.ClassSource;

import com.sap.adt.communication.content.AdtMediaType;
import com.sap.adt.communication.content.IContentHandler;
import com.sap.adt.communication.message.IMessageBody;
import com.sap.adt.communication.message.InputStreamMessageBody;

public class ClassSourceContentHandler implements IContentHandler<ClassSource> {

	@Override
	public ClassSource deserialize(IMessageBody arg0,
			Class<? extends ClassSource> arg1) {
		return null;
	}

	@Override
	public String getSupportedContentType() {
		return AdtMediaType.TEXT_PLAIN;
	}

	@Override
	public Class<ClassSource> getSupportedDataType() {
		return ClassSource.class;
	}

	@Override
	public IMessageBody serialize(ClassSource dataObject, Charset arg1) {
		String source = dataObject.getSourceDefinition();
		return new InputStreamMessageBody(getSupportedContentType(), new ByteArrayInputStream(source.getBytes()));
	}

}
