package fr.jussieu.pps.keditor.ui.preferences;

import fr.jussieu.pps.keditor.ui.KappaUiPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/** The main SML page in the Preferences window. */
public class KappaMainPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/** Creates an SML preference page and sets it to save preferences in
	 * the SML UI plug-in's preference store. */
	public KappaMainPreferencePage () {
		super(GRID);
		setPreferenceStore(KappaUiPlugin.getDefault().getPreferenceStore());
	}
	
	/** Creates the page's fields. Since this class extends
	 * FieldEditorPreferencePage, all we have to do is create the field
	 * editors for all the required fields, and everything else will take
	 * care of itself. */
	protected void createFieldEditors () {
		addField(new ColorFieldEditor(KappaUiPlugin.KAPPA_KEYWORD_COLOR, "Keyword colour:", getFieldEditorParent()));
		addField(new ColorFieldEditor(KappaUiPlugin.KAPPA_STRING_COLOR, "String colour:", getFieldEditorParent()));
		addField(new ColorFieldEditor(KappaUiPlugin.KAPPA_COMMENT_COLOR, "Comment colour:", getFieldEditorParent()));
		addField(new IntegerFieldEditor(KappaUiPlugin.KAPPA_TAB_WIDTH, "Tab width:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(KappaUiPlugin.KAPPA_MARK_ERRORS, "Mark syntax errors in editor", getFieldEditorParent()));
	}

	/** Nothing to initialize. */
	public void init (IWorkbench workbench) {}

}