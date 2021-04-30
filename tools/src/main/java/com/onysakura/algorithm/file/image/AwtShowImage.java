package com.onysakura.algorithm.file.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AwtShowImage {

    public static void main(String[] args) {
        String path = "/Files/Temp/code.jpg";
        show(path);
    }

    public static void show(String path) {
        EventQueue.invokeLater(() -> {
                    ImageFrame frame = new ImageFrame(path);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                }
        );
    }
}

class ImageFrame extends JFrame {
    public ImageFrame(String path) {
        setTitle("ImageTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        ImageComponent component = new ImageComponent(path);
        add(component);
        getContentPane().validate();
        getContentPane().repaint();
    }

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;
}

class ImageComponent extends JComponent {
    private static final long serialVersionUID = 1L;
    private Image image;

    public ImageComponent(String path) {
        try {
            File image2 = new File(path);
            image = ImageIO.read(image2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        if (image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);
        g.drawImage(image, 0, 0, this);
        for (int i = 0; i * imageWidth <= getWidth(); i++)
            for (int j = 0; j * imageHeight <= getHeight(); j++)
                if (i + j > 0) g.copyArea(0, 0, imageWidth, imageHeight, i * imageWidth, j * imageHeight);
    }
}