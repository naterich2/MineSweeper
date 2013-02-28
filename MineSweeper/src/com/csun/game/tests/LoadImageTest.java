package com.csun.game.tests;

import com.csun.game.Board;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class LoadImageTest {
    public static void loadImageFromFile(String fileName) {
        BufferedImage buffer = null;
        File f = new File(fileName);
        try {
            buffer = ImageIO.read(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            
        }
        final Board b = new Board(buffer);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("MineSweeper");
                frame.setContentPane(b);
                frame.setSize(300, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
