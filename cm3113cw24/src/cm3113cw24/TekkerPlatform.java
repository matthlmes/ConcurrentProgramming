package cm3113cw24;

/**
 * CM3113 Session 2024-25 Starting point for Coursework
 * TekkerPlatform.java runs as a Thread and starts a specified number of Tekker tasks
 * that are associated with the specified Tawk 
 */

public class TekkerPlatform extends Thread {
    private Tawk tawk;
    private int numberTekkers;

    public TekkerPlatform(Tawk t, int n){
        this.tawk = t;
        this.numberTekkers = n;
    }

    @Override public void run(){
        for(int i = 0; i < numberTekkers; i++){
            Tekker t = new Tekker(tawk);
            tawk.addTekker(t);
        }
    }

}
