package fr.jussieu.pps.keditor.ui.editor.outline;

import fr.jussieu.pps.kappa.core.model.KappaBinding;
import fr.jussieu.pps.keditor.ui.KappaUiPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/** Provides the tree viewer with the label and icon for each binding.
 * See SmlBindingsContentProvider. */
public class KappaBindingsLabelProvider implements ILabelProvider {

	/** Maps the type of a binding, i.e. SmlBinding.getType(), to its icon. */
	Map images;
	
	/** Creates a new label provider. */
	public KappaBindingsLabelProvider () {
		images = new HashMap();
	}
	
	/** Returns the icon to display for the given binding. */
	public Image getImage (Object element) {
		String type = ((KappaBinding)element).getType();
		Image image = (Image) images.get(type);
		if (image == null) {
			String url = "/icons/"+type+".gif";
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(KappaUiPlugin.getDefault().getBundle().getEntry(url));
			image = descriptor.createImage();
			images.put(type, image);
		}
		return image;
	}

	/** Returns the label of the given binding. */
	public String getText (Object element) {
		return ((KappaBinding)element).getIdent();
	}

	/** Called when the outline view is being closed.
	 * Disposes all the icons loaded. */
	public void dispose () {
		for (Iterator i = images.values().iterator(); i.hasNext();) {
			Image image = (Image) i.next();
			image.dispose();
		}
	}

	/** Returns whether the label needs to be updated when the given property
	 * of the given object changes. Always returns true, to be safe. */
	public boolean isLabelProperty (Object element, String property) {
		return true;
	}

	/** Required by the interface ILabelProvider, but does nothing. */
	public void addListener (ILabelProviderListener listener) {}
	/** Required by the interface ILabelProvider, but does nothing. */
	public void removeListener (ILabelProviderListener listener) {}

}