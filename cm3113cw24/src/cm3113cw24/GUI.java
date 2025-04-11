
package cm3113cw24;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * CM3113 Session 2024-25 Starting point for Coursework
 * GUI.java runs as a JavaFX application that manages a Tawk object and its concurrent opration 
 * with a number of Tekker objects
 * @author DAVID
 */
public class GUI extends Application {

    private Tawk tawk;                    /* Tawk associated with this GUI */
    ArrayList<TekkerPlatform> portals;    /* list of poertals that can start viewer threads */

    // variables for display/user-entry in TextFields
    private String tawkTitle;
    private int numberPortals;
    private int numberTekkersPerPortal;
    private int pauseTimeMillis;
    private long MaxThreadsUsed;
    private DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("HH:mm:ss  dd-MM-yy");
    
    /* startTawk() resets counts, reads settings for new Tawk
    initialises new Tawk and user-selected number of portals and viewers per portal then starts threads*/
    private void startTawk() {
        MaxThreadsUsed = 0;
        
        // read info from GUI text fields and construct and start new Tawk based on values
        readSettings();
        Tekker.setAveragePause(pauseTimeMillis);
        tawk = new Tawk(tawkTitle, this);
        addProduct();
        tawk.start();
           
        // start Tekker threads via TekkerPortals
        for(int i = 0; i < numberPortals; i++){
            new TekkerPlatform(tawk, numberTekkersPerPortal).start();
        }

        reverseButtons();
        // one off updates
        updateData();  // replace by periodic updates at period 0.1s
        updateTime(); // replace by periodic updates at period 1s

    }
        
    /* updateGUI() updates Tawk stats  */
    public void updateData() {

        int numThreads = Thread.activeCount();
        if (numThreads > MaxThreadsUsed) { MaxThreadsUsed = numThreads; }
        textThreadsRunning.setText("Current= " + numThreads + "   Max= " + MaxThreadsUsed);
        textNumberComments.setText("" + tawk.getNumberCommentsInList());
        textNumberTekkersViewing.setText("" + tawk.getNumberConnectedTekkers());

        areaProducts.setText(tawk.getTawkProductsList());
    }

    /* doChecks() adds results from concert ticket allocation checks to textHistory text area */
    public void updateChecks() {
        String s = tawk.doChecks();
        this.areaChecks.setText(s);
    }

    /* doChecks() adds results from concert ticket allocation checks to textHistory text area */
    public void appendComments(String s) {
        this.areaComments.setText(s);
    }

    /* stopTawk() stops the Tawk thread */
    public void stopTawk() {
        tawk.stopTheTawk();
        reverseButtons();
    }

    // makes Stop, Start, and AddProduct buttons visible/invisible
    private void reverseButtons(){
        buttonAddProduct.setVisible(!buttonAddProduct.isVisible());
        buttonStop.setVisible(!buttonStop.isVisible());
        buttonStart.setVisible(!buttonStart.isVisible());
    }

    /* updateTime() updates current time  */
    public void updateTime() {
        this.textCurrentTime.setText(LocalDateTime.now().format(timeformat));
    }

    /* addToComments() adds a String to the TextArea  */
    public void addToComments(String s) {
        areaComments.appendText("\n" + s);
    }

    /* addProduct() adds Product to Tawk product list, and updates GUI with next default product name */
    public void addProduct() {
        String s = this.textProductName.getText();
        tawk.addProduct(new Product(s));
        textProductName.setText("Product " + (tawk.getTawkProducts().size() + 1));
    }


    /* readSettings() reads user selected values from GUItext fields  */
    public void readSettings(){
        tawkTitle = this.textTawkName.getText();
        numberPortals = Integer.parseInt(this.textNumberPortals.getText());
        numberTekkersPerPortal = Integer.parseInt(this.textNumberTekkerPerPortal.getText());
        pauseTimeMillis = Integer.parseInt(this.textPauseMillis.getText());
    }


    /* ******************************************************************************** */
    /* You should not need to change any code below this point */
    /* ******************************************************************************** */

    // named GUI components
    private TextField textTawkName, textProductName;
    private TextField textNumberPortals, textNumberTekkerPerPortal, textPauseMillis; 
    private TextField textCurrentTime, textThreadsRunning, textNumberTekkersViewing,textNumberComments;
    private Button buttonStart, buttonStop, buttonCheck, buttonAddProduct;
    private TextArea areaProducts, areaComments, areaChecks;

    @Override
    public void start(Stage stage) {
        setUpGUI(stage);
    }

    // setting up GUI components
    private void setUpGUI(Stage stage) {
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 800, 600);

        GridPane grid = new GridPane();
        root.add(grid, 0, 0);

        grid.add(new Label("Tawk Controls"), 0, 0, 2, 1);
        
        GridPane tawkInfoPane = new GridPane();
        grid.add(tawkInfoPane, 0, 1);

        // set-up buttons
        buttonStart = new Button("Start Tawk");
        buttonStop = new Button("Stop Tawk");  buttonStop.setVisible(false);
        buttonCheck = new Button("Run Checks");
        buttonAddProduct = new Button("Add Product"); buttonAddProduct.setVisible(false);

        // GUI text labels
        tawkInfoPane.add(new Label("Tawk Name"), 0, 1);
        textTawkName = new TextField("Latest Phones");
        tawkInfoPane.add(textTawkName, 1, 1);
        
        textProductName = new TextField("Product 1"); 
        tawkInfoPane.add(textProductName, 0, 2);
        tawkInfoPane.add(buttonAddProduct, 1, 2);
        tawkInfoPane.add(buttonStart, 0, 3);
        tawkInfoPane.add(buttonStop, 1, 3);

        grid.add(new Label(""), 0, 2);
        grid.add(new Label("Tawk Settings (these can be changed before Starting)"), 0, 3);
        GridPane tawkSettingPane = new GridPane();

        grid.add(tawkSettingPane, 0, 4);
          
        tawkSettingPane.add(new Label("Number Portals"), 0, 1);
        textNumberPortals = new TextField("1");
        tawkSettingPane.add(textNumberPortals, 1, 1);
        
        tawkSettingPane.add(new Label("Tekkers Per Portal"), 0, 2);
        textNumberTekkerPerPortal = new TextField("20");
        tawkSettingPane.add(textNumberTekkerPerPortal, 1, 2);

        tawkSettingPane.add(new Label("Action Interval (ms)"), 0, 3);
        textPauseMillis = new TextField("100"); 
        tawkSettingPane.add(textPauseMillis, 1, 3);

  
        grid.add(new Label(""), 0, 5);
        grid.add(new Label("Tawk Data"), 0, 6);
        GridPane tawkDataPane = new GridPane();

        grid.add(tawkDataPane, 0, 7);
        // text labels
        tawkDataPane.add(new Label("Current Time"), 0, 0);
        textCurrentTime = new TextField("");
        tawkDataPane.add(textCurrentTime, 1, 0);

        tawkDataPane.add(new Label("Threads Running"), 0, 1);
        textThreadsRunning = new TextField("");
        tawkDataPane.add(textThreadsRunning, 1, 1);

        tawkDataPane.add(new Label("Number Tekkers"), 0, 2);
        textNumberTekkersViewing = new TextField("");
        tawkDataPane.add(textNumberTekkersViewing, 1, 2);

        tawkDataPane.add(new Label("Number Comments Queued"), 0, 3);
        textNumberComments = new TextField("");
        tawkDataPane.add(textNumberComments, 1, 3);
   
        tawkDataPane.add(new Label("Products"), 0, 4);
        tawkDataPane.add(new Label("Comments"), 0, 5);
        tawkDataPane.add(buttonCheck, 0, 6);
        
        // GUI TextFields with initial values
        areaProducts = new TextArea("");
        tawkDataPane.add(areaProducts, 1, 4);
        
        areaComments = new TextArea("");
        tawkDataPane.add(areaComments, 1, 5);

        areaChecks = new TextArea("");
        tawkDataPane.add(areaChecks, 1, 6);

        //set up event handling for buttons
        buttonStart.setOnAction(
                e -> startTawk()
        );

        buttonStop.setOnAction(
                e -> stopTawk()
        );

        buttonCheck.setOnAction(
                e -> updateChecks()
        );

        buttonAddProduct.setOnAction(
                e -> {addProduct();}
        );

        /* add "scene" to "stage" and make visible*/
        stage.setTitle("CM3113 Coursework - TekkTawk App");
        stage.setScene(scene);
        stage.show();
    }

    /* launch GUI here */
    public static void main(String[] args) {
        Application.launch(args);
    }

}
