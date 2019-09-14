package com.titaniumproductionco.db.ui.component;

import java.util.regex.Pattern;

import javax.swing.JTextField;

/**
 * A JTextField with integer-related validation functions
 *
 */
@SuppressWarnings("serial")
public class IntegerField extends JTextField {

    public boolean isInputValidPos() {
        return Pattern.matches("0*[1-9][0-9]*", getText());
    }

    public boolean isInputValidNonNeg() {
        return Pattern.matches("[0-9]+", getText());
    }

    public boolean isInputValidNeg() {
        return Pattern.matches("-0*[1-9][0-9]*", getText());
    }

    public int getParsedInt() {
        return Integer.parseInt(getText());
    }
}
