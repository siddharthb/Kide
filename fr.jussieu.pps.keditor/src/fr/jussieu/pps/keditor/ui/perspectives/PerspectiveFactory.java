package fr.jussieu.pps.keditor.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory {

		 private static final String VIEW_ID =
		        "fr.jussieu.pps.keditor.kappaview";
		 private static final String VIEW_ID1 =
		        "fr.jussieu.pps.keditor.binaryview";


		  private static final String BOTTOM = "bottom";

		     public void createInitialLayout(IPageLayout myLayout) {

		      myLayout.addView(IPageLayout.ID_OUTLINE,IPageLayout.RIGHT,0.70f,
		      						myLayout.getEditorArea());
		      
		      myLayout.addView(IPageLayout.ID_PROJECT_EXPLORER,IPageLayout.LEFT,0.40f,
						myLayout.getEditorArea());

		      IFolderLayout bot = myLayout.createFolder(BOTTOM,IPageLayout.BOTTOM,0.76f,
		      						myLayout.getEditorArea());
		      bot.addView(VIEW_ID);
		      bot.addView(VIEW_ID1);
	}

}
