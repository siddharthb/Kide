package fr.jussieu.pps.keditor.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import fr.jussieu.pps.keditor.wizards.KappaNewWizard;
import fr.jussieu.pps.keditor.testing.*;
import fr.jussieu.pps.keditor.ui.KappaUiPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;



public class NewFile implements IWorkbenchWindowActionDelegate{

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
	if (!(window.getActivePage().getActiveEditor().getEditorInput() instanceof IFileEditorInput)) return;
	IFile file = ((IFileEditorInput) window.getActivePage().getActiveEditor().getEditorInput()).getFile();
		

	
	KappaNewWizard wizard = new KappaNewWizard();
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