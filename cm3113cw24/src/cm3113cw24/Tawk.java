package cm3113cw24;

import java.util.Timer;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * CM3113 Session 2024-25 Starting point for Coursework
 * encapsulated properties of a TekkTawk session where viewers vote on products 
 * and offer comments
 */
public class Tawk extends Thread {

    private final String tawkName;
    private boolean running;                    /* Tawk thread run() method runs until running is set to false */
    private GUI gui;                            /* reference to the GUI object associated with this Tawk */
    private Timer guiTimer;                     /* Timer thread used by the Tawk to update the GUI */

    private ArrayList<Product> tawkProducts;    /* list of products the presenter of the Tawk is reviewing */
    private LinkedList<Comment> commentList;    /* list of comments awaiting processing */
    private ArrayList<Tekker> tekkers;          /* list of viewers watching the Tawk */

    /* constructs a new Tawk */
    public Tawk(String name, GUI gui) {
        this.setDaemon(true);
        this.tawkName = name;
        this.running = true;
        this.gui = gui;
        tawkProducts = new ArrayList<Product>();
        commentList = new LinkedList<Comment>();
        tekkers = new ArrayList<Tekker>();

        createTimer();
    }


    @Override public void run(){
        while(running){
            try { /* testing showed that a small pause is needed or else the list never gets emptied */
                Thread.sleep(1);
            } catch (InterruptedException e) {}
            /* read Comment from commentList and process */
            if(commentList.size() > 0) {
                Comment c = commentList.removeFirst();
                processComment(c);
            }        
        }
    }

    /* adds a comment to the commentList */
    public void makeComment(Comment c){
        commentList.addLast(c);
    }

    /* gets number of comments currently on the commentList */
    public int getNumberCommentsInList(){
        return commentList.size();
    }
    
    /* method to process a comment by adding to list of comments for the relevant Product
     * and updating GUI to indicate comment has been received
     */
    public void processComment(Comment comment){
        comment.getProduct().addComment(comment.getTekker(), comment.getCommentText());
        gui.appendComments(comment.toString());
    }

    /* gets number of Tekker viewers currently watching the Tawk */
    public int getNumberConnectedTekkers(){
        return tekkers.size();
    }

    /* creates ans schedules a Timer thread that will regularly update the GUI */
    private void createTimer(){
        java.util.TimerTask task1 = new java.util.TimerTask(){
            @Override public void run(){
             gui.updateData();
            }
            };
        guiTimer = new java.util.Timer(true);
        guiTimer.schedule(task1, 0, 100);
    }

    public GUI getGUI(){
        return gui;
    }

    public String getTawkName() {
        return tawkName;
    }

    public boolean isTawkRunning() {
        return running;
    }

    public ArrayList<Product> getTawkProducts() {
        return tawkProducts;
    }

    /* returns formatted list of current products */
    public String getTawkProductsList() {
        String message = "";
        for(Product p: tawkProducts){
            message += p.toString() + "\n";
        }
        return message;
    }

    /* adds Product to tawkProduct product list */
    public void addProduct(Product p){
        tawkProducts.add(p);
    }

    /* adds Tekker to tekker list and starts Tekker thread */
    public void addTekker(Tekker t){
        tekkers.add(t);
        new Thread(t).start();
    }

     /* disconnects viewers and stops Tawk thread*/
     public void stopTheTawk() {
        disconnectTekkers();
        this.stop();
    }   

    /* disconnects viewers*/
    public void disconnectTekkers() {
        for(Tekker t: tekkers) t.disconnect();
    }

    /* method that returns reference to randomly selected product from product list*/
    public Product getRandomProduct(){
        return tawkProducts.get((int)Math.floor(tawkProducts.size()*Math.random()));
    }
   
    /* method that runs consistency checks on the concert data and returns results as a String*/
    public String doChecks(){
        String report = "";
        long totalLovesSent = 0, totalHatesSent = 0, totalCommentsSent = 0;
        for(Tekker t: tekkers){
            totalLovesSent += t.getNumberLoves();
            totalHatesSent += t.getNumberHates();
            totalCommentsSent += t.getNumberComments();
        }

        report = "Tekkers sent        : " + totalLovesSent + " Loves,  " + totalHatesSent + " Hates,  " + totalCommentsSent + " Comments" + "\n";
        
        long totalLovesRecv = 0, totalHatesRecv = 0, totalCommentsRecv = 0;
        for(Product p: tawkProducts){
            totalLovesRecv += p.numberLove();
            totalHatesRecv += p.numberHate();
            totalCommentsRecv += p.numberComments();
        }

        report += "Products received: " + totalLovesRecv + " Loves,  " + totalHatesRecv + " Hates,  " + totalCommentsRecv + " Comments";
   
        return report;

    }
}
