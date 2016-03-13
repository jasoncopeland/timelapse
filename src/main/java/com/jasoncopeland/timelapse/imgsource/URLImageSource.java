package com.jasoncopeland.timelapse.imgsource;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jason on 3/12/2016.
 */
public class URLImageSource implements IImageSource {

    protected URL url = null;
    protected String basicAuth = null;


    public URLImageSource(String url, String username, String password) {
        try {
            this.url = new URL(url);

            String userpass = username + ":" + password;
            basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public BufferedImage getCurrentImage() {
        long startTime = System.currentTimeMillis();
        try {
            URLConnection uc = this.url.openConnection();
            uc.setRequestProperty("Authorization", basicAuth);
            return ImageIO.read(uc.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Image fetch time: " + (System.currentTimeMillis() - startTime));
        }

        return null;
    }
}
