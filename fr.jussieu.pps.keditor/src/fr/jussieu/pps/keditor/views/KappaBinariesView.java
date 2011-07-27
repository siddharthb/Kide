package fr.jussieu.pps.keditor.views;
   import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
   import org.eclipse.core.resources.IFile;
   import org.eclipse.core.resources.IFolder;
  import org.eclipse.core.resources.IProject;
  import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
   import org.eclipse.core.resources.IWorkspace;
   import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
   import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
 import org.eclipse.jface.action.Action;
  import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
  import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
 import org.eclipse.jface.viewers.DoubleClickEvent;
  import org.eclipse.jface.viewers.IDoubleClickListener;
 import org.eclipse.jface.viewers.ISelection;
  import org.eclipse.jface.viewers.IStructuredContentProvider;
  import org.eclipse.jface.viewers.IStructuredSelection;
  import org.eclipse.jface.viewers.ITreeContentProvider;
  import org.eclipse.jface.viewers.LabelProvider;
  import org.eclipse.jface.viewers.TreeViewer;
  import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
  import org.eclipse.swt.SWT;
  import org.eclipse.swt.graphics.Image;
  import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
  import org.eclipse.swt.widgets.Menu;
 import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
  import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
  import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyDialogAction;
  import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import fr.jussieu.pps.keditor.ui.KappaUiPlugin;
import fr.jussieu.pps.keditor.ui.binPropertyPage;
import fr.jussieu.pps.keditor.views.MultipleSimulationView.TreeObject;
import fr.jussieu.pps.keditor.views.MultipleSimulationView.TreeParent;
import fr.jussieu.pps.keditor.wizards.SimulateWizard;
import fr.jussieu.pps.keditor.exec.fileName;

 public class KappaBinariesView extends ViewPart implements IResourceChangeListener {
           private TreeViewer viewer;
         private TreeParent invisibleRoot;
         private static final String AUTHOR_ID = "BIN.author";
         private static final TextPropertyDescriptor AUTHOR_PROP_DESC = new
         					TextPropertyDescriptor(AUTHOR_ID,"author");
         private static final IPropertyDescriptor[] DESCRIPTORS =  { AUTHOR_PROP_DESC };
    public class TreeObject1 implements IAdaptable, IPropertySource {
          private String name;
          private TreeParent parent;
          private IResource resouce;
          public TreeObject1(String name) {
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
          public String toString() {
                    return getName();
          }
          public Object getAdapter(Class key) {
                    return null;
          }
          public IResource getResouce() {
                    return resouce;
          }
          protected void setResouce(IResource resouce) {
                    this.resouce = resouce;
          }
          public Object getEditableValue() {
              return null;
  }

  public IPropertyDescriptor[] getPropertyDescriptors() {
              return DESCRIPTORS;
  }

  public Object getPropertyValue(Object id) {
        try{
          if(AUTHOR_ID.equals(id)){
             return resouce.getPersistentProperty(binPropertyPage.AUTHOR_PROP_KEY);
          }
        }catch(Exception e){

        }
     return null;
  }

  public boolean isPropertySet(Object id) {
              return false;
  }

  public void resetPropertyValue(Object id) {

  }

  public void setPropertyValue(Object id, Object value) {
     try{
       if(AUTHOR_ID.equals(id)){
         resouce.setPersistentProperty(binPropertyPage.AUTHOR_PROP_KEY,(String)value);
       }
     }catch(Exception e){

     }
  }
    }
    class TreeParent extends TreeObject1 {
          private ArrayList children;
          public TreeParent(String name) {
                    super(name);
                    children = new ArrayList();
         }
          public void addChild(TreeObject1 child) {
                    children.add(child);
                    child.setParent(this);
         }
          public void removeChild(TreeObject1 child) {
                    children.remove(child);
                    child.setParent(null);
          }
          public TreeObject1[] getChildren() {
                    return (TreeObject1[]) children.toArray(new TreeObject1[children.size()]);
          }
          public boolean hasChildren() {
                    return children.size() > 0;
        }
   }
   class ViewContentProvider implements ITreeContentProvider {
          public void inputChanged(Viewer v, Object oldInput, Object newInput) {
          }

          public void dispose() {
          }
         public Object[] getElements(Object parent) {
                    if (parent.equals(getViewSite())) {
                             if (invisibleRoot == null)
                                       initialize();
                             return getChildren(invisibleRoot);
                   }
                   return getChildren(parent);
         }
         public Object getParent(Object child) {
                    if (child instanceof TreeObject1) {
                             return ((TreeObject1) child).getParent();
                           }
                           return null;
                 }
                  public Object[] getChildren(Object parent) {
                            if (parent instanceof TreeParent) {
                                    return ((TreeParent) parent).getChildren();
                            }
                            return new Object[0];
                  }
                  public boolean hasChildren(Object parent) {
                            if (parent instanceof TreeParent)
                                     return ((TreeParent) parent).hasChildren();
                           return false;
                  }
         }
         class ViewLabelProvider extends LabelProvider {
                 public String getText(Object obj) {
                	 TreeObject1 tempObj = (TreeObject1) obj;
                	 if(tempObj.getResouce()!=null)
                            return obj.toString()+ "  ["+tempObj.getResouce().getFullPath().toString().substring(0,tempObj.getResouce().getFullPath().toString().lastIndexOf('/')+1)+"]";
                	 else return obj.toString();
                  }
                  public Image getImage(Object obj) {
                    Image image;
                    String url="";
                	  if (obj instanceof TreeParent)
                       {   String imageKey = ISharedImages.IMG_OBJ_FOLDER;
                       return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
                       }else url = "/images/compression.png";
                	  ImageDescriptor descriptor = ImageDescriptor.createFromURL(KappaUiPlugin.getDefault().getBundle().getEntry(url));
           			 image = descriptor.createImage();
                       
                  		return image;
                  }
        }
        public void initialize() {
        //	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            
            String folderName = "Binary Files";
                        
            TreeParent root = new TreeParent(folderName);
                  try {
                      IWorkspace workspace = ResourcesPlugin.getWorkspace();
                      IProject[] projects = workspace.getRoot().getProjects();
                      for (int i = 0; i < projects.length; i++) {
                           IResource[] folderResources = projects[i].members();
                           for (int j = 0; j < folderResources.length; j++) {
                        	   if (folderResources[j] instanceof IFile &&           
                                       folderResources[j].getName().endsWith(".bin")){
                                        TreeObject1 obj = new TreeObject1(folderResources[j]
					.getName());
                                         obj.setResouce(folderResources[j]);
                                         root.addChild(obj);
                                      }
                               if (folderResources[j] instanceof IFolder) {
                        IFolder resource = (IFolder) folderResources[j];
                                  if (true){//resource.getName().equalsIgnoreCase("PropertyFiles")) {
                                    IResource[] fileResources = resource.members();
                                     for (int k = 0; k < fileResources.length; k++) {
                                       if (fileResources[k] instanceof IFile &&           
                                          fileResources[k].getName().endsWith(".bin")){
                                           TreeObject1 obj = new TreeObject1(fileResources[k]
					.getName());
                                            obj.setResouce(fileResources[k]);
                                            root.addChild(obj);
                                         }
                                      }
                               	 }
                               }
                         }
                       }
                  }catch (Exception e) {
                     // log exception
                  }
                  invisibleRoot = new TreeParent("");
                  invisibleRoot.addChild(root);
         }
        
        public void folderRec(TreeParent root,IResource[] folderResources)
        {
        	try{
        	for (int j = 0; j < folderResources.length; j++) {
         	   if (folderResources[j] instanceof IFile &&           
                        (folderResources[j].getName().endsWith(".bin"))){
                         TreeObject1 obj = new TreeObject1(folderResources[j]
		.getName());
                          obj.setResouce(folderResources[j]);
                          root.addChild(obj);
                       }
                if (folderResources[j] instanceof IFolder) {
         IFolder resource = (IFolder) folderResources[j];
                   if (true){
                     IResource[] fileResources = resource.members();
                      for (int k = 0; k < fileResources.length; k++) {
                        if (fileResources[k] instanceof IFile &&           
                           (fileResources[k].getName().endsWith(".bin"))){
                            TreeObject1 obj = new TreeObject1(fileResources[k]
		.getName());
                             obj.setResouce(fileResources[k]);
                             root.addChild(obj);
                          }
                        if(fileResources[k] instanceof IFolder)
                        {
                        	IFolder resource1 = (IFolder) fileResources[k];
                        	IResource[] fileResources1 = resource1.members();
                            folderRec(root,fileResources1);
                        }
                       }
                	 }
                }
          }
        	}
        	catch(Exception e){
        		System.out.println("Error in folder Recursion");
        	}
        }


         public KappaBinariesView() {
        }
         public void createPartControl(Composite parent) {
             viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
             viewer.setContentProvider(new ViewContentProvider());
             viewer.setLabelProvider(new ViewLabelProvider());
             viewer.setInput(getViewSite());
             ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
             getSite().setSelectionProvider(viewer);
             hookContextMenu();
             hookDoubleCLickAction();
    }
         public void dispose() {
             super.dispose();
             ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    }
         
         public void resourceChanged(IResourceChangeEvent event) {
        	     try{
        	            if(event.getType() == IResourceChangeEvent.POST_CHANGE){
        	                event.getDelta().accept(new IResourceDeltaVisitor(){
        	                       public boolean visit(IResourceDelta delta) throws CoreException {
        	                            if(delta.getResource().getName().endsWith(".bin")){
        	                                     initialize();
        	                                    Display.getDefault().asyncExec(new Runnable() {
        	                                                    public void run() {
        	                                                             viewer.refresh();
        	                                                             viewer.expandAll();
        	                                                   	   }
        	                                                    });
        	                                             }
        	                                             return true;
        	                                        }
        	                             });
        	                     }
        	           }catch(CoreException e){
        	                    // log it
        	                     e.printStackTrace();
        	           }
        	   }
         
         private void hookDoubleCLickAction() {
                viewer.addDoubleClickListener(new IDoubleClickListener() {
                       public void doubleClick(DoubleClickEvent event) {
                          ISelection selection = event.getSelection();
                            Object obj = ((IStructuredSelection) selection).getFirstElement();
                           if (!(obj instanceof TreeObject1)) {
                                  return;
                            }else {
                                 TreeObject1 tempObj = (TreeObject1) obj;
                                IFile ifile = ResourcesPlugin.getWorkspace().getRoot().
				      getFile(tempObj.getResouce().getFullPath());
                                 IWorkbenchPage dpage =
                                                   KappaBinariesView.this.getViewSite()
    							  .getWorkbenchWindow().getActivePage();
                                 if (dpage != null) {
                                    try {
                                           IDE.openEditor(dpage, ifile,true);
                                    }catch (Exception e) {
                                                      // log exception
                                  }
                             }
                        }
                    };
              });
         }
         private void hookContextMenu() {
               MenuManager menuMgr = new MenuManager("#PopupMenu");
                  Menu menu = menuMgr.createContextMenu(viewer.getControl());
                 viewer.getControl().setMenu(menu);
                 Action refresh =new Action() {
                            public void run() {
                                     initialize();
                                     viewer.refresh();
                            }
                 };
                  refresh.setText("Refresh");
                 menuMgr.add(refresh);
                 menuMgr.add(new Separator());
                 Action simulate =new Action() {
                     public void run() {
                     //     System.out.println("Hello World");
                          String s="",finalp="";
                          ISelection selection=viewer.getSelection();
                          Object obj = ((IStructuredSelection)selection).getFirstElement();
                          if (!(obj instanceof TreeObject1)) {
                              return;
                        }else {
                             TreeObject1 tempObj = (TreeObject1) obj;
                            String path1=Platform.getLocation().toString();
                            path1=path1+tempObj.getResouce().getFullPath();
                    			path1=path1.replaceAll(" ","\\ ");
                            //System.out.println(path1);
                    			s=s+"-load-sim "+path1+" ";
                    			finalp= ""+tempObj.getResouce().getFullPath();
                        		
                    		//	System.out.println(s);
                        }
                          IWorkbenchWindow window = getViewSite().getWorkbenchWindow();
                          
                        SimulateWizard wizard = new SimulateWizard(window.getWorkbench(),s,(IContainer)ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(finalp.substring(0, finalp.lastIndexOf('/')+1))));
      				    WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
      				    dialog.create();
      				    dialog.open();
                     
                         // System.out.println(((IStructuredSelection) selection).size());

                     }
                 };
           simulate.setText("Load binary...");
          menuMgr.add(simulate);
          
             menuMgr.add(new PropertyDialogAction(getSite(), viewer));
         }
   
         public void setFocus() {
                 viewer.getControl().setFocus();
         }
 }

 