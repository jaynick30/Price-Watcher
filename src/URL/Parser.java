package URL;

import model.Item;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

    private URLConnector connector = new URLConnector();
    private String normalPriceId;
    private String normalPriceClass;
    private String altPriceClass;
    private String shippingText;
    private String freeShippingText;
    private String normalTitleId;
    private String altTitleId;
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
        if (getLine(doc, normalPriceId) != null) {
            System.out.println("first");
            currentLine = getLine(doc, normalPriceId);
            item.price = getPriceFromLine(currentLine);
        }
        else if (getLine(doc, altPriceClass) != null) {
            System.out.println("second");
            currentLine = getLine(doc, altPriceClass);
            item.price = getPriceFromLine(currentLine);
        }
        else if (doc.select(normalPriceClass) != null) {
            System.out.println("third");
            Elements elements = doc.select(normalPriceClass);
            for (Element element : elements) {
                if (item.price == null) {
                    System.out.println(element.text());
                    item.price = getPriceFromLine(element.text());
                }
            }
        }

    }

    private void setTitle(Item item, Document doc) {
        String currentLine;
        if (getLine(doc, normalTitleId) != null) {
            currentLine = getLine(doc, normalTitleId);
        }
        else if (getLine(doc, altTitleId) != null) {
            System.out.println("here");
            currentLine = getLine(doc, altTitleId);
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
        char currentChar = s.charAt(i);
        while (currentChar != endChar) {
            newStr += currentChar;
            i++;
            if (i >= s.length()) {break;}
            currentChar = s.charAt(i);
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
        normalPriceId = "priceblock_ourprice";
        normalPriceClass = ".a-color-price";
        altPriceClass = ".priceLarge";

        normalTitleId = "productTitle";
        altTitleId = "btAsinTitle";

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
}