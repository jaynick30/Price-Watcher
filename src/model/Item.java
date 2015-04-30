package model;

public class Item {
    public String url;
    public String price;
    public String title;
    public Shipping shipping;


    public Item(String url) {
    	this.url = url;
    }

    public boolean hasPrice() {return price!=null;}
    public boolean hasTitle() {return title!=null;}
    public void setShipping(boolean b) {
    	if (b) {shipping = Shipping.FREE;}
    	else{shipping = Shipping.PAID;}
    }
}
