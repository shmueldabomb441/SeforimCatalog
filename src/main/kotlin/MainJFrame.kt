import java.awt.EventQueue
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
class MainJFrame : JFrame() {
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private fun initComponents() {
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
        jButton1 = JButton()
        jLabel1 = JLabel()
        jLabel2 = JLabel()
        defaultCloseOperation = EXIT_ON_CLOSE
        jTabbedPane1!!.addTab("Seforim by name", findSeferByNameJPanel1)
        jTabbedPane1!!.addTab("Seforim by criteria", seforimByCriteriaTabJPanel)
        jTabbedPane1!!.addTab("Criteria", criteriaTabJPanel)
        jTabbedPane1!!.addTab("Tips (5)", textJPanel1)
        jTabbedPane1!!.addTab("Help", textJPanel2)
        jButton1!!.text = "Refresh Database"
        jLabel1!!.text = "Database last updated: 12/13/14 12:40 PM"
        jLabel2!!.text = "Program Version: 1.0.0"
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
                                        .addComponent(jButton1)
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
                                .addComponent(jButton1)
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
    private var jButton1: JButton? = null
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
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
            try {
                for (info in UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus" == info.name) {
                        UIManager.setLookAndFeel(info.className)
                        break
                    }
                }
            } catch (ex: ClassNotFoundException) {
                Logger.getLogger(MainJFrame::class.java.name).log(Level.SEVERE, null, ex)
            } catch (ex: InstantiationException) {
                Logger.getLogger(MainJFrame::class.java.name).log(Level.SEVERE, null, ex)
            } catch (ex: IllegalAccessException) {
                Logger.getLogger(MainJFrame::class.java.name).log(Level.SEVERE, null, ex)
            } catch (ex: UnsupportedLookAndFeelException) {
                Logger.getLogger(MainJFrame::class.java.name).log(Level.SEVERE, null, ex)
            }
            //</editor-fold>

            /* Create and display the form */EventQueue.invokeLater { MainJFrame().isVisible = true }
        }
    }
}