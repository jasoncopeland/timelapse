package com.jasoncopeland.timelapse;

import com.jasoncopeland.timelapse.imgsource.IImageSource;
import com.jasoncopeland.timelapse.imgsource.RaspberryCamera;
import com.jasoncopeland.timelapse.imgsource.URLImageSource;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

public class TimeLapseRecorder {

    public static void main(String[] args) {
        final List<ImageTimelapse> timeLapses = new ArrayList<ImageTimelapse>();

        // args[0] == time lapse output folder
        // args[1] == path source
        // args[2] == realSecBetweenVidSec
        // args[3] == dump every X frames
        // args[4] == username
        // args[5] == password

        if (args.length < 4) {
            System.out.println("Usage: java -jar [path to jar] \"[timelapse output folder]\" \"[pi|http...]\" [real seconds between video seconds] [new file every n frames] [username] [password]");
        }
        // new file on X frame, location, name
//        timeLapses.add(new ImageTimelapse(450, 1440, args[0], "1440s"));
        timeLapses.add(new ImageTimelapse(Integer.parseInt(args[3]), Integer.parseInt(args[2]), args[0], args[2]));

        IImageSource imageSource = null;
        if (args[1].equals("pi")) {
            imageSource = new RaspberryCamera(Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[4]);
        } else if (args[1].startsWith("http")) {
            imageSource = new URLImageSource(args[1], args[2], args[3]);
        } else {
            System.out.println("Unknown image source: " + args[1]);
        }

        if (!(imageSource instanceof RaspberryCamera)) {
            System.out.println("Initializing HTTP server...");

            Undertow server = Undertow.builder()
                    .addHttpListener(8080, "localhost")
                    .setHandler(new HttpHandler() {

                        public void handleRequest(final HttpServerExchange exchange) throws Exception {
                            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                            exchange.getResponseSender().send("test");
                        }
                    }).build();
            server.start();
            System.out.println("Done");
        }

        while (true) {
            try {
                boolean flushFile = false;
                if (System.in.available() > 0) {
                    int ch = System.in.read();
                    if (ch == '1') {
                        System.out.println("Flushing current recordings.");
                        flushFile = true;
                    }
                }
                BufferedImage img = null;
                for (ImageTimelapse tl : timeLapses) {
                    if (flushFile) {
                        tl.createNewFile();
                    }
                    // We found one that needs a fresh image!
                    if (tl.needNewImage()) {
                        // If one hasn't been captured for this segment, capture one
                        if (img == null) {
                            img = imageSource.getCurrentImage();
                        }
                        // write it to the stream
			if (img != null) {
	                        tl.processImage(img);
			}
                    }
                }
                if (img != null) {
                    img.flush();
                }
                Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
