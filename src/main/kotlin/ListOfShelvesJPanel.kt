class ListOfShelvesJPanel : SearchableTableJPanel("Enter number of shelf$alternatePhrases:") {
    override val originalCollection: Collection<Any>
        get() = Catalog.entries
            .mapTo(mutableSetOf()) { it.shelfNum }
    override val listBeingDisplayed: MutableList<Any> = originalCollection.toMutableList().toSynchronizedList()
    override val displayingCatalogEntry: Boolean = false
    override val columns: List<String> = listOf("Shelf")
}
