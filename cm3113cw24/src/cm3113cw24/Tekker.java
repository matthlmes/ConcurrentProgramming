package cm3113cw24;

/**
 * Tekker.java Starting point code for CM3113 Coursework 2024-25
 * Tekker captures the properties of a viewer watching the TekTawk and making suggestions
 * @author DAVID
 */
public class Tekker implements Runnable{
    
    /* object level data relating to individual Viewer of the Tawk */
    private final String tekkerID;  
    private int pause;              /* millisecond delay between Tekker actions in the run() method */
    private Tawk tawk;              /* reference to Tawk that Tekker is viewing */
    private boolean disconnected;   /* boolean that when set to true should cause run() to finish  */
    private int numberLoves;        /* number of upvotes sent to tawk by this Tekker */ 
    private int numberHates;        /* number of downvotes sent to tawk by this Tekker */ 
    private int numberCommentsMade; /* number of comments sent to tawk by this Tekker */ 
    /* shared data relating to all viewers of the Tawk */
    private static int averagePause = 100; /* average pause in milliseconds between actions */
      
    public Tekker(Tawk t) {
        this.tekkerID = getRandomName();
        this.pause = averagePause + (int)Math.floor(4*Math.random()); /* Tekkers have small random variation in pause */
        this.tawk = t;
        this.disconnected = false; /* Tekker should run until disconnected is set to true */
    }

    public void run(){
        while(!disconnected){
            try {
                Thread.sleep(pause);
            } catch (InterruptedException e) {/* ignore */ }
            Product p = tawk.getRandomProduct();   /* randomly select a current Product from Tawk */
            if(Math.random() < 0.05){              /* 5% of times tekker will add a comment */
                tawk.makeComment(new Comment(tekkerID+"-"+(++numberCommentsMade), p, this));
            } else{                                /* otherwise just vote randomly up or down */
                if(100.0*Math.random() > p.getAverageRating()){
                    p.addHate(); numberHates++;
                }
                else{p.addLove(); numberLoves++;
                }
            }
        }
    }

    /* allows external thread to stop this Tekker's run() method */
    public void disconnect(){
        disconnected = true;
    }

    public String getID() {
        return this.tekkerID;
    }

    public long getNumberLoves() {
        return this.numberLoves;
    }

    public long getNumberHates() {
        return this.numberHates;
    }

    public long getNumberComments() {
        return this.numberCommentsMade;
    }

    /* class level method allowing average pause of Tekkers to be set */
    public static void setAveragePause(int ms){
        Tekker.averagePause = ms;
    }

    
    
    // class constant and method for generating unique IDs
    // 62x62x62x62x62 = 916,132,832 permutations should make chance of same ID negligible
    private static String digitsForID 
        = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static String getRandomName(){
        return ""+digitsForID.charAt((int)Math.floor(62*Math.random())) 
        + digitsForID.charAt((int)Math.floor(62*Math.random()))
        + digitsForID.charAt((int)Math.floor(62*Math.random()))
        + digitsForID.charAt((int)Math.floor(62*Math.random()))
        + digitsForID.charAt((int)Math.floor(62*Math.random()));
    }

    @Override
    public String toString() {
        return "Tekker ID:" + tekkerID;
    }

    /* main method only used for testing that random name method works as intended */
    public static void main(String[] args) {
        for(int i=0; i<100; i++) System.out.println(getRandomName());
    }
}
