
package com.api.kdevfolio.analytics.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ClientIpResolver {

    public String resolve(HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        if (ip == null || ip.isBlank()) {
            return null;
        }

        return ip;
    }
}
