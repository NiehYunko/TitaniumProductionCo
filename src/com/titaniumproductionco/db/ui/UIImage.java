package com.titaniumproductionco.db.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UIImage {
    public static final Image PROGRAM_ICON = load("icon.png");
    public static final Image ADD_FILE = load("addfile.png");
    public static final Image FILE = load("file.png");
    public static final Image UPDATE_FILE = load("updatefile.png");
    public static final Image REMOVE_FILE = load("removefile.png");
    public static final Image EXIT = load("exit.png");
    public static final Image REFRESH = load("refresh.png");
    public static final Image USER = load("user.png");
    public static final Image ANALYZE_FILE = load("analyzefile.png");
    public static final Image BIG_ICON = bigIcon();

    private static Image load(String location) {
        try {
            return ImageIO.read(UIImage.class.getResourceAsStream("/com/titaniumproductionco/assets/" + location));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Image bigIcon() {
        int mult = 2;
        BufferedImage bigicon = new BufferedImage(PROGRAM_ICON.getWidth(null) * mult, PROGRAM_ICON.getHeight(null) * mult, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bigicon.createGraphics();
        g2d.scale(mult, mult);
        g2d.drawImage(PROGRAM_ICON, 0, 0, null);
        g2d.dispose();
        return bigicon;
    }
}
