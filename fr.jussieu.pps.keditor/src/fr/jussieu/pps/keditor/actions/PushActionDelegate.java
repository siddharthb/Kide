package fr.jussieu.pps.keditor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import fr.jussieu.pps.keditor.views.InfluenceMapView;
import fr.jussieu.pps.keditor.views.SWTImageCanvas;

/**
 * Action delegate for all toolbar push-buttons.
 * <p>
 * @author Chengdong Li: cli4@uky.edu
 */
public class PushActionDelegate implements IViewActionDelegate {
	/** pointer to image view */	
	public InfluenceMapView view=null;
	/** Action id of this delegate */
	public String id;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart viewPart) {
		if(viewPart instanceof InfluenceMapView){
			this.view=(InfluenceMapView)viewPart;
			
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		String id=action.getId();
		SWTImageCanvas imageCanvas=view.imageCanvas;
	/*	if(id.equals("toolbar.open")){
			imageCanvas.onFileOpen();
			return;
		}*/
		if(imageCanvas.getSourceImage()==null) return;
		if(id.equals("toolbar.zoomin")){
			imageCanvas.zoomIn();
			return;
		}else if(id.equals("toolbar.zoomout")){
			imageCanvas.zoomOut();
			return;
		}else if(id.equals("toolbar.fit")){
			imageCanvas.fitCanvas();
			return;
		}else if(id.equals("toolbar.rotate")){
			/* rotate image anti-clockwise */
			ImageData src=imageCanvas.getImageData();
			if(src==null) return;
			PaletteData srcPal=src.palette;
			PaletteData destPal;
			ImageData dest;
			/* construct a new ImageData */
			if(srcPal.isDirect){
				destPal=new PaletteData(srcPal.redMask,srcPal.greenMask,srcPal.blueMask);
			}else{
				destPal=new PaletteData(srcPal.getRGBs());
			}
			dest=new ImageData(src.height,src.width,src.depth,destPal);
			/* rotate by rearranging the pixels */
			for(int i=0;i<src.width;i++){
				for(int j=0;j<src.height;j++){
					int pixel=src.getPixel(i,j);
					dest.setPixel(j,src.width-1-i,pixel);
				}
			}
			imageCanvas.setImageData(dest);
			return;
		}else if(id.equals("toolbar.original")){
			imageCanvas.showOriginal();
			return;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}

}
