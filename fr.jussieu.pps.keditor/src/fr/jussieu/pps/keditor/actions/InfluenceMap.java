package fr.jussieu.pps.keditor.actions;

import java.io.File;

import fr.jussieu.pps.keditor.views.InfluenceMapView;
import fr.jussieu.pps.keditor.wizards.SimulateWizard;
import fr.jussieu.pps.keditor.testing.*;
import fr.jussieu.pps.keditor.ui.KappaUiPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;



public class InfluenceMap implements IWorkbenchWindowActionDelegate{

	IWorkbenchWindow window;
	ISelection selection;
	public static final String ID = "fr.jussieu.pps.keditor.views.InfluenceMapView";

@Override
public void dispose() {
	// TODO Auto-generated method stub
	
}

@Override
public void init(IWorkbenchWindow window) {
	this.window=window;
	// TODO Auto-generated method stub
	
}

@Override
public void run(IAction action) {
	// Instantiates and initializes the wizard
//temporarily removed
  //  parseCheck.main(window);
	
	if (!(window.getActivePage().getActiveEditor().getEditorInput() instanceof IFileEditorInput)) return;
	IFile file = ((IFileEditorInput) window.getActivePage().getActiveEditor().getEditorInput()).getFile();
		
//	System.out.println("jumbawumba");
 
	IWorkbenchPage page = window.getActivePage();

    try {
    
    	if(page.findView("fr.jussieu.pps.keditor.views.InfluenceMapView")==null)
		{
    	       page.showView(ID);
    		System.out.println("null");
		}
	else 
	{
		IFile f=((IFileEditorInput)page.findView("fr.jussieu.pps.keditor.views.InfluenceMapView").getViewSite().getPage().getActiveEditor().getEditorInput()).getFile();
		String path1=Platform.getLocation().toString();
		String path2=path1+f.getFullPath().toString();
		path2=path2.replaceAll(" ","\\ ");
		String pathnew=path2.substring(0,path2.lastIndexOf("/")+1);
//		System.out.println(pathnew);
		 Process process=null;
			Runtime runtime = Runtime.getRuntime();
			IPreferenceStore store = KappaUiPlugin.getDefault().getPreferenceStore();
			String binary=store.getString("pathPreference");
			binary=(binary.substring(0,binary.lastIndexOf('/')+1)+"./"+binary.substring(binary.lastIndexOf('/')+1));

			try {
			//	System.out.println(pathnew);
				// execute the command
				process = runtime.exec("rm -rf "+pathnew+"map.gif",null,null);
				process.waitFor();
				process = runtime.exec("rm -rf "+pathnew+"map.dot",null,null);
				process.waitFor();
				
				
				File f1 = new File("");
				String s=(KappaUiPlugin.getDefault().getBundle().getLocation());
	//			System.out.println(path2);
//				process = runtime.exec(s.substring(s.lastIndexOf(':')+1)+"lib/./KaSim -i " + path2 + " -im map.dot -e 0",null,new File(f1.getAbsolutePath()));
				process = runtime.exec(binary+" -i " + path2 + " -im map.dot -e 0 -d "+pathnew,null,new File(f1.getAbsolutePath()));

				// get the standard and error outputs
	
				// start the two threads that will get the outputs from stderr and stdout
				
				// wait for the process to end
				process.waitFor();
			      process = runtime.exec("dot -T gif "+pathnew+"map.dot -o "+pathnew+"map.gif",null,null);
			      process.waitFor();				    
			  //    System.out.println("Ran successfully");
			      InfluenceMapView.imageCanvas.onFileOpen(pathnew);
			      page.showView(ID);
			} catch (Exception e) {
			System.out.println("Error in Running");
			}
	//	page.findView("fr.jussieu.pps.keditor.views.InfluenceMapView").;
		System.out.println("not null");
	}

 // use the Resource Manager View id to open up view.

    } catch (PartInitException e) {

    }
}

@Override
public void selectionChanged(IAction action, ISelection selection) {
	this.selection = selection;
	
}
}
