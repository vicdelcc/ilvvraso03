package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Recipe;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Class used for scrapping recipes.
 *
 * @since 24.10.2018
 * @author Lucas Larisch
 */
public class RecipeCrawlerChefkoch extends ChefkochCrawler {

    private final String RECIPES_APPEND_BEFORE_ID = "/rezepte/";
    private final String ONE_PORTION_APPEND = "?portionen=1";

    private final String CSS_QUERY_RECIPE_PRERARATION = "div#rezept-zubereitung";
    private final String CSS_QUERY_H1 = "h1";
    private final String CSS_QUERY_PREPARATION_INFO = "p#preparation-info";

    private Recipe recipe;

    public Recipe scrapRecipe(long recipeId) {
        super.appendToBaseUrl(RECIPES_APPEND_BEFORE_ID + recipeId + ONE_PORTION_APPEND);

        recipe = null;

        try {
            Document recipePage = getUnlimitedDocument();
            recipe = new Recipe();
            addBasicContentToRecipe(recipePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    private Recipe addBasicContentToRecipe(Document recipePage) {
        // ID!
        String title = recipePage.select(CSS_QUERY_H1).text();
        String preparation = recipePage.select(CSS_QUERY_RECIPE_PRERARATION).text();

        String preparationInfo = recipePage.select(CSS_QUERY_PREPARATION_INFO).first().toString();
        for (String s : preparationInfo.split("<\\/strong>"))
            System.out.println(s);

        return recipe;
    }

}
