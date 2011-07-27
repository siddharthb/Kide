package fr.jussieu.pps.keditor.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import fr.jussieu.pps.keditor.wizards.SimulateWizard;
import fr.jussieu.pps.keditor.testing.*;
import fr.jussieu.pps.keditor.ui.KappaUiPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;



public class version implements IWorkbenchWindowActionDelegate{

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

	
	IPreferenceStore store = KappaUiPlugin.getDefault().getPreferenceStore();
	String binary=store.getString("pathPreference");
	binary=(binary.substring(0,binary.lastIndexOf('/')+1)+"./"+binary.substring(binary.lastIndexOf('/')+1));
//	System.out.println(binary);
	  Process process=null;
	  int exitValue;
 StreamGobbler errorGobbler = null;

 StreamGobbler outputGobbler = null;

Runtime runtime=Runtime.getRuntime();
	IWorkbenchPage page = window.getActivePage();
	String s=(KappaUiPlugin.getDefault().getBundle().getLocation());

	try{
//		process = runtime.exec("chmod a+x "+s.substring(s.lastIndexOf(':')+1)+"lib/./KaSim",null,null);
//		process.waitFor();
	if(KappaUiPlugin.runningOnLinuxCompatibleSystem())	
//		process = runtime.exec(s.substring(s.lastIndexOf(':')+1)+"lib/./KaSim --version",null,null);
		process = runtime.exec(binary+" --version",null,null);
	else if(KappaUiPlugin.runningOnMacCompatibleSystem())
		process = runtime.exec(binary+" --version" ,null,null);
	else 
		System.out.println("bad Luck get Linux or MacOs");
	// get the standard and error outputs
InputStream stderr = process.getErrorStream();
InputStream stdout = process.getInputStream();


// start the two threads that will get the outputs from stderr and stdout
errorGobbler = new StreamGobbler(stderr,window);
outputGobbler = new StreamGobbler(stdout,window);
errorGobbler.start();
outputGobbler.start();
	
	}
	catch(Exception e)
	{
	System.out.println("Cannot tell the version");	
	MessageBox messageBox = new MessageBox(window.getShell(), SWT.ICON_INFORMATION); 
	messageBox.setText("Version");
	messageBox.setMessage("You input some wrong Binary");
	messageBox.open();
    
	}
//	System.out.println(errorGobbler.waitAndGetResult());

	MessageBox messageBox = new MessageBox(window.getShell(), SWT.ICON_INFORMATION); 
	messageBox.setText("Version");
	messageBox.setMessage(outputGobbler.waitAndGetResult());
	messageBox.open();
    
    
}

@Override
public void selectionChanged(IAction action, ISelection selection) {
	this.selection = selection;
	
}
}
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
