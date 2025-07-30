package com.dev2ever;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("logged-out")
@AnonymousAllowed
public class LoggedOutView extends VerticalLayout implements BeforeEnterObserver {

    public LoggedOutView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div card = new Div();
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("box-shadow", "var(--lumo-box-shadow-xl)")
                .set("padding", "var(--lumo-space-xl)")
                .set("text-align", "center")
                .set("max-width", "400px");

        H2 title = new H2("Logged Out Successfully");
        Paragraph message = new Paragraph("You have been logged out. Redirecting to login...");

        card.add(title, message);
        add(card);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Handle the state parameter from Keycloak
        // Schedule the redirect after the view is shown
        UI.getCurrent().getPage().executeJs(
                "setTimeout(function() { window.location.href = '/login'; }, 2000);"
        );
    }
}

