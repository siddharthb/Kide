package fr.jussieu.pps.keditor.wizards;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import fr.jussieu.pps.keditor.views.MultipleSimulationView;

public class Page1 extends WizardPage implements Listener
{
     Text fromText;
     Text toText;
     Text outFile;
     Text cL;
   //  Button insuranceButton;
 	
     protected Page1(String pageName) {
              super(pageName);
              setTitle("Simulation");
              setDescription("Please enter the simulation information");
     }
     public void createControl(Composite parent) {
 		GridData gd;
		Composite composite =  new Composite(parent, SWT.NULL);

	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 4;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		setControl(composite);
     
      		new Label (composite, SWT.NONE).setText("Events:");				
    		fromText = new Text(composite, SWT.BORDER);
    		gd = new GridData(GridData.FILL_HORIZONTAL);
    		gd.horizontalSpan = ncol - 1;
    		fromText.setLayoutData(gd);
    		
    		// Destination
    		new Label (composite, SWT.NONE).setText("Time:");				
    		toText = new Text(composite, SWT.BORDER);
    		gd = new GridData(GridData.FILL_HORIZONTAL);
    		gd.horizontalSpan = ncol - 1;
    		toText.setLayoutData(gd);

    		createLine(composite, ncol);

    		new Label (composite, SWT.NONE).setText("Output File:");				
    		outFile = new Text(composite, SWT.BORDER);
    		gd = new GridData(GridData.FILL_HORIZONTAL);
    		gd.horizontalSpan = ncol - 1;
    		outFile.setLayoutData(gd);
    		outFile.setText("default.out");

    		new Label (composite, SWT.NONE).setText("Additional Options:");				
    		cL = new Text(composite, SWT.BORDER);
    		gd = new GridData(GridData.FILL_HORIZONTAL);
    		gd.horizontalSpan = ncol - 1;
    		cL.setLayoutData(gd);

  /*  		insuranceButton = new Button(composite, SWT.CHECK);
    		    insuranceButton.setText("Generate data.out file");
    		    gd = new GridData(GridData.FILL_HORIZONTAL);
    		    gd.horizontalSpan = ncol;
    			insuranceButton.setLayoutData(gd);
    			insuranceButton.setSelection(true);
    */		    
    		
    		
    	    // set the composite as the control for this page
    		setControl(composite);		
    		addListeners();
     }
     private void addListeners()
 	{
 		fromText.addListener(SWT.KeyUp, this);
 		toText.addListener(SWT.KeyUp, this);
 		outFile.addListener(SWT.KeyUp, this);		
 		cL.addListener(SWT.KeyUp, this);
 //		insuranceButton.addListener(SWT.Selection, this);
 		SimulateWizard wizard = (SimulateWizard)getWizard();
		IFile file = wizard.container.getFile(new Path(outFile.getText()));
		if (file.exists())
			setErrorMessage("A similar Output file exists. It will be overwritten");
		else
			setErrorMessage(null);
		
 	}
     private void createLine(Composite parent, int ncol) 
 	{
 		Label line = new Label(parent, SWT.SEPARATOR|SWT.HORIZONTAL|SWT.BOLD);
 		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
 		gridData.horizontalSpan = ncol;
 		line.setLayoutData(gridData);
 	}
	@Override
	public void handleEvent(Event event) {
		saveDataToModel();
	//	setErrorMessage("This is an illegal software");
		SimulateWizard wizard = (SimulateWizard)getWizard();
		IFile file = wizard.container.getFile(new Path(outFile.getText()));
		if (file.exists())
			setErrorMessage("A similar Output file exists. It will be overwritten");
		else
			setErrorMessage(null);
		// TODO Auto-generated method stub
		
	}	

	private void saveDataToModel()
	{
		SimulateWizard wizard = (SimulateWizard)getWizard();
		wizard.model.events=fromText.getText();
		wizard.model.time=toText.getText();
		wizard.model.outfile=outFile.getText();
		wizard.model.commandLine=cL.getText();
//		wizard.model.buyInsurance = insuranceButton.getSelection();
	}
}