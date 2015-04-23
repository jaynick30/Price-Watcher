package URL;

import model.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Parser {

    private String priceText;
    private String shippingText;
    private String freeShippingText;
    private String titleText;
    private char startPriceChar;
    private char endLineChar;
    private char startTitleChar;

    public Parser(Website site) {
        setSite(site);
    }

    public Item parse(String url) {
        Item item = new Item(url);
        try {
            URL site = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) site.openConnection();
            httpCon.connect();
            InputStreamReader reader = new InputStreamReader((InputStream) httpCon.getContent());
            BufferedReader in = new BufferedReader(reader);
            String line;
            while(true) {
                line = in.readLine();
                if (line == null) {break;}
                if (line.contains(priceText)) {
                    item.price = getPriceFromLine(line);
                }
                else if (line.contains(titleText)) {
                    item.title = getTitleFromLine(line);
                }
                else if (line.contains(shippingText)) {
                    item.shipping = getShipping(line);
                }
            }
            in.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
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

    private boolean getShipping(String s) {
        System.out.println(s);
        if (s.contains(freeShippingText)) {return true;}
        return false;
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
        String newStr = "";
        i++;
        char currentChar = s.charAt(i++);
        while (currentChar != endLineChar) {
            newStr += currentChar;
            currentChar = s.charAt(i++);
        }
        return manageString(newStr);
    }

    private String manageString(String s) {
        s.replace("&quot",""+'"');
        return s;
    }

    private void setSite(Website site) {
        if (site.equals(Website.AMAZON)) {
            amazonStringParsing();
        }
    }

    private void amazonStringParsing() {
        priceText = "span id="+'"'+"priceblock_ourprice";//TODO check \
        startPriceChar = '$';
        endLineChar = '<';
        titleText = "span id="+'"'+"productTitle";
        startTitleChar = '>';
        shippingText = "id=\"ourprice_shippingmessage";
        freeShippingText = "FREE Shipping";
    }
}
