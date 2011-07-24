package fr.jussieu.pps.keditor.ui.editor;

import fr.jussieu.pps.kappa.Activator;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;

/** Sets up an SML document when an editor is opened. */
public class KappaDocumentSetupParticipant implements IDocumentSetupParticipant {

	/** Sets up the document by adding an SML partitioner. */
    public void setup (IDocument document) {
    	Activator.getPartitioner(document);
    }

}