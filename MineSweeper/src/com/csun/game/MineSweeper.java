
package com.csun.game;

import com.csun.game.tests.LoadImageTest;

import javax.swing.SwingUtilities;

public class MineSweeper {
    public static void main(String[] args) {
//        test();
        real();
    }

    private static void test() {
        LoadImageTest.loadImageFromFile(Constants.FACE.CLOCK);
    }
    
    private static void real() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Board.play();
            }
        });

    }

}
