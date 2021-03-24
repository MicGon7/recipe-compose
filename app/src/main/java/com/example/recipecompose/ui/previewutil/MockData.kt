package com.example.recipecompose.ui.previewutil

import com.example.recipecompose.domain.model.Recipe
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

val recipesJson = """
        {
	"count": 433,
	"next": "https://food2fork.ca/api/recipe/search/?page=2&query=chicken",
	"previous": null,
	"results": [{
			"pk": 2050,
			"title": "Chicken, sweet potato &amp; coconut curry",
			"publisher": "jessica",
			"featured_image": "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/2050/featured_image.png",
			"rating": 50,
			"source_url": "http://www.bbcgoodfood.com/recipes/1555/chicken-sweet-potato-and-coconut-curry",
			"description": "N/A",
			"cooking_instructions": null,
			"ingredients": [
				"175g frozen peas",
				"300ml chicken stock",
				"1 tbsp sunflower oil",
				"2 tsp mild curry paste",
				"400ml can coconut milk",
				"4 tbsp red split lentils",
				"2 medium-sized sweet potatoes , peeled and cut int",
				"2 large boneless, skinless chicken breasts , cut i"
			],
			"date_added": "November 11 2020",
			"date_updated": "November 11 2020",
			"long_date_added": 1606349252,
			"long_date_updated": 1606349252
		},
		{
			"pk": 2061,
			"title": "Chipotle Style Barbacoa",
			"publisher": "mitch",
			"featured_image": "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/2061/featured_image.png",
			"rating": 10,
			"source_url": "http://www.mybakingaddiction.com/chipotle-barbacoa-recipe/",
			"description": "N/A",
			"cooking_instructions": null,
			"ingredients": [
				"4 teaspoons cumin",
				"2 teaspoons oregano",
				"4 pound chuck roast",
				"3/4 cup chicken stock",
				"1 teaspoon kosher salt",
				"1/4 teaspoon ground cloves",
				"1/3 cup apple cider vinegar",
				"2 tablespoons vegetable oil",
				"5 large garlic cloves, minced",
				"1 teaspoon ground black pepper",
				"3 tablespoons freshly squeezed lime juice",
				"3-4 chipotle chiles in adobo (add more if you want"
			],
			"date_added": "November 11 2020",
			"date_updated": "November 11 2020",
			"long_date_added": 1606349255,
			"long_date_updated": 1606349255
		},
		{
			"pk": 2076,
			"title": "Chicken Enchiladas Verdes",
			"publisher": "mitch",
			"featured_image": "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/2076/featured_image.png",
			"rating": 2,
			"source_url": "http://www.realsimple.com/food-recipes/browse-all-recipes/chicken-enchiladas-verdes-00000000039506/index.html",
			"description": "N/A",
			"cooking_instructions": null,
			"ingredients": [
				"1 cup 6-inch flour tortillas\n",
				"1 1/2 cups frozen corn, thawed",
				"8 kosher salt and black pepper",
				"1 16-ounce jar mild salsa verde",
				"1 cup sour cream, plus more for serving",
				"2 pounds bone-in chicken breasts (2 to 3)",
				"2 cups fresh cilantro sprigs, plus more for servin",
				"1/2 pound Muenster or Monterey Jack cheese, grated"
			],
			"date_added": "November 11 2020",
			"date_updated": "November 11 2020",
			"long_date_added": 1606349258,
			"long_date_updated": 1606349258
		},
		{
			"pk": 28,
			"title": "Pot Pie",
			"publisher": "blake",
			"featured_image": "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/28/featured_image.png",
			"rating": 40,
			"source_url": "http://thepioneerwoman.com/cooking/2013/08/pot-pie/",
			"description": "N/A",
			"cooking_instructions": null,
			"ingredients": [
				"1 whole Egg",
				"1/4 cup Flour",
				"2 Tablespoons Water",
				"4 Tablespoons Butter",
				"1/4 teaspoon Turmeric",
				"1 whole Unbaked Pie Crust",
				"Salt And Pepper, to taste",
				"1/2 cup Finely Diced Onion",
				"1/2 cup Finely Diced Carrot",
				"1/2 cup Finely Diced Celery",
				"Chopped Fresh Thyme To Taste",
				"1/4 cup Half-and-half Or Cream",
				"Splash Of White Wine (optional)",
				"3 cups Shredded Cooked Chicken Or Turkey",
				"3 cups Low-sodium Chicken Broth, Plus More If Need"
			],
			"date_added": "November 11 2020",
			"date_updated": "November 11 2020",
			"long_date_added": 1606348720,
			"long_date_updated": 1606348720
		}
	]
} 
""".trimIndent()




val mockRecipe = Recipe(
    id = 0,
    title = "Chipotle Style Barbacoa",
    publisher = "mitch",
    featuredImage = "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/2061/featured_image.png",
    rating = 10,
    sourceUrl = "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/2061/featured_image.png",
    ingredients = listOf(),
    dateAdded = "November 11 2020",
    dateUpdated = "November 11 2020"
)

val mockRecipeList: List<Recipe> = listOf(
    mockRecipe,
    mockRecipe,
    mockRecipe,
    mockRecipe,
    mockRecipe
)



fun getRecipeListFake(recipesJson: String): List<Recipe>? {
    val moshi = Moshi.Builder().build()
    val recipesData: Type = Types.newParameterizedType(List::class.java, Recipe::class.java)
    val jsonAdapter: JsonAdapter<List<Recipe>> = moshi.adapter(recipesData)

    return jsonAdapter.fromJson(recipesJson)

}
