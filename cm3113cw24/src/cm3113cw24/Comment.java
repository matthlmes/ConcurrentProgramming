package cm3113cw24;
/**
 * Comment.java Starting point code for CM3113 Coursework 2024-25
 * Comment class encapsulates a string comment with a Product and Tekker
 * @author DAVID
 */
public class Comment {
    private String commentText;
    private Product product;
    private Tekker tekker;

    public Comment(String s, Product p, Tekker t){
        commentText = s;
        product = p;
        tekker = t;
    }

    public String getCommentText(){
        return commentText;
    }

    public Product getProduct(){
        return product;
    }

    public Tekker getTekker(){
        return tekker;
    }

    public String toString(){
        return tekker.getID() + " commented " + commentText + " about " + product.getProductName();
    }
}
