package URL;

import model.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.scene.control.Hyperlink;

public class Parser {

    private String normalPriceText;
    private String otherPriceText;
    private String blockPriceText;
    private String shippingText;
    private String freeShippingText;
    private String titleText;
    private char startPriceChar;
    private char endLineChar;
    private char startTitleChar;
    private int siteShippingSpacing;

    public Parser() {}

    public Item parse(String url) {
        setSite(Website.getSite(url));
        Item item = new Item(url);
        try {
            BufferedReader in = connectToSite(url);
            String line;
            while(true) {
                line = in.readLine();
                if (line == null) {break;}
                else if (!item.hasPrice()) {
                    if (hasPriceText(line)) {
                        item.price = getPriceFromLine(line);
                    }
                }
                else if (!item.hasTitle()) {
                    if (hasTitleText(line)) {
                        System.out.println(line);
                        item.title = getTitleFromLine(line);
                    }
                }
                else if (line.contains(shippingText)) {
                    for (int i=0; i<siteShippingSpacing; i++) {line = in.readLine();}
                    item.shipping = getShipping(line);
                }
            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    private String getPriceFromLine(String s) {
        return iterateLine(s, startPriceChar);
    }

    private String getTitleFromLine(String s) {
        return iterateLine(s, startTitleChar);
    }

    private String getShipping(String s) {
        System.out.println(s);
        if (s.contains(freeShippingText)) {return "1";}
        return "0";
    }

    private String iterateLine(String s, char startChar) {
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i) == startChar) {
                return getEndOfString(s, i);
            }
        }
        return "information not in line";
    }

    private String getEndOfString(String s, int i) {
        if (s.charAt(i) != '$') {i++;}
        String newStr = "";
        char currentChar = s.charAt(i++);
        while (currentChar != endLineChar) {
            newStr += currentChar;
            currentChar = s.charAt(i++);
        }
        return manageString(newStr);
    }

    private String manageString(String s) {
        return s.replace("&quot;",""+'"');

    }

    private void setSite(Website site) {
        if (site.equals(Website.AMAZON)) {
            amazonStringParsing();
        }
    }

    private void amazonStringParsing() {
        normalPriceText = "span id=\"priceblock_ourprice";
        otherPriceText = "class=\"priceLarge";
        blockPriceText = "a-color-price offer-price";
        startPriceChar = '$';
        endLineChar = '<';
        titleText = "span id="+'"'+"productTitle";
        startTitleChar = '>';
        shippingText = "id=\"ourprice_shippingmessage";
        freeShippingText = "FREE Shipping";
        siteShippingSpacing = 17;
    }

    private BufferedReader connectToSite(String url) {
        try {
            URL site = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) site.openConnection();
            httpCon.connect();
            InputStreamReader reader = new InputStreamReader((InputStream) httpCon.getContent());
            return new BufferedReader(reader);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean hasPriceText(String line) {
        return line.contains(normalPriceText) || line.contains(otherPriceText) || line.contains(blockPriceText);
    }
    private boolean hasTitleText(String line) {
        return line.contains(titleText);
    }
}
