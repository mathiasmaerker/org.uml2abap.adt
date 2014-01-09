package org.uml2abap.adt.views.handler;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.uml2abap.adt.commons.UmapSessionFactory;
import org.uml2abap.adt.views.UmapView;

import com.sap.adt.tools.core.project.IAbapProject;

public class ContributionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// @ TODO echte implementierung mit setzen der Session anstelle der
		// Dummy werte
		Event event2 = (Event) event.getTrigger();
		MenuItem item = (MenuItem) event2.widget;
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
							.findView("org.uml2abap.adt.UmapView");
					
					view.contributeToActionBars();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(),
		// "Info", event.getTrigger().toString());

		return null;
	}

}