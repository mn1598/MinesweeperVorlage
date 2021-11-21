package api;

import java.util.ArrayList;
import java.util.Arrays;

import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.Collections;
import java.util.Random;

/**
 * This agent uncovers positions randomly. It obviously does not use a SAT
 * solver.
 * You can do whatever you want with this class.
 */
public class RandomMSAgent extends MSAgent {

    private final Random rand;
    private final ArrayList<int[]> knowledgeBase = new ArrayList<int[]>();
    private boolean displayActivated = true;
    private boolean firstDecision = true;
    private int[][] uncovered;
    private final ArrayList<Integer> pending = new ArrayList<Integer>();

    public RandomMSAgent(MSField field) {
        super(field);
        this.rand = new Random();
    }

    public static void main(String[] args) {
        MSField f = new MSField("fields/" + "baby2-3x3-1.txt");
        RandomMSAgent r = new RandomMSAgent(f);
        //System.out.println(r.anzNachbarn(1,1));
        //r.solve();
		/*for(int i = 0; i < r.field.getNumOfRows(); i++){
			for (int j = 0; j < r.field.getNumOfCols(); j++){
				//System.out.print(r.updateKnowledgeBase(j,i,0) + ", ");

			}
			System.out.println();
		}*/

       // r.updateKnowledgeBase(2, 2, 2);

//        int[][] test ={ {-2 , 4, 5},
//        {2, -4 , 5},
//        {2, 4, -5},
//        {-2, -4, 5},
//        {-2, 4, -5},
//        {2, -4, -5},
//        {2,4,5}};
//        for (int i = 0; i < test.length;i++){
//            r.knowledgeBase.add(test[i]);
//        }


       /* System.out.println(r.knowledgeBase.size());
        for (int[] array : r.knowledgeBase) {
            System.out.println(Arrays.toString(array));
        }
		System.out.println(r.satSolve(r.knowledgeBase));*/

        r.solve();
        for (int i = 1; i < 481; i++) {
        System.out.println(i + ":" + Arrays.toString(r.toCoordinate(i)));
        }
    }

    @Override
    public boolean solve() {

        int numOfRows = this.field.getNumOfRows();
        int numOfCols = this.field.getNumOfCols();
        uncovered = new int[numOfCols][numOfRows];
        for (int i = 0; i < uncovered.length; i++) {
            Arrays.fill(uncovered[i], -2);
        }

        int x = 0, y = 0, feedback = 0;

        do {
            if (displayActivated) {
                System.out.println(field);
            }
            if (firstDecision) {
                firstDecision = false;
            } else {
                //int erg[][] = getClauses(anzNachbarn(x, y), x, y);

                //use SAT solver

                //int[] erg = sat(x,y ,feedback); // TODO: pass values of the field which should be uncovered next
                //x = erg[0];
                //y = erg[1];

                int next = 0; // deault wert. MUSS vor naechstem Durchgang geaendert werden!!
                this.updateKnowledgeBase(x, y, feedback);
                int anzNach = this.anzNachbarn(x, y);
                int[] nachbarn = this.getClauses(anzNach, toLiteral(x, y), x, y);

                for(int i = 0; i < nachbarn.length; i++){
                    int[] coordinates = this.toCoordinate(nachbarn[i]);
                    if(this.uncovered[coordinates[0]][coordinates[1]] != -2){
                            continue;
                    }
                    this.knowledgeBase.add(new int[] {nachbarn[i] * -1});
                    //this.knowledgeBase.forEach((n) -> {System.out.println(Arrays.toString(n));});
                    if(this.satSolve(this.knowledgeBase)){
                        next = nachbarn[i];
                        this.knowledgeBase.remove(this.knowledgeBase.size()-1);
                        break;
                    }
                    this.knowledgeBase.remove(this.knowledgeBase.size()-1);
                }
                System.out.println(next);
                int[] erg = this.toCoordinate(next); // TODO: pass values of the field which should be uncovered next
                x = erg[0];
                y = erg[1];
            }


            if (displayActivated) {
                System.out.println("Uncovering (" + x + "," + y + ")");
            }

            feedback = field.uncover(x, y);
            uncovered[x][y] = feedback;

			/*for (int i = 0; i < uncovered.length; i++) {
				for (int j = 0; j < uncovered[i].length ; j++) {
					System.out.println("(" + i + ", " + j + ") = " + uncovered[i][j]);
				}
			}*/


        } while (feedback >= 0 && !field.solved());

        if (field.solved()) {
            if (displayActivated) {
                System.out.println("Solved the field");
            }
            return true;
        } else {
            if (displayActivated) {
                System.out.println("BOOM!");
            }
            return false;
        }
    }

    /**
     * uncovers all neighboring fields of the current field
     *
     * @param x x-Coordinate of current field.
     * @param y y-Coordinate of current field.
     */
    public void uncoverAll(int x, int y) {
        // TODO
    }

    /*
     * Solves al formula.
     *
     * @param numMines number of mines mentioned at current field.
     * @return
     */
	/*
	public int[] sat(int x, int y, int numMines){
		final int MAXVAR = 1000000;
		final int NBCLAUSES = 55;

		ISolver solver = SolverFactory.newDefault();

		// prepare the solver to accept MAXVAR variables. MANDATORY for MAXSAT solving
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);
		try {
			solver.addClause(new VecInt(new int[] {-1}));
		} catch (ContradictionException e) {
			e.printStackTrace();
		}

		IProblem problem = solver;
		int res[] = new int[anzNachbarn(x, y)];
		try {
			res = problem.findModel();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		int next = 0;
		for(int i = 0; i < res.length ; i++){
			if(res[i] > 0){
				next = res[i];
				break;
			}
		}
		// next field e 1..8
		// uncover 1: x-1, y-1		2: x, y-1		3: x+1, y-1
		// uncover 4: x-1, y		current			5: x+1, y
		// uncover 6: x-1, y+1		7: x, y+1		8: x+1, y+1
		switch(next){
			case 1: x--; y--; break;
			case 2: y--; break;
			case 3: x++; y--; break;
			case 4: x--; break;
			case 5: x++; break;
			case 6: x--; y++; break;
			case 7: y++; break;
			case 8: x++; y++;
		}
		return new int[] {x, y};
	}
	*/
    public boolean satSolve(ArrayList<int[]> input) {
        final int MAXVAR = 500;
        final int NBCLAUSES = input.size();

        ISolver solver = SolverFactory.newDefault();

        // prepare the solver to accept MAXVAR variables. MANDATORY for MAXSAT solving
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);
        // Feed the solver using Dimacs format, using arrays of int
        // (best option to avoid dependencies on SAT4J IVecInt)
        for (int i = 0; i < input.size(); i++) {
            int[] clause = input.get(i);// get the clause from somewhere
            // the clause should not contain a 0, only integer (positive or negative)
            // with absolute values less or equal to MAXVAR
            // e.g. int [] clause = {1, -3, 7}; is fine
            // while int [] clause = {1, -3, 7, 0}; is not fine
            try {
                solver.addClause(new VecInt(clause)); // adapt Array to IVecInt
            } catch (ContradictionException e) {
				System.out.println("contradiction detected, returning false");
				return false;
            }
        }

        // we are done. Working now on the IProblem interface
        IProblem problem = solver;
        try {
            if (problem.isSatisfiable()) {
                System.out.println("Modell gefunden: " + Arrays.toString(solver.model()));

                return true;
            } else {
                return false;
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * calculates the number of neighboring fields of the current field.
     *
     * @param x x-Coordinate of current field.
     * @param y y-Coordinate of current field.
     * @return number of neighboring fields
     */
    public int anzNachbarn(int x, int y) {
        int res = 8;
        int maxRow = field.getNumOfRows() - 1;
        int maxCol = field.getNumOfCols() - 1;

        if (x == 0 || x == maxCol || y == 0 || y == maxRow) {
            res -= 3;
            if (y == 0 && x == 0 || y == 0 && x == maxCol || x == 0 && y == maxRow || x == maxCol && y == maxRow) {
                res -= 2;
            }
            return res;
        }
        return res;
    }

    public void activateDisplay() {
        this.displayActivated = true;

    }

    public void deactivateDisplay() {
        this.displayActivated = false;
    }

    /**
     * calculates the al formula for the sat solver.
     * // * @param arr array with neighboring fields
     *
     * @param n number of neighbors
     * @param l Literal
     * @param x x-Coordinate of current field.
     * @param y y-Coordinate of current field.
     * @return al formula
     */
    public int[] getClauses(int n, int l, int x, int y) {
        System.out.println("Feld: " + x + ", " + y);
        int[] arr;
        int cols = this.field.getNumOfCols();

        switch (n) {
            case 3:
                if (x == 0 && y == 0) {    // top left
                    arr = new int[]{l + 1, l + cols, l + cols + 1};
                } else if (x == this.field.getNumOfCols() - 1 && y == 0) {    // top right
                    arr = new int[]{l - 1, l + cols, l + cols - 1};
                } else if (y == this.field.getNumOfRows() - 1 && x == 0) {    // bottom left
                    arr = new int[]{l - cols, l - cols + 1, l + 1};
                } else {    // bottom right
                    arr = new int[]{l - cols, l - cols - 1, l - 1};
                }
                break;
            case 5:
                if (x == 0) {    //left border
                    arr = new int[]{l - cols, l - cols + 1, l + 1, l + cols, l + cols + 1};
                } else if (y == 0) {    //top border
                    arr = new int[]{l - 1, l + 1, l + cols - 1, l + cols, l + cols + 1};
                } else if (x == this.field.getNumOfCols() - 1) {        //right border
                    arr = new int[]{l - cols - 1, l - cols, l - 1, l + cols - 1, l + cols};
                } else { //y == this.field.getNumOfRows()-1	//bottom border
                    arr = new int[]{l - 1, l - cols - 1, l - cols, l - cols + 1, l + 1};
                }
                break;
            default:
                arr = new int[]{l + 1, l - 1, l + cols, l + cols + 1, l + cols - 1, l - cols, l - cols + 1, l - cols - 1};
                break;
        }
        return arr;
    }

    /**
     * Updates the KB with newly discovered clauses.
     *
     * @param x        x-Coordinate of current field.
     * @param y        y-Coordinate of current field.
     * @param anzMinen number of mines surrounding the current field.
     */
    public void updateKnowledgeBase(int x, int y, int anzMinen) {
        int literal = toLiteral(x,y);
        knowledgeBase.add(new int[]{-1 * literal});
        int[] arr;
        if (x == 0 && y == 0 || x == field.getNumOfCols() - 1 && y == 0 || x == 0 && y == field.getNumOfRows() - 1 ||    //Corner fields
                x == field.getNumOfCols() - 1 && y == field.getNumOfRows() - 1) {
            arr = getClauses(3, literal, x, y);
        } else if (x == 0 || y == 0 || x == field.getNumOfCols() - 1 || y == field.getNumOfRows() - 1) { // Edge fields but no corner fields
            arr = getClauses(5, literal, x, y);
        } else { // alle anderen
            arr = getClauses(8, literal, x, y);
        }

        for (int i = 1; i < arr.length + 1; i++) {
            if (i != anzMinen) {
                knowledgeBase.addAll(api.Permutation.computeClauses(arr, arr.length, i));
            }
        }
        if (anzMinen != 0) {
            knowledgeBase.add(arr);
        }
    }

    /**
     * For given coordinates, returns literal value of a field
     * @param x x coordinate
     * @param y y coordinate
     * @return literal value
     */
    public int toLiteral(int x, int y){
        return y * this.field.getNumOfCols() + x + 1;
    }

    public int[] toCoordinate(int literal){
        int[] res = new int[2];
        res[1] = (literal - 1) / this.field.getNumOfCols();
        res[0] = literal - res[1] * this.field.getNumOfCols() - 1;
        return res;
    }
}
