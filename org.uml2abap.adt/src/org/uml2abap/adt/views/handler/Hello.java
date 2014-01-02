package org.uml2abap.adt.views.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.handlers.HandlerUtil;
import org.uml2abap.adt.commons.UmapSessionFactory;

public class Hello extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
	  //@ TODO echte implementierung mit setzen der Session anstelle der Dummy werte
	  Event event2 = (Event) event.getTrigger();
	  MenuItem item = (MenuItem) event2.widget;
	  try {
		String[] projects = UmapSessionFactory.getInstance().getAbapProjects();
		for (int i = 0; i < projects.length; i++) {
			if(projects[i].equals(item.getText())){
//				UmapSessionFactory.getInstance()
			}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//    MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Info", event.getTrigger().toString());
	
    return null;
  }

} 