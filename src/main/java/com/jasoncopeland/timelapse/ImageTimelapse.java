package com.jasoncopeland.timelapse;

import org.jcodec.api.awt.SequenceEncoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jason on 3/12/2016.
 */
public class ImageTimelapse {
    public int frameCount;
    public int newFileOnFrameCount;
    public String location;
    public String name;
    public SequenceEncoder encoder;
    //
    public int timeBetweenFramesInMs;
    protected long lastFrameTime = 0;

    public ImageTimelapse(int newFileOnFrameCount, int realSecPerVideoSec, String location, String name) {
        this.location = location;
        this.name = name;
        this.newFileOnFrameCount = newFileOnFrameCount;
        frameCount = 0;

        // 1:1 = 1 * 1000 / 30 = 33.33ms
        // 10:1 = 10 * 1000 / 30 = 333.33ms
        // 86400 (1 day) / 60 (in 60 seconds) = 1440 real seconds per video seconds
        this.timeBetweenFramesInMs =  realSecPerVideoSec * 1000 / 30;

        createNewFile();
    }

    public boolean needNewImage() {
        return (System.currentTimeMillis() - lastFrameTime >= timeBetweenFramesInMs);
    }

    public void processImage(BufferedImage image) {
        try {
            long startTime = System.currentTimeMillis();
            System.out.print(" into video...");
            lastFrameTime = System.currentTimeMillis();
            encoder.encodeImage(image);

            long remainInMin = (newFileOnFrameCount - frameCount) * timeBetweenFramesInMs / 1000 / 60;
            System.out.println(" in " + (System.currentTimeMillis() - startTime) + " ms for " + name + " frame: " + frameCount + " ms remain: " + remainInMin + " min");
            if (frameCount++ >= newFileOnFrameCount) {
                createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewFile() {
        if (encoder != null) {
            closeStream();
        }
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());

        try {
            String videoPath = (location + "/" + name + "-" + formattedDate + ".mp4").replaceAll("//", "/");
            System.out.println(name + " - New video file: " + videoPath  + " msBetweenFrames: " + timeBetweenFramesInMs);
            encoder = new SequenceEncoder(new File(videoPath));
            frameCount = 0;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void closeStream() {
        try {
            encoder.finish();
            frameCount = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        encoder = null;
    }
}
