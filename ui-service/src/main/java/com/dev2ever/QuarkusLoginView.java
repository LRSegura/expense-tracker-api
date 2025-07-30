package com.dev2ever;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;

@Route("login")
@PageTitle("Login | Expense Tracker")
@AnonymousAllowed
@Menu(order = 2, icon = "USER")
public class QuarkusLoginView extends VerticalLayout implements BeforeEnterObserver {

    @Inject
    SecurityIdentity securityIdentity;

    public QuarkusLoginView() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Create login card
        Div loginCard = createLoginCard();
        add(loginCard);

        // Style the background
        getStyle()
                .set("background-color", "var(--lumo-contrast-5pct)")
                .set("background-image", "linear-gradient(135deg, var(--lumo-primary-color-10pct) 0%, var(--lumo-contrast-5pct) 100%)");
    }

    private Div createLoginCard() {
        Div card = new Div();
        card.addClassName("login-card");
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("box-shadow", "var(--lumo-box-shadow-xl)")
                .set("padding", "var(--lumo-space-xl)")
                .set("max-width", "400px")
                .set("width", "100%");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        // App icon/logo
        Icon icon = VaadinIcon.WALLET.create();
        icon.setSize("48px");
        icon.getStyle().set("color", "var(--lumo-primary-color)");

        // Title
        H2 title = new H2("Expense Tracker");
        title.getStyle()
                .set("margin", "0")
                .set("color", "var(--lumo-header-text-color)");

        // Description
        Paragraph description = new Paragraph("Sign in to manage your expenses and income");
        description.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("text-align", "center");

        // Login button
        Button loginButton = new Button("Sign in with Keycloak",
                new Icon(VaadinIcon.SIGN_IN));
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        loginButton.setWidthFull();
        loginButton.addClickListener(e -> redirectToKeycloak());

        // Security info
        Paragraph securityInfo = new Paragraph("🔒 Secure authentication powered by Keycloak");
        securityInfo.getStyle()
                .set("font-size", "var(--lumo-font-size-s)")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("text-align", "center")
                .set("margin-top", "var(--lumo-space-m)");

        content.add(icon, title, description, loginButton, securityInfo);
        card.add(content);

        return card;
    }

    private void redirectToKeycloak() {
        // Quarkus OIDC will handle the redirect automatically
        // We just need to navigate to a protected route
        UI.getCurrent().navigate("");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check if user is already authenticated
        if (securityIdentity != null && !securityIdentity.isAnonymous()) {
            // Already authenticated, redirect to main view
            event.forwardTo("");
        }
    }

}