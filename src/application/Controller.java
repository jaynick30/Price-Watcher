package application;

import URL.Parser;
import database.Manager;
import database.PriceGraphMaker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.Item;
import model.Point;
import model.PriceGraph;
import model.StringIterator;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
	@FXML
	VBox applicationBounds;
	@FXML
	TableView<HBox> tableView;
	@FXML
	ComboBox<?> categorySelection;
	@FXML
	Button addCategory, addURL, nameButton, priceButton, siteButton, startShopping, updateItems;
	@FXML
	TextField urlTextField;
	@FXML
	ListView<String> products;
	@FXML
	ListView<HBox> prices;
	@FXML
	ListView<Hyperlink> sites;
	
	public ObservableList<String> productList = FXCollections.observableArrayList();
	public ObservableList<HBox> priceList = FXCollections.observableArrayList();
	public ObservableList<Hyperlink> siteList = FXCollections.observableArrayList();

    private StringIterator iterator;
    private Parser parser = new Parser();
	private Manager itemBase;
	private ArrayList<Item> items;
    private String currentURL;
    private final WebView browser = new WebView();
    private final WebEngine webEngine = browser.getEngine();
	

	
	@FXML
	private void initialize() throws MalformedURLException{
		itemBase = new Manager("Items");
		itemBase.createTable();

        hideBrowser();
		
		products.setItems(productList);
		prices.setItems(priceList);
        sites.setItems(siteList);
		 
		products.setFixedCellSize(30);
		prices.setFixedCellSize(30);
		sites.setFixedCellSize(30);
        populateItems();
        updateItems();
	}
	
	private void updatePrice(Item item, String change, int index){
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		String newPrice = item.price;
		Text text = new Text();
		text.setText(newPrice);
		ImageView arrow = new ImageView();
		if(change.equals("same")){
			Image image = new Image("file:GrayLine.png");
			arrow.setImage(image);
			arrow.setFitHeight(3);
			arrow.setFitWidth(15);
		}
		else if(change.equals("up")){
			Image image = new Image("file:RedArrow.png");
			arrow.setImage(image);
			arrow.setFitHeight(15);
			arrow.setFitWidth(15);
		}
		else if(change.equals("down")){
			Image image = new Image("file:GreenArrow.png");
			arrow.setImage(image);
			arrow.setFitHeight(15);
			arrow.setFitWidth(15);
		}
		else{
			throwError("That price change does not exist!");
		}
		hbox.getChildren().addAll(text, arrow);
		priceList.add(index, hbox);
		if(item.shipping.isFree() == "1") {
			Text shipping = new Text(" + free shipping!");
	        hbox.getChildren().add(shipping);
	    }
	}
	
	private void watchItem(String url) throws MalformedURLException{
		try{
			Hyperlink hyper = createHyperlink(url);
            siteList.add(hyper);
			Parser parser = new Parser();
			Item item = new Item(url);
		
			item = parser.parse(url);
			String newProduct = item.title;
			if (!newProduct.equals(null)){
				String newPrice = item.price;
				itemBase.addItem(item);
		
				productList.add(newProduct);
				addPrice(item);
				siteList.add(hyper);
			}
		}
		catch(NullPointerException e){
			throwError("URLException");
		}
	}
	
	public void throwError(String errorType){
			VBox bounds = new VBox();
			bounds.setSpacing(10);
			HBox buttonBox = new HBox();
			buttonBox.setSpacing(20);
			bounds.setMinSize(100, 20);
			bounds.setAlignment(Pos.CENTER);
			buttonBox.setAlignment(Pos.CENTER);
			Scene scene = new Scene(bounds, 250, 75);
			final Stage stage = new Stage();
			stage.setTitle("error");
			Button okButton = new Button("Ok");
			Button cancelButton = new Button("cancel");
			okButton.setOnAction((e) -> {stage.close();});
			cancelButton.setOnAction((e) -> stage.close());

			Label errorMessage = new Label("Item not found");
			
			buttonBox.getChildren().addAll(okButton, cancelButton);
			bounds.getChildren().addAll(errorMessage, buttonBox);
	        VBox.setVgrow(bounds, Priority.ALWAYS);
	        
	        stage.setScene(scene);
	        stage.show();
	        System.out.println("Error thrown");
	}
	
	@FXML
	public void addItem(){
		String url = urlTextField.getText();
		Hyperlink hyper = createHyperlink(url);
        try {
            Item item = parser.parse(url);
            itemBase.addItem(item);
            items.add(item);

            productList.add(item.title);
            addPrice(item);
            siteList.add(hyper);
            urlTextField.clear();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	public void browserSelctionMenu(){
		VBox bounds = new VBox();
		bounds.setMinSize(350, 200);
		bounds.setAlignment(Pos.CENTER);
		Scene scene = new Scene(bounds, 350, 200);
		final Stage stage = new Stage();
		stage.setTitle("Select a Browser");
		Button amazonButton = new Button("Amazon");
		amazonButton.setMinSize(100, 50);
		Button ebayButton = new Button("eBay");
		ebayButton.setMinSize(100, 50);
		Button newEggButton = new Button("newEgg");
		newEggButton.setMinSize(100, 50);
		
		amazonButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				createBrowser("http://www.amazon.com/");
				stage.close();
			}
        });
		ebayButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				createBrowser("http://www.ebay.com/");
				stage.close();
			}
        });
		newEggButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				createBrowser("http://www.newegg.com/");
				stage.close();
			}
        });
		
		bounds.getChildren().addAll(amazonButton, ebayButton, newEggButton);
        VBox.setVgrow(bounds, Priority.ALWAYS);
        
        stage.setScene(scene);
        stage.show();
		
		
	}
	
	
	private void createBrowser(String url){
		 VBox vbox = new VBox();
         Scene scene = new Scene(vbox);
         final Stage stage = new Stage();
         stage.setTitle(url);
         ArrayList<String> listOfPages = new ArrayList<String>();
         listOfPages.add(url);
         HBox buttonBox = new HBox();
         buttonBox.setSpacing(10);
         Button addSite = new Button("Watch this item!");
         Button switchBrowsers = new Button("Switch Browsers");
         Button addBrowser = new Button("Add Browser");
         Button previous = new Button("<--");
         buttonBox.getChildren().addAll(addSite, switchBrowsers, addBrowser);
     
         final WebView browser = new WebView();
	     final WebEngine webEngine = browser.getEngine();
         webEngine.load(url);
         
         
         previous.setOnAction(new EventHandler<ActionEvent>() {
        	 @Override
        	 public void handle(ActionEvent e) {
        		 WebHistory history = webEngine.getHistory();
        		 String previousPage = history.getEntries().get(history.getEntries().size()-2).getUrl();
        		 webEngine.load(previousPage);
        		 System.out.println("Loading " + previousPage);
        		 System.out.println("Total History: " + history.getEntries());
        	 }
         });
         
         addSite.setOnAction(new EventHandler<ActionEvent>() {
        	 @Override
        	 public void handle(ActionEvent e) {
        		 String currentPage = webEngine.getLocation();
        		 System.out.println("URL is: " + currentPage);
        		 try {
					watchItem(currentPage);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
        	 }
         });
         
         switchBrowsers.setOnAction(new EventHandler<ActionEvent>() {
        	 @Override
        	 public void handle(ActionEvent e) {
        		 browserSelctionMenu();
        		 stage.close();
        	 }
         });
         
         addBrowser.setOnAction(new EventHandler<ActionEvent>() {
        	 @Override
        	 public void handle(ActionEvent e) {
        		 browserSelctionMenu();
        	 }
         });
         
         vbox.getChildren().addAll(previous, buttonBox, browser);
         VBox.setVgrow(browser, Priority.ALWAYS);
         
         stage.setScene(scene);
         stage.show();
         System.out.println("Done!");
		
		
	}
	
	private Hyperlink createHyperlink(final String url){
		Hyperlink hyper = new Hyperlink();
		 hyper.setText(url);
		 hyper.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent e) {
                 createBrowser(url);
             }
         });
		 return hyper;
	}

    private void addItem(Item item) {
        if (!item.title.equals(null)){
            String newPrice = item.price;
            itemBase.addItem(item);
            productList.add(item.title);
            addPrice(item);
        }
    }
	
	private void update() throws SQLException {
		for (int i = 0; i < items.size(); i++) {
			Item update = itemBase.getMostRecent(items.get(i));
			items.set(i, update);
		}
	}
	
	private void drawPriceGraph(Item item) {
		PriceGraphMaker maker = new PriceGraphMaker();
		try{
			PriceGraph graph = maker.makePriceGraph(item);
			ArrayList<Point> recentPoints = graph.getLastOneHundredPoints();
			//TODO: draw recentPoints on a pane
		}
		catch (SQLException e) {e.printStackTrace();}
	}


    private void populateItems() {
        items = itemBase.getAllRecent();
        for (int i=0; i < items.size(); i++) {
            Item temp = items.get(i);
            productList.add(temp.title);
            addPrice(temp);
            Hyperlink link = createHyperlink(temp.url);
            siteList.add(link);
        }
    }
    
    @FXML
    public void manualUpdate() throws MalformedURLException{
    	updateItems();
    }
    
    public void updateItems() throws MalformedURLException{
    	items = itemBase.getAllRecent();
    	for(int i = 0; i < items.size(); i++){
    		Item oldItem = items.get(i);
    		String oldPriceString = oldItem.price;
    		oldPriceString = oldPriceString.substring(1);
    		double oldPrice = Double.parseDouble(oldPriceString);
    		
    		String url = oldItem.url;
    		Item newItem = new Item(url);
    		newItem = parser.parse(url);
    		String newPriceString = newItem.price;
    		newPriceString = newPriceString.substring(1);
    		double newPrice = Double.parseDouble(newPriceString);
    		
    		if(newPrice > oldPrice){
    			priceList.remove(i);
    			updatePrice(newItem, "up", i);
    			System.out.println("The price has gone up!");
    		}
    		else if(newPrice < oldPrice){
    			priceList.remove(i);
    			updatePrice(newItem, "down", i);
    			System.out.println("The price has gone down!");
    		}
    		else if(newPrice == oldPrice){
    			System.out.println("The price is the same");
    		}
    		else{
    			throwError("Cannot find the price!");
    		}
    		
    	}
    	
    	
    }

    private void addPrice(Item item){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        Text text = new Text(item.price);
        ImageView image = getImage();
        hbox.getChildren().addAll(text, image);
        priceList.add(hbox);
        if(item.shipping.isFree() == "1") {
            Text shipping = new Text(" + free shipping!");
            hbox.getChildren().add(shipping);
        }
    }

    private ImageView getImage() {
        return createArrow();
    }

    private ImageView createArrow() {
        ImageView arrow = new ImageView();
        Image image = new Image("file:GrayLine.png");
        arrow.setImage(image);
        arrow.setFitHeight(3);
        arrow.setFitWidth(15);
        return arrow;
    }

    private String requestURL() {
        currentURL = webEngine.locationProperty().get();
        return currentURL;
    }

    private void showBrowser() {
        browser.setVisible(true);
    }

    private void hideBrowser() {
        browser.setVisible(false);
    }
}
