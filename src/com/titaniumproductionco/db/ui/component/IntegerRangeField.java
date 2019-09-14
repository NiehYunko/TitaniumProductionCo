package com.titaniumproductionco.db.ui.component;

import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IntegerRangeField extends JPanel {
    private IntegerField from;
    private IntegerField to;

    public IntegerRangeField() {
        add(new JLabel("From"));
        add(from = new IntegerField());
        add(new JLabel("To"));
        add(to = new IntegerField());
        FontMetrics fm = getFontMetrics(getFont());
        from.setPreferredSize(new Dimension(fm.stringWidth("000000000"), fm.getHeight()));
        to.setPreferredSize(new Dimension(fm.stringWidth("000000000"), fm.getHeight()));

    }

    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        from.setEnabled(enable);
        to.setEnabled(enable);
    }

    public boolean isLowerBoundSet() {
        return from.isInputValidNeg() || from.isInputValidNonNeg();
    }

    public boolean isUpperBoundSet() {
        return to.isInputValidNeg() || to.isInputValidNonNeg();
    }

    public int getLowerBound() {
        if (isLowerBoundSet())
            return from.getParsedInt();
        return Short.MIN_VALUE;
    }

    public int getUpperBound() {
        if (isUpperBoundSet())
            return to.getParsedInt();
        return Short.MAX_VALUE;
    }
}
