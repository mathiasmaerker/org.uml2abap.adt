package org.uml2abap.adt.contentHandler;

import java.nio.charset.Charset;

import org.uml2abap.adt.data.ClassSource;

import com.sap.adt.communication.content.IContentHandler;
import com.sap.adt.communication.message.IMessageBody;

public class ClassSourceContentHandler implements IContentHandler<ClassSource> {

	@Override
	public ClassSource deserialize(IMessageBody arg0,
			Class<? extends ClassSource> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSupportedContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<ClassSource> getSupportedDataType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMessageBody serialize(ClassSource arg0, Charset arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
