package com.titaniumproductionco.db.generator;

import com.titaniumproductionco.db.services.LoginService;
import com.titaniumproductionco.db.services.Role;

public class UserGenerator {
    private int uid;
    private int argID;
    private Role role;

    public UserGenerator(Role r, int argID) {
        role = r;
        this.argID = argID;
    }

    public String[] generate(int id, DataGenerator gen) {
        uid = id;
        byte[] salt = LoginService.newSalt();
        String saltString = LoginService.bytesToString(salt);
        String hashPass = LoginService.hashPassword(salt, "boss".toCharArray());
        String roleName = role.NAME;
        String username = Dictionary.randomPerson(gen.rand());
        return new String[] { id + "", username, saltString, hashPass, roleName };
    }

    public String[] generateArg(Role targetRole) {
        if (role == targetRole) {
            return new String[] { uid + "", argID + "" };
        }
        return null;
    }
}
