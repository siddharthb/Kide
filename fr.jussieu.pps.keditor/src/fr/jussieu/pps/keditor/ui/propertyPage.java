package fr.jussieu.pps.keditor.ui;
  import org.eclipse.core.resources.IResource;
  import org.eclipse.core.runtime.CoreException;
  import org.eclipse.core.runtime.QualifiedName;
  import org.eclipse.swt.SWT;
  import org.eclipse.swt.layout.GridData;
  import org.eclipse.swt.layout.GridLayout;
  import org.eclipse.swt.widgets.Composite;
  import org.eclipse.swt.widgets.Control;
 import org.eclipse.swt.widgets.Label;
 import org.eclipse.swt.widgets.Text;
 import org.eclipse.ui.IWorkbenchPropertyPage;
 import org.eclipse.ui.dialogs.PropertyPage;
 import fr.jussieu.pps.keditor.views.MultipleSimulationView.TreeObject;
 public class propertyPage extends PropertyPage implements
                    IWorkbenchPropertyPage {
          private Text textField;
          public static QualifiedName  AUTHOR_PROP_KEY = new QualifiedName("Author", "Author");
          public propertyPage() {
                    super();
          }
          protected Control createContents(Composite parent) {
                       Composite myComposite = new Composite(parent, SWT.NONE);
                       GridLayout mylayout = new GridLayout();
                       mylayout.marginHeight = 1;
                       mylayout.marginWidth = 1;
                       myComposite.setLayout(mylayout);
                       Label mylabel = new Label(myComposite, SWT.NONE);
                       mylabel.setLayoutData(new GridData());
                       mylabel.setText("Author");
                       textField = new Text(myComposite, SWT.BORDER);
                       textField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                       textField.setText(getAuthor());
                      return myComposite;
          }
          protected String getAuthor() {
                       IResource resource =
                          ((TreeObject) getElement()).getResouce();
                       try {
                          String value =
                             resource.getPersistentProperty(
                                AUTHOR_PROP_KEY);
                          if (value == null)
                             return "";
                          return value;
                        }
                        catch (CoreException e) {
                           return e.getMessage();
                        }
                    }
          protected void setAuthor(String author) {
                       IResource resource =
                                   ((TreeObject) getElement()).getResouce();
                       String value = author;
                       if (value.equals(""))
                          value = null;
                       try {
                                   resource.setPersistentProperty(
                                         AUTHOR_PROP_KEY,
                             value);
                       }
                       catch (CoreException e) {
                       }
                    }
          public boolean performOk() {
                       setAuthor(textField.getText());
                       return super.performOk();
                    }
}