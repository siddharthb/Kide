package fr.jussieu.pps.keditor.testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbenchWindow;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import fr.jussieu.pps.keditor.ui.KappaUiPlugin;

/**
 * This class demonstrates JFace's ProgressMonitorDialog class
 */
public class progressBar extends ApplicationWindow {
  /**
   * ShowProgress constructor
   */
	static String path;
	static IWorkbenchWindow window;
	static String cmd;
	public static String out;
	public static String err;	
	public progressBar(String path1,IWorkbenchWindow window1,String cmd1) {
    super(null);
    path=path1;
    cmd=cmd1;
    window=window1;
    out="";
    err="";
  }

  /**
   * Runs the application
   */
  public void run() {
    // Don't return from open() until window closes
    setBlockOnOpen(true);

    // Open the main window
    open();
    // Dispose the display
 //   Display.getCurrent().dispose();
  }

  /**
   * Configures the shell
   * 
   * @param shell the shell
   */
  protected void configureShell(Shell shell) {
    super.configureShell(shell);
    // Set the title bar text
    shell.setText("Show Progress");
  }

  /**
   * Creates the main window's contents
   * 
   * @param parent the main window
   * @return Control
   */
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, true));

    // Create the indeterminate checkbox
 //   final Button indeterminate = new Button(composite, SWT.CHECK);
  //  indeterminate.setText("Indeterminate");

    // Create the ShowProgress button
  //  Button showProgress = new Button(composite, SWT.NONE);
   // showProgress.setText("Show Progress");

    final Shell shell = parent.getShell();
    // Display the ProgressMonitorDialog
  //  showProgress.addSelectionListener(new SelectionAdapter() {
  //    public void widgetSelected(SelectionEvent event) {
        try {
          new ProgressMonitorDialog(shell).run(true, true,
              new LongRunningOperation(true));
          close();
        } catch (InvocationTargetException e) {
          MessageDialog.openError(shell, "Error", e.getMessage());
          close();
        } catch (InterruptedException e) {
          MessageDialog.openInformation(shell, "Cancelled", e.getMessage());
          close();
        }
   //   }
  //  });

    parent.pack();
    return composite;
  }

  /**
   * The application entry point
   * 
   * @param args the command line arguments
   */
  public static void main(String path,IWorkbenchWindow window,String cmd) {
    new progressBar(path,window,cmd).run();
  }
}



/**
 * This class represents a long running operation
 */
class LongRunningOperation implements IRunnableWithProgress {
  // The total sleep time
  private static final int TOTAL_TIME = 10000;

  // The increment sleep time
  private static final int INCREMENT = 500;

  private boolean indeterminate;

  /**
   * LongRunningOperation constructor
   * 
   * @param indeterminate whether the animation is unknown
   */
  public LongRunningOperation(boolean indeterminate) {
    this.indeterminate = indeterminate;
  }

  /**
   * Runs the long running operation
   * 
   * @param monitor the progress monitor
   */
  public void run(IProgressMonitor monitor) throws InvocationTargetException,
      InterruptedException {
	  monitor.setTaskName("Compiling The code");
	  monitor.subTask("Please wait...");
	  
	  
	  Process process=null;
	  int exitValue;
	  StreamGobbler errorGobbler = null;

	  StreamGobbler outputGobbler = null;
	  Runtime runtime = Runtime.getRuntime();
		try {
			// execute the command
			process = runtime.exec("rm -rf abc.out",null,null);
			exitValue = process.waitFor();

					File f = new File("");
					System.out.println(KappaUiPlugin.getDefault().getStateLocation().toString());
					String s=(KappaUiPlugin.getDefault().getBundle().getLocation());

				    monitor.beginTask("Running long running operation",
				            indeterminate ? IProgressMonitor.UNKNOWN : TOTAL_TIME);
				    monitor.subTask("Simulating...");
				if(KappaUiPlugin.runningOnLinuxCompatibleSystem())	
					process = runtime.exec(s.substring(s.lastIndexOf(':')+1)+"lib/./KaSim " + progressBar.path + " " + progressBar.cmd,null,new File(f.getAbsolutePath()));
				else if(KappaUiPlugin.runningOnMacCompatibleSystem())
					process = runtime.exec(s.substring(s.lastIndexOf(':')+1)+"lib/./KaSimMac " + progressBar.path + " " + progressBar.cmd,null,new File(f.getAbsolutePath()));
				else 
					System.out.println("bad Luck get Linux or MacOs");
				// get the standard and error outputs
		//		System.out.println("Ran till here4");
				
			InputStream stderr = process.getErrorStream();
			InputStream stdout = process.getInputStream();

			// start the two threads that will get the outputs from stderr and stdout
			errorGobbler = new StreamGobbler(stderr,progressBar.window);
			outputGobbler = new StreamGobbler(stdout,progressBar.window);
			errorGobbler.start();
			outputGobbler.start();
	//		System.out.println("Ran till here3");
			
		//	System.out.println(errorGobbler.waitAndGetResult());
/*			for (int total = 0;!monitor.isCanceled(); total += INCREMENT) 
			{
			      Thread.sleep(INCREMENT);
			      monitor.worked(INCREMENT);
			      if (total == TOTAL_TIME / 2) monitor.subTask("Doing second half");
		    }
*/			
		//	System.out.println("Ran till here2");
			
	//		Shell shell=progressBar.shell1;
	//		System.out.println("Ran till here1.5");
			   	
		//		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WORKING); 
		 //       messageBox.setText("Result");
	   // 	System.out.println("Ran till here1.4");
	    	progressBar.out=outputGobbler.waitAndGetResult();
	//		    monitor.subTask(outputGobbler.waitAndGetResult());
	//			Thread.sleep(3000);
		        String out=(outputGobbler.waitAndGetResult());
		//    	System.out.println("Ran till here1.3");
				
		 //       messageBox.open();
		        progressBar.err=errorGobbler.waitAndGetResult();
		        if(progressBar.err.length()!=0)  
		    { 
		            monitor.subTask(progressBar.err);
					Thread.sleep(3000);
		       // 	MessageBox messageBox1 = new MessageBox(shell, SWT.ICON_ERROR); 
		      //  messageBox1.setText("Error");
		      //  messageBox1.setMessage(s1);
		      //  messageBox1.open();
		    } 
			// wait for the process to end
		//    	System.out.println("Ran till here1");
				
		
		    exitValue = process.waitFor();

		   //   exitValue = process.waitFor();
			    
		      System.out.println("Ran successfully");
		    
		      monitor.done();

			  	if (monitor.isCanceled())
				{ 
			  		process.destroy();
					throw new InterruptedException("The long running operation was cancelled");
				}

		      

		} catch (Exception e) {
		System.out.println("Error in Running");
		File f = new File("");
		System.out.println(f.getAbsolutePath());
		}
	   
	  
    
    
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
