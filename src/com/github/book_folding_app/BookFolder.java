package com.github.book_folding_app;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class BookFolder {
    private String img_name;
    private float height, width, px_height;
    private float num_pages;
    private float offset_front, offset_back;
    private float min_cut_len;
    private short tolerance;
    private Scanner scanner = new Scanner(System.in);

    // Retrieves specified image as a 2D array of greyscale pixels
    private int[][] get_img() {

    }

    // Gets string input from user
    private String get_str(String message) {
        System.out.println(message);
        return scanner.nextLine();
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
                    System.out.format("ERROR: Input must be between %.4f and %.4f", min, max);
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
                    System.out.format("ERROR: Input must be between %d and %d", min, max);
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

    private float get_height(float num, float px_height, float height) {
        return num / px_height * height;
    }

    private void run() {
        // Get dimensions from user input
        img_name = get_str("\nEnter filename of image\n" +
                " This image should be black--and-white only\n" +
                " And must be in the current directory\n");
        height = get_num("\nEnter page height in inches\n",
                0, Float.MAX_VALUE); // Change this from MAX_VALUE to something reasonably big
        num_pages = get_num("\nEnter number of pages in book\n" +
                " Numbered pages (with different numbers on front and back) will count as a single page\n" +
                " Be sure to include non-numbered pages\n", 0, Float.MAX_VALUE);
        offset_front = get_num("\nEnter number of pages to offset image from the front cover\n",
                0, num_pages);
        offset_back = get_num("\nEnter number of pages to offset image from the back cover\n",
                0, num_pages);
        min_cut_len = get_num("\nEnter the minimum cut length in inches for each page\n",
                0, height);
        tolerance = get_num("\nInput a black-white pixel tolearnce (0 - 255)\n" +
                " All values above this will not be considered part of the image\n",
                (short)0, (short)255);
        width = num_pages - offset_front - offset_back;
        // 1 in : 96 px
        px_height = height * 96;

    }

    public static void main(String[] args) {
        BookFolder bookFolder = new BookFolder();
        bookFolder.run();
    }
}
