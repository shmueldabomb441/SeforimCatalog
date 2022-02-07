import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.awt.EventQueue
import java.io.File
import kotlin.jvm.JvmStatic
import java.lang.ClassNotFoundException
import java.lang.InstantiationException
import java.lang.IllegalAccessException
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.*

/**
 *
 * @author shmuel
 */
lateinit var logFile: File
val scope = CoroutineScope(SupervisorJob())
class MainJFrame : JFrame() {
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="expanded" desc="Generated Code">
    private fun initComponents() {

        extendedState = JFrame.MAXIMIZED_BOTH;
        isUndecorated = false;
        jTabbedPane1 = JTabbedPane()
        findSeferByNameJPanel1 = (FindSeferByCriteriaJPanel("Enter name of sefer$alternatePhrases:") { it.seferName }).initComponents() as FindSeferByCriteriaJPanel
        seforimByCriteriaTabJPanel = TabJPanel(
            listOf(
                "Seforim by author" to (FindSeferByCriteriaJPanel("Enter author of sefer$alternatePhrases:") { it.author }).initComponents(),
                "Seforim by category" to (FindSeferByCriteriaJPanel("Enter category of sefer$alternatePhrases:") { it.category }).initComponents(),
                "Seforim by publisher" to (FindSeferByCriteriaJPanel("Enter publisher of sefer$alternatePhrases:") { it.publisher }).initComponents(),
                "Seforim by shelf" to (FindSeferByCriteriaJPanel("Enter shelf of sefer$alternatePhrases:") { it.shelfNum }).initComponents(),
            )
        )
        criteriaTabJPanel = TabJPanel(
            listOf(
                "Authors" to ListOfAuthorsJPanel().initComponents(),
                "Categories" to ListOfCategoriesJPanel().initComponents(),
                "Publishers" to ListOfPublishersJPanel().initComponents(),
                "Shelves" to ListOfShelvesJPanel().initComponents()
            )
        )
        textJPanel1 = TextJPanel(TIPS)
        textJPanel2 = TextJPanel("For tech support, please contact ssternbach@torahdownloads.com; for catalog support, please contact Asher Lewis.")
        refreshDatabaseButton = JButton()
        jLabel1 = JLabel()
        jLabel2 = JLabel()
        defaultCloseOperation = EXIT_ON_CLOSE
        jTabbedPane1!!.addTab("Seforim by name", findSeferByNameJPanel1)
        jTabbedPane1!!.addTab("Seforim by criteria", seforimByCriteriaTabJPanel)
        jTabbedPane1!!.addTab("Criteria", criteriaTabJPanel)
        jTabbedPane1!!.addTab("Tips (6)", textJPanel1)
        jTabbedPane1!!.addTab("Help", HelpJPanel())
        refreshDatabaseButton!!.text = "Refresh Catalog"

        val getLastUpdateString = { "Catalog last updated: ${Catalog.lastModificationDate()}"}
        jLabel1!!.text = getLastUpdateString()
        jLabel2!!.text = "Program Version: 1.0.0"

        refreshDatabaseButton!!.addActionListener {
            Catalog.refreshObjects()
            jLabel1!!.text = getLastUpdateString()
            //LevenshteinDistance()
            (0 until jTabbedPane1!!.tabCount).map { jTabbedPane1!!.getTabComponentAt(it) }.forEach {
                if(it is SearchableTableJPanel) {
                    try {
                        it.filterList()
                    } catch (t: Throwable) { t.printStackTrace() }
                }
            }
        }
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jTabbedPane1)
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addComponent(refreshDatabaseButton)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(
                                            LayoutStyle.ComponentPlacement.RELATED,
                                            GroupLayout.DEFAULT_SIZE,
                                            Short.MAX_VALUE.toInt()
                                        )
                                        .addComponent(jLabel2)
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
                        .addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE.toInt())
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(refreshDatabaseButton)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                        )
                        .addContainerGap()
                )
        )
        pack()
    } // </editor-fold>                        

    // Variables declaration - do not modify                     
    private var findSeferByNameJPanel1: FindSeferByCriteriaJPanel? = null
    private var refreshDatabaseButton: JButton? = null
    private var jLabel1: JLabel? = null
    private var jLabel2: JLabel? = null
    private var jTabbedPane1: JTabbedPane? = null
    private var seforimByCriteriaTabJPanel: TabJPanel? = null
    private var criteriaTabJPanel: TabJPanel? = null
    private var textJPanel1: TextJPanel? = null
    private var textJPanel2: TextJPanel? = null // End of variables declaration                   

    /**
     * Creates new form MainJFrame
     */
    init {
        initComponents()
    }

    companion object {
        /**
         * @param args the command line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // Set the Nimbus look and feel
            // For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
            UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels().find { it.name == "Nimbus" }?.className)
            args.getOrNull(0)?.let {
                catalogDirectory = File(it)
                logFile = File(catalogDirectory,"logs.txt")
            }
            /* Create and display the form */EventQueue.invokeLater {
                MainJFrame().apply {
                    title = "Seforim Finder"
                    isVisible = true
                    args.getOrNull(1)?.let { iconImage = ImageIcon(it).image }
                }
            }
        }
    }
}