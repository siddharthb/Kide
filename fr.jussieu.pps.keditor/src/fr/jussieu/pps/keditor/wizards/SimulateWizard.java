package fr.jussieu.pps.keditor.wizards;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.Platform;
import fr.jussieu.pps.keditor.exec.spawn;

public class SimulateWizard extends Wizard {

	Page1 page1;
	
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
	}
	public SimulateWizard(IWorkbench workbench,String path) {
		super();
		this.workbench=workbench;
		this.path=path;
		model = new SimulateModel();
		j=1;
	}
	public boolean canFinish()
	{
			return true;
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
		MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
			"File info",model.commandLine);
		System.out.println((model.events)+"hello"+(model.time));
		spawn.main("-i "+path2,workbench.getActiveWorkbenchWindow(),model.commandLine);
		}
		else if (j==1)
		{
			MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
					"File info","Multiple Files Spawned");
				spawn.main(path,workbench.getActiveWorkbenchWindow(),model.commandLine);
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
