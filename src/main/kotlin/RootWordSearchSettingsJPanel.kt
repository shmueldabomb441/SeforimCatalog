import java.awt.EventQueue
import java.awt.event.ActionEvent
import javax.swing.*

/**
 *
 * @author shmuel
 */
class RootWordSearchSettingsJPanel(val searchableTableJPanel: SearchableTableJPanel) : JPanel() {
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private fun initComponents() {
        jPanel5 = JPanel()
        sequentialRadioButton = JRadioButton()
        unorderedRadioButton = JRadioButton()
        sequentialLabel = JLabel()
        unorderedLabel = JLabel()
        jPanel16 = JPanel()
        matchesAllRadioButton = JRadioButton()
        matchesAnyRadioButton = JRadioButton()
        matchesAllLabel = JLabel()
        matchesAny = JLabel()
        jPanel5!!.border = BorderFactory.createTitledBorder("Word order mode")
        jPanel16!!.border = BorderFactory.createTitledBorder("Word inclusivity mode")

        sequentialRadioButton!!.text = "Ordered/Sequential"
        sequentialLabel!!.text =
            "<html><body style='width: 1%spx'>Respect the order that the words in the search phrase appear in, and only return results in which the shorashim appear in the same order as the order in which they appear in the search phrase."

        unorderedRadioButton!!.text = "Unordered/Keywords"
        unorderedLabel!!.text =
            "Disregard the order that the words in the search phrase appear in, and return results which contain the shorashim that are in the search phrase, regardless of the order in which they appear in the result."

        matchesAllRadioButton!!.text = "Match all"
        matchesAllLabel!!.text = "Only return results which contain all of the shorashim contained in the search phrase."

        matchesAnyRadioButton!!.text = "Match any"
        matchesAny!!.text = "Return results which contain any (even one) of the shorashim in the search phrase. This is by definition neither sequential nor unordered."


        val orderRadioGroup = ButtonGroup()
        val strictnessButtonGroup = ButtonGroup()
        orderRadioGroup.add(sequentialRadioButton)
        orderRadioGroup.add(unorderedRadioButton)
        strictnessButtonGroup.add(matchesAllRadioButton)
        strictnessButtonGroup.add(matchesAnyRadioButton)
        var buttonToReenableAfterSelectingMatchAny: JRadioButton? = null
        sequentialRadioButton!!.addActionListener {
            rootSearchShouldMatchSequential = true
            searchableTableJPanel.filterList()
        }
        unorderedRadioButton!!.addActionListener {
            rootSearchShouldMatchSequential = false
            searchableTableJPanel.filterList()
        }
        matchesAllRadioButton!!.addActionListener {
            rootSearchShouldMatchAll = true
            searchableTableJPanel.filterList()
            if(buttonToReenableAfterSelectingMatchAny != null){
                orderRadioGroup.setSelected(buttonToReenableAfterSelectingMatchAny?.model, true)
                orderRadioGroup.elements.iterator().forEach { it.isEnabled = true }
                buttonToReenableAfterSelectingMatchAny = null
            }
        }
        matchesAnyRadioButton!!.addActionListener {
            rootSearchShouldMatchAll = false
            searchableTableJPanel.filterList()
            buttonToReenableAfterSelectingMatchAny = orderRadioGroup.selectedRadioButton()
            orderRadioGroup.clearSelection()
            orderRadioGroup.elements.iterator().forEach { it.isEnabled = false }
        }
        EventQueue.invokeLater {
            sequentialRadioButton!!.doClick()
        }
        EventQueue.invokeLater {
            matchesAllRadioButton!!.doClick()
        }
        val jPanel5Layout = GroupLayout(jPanel5)
        jPanel5!!.layout = jPanel5Layout
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    sequentialRadioButton,
                                    GroupLayout.PREFERRED_SIZE,
                                    1297,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    unorderedRadioButton,
                                    GroupLayout.PREFERRED_SIZE,
                                    983,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addGroup(
                                    jPanel5Layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addGroup(
                                            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(
                                                    unorderedLabel,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    1268,
                                                    GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(sequentialLabel)
                                        )
                                )
                        )
                        .addGap(0, 0, 0)
                )
        )
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sequentialRadioButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            LayoutStyle.ComponentPlacement.RELATED,
                            GroupLayout.DEFAULT_SIZE,
                            Short.MAX_VALUE.toInt()
                        )
                        .addComponent(sequentialLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(unorderedRadioButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(unorderedLabel)
                        .addContainerGap()
                )
        )
        val jPanel16Layout = GroupLayout(jPanel16)
        jPanel16!!.layout = jPanel16Layout
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    jPanel16Layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addGroup(
                                            jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(matchesAllLabel)
                                                .addComponent(matchesAny)
                                        )
                                )
                                .addGroup(
                                    jPanel16Layout.createSequentialGroup()
                                        .addGroup(
                                            jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(
                                                    matchesAnyRadioButton,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    983,
                                                    GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(
                                                    matchesAllRadioButton,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    Short.MAX_VALUE.toInt()
                                                )
                                        )
                                        .addContainerGap()
                                )
                        )
                )
        )
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(matchesAllRadioButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(matchesAllLabel)
                        .addPreferredGap(
                            LayoutStyle.ComponentPlacement.RELATED,
                            GroupLayout.DEFAULT_SIZE,
                            Short.MAX_VALUE.toInt()
                        )
                        .addComponent(matchesAnyRadioButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(matchesAny)
                        .addContainerGap()
                )
        )
        val layout = GroupLayout(this)
        this.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 1327, Short.MAX_VALUE.toInt())
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                            layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                    layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(
                                            jPanel16,
                                            GroupLayout.Alignment.LEADING,
                                            GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.DEFAULT_SIZE,
                                            Short.MAX_VALUE.toInt()
                                        )
                                        .addComponent(
                                            jPanel5,
                                            GroupLayout.Alignment.LEADING,
                                            GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.DEFAULT_SIZE,
                                            Short.MAX_VALUE.toInt()
                                        )
                                )
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                        )
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 300, Short.MAX_VALUE.toInt())
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                            layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(
                                    jPanel5,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(
                                    jPanel16,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addContainerGap(12, Short.MAX_VALUE.toInt())
                        )
                )
        )
    } // </editor-fold>

    private fun ButtonGroup.selectedRadioButton(): JRadioButton = elements.asSequence().find { isSelected(it.model)  } as JRadioButton

    private fun jRadioButton6ActionPerformed(evt: ActionEvent) {
        // TODO add your handling code here:
    }

    private fun jRadioButton7ActionPerformed(evt: ActionEvent) {
        // TODO add your handling code here:
    }

    private fun jRadioButton28ActionPerformed(evt: ActionEvent) {
        // TODO add your handling code here:
    }

    private fun jRadioButton29ActionPerformed(evt: ActionEvent) {
        // TODO add your handling code here:
    }

    // Variables declaration - do not modify                     
    private var sequentialLabel: JLabel? = null
    private var unorderedLabel: JLabel? = null
    private var matchesAllLabel: JLabel? = null
    private var matchesAny: JLabel? = null
    private var jPanel16: JPanel? = null
    private var jPanel5: JPanel? = null
    private var matchesAllRadioButton: JRadioButton? = null
    private var matchesAnyRadioButton: JRadioButton? = null
    private var sequentialRadioButton: JRadioButton? = null
    private var unorderedRadioButton: JRadioButton? = null // End of variables declaration

    /**
     * Creates new form RootWordSearchSettingsJPanel
     * @param searchableTableJPanel
     */
    init {
        initComponents()
    }
}