package fr.jussieu.pps.keditor.exec;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IFileEditorInput;
import fr.jussieu.pps.keditor.ui.KappaUiPlugin;


public class spawn
{
  public static void main(String path,IWorkbenchWindow window,String cmd)
  {
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
			process = runtime.exec(s.substring(s.lastIndexOf(':')+1)+"lib/./KaSim " + path + " " + cmd,null,new File(f.getAbsolutePath()));

			// get the standard and error outputs
			InputStream stderr = process.getErrorStream();
			InputStream stdout = process.getInputStream();

			// start the two threads that will get the outputs from stderr and stdout
			errorGobbler = new StreamGobbler(stderr,window);
			outputGobbler = new StreamGobbler(stdout,window);
			errorGobbler.start();
			outputGobbler.start();
		//	System.out.println(errorGobbler.waitAndGetResult());
			 Shell shell=window.getShell();
			   	MessageBox messageBox = new MessageBox(shell, SWT.ICON_WORKING); 
		        messageBox.setText("Result");
		        messageBox.setMessage(outputGobbler.waitAndGetResult());
		        messageBox.open();
		        String s1=errorGobbler.waitAndGetResult();
		        if(s1.length()!=0)  
		    { MessageBox messageBox1 = new MessageBox(shell, SWT.ICON_ERROR); 
		        messageBox1.setText("Error");
		        messageBox1.setMessage(s1);
		        messageBox1.open();
		    } 
			// wait for the process to end
			exitValue = process.waitFor();
		      process = runtime.exec("java -jar /home/siddharth/PhiBPlot.jar abc.out",null,null);
		   //   exitValue = process.waitFor();
			    
		      System.out.println("Ran successfully");
				

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