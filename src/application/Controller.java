package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller {
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
	ListView<String> prices;
	@FXML
	ListView<Hyperlink> sites;
	
	public ObservableList<String> productList = FXCollections.observableArrayList();
	public ObservableList<String> priceList = FXCollections.observableArrayList();
	public ObservableList<Hyperlink> siteList = FXCollections.observableArrayList();
	
	@FXML
	private void initialize(){
		 products.setItems(productList);
		 prices.setItems(priceList);
		 sites.setItems(siteList);
		 
		 Hyperlink hyper = new Hyperlink();
		 String url = "http://www.amazon.com/Aldo-Womens-Brooklyn-Combat-Black/dp/B00KLMJ4HW/ref=sr_1_7_mc/188-2136881-9670640?s=shoes&ie=UTF8&qid=1429760710&sr=1-7&keywords=amazon+boots";
		 hyper.setText("http://www.amazon.com/Aldo-Womens-Brooklyn-Combat-Black");
		 
		 productList.add("Aldo Womens Brooklyn Combat Boots");
		 priceList.add("$120");
		 siteList.add(hyper);
		 
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
		
	}
	
	
	
	
	
	
	
	
	
	
	
}