package tests;

import URL.Parser;
import model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTests {
    private Parser parser = new Parser();

    private Document doc;
    private Item item;
    private Item test;
    private Item book;
    private Item game;
    private Item backpack;
    private Item art;

    private String testFileName, bookFileName, gameFileName, backpackFileName, artFileName;

    @Before
    public void initialize() {
        test = new Item("http://www.amazon.com/gp/product/B00IOY8XWQ/ref=s9_psimh_gw_p349_d0_i2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-1&pf_rd_r=1GFJPB3F15ZGZ6GGH4AV&pf_rd_t=36701&pf_rd_p=2079475182&pf_rd_i=desktop");
        test.price = "$199.00";
        test.title = "Kindle Voyage";
        test.shipping = "0";

        book = new Item("http://www.amazon.com/Memory-Amos-Decker-David-Baldacci/dp/1455559822/ref=sr_1_1?s=books&ie=UTF8&qid=1429838562&sr=1-1&keywords=book");
        book.price = "$14.67";
        book.title = "Memory Man (Amos Decker series)";
        book.shipping = "0";

        game = new Item("http://www.amazon.com/Bloodborne-PlayStation-4/dp/B00KVR4HEC/ref=sr_1_1?ie=UTF8&qid=1429839651&sr=8-1&keywords=bloodborne");
        game.price = "$59.02";
        game.title = "Bloodborne";
        game.shipping = "1";

        backpack = new Item("http://www.amazon.com/JanSport-Superbreak-Classic-Backpack-Black/dp/B0007QCQGI/ref=sr_1_1?ie=UTF8&qid=1430147820&sr=8-1&keywords=backpack");
        backpack.price = "$30.50";
        backpack.title = "Classic SuperBreak Backpack";
        backpack.shipping = "1";

        art = new Item("http://www.amazon.com/The-Artist-and-His-Wife/dp/B00E9EC28G/ref=sr_1_1?ie=UTF8&qid=1430271197&sr=8-1&keywords=fine+art");
        art.price = "$285,000.00";
        art.title = "The Artist and His Wife";
        art.shipping = "1";

        testFileName = "Kindle Voyage";
        bookFileName = "Memory Man";
        gameFileName = "Bloodborne";
        backpackFileName = "Classic SuperBreak Backpack";
        artFileName = "The Artist and His Wife";
    }


    @Test
    public void testPrint() {
        testItem(test, testFileName);
    }

    @Test
    public void testBookURL() {
        testItem(book, bookFileName);
    }

    @Test
    public void testGameURL() {
        testItem(game, gameFileName);
    }

    @Test
    public void testBackpackURL() { testItem(backpack, backpackFileName);}

    @Test
    public void testArtURL() {
        testItem(art, artFileName);
    }

    private void testItem(Item testItem, String fileName) {
        doc = getDocument(testItem, fileName);
        item = parser.setValues(doc, testItem.url);
        checkItem(testItem);
    }

    private Document getDocument(Item item, String fileName) {
        File input = new File("other/files/" + fileName+".txt");
        try {
            return Jsoup.parse(input, "UTF-8", item.url);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printItemValues(Item item) {
        System.out.println(item.title);
        System.out.println(item.price);
        System.out.println(item.shipping);
    }

    private void checkItem(Item other) {
        assertEquals(other.title, item.title);
        assertEquals(other.price, item.price);
        assertEquals(other.shipping, item.shipping);
        assertTrue(item.hasPrice());
        assertTrue(item.hasTitle());
    }
}