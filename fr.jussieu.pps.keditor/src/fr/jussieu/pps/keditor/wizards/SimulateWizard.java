package fr.jussieu.pps.keditor.wizards;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import fr.jussieu.pps.keditor.exec.spawn;

public class SimulateWizard extends Wizard {

	Page1 page1;
	IContainer container;
	IWorkbench workbench;
	SimulateModel model;
	String name;
	String path;
	int j=0;
	public SimulateWizard(IWorkbench workbench,String name,String path) {
		super();
		this.workbench=workbench;
		this.name=name;
		this.path=path;
		model = new SimulateModel();
		
     	  this.container = (IContainer)ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path.substring(0, path.lastIndexOf('/')+1)));

	}
	public SimulateWizard(IWorkbench workbench,String path,IContainer cnt) {
		super();
		this.workbench=workbench;
		this.path=path;
		this.container=cnt;
		model = new SimulateModel();
		j=1;
	}
	public boolean canFinish()
	{
			return model.buyInsurance;
	}
	
	public boolean performFinish() 
	{
		if(j==0)
		{
		String summary = model.toString();
		File f = new File("");
		String path1=Platform.getLocation().toString();
		//String path1=f.getAbsolutePath();
		String path2=path1+path;
		path2=path2.replaceAll(" ","\\ ");
		if(model.events.compareTo("")!=0)
			model.commandLine=model.commandLine+" -e "+model.events;
		if(model.time.compareTo("")!=0)
			model.commandLine=model.commandLine+" -t "+model.time;
		if(model.outfile.compareTo("")!=0)
			model.commandLine=model.commandLine+" -o "+model.outfile;
		
		MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(),"Options",model.commandLine);
//		System.out.println((model.events)+"hello"+(model.time));
		spawn.main("-i "+path2,workbench.getActiveWorkbenchWindow(),model.commandLine,path2.substring(0,path2.lastIndexOf('/')+1)+model.outfile);
		}
		else if (j==1)
		{
			if(model.events.compareTo("")!=0)
				model.commandLine=model.commandLine+" -e "+model.events;
			if(model.time.compareTo("")!=0)
				model.commandLine=model.commandLine+" -t "+model.time;
			if(model.outfile.compareTo("")!=0)
				model.commandLine=model.commandLine+" -o "+model.outfile;
			
			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(),"Options",model.commandLine);
			spawn.main(path,workbench.getActiveWorkbenchWindow(),model.commandLine,path.substring(path.lastIndexOf(' '),path.lastIndexOf('/')+1)+model.outfile);
		}

		System.out.println("I reached here");
		return true;
		
		}
	
	public void addPages()
	{
		page1 = new Page1("");
		addPage(page1);
	}
	
}
