package com.dev2ever;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = QuarkusMainLayout.class)
@RouteAlias(value = "", layout = QuarkusMainLayout.class) // Default route after login
@Authenticated
@Menu(order = 1, icon = "dashboard")
public class QuarkusDashboardView extends VerticalLayout {

    @Inject
    SecurityIdentity securityIdentity;

    private H2 welcomeHeader;
    private Paragraph roleInfo;
    private boolean contentInitialized = false;

    public QuarkusDashboardView() {
        setSpacing(true);
        setPadding(true);
        addClassName("dashboard-view");

        // Initialize basic layout without security-dependent content
        initializeBasicLayout();
    }

    private void logout() {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Confirm Logout");

        Paragraph message = new Paragraph("Are you sure you want to logout?");
        confirmDialog.add(message);

        Button confirmButton = new Button("Logout", e -> {
            confirmDialog.close();
            // Use Quarkus's built-in OIDC logout endpoint
            UI.getCurrent().getPage().setLocation("/q/logout");
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancel", e -> confirmDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, confirmButton);
        buttons.setJustifyContentMode(JustifyContentMode.END);

        confirmDialog.getFooter().add(buttons);
        confirmDialog.open();
    }


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Initialize security-dependent content after component is attached
        if (!contentInitialized && isSecurityContextAvailable()) {
            initializeSecurityDependentContent();
            contentInitialized = true;
        }
    }

    private void initializeBasicLayout() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        VerticalLayout welcomeSection = new VerticalLayout();
        welcomeSection.setPadding(false);
        welcomeSection.setSpacing(false);



        // Create placeholder elements that will be updated when security context is available
        welcomeHeader = new H2("Welcome to Expense Tracker!");
        welcomeHeader.addClassName(LumoUtility.TextColor.PRIMARY);

        Paragraph intro = new Paragraph("Track your expenses and income to better manage your finances.");
        intro.addClassName(LumoUtility.TextColor.SECONDARY);

        roleInfo = new Paragraph("Loading user information...");
        roleInfo.addClassName(LumoUtility.FontSize.SMALL);
        roleInfo.addClassName(LumoUtility.TextColor.TERTIARY);



        Button logoutButton = new Button("Logout", VaadinIcon.SIGN_OUT.create());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logoutButton.addClickListener(e -> logout());
        header.add(welcomeSection, logoutButton);

        add(header, welcomeHeader, intro, roleInfo);

        // Add dashboard widgets/cards
        createDashboardCards();
    }

    private void initializeSecurityDependentContent() {
        String username = getUsernameSafely();
        String roles = getRolesSafely();

        // Update welcome message with actual username
        welcomeHeader.setText("Welcome to Expense Tracker, " + username + "!");

        // Update role information
        roleInfo.setText("Your roles: " + roles);
    }

    private boolean isSecurityContextAvailable() {
        return securityIdentity != null && securityIdentity.getPrincipal() != null;
    }

    private String getUsernameSafely() {
        if (isSecurityContextAvailable()) {
            return securityIdentity.getPrincipal().getName();
        }
        return "Guest";
    }

    private String getRolesSafely() {
        if (isSecurityContextAvailable() && securityIdentity.getRoles() != null) {
            return String.join(", ", securityIdentity.getRoles());
        }
        return "No roles assigned";
    }

    private void createDashboardCards() {
        HorizontalLayout cardsLayout = new HorizontalLayout();
        cardsLayout.setWidthFull();
        cardsLayout.addClassName(LumoUtility.Gap.LARGE);
        cardsLayout.setFlexGrow(1);

        // Quick Stats Cards
        cardsLayout.add(
                createStatCard("Total Expenses", "$0.00", VaadinIcon.TRENDING_DOWN, "error"),
                createStatCard("Total Income", "$0.00", VaadinIcon.TRENDING_UP, "success"),
                createStatCard("Balance", "$0.00", VaadinIcon.WALLET, "primary")
        );

        add(cardsLayout);

        // Recent activity section
        H2 recentTitle = new H2("Recent Activity");
        recentTitle.addClassName(LumoUtility.Margin.Top.XLARGE);

        Paragraph noActivity = new Paragraph("No recent activity to display.");
        noActivity.addClassName(LumoUtility.TextColor.SECONDARY);

        add(recentTitle, noActivity);
    }

    private VerticalLayout createStatCard(String title, String value, VaadinIcon icon, String theme) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("stat-card");
        card.setPadding(true);
        card.setSpacing(false);
        card.setWidth("100%");
        card.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("cursor", "pointer")
                .set("transition", "all 0.3s ease");

        // Add hover effect
        card.getElement().addEventListener("mouseenter", e ->
                card.getStyle().set("box-shadow", "var(--lumo-box-shadow-m)")
        );
        card.getElement().addEventListener("mouseleave", e ->
                card.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)")
        );

        // Icon
        Icon cardIcon = icon.create();
        cardIcon.setSize("24px");
        cardIcon.addClassName("text-" + theme);

        // Title
        Span titleSpan = new Span(title);
        titleSpan.addClassName(LumoUtility.FontSize.SMALL);
        titleSpan.addClassName(LumoUtility.TextColor.SECONDARY);

        // Value
        H2 valueHeader = new H2(value);
        valueHeader.addClassName(LumoUtility.Margin.NONE);
        valueHeader.addClassName("text-" + theme);

        card.add(cardIcon, titleSpan, valueHeader);
        card.setAlignItems(Alignment.START);

        return card;
    }
}
