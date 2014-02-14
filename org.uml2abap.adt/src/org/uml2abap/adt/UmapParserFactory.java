package org.uml2abap.adt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.uml2abap.adt.data.ClassMetaData;
import org.uml2abap.adt.data.ClassSource;
import org.uml2abap.adt.wrapper.IUmapObject;
import org.uml2abap.adt.wrapper.UmapObject;

import com.tts.umap.umapExport.UmapExportMain;

public class UmapParserFactory {

	private static final UmapParserFactory INSTANCE = new UmapParserFactory();
	private ArrayList<IUmapObject> objects = new ArrayList<IUmapObject>();
	private Map<String, String> generatedFiles;

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
/**
 * This Method will parse a given UML file with the dependend plugin org.uml2abap.umap to generate a so called
 * UMAP file, which by itself will be parsed by {@link org.uml2abap.adt.UmapParserFactory}
 * @param URI thr URI of the UML file
 */
	private void doGenerate(final String file) {
//		IRunnableWithProgress operation = new IRunnableWithProgress() {
//			@Override
//			public void run(IProgressMonitor monitor) throws InvocationTargetException,
//					InterruptedException {
//				try {
//					URI model = URI.createFileURI(file);
//					UmapExportMain exportMain = new UmapExportMain(model, null, null);
//					generatedFiles = exportMain.generate(BasicMonitor.toMonitor(monitor));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		try {
//			PlatformUI.getWorkbench().getProgressService().run(true, true, operation);
//		} catch (InvocationTargetException e) {
//			// TODO log
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO log
//			e.printStackTrace();
//		}
//		Platform.
//		URI model = URI.createPlatformResourceURI(file, true);	
		URI model = URI.createFileURI(file);
		try {
			UmapExportMain exportMain = new UmapExportMain(model, null, getArguments());
			Activator.getDefault().getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "No Dump yet, next doGeneration in UmapExportMain"));
			generatedFiles = exportMain.generate(BasicMonitor.toMonitor(new NullProgressMonitor()));
		} catch (IOException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "IOException during generation", e));
			return;
		}
//	
		Activator.getDefault().getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "No Dump yet, next parse UmapFile"));
		for (Map.Entry<String, String> entries : generatedFiles.entrySet()) {
			System.out.println(entries.getKey());
			parseUmapFile(entries.getValue());
		}
	}
	
	private void parseUmapFile(File URI) {
		InputStream stream = getClass().getResourceAsStream(URI.getAbsolutePath());
		parseUmapFile(stream);
	}
	
	private void parseUmapFile(String generatedFile) {
		InputStream stream = new ByteArrayInputStream(generatedFile.trim().getBytes());
		parseUmapFile(stream);
	}
	
	private void parseUmapFile(InputStream is){
		try {
			UmapObject umapObject = new UmapObject();
			ClassMetaData metaData = new ClassMetaData();
			ClassSource source = new ClassSource();

			// ArrayList<ClassMetaData> list = new ArrayList<>();

			StringBuilder builder = new StringBuilder();

			XMLStreamReader reader = XMLInputFactory
					.newInstance()
					.createXMLStreamReader(is);
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

		} 
		catch (XMLStreamException e){
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"Error while parsing XML", e);
			Activator.getDefault().getLog().log(status);
		}
		catch (FactoryConfigurationError e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"XML Streamfactory Error", e);
			Activator.getDefault().getLog().log(status);
		}

	}

	public ArrayList<IUmapObject> getObjects() {
		return objects;
	}
	
	/**
	 * Computes the arguments of the generator.
	 * 
	 * @return the arguments
	 * @generated
	 */
	protected List<? extends Object> getArguments() {
		return new ArrayList<String>();
	}

}
