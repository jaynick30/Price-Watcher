package URL;

import model.Item;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;

public class Parser {

    private URLConnector connector = new URLConnector();
    private String normalPriceText;
    private String altPriceText;
    private String blockPriceText;
    private String shippingText;
    private String freeShippingText;
    private String normalTitleText;
    private String altTitleText;
    private String altStartTitleStr;
    private char startPriceChar;
    private char startTitleChar;
    private char endTitleChar;
    private char endPriceChar;
    private char altEndTitleChar;
    private int siteShippingSpacing;

    public Parser() {}

    public Item parse(String url) {
        String currentLine;
        setSite(Website.getSite(url));
        Item item = new Item(url);
        Document doc = connector.connect2(url);
        setPrice(item, doc);
        setTitle(item, doc);
        setShipping(item, doc);
        return item;
    }

    private void setPrice(Item item, Document doc) {
        String currentLine;
        if (getLine(doc, normalPriceText) != null) {
            System.out.println("first");
            currentLine = getLine(doc, normalPriceText);
        }
        else if (getLine(doc, blockPriceText) != null) {
            System.out.println("second");
            currentLine = getLine(doc, blockPriceText);
        }
        else if (getLine(doc, altPriceText) != null) {
            System.out.println("third");
            currentLine = getLine(doc, blockPriceText);
        }
        else {return;}
        item.price = getPriceFromLine(currentLine);
    }

    private void setTitle(Item item, Document doc) {
        String currentLine;
        if (getLine(doc, normalTitleText) != null) {
            System.out.println("first");
            currentLine = getLine(doc, normalTitleText);
        }
        else if (getLine(doc, altTitleText) != null) {
            System.out.println("second");
            currentLine = getLine(doc, altTitleText);
        }
        else {return;}
        item.title = getTitleFromLine(currentLine);
    }

    private void setShipping(Item item, Document doc) {
        String currentLine = getLine(doc, shippingText);
        if (currentLine != null) {
            item.shipping = getShipping(currentLine);
        }
    }

    private String getPriceFromLine(String s) {
        return iterateLine(s, startPriceChar, endPriceChar);
    }

    private String getTitleFromLine(String s) {
        return iterateLine(s, startTitleChar, endTitleChar);
    }

    private String getTitleFromAltLine(String s) { return iterateAltLine(s, altStartTitleStr);}

    private String getShipping(String s) {
        if (s.contains(freeShippingText)) {return "1";}
        return "0";
    }

    private String iterateAltLine(String s, String start) {
        String[] strings=s.split(start);
        return getEndOfString(strings[1],0, altEndTitleChar);
    }

    private String iterateLine(String s, char startChar, char endChar) {
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i) == startChar) {
                return getEndOfString(s, i, endChar);
            }
        }
        return "information not in line";
    }

    private String getEndOfString(String s, int i, char endChar) {
        if (s.charAt(i) != startPriceChar) {i++;}
        String newStr = "";
        char currentChar = s.charAt(i++);
        while (currentChar != endChar) {
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
        normalPriceText = "priceblock_ourprice";
        altPriceText = "priceLarge";
        blockPriceText = "a-color-price offer-price";

        normalTitleText = "productTitle";
        altTitleText = "title:";

        shippingText = "ourprice_shippingmessage";
        freeShippingText = "FREE Shipping";

        altStartTitleStr = "title: '";

        startPriceChar = '$';
        endPriceChar = '<';
        endTitleChar = '<';
        altEndTitleChar = '\'';
        startTitleChar = '>';

        siteShippingSpacing = 17;
    }

    private String getLine(Document doc, String id) {
        Element element = doc.getElementById(id);
        if (element == null) {return null;}
        return element.toString();
    }

    private BufferedReader connectToSite(String url) {
        return connector.connect(url);
    }

    private boolean hasPriceText(String line) {
        return line.contains(normalPriceText) || line.contains(altPriceText) || line.contains(blockPriceText);
    }
    private boolean hasNormalTitleText(String line) {
        return line.contains(normalTitleText);
    }
    private boolean hasAltTitleText(String line) {
        return line.contains(altTitleText);
    }
}
