package com.example.recipecompose.interactors.home

import com.example.recipecompose.MockWebServerResponses.recipeListResponse
import com.example.recipecompose.cache.AppDatabaseFake
import com.example.recipecompose.cache.RecipeDaoFake
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.network.data.cache.model.RecipeEntityMapper
import com.example.recipecompose.network.data.network.RecipeService
import com.example.recipecompose.network.data.network.model.RecipeDtoMapper
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

class SearchRecipesTest {
    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var searchRecipes: SearchRecipes

    // Dependencies
    private lateinit var recipeService: RecipeService
    private lateinit var recipeDao: RecipeDaoFake
    private val dtoMapper = RecipeDtoMapper()
    private val entityMapper = RecipeEntityMapper()

    @BeforeEach
    fun set() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        recipeService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RecipeService::class.java)

        recipeDao = RecipeDaoFake(appDatabaseFake = appDatabase)

        // instantiate the system in test
        searchRecipes = SearchRecipes(
            recipeDao = recipeDao,
            recipeService = recipeService,
            entityMapper = entityMapper,
            dtoMapper = dtoMapper
        )
    }


    /**
     * 1. Are the recipes retrieved from the network
     * 2. Are the recipes inserted into the cache
     * 3. Are the recipes then emitted as a FLOW from the cache to the UI
     */

    @Test
    fun getRecipesFromNetwork_emitRecipesFromCache(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )

        // confirm the cache is empty to start
        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        val flowItems = searchRecipes.execute(
            DUMMY_TOKEN,
            1,
            DUMMY_QUERY,
            true
        ).toList()

        // confirm the cache is no longer empty
        assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

        // first emission should be LOADING status
        assert(flowItems[0].loading)

        // second emission should be the list of recipes
        val recipes = flowItems[1].data
        assert(recipes?.size ?: 0 > 0)

        // confirm they are actually Recipe objects
        assert(recipes?.get(index = 0) is Recipe)

        // confirm LOADING is false
        assert(!flowItems[1].loading)
    }

    @Test
    fun getRecipesFromNetwork_emitHttpError(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        val flowItems = searchRecipes.execute(
            DUMMY_TOKEN,
            1,
            DUMMY_QUERY,
            true
        ).toList()

        assert(flowItems[0].loading)

        val error = flowItems[1].error
        assert(error != null)

        assert(!flowItems[1].loading)

    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}