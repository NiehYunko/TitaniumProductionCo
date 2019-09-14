package com.titaniumproductionco.db.ui;

import com.titaniumproductionco.db.services.Role;

public interface IUIFactory {
    UIFrame createUIFor(Role role);
}
