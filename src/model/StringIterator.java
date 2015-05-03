package model;

public class StringIterator {
    private String freeShippingText;
    private char startPriceChar;
    private char startTitleChar;
    private char endTitleChar;
    private char endPriceChar;

    public StringIterator() {}

    public String getPriceFromLine(String s) {
        return iterateLine(s, startPriceChar, endPriceChar);
    }

    public String getTitleFromLine(String s) {
        return iterateLine(s, startTitleChar, endTitleChar);
    }

    public Shipping getShipping(String s) {
        if (s.contains(freeShippingText)) {return Shipping.FREE;}
        return Shipping.PAID;
    }

    public void amazonStringParsing() {
        startPriceChar = '$';
        endPriceChar = '<';
        endTitleChar = '<';
        startTitleChar = '>';
        freeShippingText = "FREE Shipping";
    }

    public String getGreaterPrice(String price1, String price2) {
        double p1 = getPrice(price1);
        double p2 = getPrice(price2);
        if (p1 > p2) {return price1;}
        else if (p2 > p1) {return price2;}
        return null;
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

    private double getPrice(String price) {
        String temp = price.substring(1,price.length()-1);
        return Double.valueOf(temp);
    }
}
