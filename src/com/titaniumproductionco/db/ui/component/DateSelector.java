package com.titaniumproductionco.db.ui.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A component for selecting dates
 *
 */
@SuppressWarnings("serial")
public class DateSelector extends JButton implements ActionListener {
    private int year;
    private int month;
    private int day;

    public DateSelector() {
        year = -1;
        setPreferredSize(new Dimension(150, getFontMetrics(getFont()).getHeight() + 10));
        addActionListener(this);
    }

    @Override
    public String getText() {
        if (year < 0)
            return "Default";
        return year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
    }

    public Date getDate() {
        if (year < 0)
            return null;
        Calendar c = new GregorianCalendar(year, month - 1, day);
        return new Date(c.getTimeInMillis());
    }

    public void setDate(Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (year < 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        Input input = new Input(year, month, day);
        int op = JOptionPane.showConfirmDialog(this.getParent(), input, "Date", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            input.setDate(this);
        }
    }

    private static class Input extends JPanel {
        private JTextField year;
        private JTextField month;
        private JTextField day;

        private Input(int ayear, int amonth, int aday) {
            this.setLayout(new BorderLayout());

            FontMetrics fm = getFontMetrics(getFont());
            int width = fm.charWidth('0') + 1;
            int height = fm.getHeight() + 8;
            year = new JTextField();
            year.setPreferredSize(new Dimension(width * 5, height));
            if (ayear >= 0)
                year.setText(String.valueOf(ayear));
            else
                year.setText("1900");
            month = new JTextField();
            month.setPreferredSize(new Dimension(width * 3, height));
            if (ayear >= 0)
                month.setText(String.valueOf(amonth));
            else
                month.setText("01");
            day = new JTextField();
            day.setPreferredSize(new Dimension(width * 3, height));
            if (ayear >= 0)
                day.setText(String.valueOf(aday));
            else
                day.setText("01");
            this.add(new JLabel("Enter a date"), BorderLayout.NORTH);
            JPanel bot = new JPanel();
            bot.add(year);
            bot.add(new JLabel("-"));
            bot.add(month);
            bot.add(new JLabel("-"));
            bot.add(day);
            this.add(bot, BorderLayout.CENTER);

            year.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent arg0) {
                    if (getParsedYear() < 0) {
                        popError("Invalid Year!");
                        year.requestFocusInWindow();
                    }
                }
            });

            month.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent arg0) {
                    if (getParsedMonth() < 0) {
                        popError("Invalid Month!");
                        month.requestFocusInWindow();
                    }
                }
            });

            day.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent arg0) {
                    if (getParsedDay() < 0) {
                        popError("Invalid Day!");
                        day.requestFocusInWindow();
                    }
                }
            });
        }

        private void popError(String error) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }

        private int getParsedYear() {
            try {
                return Integer.parseInt(year.getText());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private int getParsedMonth() {
            try {
                int m = Integer.parseInt(month.getText());
                if (m > 12 || m <= 0)
                    return -1;
                return m;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private int getParsedDay() {
            try {
                int d = Integer.parseInt(day.getText());
                if (d <= 0 || d > 31)
                    return -1;
                return d;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private void setDate(DateSelector ds) {
            int yr = getParsedYear();
            int m = getParsedMonth();
            int d = getParsedDay();
            if (yr < 0 || m < 0 || d < 0) {
                ds.year = -1;
                return;
            }
            if (m == 2) {
                if (d > 29 || (yr % 4 != 0 && d > 28)) {
                    popError("Invalid Date!");
                    ds.year = -1;
                    return;
                }
            }
            if (m == 4 || m == 6 || m == 9 || m == 11) {
                if (d >= 31) {
                    popError("Invalid Date!");
                    ds.year = -1;
                    return;
                }
            }
            ds.year = yr;
            ds.month = m;
            ds.day = d;
        }
    }

    public static Date parseDate(String sqlDate) {
        if (sqlDate.length() != 10)
            return null;
        try {
            int year = Integer.parseInt(sqlDate.substring(0, 4));
            int month = Integer.parseInt(sqlDate.substring(5, 7));
            int day = Integer.parseInt(sqlDate.substring(8, 10));
            if (month <= 0 || day <= 0 || day > 31 || month > 12)
                return null;
            if (year < 0)
                return null;
            if (month == 2) {
                if (day > 29 || (year % 4 != 0 && day > 28)) {
                    return null;
                }
            }
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day >= 31) {
                    return null;
                }
            }
            Calendar c = new GregorianCalendar(year, month - 1, day);
            return new Date(c.getTimeInMillis());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Timestamp parseTimeStamp(String sqlTime) {
        if (sqlTime.length() != 23)
            return null;
        try {
            int year = Integer.parseInt(sqlTime.substring(0, 4));
            int month = Integer.parseInt(sqlTime.substring(5, 7));
            int day = Integer.parseInt(sqlTime.substring(8, 10));
            if (month <= 0 || day <= 0 || day > 31 || month > 12)
                return null;
            if (year < 0)
                return null;
            if (month == 2) {
                if (day > 29 || (year % 4 != 0 && day > 28)) {
                    return null;
                }
            }
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day >= 31) {
                    return null;
                }
            }
            Calendar c = new GregorianCalendar(year, month - 1, day);
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sqlTime.substring(11, 13)));
            c.set(Calendar.MINUTE, Integer.parseInt(sqlTime.substring(14, 16)));
            c.set(Calendar.SECOND, Integer.parseInt(sqlTime.substring(17, 19)));
            c.set(Calendar.MILLISECOND, Integer.parseInt(sqlTime.substring(20, 23)));
            return new Timestamp(c.getTimeInMillis());
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    public static Date daysAfterCurrentDate(int day) {
        return new Date(System.currentTimeMillis() + day * 24L * 60L * 60L * 1000L);
    }

}
