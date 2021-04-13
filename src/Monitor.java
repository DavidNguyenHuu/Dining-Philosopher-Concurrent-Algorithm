public class Monitor<i>
{
	private boolean[] chopsticks;
	private static boolean isTalking=false;

	public Monitor(int piNumberOfPhilosophers) //Done
	
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		//If there's only 1 philosopher, then we need 2 chopsticks.
		if(piNumberOfPhilosophers==1){
			chopsticks=new boolean[piNumberOfPhilosophers+1];
			System.out.println("only one");
		}
		//If there's more than 1 philosopher, then we need the appropriate amount of chopsticks.
		else{
			chopsticks = new boolean[piNumberOfPhilosophers];
		}
		//System.out.println("Monitor - DEBUG");
		//Initialize all chopsticks to false
		for (int i = 0; i < chopsticks.length; i++) {
			chopsticks[i]=false;
			System.out.println("Chopstick "+(i+1)+" = "+chopsticks[i]);

		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) throws InterruptedException
	{

		//Case for only 1 philosopher
		if(chopsticks.length==1){

			//Right chopstick = true
			System.out.println("Picked up right chopsticks"+piTID);
			chopsticks[piTID]=true;
			System.out.println("Picked up left chopsticks"+piTID);
			//Left chopstick = true
			chopsticks[piTID-1]=true;
			//Philosopher may pickup as 2 of the chopsticks are available.
		}

		else{
			//Case for more than 1, if the amount of chopsticks and ID are equal, then philosopher can't pick up.
			if(chopsticks.length!=piTID){
				//If the chopsticks (left or right, since he needs both) are picked up, then philosopher must wait. HERE
				while(chopsticks[piTID]||chopsticks[piTID-1]){
					wait();
				}
				//If both chopsticks are free, then the philosopher can pick up the 2 chopsticks
				if (!(chopsticks[piTID] || chopsticks[piTID-1])) {
					System.out.println("Picked up right chopsticks"+piTID);
					chopsticks[piTID] = true;
					System.out.println("Picked up left chopsticks"+piTID);
					chopsticks[piTID - 1] = true;
				}
			}
			//In case the philosopher is last in the array.
			else{
				while(chopsticks[0]||chopsticks[piTID-1]){
					wait();
				}
				if(!(chopsticks[0]&&chopsticks[piTID-1])){
					System.out.println("Picked up right chopsticks"+piTID);
					System.out.println("Picked up left chopsticks"+piTID);
					chopsticks[0]=true;
					chopsticks[piTID-1]=true;
				}
			}
		}

	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// Case for 1 philosopher , puts down both chopsticks. Making both left and right available.
		if (chopsticks.length == 1) {

			chopsticks[piTID] = false;
			chopsticks[piTID-1] = false;
		}
		else {
			// If more than one philosopher, checks if the ID is not the same as the number of sticks
			if(chopsticks.length!=piTID) {
				System.out.println("Put down right chopsticks "+piTID);
				System.out.println("Picked up left chopsticks "+piTID);
				chopsticks[piTID] = false;
				chopsticks[piTID-1] = false;
			}
			else {
				chopsticks[0] = false;
				chopsticks[piTID-1] = false;
			}
		}
		//Lets all other philosophers know that he's done eating.
		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID) throws InterruptedException
	{
		
		if (chopsticks.length!=1){
			while(isTalking==true){
				wait();
			}
			isTalking=true;
		}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk() //Done
	{
		//Notifies other philosopher can talk and the current philosopher stops talking.
		isTalking=false;
		notifyAll();
	}
}

// EOF
