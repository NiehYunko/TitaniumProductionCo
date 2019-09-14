package com.titaniumproductionco.db.ui.component;

import javax.swing.JComboBox;

import com.titaniumproductionco.db.services.func.ConditionedView;
import com.titaniumproductionco.db.services.func.View;
import com.titaniumproductionco.db.ui.model.UIComboBoxModel;

/**
 * A JComboBox that picks value from a view.
 *
 */
@SuppressWarnings("serial")
public class ViewComboBox extends JComboBox<String> {
    public ViewComboBox(View v) {
        this(v, "*");
    }

    public ViewComboBox(View v, String column) {
        v.select(column, (rs) -> {
            UIComboBoxModel model = UIComboBoxModel.createComboBoxModel(rs);
            this.setModel(model);
        });
    }

    public ViewComboBox(ConditionedView v, String column) {
        v.select(column, (rs) -> {
            UIComboBoxModel model = UIComboBoxModel.createComboBoxModel(rs);
            this.setModel(model);
        });
    }

    public String getSelected() {
        return (String) getSelectedItem();
    }

}
