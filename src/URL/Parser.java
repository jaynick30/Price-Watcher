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
                System.out.println(line);
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
}
