package fr.jussieu.pps.kappa;

import org.osgi.framework.BundleContext;
import fr.jussieu.pps.kappa.core.scan.KappaPartitionScanner;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "fr.jussieu.pps.kappa";

	// The shared instance
	private static Activator plugin;
	

	//Resource bundle.
	private ResourceBundle resourceBundle;

	
	/** Identifier used to access SML-specific document partitioning
	 * (SML comments and strings) computed by a SmlPartitionScanner. */
	public static final String SML_PARTITIONING = "fr.jussieu.pps.kappa.kappaPartitioning";

	
	/**
	 * The constructor
	 */
	public Activator() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("fr.jussieu.pps.kappa.kappaCorePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	/**
	 * Returns the partitioner for the given document. A DefaultPartitioner
	 * using an SmlPartitionScanner will be created and connected to
	 * the document if one is not already registered.
	 */
	/* TODO: This method should go into the SmlDocumentSetupParticipant,
	 * once the lexer is rewritten to not use document partitioning. */
	public static IDocumentPartitioner getPartitioner (IDocument document) {
		IDocumentExtension3 documentExt = (IDocumentExtension3)document;
		IDocumentPartitioner partitioner = documentExt.getDocumentPartitioner(SML_PARTITIONING);
		if (partitioner == null) {
	    	partitioner = new FastPartitioner(new KappaPartitionScanner(), KappaPartitionScanner.CONTENT_TYPES);
	    	documentExt.setDocumentPartitioner(Activator.SML_PARTITIONING, partitioner);
	    	partitioner.connect(document);
		}
		return partitioner;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = Activator.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

}
