package com.phen.bankingapp;

public class PerfectSquareCount {

    public static void main(String[] args) {

        // Test for when length and breath are equal
        int[] squareGrid = new int[]{3,3};
        printPerfectSquares(squareGrid);

        // Test for when length and breath are not equal
        int[] nonSquareGrid = new int[]{4,5};
        printPerfectSquares(nonSquareGrid);
    }

    public static void printPerfectSquares(int[] arr) {
        int result = 0;
        if(arr[0] == arr[1]) {
            result = getPerfectSquaresInSquareGrid(arr[0]);
        } else {
           result = getPerfectSquaresInNonSquareGrid(arr);
        }
        System.out.println(result);
    }

    private static int getPerfectSquaresInSquareGrid(int num) {
        int total = 0;
        for(int x = 1; x <= num; x++) {
            total = total + (int) Math.pow(x, 2);
        }
        return total;
    }

    private static int getPerfectSquaresInNonSquareGrid(int[] arr) {
        int result = 0;
        int min = Math.min(arr[0], arr[1]);
        int max = Math.max(arr[0], arr[1]);
        int minTracker = min;
        int maxTracker = max;

        for (int x = 0; x < min; x++) {
            result += maxTracker * minTracker;
            minTracker--;
            maxTracker--;
        }
        return result;
    }
}
