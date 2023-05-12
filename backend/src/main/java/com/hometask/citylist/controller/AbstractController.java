package com.hometask.citylist.controller;

import com.hometask.citylist.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Elnur Mammadov
 */
public abstract class AbstractController {

    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAILURE = "FAILURE";

    public static Map<String, Object> successReply(String message) {
        var reply = new HashMap<String, Object>();
        reply.put("message", message);
        reply.put("status", STATUS_SUCCESS);
        reply.put("result", true);
        return reply;
    }

    public static Map<String, Object> failureReply(String message) {
        var reply = new HashMap<String, Object>();
        reply.put("message", message);
        reply.put("status", STATUS_FAILURE);
        reply.put("result", false);
        return reply;
    }

    public static User getUserOrThrow() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = auth.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new RuntimeException("can't authenticate user");
    }
}
