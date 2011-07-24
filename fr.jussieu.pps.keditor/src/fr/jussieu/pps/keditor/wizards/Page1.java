package fr.jussieu.pps.keditor.wizards;
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

public class Page1 extends WizardPage implements Listener
{
     Text fromText;
     Text toText;
     Text cL;
     Button insuranceButton;
 	
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

    		new Label (composite, SWT.NONE).setText("Command Line:");				
    		cL = new Text(composite, SWT.BORDER);
    		gd = new GridData(GridData.FILL_HORIZONTAL);
    		gd.horizontalSpan = ncol - 1;
    		cL.setLayoutData(gd);

    		insuranceButton = new Button(composite, SWT.CHECK);
    		    insuranceButton.setText("Generate data.out file");
    		    gd = new GridData(GridData.FILL_HORIZONTAL);
    		    gd.horizontalSpan = ncol;
    			insuranceButton.setLayoutData(gd);
    			insuranceButton.setSelection(true);
    		    
    		
    		
    	    // set the composite as the control for this page
    		setControl(composite);		
    		addListeners();
     }
     private void addListeners()
 	{
 		fromText.addListener(SWT.KeyUp, this);
 		toText.addListener(SWT.KeyUp, this);
 		cL.addListener(SWT.KeyUp, this);
 		insuranceButton.addListener(SWT.Selection, this);
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
		// TODO Auto-generated method stub
		
	}	

	private void saveDataToModel()
	{
		SimulateWizard wizard = (SimulateWizard)getWizard();
		wizard.model.events=fromText.getText();
		wizard.model.time=toText.getText();
		wizard.model.commandLine=cL.getText();
		wizard.model.buyInsurance = insuranceButton.getSelection();
	}
}