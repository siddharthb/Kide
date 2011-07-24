package fr.jussieu.pps.keditor.ui.editor;

import fr.jussieu.pps.kappa.core.model.IKappaProgramListener;
import fr.jussieu.pps.kappa.core.model.KappaBinding;
import fr.jussieu.pps.kappa.core.model.KappaProgram;
import fr.jussieu.pps.keditor.ui.KappaUiPlugin;
import fr.jussieu.pps.keditor.ui.editor.outline.KappaContentOutlinePage;
import fr.jussieu.pps.keditor.ui.text.KappaBracketMatcher;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.texteditor.TextOperationAction;

/** The Kappa editor. */
public class KappaEditor extends TextEditor implements IKappaProgramListener {
    
	/** The content outline page shown in the Outline view. */
    private KappaContentOutlinePage outlinePage;
    /** The high-level model of the program in the editor. */
	private KappaProgram program;

	/** Called by Eclipse to initialize the editor. Sets the editor to use
	 * the Kappa UI plug-in's preference store, and configures its syntax
	 * highlighting, tab width, etc. See SmlSourceViewerConfiguration. */
	protected void initializeEditor () {
		super.initializeEditor();
		setPreferenceStore(new ChainedPreferenceStore(new IPreferenceStore[] {
				KappaUiPlugin.getDefault().getPreferenceStore(),
				EditorsPlugin.getDefault().getPreferenceStore()}));
        setSourceViewerConfiguration(new KappaSourceViewerConfiguration(this));
        setRangeIndicator(new DefaultRangeIndicator());
        showOverviewRuler();
	}

	/** Returns this editor's outline page if requested. (This is how
	 * Eclipse gets the outline from the editor.) If the request is for
	 * something else, passes it on to the superclass. */
	public Object getAdapter (Class adaptTo) {
        if (adaptTo.equals(IContentOutlinePage.class)) {
            if (outlinePage == null)
            	outlinePage = new KappaContentOutlinePage(this);
            return outlinePage;
        }
        return super.getAdapter(adaptTo);
    }
    
	/** Called by Eclipse when creating the editor. Adds bracket matching. */
	protected void configureSourceViewerDecorationSupport (SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		support.setCharacterPairMatcher(new KappaBracketMatcher());
		support.setMatchingCharacterPainterPreferenceKeys(KappaUiPlugin.KAPPA_BRACKET_MATCHING_ENABLED, KappaUiPlugin.KAPPA_BRACKET_MATCHING_COLOR);
	}
	
	/** Returns the KappaProgram representing the document in this editor. */
	public KappaProgram getProgram () {
		if (program == null) {
			IDocument document = getDocumentProvider().getDocument(getEditorInput());
			program = new KappaProgram(document);
			program.addListener(this);
		}
		return program;
	}

	/** Called when the KappaProgram is updated. Removes existing error markers,
	 * and optionally adds markers for the errors in the updated program. */
	/* We always remove all markers to avoid any markers getting left over
	 * after the user disables error marking. */
	public void programChanged (KappaProgram program) {
		removeErrorMarkers();
		if (true)//KappaUiPlugin.getDefault().getPluginPreferences().getBoolean(KappaUiPlugin.KAPPA_MARK_ERRORS))
			addErrorMarkers(program);
	}
	
	/** Removes all error markers from the editor's document. */
	private void removeErrorMarkers () {
		if (!(getEditorInput() instanceof IFileEditorInput)) return;
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {e.printStackTrace();}
	}
	
	/** Adds markers for all errors in the given program. */
	private void addErrorMarkers (KappaProgram program) {
		if (!(getEditorInput() instanceof IFileEditorInput)) return;
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		IRegion[] problems = program.getProblems();
		String[] msg=program.getErrMsg();
		try {
			for (int i = 0; i < problems.length; i++) {
				IDocument document = getDocumentProvider().getDocument(getEditorInput());
				Map attributes = new HashMap();
				attributes.put(IMarker.MESSAGE, msg[i]);
			//	attributes.put(IMarker.TEXT, msg[i]);
				attributes.put(IMarker.CHAR_START, new Integer(problems[i].getOffset()));
				attributes.put(IMarker.CHAR_END, new Integer(problems[i].getOffset()+problems[i].getLength()));
				attributes.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
				attributes.put(IMarker.TRANSIENT, Boolean.TRUE);
				MarkerUtilities.createMarker(file, attributes, IMarker.PROBLEM);
			}
		} catch (CoreException e) {e.printStackTrace();}
	}

	/** Called when the cursor position changes. Selects the binding at that
	 * position in the outline page. */
	protected void handleCursorPositionChanged () {
		super.handleCursorPositionChanged();
		ISourceViewer sourceViewer = getSourceViewer();
		int offset = widgetOffset2ModelOffset(sourceViewer, sourceViewer.getTextWidget().getCaretOffset());
	/*	KappaBinding binding = program.getBinding(offset);
		if (binding != null)
			outlinePage.select(binding);
		else
			outlinePage.deselect();
	*/}
	private static final String CONTENTASSIST_PROPOSAL_ID = 
		 "com.bdaum.HTMLeditor.ContentAssistProposal"; 

/* (non-Javadoc)
* @see org.eclipse.ui.texteditor.AbstractTextEditor#createActions()
*/
protected void createActions() {
super.createActions();
// This action will fire a CONTENTASSIST_PROPOSALS operation
// when executed
IAction action= new TextOperationAction(KappaUiPlugin.getDefault().getResourceBundle(), "ContentAssistProposal",this,SourceViewer.CONTENTASSIST_PROPOSALS);
action.setActionDefinitionId(CONTENTASSIST_PROPOSAL_ID);
// Tell the editor about this new action
setAction(CONTENTASSIST_PROPOSAL_ID, action);
// Tell the editor to execute this action 
// when Ctrl+Spacebar is pressed
setActionActivationCode(CONTENTASSIST_PROPOSAL_ID,' ', -1, SWT.CTRL);
}
}