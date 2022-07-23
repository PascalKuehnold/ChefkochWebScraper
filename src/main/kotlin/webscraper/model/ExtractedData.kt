package webscraper.model

/**
 * Data class for the desired data you want to extract from the website
 * @property httpMessage the http message you receive when trying to connect to the website
 * @property pageLink the url of the website you want to extract data from
 * @property mealName the name of the meal
 * @property authorName the name of the author who created the recipe
 * @property imageLink the link to the first image of the recipe
 * @property cookTime the overall cooking time defined by the author
 * @property difficulty the difficulty of the recipe defined by the author
 * @property kcal the overall kcal for the meal
 * @property ingredientsForServings the number of servings for the given meal (the visited link will automatically start with 2)
 * @property ingredients the ingredients you need for the recipe
 * @see Ingredient
 * @property tags the given tags for the recipe, defined by the author
 *
 * @author Pascal Kühnold
 */
data class ExtracedData(
    var httpMessage: String = "",
    var pageLink: String = "",
    var mealName: String = "",
    var authorName: String = "",
    var imageLink: String = "",
    var cookTime: String = "",
    var difficulty: String = "",
    var kcal: String = "",
    var ingredientsForServings: Int = 2,
    var ingredients: List<Ingredient> = emptyList(),
    var preparationText: String = "",
    var tags: List<String> = emptyList()

) {


    override fun toString(): String {
        return "ExtracedData(\n" +
                " httpMessage='$httpMessage',\n" +
                " pageLink='$pageLink',\n" +
                " mealName='$mealName',\n" +
                " authorName='$authorName',\n" +
                " imageLink='$imageLink',\n" +
                " cookTime='$cookTime',\n" +
                " difficulty='$difficulty',\n" +
                " kcal='$kcal',\n" +
                " ingredientsForServings=$ingredientsForServings,\n" +
                " ingredients=$ingredients,\n" +
                " preperationText='$preparationText',\n" +
                " tags=$tags" +
                ")"
    }
}
