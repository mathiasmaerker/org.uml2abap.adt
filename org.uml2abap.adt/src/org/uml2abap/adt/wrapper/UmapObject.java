/**
 * 
 */
package org.uml2abap.adt.wrapper;

import java.net.URI;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.uml2abap.adt.commons.IUmapConstants;
import org.uml2abap.adt.data.ClassMetaData;

import com.sap.adt.communication.resources.ResourceException;
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
	
	
	
	
	public ClassMetaData getClassMetaData() {
		return classMetaData;
	}

	public void setClassMetaData(ClassMetaData classMetaData) {
		this.classMetaData = classMetaData;
	}

	/* (non-Javadoc)
	 * @see com.tts.umap.adt.wrapper.IUmapObject#aquireLock()
	 */
	@Override
	public void aquireLock() throws ResourceException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.tts.umap.adt.wrapper.IUmapObject#releaseLock()
	 */
	@Override
	public void releaseLock() throws ResourceException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.tts.umap.adt.wrapper.IUmapObject#createClass()
	 */
	@Override
	public void createClass() throws ResourceException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.tts.umap.adt.wrapper.IUmapObject#updateSource()
	 */
	@Override
	public void updateSource() throws ResourceException {
		// TODO Auto-generated method stub

	}
	private URI getDiscoveryURI(String destination){
		IAdtDiscovery discovery = AdtDiscoveryFactory.createDiscovery(
				destination, URI.create(IUmapConstants.DISCOVERY_URI)); 
		String term = (getClassMetaData().getType().equalsIgnoreCase("C")) ? IUmapConstants.TERM_CLASSES : IUmapConstants.TERM_INTERFACES;
		IAdtDiscoveryCollectionMember collectionMember = discovery
				.getCollectionMember(IUmapConstants.OO_SCHEME, term, new NullProgressMonitor());
		return collectionMember.getUri();
	}

	@Override
	public boolean isExistingObject() {
		// TODO Auto-generated method stub
		return false;
	}

}
