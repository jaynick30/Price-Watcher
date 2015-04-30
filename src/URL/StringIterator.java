package URL;

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

    public boolean getShipping(String s) {
        if (s.contains(freeShippingText)) {return true;}
        return false;
    }

    public void amazonStringParsing() {
        startPriceChar = '$';
        endPriceChar = '<';
        endTitleChar = '<';
        startTitleChar = '>';
        freeShippingText = "FREE Shipping";
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
}
