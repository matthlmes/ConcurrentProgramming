package cm3113cw24;

import java.util.HashMap;
/**
 * CM3113 Session 2024-25 Starting point for Coursework
 * Product.java encapsulates properties of product being demonstrated on TekTawk video stream
 */
public class Product {
    private String productName;
    private HashMap<String, Tekker> comments;
    private long loves;
    private long hates;
    private double averageRating;

    public Product(String name){
        this.productName = name;
        this.comments = new HashMap<String, Tekker>();
        this.loves = 0;
        this.hates = 0;
        this.averageRating = 80.0*Math.random() + 20.0; /* random rating between 20 and 100 */
    }

    @Override
    public String toString() {
        return this.productName + " Loves: " + loves + " Hates: " + hates;
    }

    public void addComment(Tekker viewer, String comment){
        comments.put(comment, viewer);
    }

    public int numberComments(){
        return comments.keySet().size();
    }

    public void addLove(){
        loves++;
    }

    public void addHate(){
        hates++;
    }

    public long numberLove(){
        return loves;
    }

    public long numberHate(){
        return hates;
    }

    public String getProductName() {
        return this.productName;
    }

    public double getAverageRating(){
        return averageRating;
    }
       
}
