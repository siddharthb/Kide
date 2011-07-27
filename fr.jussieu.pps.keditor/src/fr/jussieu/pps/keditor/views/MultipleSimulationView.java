package fr.jussieu.pps.keditor.views;
   import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.eclipse.core.runtime.QualifiedName;
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
import org.eclipse.swt.widgets.MessageBox;
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
import fr.jussieu.pps.keditor.ui.propertyPage;
import fr.jussieu.pps.keditor.wizards.SimulateWizard;
import fr.jussieu.pps.keditor.exec.fileName;
import fr.jussieu.pps.keditor.views.KappaBinariesView.TreeObject1;

 public class MultipleSimulationView extends ViewPart implements IResourceChangeListener {
           private TreeViewer viewer;
         private TreeParent invisibleRoot;
         public static IContainer cnt;
         private static final String AUTHOR_ID = "RMP.author";
         private static final String COMMENT_ID = "RMP.comment";
         
         private static final TextPropertyDescriptor AUTHOR_PROP_DESC = new
         					TextPropertyDescriptor(AUTHOR_ID,"author");

         private static final TextPropertyDescriptor COMMENT_PROP_DESC = new
			TextPropertyDescriptor(COMMENT_ID,"comment");

         private static final IPropertyDescriptor[] DESCRIPTORS =  { AUTHOR_PROP_DESC,COMMENT_PROP_DESC };
    public class TreeObject implements IAdaptable, IPropertySource {
          private String name;
          private TreeParent parent;
          private IResource resouce;
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
             return resouce.getPersistentProperty(propertyPage.AUTHOR_PROP_KEY);
          }else if(COMMENT_ID.equals(id)){
                 return resouce.getPersistentProperty(propertyPage.COMMENT_PROP_KEY);
              
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
         resouce.setPersistentProperty(propertyPage.AUTHOR_PROP_KEY,(String)value);
       }else if(COMMENT_ID.equals(id)){
             resouce.setPersistentProperty(propertyPage.COMMENT_PROP_KEY,(String)value);
           
       }
     }catch(Exception e){

     }
  }
    }
    class TreeParent extends TreeObject {
          private ArrayList children;
          public TreeParent(String name) {
                    super(name);
                    children = new ArrayList();
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
                    return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
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
                    if (child instanceof TreeObject) {
                             return ((TreeObject) child).getParent();
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
                	 TreeObject tempObj = (TreeObject) obj;
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
                       }else url = "/images/kappaeditor.png";
                	  ImageDescriptor descriptor = ImageDescriptor.createFromURL(KappaUiPlugin.getDefault().getBundle().getEntry(url));
           			 image = descriptor.createImage();
                       
                  		return image;
                  }
        }
        public void initialize() {
        //	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            
            String folderName = "Kappa Files";
                        
            TreeParent root = new TreeParent(folderName);
                  try {
                      IWorkspace workspace = ResourcesPlugin.getWorkspace();
                      IProject[] projects = workspace.getRoot().getProjects();
                      for (int i = 0; i < projects.length; i++) {
                           IResource[] folderResources = projects[i].members();
                           folderRec(root,folderResources);
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
                        (folderResources[j].getName().endsWith(".ka")||folderResources[j].getName().endsWith(".ks"))){
                         TreeObject obj = new TreeObject(folderResources[j]
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
                           (fileResources[k].getName().endsWith(".ka")||folderResources[j].getName().endsWith(".ks"))){
                            TreeObject obj = new TreeObject(fileResources[k]
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

         public MultipleSimulationView() {
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
        	                            if(delta.getResource().getName().endsWith(".ka")||delta.getResource().getName().endsWith(".ks")){
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
                           if (!(obj instanceof TreeObject)) {
                                  return;
                            }else {
                                 TreeObject tempObj = (TreeObject) obj;
                                IFile ifile = ResourcesPlugin.getWorkspace().getRoot().
				      getFile(tempObj.getResouce().getFullPath());
                                 IWorkbenchPage dpage =
                                                   MultipleSimulationView.this.getViewSite()
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
                          String s="",finalp="";
                          ISelection selection=viewer.getSelection();
                          for(Iterator<Object> i=((IStructuredSelection) selection).iterator();i.hasNext();)
                          {
                        	  Object obj=i.next();
                          if (!(obj instanceof TreeObject)) {
                                 return;
                           }else {
                                TreeObject tempObj = (TreeObject) obj;
                               String path1=Platform.getLocation().toString();
                               path1=path1+tempObj.getResouce().getFullPath();
                       			path1=path1.replaceAll(" ","\\ ");
                       			s=s+"-i "+path1+" ";
                       			finalp= ""+tempObj.getResouce().getFullPath();
                        		
                           }
                          }
                         IWorkbenchWindow window = getViewSite().getWorkbenchWindow();
                      	SimulateWizard wizard = new SimulateWizard(window.getWorkbench(),s.trim(),(IContainer)ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(finalp.substring(0, finalp.lastIndexOf('/')+1))));
                        WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
                        dialog.create();
                        dialog.open();

                     }
                 };
           simulate.setText("Simulate");
          menuMgr.add(simulate);
          
          Action bin =new Action() {
              public void run() {
                   String s="";
                   String bin="",finalp="";
                   ISelection selection=viewer.getSelection();
                   for(Iterator<Object> i=((IStructuredSelection) selection).iterator();i.hasNext();)
                   {
                 	  Object obj=i.next();
                   if (!(obj instanceof TreeObject)) {
                          return;
                    }else {
                         TreeObject tempObj = (TreeObject) obj;
                        String path1=Platform.getLocation().toString();
                        path1=path1+tempObj.getResouce().getFullPath();
                		bin= bin + tempObj.getResouce().getFullPath()+"\n";
                		finalp= ""+tempObj.getResouce().getFullPath();
                		path1=path1.replaceAll(" ","\\ ");
                			s=s+"-i "+path1+" ";
                    }
                   }
                  IWorkbenchWindow window = getViewSite().getWorkbenchWindow();
                  Runtime runtime = Runtime.getRuntime();
                  Process process=null;
                  String s1=(KappaUiPlugin.getDefault().getBundle().getLocation());
                  File f=new File("");
              	String path1=(s.substring(s.trim().lastIndexOf(' '),s.lastIndexOf('/')+1));
			//	System.out.println(finalp);
                  try {
               	  IResource container = ResourcesPlugin.getWorkspace().getRoot()
      				.findMember(new Path(finalp.substring(0, finalp.lastIndexOf('/')+1)));
             //  	  System.out.println(container.exists());
             //  	System.out.println(container.getFullPath().toString());
          		
               	  cnt=(IContainer) container;
					InputDialog dialog=new InputDialog(window.getShell(),"File Name","Enter the name of the bin file","default",new valid());
					dialog.create();
					dialog.open();
					String v=dialog.getValue();
					dialog.close();
				
					IPreferenceStore store = KappaUiPlugin.getDefault().getPreferenceStore();
					String binary=store.getString("pathPreference");
					binary=(binary.substring(0,binary.lastIndexOf('/')+1)+"./"+binary.substring(binary.lastIndexOf('/')+1));

					
					if(v!=null)
					{
						process = runtime.exec(binary+" " + s + " -make-sim "+v+".bin -d "+ path1.trim() +" -e 0",null,null);
						//	process = runtime.exec(s1.substring(s1.lastIndexOf(':')+1)+"lib/./KaSim " + s + " -make-sim "+v+".bin -d "+ path1.trim() +" -e 0",null,null);
					process.waitFor();
					finalp=finalp.substring(0,finalp.lastIndexOf('/')+1)+v+".bin";
					}
					/*
					InputStream stderr = process.getErrorStream();
					InputStream stdout = process.getInputStream();

					StreamGobbler errorGobbler = null;

					StreamGobbler outputGobbler = null;

					// start the two threads that will get the outputs from stderr and stdout
					errorGobbler = new StreamGobbler(stderr,window);
					outputGobbler = new StreamGobbler(stdout,window);
					errorGobbler.start();
					outputGobbler.start();

			        String s2=outputGobbler.waitAndGetResult();
					System.out.println(s2);
					s2=errorGobbler.waitAndGetResult();
					System.out.println(s2);
	*/
					
		/*			SimulateWizard wizard = new SimulateWizard(window.getWorkbench(),file.getName(),file.getFullPath().toString());
				    WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
				    dialog.create();
				    dialog.open();
			*/		
					
					
					if(v!=null)
					{
					MessageBox messageBox1 = new MessageBox(window.getShell(), SWT.ICON_INFORMATION); 
			        messageBox1.setText("Success");
			        messageBox1.setMessage(v+".bin has been successfully created.");
			        messageBox1.open();
			
			    //    System.out.println(finalp.substring(0,finalp.lastIndexOf('/')+1));
			     //   if(finalp.substring(1,finalp.lastIndexOf('/')).indexOf('/')==-1)
			      //  	ResourcesPlugin.getWorkspace().getRoot().
			      //  else
			        ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
			        IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(finalp));
			        
			        IResource ir=ifile;
			//        System.out.println(ir.exists());
            String value = bin.trim();
            if (value.equals(""))
               value = null;
            try 
            {
                        ir.setPersistentProperty(new QualifiedName("Author", "Author"),value);
            }
            catch (CoreException e) 
            {
            	System.out.println("Property couldn't be set");
            }
            
					}
						
						} catch (Exception e) {
							MessageBox messageBox1 = new MessageBox(window.getShell(), SWT.ERROR); 
					        messageBox1.setText("Error");
					        messageBox1.setMessage("Error in making bin. Maybe KaSim's bin path is not correctly set");
					        messageBox1.open();
					 
					System.out.println("Error in Making bin");
					e.printStackTrace();
				}
    			
                   }
              
          };
    bin.setText("Make bin...");
   menuMgr.add(bin);
             menuMgr.add(new PropertyDialogAction(getSite(), viewer));
         }
   
         public void setFocus() {
                 viewer.getControl().setFocus();
         }
 }
 
 class valid implements IInputValidator
 {

	@Override
	public String isValid(String newText) {
		if(newText==null)
			return "The name cannot be empty";
		else if(newText.equals(""))
			return "The name cannot be empty";
		else if(newText.indexOf(' ')!=-1)
			return "The name cannot contain spaces";
		else if(newText.contains(".bin".subSequence(0, 4)))
			return ".bin will automatically be appended";
		IFile file = MultipleSimulationView.cnt.getFile(new Path(newText+".bin"));
		if (file.exists())
			return "Bin file with the name already exists";
		
		return null;
	}
	 
 }
 
 
 /**
  * Utility class, used by CommandRunner to absorb the output of a process quickly, so as to avoid
  * saturating the system buffers (which makes the whole application hang...).
  */
 class StreamGobbler extends Thread {

 	private InputStream inputStream = null;
 	private StringBuffer result = new StringBuffer();
 	private IWorkbenchWindow window;

 	StreamGobbler(InputStream inputStream,IWorkbenchWindow window) {
 		this.inputStream = inputStream;
 		this.window=window;
 	}

 	@Override
 	public void run() {
 		this.fillReturnBuffer();
 	}

 	protected synchronized void fillReturnBuffer() {
 		try {
 			InputStreamReader inputStreamReader = new InputStreamReader(this.inputStream);
 			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
 			String line = null;
 				while ((line = bufferedReader.readLine()) != null) {
 					
 					this.result.append(line + "\n");
 			}
 		} catch (Throwable e) {
 		//	ocaml.OcamlPlugin.logError("error in StreamGobbler:fillReturnBuffer", e);
 		}
 	}

 	public synchronized String waitAndGetResult() {
 		try {
 			this.join();
 		} catch (InterruptedException e) {
 		//	ocaml.OcamlPlugin.logError("StreamGobbler:waitAndGetResult interrupted", e);
 		}
 		
 		return this.result.toString();
 	}
 }