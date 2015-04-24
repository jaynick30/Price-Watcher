package model;

public class Item {
    public String url;
    public String price;
    public String title;
    public String shipping;//1 == free shipping, 0 == paid shipping


    public Item(String url) {
    	this.url = url;
    }

    public boolean hasPrice() {return price!=null;}
    public boolean hasTitle() {return title!=null;}
}
