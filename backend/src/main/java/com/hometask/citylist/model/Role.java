package com.hometask.citylist.model;

import java.util.List;

/**
 * @author Elnur Mammadov
 */
public enum Role {

    GUEST,
    ROLE_ALLOW_EDIT(
        Privilege.CITY_VIEW,
        Privilege.CITY_EDIT
    );

    private final List<Privilege> privileges;

    Role(Privilege... privileges) {
        this.privileges = List.of(privileges);
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }
}
