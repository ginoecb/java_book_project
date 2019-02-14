package com.github.book_folding_app;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class BookFolder {
    String img_name;
    float height, width, px_height;
    float num_pages;
    float offset_front, offset_back;
    float min_cut_len;
    short tolerance;

    //get_image

    // Gets string input from user
    String get_str(String message, Scanner scanner) {
        System.out.println(message);
        return scanner.nextLine();
    }

    // Gets float input from user
    float get_num(String message, Scanner scanner, float min, float max) {
        float num;
        while (true) {
            System.out.println(message);
            if(scanner.hasNextFloat()) {
                num = scanner.nextFloat();
                if (num >= min && num <= max) {
                    break;
                } else {
                    System.out.format("ERROR: Input must be between %.4f and %.4f\n", min, max);
                }
            }
            else {
                System.out.println("ERROR: Input must be a number\n");
            }
        }
        return num;
    }

    // Determine cut distance(s) for a given page
    ArrayList<Float> get_page_cuts(int[] arr, float min_cut_len, int tolerance, float px_height, float height) {
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
                cuts = check_page_cuts(cuts, min_cut_len, px_height, height);
            }
        }
        if (!start_cut) {
            cuts.add((float)arr.length);
        }
        return cuts;
    }

    // Removes cut instructions if less than minimum cut length
    ArrayList<Float> check_page_cuts(ArrayList<Float> cuts, float min_cut_len, float px_height, float height) {
        int end = cuts.size() - 1;
        // if get_height
        if (cuts.get(end) - cuts.get(end - 1) < min_cut_len) {
            cuts.remove(cuts.size() - 1);
            cuts.remove(cuts.size() - 1);
        }
        return cuts;
    }

    float get_height(float num, float px_height, float height) {
        return num / px_height * height;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookFolder bf = new BookFolder();
        bf.img_name = bf.get_str("\nEnter filename of image\n" +
                " This image should be black--and-white only\n> ", scanner);
        bf.height = bf.get_num("\nEnter page height in inches\n> ", scanner, 0, Float.MAX_VALUE);
        bf.num_pages = bf.get_num("\nEnter number of pages in book\n" +
                " Numbered pages (with different numbers on front and back) will count as a single page\n" +
                " Be sure to include non-numbered pages\n> ", scanner, 0, Float.MAX_VALUE);





        /*
        bf.tolerance = 2;
        bf.min_cut_len = 0.2f;
        int[] arr = {0, 5, 5, 5, 0, 0, 0, 0, 5, 5, 5, 5, 0, 5, 0, 5, 0, 0, 5};
        ArrayList<Float> cuts = bf.get_page_cuts(arr, bf.min_cut_len, bf.tolerance, 0, 0);
        for (Float i : cuts){
            System.out.println(i);
        }
        */
    }
}
