package fr.jussieu.pps.keditor.actions;

import fr.jussieu.pps.keditor.wizards.SimulateWizard;
import fr.jussieu.pps.keditor.testing.*;
//import fr.jussieu.pps.keditor.exec.PBDialogDemo;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;



public class compile implements IWorkbenchWindowActionDelegate{

	IWorkbenchWindow window;
	ISelection selection;
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
 //   annotation.main(window);
//	parseCheck.main(window);
//	progressBar.main();
	
	
	if (!(window.getActivePage().getActiveEditor().getEditorInput() instanceof IFileEditorInput)) return;
	IFile file = ((IFileEditorInput) window.getActivePage().getActiveEditor().getEditorInput()).getFile();
		

	
	SimulateWizard wizard = new SimulateWizard(window.getWorkbench(),file.getName(),file.getFullPath().toString());
    WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
    dialog.create();
    dialog.open();
    dialog.close();

}

@Override
public void selectionChanged(IAction action, ISelection selection) {
	this.selection = selection;
	
}
}
