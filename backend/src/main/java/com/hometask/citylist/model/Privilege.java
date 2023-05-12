package com.hometask.citylist.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Elnur Mammadov
 */
public enum Privilege implements GrantedAuthority {

    CITY_VIEW,
    CITY_EDIT;

    @Override
    public String getAuthority() {
        return toString();
    }
}
