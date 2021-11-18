package api;

import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.Random;

/**
 * This agent uncovers positions randomly. It obviously does not use a SAT
 * solver.
 * You can do whatever you want with this class.
 */
public class RandomMSAgent extends MSAgent {

	private Random rand;
	private boolean displayActivated = false;
	private boolean firstDecision = true;

	public RandomMSAgent(MSField field) {
		super(field);
		this.rand = new Random();
	}

	@Override
	public boolean solve() {

		int numOfRows = this.field.getNumOfRows();
		int numOfCols = this.field.getNumOfCols();
		int x = 0, y = 0, feedback = 0;

		do {
			if (displayActivated) {
				System.out.println(field);
			}
			if (firstDecision) {
				firstDecision = false;
			} /*else if(feedback == 0){			// no neighbor contains a mine
				// TODO uncover all neighbors
				// uncoverAll(x, y);
				// uncover x-1, y-1		x, y-1		x+1, y-1
				// uncover x-1, y		current		x+1, y
				// uncover x-1, y+1		x, y+1		x+1, y+1
			}*/
			else {//if(feedback != 0 && feedback != anzNachbarn(x, y)){	// mines are present in the neighborhood
				// TODO: something cool
				//crateFormula;
				System.out.println("ready");
				int erg[][] = getClauses(anzNachbarn(x, y), feedback, x, y);
				for(int i = 0; i < erg.length; i++){        //fill array with valid values
					for(int j = 0; j < erg[i].length; j++){
						System.out.print(erg[i][j] + " ");
					}
					System.out.println();
				}
				System.out.println("fertig");
				//use SAT solver
						//x = rand.nextInt(numOfCols);
						//y = rand.nextInt(numOfRows);
				//int[] erg = sat(x,y ,feedback); // TODO: pass values of the field which should be uncovered next
				//x = erg[0];
				//y = erg[1];
			}/* else {							// all neighbors contain a mine, no more move is possible.
				// TODO: end Game
			}*/

			if (displayActivated)
				System.out.println("Uncovering (" + x + "," + y + ")");
			feedback = field.uncover(x, y);

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
	 * @param x x-Coordinate of current field.
	 * @param y y-Coordinate of current field.
	 */
	public void uncoverAll(int x, int y){

	}

	/**
	 * Solves al formula.
	 * @param numMines number of mines mentioned at current field.
	 * @return
	 */
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

	/**
	 * calculates the number of neighboring fields of the current field.
	 * @param x x-Coordinate of current field.
	 * @param y y-Coordinate of current field.
	 * @return number of neighboring fields
	 */
	public int anzNachbarn(int x, int y){
		int res = 8;
		int maxRow = field.getNumOfRows()-1;
		int maxCol = field.getNumOfCols()-1;

		if(x == 0 || x == maxCol){
			res -= 3;
			if(y == 0 || y == maxRow){
				res -= 2;
			}
			return res;
		}

		if(y == 0 || y == maxRow){
			res -= 3;
			if(x == 0 || x == maxCol){
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
	// * @param arr array with neighboring fields
	 * @param n  number of neighbors
	 * @param anzMinen number of mines
	 * @param x x-Coordinate of current field.
	 * @param y y-Coordinate of current field.
	 * @return al formula
	 */
	public int[][] getClauses(int n, int anzMinen, int x, int y){
		System.out.println("Los geht's");
		int[] arr;
		int rows = 0;
		for(int i = 0; i <= n; i++){
			if(i == anzMinen){
				continue;
			}
			rows += fac(n) / (fac(i)*(fac(n-i))); // (n!)/(n*(n-k)!)
		}
		System.out.println(rows);
		int[][] res = new int[rows][n];     //result array for formula

		switch(n){
			case 3:
				if (x == 0 && y == 0){
					arr = new int[] {5, 7, 8};
				}
				else if(x == this.field.getNumOfCols()-1 && y == 0){
					arr = new int[] {4, 6, 7};
				}
				else if (y == this.field.getNumOfRows()-1 && x == 0){
					arr = new int[] {2, 3, 5};
				}
				else{
					arr = new int[] {1, 2, 4};
				}
			case 5:
				if (x == 0){
					arr = new int[] {4, 5, 6, 7, 8};
				}
				else if (y == 0){
					arr = new int[] {2, 3, 5, 7, 8};
				}
				else if(x == this.field.getNumOfCols()-1) {
					arr = new int[] {1, 2, 4, 6, 7};
				}
				else{ //y == this.field.getNumOfRows()-1
					arr = new int[] {1, 2, 3, 4, 5};
				}
			default:
				arr = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
		}

		for(int i = 0; i < rows; i++){        //fill array with valid values
			for(int j = 0; j < n; j++){
					res[i][j] = arr[j];
			}
		}
		return res;
	}

	public static void main(String[] args) {
		MSField f = new MSField("fields/" + "baby1-3x3-0.txt");
		RandomMSAgent r = new RandomMSAgent(f);
		//System.out.println(r.anzNachbarn(1,1));
		r.solve();

	}
}
