package fr.jussieu.pps.keditor.testing;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.editors.text.TextEditor;

public class annotation {

	public static void main(IWorkbenchWindow window)
	{
		IDocument d=((TextEditor) window.getActivePage().getActiveEditor()).getDocumentProvider().getDocument(window.getActivePage().getActiveEditor().getEditorInput());
		try {
			System.out.println(d.getLineOfOffset(19));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
