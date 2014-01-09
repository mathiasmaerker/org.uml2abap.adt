package org.uml2abap.adt.commons;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.destinations.ui.logon.AdtLogonServiceUIFactory;
import com.sap.adt.tools.core.project.AdtProjectServiceFactory;
import com.sap.adt.tools.core.project.IAbapProject;

public class UmapSessionFactory {
	private static final UmapSessionFactory INSTANCE = new UmapSessionFactory();
	private IAbapProject currentProject;

	private UmapSessionFactory() {
		super();
	}

	public static UmapSessionFactory getInstance() {
		return INSTANCE;
	}

	//@ FIXME Exception typ ändern Retruntyp ändern in Iproject
	public ArrayList<IAbapProject> getAbapProjects() throws Exception{
		IProject[] projects = AdtProjectServiceFactory.createProjectService()
				.getAvailableAbapProjects();
		
		if (projects.length <= 0)throw new Exception();
		ArrayList<IAbapProject> list = new ArrayList<IAbapProject>();
		for (IProject iProject : projects) {
			IAbapProject abapProject = (IAbapProject) iProject
					.getAdapter(IAbapProject.class);
			list.add(abapProject);
		}
		

		return list;
	}
	//@ FIXME Ändern in IPROJECT 
	public IAbapProject getCurrentProject() {
		return currentProject;
	}
//@ FIXME Ändern in IPROJECT 
	public void setCurrentProject(IAbapProject currentProject) {
		this.currentProject = currentProject;

	}
	public void ensureLogIn() {
		if(getCurrentProject() == null)return;// @ FIXME Exception werfen
		// Sicherstellen das wir auch eingeloggt sind
		IAbapProject abapProject = getCurrentProject();
		// Trigger logon dialog if necessary
		IStatus status = AdtLogonServiceUIFactory.createLogonServiceUI().ensureLoggedOn(
				abapProject.getDestinationData(),
				PlatformUI.getWorkbench().getProgressService());	
		// TODO Status überprüfen
	}
}
