
package com.csun.game.utils;

import com.csun.game.DrawInfo;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class DrawingUtil {
    private DrawingUtil() {
        
    }
    
    public static void drawImage(BufferedImage image, Graphics g, DrawInfo info, int x, int y) {
        g.drawImage(
                image, info.getOffsetX() + x * info.getCellLength(),
                info.getOffsetY() + y * info.getCellLength(),
                info.getCellLength(), info.getCellLength(), 
                null);

    }
}
