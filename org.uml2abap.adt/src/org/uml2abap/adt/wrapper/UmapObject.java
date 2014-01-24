/**
 * 
 */
package org.uml2abap.adt.wrapper;

import java.io.IOException;
import java.net.URI;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.uml2abap.adt.Activator;
import org.uml2abap.adt.commons.IUmapConstants;
import org.uml2abap.adt.contentHandler.ClassMetaDataContentHandler;
import org.uml2abap.adt.contentHandler.ClassSourceContentHandler;
import org.uml2abap.adt.data.ClassMetaData;

import com.sap.adt.communication.message.IMessageBody;
import com.sap.adt.communication.message.IResponse;
import com.sap.adt.communication.resources.AdtRestResourceFactory;
import com.sap.adt.communication.resources.IQueryParameter;
import com.sap.adt.communication.resources.IRestResource;
import com.sap.adt.communication.resources.IRestResourceFactory;
import com.sap.adt.communication.resources.QueryParameter;
import com.sap.adt.communication.resources.ResourceException;
import com.sap.adt.communication.resources.UriBuilder;
import com.sap.adt.communication.session.AdtSystemSessionFactory;
import com.sap.adt.communication.session.ISystemSession;
import com.sap.adt.communication.session.ISystemSessionFactory;
import com.sap.adt.compatibility.discovery.AdtDiscoveryFactory;
import com.sap.adt.compatibility.discovery.IAdtDiscovery;
import com.sap.adt.compatibility.discovery.IAdtDiscoveryCollectionMember;

/**
 * @author MaerkerMa
 * 
 */
public class UmapObject implements IUmapObject {
	private ClassMetaData classMetaData;
	private String lockHandle;
	private String destination;
	private URI ressourcePath;
	private IRestResourceFactory restResourceFactory;
	private ISystemSession session;

	public ClassMetaData getClassMetaData() {
		return classMetaData;
	}

	public void setClassMetaData(ClassMetaData classMetaData) {
		this.classMetaData = classMetaData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tts.umap.adt.wrapper.IUmapObject#aquireLock()
	 */
	@Override
	public void aquireLock() throws ResourceException {
		// FIXME Exceptiontyp ändern
		ISystemSessionFactory factory = AdtSystemSessionFactory
				.createSystemSessionFactory();
		session = factory.getEnqueueSession(destination);
		URI classUri = new UriBuilder(getDiscoveryURI(destination))
				.addPathSegments(classMetaData.getClassName()).getUri();
		IRestResource classResource = getRestResourceFactory()
				.createRestResource(classUri, session);

		IQueryParameter[] parameters = new QueryParameter[2];
		parameters[0] = new QueryParameter("_action", "LOCK");
		parameters[1] = new QueryParameter("accessMode", "MODIFY");

		// flightResource = restResourceFactory
		// .createResourceWithStatelessSession(getFlightURI(destination,
		// metaData.getClassName()), destination);
		IResponse response = classResource.post(null, IResponse.class,
				parameters);
		IMessageBody body = response.getBody();
		XMLInputFactory inputFactory = XMLInputFactory.newFactory();
		XMLStreamReader reader;
		try {
			reader = inputFactory.createXMLStreamReader(body.getContent());
			int eventTYpe = reader.getEventType();
			while (reader.hasNext()) {
				eventTYpe = reader.next();
				System.out.println(eventTYpe);

				if (eventTYpe == XMLStreamConstants.START_ELEMENT
						&& reader.getLocalName().equals("LOCK_HANDLE")) {
					reader.next();
					//@ TODO Was Passiert wenn ich kein Lockhandle bekomme?
					lockHandle = reader.getText().trim();
				}
			}
		} catch (XMLStreamException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"Error while parsing XML", e);
			Activator.getDefault().getLog().log(status);
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"IOError", e);
			Activator.getDefault().getLog().log(status);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tts.umap.adt.wrapper.IUmapObject#releaseLock()
	 */
	@Override
	public void releaseLock() throws ResourceException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tts.umap.adt.wrapper.IUmapObject#createClass()
	 */
	@Override
	public void createClass() throws ResourceException {
		// FIXME Exceptiontyp ändern
		ClassMetaDataContentHandler metaDataContentHandler = new ClassMetaDataContentHandler();
		URI classUri = new UriBuilder(getDiscoveryURI(destination))
				.addPathSegments(classMetaData.getClassName()).getUri();
		IRestResource resource = getRestResourceFactory()
				.createResourceWithStatelessSession(classUri, destination);
		resource.addContentHandler(metaDataContentHandler);
		IMessageBody messageBody = metaDataContentHandler.serialize(
				classMetaData, null);
		IResponse response = resource.post(null, IResponse.class, messageBody,
				(IQueryParameter) null);
		// TODO Fehlerhandling und Status abfragen
		response.getStatus();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tts.umap.adt.wrapper.IUmapObject#updateSource()
	 */
	@Override
	public void updateSource() throws ResourceException {
		// FIXME Exceptiontyp ändern
		URI classUri = new UriBuilder(getDiscoveryURI(destination))
				.addPathSegments(classMetaData.getClassName())
				.addPathSegments("source").addPathSegments("main").getUri();
		IRestResource resource = getRestResourceFactory().createRestResource(
				classUri, session);

		ClassSourceContentHandler sourceContentHandler = new ClassSourceContentHandler();
		IMessageBody body = sourceContentHandler.serialize(
				classMetaData.getClassSource(), null);
		IQueryParameter[] parameters = new IQueryParameter[1];
		parameters[0] = new QueryParameter("lockHandle", lockHandle);
		IResponse response = resource.put(null, IResponse.class, body,
				parameters);
		response.getStatus();
	}

	// FIXME auslagern in sessionfactory
	private URI getDiscoveryURI(String destination) {
		IAdtDiscovery discovery = AdtDiscoveryFactory.createDiscovery(
				destination, URI.create(IUmapConstants.DISCOVERY_URI));
		String term = (getClassMetaData().getType().equalsIgnoreCase("C")) ? IUmapConstants.TERM_CLASSES
				: IUmapConstants.TERM_INTERFACES;
		IAdtDiscoveryCollectionMember collectionMember = discovery
				.getCollectionMember(IUmapConstants.OO_SCHEME, term,
						new NullProgressMonitor());
		return collectionMember.getUri();
	}

	@Override
	public boolean isExistingObject() {
		// TODO Call validation Services
		return false;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public IRestResourceFactory getRestResourceFactory() {
		if (restResourceFactory == null)
			restResourceFactory = AdtRestResourceFactory
					.createRestResourceFactory();
		return restResourceFactory;
	}

	public void setRestResourceFactory(IRestResourceFactory restResourceFactory) {
		this.restResourceFactory = restResourceFactory;
	}

}
