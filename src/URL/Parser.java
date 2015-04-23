package URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Parser {

    private String priceText = "";
    private char startPriceChar = '/';
    private char endLineChar = '/';
    private String titleText = "";
    private char startTitleChar = '/';

    public Parser(Website site) {
        setSite(site);
    }

    public void parse(String url) {
        try {
            URL site = new URL(url);
            InputStreamReader reader = new InputStreamReader(site.openStream());
            BufferedReader in = new BufferedReader(reader);
            String line;
            while(true) {
                line = in.readLine();
                if (line == null) {break;}
                if (line.contains(priceText)) {
                    System.out.println(getPriceFromLine(line));
                }
                else if (line.contains(titleText)) {
                    System.out.println(getTitleFromLine(line));
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
    }

    private String getPriceFromLine(String s) {
        return iterateLine(s, startPriceChar);
    }

    private String getTitleFromLine(String s) {
        return iterateLine(s, startTitleChar);
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
        return newStr;
    }

    private void setSite(Website site) {
        if (site.equals(Website.AMAZON)) {
            amazonStringParsing();
        }
    }

    private void amazonStringParsing() {
        priceText = "span id="+'"'+"priceblock_ourprice";
        startPriceChar = '$';
        endLineChar = '<';
        titleText = "span id="+'"'+"productTitle";
        startTitleChar = '>';
    }
}
