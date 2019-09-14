package com.titaniumproductionco.db.services;

public enum Role {
    HUMAN_RESOURCE(
        "HumanResource",
        "TiHumanResource",
        "Password123"),
    MINE_SUPERVISOR(
        "MineSupervisor",
        "TiMineSupervisor",
        "Password123"),
    MACHINE_OPERATOR(
        "MachineOperator",
        "TiMachineOperator",
        "Password123"),
    SALES_DEPART(
        "SalesDepartment",
        "TiSalesDepartment",
        "Password123"),
    FACTORY_SUPERVISOR(
        "FactorySupervisor",
        "TiFactorySupervisor",
        "Password123"),
    CEO(
        "CEO",
        "TiCEO",
        "Password123"),
    UNKNOWN(
        "",
        "",
        "");

    public final String NAME;
    public final String USERNAME;
    public final String PASSWORD;

    Role(String name, String username, String password) {
        NAME = name;
        USERNAME = username;
        PASSWORD = password;
    }

    @Override
    public String toString() {
        return NAME;
    }

    /**
     * Convert the name (not enum name) of a role to the role enum type
     * 
     * @param s name of the role
     * @return the enum type Role corresponding to the name, or UNKNOWN if the name
     *         does not match any role
     */
    public static Role toRoleSafe(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        for (Role r : Role.values()) {
            if (s.equals(r.NAME)) {
                return r;
            }
        }
        return UNKNOWN;
    }

    /**
     * 
     * @return true if the role needs an argumentID when registering
     */
    public boolean needsArgID() {
        return this == MINE_SUPERVISOR || this == MACHINE_OPERATOR || this == FACTORY_SUPERVISOR;
    }

    public static String[] roleNames() {
        Role[] r = values();
        String[] str = new String[r.length - 1];
        for (int i = 0, j = 0; i < r.length; i++) {
            if (r[i] != UNKNOWN) {
                str[j++] = r[i].NAME;
            }
        }
        return str;
    }
}
