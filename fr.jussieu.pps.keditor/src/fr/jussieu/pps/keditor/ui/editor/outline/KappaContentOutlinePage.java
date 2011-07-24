package fr.jussieu.pps.keditor.ui.editor.outline;

import fr.jussieu.pps.kappa.core.model.IKappaProgramListener;
import fr.jussieu.pps.kappa.core.model.KappaBinding;
import fr.jussieu.pps.kappa.core.model.KappaProgram;
//import in.iitd.mldev.core.parse.ast.Ident;
import fr.jussieu.pps.keditor.ui.editor.KappaEditor;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * The page that is shown in the Outline view. It's only responsible for
 * managing the internal tree viewer, which is what actually displays the
 * tree of the program's bindings. We pass the tree viewer only the SmlProgram
 * as input, and it finds out the tree structure using the
 * SmlBindingsContentProvider and SmlBindingsLabelProvider.
 * <p>
 * See http://www.eclipse.org/articles/treeviewer-cg/TreeViewerArticle.htm
 * for a good explanation of how this works.
 * */
public class KappaContentOutlinePage extends ContentOutlinePage implements IKappaProgramListener {

	/** The editor whose outline is to be displayed. */
	private KappaEditor editor;
	
	/** Creates a new outline page for the given editor. */
    public KappaContentOutlinePage (KappaEditor editor) {
        super();
        this.editor = editor;
        editor.getProgram().addListener(this);
    }
    
    /** Sets up the internal tree viewer to use SML's content and label
     * providers, to inform this page when the user selects a binding, and
     * to display the bindings in the editor's program. */
    public void createControl (Composite parent) {
        super.createControl(parent);
        TreeViewer viewer = getTreeViewer();
        viewer.setAutoExpandLevel(2);
        viewer.setContentProvider(new KappaBindingsContentProvider());
        viewer.setLabelProvider(new KappaBindingsLabelProvider());
        viewer.addSelectionChangedListener(this);
        viewer.setInput(editor.getProgram());
    }
    
    
    /** Refreshes the tree viewer when the program is updated. */
    private void update () {
		TreeViewer viewer = getTreeViewer();
		if (viewer != null) {
			Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
		    	viewer.removeSelectionChangedListener(this);
				control.setRedraw(false);
				viewer.refresh();
				viewer.expandToLevel(2);
				control.setRedraw(true);
		    	viewer.addSelectionChangedListener(this);
			}
		}
    }
    
    /** Called when the program is updated. Updates the outline.*/
    /* This has to be done by passing a runnable to Eclipse's UI thread,
     * because other threads aren't allowed to mess with the UI. */
    public void programChanged (KappaProgram program) {
    	if (!getControl().isDisposed()) {
    		getControl().getDisplay().asyncExec(new Runnable () {
				public void run () {
					update();
				}
    		});
    	}
    }
    
    /** When the user selects a binding in the tree, selects the 
     * corresponding identifier in the editor. */
	public void selectionChanged (SelectionChangedEvent event) {
		super.selectionChanged(event);
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			if (selection.size() != 1) return;
			 int left = ((KappaBinding) selection.getFirstElement()).getLeft();

			 int right = ((KappaBinding) selection.getFirstElement()).getRight();
			editor.selectAndReveal(left, right - left);
		}
	}
    
	/** Selects the given binding in the tree.*/
	/* Prevents selection change events so that seelctionChanged() doesn't
	 * get called, which would create an infinite loop of events. */
    public void select (KappaBinding binding) {
    	TreeViewer viewer = getTreeViewer();
    	viewer.removeSelectionChangedListener(this);
    	viewer.setSelection(new StructuredSelection(binding), true);
    	viewer.addSelectionChangedListener(this);
    }
    
    /** Deselects everything selected in the tree. */
    public void deselect () {
    	getTreeViewer().setSelection(new StructuredSelection());
    }

}