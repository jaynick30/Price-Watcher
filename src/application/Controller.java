package application;

import URL.Parser;
import database.Manager;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.Item;

import java.sql.SQLException;

public class Controller {
	@FXML
	VBox applicationBounds;
	@FXML
	TableView<HBox> tableView;
	@FXML
	ComboBox<?> categorySelection;
	@FXML
	Button addCategory;
	@FXML
	Button addURL;
	@FXML
	Button nameButton;
	@FXML
	Button priceButton;
	@FXML
	Button siteButton;
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
	
	private Manager itemBase;
	
	@FXML
	private void initialize(){
		try {itemBase = new Manager("Items");}
		catch (ClassNotFoundException e) {e.printStackTrace();}
		catch (SQLException e) {e.printStackTrace();}
		
		 products.setItems(productList);
		 prices.setItems(priceList);
		 sites.setItems(siteList);
		 
		 products.setFixedCellSize(30);
		 prices.setFixedCellSize(30);
		 sites.setFixedCellSize(30);
		
	}
	
	private void addPrice(String newPrice){
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		Text text = new Text();
		text.setText(newPrice);
		ImageView arrow = new ImageView();
		Image image = new Image("file:GrayLine.png");
		arrow.setImage(image);
		arrow.setFitHeight(3);
		arrow.setFitWidth(15);
		hbox.getChildren().addAll(text, arrow);
		priceList.add(hbox);
	}		
	
	@FXML
	public void addItem(){
		Hyperlink hyper = new Hyperlink();
		String url = urlTextField.getText();
		hyper = createHyperlink(url);
		Parser parser = new Parser();
		Item item = new Item(url);
		
		item = parser.parse(url);
		String newProduct = item.title;
		String newPrice = item.price;
		itemBase.addItem(item);
		
		productList.add(newProduct);
		addPrice(newPrice);
		siteList.add(hyper);
		urlTextField.clear();
	}
	
	private Hyperlink createHyperlink(final String url){
		Hyperlink hyper = new Hyperlink();
		 hyper.setText(url);
		 hyper.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent e) {
            	 VBox vbox = new VBox();
                 Scene scene = new Scene(vbox);
                 Stage stage = new Stage();
             
                 final WebView browser = new WebView();
        	     final WebEngine webEngine = browser.getEngine();
                 webEngine.load(url);
                 
                 vbox.getChildren().add(browser);
                 VBox.setVgrow(browser, Priority.ALWAYS);
                 
                 stage.setScene(scene);
                 stage.show();
                 System.out.println("Done!");
             }
         });
		 return hyper;
	}
	
	
	
	
	
	
	
}
