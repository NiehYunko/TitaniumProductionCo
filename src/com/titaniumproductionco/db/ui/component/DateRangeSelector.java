package com.titaniumproductionco.db.ui.component;

import java.sql.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DateRangeSelector extends JPanel {
    private DateSelector fromDate;
    private DateSelector toDate;

    public DateRangeSelector() {
        add(new JLabel("From"));
        add(fromDate = new DateSelector());
        add(new JLabel("To"));
        add(toDate = new DateSelector());
    }

    public void setFromDate(Date from) {
        fromDate.setDate(from);
    }

    public void setToDate(Date to) {
        toDate.setDate(to);
    }

    public Date getFromDate() {
        return fromDate.getDate();
    }

    public Date getToDate() {
        return toDate.getDate();
    }
}
