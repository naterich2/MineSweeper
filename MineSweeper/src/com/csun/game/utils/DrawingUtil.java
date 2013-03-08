
package com.csun.game.utils;

import com.csun.game.DrawInfo;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class DrawingUtil {
    private DrawingUtil() {
        
    }
    
    public static void drawImage(BufferedImage image, Graphics g, int x, int y) {
        g.drawImage(
                image, DrawInfo.OFFSET_X + x * DrawInfo.CELL_LENGTH,
                DrawInfo.OFFSET_Y + y * DrawInfo.CELL_LENGTH,
                DrawInfo.CELL_LENGTH, DrawInfo.CELL_LENGTH,
                null);

    }
}
