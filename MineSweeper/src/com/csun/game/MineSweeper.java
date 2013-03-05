
package com.csun.game;

import javax.swing.SwingUtilities;

public class MineSweeper {
    public static void main(String[] args) {
        real();
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
