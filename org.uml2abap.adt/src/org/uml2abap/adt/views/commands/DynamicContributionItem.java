package org.uml2abap.adt.views.commands;

import java.util.ArrayList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;
import org.uml2abap.adt.Activator;
import org.uml2abap.adt.commons.UmapSessionFactory;

import com.sap.adt.tools.core.project.IAbapProject;

public class DynamicContributionItem extends CompoundContributionItem implements
		IWorkbenchContribution {

	private IServiceLocator mServiceLocator;
	private long mLastTimeStamp = 0;

	public DynamicContributionItem() {
	}

	public DynamicContributionItem(final String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {

		// mLastTimeStamp = System.currentTimeMillis();

		// final CommandContributionItemParameter contributionParameter = new
		// CommandContributionItemParameter(mServiceLocator, null,
		// "org.uml2abap.adt.command",
		// CommandContributionItem.STYLE_PUSH);
		// contributionParameter.label = "Cut " + System.currentTimeMillis();
		// contributionParameter.visibleEnabled = true;
		try {
			// @ TODO Anpassung der Session DAten, das Label sollte das System
			// sein, doch was ist das value?
			ArrayList<IAbapProject> sessions = UmapSessionFactory.getInstance()
					.getAbapProjects();
			ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();
			// IContributionItem[] items = new
			// CommandContributionItem[sessions.toArray().length];

			for (IAbapProject project : sessions) {
				final CommandContributionItemParameter contributionParameter = new CommandContributionItemParameter(
						mServiceLocator, null, "org.uml2abap.adt.command",
						CommandContributionItem.STYLE_PUSH);
				contributionParameter.label = project.toString();
				contributionParameter.visibleEnabled = true;
				items.add(new CommandContributionItem(contributionParameter));
			}
			return items.toArray(new IContributionItem[items.size()]);
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"Error while building System list", e);
			Activator.getDefault().getLog().log(status);
		}
		return null;

		// return new IContributionItem[] { new
		// CommandContributionItem(contributionParameter) };
	}

	@Override
	public void initialize(final IServiceLocator serviceLocator) {
		mServiceLocator = serviceLocator;
	}

	@Override
	public boolean isDirty() {
		return mLastTimeStamp + 5000 < System.currentTimeMillis();
	}
}
