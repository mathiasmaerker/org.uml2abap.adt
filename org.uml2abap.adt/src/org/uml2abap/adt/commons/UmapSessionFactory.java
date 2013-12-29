package org.uml2abap.adt.commons;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;

import com.sap.adt.tools.core.project.AdtProjectServiceFactory;
import com.sap.adt.tools.core.project.IAbapProject;

public class UmapSessionFactory {
	private static final UmapSessionFactory INSTANCE = new UmapSessionFactory();

	private UmapSessionFactory() {
		super();
	}

	public static UmapSessionFactory getInstance() {
		return INSTANCE;
	}

	//@ FIXME Exception typ ändern
	public void connectToSystem() throws Exception{
		IProject[] projects = AdtProjectServiceFactory.createProjectService()
				.getAvailableAbapProjects();
		
		if (projects.length <= 0)throw new Exception();
		
		for (IProject iProject : projects) {
			IAbapProject abapProject = (IAbapProject)iProject;
			System.out.println(abapProject.getDestinationDisplayText());
		}
	}
}
