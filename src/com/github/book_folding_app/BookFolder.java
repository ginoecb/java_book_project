package com.github.book_folding_app;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BookFolder {
    private String img_name, img_resize_name;
    private float height;
    private int num_pages;
    private int px_height, px_width;
    private int offset_front, offset_back;
    private float min_cut_len;
    private short tolerance;
    private Scanner scanner = new Scanner(System.in);

    // Gets string input from user
    private String get_str(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    // Gets int input from user
    private int get_num(String message, int min, int max) {
        short num;
        while (true) {
            System.out.println(message);
            if(scanner.hasNextFloat()) {
                num = scanner.nextShort();
                if (num >= min && num <= max) {
                    break;
                } else {
                    System.out.format(
                            "ERROR: Input must be between %d and %d", min, max);
                    scanner.next();
                }
            }
            else {
                System.out.println("ERROR: Input must be a number");
                scanner.next();
            }
        }
        return num;
    }

    // Gets float input from user
    private float get_num(String message, float min, float max) {
        float num;
        while (true) {
            System.out.println(message);
            if(scanner.hasNextFloat()) {
                num = scanner.nextFloat();
                if (num >= min && num <= max) {
                    break;
                } else {
                    System.out.format(
                            "ERROR: Input must be between %.4f and %.4f", min, max);
                    scanner.next();
                }
            }
            else {
                System.out.println("ERROR: Input must be a number");
                scanner.next();
            }
        }
        return num;
    }

    // Gets short input from user
    private short get_num(String message, short min, short max) {
        short num;
        while (true) {
            System.out.println(message);
            if(scanner.hasNextFloat()) {
                num = scanner.nextShort();
                if (num >= min && num <= max) {
                    break;
                } else {
                    System.out.format(
                            "ERROR: Input must be between %d and %d", min, max);
                    scanner.next();
                }
            }
            else {
                System.out.println("ERROR: Input must be a number");
                scanner.next();
            }
        }
        return num;
    }

    // Determine cut distance(s) for a given page
    private ArrayList<Float> get_page_cuts(int[] arr) {
        ArrayList<Float> cuts = new ArrayList<>();
        boolean start_cut = true;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < tolerance && start_cut) {
                cuts.add((float)i);
                start_cut = false;
            }
            else if (arr[i] >= tolerance && !start_cut) {
                cuts.add((float)i);
                start_cut = true;
                cuts = check_page_cuts(cuts);
            }
        }
        if (!start_cut) {
            cuts.add((float)arr.length);
        }
        return cuts;
    }

    // Removes cut instructions if less than minimum cut length
    private ArrayList<Float> check_page_cuts(ArrayList<Float> cuts) {
        int end = cuts.size() - 1;
        // if get_height
        if (cuts.get(end) - cuts.get(end - 1) < min_cut_len) {
            cuts.remove(cuts.size() - 1);
            cuts.remove(cuts.size() - 1);
        }
        return cuts;
    }

    // Convert height from pixels to inches
    private float get_height(float num, float px_height, float height) {
        return num / px_height * height;
    }

    // Retrieve specified image
    private BufferedImage get_img() {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(img_name));
        }
        catch (IOException e) {
            System.out.println("ERROR: File not found");
        }
        return img;
    }

    // Resize image
    private BufferedImage resize_img(BufferedImage img) {
        Image img_temp = img.getScaledInstance(num_pages, px_height, Image.SCALE_SMOOTH);
        BufferedImage img_resize = new BufferedImage(num_pages, px_height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = img_resize.createGraphics();
        graphics.drawImage(img_temp, 0, 0, null);
        graphics.dispose();
        return img_resize;
    }

    // Convert image to grayscale
    private BufferedImage get_img_gray(BufferedImage img) {
        BufferedImage img_grayscale = new BufferedImage(
                img.getWidth(),
                img.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics graphics = img_grayscale.getGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();
        return img_grayscale;
    }

    // Convert grayscale image to 2D array of pixels

    // Perform image manipulation functions
    private BufferedImage edit_img() {
        BufferedImage img = get_img();
        BufferedImage img_resize = resize_img(img);
        BufferedImage img_gray = get_img_gray(img_resize);
        return img_gray;
    }

    // Save image
    private void save_img(BufferedImage img, String file_path, String file_name) {
        File out_file = new File(file_path + File.separator + file_name);
        System.out.println(out_file);
        try {
            ImageIO.write(img, img_name.substring(img_name.lastIndexOf('.') + 1), out_file);
        }
        catch (IOException e) {
            System.out.println("ERROR: File not saved");
        }
    }

    private void test() {
        num_pages = 100;
        height = 10f;
        px_height = (int)(height * 96);
        img_name = get_str("\nEnter image name\n");
        px_width = num_pages;
        BufferedImage img = edit_img();
        // Try using this:
        // C:\Users\g3bry\Desktop\Python\BookProject\pugtest.png
        // Works as long as the image is placed in the \BookApp dir
        String working_dir = System.getProperty("user.dir");
        String abs_file_path = working_dir + File.separator + img_name;
        System.out.println(abs_file_path);

        save_img(img, working_dir, "test_out" + img_name.substring(img_name.lastIndexOf('.')));

        // Converting image to array
        int[][] arr = new int[px_width][px_height];
        for (int x = 0; x < px_width; x++) {
            for (int y = 0; y < px_height; y++) {
                arr[x][y] = img.getRGB(x, y);
                //System.out.println(px & 0xFF);
            }
        }

        /*
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, img_name.substring(img_name.lastIndexOf('.') + 1), baos);
        }
        catch (IOException e) {
            System.out.println("ERROR: File not saved as ByteArrayOutputStream");
        }
        byte [] data = baos.toByteArray();
        for (int i = 0; i < data.length; i++) {
            // Masks off sign bits
            System.out.println(data[i] & 0xFF);
            //System.out.println(data[i]);
        }
        System.out.println(data.length);
        */
    }

    private void run() {
        // Get dimensions from user input
        img_name = get_str(
                "\nEnter filename of image\n" +
                " This image should be black--and-white only\n" +
                " And must be in the current directory\n");
        height = get_num(
                "\nEnter page height in inches\n",
                0,
                Float.MAX_VALUE); // Change this from MAX_VALUE to something reasonably big
        num_pages = get_num(
                "\nEnter number of pages in book\n" +
                " Numbered pages (with different numbers on front and back) will count as a single page\n" +
                " Be sure to include non-numbered pages\n",
                0,
                Integer.MAX_VALUE);
        offset_front = get_num(
                "\nEnter number of pages to offset image from the front cover\n",
                0,
                num_pages);
        offset_back = get_num(
                "\nEnter number of pages to offset image from the back cover\n",
                0,
                num_pages);
        min_cut_len = get_num(
                "\nEnter the minimum cut length in inches for each page\n",
                0,
                height);
        tolerance = get_num(
                "\nInput a black-white pixel tolearnce (0 - 255)\n" +
                " All values above this will not be considered part of the image\n",
                (short)0,
                (short)255);
        px_width = num_pages - offset_front - offset_back;
        // 1 in : 96 px
        px_height = (int)(height * 96);

    }

    public static void main(String[] args) {
        BookFolder bookFolder = new BookFolder();
        //bookFolder.run();
        bookFolder.test();
    }
}
