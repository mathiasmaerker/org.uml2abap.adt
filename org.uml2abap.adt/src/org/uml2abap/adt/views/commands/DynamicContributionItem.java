package org.uml2abap.adt.views.commands;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;
import org.uml2abap.adt.commons.UmapSessionFactory;

public class DynamicContributionItem extends CompoundContributionItem implements IWorkbenchContribution {

 private IServiceLocator mServiceLocator;
 private long mLastTimeStamp = 0;

 public DynamicContributionItem() {
 }

 public DynamicContributionItem(final String id) {
  super(id);
 }

 @Override
 protected IContributionItem[] getContributionItems() {

//  mLastTimeStamp = System.currentTimeMillis();

//  final CommandContributionItemParameter contributionParameter = new CommandContributionItemParameter(mServiceLocator, null, "org.uml2abap.adt.command",
//    CommandContributionItem.STYLE_PUSH);
//  contributionParameter.label = "Cut " + System.currentTimeMillis();
//  contributionParameter.visibleEnabled = true;
  try {
	  //@ TODO Anpassung der Session DAten, das Label sollte das System sein, doch was ist das value?
	String[] sessions = UmapSessionFactory.getInstance().getAbapProjects();
	IContributionItem[] items = new CommandContributionItem[sessions.length];
	for (int i = 0; i < sessions.length; i++) {
		  final CommandContributionItemParameter contributionParameter = new CommandContributionItemParameter(mServiceLocator, "TEST_AA" + i, "org.uml2abap.adt.command",
				    CommandContributionItem.STYLE_PUSH);
		  contributionParameter.label = sessions[i];
		  contributionParameter.visibleEnabled = true;
		  items[i] = new CommandContributionItem(contributionParameter);
	}
	return items;
} catch (Exception e) {
	// TODO log
	e.printStackTrace();
}
  return null;

//  return new IContributionItem[] { new CommandContributionItem(contributionParameter) };
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
