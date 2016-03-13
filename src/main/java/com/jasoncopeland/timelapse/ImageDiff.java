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
package com.jasoncopeland.timelapse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.Raster;
import java.io.File;

/**
 * Created by Jason on 3/12/2016.
 */
public class ImageDiff {
    protected BufferedImage imgA;
    protected BufferedImage imgB;

    public ImageDiff(BufferedImage imgA, BufferedImage imgB) {
        this.imgA = imgA;
        this.imgB = imgB;
    }

    public void applyImageOp(BufferedImageOp op) {

        try {
            ImageIO.write(imgA, "png", new File("d:/pers/timelapse/imgANorm.png"));
            imgA = op.filter(imgA, null);
            imgB = op.filter(imgB, null);

            ImageIO.write(imgA, "png", new File("d:/pers/timelapse/imgABlur.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long[] calgulateDiff() {
        long diff[] = new long[] {0, 0, 0};

        for (int w = 0; w < imgA.getWidth(); w++) {
            for (int h = 0; h < imgA.getHeight(); h++) {
                int pixelA = imgA.getRGB(w, h);
                int pixelB = imgB.getRGB(w, h);

                // Red
                diff[0] += Math.abs( ((pixelA & 0xff0000) >> 16) - ((pixelB & 0xff0000) >> 16) );
                // Green
                diff[1] += Math.abs( ((pixelA & 0xff00) >> 8) - ((pixelB & 0xff00) >> 8) );
                // Blue
                diff[2] += Math.abs( (pixelA & 0xff) - (pixelB & 0xff) );
            }
        }

        return diff;
    }
}
