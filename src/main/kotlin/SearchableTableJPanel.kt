import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*
import javax.swing.*
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableRowSorter

abstract class SearchableTableJPanel(
    private val searchPhrase: String
    ): JPanel() {
    open val originalCollection: Collection<Any> = emptyList()
    open val listBeingDisplayed: MutableList<Any> = mutableListOf()
    open val displayingCatalogEntry: Boolean = false //if false, return listBeingDisplayed[rowNum] in table model, otherwise return value depending on columnNum
    open val columns = emptyList<String>()

    var _constraint: Regex? = null //singleton pattern
    var _searchPhrase: String? = null
    /*only gets regex when the constraint hasn't changed, so that it doesn't create a new regex for every list item*/
    fun getConstraint(constraint: String) =
        (if(constraint == _searchPhrase /*already got regex*/) _constraint /*use "old" regex*/ else /*get new regex*/ null) ?: constraint
            .replace("#", "|")
            .toRegex(/*RegexOption.IGNORE_CASE*/)
            .also {
                _constraint = it
                _searchPhrase = constraint
                println("Pattern of constraint: $it")
            }

    open fun getCriteria(entry: CatalogEntry): String = ""//has default implementation so that JPanels which don't contain CatalogEntrys (e.g. list of authors) don't need to implement it
    open fun matchesConstraint(element: String, constraint: String) = element.contains(getConstraint(constraint)).also {/* println("matchesConstraint(element: String) called")*/ }
    open fun matchesConstraint(element: CatalogEntry, constraint: String) = getCriteria(element).contains(getConstraint(constraint)).also { /*println("matchesConstraint(element: CatalogEntry) called")*/ }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="expanded" desc="Generated Code">
    fun initComponents(): SearchableTableJPanel {
        jLabel1 = JLabel()
        seferNameTextField = JTextField()
        jScrollPane1 = JScrollPane()
        table = JTable()
        jLabel1.text = searchPhrase
        table.model = catalogModel()
        val rightToLeftAlignmentRenderer = DefaultTableCellRenderer()
        rightToLeftAlignmentRenderer.horizontalAlignment = JLabel.RIGHT
        table.columnModel.columns.asIterator().forEach { it.cellRenderer = rightToLeftAlignmentRenderer }
        table.font = Font("Default", 0, 14)
        jScrollPane1.setViewportView(table)
        seferNameTextField.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {}

            override fun keyPressed(e: KeyEvent?) {}

            override fun keyReleased(e: KeyEvent?) {
                val constraint = seferNameTextField.text
                filterList(constraint)
            }
        }
        )
//        table.autoCreateRowSorter = true
        val rowSorter = TableRowSorter(table.model as AbstractTableModel)
        val comparator = kotlin.Comparator<String> { o1, o2 ->
            val o1ContainsEnglish = Catalog.containsEnglish(o1)
            val o2ContainsEnglish = Catalog.containsEnglish(o2)
            if (o1ContainsEnglish && !o2ContainsEnglish) 1
            else if(!o1ContainsEnglish && o2ContainsEnglish) -1
            else o1.compareTo(o2)
        }
        val columnIndexToSort = minOf(columns.size - 1, 1)//if only 1 column (e.g. authors), index 0, else "name of sefer" column
        rowSorter.setComparator(columnIndexToSort, comparator)
        table.rowSorter = rowSorter
        rowSorter.sortKeys = listOf(RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING))

        val layout = GroupLayout(this)
        setLayout(layout)
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE.toInt())
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(seferNameTextField)
                                )
                        )
                        .addContainerGap()
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(
                                    seferNameTextField,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE.toInt())
                        .addContainerGap()
                )
        )
        return this
    } // </editor-fold>

    private fun catalogModel() = object : AbstractTableModel() {

        override fun getColumnName(col: Int): String = columns[col] //same as { return columns[col] }

        override fun getRowCount(): Int = listBeingDisplayed.size

        override fun getColumnCount(): Int = columns.size

        override fun getValueAt(row: Int, col: Int): Any {
            return listBeingDisplayed[row].let {
                if(!displayingCatalogEntry) it else {
                    it as CatalogEntry
                    when(col) {
                        0 ->  it.shelfNum
                        1 ->  it.seferName
                        2 ->  it.author
                        3 ->  it.publisher
                        4 ->  it.volumeNum
                        5 ->  it.numCopies
                        6 ->  it.category
                        else -> TODO("This should not have happened: getValueAt($row:, $col)")
                    }
                }
            }
        }

        override fun isCellEditable(row: Int, col: Int): Boolean = false

    }.also { tableModel = it }

    private lateinit var jLabel1: JLabel
    private lateinit var jScrollPane1: JScrollPane
    lateinit var seferNameTextField: JTextField
    lateinit var table: JTable
    lateinit var tableModel: AbstractTableModel
    var oldList = originalCollection
    fun filterList(constraint: String) {
        println("Key released, constraint=$constraint, list size: ${listBeingDisplayed.size}")
        if(constraint.isBlank()) {
            println("Constraint is blank, resetting list.")
            listBeingDisplayed.clear()
            listBeingDisplayed.addAll(originalCollection)
            (table.model as AbstractTableModel).fireTableDataChanged()
            return
        }
        val newList = Collections.synchronizedList(mutableListOf<Any>())
        originalCollection
            .also { if(it != oldList) {
                println("Original list changed: ${it.size}, ${oldList.size}")
                oldList = it
            } }
            .parallelStream()
            .filter {
                when (it) {
                    is String -> matchesConstraint(it, constraint) //need to have seperate branches to guarantee the compiler of its type
                    is CatalogEntry -> matchesConstraint(it, constraint)
                    else -> TODO("This should never happen, it=$it, it.javaClass = ${it.javaClass}")
                }
            }
            .forEach {
//                println("Item being added: $it")
                newList.add(it)
            }
        listBeingDisplayed.clear()
        listBeingDisplayed.addAll(newList)
        (table.model as AbstractTableModel).fireTableDataChanged()
        println("List updated, size: ${listBeingDisplayed.size}")
    }
}