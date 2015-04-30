package URL;

import model.Item;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
    private StringIterator iterator = new StringIterator();
    private URLConnector connector = new URLConnector();
    private String normalPriceId;
    private String altPriceId;
    private String normalPriceClass;
    private String altPriceClass;
    private String shippingId;
    private String altShippingId;
    private String normalTitleId;
    private String altTitleId;
    private String artTitleId;

    public Parser() {}

    public Item parse(String url) {
        Document doc = connector.connect(url);
        System.out.println(doc.toString());
        return setValues(doc, url);
    }

    public Item setValues(Document doc, String url) {
        setSite(Website.getSite(url));
        Item item = new Item(url);
        setPrice(item, doc);
        setTitle(item, doc);
        setShipping(item, doc);
        return item;
    }

    private void setPrice(Item item, Document doc) {
        if (getLine(doc, normalPriceId) != null) {
            item.price = getPriceFromId(doc, normalPriceId);
        }
        else if (getLine(doc, altPriceId) != null) {
            item.price = getPriceFromId(doc, altPriceId);
        } else if (doc.select(normalPriceClass).size() != 0) {
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
            currentLine = iterator.getTitleFromLine(currentLine);
            item.title = currentLine.substring(0,currentLine.length()-1);
            return;
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

    private String getPriceFromId(Document doc, String id) {
        String currentLine = getLine(doc, id);
        return iterator.getPriceFromLine(currentLine);
    }

    private String getPriceFromClass(Document doc, String className) {
        Elements elements = doc.select(className);
        for (Element element : elements) {
            if (correctPrice(element.toString())) {return element.text();}
        }
        return null;
    }

    private String getShippingFromId(Document doc, String id) {
        String currentLine = getLine(doc, id);
        return iterator.getShipping(currentLine);
    }

    private String getLine(Document doc, String id) {
        if (doc.getElementById(id) == null) {return null;}
        Element element = doc.getElementById(id);
        return element.toString();
    }

    private boolean correctPrice(String price) {
        if (price != null) {
            if (!price.contains("style")) {return true;}
        }
        return false;
    }

    private void setSite(Website site) {
        if (site.equals(Website.AMAZON)) {
            amazonStringParsing();
        }
    }

    private void amazonStringParsing() {
        normalPriceId = "priceblock_ourprice";
        altPriceId = "buyingPriceContent";
        normalPriceClass = ".a-color-price";
        altPriceClass = ".priceLarge";

        normalTitleId = "productTitle";
        altTitleId = "btAsinTitle";
        artTitleId = "fineArtTitle";

        shippingId = "ourprice_shippingmessage";
        altShippingId = "actualPriceExtraMessaging";

        iterator.amazonStringParsing();
    }
}