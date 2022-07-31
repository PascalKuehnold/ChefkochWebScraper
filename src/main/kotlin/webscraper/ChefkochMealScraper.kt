package webscraper

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extractIt
import it.skrape.selects.and
import it.skrape.selects.eachText
import it.skrape.selects.html5.a
import it.skrape.skrape
import webscraper.model.ExtractedData
import webscraper.model.Ingredient

/**
 * The main class for extracting the data from a given url from Chefkoch.de
 * The data is defined by the ExtractedData data class
 * The library used for extracting the data is skrape{it}
 * @author Pascal Kühnold
 * @see <a href="https://docs.skrape.it/docs/">https://docs.skrape.it/docs/</a>
 *
 * @property extracted the extracted data from the website, defined be the ExtractedData dataclass
 * @see ExtractedData
 *
 * @property pageLink the url the user wants to visit to get the data from the website
 *
 */
class ChefkochMealScraper(recipeUrl: String) {

    private val _url = checkIfValidUrl(recipeUrl)

    private fun checkIfValidUrl(recipeUrl: String): String {
        val regex = Regex(pattern = "https://www.chefkoch.de/rezepte/\\d*\\/.*\\.html", options = setOf(RegexOption.IGNORE_CASE))
        val matches = regex.matches(recipeUrl)
        println(matches)

        return if(matches){
            "$recipeUrl?portionen=2"
        } else {
            ""
        }
    }

    private var pageLink: String = ""

    //IMPORTANT Url must have ?portionen=2 for correct scraping -> standard route
    //Url should be dynamic and not hardcoded
    private val extracted = skrape(HttpFetcher) {
        request {
            url =
                _url
            pageLink = url
        }

        extractIt<ExtractedData> { it ->
            val ingredients: MutableList<Ingredient> = mutableListOf()

            it.httpMessage = status { message }

            htmlDocument {
                relaxed = true

                it.pageLink = pageLink
                it.mealName = ".recipe-header h1" { findFirst { text } }
                it.authorName = ".ds-mb-right .bi-profile span" { findFirst { text } }
                it.imageLink =
                    ".recipe-image" { findFirst { eachImage.map { image -> image.value } }.take(1).toString() }
                it.cookTime = ".recipe-meta .recipe-preptime" { findFirst { text } }.drop(1).trim()
                it.difficulty = ".recipe-meta .recipe-difficulty" { findFirst { text } }.drop(1).trim()
                it.kcal = ".recipe-meta .recipe-kcalories" { findFirst { text } }.drop(1).trim()

                val ingredientAmount = ".recipe-ingredients .td-left" {
                    findAll() {
                        eachText
                    }
                }
                val ingredientName = ".recipe-ingredients .td-right" {
                    findAll() {
                        eachText
                    }
                }

                for (i in ingredientAmount.indices) {
                    ingredients.add(i, Ingredient(ingredientAmount[i], ingredientName[i]))
                }


                it.ingredients = ingredients

                it.preparationText =
                    "body > main > article.ds-box.ds-grid-float.ds-col-12.ds-col-m-8.ds-or-3 > div:nth-child(3)" {
                        findFirst { text }
                    }
                it.tags = a {
                        withClass = "ds-tag" and "bi-tags"
                        findAll {
                            eachText
                        }
                    }
            }
        }
    }

    /**
     * Method for getting the extracted data
     *
     * @return the extracted Data as an ExtractedData class
     */
    fun getExtractedMealData(): ExtractedData{
        return extracted
    }

    /**
     * Debug method
     */
    fun printValue() {
        println(
            extracted
        )
    }

}