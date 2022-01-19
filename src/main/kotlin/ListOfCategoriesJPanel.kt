class ListOfCategoriesJPanel : SearchableTableJPanel("Enter name of category:") {
    override val originalCollection: Collection<Any>
        get() = Catalog.entries
            .mapTo(mutableSetOf()) { it.category }
    override val listBeingDisplayed: MutableList<Any> = originalCollection.toMutableList()
    override val displayingCatalogEntry: Boolean = false
    override val columns: List<String> = listOf("Category")
}