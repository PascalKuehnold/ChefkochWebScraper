package webscraper

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extractIt
import it.skrape.selects.and
import it.skrape.selects.eachText
import it.skrape.selects.html5.a
import it.skrape.skrape
import webscraper.model.ExtracedData
import webscraper.model.Ingredient

class WebScraper {
    var pageLink: String = ""

    //IMPORTANT Url must have ?portionen=2 for correct scraping -> standard route
    val extracted = skrape(HttpFetcher) {
        request {
            url =
                "https://www.chefkoch.de/rezepte/1769391286881789/Zucchini-Omelett-mit-Kaese.html?portionen=2"
            pageLink = url
        }

        extractIt<ExtracedData> { it ->
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

                it.preperationText =
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

    fun printValue() {
        println(
            extracted
        )
    }
}