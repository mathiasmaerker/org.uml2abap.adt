package org.uml2abap.adt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.uml2abap.adt.data.ClassMetaData;
import org.uml2abap.adt.data.ClassSource;
import org.uml2abap.adt.wrapper.IUmapObject;
import org.uml2abap.adt.wrapper.UmapObject;

public class UmapParserFactory {

	private static final UmapParserFactory INSTANCE = new UmapParserFactory();
	private ArrayList<IUmapObject> objects = new ArrayList<IUmapObject>();

	private UmapParserFactory() {
		super();
	}

	public static UmapParserFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * This Method will parse a given Uml OR Umap file for further processing
	 * 
	 * @param URI
	 * @throws IOException
	 */
	public void parseFile(String URI) throws IOException {
		if (URI.endsWith(".umap")) {
			parseUmapFile(URI);
		} else if (URI.endsWith(".uml")) {
			doGenerate(URI);
		} else {
			throw new IOException();
		}
		// File file = new File(URI);

	}

	private void doGenerate(String uRI) {
		// TODO Call generator

	}

	private void parseUmapFile(String uRI) {
		try {
			UmapObject umapObject = new UmapObject();
			ClassMetaData metaData = new ClassMetaData();
			ClassSource source = new ClassSource();

			// ArrayList<ClassMetaData> list = new ArrayList<>();

			StringBuilder builder = new StringBuilder();

			XMLStreamReader reader = XMLInputFactory
					.newInstance()
					.createXMLStreamReader(
							new FileReader(
									new File(
											uRI)));
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
						umapObject = new UmapObject();
						
						metaData.setClassSource(source);
						umapObject.setClassMetaData(metaData);
						objects.add( umapObject);

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
					if (reader.getText().trim().equals(""))
						break;
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

	public ArrayList<IUmapObject> getObjects() {
		return objects;
	}

}
