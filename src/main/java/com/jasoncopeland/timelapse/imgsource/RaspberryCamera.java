package com.jasoncopeland.timelapse.imgsource;

import java.awt.image.BufferedImage;

/**
 * Created by Jason on 3/13/2016.
 */
public class RaspberryCamera implements IImageSource {

    protected final String raspistillPath = "/opt/vc/bin/raspistill";
    protected final int captureTimeoutInMS = 5000;
    protected final int imgQuality = 100;
    protected int imgWidth = 0;
    protected int imgHeight = 0;
    protected String imageType = "png";

    public BufferedImage getCurrentImage() {
        try
        {
            String fileName = "/tmp/tmlpse-" + System.currentTimeMillis() + ".png";

            StringBuilder sb = new StringBuilder(raspistillPath);

            sb.append(" -n -bm"); // no prview or burst
            sb.append(" -t " + captureTimeoutInMS); // timeout
            if (imgWidth > 0) {
                sb.append(" -w " + imgWidth);
            }
            if (imgHeight > 0) {
                sb.append(" -h " + imgHeight);
            }
            sb.append(" -q " + imgQuality);
            sb.append(" -e " + imageType); // jpg, png, bmp, gif
            sb.append(" -o " + fileName); // destination file path

            Runtime.getRuntime().exec(sb.toString());
            Thread.sleep(captureTimeoutInMS);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
