/*************************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 * <p>
 * Copyright 2015-2016 Adobe Systems Incorporated
 * All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package com.jasoncopeland.timelapse.imgsource;


import javax.imageio.ImageIO;
import java.awt.*;
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
