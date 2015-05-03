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
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.Item;
import model.Point;
import model.PriceGraph;

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

    private Parser parser = new Parser();
	private Manager itemBase;
	private ArrayList<Item> items;
    private String currentURL;
    private final WebView browser = new WebView();
    private final WebEngine webEngine = browser.getEngine();
	

	
	@FXML
	private void initialize(){
		itemBase = new Manager("Items");
		itemBase.createTable();

        hideBrowser();
        initializeListViews();
        populateItems();
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

	public void update() throws SQLException {
		for (int i = 0; i < items.size(); i++) {
			Item update = itemBase.getMostRecent(items.get(i));
			items.set(i, update);
		}
	}
	
	public void drawPriceGraph(Item item) {
		PriceGraphMaker maker = new PriceGraphMaker();
		try{
			PriceGraph graph = maker.makePriceGraph(item);
			ArrayList<Point> recentPoints = graph.getLastOneHundredPoints();
			//TODO: draw recentPoints on a pane
		}
		catch (SQLException e) {e.printStackTrace();}
	}

    public void updatePrice() {

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

    private void addPrice(Item item){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        Text text = new Text(item.price);
        ImageView arrow = createArrow();
        hbox.getChildren().addAll(text, arrow);
        priceList.add(hbox);
        if(item.shipping.isFree() == "1") {
            Text shipping = new Text(" + free shipping!");
            hbox.getChildren().add(shipping);
        }
    }

    private ImageView createArrow() {
        ImageView arrow = new ImageView();
        Image image = new Image("file:GrayLine.png");
        arrow.setImage(image);
        arrow.setFitHeight(3);
        arrow.setFitWidth(15);
        return arrow;
    }

    private void initializeListViews() {
        prices.setEditable(true);

        products.setItems(productList);
        prices.setItems(priceList);
        sites.setItems(siteList);

        products.setFixedCellSize(30);
        prices.setFixedCellSize(30);
        sites.setFixedCellSize(30);
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
                 showBrowser();
                 webEngine.load(url);
                 
                 vbox.getChildren().add(browser);
                 VBox.setVgrow(browser, Priority.ALWAYS);
                 
                 stage.setScene(scene);
                 stage.show();
             }
         });
		 return hyper;
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
