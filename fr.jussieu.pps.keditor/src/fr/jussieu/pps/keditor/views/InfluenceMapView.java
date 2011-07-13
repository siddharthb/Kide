package fr.jussieu.pps.keditor.views;

import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.ViewPart;

import fr.jussieu.pps.keditor.ui.KappaUiPlugin;



/**
 * This ImageView class shows how to use SWTImageCanvas to 
 * manipulate images. 
 * <p>
 * To facilitate the usage, you should setFocus to the canvas
 * at the beginning, and call the dispose at the end.
 * <p>
 * @author Chengdong Li: cli4@uky.edu
 * @see uky.article.imageviewer.SWTImageCanvas
 */

public class InfluenceMapView extends ViewPart {
	public SWTImageCanvas imageCanvas;
	
	/**
	 * The constructor.
	 */
	public InfluenceMapView() {
	}
	
	/**
	 * Create the GUI.
	 * @param frame The Composite handle of parent
	 */
	public void createPartControl(Composite frame) {
		IFile f=((IFileEditorInput)getViewSite().getPage().getActiveEditor().getEditorInput()).getFile();
		String path1=Platform.getLocation().toString();
		String path2=path1+f.getFullPath().toString();
		path2=path2.replaceAll(" ","\\ ");
		String pathnew=path2.substring(0,path2.lastIndexOf("/")+1);
//		System.out.println(pathnew);
		 Process process=null;
			Runtime runtime = Runtime.getRuntime();
			try {
				// execute the command
				process = runtime.exec("rm -rf "+pathnew+"map.gif",null,null);
				process.waitFor();
				process = runtime.exec("rm -rf map.dot",null,null);
				process.waitFor();
				
				
				File f1 = new File("");
				String s=(KappaUiPlugin.getDefault().getBundle().getLocation());
				System.out.println(path2);
				process = runtime.exec(s.substring(s.lastIndexOf(':')+1)+"lib/./KaSim -i " + path2 + " -im map.dot -e 0",null,new File(f1.getAbsolutePath()));

				// get the standard and error outputs
	
				// start the two threads that will get the outputs from stderr and stdout
				
				// wait for the process to end
				process.waitFor();
			      process = runtime.exec("dot -T gif map.dot -o "+pathnew+"map.gif",null,null);
			      process.waitFor();				    
			      System.out.println("Ran successfully");
			} catch (Exception e) {
			System.out.println("Error in Running");
			}

		imageCanvas=new SWTImageCanvas(frame);
		imageCanvas.onFileOpen(pathnew);		
	}

	/**
	 * Called when we must grab focus.
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus() {
		imageCanvas.setFocus();
	}

	/**
	 * Called when the View is to be disposed
	 */
	public void dispose() {
		imageCanvas.dispose();
		super.dispose();
	}

}