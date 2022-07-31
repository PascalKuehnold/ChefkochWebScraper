import webscraper.ChefkochMealScraper


fun main(){
    println(
        ChefkochMealScraper("https://www.chefkoch.de/rezepte/1769391286881789/Zucchini-Omelett-mit-Kaese.html")
        .getExtractedMealData()
        .toString()
    )
}