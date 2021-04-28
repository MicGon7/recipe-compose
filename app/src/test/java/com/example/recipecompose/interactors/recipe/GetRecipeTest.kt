package com.example.recipecompose.interactors.recipe

import com.example.recipecompose.MockWebServerResponses.recipeListResponse
import com.example.recipecompose.cache.AppDatabaseFake
import com.example.recipecompose.cache.RecipeDaoFake
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.network.data.cache.model.RecipeEntityMapper
import com.example.recipecompose.network.data.network.RecipeService
import com.example.recipecompose.network.data.network.model.RecipeDtoMapper
import com.example.recipecompose.interactors.home.SearchRecipes
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

private const val DUMMY_TOKEN = "fake_token"
private const val DUMMY_QUERY = "fake_query"
private const val RECIPE_ID = 1551

class GetRecipeTest {
    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var getRecipe: GetRecipe

    // Dependencies
    private lateinit var searchRecipes: SearchRecipes
    private lateinit var recipeService: RecipeService
    private lateinit var recipeDao: RecipeDaoFake
    private val dtoMapper = RecipeDtoMapper()
    private val entityMapper = RecipeEntityMapper()

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        recipeService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RecipeService::class.java)
        recipeDao = RecipeDaoFake(appDatabaseFake = appDatabase)

        searchRecipes = SearchRecipes(
            recipeDao = recipeDao,
            recipeService = recipeService,
            entityMapper = entityMapper,
            dtoMapper = dtoMapper
        )

        // instantiate the system in test
        getRecipe = GetRecipe(
            recipeDao = recipeDao,
            entityMapper = entityMapper
        )
    }

    /**
     * 1. Get some recipes from the network and insert into cache
     * 2. Try to retrieve recipes by their specific recipe id
     */
    @Test
    fun getRecipesFromNetwork_getRecipeById(): Unit = runBlocking {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )

        // confirm the cache is empty to start
        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        // get recipes from network and insert into cache
        searchRecipes.execute(DUMMY_TOKEN, 1, DUMMY_QUERY, true).toList()

        // confirm the cache is no longer empty
        assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

        // run use case
        val recipeAsFlow = getRecipe.execute(RECIPE_ID).toList()

        // first emission should be `loading`
        assert(recipeAsFlow[0].loading)

        // second emission should be the recipe
        val recipe = recipeAsFlow[1].data
        assert(recipe?.id == RECIPE_ID)

        // confirm it is actually a Recipe object
        assert(recipe is Recipe)

        // 'loading' should be false now
        assert(!recipeAsFlow[1].loading)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
