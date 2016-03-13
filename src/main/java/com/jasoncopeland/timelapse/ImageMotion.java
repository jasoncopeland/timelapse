package com.jasoncopeland.timelapse;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Created by Jason on 3/12/2016.
 */
public class ImageMotion extends ImageTimelapse {
    BufferedImage prevImage = null;

    public ImageMotion(int newFileOnFrameCount, int realSecPerVideoSec, String location, String name) {
        super(newFileOnFrameCount, realSecPerVideoSec, location, name);
    }

    private long lastDiff = 0;
    @Override
    public void processImage(BufferedImage image) {
        long diff[] = null;
        if (prevImage != null) {
            ImageDiff imageDiff = new ImageDiff(prevImage, image);

            int matrixDim = 13;
            float divBy = matrixDim * matrixDim;
            float[] matrix = new float[matrixDim * matrixDim];
            for (int i = 0; i < matrixDim * matrixDim; i++)
                matrix[i] = 1.0f/divBy;

            Kernel kernel = new Kernel(matrixDim, matrixDim, matrix);
            BufferedImageOp op = new ConvolveOp(kernel);
            imageDiff.applyImageOp(op);

            diff = imageDiff.calgulateDiff();

            long timeSinceLastProcess = System.currentTimeMillis() - lastDiff;
            System.out.println("Diff: " + diff[0] + "," + diff[1] + "," + diff[2] + " total: " + (diff[0] + diff[1] + diff[2]) + " Since Last: " + timeSinceLastProcess);
            lastDiff = System.currentTimeMillis();
        }
        prevImage = image;

        if (diff != null) {
            long diffTotal = diff[0] + diff[1] + diff[2];
            if (diffTotal > 1000000) {
                super.processImage(image);
            }
        }
    }
}
