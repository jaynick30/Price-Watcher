package URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLConnector {

    public URLConnector() {

    }

    public BufferedReader connect(String url) {
        try {
            clearCookies();
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

    private void clearCookies() {
        //TODO
    }
}
