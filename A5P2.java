import java.lang.Thread;
import java.lang.System;
import java.util.ArrayList;
import java.util.Objects;

class PrimeFinder extends Thread {

    static ArrayList<Thread> running;
    private final String thread_name;
    private final int min;
    private final int max;
    public static PrimeNumbers a;
    private Object l = new Object();


    public static void main (String [] args) {
        if (args.length != 3){
            System.out.println("Usage: java PrimeFinder <min> <max> <thread_count>");
            System.exit(0);
        }
        int lower = Integer.parseInt(args[0]);
        int upper = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);
        if (upper<lower){
            System.out.println("The argument <max> must be greater than <min>");
            System.exit(0);
        }
        if (count<1){
            System.out.println("The argument <thread_count> must be greater than 1");
            System.exit(0);
        }
        int dif = upper - lower;
        if (dif<count){
            count=dif;
        }
        int extra = dif%count;
        int inc = (dif-extra)/count;
        a = new PrimeNumbers(lower, upper);
        System.out.println();
        for (int i = lower; i <= upper; i++) {
            int temp = i+inc;
            if (extra>0){
                extra--;
                temp++;
            }
            running.add(new PrimeFinder(i + " - " +temp,i,temp));
            System.out.println("Thread "+(running.size()-1)+" searching range ["+i+", "+temp+"]");
            i = temp;
        }
        System.out.println();
        boolean isRunning = true;
        while (isRunning){
            isRunning = false;
            for (Thread thread : running) {
                isRunning = isRunning || thread.isAlive();
            }
        }
        System.out.println("Main Thread: All workers finished. Primes found:");
        a.sort();
        int u = 0;
        for (int i : a.getPrimeNumbers()){
            u++;
            System.out.print(i);
            if (u != a.getLength()){
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.println("Main Thread: "+a.getLength()+" prime numbers found.");
    }

    PrimeFinder (String thread_name, int min, int max) {
        this.thread_name = thread_name;
        this.min = min;
        this.max = max;
    }


    public void run () {
        for (int i = this.min; i <= this.max; i++) {
            if (is_prime(i)){
                synchronized (l){
                    a.addPrimeNumbers(i);
                }
            }
        }
    }

    static class PrimeNumbers {
        private int max;
        private int min;
        private ArrayList<Integer> primeNumbers;

        public PrimeNumbers(int a, int b){
            this.min = a;
            this.max = b;
            primeNumbers = new ArrayList<>();
        }

        public void addPrimeNumbers(int i) {
            this.primeNumbers.add(i);
        }

        public ArrayList<Integer> getPrimeNumbers() {
            return this.primeNumbers;
        }

        public int getLength(){
            return this.primeNumbers.size();
        }

        public void sort() {
            this.primeNumbers.sort(null);
        }
    }

    boolean is_prime(int num) {
        if (num < 2) return false;
        for (int NUM_OF_CHILD = 2; NUM_OF_CHILD <= Math.sqrt(num); NUM_OF_CHILD++) {
            if (num % NUM_OF_CHILD == 0) return false;
        }
        return true;
    }
}
