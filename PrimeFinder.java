import java.lang.Thread;
import java.lang.System;
import java.util.ArrayList;

class PrimeFinder extends Thread {

    //holds all threads
    static ArrayList<Thread> running;
    //thread name
    private final String thread_name;
    //min of thread
    private final int min;
    //max of thread
    private final int max;
    //object that holds threads
    public static PrimeNumbers primes;
    //object used to synchronize the threads on to prevent race conditions
    private static final Object l = new Object();


    public static void main (String [] args) {
        //makes sure enough arguments are given, otherwise ends
        if (args.length != 3){
            System.out.println("Usage: java PrimeFinder <min> <max> <thread_count>");
            System.exit(0);
        }
        //gets the arguments from run command
        int lower = Integer.parseInt(args[0]);
        int upper = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);
        //makes sure upper is less than lower, otherwise errors
        if (upper<lower){
            System.out.println("The argument <max> must be greater than <min>");
            System.exit(0);
        }
        //makes sure at least one thread is used, otherwise errors
        if (count<1){
            System.out.println("The argument <thread_count> must be greater than 1");
            System.exit(0);
        }
        //calculates difference from upper and lower, adding one to make inclusive
        int dif = (upper - lower)+1;
        //decreases count if amount of threads is greater than the numbers to check
        if (dif<count){
            count=dif;
        }
        //calculates if dif can be evenly divided across threads, gets remainder otherwise
        int extra = dif%count;
        //gets how many per thread excluding the remainder
        int inc = (dif-extra)/count;
        //initializes running
        running = new ArrayList<>();
        //creates the object holding Prime Numbers
        primes = new PrimeNumbers(lower, upper);
        System.out.println();
        //creates the lower that will move to set range of each thread
        int movingLower = lower;
        //loops to create all threads, supplying range to each and starting them
        for (int i = 0; i < count; i++) {
            //creates and sets the upper used to create the current thread, subtracts one to make inclusive
            int movingUpper = movingLower+inc-1;
            //Distributes the remainder by adding 1 to the current range upper, then subtracts 1 from extra to track where to add remainder
            if (extra>0){
                extra--;
                movingUpper++;
            }
            //creates thread then adds it to running
            running.add(new PrimeFinder(movingLower + " - " +movingUpper,movingLower,movingUpper));
            //starts the newly created thread
            running.getLast().start();
            //prints that thread has started and the range its checking
            System.out.println("Thread "+(running.size()-1)+" searching range ["+movingLower+", "+movingUpper+"]");
            //updates the moving lower, adding 1 to set the inclusive lower
            movingLower = movingUpper+1;
        }
        //prints line to match example
        System.out.println();
        //loops through each thread, using join to pause main until thread terminates.
        //doesn't care the order the threads terminate
        for (int i = 0; i < running.size(); i++) {
            try {
                running.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Main Thread: All workers finished. Primes found:");
        //sorts primes
        primes.sort();
        //just exists to not add an additional space where not needed
        int u = 0;
        //loops through primes, printing them on the same line
        for (int i : primes.getPrimeNumbers()){
            u++;
            System.out.print(i);
            if (u != primes.getLength()){
                System.out.print(" ");
            }
        }
        //prints number of primes
        System.out.println();
        System.out.println("Main Thread: "+primes.getLength()+" prime numbers found.");
    }

    //thread constructor
    PrimeFinder (String thread_name, int min, int max) {
        this.thread_name = thread_name;
        this.min = min;
        this.max = max;
    }

    //declares what each thread will run
    public void run () {
        //loops through each number in range
        for (int i = this.min; i <= this.max; i++) {
            //calls is_prime to check if prime
            if (is_prime(i)){
                //this prevents race conditions, by using object l as a monitor
                //this prevents all instances that share this monitor from executing code associated to the monitor simultaneously
                //because each object has a lock, which only one thread can hold at a time
                synchronized (l){
                    primes.addPrimeNumbers(i);
                }
            }
        }
    }

    //object class for holding primes
    static class PrimeNumbers {
        private int max;
        private int min;
        private ArrayList<Integer> primeNumbers;

        //object constructor
        public PrimeNumbers(int primes, int b){
            this.min = primes;
            this.max = b;
            primeNumbers = new ArrayList<>();
        }

        //object method to add to the array
        public void addPrimeNumbers(int i) {
            this.primeNumbers.add(i);
        }

        //gets stored array
        public ArrayList<Integer> getPrimeNumbers() {
            return this.primeNumbers;
        }

        //gets length, easier than getting list then running size
        public int getLength(){
            return this.primeNumbers.size();
        }

        //sorts array
        public void sort() {
            this.primeNumbers.sort(null);
        }
    }

    //checks if number given is prime. checks if given number can be evenly divided by any number less than or equal to its square.
    boolean is_prime(int num) {
        if (num < 2) return false;
        for (int NUM_OF_CHILD = 2; NUM_OF_CHILD <= Math.sqrt(num); NUM_OF_CHILD++) {
            if (num % NUM_OF_CHILD == 0) return false;
        }
        return true;
    }
}
