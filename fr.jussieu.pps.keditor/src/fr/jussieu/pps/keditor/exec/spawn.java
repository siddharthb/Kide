package fr.jussieu.pps.keditor.exec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IFileEditorInput;

import fr.jussieu.pps.keditor.testing.progressBar;
import fr.jussieu.pps.keditor.ui.KappaUiPlugin;


public class spawn
{
  public static void main(String path,IWorkbenchWindow window,String cmd,String dest)
  {
	
//	  progressBar.main(path, window, cmd);
		
	  Shell shell=window.getShell();
		

	  Process process=null;
	  int exitValue;
 StreamGobbler errorGobbler = null;

 StreamGobbler outputGobbler = null;
	
	IPreferenceStore store = KappaUiPlugin.getDefault().getPreferenceStore();
	String binary=store.getString("pathPreference");
	binary=(binary.substring(0,binary.lastIndexOf('/')+1)+"./"+binary.substring(binary.lastIndexOf('/')+1));

 
 Runtime runtime = Runtime.getRuntime();
		try {
			// execute the command
			process = runtime.exec("rm -rf "+dest,null,null);
			exitValue = process.waitFor();

					File f = new File("");
			//		System.out.println(KappaUiPlugin.getDefault().getStateLocation().toString());
					String s=(KappaUiPlugin.getDefault().getBundle().getLocation());

				if(KappaUiPlugin.runningOnLinuxCompatibleSystem())	
					process = runtime.exec(binary+" --eclipse " + path + " -d "+ dest.substring(0,dest.lastIndexOf('/')+1)+" " + cmd,null,new File(f.getAbsolutePath()));
			//		process = runtime.exec(s.substring(s.lastIndexOf(':')+1)+"lib/./KaSim --eclipse " + path + " " + cmd,null,new File(f.getAbsolutePath()));
				else if(KappaUiPlugin.runningOnMacCompatibleSystem())
					process = runtime.exec(binary+" --eclipse " + path + " " + cmd,null,new File(f.getAbsolutePath()));
					//			process = runtime.exec(s.substring(s.lastIndexOf(':')+1)+"lib/./KaSimMac " + path + " " + cmd,null,new File(f.getAbsolutePath()));
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
			
			new ProgressMonitorDialog(shell).run(true, true,new LongRunningOperation(true));
			LongRunningOperation.error=true;
			//	System.out.println(errorGobbler.waitAndGetResult());
			   	MessageBox messageBox = new MessageBox(shell, SWT.ICON_WORKING); 
		        messageBox.setText("Result");
		        messageBox.setMessage(outputGobbler.waitAndGetResult());
		        messageBox.open();
		        String s1=errorGobbler.waitAndGetResult();
		        if(s1.length()!=0)  
		    { 
		        	MessageBox messageBox1 = new MessageBox(shell, SWT.ICON_ERROR); 
		        messageBox1.setText("Error");
		        messageBox1.setMessage(s1);
		        messageBox1.open();
		    } 
			// wait for the process to end
		    exitValue = process.waitFor();
		    ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
	        		
	//		System.out.println("Ran till here");
	//		s=(KappaUiPlugin.getDefault().getBundle().getLocation());
	//	    process = runtime.exec("java -jar "+s.substring(s.lastIndexOf(':')+1,(s.substring(0,s.length()-1)).lastIndexOf('/'))+"/PhiBPlot.jar abc.out",null,null);
		   //   exitValue = process.waitFor();
			    
	//	      System.out.println("Ran successfully");
				

		} catch (IOException e) {
		System.out.println("Error in Running");
		File f = new File("");
		System.out.println(f.getAbsolutePath());
		}
		catch (InvocationTargetException e) {
		     MessageDialog.openError(shell, "Error", e.getMessage());
		   } catch (InterruptedException e) {
			   if (e.getMessage().compareTo("Your simulation was cancelled")==0)
			   {
			 boolean d=MessageDialog.openQuestion(shell, "Save State", "Do you want to save the current state?");
			 System.out.println(d);  
			 process.destroy();
		     MessageDialog.openInformation(shell, "Cancelled", e.getMessage());
			   }
			   else
				   MessageDialog.openInformation(shell, "Deadlock", e.getMessage());
				 	   
			   } catch (CoreException e) {
			System.out.println("Error in refreshing");
		}
 
  }
  
  
}

class LongRunningOperation implements IRunnableWithProgress {
	  // The total sleep time
	  private static final int TOTAL_TIME = 60;

	  // The increment sleep time
	
	  public static int num=0;
	  public static boolean dl=false;	  
	  public static boolean error=false;	  
	  private boolean indeterminate;

	  /**
	   * LongRunningOperation constructor
	   * 
	   * @param indeterminate whether the animation is unknown
	   */
	  public LongRunningOperation(boolean indeterminate) {
	    this.indeterminate = indeterminate;
	    num=0;
	    dl=false;
	    error=false;
	  }

	  /**
	   * Runs the long running operation
	   * 
	   * @param monitor the progress monitor
	   */
	  public void run(IProgressMonitor monitor) throws InvocationTargetException,
	      InterruptedException {
	    monitor.beginTask("Simulating....",58);
	    
	    for (int total = 0; total < TOTAL_TIME-2 && !monitor.isCanceled() && !dl && !error;) 
	    {	
	    	if(num>total)
	    		{
	    		monitor.worked(num-total);
	    		total=num;
	    		monitor.subTask("Please wait....");
	    		}
	      
	    }
	    monitor.done();
	    if (monitor.isCanceled())
	        throw new InterruptedException("Your simulation was cancelled");
	    if (dl)
	        throw new InterruptedException("Your simulation has reached a deadlock");
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
					if(line.compareTo("#")==0)
						LongRunningOperation.num++;
					else if(line.compareTo("?")==0)
						LongRunningOperation.dl=true;
				//	System.out.println(LongRunningOperation.num);
					else
					this.result.append(line + "\n");
			}
				LongRunningOperation.error=true;
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