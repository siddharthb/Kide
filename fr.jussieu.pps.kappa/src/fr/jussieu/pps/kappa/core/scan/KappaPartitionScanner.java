package fr.jussieu.pps.kappa.core.scan;

import fr.jussieu.pps.kappa.core.scan.KappaTokenTypes;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.Token;

/**
 * A partition scanner that separates a document into comments, strings, and
 * normal SML code. This is useful for syntax highlighting and bracket matching.
 * Partitioning of a document is done by creating a DefaultPartitioner that
 * uses this scanner to identify the partitions. The partitioner must be
 * connected to the document, and vice versa.
 * See SmlCorePlugin.getPartitioner(). 
 * <p>
 * We can't just use a standard RuleBasedPartitionScanner to partition
 * an SML document. This is because a RuleBasedPartitionScanner uses
 * IPredicateRules, which are expected to be able to find the end
 * of a partition when started from the middle of the partition.
 * This simply isn't possible for nested comments in SML.
 */
public class KappaPartitionScanner extends BufferedRuleBasedScanner implements IPartitionTokenScanner {

	public static final String[] CONTENT_TYPES = new String[] {IDocument.DEFAULT_CONTENT_TYPE, KappaTokenTypes.COMMENT, KappaTokenTypes.LABEL};
	
	/** Creates and initializes a new partition scanner. */
	public KappaPartitionScanner () {
		super();
		IRule commentRule = new KappaCommentRule(new Token(KappaTokenTypes.COMMENT));
		IRule labelRule = new KappaLabelRule(new Token(KappaTokenTypes.LABEL));
	//	IRule charRule = new KappaCharRule(new Token(KappaTokenTypes.CHAR));
		setRules(new IRule[] {commentRule, labelRule});
	}
	
	public void setPartialRange (IDocument document, int offset, int length, String contentType, int partitionOffset) {
		if (partitionOffset > -1 && partitionOffset < offset) setRange(document, partitionOffset, length + offset-partitionOffset);
		else setRange(document, offset, length);
	}
	
}