package fr.jussieu.pps.keditor.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

/** Updates the SmlProgram representing a document open in the workspace,
 * whenever the document is changed. */
public class KappaReconcilingStrategy implements IReconcilingStrategy {

	/** The editor whose document this instance maintains. */
	private KappaEditor editor;

	/** Creates a reconciling strategy to update the given editor's model
	 * when the document changes. It should only be used with this editor. */
	public KappaReconcilingStrategy (KappaEditor editor) {
		this.editor = editor;
	}
	
	/** Tells this reconciling strategy on which document it will work.
	 * However, since the containing editor has already been given in the
	 * constructor, this method doesn't do anything. */
	public void setDocument (IDocument document) {}

	public void reconcile (DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile();
	}

	public void reconcile (IRegion partition) {
		reconcile();
	}
	
	/** Blindly updates the entire program. */
	private void reconcile () {
		editor.getProgram().update();
	}

}