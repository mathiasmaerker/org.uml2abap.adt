

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import javax.swing.ProgressMonitor;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import com.sap.adt.communication.content.AdtContentHandlingFactory;
import com.sap.adt.communication.content.AdtMediaType;
import com.sap.adt.communication.content.IContentHandler;
import com.sap.adt.communication.content.IContentHandlerRegistry;
import com.sap.adt.communication.content.IContentHandlerResolver;
import com.sap.adt.communication.content.IContentHandlingFactory;
import com.sap.adt.communication.content.IContentHandlingService;
import com.sap.adt.communication.content.asx.IAsxDataFactory;
import com.sap.adt.communication.message.ByteArrayMessageBody;
import com.sap.adt.communication.message.IMessageBody;
import com.sap.adt.communication.message.IResponse;
import com.sap.adt.communication.resources.AdtRestResourceFactory;
import com.sap.adt.communication.resources.IQueryParameter;
import com.sap.adt.communication.resources.IRestResource;
import com.sap.adt.communication.resources.IRestResourceFactory;
import com.sap.adt.communication.resources.IUriBuilder;
import com.sap.adt.communication.resources.QueryParameter;
import com.sap.adt.communication.resources.ResourceNotFoundException;
import com.sap.adt.communication.resources.UriBuilder;
import com.sap.adt.communication.session.AdtSystemSessionFactory;
import com.sap.adt.communication.session.ISystemSession;
import com.sap.adt.communication.session.ISystemSessionFactory;
import com.sap.adt.compatibility.discovery.AdtDiscoveryFactory;
import com.sap.adt.compatibility.discovery.IAdtDiscovery;
import com.sap.adt.compatibility.discovery.IAdtDiscoveryCollectionMember;
import com.sap.adt.compatibility.model.templatelink.IAdtTemplateLink;
import com.sap.adt.compatibility.uritemplate.IAdtUriTemplate;
import com.sap.adt.destinations.model.internal.Activator;
import com.sap.adt.destinations.ui.logon.AdtLogonServiceUIFactory;
import com.sap.adt.oo.OoPlugin;
import com.sap.adt.tools.core.model.adtcore.IAdtCoreFactory;
import com.sap.adt.tools.core.model.adtcore.IAdtMainObject;
import com.sap.adt.tools.core.project.AdtProjectServiceFactory;
import com.sap.adt.tools.core.project.IAbapProject;

////import data.ClassMetaData;
//
///**
// * Our sample handler extends AbstractHandler, an IHandler base class.
// * 
// * @see org.eclipse.core.commands.IHandler
// * @see org.eclipse.core.commands.AbstractHandler
// */
@SuppressWarnings({ "unused", "restriction" })
public  class SampleHandler extends AbstractHandler {
	private static final String SAMPLE_FLIGHT_RESOURCE_URI = "/sap/bc/adt/oo/classes/ztest_adt";
	private static final String MYCOMPANY_DISCOVERY = "/sap/bc/adt/discovery";
	private static final String URI_PARAMETER_FLIGHT_DATE = "flight_date";
	private static final String URI_PARAMETER_CONNECTION_ID = "connection_id";
	private static final String URI_PARAMETER_CARRIER_ID = "carrier_id";
	private static final String FLIGHT_RELATION = "http://www.sap.com/adt/relations/oo/singleaccess";
	private static final String SCHEME = "http://www.sap.com/adt/categories/oo";
	private static final String TERM = "classes";


	//
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	@SuppressWarnings("restriction")
	public Object execute(ExecutionEvent event) throws ExecutionException {
OoPlugin.getDefault().createClassContentHandler();
		// Create resource factory
		IRestResourceFactory restResourceFactory = AdtRestResourceFactory
				.createRestResourceFactory();
		// Get available projects in the workspace
		IProject[] abapProjects = AdtProjectServiceFactory
				.createProjectService().getAvailableAbapProjects();
		// Use the first project in the workspace for the demo
		// to keep the example simple
		IAbapProject abapProject = (IAbapProject) abapProjects[0]
				.getAdapter(IAbapProject.class);
		// Trigger logon dialog if necessary
		AdtLogonServiceUIFactory.createLogonServiceUI().ensureLoggedOn(
				abapProject.getDestinationData(),
				PlatformUI.getWorkbench().getProgressService());
		// Create REST resource for given destination and URI
		String destination = abapProject.getDestinationId();
//		URI flightUri = URI.create(SAMPLE_FLIGHT_RESOURCE_URI);
//		URI flightUri = getFlightURI(destination, null, null, null);
		
		
		
		URI classHeader = getDiscoveryClassURI(destination);
		
		
		IRestResource classResource = restResourceFactory
				.createResourceWithStatelessSession(classHeader, destination);
//		try {
			// Trigger GET request on resource data
//			IResponse response = flightResource.get(null, IResponse.class);
//			ClassMetaDataContentHandler contentHandler = new ClassMetaDataContentHandler();
//			ClassMetaData metaData = new ClassMetaData(null, null, null, null, null, null, null, null, null, null, null);
//			metaData.setClassName(openInputWindow());
//			classResource.addContentHandler(contentHandler);
//			IMessageBody body = contentHandler.serialize(metaData, null);
//			IResponse response =  classResource.post(null, IResponse.class, body, (IQueryParameter)null);
//			classResource = restResourceFactory
//					.createResourceWithStatelessSession(getClassURI(destination, metaData.getClassName()), destination);
//			
//			response = classResource.get(null, IResponse.class, (IQueryParameter)null);
//			
//			int i = response.getStatus();
//			IQueryParameter [] parameters;
////			IProgressMonitor monitor = new progres
//			if ( i == 200 ) {
//				ISystemSessionFactory factory = AdtSystemSessionFactory.createSystemSessionFactory();
//				ISystemSession session = factory.getEnqueueSession(destination);
//				classResource = restResourceFactory 
//						.createRestResource(getClassURI(destination, metaData.getClassName()), session);
//				 
//				parameters = new QueryParameter[2]; 
//				parameters[0] = new QueryParameter("_action", "LOCK");
//				parameters[1] = new QueryParameter("accessMode", "MODIFY");
//				
////				flightResource = restResourceFactory
////						.createResourceWithStatelessSession(getFlightURI(destination, metaData.getClassName()), destination);				
//				response = classResource.post(null, IResponse.class, parameters);
//				body = response.getBody();	
//				XMLInputFactory inputFactory = XMLInputFactory.newFactory();
//				XMLStreamReader reader = inputFactory.createXMLStreamReader(body.getContent());
//				int eventTYpe = reader.getEventType();
//				String lockHandel = null;
//				while (reader.hasNext()) {
//					eventTYpe = reader.next();
//					System.out.println(eventTYpe);
//
//					if (eventTYpe == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals("LOCK_HANDLE")) {
//						reader.next();
//						lockHandel = reader.getText().trim();
//					}
//				}
//				
//
//				classResource = restResourceFactory 
//						.createRestResource(getSourceURI(destination, metaData.getClassName().toLowerCase(), "source", "main"), session);
//				
//				parameters = new QueryParameter[2];
//				parameters[0] = new QueryParameter("_action", "CHECK");
//				parameters[1] = new QueryParameter("TRANSPORT_URIS", "X");
//				classResource.post(null, IResponse.class, parameters);
//				
//				final String CRLF = "\n\r";
//				
//				String classSource = "CLASS " + metaData.getClassName().toUpperCase() + " DEFINITION" + CRLF +
//						  "PUBLIC" + CRLF +
//						  "FINAL" + CRLF +
//						  "CREATE PRIVATE ." + CRLF + CRLF +
//						  "PUBLIC SECTION." + CRLF +
//						  "data test type i." + CRLF +  
//						  "PROTECTED SECTION." + CRLF +  
//						  "PRIVATE SECTION." + CRLF +  
//						"ENDCLASS." + CRLF   + CRLF   + CRLF + 
//						"CLASS " + metaData.getClassName() + " IMPLEMENTATION." + CRLF +  
//						"ENDCLASS.";
////				lockHandel = URLEncoder.encode(lockHandel);
//				ClassSourceContentHandler sourceContentHandler = new ClassSourceContentHandler();
//				body = sourceContentHandler.serialize(classSource, null);
//				parameters = new QueryParameter[1]; 
//				parameters[0] = new QueryParameter("lockHandle", lockHandel);
//				response = classResource.put(null, IResponse.class, body, parameters );
		return null;
				
				

//<?xml version="1.0" encoding="UTF-8"?>
//-<asx:abap xmlns:asx="http://www.sap.com/abapxml" version="1.0">-<asx:values>-<DATA><LOCK_HANDLE>zhSCz3fDA3lwRvv4puvGMS0qEFs=</LOCK_HANDLE><CORRNR/><CORRUSER/><CORRTEXT/><IS_LOCAL>X</IS_LOCAL><IS_LINK_UP/></DATA></asx:values></asx:abap>
	
//				
//			}
//			POST /sap/bc/adt/oo/classes/zcl_tester10?_action=LOCK&accessMode=MODIFY HTTP/1.1
			// confirm that flight exists
//			openDialogWindow(
//					"Flight exists! HTTP-status:"
//							+ String.valueOf(response.getStatus()),
//					"Flight Confirmation");
//		} catch (ResourceNotFoundException e) {
//			displayError("No flight data found");
//		} catch (RuntimeException e) {
//			// Display any kind of other error
//			displayError(e.getMessage());
//		} catch (Exception e2) {
//			e2.printStackTrace();
//		}
//		return null;
	}

	private String openInputWindow() {
		InputDialog dialog = new InputDialog(getShell(), "Create Class", "Enter Class Name", null, null);
		dialog.open();
		return dialog.getValue();
	}

	/*
	 * Display the exception text
	 */
	private void displayError(String messageText) {
		String dialogTitle = "Flight Exception";
		openDialogWindow(messageText, dialogTitle);
	}

	/*
	 * Display a simple dialog box with a text and an OK button to confirm
	 */
	protected void openDialogWindow(String dialogText, String dialogTitle) {
		String[] DIALOG_BUTTON_LABELS = new String[] { IDialogConstants.OK_LABEL };
		MessageDialog dialog = new MessageDialog(getShell(), dialogTitle, null,
				dialogText, MessageDialog.INFORMATION, DIALOG_BUTTON_LABELS, 0);
		dialog.open();
	}

	protected Shell getShell() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		return shell;
	}
	
	private URI getSourceURI(String destination, String className, String source, String main ){
		URI sofar = getClassURI(destination, className);
		IUriBuilder builder = new UriBuilder(sofar);
		builder.addPathSegments(source);
		builder.addPathSegments(main);
		return builder.getUri();
	}
	private URI getClassURI(String destination, String className) {
		IAdtDiscovery discovery = AdtDiscoveryFactory.createDiscovery(
				destination, URI.create(MYCOMPANY_DISCOVERY)); // Get collection
																// member by use
																// of scheme and
																// term
		
		IAdtDiscoveryCollectionMember collectionMember = discovery
				.getCollectionMember(SCHEME, TERM, new NullProgressMonitor());
		
//		IAdtTemplateLink templateLink = collectionMember
//				.getTemplateLink(FLIGHT_RELATION);
//		if (templateLink != null) {
//			IAdtUriTemplate uriTemplate = templateLink.getUriTemplate();
//			String uri = uriTemplate.set(URI_PARAMETER_CARRIER_ID, carrierId)
//					.set(URI_PARAMETER_CONNECTION_ID, connectionId)
//					.set(URI_PARAMETER_FLIGHT_DATE, flightDate).expand();	
//			return URI.create(uri);
//		}
		IUriBuilder builder = new UriBuilder(collectionMember.getUri());
		builder.addPathSegments(className);
		return builder.getUri();
	}
	private URI getDiscoveryClassURI(String destination){
		IAdtDiscovery discovery = AdtDiscoveryFactory.createDiscovery(
				destination, URI.create(MYCOMPANY_DISCOVERY)); // Get collection
																// member by use
																// of scheme and
																// term
		
		IAdtDiscoveryCollectionMember collectionMember = discovery
				.getCollectionMember(SCHEME, TERM, new NullProgressMonitor());
		return collectionMember.getUri();
		
	}
	
//
}
