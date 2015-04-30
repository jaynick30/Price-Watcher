package URL;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

public class URLConnector {
    public URLConnector() {

    }

    public Document connect(String url) throws MalformedURLException {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc;
        } catch (HttpStatusException e) {
            try {
                Thread.sleep(1000);
                return connect(url);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        catch (SocketTimeoutException e) {
            try {
                Thread.sleep(1000);
                return connect(url);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
