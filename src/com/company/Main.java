package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private static final String IMAGE_FILE   = "/Users/DimKa_N7/Documents/IS/lab6/self1.png";
    private static final String CORRUPTED_FILE   = "/Users/DimKa_N7/Documents/IS/lab6/selfCorrupted.png";

    public static void main(String[] args) throws IOException {
        BufferedImage imageOriginal = ImageIO.read(new File(IMAGE_FILE));
        BufferedImage corruptedOriginal = ImageIO.read(new File(CORRUPTED_FILE));
        print("Original image: " + getHashSum(imageOriginal)); // e167f5243815e8e79685bca05914e8df
        print("Corrupted image: " + getHashSum(corruptedOriginal)); // c9fb3769379379449e4ffcf3ac3416e2
    }

    private static String getHashSum(BufferedImage image) {
        String message = "";

        for (int c = 0; c < 3; c++) {
            int[][] imagePixels = getPixels(image, c);
            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < 50; j++) {
                    message += Integer.toBinaryString(imagePixels[i][j]);
                }
            }
        }

        String message512 = addBites(message);

        ArrayList<String> mdBuffer = new ArrayList<>();
        mdBuffer.add("01234567");
        mdBuffer.add("89abcdef");
        mdBuffer.add("fedcba98");
        mdBuffer.add("76543210");

        String A = completeHex(mdBuffer.get(0));
        String B = completeHex(mdBuffer.get(1));
        String C = completeHex(mdBuffer.get(2));
        String D = completeHex(mdBuffer.get(3));

        for (int i = 0; i < message512.length() / 16; i++) {

            String AA = A;
            String BB = B;
            String CC = C;
            String DD = D;

            String[] x = new String[16];
            for (int j = 0; j < 16; j++) {
                x[j] = Character.toString(message512.charAt(i * 16 + j));
            }

            //r1
            A = roundFunc(A, B, C, D, x, 0, 0, 3);
            D = roundFunc(D, A, B, C, x, 0, 1, 7);
            C = roundFunc(C, D, A, B, x, 0, 2, 11);
            B = roundFunc(B, C, D, A, x, 0, 3, 19);

            A = roundFunc(A, B, C, D, x, 0, 4, 3);
            D = roundFunc(D, A, B, C, x, 0, 5, 7);
            C = roundFunc(C, D, A, B, x, 0, 6, 11);
            B = roundFunc(B, C, D, A, x, 0, 7, 19);

            A = roundFunc(A, B, C, D, x, 0, 8, 3);
            D = roundFunc(D, A, B, C, x, 0, 9, 7);
            C = roundFunc(C, D, A, B, x, 0, 10, 11);
            B = roundFunc(B, C, D, A, x, 0, 11, 19);

            A = roundFunc(A, B, C, D, x, 0, 12, 3);
            D = roundFunc(D, A, B, C, x, 0, 13, 7);
            C = roundFunc(C, D, A, B, x, 0, 14, 11);
            B = roundFunc(B, C, D, A, x, 0, 15, 19);

            //r2
            A = roundFunc(A, B, C, D, x, 1, 0, 3);
            D = roundFunc(D, A, B, C, x, 1, 4, 5);
            C = roundFunc(C, D, A, B, x, 1, 8, 9);
            B = roundFunc(B, C, D, A, x, 1, 12, 13);

            A = roundFunc(A, B, C, D, x, 1, 1, 3);
            D = roundFunc(D, A, B, C, x, 1, 5, 5);
            C = roundFunc(C, D, A, B, x, 1, 9, 9);
            B = roundFunc(B, C, D, A, x, 1, 13, 13);

            A = roundFunc(A, B, C, D, x, 1, 2, 3);
            D = roundFunc(D, A, B, C, x, 1, 6, 5);
            C = roundFunc(C, D, A, B, x, 1, 10, 9);
            B = roundFunc(B, C, D, A, x, 1, 14, 13);

            A = roundFunc(A, B, C, D, x, 1, 3, 3);
            D = roundFunc(D, A, B, C, x, 1, 7, 5);
            C = roundFunc(C, D, A, B, x, 1, 11, 9);
            B = roundFunc(B, C, D, A, x, 1, 15, 13);

            //r3
            A = roundFunc(A, B, C, D, x, 2, 0, 3);
            D = roundFunc(D, A, B, C, x, 2, 8, 9);
            C = roundFunc(C, D, A, B, x, 2, 4, 11);
            B = roundFunc(B, C, D, A, x, 2, 12, 15);

            A = roundFunc(A, B, C, D, x, 2, 2, 3);
            D = roundFunc(D, A, B, C, x, 2, 10, 9);
            C = roundFunc(C, D, A, B, x, 2, 6, 11);
            B = roundFunc(B, C, D, A, x, 2, 14, 15);

            A = roundFunc(A, B, C, D, x, 2, 1, 3);
            D = roundFunc(D, A, B, C, x, 2, 9, 9);
            C = roundFunc(C, D, A, B, x, 2, 5, 11);
            B = roundFunc(B, C, D, A, x, 2, 13, 15);

            A = roundFunc(A, B, C, D, x, 2, 3, 3);
            D = roundFunc(D, A, B, C, x, 2, 11, 9);
            C = roundFunc(C, D, A, B, x, 2, 7, 11);
            B = roundFunc(B, C, D, A, x, 2, 15, 15);

            A = sum(A, AA);
            B = sum(B, BB);
            C = sum(C, CC);
            D = sum(D, DD);
        }

        return toHexString(A) + toHexString(B) + toHexString(C) + toHexString(D);
    }

    private static int[][] getPixels(BufferedImage image, int color) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(image.getRGB(col, row));
                switch (color) {
                    case 0:
                        result[row][col] = c.getRed();
                        break;
                    case 1:
                        result[row][col] = c.getGreen();
                        break;
                    case 2:
                        result[row][col] = c.getBlue();
                        break;
                }
            }
        }
        return result;
    }

    private static String toHexString(String binary) {
        Long dec = Long.parseLong(binary, 2);
        return Long.toHexString(dec);
    }

    // дополнить 0 чтобы получилось 32битное сообщение
    private static String completeHex(String hex) {
        String result = Long.toBinaryString(Long.parseLong(hex, 16));
        while (result.length() < 32) {
            result = "0" + result;
        }
        return result;
    }

    // funcNum = 0 => f
    // funcNum = 1 => g
    // funcNum = 2 => h
    private static String roundFunc(String arg1, String arg2, String arg3, String arg4, String[] x, int funcNum, int k, int s) {
        String result = "";
        switch (funcNum) {
            case 0:
                result = sum(arg1, f(arg2, arg3, arg4));
                result = sum(result, x[k]);
                result = shift(toBoolArr(result), s);
                break;
            case 1:
                result = sum(arg1, g(arg2, arg3, arg4));
                result = sum(result, x[k]);
                result = sum(result, completeHex("5A827999"));
                result = shift(toBoolArr(result), s);
                break;
            case 2:
                result = sum(arg1, h(arg2, arg3, arg4));
                result = sum(result, x[k]);
                result = sum(result, completeHex("6ED9EBA1"));
                result = shift(toBoolArr(result), s);
                break;
        }

        return result;
    }

    private static String sum(String word1, String word2) {
        boolean[] arr1Bool = toBoolArr(word1);
        boolean[] arr2Bool = toBoolArr(word2);
        String result = "";
        for (int i = 0; i < 32; i++) {
            if (arr2Bool.length > 1) {
                result += toString(arr1Bool[i] ^ arr2Bool[i]);
            } else {
                result += toString(arr1Bool[i] ^ arr2Bool[0]);
            }
        }
        return result;
    }

    private static String shift(boolean[] arr, int s) {
        boolean[] result = arr;
        for (int i = 0; i < s; i++) {
            boolean temp = result[0];
            for (int j = 0; j < result.length - 1; j++) {
                result[j] = arr[j + 1];
            }
            result[result.length - 1] = temp;
            arr = result;
        }
        return toString(result);
    }

    private static String addBites(String message) {
        int len = message.length();
        while (message.length() % 512 != 448) {
            if (message.length() == len) {
                message += "1";
            }
            else {
                message += "0";
            }
        }
        String bitLen = Integer.toBinaryString(len);
        for (int i = bitLen.length(); i < 64; i++) {
            bitLen = "0" + bitLen;
        }
        return message + bitLen;
    }

    private static String f(String x, String y, String z) {
        String result = "";
        boolean[] xBool = toBoolArr(x);
        boolean[] yBool = toBoolArr(y);
        boolean[] zBool = toBoolArr(z);
        for (int i = 0; i < 32; i++) {
            if (xBool[i] == true) result += toString(yBool[i]);
            else result += toString(zBool[i]);
        }
        return result;
    }

    private static String g(String x, String y, String z) {
        String result = "";
        boolean[] xBool = toBoolArr(x);
        boolean[] yBool = toBoolArr(y);
        boolean[] zBool = toBoolArr(z);
        for (int i = 0; i < 32; i++) {
            if (xBool[i] && yBool[i] || xBool[i] && zBool[i] || yBool[i] && zBool[i]) result += "1";
            else result += "0";
        }
        return result;
    }

    private static String h(String x, String y, String z) {
        String result = "";
        boolean[] xBool = toBoolArr(x);
        boolean[] yBool = toBoolArr(y);
        boolean[] zBool = toBoolArr(z);
        for (int i = 0; i < 32; i++) {
            result += toString(xBool[i] ^ yBool[i] ^ zBool[i]);
        }
        return result;
    }

    private static boolean[] toBoolArr(String str) {
        boolean[] result = new boolean[str.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = str.charAt(i) == '0' ? false : true;
        }
        return result;
    }

    private static String toString(boolean[] arr) {
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            result += arr[i] == false ? "0" : "1";
        }
        return result;
    }

    private static String toString(boolean value) {
        return value == false ? "0" : "1";
    }

    private static void print(String text) {
        System.out.println(text);
    }
}