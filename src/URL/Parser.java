package URL;

import model.Item;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {

    private URLConnector connector = new URLConnector();
    private String normalPriceText;
    private String otherPriceText;
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
                    System.out.println(line);
                    if (hasNormalTitleText(line)) {
                        System.out.println(line);
                        System.out.println("1");
                        item.title = getTitleFromLine(line);
                    } else if (hasAltTitleText(line)) {
                        System.out.println(line);
                        System.out.println("2");
                        item.title = getTitleFromAltLine(line);
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
        return iterateLine(s, startPriceChar, endPriceChar);
    }

    private String getTitleFromLine(String s) {
        return iterateLine(s, startTitleChar, endTitleChar);
    }

    private String getTitleFromAltLine(String s) { return iterateAltLine(s, altStartTitleStr);}

    private String getShipping(String s) {
        System.out.println(s);
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
        if (s.charAt(i) != '$') {i++;}
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
        normalPriceText = "span id=\"priceblock_ourprice";
        otherPriceText = "class=\"priceLarge";
        blockPriceText = "a-color-price offer-price";
        startPriceChar = '$';
        endPriceChar = '<';
        endTitleChar = '<';
        altEndTitleChar = '\'';
        normalTitleText = "span id=\"productTitle";
        altTitleText = "title:";
        startTitleChar = '>';
        altStartTitleStr = "title: '";
        shippingText = "id=\"ourprice_shippingmessage";
        freeShippingText = "FREE Shipping";
        siteShippingSpacing = 17;
    }

    private BufferedReader connectToSite(String url) {
        return connector.connect(url);
    }

    private boolean hasPriceText(String line) {
        return line.contains(normalPriceText) || line.contains(otherPriceText) || line.contains(blockPriceText);
    }
    private boolean hasNormalTitleText(String line) {
        return line.contains(normalTitleText);
    }
    private boolean hasAltTitleText(String line) {
        return line.contains(altTitleText);
    }
}
