package URL;

import model.Item;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

    private StringIterator iterator = new StringIterator();
    private URLConnector connector = new URLConnector();
    private String normalPriceId;
    private String normalPriceClass;
    private String altPriceClass;
    private String shippingId;
    private String altShippingId;
    private String normalTitleId;
    private String altTitleId;
    private String artTitleId;
    private String artistTitleId;

    public Parser() {}

    public Item parse(String url) {
        setSite(Website.getSite(url));
        Item item = new Item(url);
        Document doc = connector.connect(url);
        setPrice(item, doc);
        setTitle(item, doc);
        setShipping(item, doc);
        return item;
    }

    private void setPrice(Item item, Document doc) {
        if (getLine(doc, normalPriceId) != null) {
            item.price = getPriceFromId(doc, normalPriceId);
        }
        else if (doc.select(normalPriceClass).size() != 0) {
            item.price = getPriceFromClass(doc, normalPriceClass);
        }
        else if (doc.select(altPriceClass).size() != 0) {
            item.price = getPriceFromClass(doc, altPriceClass);
        }
    }

    private void setTitle(Item item, Document doc) {
        String currentLine;
        if (getLine(doc, normalTitleId) != null) {
            currentLine = getLine(doc, normalTitleId);
        }
        else if (getLine(doc, altTitleId) != null) {
            currentLine = getLine(doc, altTitleId);
        }
        else if (getLine(doc, artTitleId) != null) {
            currentLine = getLine(doc, artTitleId);
            //currentLine = "\"" + currentLine + "\"" + getLine(doc, artistTitleId);
        }
        else {return;}
        item.title = iterator.getTitleFromLine(currentLine);
    }

    private void setShipping(Item item, Document doc) {
        if (getLine(doc, shippingId) != null) {
            item.shipping = getShippingFromId(doc, shippingId);
        }
        else if (getLine(doc, altShippingId) != null) {
            item.shipping = getShippingFromId(doc, altShippingId);
        }
    }

    private String getShippingFromId(Document doc, String id) {
        String currentLine = getLine(doc, id);
        return iterator.getShipping(currentLine);
    }

    private String getPriceFromClass(Document doc, String className) {
        Elements elements = doc.select(className);
        for (Element element : elements) {
            if (correctPrice(element.toString())) {return element.text();}
        }
        return null;
    }

    private boolean correctPrice(String price) {
        if (price != null) {
            if (!price.contains("style")) {return true;}
        }
        return false;
    }

    private String getPriceFromId(Document doc, String id) {
        String currentLine = getLine(doc, id);
        return iterator.getPriceFromLine(currentLine);
    }

    private String getLine(Document doc, String id) {
        Element element = doc.getElementById(id);
        if (element == null) {return null;}
        return element.toString();
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
        artTitleId = "fineArtTitle";
        artistTitleId = "fine-ART-ProductLabelArtistNameLink";

        shippingId = "ourprice_shippingmessage";
        altShippingId = "actualPriceExtraMessaging";

        iterator.amazonStringParsing();
    }
}