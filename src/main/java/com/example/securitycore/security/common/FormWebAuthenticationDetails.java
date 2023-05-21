package com.example.securitycore.security.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Getter
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {
    private String secret_key;

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secret_key = request.getParameter("secret_key");

    }
}
