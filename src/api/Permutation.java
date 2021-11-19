package api;// Java program to print all combination of size
// r in an array of size n
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

class Permutation {

    /* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */

    /**
     *
     * @param arr
     * @param n
     * @param r
     * @param index
     * @param data
     * @param i
     * @param res
     */
    static void combinationUtil(int arr[], int n, int r,
                                int index, int data[], int i, ArrayList<int[]> res)
    {
        // negates literals
        if (index == r) {
            for (int j = 0; j < data.length; j++){
                data[j] = -1 * Math.abs(data[j]);
            }
            System.out.println("data :"  + Arrays.toString(data));
            int[] copy = new int[data.length];
            System.arraycopy(data, 0, copy, 0, copy.length);
            res.add(copy);
            return;
        }

        // When no more elements are there to put in data[]
        if (i >= n)
            return;

        // current is included, put next at next
        // location
        data[index] = arr[i];
        combinationUtil(arr, n, r, index + 1,
                data, i + 1, res);

        // current is excluded, replace it with
        // next (Note that i+1 is passed, but
        // index is not changed)
        combinationUtil(arr, n, r, index, data, i + 1, res);
    }

    // The main function that prints all combinations
    // of size r in arr[] of size n. This function
    // mainly uses combinationUtil()
    static void printCombination(int arr[], int n, int r)
    {
        // A temporary array to store all combination
        // one by one
        int data[] = new int[r];

        // Print all combination using temporary
        // array 'data[]'
        //combinationUtil(arr, n, r, 0, data, 0);
    }

    /**
     *
     * @param arr array mit allen zu betrachtenden nachbarn des aktuellen feldes
     * @param n length of array arr
     * @param r Menge der Literale innerhalb der unterteilten Klauseln
     * @return Klauseln die der KB hinzugefügt werden sollen
     */
    static ArrayList<int[]> computeClauses(int arr[], int n, int r)
    {
        ArrayList<int[]> res = new ArrayList<int[]>();
        // A temporary array to store all combination
        // one by one
        int data[] = new int[r];

        // Print all combination using temporary
        // array 'data[]'
        combinationUtil(arr, n, r, 0, data, 0, res);
        for (int[] array : res){
            System.out.println("res: " + Arrays.toString(array));
        }
        return res;
    }

    /* Driver function to check for above function */
    /*
    public static void main(String[] args)
    {
        System.out.println("Start");
        int arr[] = { 33, 43, 53, 34, 54, 35, 45, 55};
        int r = 8;
        int n = arr.length;
        printCombination(arr, n, r);
    }
     */
}
/* This code is contributed by Devesh Agrawal */