package io.rainfall.store.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@SuppressWarnings("unused")
public class CommonAdvice {

    @Value("${server.servlet.context-path:/performance}")
    private String contextPath;

    @ModelAttribute
    public void addContextPath(Model model) {
        model.addAttribute("context-path", contextPath);
    }
}
