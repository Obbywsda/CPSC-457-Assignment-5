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
        int lower = Integer.parseInt(args[0]);
        int upper = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);
        int dif = upper - lower;
        int extra = dif%count;
        int inc = (dif-extra)/count;
        a = new PrimeNumbers(lower, upper);

        

        for (int i = lower; i <= upper; i++) {
            int temp = i+inc;
            if (extra>0){
                extra--;
                temp++;
            }
            running.add(new PrimeFinder(i + " - " +temp,i,temp));
            i = temp;
            System.out.println();
        }
        boolean isRunning = true;
        while (isRunning){
            isRunning = false;
            for (Thread thread : running) {
                isRunning = isRunning || thread.isAlive();
            }
        }


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
    }

    boolean is_prime(int num) {
        if (num < 2) return false;
        for (int NUM_OF_CHILD = 2; NUM_OF_CHILD <= Math.sqrt(num); NUM_OF_CHILD++) {
            if (num % NUM_OF_CHILD == 0) return false;
        }
        return true;
    }
}
