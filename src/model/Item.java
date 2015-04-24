package model;

public class Item {
    public String url;
    public String price;
    public String title;
    public boolean shipping;//true == free shipping


    public Item(String url) {
        this.url = url;
    }

    public boolean hasPrice() {return price!=null;}
    public boolean hasTitle() {return title!=null;}
}
