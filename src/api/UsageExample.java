package api;
/**
 * An example of how to use a MSAgent to solve the game.
 * You can do whatever you want with this class.
 */
public class UsageExample {

	/**
	 * Array containing the names of all fields. If you want to iterate over all of
	 * them, this might help
	 */
	public static final String[] fields = { "baby1-3x3-0.txt", "baby2-3x3-1.txt", "baby3-5x5-1.txt",
			"baby4-5x5-3.txt", "baby5-5x5-5.txt", "baby6-7x7-1.txt", "baby7-7x7-3.txt", "baby8-7x7-5.txt",
			"baby9-7x7-10.txt", "anfaenger1-9x9-10.txt", "anfaenger2-9x9-10.txt", "anfaenger3-9x9-10.txt",
			"anfaenger4-9x9-10.txt", "anfaenger5-9x9-10.txt", "fortgeschrittene1-16x16-40.txt", "fortgeschrittene2-16x16-40.txt",
			"fortgeschrittene3-16x16-40.txt", "fortgeschrittene4-16x16-40.txt", "fortgeschrittene5-16x16-40.txt",
			"profi1-30x16-99.txt", "profi2-30x16-99.txt", "profi3-30x16-99.txt", "profi4-30x16-99.txt",
			"profi5-30x16-99.txt" };

	public static void main(String[] args) {
		int k = 22;
		// use smaller numbers for larger fields
		int iterations = 100;

		int success = 0;
		long time = 0;
		long current = 0;
		for (int i = 0; i < iterations; i++) {
			System.out.println(i + ". Durchlauf");
			MSField f = new MSField("fields/" + fields[k]);
			MSAgent agent = new SatMSAgent(f);

			// to see what happens in the first iteration
			if (i == 0) {
				agent.activateDisplay();
			} else {
				agent.deactivateDisplay();
			}
			long start = System.currentTimeMillis();
			boolean solved = agent.solve();
			long end = System.currentTimeMillis();
			current = end - start;
			if (solved) {
				time += current;
				success++;
			}
		}
		if(success > 0) {
			time /= success;
		}
		double rate = (double) success / (double) iterations;
		System.out.println(fields[k]);
		System.out.println("Erfolgsquote: " + rate);
		System.out.println("Zeit (Schnitt): " + (double)time/1000 + "s");
	}
}
