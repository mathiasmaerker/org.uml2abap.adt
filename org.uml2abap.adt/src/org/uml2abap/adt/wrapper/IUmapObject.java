package org.uml2abap.adt.wrapper;

import com.sap.adt.communication.resources.ResourceException;

public interface IUmapObject {
	/** 
	 * Get a lock handle, the actual implementing class has to take care of locking strategies
	 * as well as session handling
	 * @throws ResourceException
	 */
	public void aquireLock() throws ResourceException;
	/**
	 * Get a lock handle, the actual implementing class has to take care of release strategies
	 * as well as session handling
	 * @throws ResourceException
	 */
	public void releaseLock() throws ResourceException;
	/**
	 * Creates the actual resource. a resource is identified by its name, internally the implementations 
	 * has to take care of pre checking if everything is valid
	 * @throws ResourceException
	 */
	public void createClass() throws ResourceException;
	/**
	 * Update the source code
	 * @throws ResourceException
	 */
	public void updateSource() throws ResourceException;
	public boolean isExistingObject();

}
