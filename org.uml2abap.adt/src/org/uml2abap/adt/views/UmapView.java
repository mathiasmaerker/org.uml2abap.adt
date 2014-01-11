package org.uml2abap.adt.views;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.uml2abap.adt.UmapParserFactory;
import org.uml2abap.adt.commons.UmapSessionFactory;
import org.uml2abap.adt.wrapper.IUmapObject;
import org.uml2abap.adt.wrapper.UmapObject;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class UmapView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.uml2abap.adt.views.UmapView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action importAction;
	private Action connectAction;
	private Action doubleClickAction;
	private ArrayList<IUmapObject> objects;
	IContributionItem test;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;

		public TreeObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return getName();
		}

		@Override
		public Object getAdapter(Class key) {
			return null;
		}
	}

	class TreeParent extends TreeObject {
		private ArrayList children;

		public TreeParent(String name) {
			super(name);
			children = new ArrayList<IUmapObject>();
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children
					.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {
		private TreeParent invisibleRoot;

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object parent) {
			if (parent instanceof String) return new Object[]{parent.toString()};
			if (parent instanceof ArrayList<?>) {
				if (invisibleRoot == null)
					initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		@Override
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		@Override
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		/*
		 * We will set up a dummy model to initialize tree heararchy. In a real
		 * code, you will connect to a real model and expose its hierarchy.
		 */
		private void initialize() {
			invisibleRoot = new TreeParent("");
			for (IUmapObject iUmapObject : objects) {
				TreeParent parent = new TreeParent(((UmapObject) iUmapObject)
						.getClassMetaData().getClassName());
				invisibleRoot.addChild(parent);
			}
		}
	}

	class ViewLabelProvider extends LabelProvider {

		@Override
		public String getText(Object obj) {
			return obj.toString();
		}

		@Override
		public Image getImage(Object obj) {
			if (objects == null) return null;
			for (IUmapObject umapObject : objects) {
				if (((UmapObject) umapObject).getClassMetaData().getClassName()
						.equalsIgnoreCase(obj.toString())) {
					if (((UmapObject) umapObject).getClassMetaData().getType()
							.equals("C")) {
						return getIconDescriptor("classes.gif").createImage();
					}else{
						return getIconDescriptor("interfaces.gif").createImage();
					}
				}
			}
			return null;
		}


	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public UmapView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		Transfer[] transferTypes = new Transfer[] { FileTransfer.getInstance() };

		viewer.addDropSupport(DND.DROP_COPY | DND.DROP_MOVE, transferTypes,
				new ViewerDropAdapter(viewer) {

					@Override
					public boolean validateDrop(Object arg0, int dataObject,
							TransferData transfer) {
						return FileTransfer.getInstance().isSupportedType(
								transfer);
					}

					@Override
					public boolean performDrop(Object dataObject) {
						String[] fileSource = (String[]) dataObject;
						if (fileSource.length <= 1) {
							// FIXME Momentan nur Umap dateien und nur eine
							// einzige
							if (fileSource[0].endsWith(".umap") || fileSource[0].endsWith(".uml") ) {
								UmapParserFactory umapParserFactory = UmapParserFactory
										.getInstance();
								try {
									umapParserFactory.parseFile(fileSource[0]);
									objects = umapParserFactory.getObjects();
									
//									connectAction.setEnabled(true);
//									importAction.setEnabled(true);
//									//@ FIXME Variable ändern 
//									test.setVisible(true);
									
									
									viewer.setInput(objects);
								} catch (IOException e) {
									// TODO Add log support or Message
									e.printStackTrace();
								}
							}
						}
						return false;
					}

				});
		viewer.setInput(new String("Drag and Drop Uml or Umap File"));

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), "org.uml2abap.adt.viewer");
//		makeActions();
//		hookContextMenu();
////		hookDoubleClickAction();
//		contributeToActionBars();
//		connectAction.setEnabled(false);
//		importAction.setEnabled(false);
//		test.setVisible(false);
		

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				UmapView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	public void contributeToActionBars() {
		makeActions();
		IActionBars bars = getViewSite().getActionBars();
//		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
		bars.updateActionBars();
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(importAction);
		manager.add(new Separator());
		manager.add(connectAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(importAction);
		manager.add(connectAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(importAction);
//		manager.add(connectAction);
//		manager.add(test);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		importAction = new Action() {
			@Override
			public void run() {
				for (IUmapObject umapObject : objects) {
					((UmapObject)umapObject).setDestination(UmapSessionFactory.getInstance().getCurrentProject().getDestinationId());
//					TODO geschachtelt in einem Aufruf? Überprüfen ob Klasse schon existiert etc.
					umapObject.createClass();
					umapObject.aquireLock();
					umapObject.updateSource();
				}
			}
		};
		importAction.setText("Import Objects into SAP");
		importAction.setToolTipText("Start import of the selected Objects into SAP Backend");
		importAction.setImageDescriptor(getIconDescriptor("import.gif"));

//		connectAction = new Action() {
//			@Override
//			public void run() {
//				try {
//					String[] systems = UmapSessionFactory.getInstance().getAbapProjects();
//				} catch (Exception e) {
//					// TODO Add log or Message
//					e.printStackTrace();
//				}
//			}
//		};
//		connectAction.setText("Connect and Check");
//		connectAction.setToolTipText("Connect to System and check Objects");
//		connectAction.setImageDescriptor(getIconDescriptor("discovery.gif"));
		
		
//		doubleClickAction = new Action() {
//			public void run() {
//				ISelection selection = viewer.getSelection();
//				Object obj = ((IStructuredSelection) selection)
//						.getFirstElement();
//				// showMessage("Double-click detected on "+obj.toString());
//			}
//		};
	}

//	private void hookDoubleClickAction() {
//		viewer.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				doubleClickAction.run();
//			}
//		});
//	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	@Deprecated
	private static Image getIcon(String file) {
		Bundle bundle = FrameworkUtil.getBundle(UmapView.class);
		// URL url = FileLocator.find(bundle, new Path("icons/" + file),
		// null);
		URL url = FileLocator.find(bundle,
				new org.eclipse.core.runtime.Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();
	}
	private static ImageDescriptor getIconDescriptor(String file) {
		Bundle bundle = FrameworkUtil.getBundle(UmapView.class);
		// URL url = FileLocator.find(bundle, new Path("icons/" + file),
		// null);
		URL url = FileLocator.find(bundle,
				new org.eclipse.core.runtime.Path("icons/" + file), null);
		return ImageDescriptor.createFromURL(url);
	}	
}