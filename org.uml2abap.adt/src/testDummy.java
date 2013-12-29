import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.uml2abap.adt.contentHandler.ClassMetaDataContentHandler;
import org.uml2abap.adt.contentHandler.ClassSourceContentHandler;
import org.uml2abap.adt.data.ClassMetaData;
import org.uml2abap.adt.data.ClassSource;
import org.uml2abap.adt.wrapper.UmapObject;

public class testDummy {

	public static void main(String[] args) {
		try {
			UmapObject umapObject = new UmapObject();
			ClassMetaData metaData = new ClassMetaData();
			ClassSource source = new ClassSource();
			ArrayList<ClassMetaData> list = new ArrayList<>();
			StringBuilder builder = new StringBuilder();
			XMLStreamReader reader = XMLInputFactory
					.newInstance()
					.createXMLStreamReader(
							new FileReader(
									new File(
											"C:/Users/maerkerma/eclipseJ2EE/luna/luna ws/com.tts.umap.adt/DemoJam 2013.umap")));
			int xmlEvent = reader.getEventType();
			while (reader.hasNext()) {
				xmlEvent = reader.next();
				switch (xmlEvent) {
				case XMLStreamConstants.START_ELEMENT:
					// System.out.println(reader.getLocalName());
					if (reader.getLocalName().equalsIgnoreCase("object")) {
						
						builder = new StringBuilder();
						metaData = new ClassMetaData();
						source = new ClassSource();
						metaData.setClassSource(source);
						list.add(metaData);

						for (int i = 0; i < reader.getAttributeCount(); i++) {
							String name = reader.getAttributeLocalName(i);

							if (name.equalsIgnoreCase("name")) {
								metaData.setClassName(reader
										.getAttributeValue(i));
							} else if (name.equalsIgnoreCase("type"))
								metaData.setType(reader.getAttributeValue(i));
						}

					}
					;
					// else if (reader.getLocalName().equalsIgnoreCase(
					// "definition"))
					// reader.next();
					// System.out.println(reader.getText());
					// builder.append(reader.getText());

					break;

				case XMLStreamConstants.CHARACTERS:

					// System.out.println(reader.getText());
					if (reader.getText().trim().equals("")) break;
					builder.append(reader.getText().trim()).append("\n");
					source.setSourceDefinition(builder.toString());
					break;
				}

			}

		} catch (FileNotFoundException | XMLStreamException
				| FactoryConfigurationError e) {
			e.printStackTrace();
		}

	}

}
