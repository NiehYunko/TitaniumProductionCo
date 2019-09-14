package com.titaniumproductionco.db.ui.component;

import javax.swing.JTextField;

/**
 * A JTextField with decimal-related validation
 *
 */
@SuppressWarnings("serial")
public class DoubleField extends JTextField {

	public boolean isInputValidPos() {
		try {
			return getParsedDouble() > 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isInputValidNonNeg() {
		try {
			return getParsedDouble() >= 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isInputValidNeg() {
		try {
			return getParsedDouble() < 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public double getParsedDouble() {
		return Double.parseDouble(getText());
	}
}
