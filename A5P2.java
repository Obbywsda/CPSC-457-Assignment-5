import java.lang.Thread;
import java.lang.System;
import java.util.ArrayList;

class ThreadExample extends Thread {

    static ArrayList<Thread> running;
    private final String thread_name;
    private final long sleep_duration;
    private final Object min;
    private final Object max;



    public static void main (String [] args) {
        int lower = Integer.parseInt(args[0]);
        int upper = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);
        int dif = upper - lower;
        int extra = dif%count;
        int inc = (dif-extra)/count;
        PrimeNumbers a = new PrimeNumbers();

        for (int i = lower; i < upper; i++) {
            int temp = i+inc;
            if (extra>0){
                extra--;
                temp++;
            }
            running.add(new ThreadExample(i + " - " +temp,1000,i,temp));
            i = temp;

        }

    }

    ThreadExample (String thread_name, long sleep_duration, int min, int max) {

        this.thread_name = thread_name;
        this.sleep_duration = sleep_duration;
        this.min = min;
        this.max = max;
    }


    public void run () {
        try {
            this.sleep (this.sleep_duration);
        }
        catch (Exception e) {
        }
        for (int i = this.min; i < this.max; i++) {
            
        }

    }

    static class PrimeNumbers {
        ArrayList<Integer> primeNumbers;


    }
    
    boolean is_prime(int num) {
        if (num < 2) return false;
        for (int NUM_OF_CHILD = 2; NUM_OF_CHILD <= Math.sqrt(num); NUM_OF_CHILD++) {
            if (num % NUM_OF_CHILD == 0) return false;
        }
        return true;
    }


}
