package webscraper.model

/**
 * Data class for Ingredients
 * @author Pascal Kühnold
 *
 * @property amount the ingredient amount
 * @property name the name of the ingredient
 */
data class Ingredient(
    var amount: String = "",
    var name: String = ""
)
