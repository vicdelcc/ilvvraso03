package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Product;
import org.junit.Test;

import java.io.IOException;

public class ReweCrawlerTest {

    private ReweCrawler reweCrawler = new ReweCrawler();

    @Test
    public void testGetProductForIngredientREWE() throws IOException {

        Product banane = reweCrawler.getProductForIngredientREWE("Banane");

        System.out.print(banane.toString());
    }
}
