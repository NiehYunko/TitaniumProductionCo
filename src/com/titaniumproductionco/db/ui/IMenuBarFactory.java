package com.titaniumproductionco.db.ui;

import javax.swing.JMenuBar;

import com.titaniumproductionco.db.services.Role;

public interface IMenuBarFactory {
    JMenuBar createMenuBarFor(Role role, UIFrame frame);
}
