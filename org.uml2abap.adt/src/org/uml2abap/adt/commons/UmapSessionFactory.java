package org.uml2abap.adt.commons;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.destinations.ui.logon.AdtLogonServiceUIFactory;
import com.sap.adt.tools.core.project.IAbapProject;

public class UmapSessionFactory {
	private static final UmapSessionFactory INSTANCE = new UmapSessionFactory();
	private IProject currentProject;

	private UmapSessionFactory() {
		super();
	}

	public static UmapSessionFactory getInstance() {
		return INSTANCE;
	}

	//@ FIXME Exception typ ändern Retruntyp ändern in Iproject
	public String[] getAbapProjects() throws Exception{
//		IProject[] projects = AdtProjectServiceFactory.createProjectService()
//				.getAvailableAbapProjects();
//		
//		if (projects.length <= 0)throw new Exception();
//		
//		for (IProject iProject : projects) {
//			IAbapProject abapProject = (IAbapProject)iProject;
//			System.out.println(abapProject.getDestinationDisplayText());
//		}
		String[] systems = new String[3];
		systems[0] = "R6T CuserXY";
		systems[1] = "R6T Cuser12";
		systems[2] = "R6T Cuser34";
		return systems;
	}
	//@ FIXME Ändern in IPROJECT 
	public IProject getCurrentProject() {
		return currentProject;
	}
//@ FIXME Ändern in IPROJECT 
	public void setCurrentProject(IProject currentProject) {
		this.currentProject = currentProject;

	}
	public void ensureLogIn() {
		if(getCurrentProject() == null)return;// @ FIXME Exception werfen
		// Sicherstellen das wir auch eingeloggt sind
		IAbapProject abapProject = (IAbapProject) getCurrentProject()
				.getAdapter(IAbapProject.class);
		// Trigger logon dialog if necessary
		IStatus status = AdtLogonServiceUIFactory.createLogonServiceUI().ensureLoggedOn(
				abapProject.getDestinationData(),
				PlatformUI.getWorkbench().getProgressService());		
	}
}
