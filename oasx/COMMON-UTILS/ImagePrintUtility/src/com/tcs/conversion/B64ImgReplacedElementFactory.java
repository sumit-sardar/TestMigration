package com.tcs.conversion;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.tcs.Main;



public class B64ImgReplacedElementFactory implements ReplacedElementFactory {

	private static Logger slf4jLogger = LoggerFactory.getLogger(B64ImgReplacedElementFactory.class);
	
	public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
		Element e = box.getElement();
		if (e == null) {
			return null;
		}
		String nodeName = e.getNodeName();
		if (nodeName.equals("img")) {
			String attribute = e.getAttribute("src");
			FSImage fsImage;
			try {
				fsImage = buildImage(attribute, uac);
			} catch (BadElementException e1) {
				fsImage = null;
			} catch (IOException e1) {
				fsImage = null;
			}
			if (fsImage != null) {
				slf4jLogger.info("Image CSSWidth :: " + cssWidth + ", Image CSSHeight :: " + cssHeight);
				if (cssWidth != -1 || cssHeight != -1) {
					fsImage.scale(cssWidth, cssHeight);
				}
				return new ITextImageElement(fsImage);
			}
		}

		return null;
	}

	protected FSImage buildImage(String srcAttr, UserAgentCallback uac) throws IOException, BadElementException {
		FSImage fsImage;
		if (srcAttr.startsWith("data:image/")) {
			String b64encoded = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(), srcAttr.length());
//			BASE64Decoder decoder = new BASE64Decoder();
//			byte[] decodedBytes = decoder.decodeBuffer(b64encoded);
//			byte[] decodedBytes = B64Decoder.decode(b64encoded);
			byte[] decodedBytes = Base64.decode(b64encoded);

			fsImage = new ITextFSImage(Image.getInstance(decodedBytes));
		} else {
			
			fsImage = uac.getImageResource(srcAttr).getImage();
//			slf4jLogger.info("Image CSSWidth :: " + cssWidth + ", Image CSSHeight :: " + cssHeight);
//			Image image = ((ITextFSImage) fsImage).getImage();
//			float imageActualWidth = image.getWidth();
//			float imageActualHeight = image.getHeight();
//			slf4jLogger.info("Image Width :: " + imageActualWidth + ", Image Height :: " + imageActualHeight);
//			float scalingWidthFactor = 500/imageActualWidth;
//			float scalingHeightFactor = 500/imageActualHeight;
//			slf4jLogger.info("Scaling factor for Width :: " + scalingWidthFactor + ", for Height :: " + scalingHeightFactor);
//			if(imageActualWidth > 500)
//				imageActualWidth = (imageActualWidth * scalingWidthFactor);
//			if(imageActualHeight > 500)
//				imageActualHeight = (imageActualHeight * scalingHeightFactor);
//			slf4jLogger.info("Final Image Width :: " + imageActualWidth + ", Image Height :: " + imageActualHeight);
//			fsImage.scale((int)imageActualWidth, (int)imageActualHeight);
			
		}
		return fsImage;
	}

	public void remove(Element e) {
	}

	public void reset() {
	}

	@Override	
	public void setFormSubmissionListener(FormSubmissionListener arg0) {
		// TODO Auto-generated method stub
		
	}
}