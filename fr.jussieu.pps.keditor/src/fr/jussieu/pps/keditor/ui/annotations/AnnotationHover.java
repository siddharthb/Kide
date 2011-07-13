package fr.jussieu.pps.keditor.ui.annotations;

import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

public class AnnotationHover implements IAnnotationHover {

	@Override
	public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
		IDocument d=sourceViewer.getDocument();
		for(Iterator i=sourceViewer.getAnnotationModel().getAnnotationIterator();i.hasNext();)
		{
			SimpleMarkerAnnotation s=(SimpleMarkerAnnotation)i.next();
			IMarker m=s.getMarker();
			
			try {
				if(lineNumber ==(d.getLineOfOffset(MarkerUtilities.getCharStart(m))));
				 return MarkerUtilities.getMessage(m)+lineNumber;
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (lineNumber==1) return "syntax error";
		return "syntax error11";
	}

}
