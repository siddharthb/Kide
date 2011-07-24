package fr.jussieu.pps.keditor.actions;

import fr.jussieu.pps.keditor.wizards.SimulateWizard;
import fr.jussieu.pps.keditor.testing.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
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
		
	System.out.println("jumbawumba");
    IWorkbenchPage page = window.getActivePage();

    try {

       page.showView(ID); // use the Resource Manager View id to open up view.

    } catch (PartInitException e) {

    }
}

@Override
public void selectionChanged(IAction action, ISelection selection) {
	this.selection = selection;
	
}
}
