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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * Created by Jason on 3/12/2016.
 */
public class MJPEGParser {
    /**
     * @param args
     */
    public static void main(String[] args) {
        MJPEGParser mp = new MJPEGParser(args[0], args[1], args[2]);
    }

    public MJPEGParser(String mjpeg_url)
    {
        this(mjpeg_url,null,null);
    }

    public MJPEGParser(String mjpeg_url, String username, String password)
    {
        int imageCount = 0;

        try {
            if (username != null && password != null)
            {
                Authenticator.setDefault(new HTTPAuthenticator(username, password));
            }

            URL url = new URL(mjpeg_url);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            int lineCount = 0;
            boolean lineCountStart = false;
            boolean saveImage = false;
            while ((inputLine = in.readLine()) != null) {
                // Should be checking just for "--" probably
                if (inputLine.lastIndexOf("--videoboundary") > -1)
                {
                    // Got an image boundary, stop last image
                    // Start counting lines to get past:
                    // Content-Type: image/jpeg
                    // Content-Length: 22517

                    saveImage = false;
                    lineCountStart = true;

                    System.out.println("Got a new boundary");
                    System.out.println(inputLine);
                }
                else if (lineCountStart)
                {
                    lineCount++;
                    if (lineCount >= 2)
                    {
                        lineCount = 0;
                        lineCountStart = false;
                        imageCount++;
                        saveImage = true;
                        System.out.println("Starting a new image");

                    }
                }
                else if (saveImage)
                {
                    System.out.println("Saving an image line");
                }
                else {

                    System.out.println("What's this:");
                    System.out.println(inputLine);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    static class HTTPAuthenticator extends Authenticator {
        private String username, password;

        public HTTPAuthenticator(String user, String pass) {
            username = user;
            password = pass;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            System.out.println("Requesting Host  : " + getRequestingHost());
            System.out.println("Requesting Port  : " + getRequestingPort());
            System.out.println("Requesting Prompt : " + getRequestingPrompt());
            System.out.println("Requesting Protocol: "
                    + getRequestingProtocol());
            System.out.println("Requesting Scheme : " + getRequestingScheme());
            System.out.println("Requesting Site  : " + getRequestingSite());
            return new PasswordAuthentication(username, password.toCharArray());
        }
    }
}
