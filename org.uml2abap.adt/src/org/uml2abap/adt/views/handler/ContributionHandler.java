package org.uml2abap.adt.views.handler;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.uml2abap.adt.Activator;
import org.uml2abap.adt.commons.UmapSessionFactory;
import org.uml2abap.adt.views.UmapView;

import com.sap.adt.tools.core.project.IAbapProject;

public class ContributionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		 Event eve =  (Event) event.getTrigger();
		 if (!(eve.widget instanceof MenuItem)) return null;
		 MenuItem item = (MenuItem) eve.widget;
		try {
			ArrayList<IAbapProject> projects = UmapSessionFactory.getInstance()
					.getAbapProjects();
			for (IAbapProject abapProject : projects) {
				if (abapProject.toString().equals(item.getText())) {
					UmapSessionFactory sessionFactory = UmapSessionFactory
							.getInstance();
					sessionFactory.setCurrentProject(abapProject);
					sessionFactory.ensureLogIn();

					IWorkbenchPage page = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();
					UmapView view = (UmapView) page
							.findView(UmapView.ID);
					
					view.contributeToActionBars();
				}
			}

		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Exception in Contributionhandler", e);
			Activator.getDefault().getLog().log(status);
		}
		return null;
	}

}