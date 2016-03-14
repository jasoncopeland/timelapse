package com.jasoncopeland.timelapse.imgsource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Jason on 3/13/2016.
 */
public class RaspberryCamera implements IImageSource {

    protected final String raspistillPath = "/opt/vc/bin/raspistill";
    protected final int captureTimeoutInMS = 10000;
    protected final int imgQuality = 100;
    protected int imgWidth = 800;
    protected int imgHeight = 600;
    protected String imageType = "png";

    public BufferedImage getCurrentImage() {
        long startTime = System.currentTimeMillis();
        String fileName = "/tmp/tmlpse-" + System.currentTimeMillis() + ".png";
        try {

            StringBuilder sb = new StringBuilder();

            if (imgWidth > 0) {
                sb.append(" -w " + imgWidth);
            }
            if (imgHeight > 0) {
                sb.append(" -h " + imgHeight);
            }

            String params = sb.toString();
            System.out.println("Capture params: " + params);
            ProcessBuilder pb = new ProcessBuilder(raspistillPath, params, "-n", "-bm", "-t", Integer.toString(captureTimeoutInMS), "-e", imageType, "-q", Integer.toString(imgQuality), "-o", fileName);
            Process process = pb.start();
            int exitCode = 0;
            if (process != null && (exitCode = process.waitFor()) == 0) {
                String error = readInputStreamAsString(process.getErrorStream());
                String output = readInputStreamAsString(process.getErrorStream());
                if (error.length() > 0 || output.length() > 0) {
                    System.out.println("capture cmd output: \"" + output + "\" error: \"" + error + "\"");
                }
                File targetFile = new File(fileName);
                BufferedImage buffImg = ImageIO.read(targetFile);
                targetFile.delete();

                buffImg.flush();
                return buffImg;
            } else {
                System.out.println("Failed to create process exit code: " + exitCode);
            }
            process.destroy();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Total pi camera capture time: " + (System.currentTimeMillis() - startTime) + "ms");
        }
        return null;
    }

    public static String readInputStreamAsString(InputStream in)
            throws IOException {

        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            byte b = (byte)result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }
}
