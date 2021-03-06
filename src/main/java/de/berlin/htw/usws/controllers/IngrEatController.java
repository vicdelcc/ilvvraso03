package de.berlin.htw.usws.controllers;

import com.google.common.base.Joiner;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Protokoll;
import de.berlin.htw.usws.model.Recipe;
import de.berlin.htw.usws.model.SupermarketGEO;
import de.berlin.htw.usws.repositories.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

@Path("/")
public class IngrEatController {

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private IngredientsInRecipeRepository ingredientsInRecipeRepository;

    @Inject
    private ProtokollRepository protokollRepository;

    @Inject
    private SupermarketGEORepository supermarketGEORepository;

    /**
     * GET-Auruf, um alle Ingredients von der DB zu holen
     *
     * @return
     */
    @GET
    @Path("/getAllIngredients")
    @Produces("application/json")
    public Response getAllIngredients() {
        List<Ingredient> ingredients = this.ingredientRepository.findAllIngredients();
        for (Ingredient ingredient : ingredients) {
            ingredient.setProducts(null);
        }
        // Create protokoll
        Protokoll protokoll = new Protokoll();
        protokoll.setErzeuger("API: getAllIngredients");
        protokoll.setNumberGetElements(ingredients.size());
        this.protokollRepository.save(protokoll);

        return Response.ok().entity(ingredients).build();
    }

    /**
     * GET-Aufruf, um die Measures von einem Ingredient zu holen
     *
     * @param ingredientName
     * @return
     */
    @GET
    @Path("/getMeasures/{ingredientName}")
    @Produces("application/json")
    public Response getMeasures(@PathParam("ingredientName") final String ingredientName) {
        List<String> measures = this.ingredientsInRecipeRepository.getMeasuresByIngredient(ingredientName);
        CollectionUtils.filter(measures, PredicateUtils.notNullPredicate());

        // Create protokoll
        Protokoll protokoll = new Protokoll();
        protokoll.setErzeuger("API: getMeasures");
        protokoll.setAufrufparameter(ingredientName);
        protokoll.setNumberGetElements(measures.size());
        this.protokollRepository.save(protokoll);

        return Response.ok().entity(measures).build();
    }


    /**
     * POST-Aufruf, der mit den übergebenen Ingredients die ersten 10 Rezepte holt.
     * Die geholten Rezepten werden nach der Anzahl von fehlenden Zutanten aufsteigend sortiert
     *
     * @param ingredientsList
     * @return
     */
    @POST
    @Path("/getRecipesMax")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipesMax(final IngredientsList ingredientsList) {
        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredientsMax(ingredientsList.getIngredients());
        recipes = sortRecipesByMissingIngredients(recipes, ingredientsList.getIngredients().size());

        // Create protokoll
        Protokoll protokoll = new Protokoll();
        protokoll.setErzeuger("API: getRecipesMax");
        protokoll.setAufrufparameter(Joiner.on(", ").join(ingredientsList.getIngredients()));
        protokoll.setNumberGetElements(recipes.size());
        protokoll.setErgebnisListeRecipeIds(Joiner.on(", ").join(recipes.stream().map(sc -> sc.getId()).collect(Collectors.toList())));
        this.protokollRepository.save(protokoll);

        return Response.ok().entity(recipes).build();
    }

    /**
     * POST-Aufruf, der mit den übergebenen Ingredients alle Rezepte ab 10 holt.
     * Die geholten Rezepten werden nach der Anzahl von fehlenden Zutanten aufsteigend sortiert
     *
     * @param ingredientsList
     * @return
     */
    @POST
    @Path("/getRecipesRest")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipesRest(final IngredientsList ingredientsList) {
        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredientsRest(ingredientsList.getIngredients());
        recipes = sortRecipesByMissingIngredients(recipes, ingredientsList.getIngredients().size());

        // Create protokoll
        Protokoll protokoll = new Protokoll();
        protokoll.setErzeuger("API: getRecipesRest");
        protokoll.setAufrufparameter(Joiner.on(", ").join(ingredientsList.getIngredients()));
        protokoll.setNumberGetElements(recipes.size());
        protokoll.setErgebnisListeRecipeIds(Joiner.on(", ").join(recipes.stream().map(sc -> sc.getId()).collect(Collectors.toList())));
        this.protokollRepository.save(protokoll);

        return Response.ok().entity(recipes).build();
    }


    /**
     * POST-Aufruf, der mit den übergebenen Ingredients alle Rezepte holt.
     *
     * @param ingredientsList
     * @return
     */
    @POST
    @Path("/getRecipes")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getRecipes(final IngredientsList ingredientsList) {
        List<Recipe> recipes = this.recipeRepository.findRecipesContainingIngredientsAll(ingredientsList.getIngredients());

        // Create protokoll
        Protokoll protokoll = new Protokoll();
        protokoll.setErzeuger("API: getRecipes");
        protokoll.setAufrufparameter(Joiner.on(", ").join(ingredientsList.getIngredients()));
        protokoll.setNumberGetElements(recipes.size());
        protokoll.setErgebnisListeRecipeIds(Joiner.on(", ").join(recipes.stream().map(sc -> sc.getId()).collect(Collectors.toList())));
        this.protokollRepository.save(protokoll);

        return Response.ok().entity(recipes).build();
    }

    /**
     * GET-Auruf, um alle SupermarktGEO von der DB zu holen
     *
     * @return
     */
    @GET
    @Path("/getAllSupermarketGeo")
    @Produces("application/json")
    public Response getAllSupermarketGEO() {
        List<SupermarketGEO> supermarketGEOs = this.supermarketGEORepository.findAll();

        // Create protokoll
        Protokoll protokoll = new Protokoll();
        protokoll.setErzeuger("API: getAllSupermarketGeo");
        protokoll.setNumberGetElements(supermarketGEOs.size());
        this.protokollRepository.save(protokoll);

        return Response.ok().entity(supermarketGEOs).build();
    }



    private List<Recipe> sortRecipesByMissingIngredients(List<Recipe> recipes, int numberInputIngredients) {
        HashMap<Recipe, Integer> map = new HashMap<>();
        for (Recipe recipe : recipes) {
            map.put(recipe, recipe.getNumberMissingIngredients(numberInputIngredients));
        }
        Map<Recipe, Integer> sortedMap = map
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                LinkedHashMap::new));

        recipes.clear();
        for(Recipe recipe : sortedMap.keySet()) {
            recipes.add(recipe);
        }
        return  recipes;
    }
}
