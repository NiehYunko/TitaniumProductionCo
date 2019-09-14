package com.titaniumproductionco.db.ui.component;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class RadioSelectionPanel extends JPanel {
    private ArrayList<JRadioButton> radioButtons;
    private ArrayList<JComponent> components;
    private int selectedComponent;
    private ButtonGroup buttonGroup;

    public RadioSelectionPanel() {
        radioButtons = new ArrayList<>();
        components = new ArrayList<>();
        buttonGroup = new ButtonGroup();
        selectedComponent = -1;
    }

    public void addSelectionComponent(String text, JComponent comp) {
        JRadioButton button = newRadioButton(text);
        radioButtons.add(button);
        components.add(comp);
        add(button);
        add(comp);
        comp.setEnabled(false);
        if (selectedComponent == -1) {
            button.setSelected(true);
            radioButtonAction(null);
        }

    }

    public JComponent getEnabledComponent() {
        return components.get(selectedComponent);
    }

    public void radioButtonAction(ActionEvent e) {
        Object source = e == null ? null : e.getSource();
        if (source == null) {
            selectedComponent = 0;
            source = radioButtons.get(0);
        }
        for (int i = 0; i < radioButtons.size(); i++) {
            components.get(i).setEnabled(false);
            if (source == radioButtons.get(i)) {
                components.get(i).setEnabled(true);
                selectedComponent = i;
            }
        }
    }

    private JRadioButton newRadioButton(String text) {
        JRadioButton button = new JRadioButton(text);
        button.addActionListener(this::radioButtonAction);
        buttonGroup.add(button);
        return button;
    }
}
