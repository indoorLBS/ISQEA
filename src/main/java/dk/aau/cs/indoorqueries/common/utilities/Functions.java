/**
 *
 */
package dk.aau.cs.indoorqueries.common.utilities;

import java.util.ArrayList;
import java.util.Random;

/**
 * <h>Functions</h>
 * Utility Functions
 *
 * @author feng zijin
 *
 */
public class Functions {
    /**
     * concatenate two parameters to form a key
     * @param i parameter 1
     * @param j parameter 2
     * @return string key
     */
    public static String keyConventer(int i, int j) {
        return ((i <= j)) ? (i + "#" + j) : (j + "#" + i);
    }

    /**
     * concatenate two parameters to form a key
     * @param str
     * @return integer array (size 2)
     */
    public static int[] keyReverseConventer(String str) {
        int[] result = new int[2];

        String[] temp = str.split("#");

        for (int i = 0; i < temp.length; i++) {
            result[i] = Integer.parseInt(temp[i]);
        }

        return result;
    }

    /**
     * generate random integer
     * @param min
     * @param max
     * @return int
     */
    public static int randInt(int min, int max) {
        Random rand = null;

        int randomNum = min + (int) (Math.random() * ((max - min) + 1));

        return randomNum;
    }

    /**
     * get certain part of the string array in string
     * @param arr
     * @param init
     * @return end (exlcude)
     */
    public static String arrayToString1D(String[] arr, int init, int end) {
        String result = "";

        for (int i = init; i < end - 1; i++) {
            result = result + arr[i] + "\t";
        }

        result = result + arr[end - 1];

        return result;
    }

    /**
     * get certain part of the integer array in string
     * @param arr
     * @param init
     * @return end (exlcude)
     */
    public static String arrayToInetger1D(int[] arr, int init, int end) {
        String result = "";

        for (int i = init; i < end - 1; i++) {
            result = result + arr[i] + "\t";
        }

        result = result + arr[end - 1];

        return result;
    }

    /**
     * get certain part of the array in string
     * @param arr
     * @param x_init
     * @param x_end (exlcude)
     * @param y_init
     * @param y_end (exlcude)
     * @return result
     */
    public static String print2Ddoublearray(double[][] arr, int x_init, int x_end, int y_init, int y_end) {
        String result = "";

        for (int i = x_init; i < x_end; i++) {
            for (int j = y_init; j < y_end; j++) {
                result = result + arr[i][j] + "\t" + "\t" + "\t" + "\t";
            }
            result = result + "\n";
        }

        return result;
    }

    /**
     * get certain part of the array in string
     * @param arr
     */
    public static String printIntegerList(ArrayList<Integer> arr) {
        String result = "";

        int arrSize = arr.size();
        for (int i = 0; i < arrSize; i++) {
            result = result + arr.get(i) + "\t";
        }

        return result;
    }

    /**
     * check whether two array are equals
     * @param js
     * @param js2
     */

    public static boolean equals(int[] js, int[] js2) {

        if (!js.getClass().equals(js2.getClass())) {
            System.out.println(js.getClass() + " " + js2.getClass());
            return false;
        }

        if (js.length != js2.length) {
            System.out.println("len " + js.length + " " + js2.length);
            return false;
        }

        for (int i = 0; i < js.length; i++) {
            if (js[i] != js2[i]) {
                System.out.println(js[i] + " " + js2[i]);
                return false;
            }
        }

        return true;
    }

    /**
     * @param minRoute
     * @return
     */
    public static String reverse(String minRoute) {
        String result = "";

        String[] strs = minRoute.split("\t");

        for (int i = strs.length - 1; i > 0; i--) {
            result = result + strs[i] + "\t";
        }
        result = result + strs[0];

        return result;
    }


}
