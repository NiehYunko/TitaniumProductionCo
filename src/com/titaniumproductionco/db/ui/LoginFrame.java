package com.titaniumproductionco.db.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.titaniumproductionco.db.services.DBService;
import com.titaniumproductionco.db.services.LoginService;
import com.titaniumproductionco.db.services.Role;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements WindowListener, KeyListener, IUIFactory {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton connectButton;
    private JLabel connectLabel;

    private boolean loggedIn = false;

    public LoginFrame() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        JLabel logo = new JLabel(new ImageIcon(UIImage.BIG_ICON));
        logo.setPreferredSize(new Dimension(400, 100));
        add(logo, BorderLayout.NORTH);

        this.usernameLabel = new JLabel("Username");
        this.passwordLabel = new JLabel("Password");
        this.usernameField = new JTextField();
        this.passwordField = new JPasswordField();
        this.setPreferredSize(new Dimension(350, 300 + usernameField.getFontMetrics(usernameField.getFont()).getHeight() * 4));

        this.connectButton = new JButton("Login");
        this.connectLabel = new JLabel("Please Login");

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        fieldsPanel.add(this.usernameLabel);
        fieldsPanel.add(this.usernameField);
        fieldsPanel.add(this.passwordLabel);
        fieldsPanel.add(this.passwordField);

        this.add(fieldsPanel, BorderLayout.CENTER);
        JPanel downPanel = new JPanel(new BorderLayout());
        downPanel.add(this.connectLabel, BorderLayout.SOUTH);
        JPanel clickPanel = new JPanel(new GridLayout(1, 2));
        clickPanel.add(this.connectButton);
        downPanel.add(clickPanel, BorderLayout.CENTER);
        this.add(downPanel, BorderLayout.SOUTH);

        this.addWindowListener(this);

        this.connectButton.addActionListener((e) -> {
            loginButtonClicked();
        });

        this.passwordField.addKeyListener(this);

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.setTitle("TPC Login");

        this.setIconImage(UIImage.PROGRAM_ICON);

        this.setResizable(false);

        this.pack();
    }

    public void display() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void loginButtonClicked() {
        if (connectButton.isEnabled()) {
            connectLabel.setText("Logging In...");
            connectButton.setEnabled(false);
            Thread t = new Thread(() -> {
                boolean successful = LoginService.login(usernameField.getText(), passwordField.getPassword());
                if (successful) {
                    loggedIn = true;
                    this.dispose();

                    DBService.setRole(usernameField.getText());
                    UIFrame ui = this.createUIFor(DBService.getRole());
                    ui.setVisible(true);
                    // DataBaseUI gui = new DataBaseUI();
                    // gui.display();

                } else {
                    connectLabel.setText("Login Failed");
                    JOptionPane.showMessageDialog(null, "Login Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    connectButton.setEnabled(true);
                }
            });
            t.setName("LoginThread");
            t.start();
        }

    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        if (!this.loggedIn)
            DBService.close();
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            this.loginButtonClicked();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public UIFrame createUIFor(Role role) {
        UIFrame frame = new UIFrame();
        frame.setDesktopFactory(new DesktopFactory());
        frame.setInternalFrameFactory(new InternalFrameFactory());
        frame.setMenuBarFactory(new MenuBarFactory());
        frame.init(role);
        return frame;
    }
}
