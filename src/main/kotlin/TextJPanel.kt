import java.awt.Color
import java.awt.Font
import java.awt.event.HierarchyBoundsListener
import java.awt.event.HierarchyEvent
import javax.swing.*

/**
 *
 * @author shmuel
 */
class TextJPanel(val text: String) : JPanel() {
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private fun initComponents() {
        jScrollPane2 = JScrollPane()
        jTextArea1 = JTextArea()
        jTextArea1!!.isEditable = false
        jTextArea1!!.columns = 20
        jTextArea1!!.font = Font("Tahoma", 0, 14) // NOI18N
        jTextArea1!!.lineWrap = true
        jTextArea1!!.rows = 3
        jTextArea1!!.text = text
        jTextArea1!!.background = Color(215, 217, 223)
        jTextArea1!!.border = null
        jScrollPane2!!.border = null
        jTextArea1!!.wrapStyleWord = true
        jTextArea1!!.border = BorderFactory.createEmptyBorder(8, 8, 8, 8)
        jTextArea1!!.highlighter = null
        jTextArea1!!.maximumSize = null
        jTextArea1!!.preferredSize = jTextArea1!!.preferredSize
        jTextArea1!!.addHierarchyBoundsListener(object : HierarchyBoundsListener {
            override fun ancestorMoved(evt: HierarchyEvent) {}
            override fun ancestorResized(evt: HierarchyEvent) {
                jTextArea1AncestorResized(evt)
            }
        })
        jScrollPane2!!.setViewportView(jTextArea1)
        val layout = GroupLayout(this)
        this.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE.toInt())
                        .addContainerGap()
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE.toInt())
                        .addContainerGap()
                )
        )
    } // </editor-fold>                        

    private fun jTextArea1AncestorResized(evt: HierarchyEvent) {
        jTextArea1!!.preferredSize = null
    }

    // Variables declaration - do not modify                     
    private var jScrollPane2: JScrollPane? = null
    private var jTextArea1: JTextArea? = null // End of variables declaration                   

    /**
     * Creates new form TextJPanel
     */
    init {
        initComponents()
    }
}