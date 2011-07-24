package fr.jussieu.pps.keditor.ui.editor;


import fr.jussieu.pps.kappa.Activator;
import fr.jussieu.pps.kappa.core.scan.KappaPartitionScanner;
import fr.jussieu.pps.kappa.core.scan.KappaTokenTypes;
import fr.jussieu.pps.keditor.ui.KappaUiPlugin;
import fr.jussieu.pps.keditor.ui.text.SingleTokenScanner;
import fr.jussieu.pps.keditor.ui.text.KappaTextStyleScanner;
import fr.jussieu.pps.keditor.ui.annotations.AnnotationHover;

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;

/** Bundles the configuration of an SmlEditor. Sets up its default document
 * partitioning, syntax highlighting, tab width, and the reconciler that
 * updates the program model. Except for the constructor, all the methods are
 * called only by Eclipse to determine the configuration of the editor. */
public class KappaSourceViewerConfiguration extends SourceViewerConfiguration {

	/** The editor that this instance configures. */
	private KappaEditor editor;

	/** Creates a source viewer configuration to configure the given editor.
	 * It should only be used with this editor. */
	public KappaSourceViewerConfiguration (KappaEditor editor) {
		this.editor = editor;
	}
	private ColorManager colorManager;

	public KappaSourceViewerConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		// Create content assistant
		   ContentAssistant assistant = new ContentAssistant();
		   
		   // Create content assistant processor
		   IContentAssistProcessor processor = new KappaContentAssistProcessor();
		   
		   // Set this processor for each supported content type
		   assistant.setContentAssistProcessor(processor, KappaTokenTypes.LABEL);
//		   assistant.setContentAssistProcessor(processor, XMLPartitionScanner.XML_DEFAULT);
		   assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		   assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		   assistant.enableAutoActivation(true);
		   assistant.setAutoActivationDelay(500);
		//   Color bgColor = colorManager.getColor(new RGB(230,255,230));
		//   assistant.setProposalSelectorBackground(bgColor);
		   // Return the content assistant   
		   return assistant;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getInformationControlCreator(org.eclipse.jface.text.source.ISourceViewer)
	 */
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		IAutoEditStrategy strategy= (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) 
		        ? new MyAutoEditStrategy() : new DefaultIndentLineAutoEditStrategy());
		        return new IAutoEditStrategy[] { strategy };
		        }
	private static final DefaultInformationControl.IInformationPresenter
	   presenter = new DefaultInformationControl.IInformationPresenter() {
	      public String updatePresentation(Display display, String infoText,
	         TextPresentation presentation, int maxWidth, int maxHeight) {
	         int start = -1;

	         // Loop over all characters of information text
	         for (int i = 0; i < infoText.length(); i++) {
	            switch (infoText.charAt(i)) {
	               case '<' :

	                  // Remember start of tag
	                  start = i;
	                  break;
	               case '>' :
	                  if (start >= 0) {

	                    // We have found a tag and create a new style range
	                    StyleRange range = 
	                       new StyleRange(start, i - start + 1, null, null, SWT.BOLD);

	                    // Add this style range to the presentation
	                    presentation.addStyleRange(range);

	                    // Reset tag start indicator
	                    start = -1;
	                  }
	                  break;
	         }
	      }

	      // Return the information text

	      return infoText;
	   }
	};
	
	@Override
	public IInformationControlCreator getInformationControlCreator
    (ISourceViewer sourceViewer) {
 return new IInformationControlCreator() {
    public IInformationControl createInformationControl(Shell parent) {
       return new DefaultInformationControl(parent,presenter);
    }	
 };
}

	/** Returns the content types to be configured in an SML document.
	 * See SmlPartitionScanner. */
	public String[] getConfiguredContentTypes (ISourceViewer sourceViewer) {
		return KappaPartitionScanner.CONTENT_TYPES;
	}

	/** Returns the default partitioning for an SML document.
	 * See SmlPartitionScanner. */
	public String getConfiguredDocumentPartitioning (ISourceViewer sourceViewer) {
		return Activator.SML_PARTITIONING;
	}
	
	/** Returns a presentation reconciler for performing syntax highlighting. */
	public IPresentationReconciler getPresentationReconciler (ISourceViewer viewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(viewer));
		
		KappaTextStyleProvider styleProvider = new KappaTextStyleProvider();
        
		DefaultDamagerRepairer damageRepairer;
		damageRepairer = new DefaultDamagerRepairer(new KappaTextStyleScanner(styleProvider.getKeywordStyle()));
		reconciler.setDamager(damageRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(damageRepairer, IDocument.DEFAULT_CONTENT_TYPE);
        
		damageRepairer = new DefaultDamagerRepairer(new SingleTokenScanner(styleProvider.getCommentStyle()));
		reconciler.setDamager(damageRepairer, KappaTokenTypes.COMMENT);
		reconciler.setRepairer(damageRepairer, KappaTokenTypes.COMMENT);
        
		damageRepairer = new DefaultDamagerRepairer(new SingleTokenScanner(styleProvider.getStringStyle()));
		reconciler.setDamager(damageRepairer, KappaTokenTypes.LABEL);
		reconciler.setRepairer(damageRepairer, KappaTokenTypes.LABEL);
        /*
		damageRepairer = new DefaultDamagerRepairer(new SingleTokenScanner(styleProvider.getStringStyle()));
		reconciler.setDamager(damageRepairer, KappaTokenTypes.CHAR);
		reconciler.setRepairer(damageRepairer, KappaTokenTypes.CHAR);
        */
		return reconciler;
    }
    
	/** Returns the width that a tab character should be displayed with. */
    public int getTabWidth (ISourceViewer viewer) {
    	return KappaUiPlugin.getDefault().getPluginPreferences().getInt(KappaUiPlugin.KAPPA_TAB_WIDTH);
    }
    
    /** Returns the reconciler that will updates the program model
     * in response to changes in the editor's document. */
    public IReconciler getReconciler (ISourceViewer viewer) {
    	MonoReconciler reconciler = new MonoReconciler(new KappaReconcilingStrategy(editor), false);
    	return reconciler;
    }
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAnnotationHover(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		// TODO Auto-generated method stub
		return new AnnotationHover();
	}

}