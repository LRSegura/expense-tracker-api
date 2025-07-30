package com.dev2ever;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Main layout for the Quarkus-based UI application
 */
@Layout
@AnonymousAllowed
public class QuarkusMainLayout extends AppLayout {

    private H1 viewTitle;
    @Inject
    SecurityIdentity securityIdentity;

    public QuarkusMainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        MenuBar userMenu = createUserMenuSafely();

        HorizontalLayout header = new HorizontalLayout(toggle, viewTitle, userMenu);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(viewTitle);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM
        );

        addToNavbar(header);
    }

    private MenuBar createUserMenuSafely() {
        if (!isSecurityContextAvailable()) {
            return createEmptyUserMenu();
        }
        return createUserMenu();
    }

    private boolean isSecurityContextAvailable() {
        return securityIdentity != null && securityIdentity.getPrincipal() != null;
    }

    private MenuBar createEmptyUserMenu() {
        MenuBar userMenu = new MenuBar();
        userMenu.setThemeName("tertiary-inline contrast");
        userMenu.setVisible(false); // Hide menu when no user is authenticated
        return userMenu;
    }

    private MenuBar createUserMenu() {
        String username = securityIdentity.getPrincipal().getName();
        Avatar avatar = createUserAvatar(username);

        MenuBar userMenu = new MenuBar();
        userMenu.setThemeName("tertiary-inline contrast");

        MenuItem userMenuItem = userMenu.addItem(avatar);
        populateUserMenuItems(userMenuItem, username);

        return userMenu;
    }

    private Avatar createUserAvatar(String username) {
        Avatar avatar = new Avatar(username);
        avatar.setThemeName("xsmall");
        avatar.getElement().setAttribute("tabindex", "-1");
        return avatar;
    }

    private void populateUserMenuItems(MenuItem userMenuItem, String username) {
        userMenuItem.getSubMenu().addItem(username);
        userMenuItem.getSubMenu().addSeparator();

        addUserRolesIfPresent(userMenuItem);
        addUserActionItems(userMenuItem);
    }

    private void addUserRolesIfPresent(MenuItem userMenuItem) {
        if (securityIdentity.getRoles() != null && !securityIdentity.getRoles().isEmpty()) {
            userMenuItem.getSubMenu().addItem("Roles: " + String.join(", ", securityIdentity.getRoles()));
            userMenuItem.getSubMenu().addSeparator();
        }
    }

    private void addUserActionItems(MenuItem userMenuItem) {
        userMenuItem.getSubMenu().addItem("Profile", e -> {
            // Navigate to profile page
        });
        userMenuItem.getSubMenu().addItem("Settings", e -> {
            // Navigate to settings page
        });
        userMenuItem.getSubMenu().addSeparator();
        userMenuItem.getSubMenu().addItem("Sign out", e -> logout());
    }

    private void addDrawerContent() {
        Span appName = new Span("Expense Tracker");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);
        Scroller scroller = new Scroller(createNavigation());
        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            // Check role-based access for admin-only entries
            if (isAdminOnlyEntry(entry) && !hasAdminRole()) {
                return; // Skip this entry if user doesn't have admin role
            }
            if (entry.icon() != null) {
                nav.addItem(new SideNavItem(entry.title(), entry.path(), new SvgIcon(entry.icon())));
            } else {
                nav.addItem(new SideNavItem(entry.title(), entry.path()));
            }
        });
        return nav;
    }

    private boolean hasAdminRole() {
        return isSecurityContextAvailable() && securityIdentity.hasRole("admin");
    }

    private boolean isAdminOnlyEntry(MenuEntry entry) {
        // Define which menu entries require admin role
        String path = entry.path();
        return "create-user".equals(path) ||
                "users".equals(path) ||
                entry.title().equals("Create User") ||
                entry.title().equals("Users");
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.Padding.Vertical.XSMALL);
        Paragraph copyright = new Paragraph("© 2025 Expense Tracker");
        copyright.getStyle().set("font-size", "var(--lumo-font-size-s)");
        layout.add(copyright);
        return layout;
    }

    private void logout() {
        // Redirect to Quarkus OIDC logout endpoint
        UI.getCurrent().getPage().setLocation("/q/logout");
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}