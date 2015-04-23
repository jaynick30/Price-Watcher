package URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Parser {

    public Parser() {
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
                if (line.contains("span id="+'"'+"priceblock_ourprice")) {
                    System.out.println(getPriceFromLine(line));
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
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i) == '$') {
                return getPrice(s,i);
            }
        }
        return "no price in line";
    }

    private String getPrice(String s, int i) {
        String price = "";
        char currentChar = s.charAt(i++);
        while (currentChar != '<') {
            price +=currentChar;
            currentChar = s.charAt(i++);
        }
        return price;
    }
}
