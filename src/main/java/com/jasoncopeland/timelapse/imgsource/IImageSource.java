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

import java.awt.image.BufferedImage;

/**
 * Created by Jason on 3/12/2016.
 */
public interface IImageSource {
    BufferedImage getCurrentImage();
}
