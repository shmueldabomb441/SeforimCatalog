import java.awt.event.ActionEvent
import java.lang.Exception
import java.util.Locale
import java.lang.Runtime
import javax.swing.*

/**
 *
 * @author shmuel
 */
class HelpJPanel : JPanel() {
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private fun initComponents() {
        jLabel1 = JLabel()
        jButton1 = JButton()
        jButton2 = JButton()
        jLabel1!!.text =
            "If you need help regarding the structure or content of the catalog, please click \"Catalog inquiry\". Otherwise, click \"Tech support inquiry\"."
        jButton1!!.text = "Catalog inquiry"
        jButton1!!.addActionListener { evt -> jButton1ActionPerformed(evt) }
        jButton2!!.text = "Tech support inquiry"
        jButton2!!.addActionListener { evt -> jButton2ActionPerformed(evt) }
        val layout = GroupLayout(this)
        this.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jButton2)
                                .addComponent(jButton1)
                                .addComponent(jLabel1)
                        )
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                )
        )
    } // </editor-fold>                        

    private fun jButton2ActionPerformed(evt: ActionEvent) {
        openPage("https://forms.gle/9sVAf8rJ2LMoZ6o46")
    }

    fun openPage(url: String) {
        try {
            Runtime.getRuntime().exec(
                if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")) {
                    "rundll32 url.dll,FileProtocolHandler $url"
                } else {
                    "open $url"
                }
            )
        } catch (ex: Exception) {
            JOptionPane.showMessageDialog(
                null,
                "An error occurred. Please report this to Shmuel Sternbach: " + ex.message,
                "Error occurred",
                JOptionPane.ERROR_MESSAGE
            )
            ex.printStackTrace()
        }
    }

    private fun jButton1ActionPerformed(evt: ActionEvent) {
        openPage("https://forms.gle/RqhXc1d2x6CRYyyM7")
    }

    // Variables declaration - do not modify                     
    private var jButton1: JButton? = null
    private var jButton2: JButton? = null
    private var jLabel1: JLabel? = null // End of variables declaration                   

    /**
     * Creates new form HelpJPanel
     */
    init {
        initComponents()
    }
}