package fr.jussieu.pps.keditor.ui.editor;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;
import java.util.List;
import java.util.ArrayList;
import org.eclipse.jface.text.BadLocationException;

public class KappaContentAssistProcessor implements IContentAssistProcessor {

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, 
			   int documentOffset) {

			   // Retrieve current document
			   IDocument doc = viewer.getDocument();

			   // Retrieve current selection range
			   Point selectedRange = viewer.getSelectedRange();
			   List propList = new ArrayList();
			   if (selectedRange.y > 0) {
				     try {

				       // Retrieve selected text
				       String text = doc.get(selectedRange.x, selectedRange.y);

				       // Compute completion proposals
				       computeStyleProposals(text, selectedRange, propList);

				     } catch (BadLocationException e) {

				     }
				   } else {// Retrieve qualifier
					     String qualifier = getQualifier(doc, documentOffset);

					     // Compute completion proposals
					     computeStructureProposals(qualifier, documentOffset, propList);
					   }// Create completion proposal array
			   ICompletionProposal[] proposals = new ICompletionProposal[propList.size()];

			   // and fill with list elements
			   propList.toArray(proposals);

			   // Return the proposals
			   return proposals; 
			}

	private String getQualifier(IDocument doc, int documentOffset) {

	   // Use string buffer to collect characters
	   StringBuffer buf = new StringBuffer();
	   while (true) {
	     try {

	       // Read character backwards
	       char c = doc.getChar(--documentOffset);
	       if (Character.isWhitespace(c))
	           return buf.reverse().toString();
	       buf.append(c);
	       /*  // This was not the start of a tag
	       if (c == '>' || Character.isWhitespace(c))
	          return "";

	       // Collect character
	       buf.append(c);

	       // Start of tag. Return qualifier
	       if (c == '<')
	           return buf.reverse().toString();
*/
	     } catch (BadLocationException e) {

	       // Document start reached, no tag found
	       return buf.reverse().toString();
	     }
	   }
	}
	// Proposal part before cursor
	private final static String[] STRUCTTAGS1 =
	   new String[] { "%agent:", "%init:", "%var:",  "%plot:",  "%mod:","%ref:","%obs:","$DEL","$ADD","$SNAPSHOT","$STOP","[inf]","[sqrt]","[mod]","[int]","[exp]","[tan]","[cos]","[sin]","[log]","[T]","[true]","[false]","[not]","[pi]","[emax]","[tmax]" };

	// Proposal part after cursor

	private void computeStructureProposals(String qualifier, int documentOffset, List propList) { 
	   int qlen = qualifier.length();

	   // Loop through all proposals
	   for (int i = 0; i < STRUCTTAGS1.length; i++) {
	      String startTag = STRUCTTAGS1[i];

	      // Check if proposal matches qualifier
	      if (startTag.startsWith(qualifier)) {

	         // Yes -- compute whole proposal text
	         String text = startTag ;

	         // Derive cursor position
	         int cursor = startTag.length();

	         // Construct proposal
	         CompletionProposal proposal =
	            new CompletionProposal(text, documentOffset - qlen, qlen, cursor);

	         // and add to result list
	         propList.add(proposal);
	      }
	   }
	}

	private final static String[] STYLETAGS = new String[] { 
	      "b", "i", "code", "strong" 
	};
	private final static String[] STYLELABELS = new String[] { 
	      "bold", "italic", "code", "strong" 
	};

	private void computeStyleProposals(String selectedText, Point selectedRange, List propList) {

	   // Loop through all styles
	   for (int i = 0; i < STYLETAGS.length; i++) {
	      String tag = STYLETAGS[i];

	      // Compute replacement text
	      String replacement = "<" + tag + ">" + selectedText + "</" + tag + ">";

	      // Derive cursor position
	      int cursor = tag.length()+2;

	      // Compute a suitable context information
	      IContextInformation contextInfo = 
	         new ContextInformation(null, STYLELABELS[i]+" Style");

	      // Construct proposal
	      CompletionProposal proposal = new CompletionProposal(replacement, 
	         selectedRange.x, selectedRange.y, cursor, null, STYLELABELS[i], 
	         contextInfo, replacement);

	      // and add to result list
	      propList.add(proposal);
	   }
	}
	
	@Override

	public IContextInformation[] computeContextInformation(ITextViewer viewer, 
	      int documentOffset) {

	   // Retrieve selected range
	   Point selectedRange = viewer.getSelectedRange();
	   if (selectedRange.y > 0) {

	      // Text is selected. Create a context information array.
	      ContextInformation[] contextInfos = new ContextInformation[STYLELABELS.length];

	      // Create one context information item for each style
	      for (int i = 0; i < STYLELABELS.length; i++)
	         contextInfos[i] = new ContextInformation(null, STYLELABELS[i]+" Style");
	      return contextInfos;
	   }
	   return new ContextInformation[0];
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		   return new char[] { '%' };
		}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override

	public IContextInformationValidator getContextInformationValidator() {
	   return new ContextInformationValidator(this);
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
