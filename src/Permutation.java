// Java program to print all combination of size
// r in an array of size n

class Permutation {

    /**
     * calculates how many rows the formula has if the current field has n neighbors.
     * @param n number of neighbors
     * @return number of rows
     */
    static int fac(int n){
        if(n <= 1){
            return 1;
        }
        return n * fac(n-1);
    }

    /**
     * calculates the al formula for the sat solver.
     * @param arr array with neighboring fields
     * @param n  number of neighbors
     * @param anzMinen number of mines
     * @param x x-Coordinate of current field.
     * @param y y-Coordinate of current field.
     * @return al formula
     */
    static int[][] getClauses(int arr[], int n, int anzMinen, int x, int y){
        int rows = 0;
        for(int i = 0; i <= n; i++){
            if(i == anzMinen){
                continue;
            }
            rows += fac(n) / (fac(i)*(fac(n-i))); // (n!)/(n*(n-k)!)
        }
        System.out.println(rows);
        int[][] res = new int[rows][n];     //result array for formula

        for(int i = 0; i <= n; i++){        //fill array with default values
            for(int j = 0; j <= n; j++){
                res[i][j] = 9;
            }
        }

        for(int i = 0; i <= n; i++){        //fill array with valid values
            for(int j = 0; j <= n; j++){
                if(n == 8){
                    res[i][j] = arr[j];
                }
                else if(n == 5){

                }
                else if(n == 3){

                    if (x == 0 && y == 0){
                        // Nachbarn 7, 8 ,5 bleiben
                    }
                    //else if(x == max && y == 0) 4, 6, 7 bleiben
                    //else if (y == max && x == 0) 2, 3, 5 bleiben
                    else{

                    }
                }
            }
        }
        return res;

    }

    /* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
    static void combinationUtil(int arr[], int n, int r,
                                int index, int data[], int i) {
        // Current combination is ready to be printed,
        // print it
       /* if (index == r) {
            for (int j = 0; j < r; j++) {
                System.out.print(data[j] * -1 + " ");
            }
            System.out.println();
            return;
        }*/


        // When no more elements are there to put in data[]
        if (i >= n)
           // return;

        // current is included, put next at next
        // location
        data[index] = arr[i];
        combinationUtil(arr, n, r, index + 1,
                data, i + 1);

        // current is excluded, replace it with
        // next (Note that i+1 is passed, but
        // index is not changed)
        combinationUtil(arr, n, r, index, data, i + 1);

    }

    // The main function that prints all combinations
    // of size r in arr[] of size n. This function
    // mainly uses combinationUtil()
    static void printCombination(int arr[], int n, int r) {
        // A temporary array to store all combination
        // one by one
        int data[] = new int[r];

        // Print all combination using temporary
        // array 'data[]'
        combinationUtil(arr, n, r, 0, data, 0);
    }


    /** Driver function to check for above function */
    public static void main(String[] args) {
        int arr[] = {1, 2, 3, 4, 5, 6, 7, 8};
        /*System.out.println("Start");

        int r = 7;
        int n = arr.length;
        printCombination(arr, n, r);*/

       // getClauses(arr, 8, 2);
    }
}
