package com.dev2ever.personform;

import com.dev2ever.UserServiceClient;
import com.dev2ever.model.UserDto;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;


@PageTitle("Create User")
@Route("create")
@Menu(order = 0, icon = "USER")
@AnonymousAllowed
public class PersonFormView extends Composite<VerticalLayout> {

    private static final String PERSONAL_INFORMATION_TITLE = "Personal Information";
    private static final String SAVE_BUTTON_TEXT = "Save";
    private static final String CANCEL_BUTTON_TEXT = "Cancel";
    private static final String MAX_WIDTH = "800px";

    private TextField firstNameField;
    private TextField lastNameField;
    private EmailField emailField;
    private TextField userNameField;
    private TextField passwordField;
    private Button saveButton;
    private Button cancelButton;

    @Inject
    @RestClient
    UserServiceClient userServiceClient;


    public PersonFormView() {
        createComponents();
        configureMainLayout();
        configureFormComponents();
        configureButtons();
        buildLayout();
    }

    private void createComponents() {
        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        emailField = new EmailField("Email");
        userNameField = new TextField("UserName");
        passwordField = new TextField("Password");
        saveButton = new Button(SAVE_BUTTON_TEXT);
        cancelButton = new Button(CANCEL_BUTTON_TEXT);
    }

    private void configureMainLayout() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
    }

    private void configureFormComponents() {
        firstNameField.setWidth("100%");
        lastNameField.setWidth("100%");
        emailField.setWidth("100%");
        userNameField.setWidth("100%");
    }

    private void configureButtons() {
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(this::saveButtonListener);

        cancelButton.setWidth("min-content");
    }

    private void buildLayout() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setMaxWidth(MAX_WIDTH);
        mainLayout.setHeight("min-content");

        H3 title = new H3(PERSONAL_INFORMATION_TITLE);
        title.setWidth("100%");

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");
        formLayout.add(firstNameField, lastNameField, emailField, userNameField, passwordField);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName(Gap.MEDIUM);
        buttonLayout.setWidth("100%");
        buttonLayout.getStyle().set("flex-grow", "1");
        buttonLayout.add(saveButton, cancelButton);

        mainLayout.add(title, formLayout, buttonLayout);
        getContent().add(mainLayout);
    }

    private void saveButtonListener(ClickEvent<Button> event) {
        UserDto userDto = new UserDto(null, userNameField.getValue(), emailField.getValue(),
                firstNameField.getValue(), lastNameField.getValue(), passwordField.getValue());

        try {
            Response response = userServiceClient.createUser(userDto);

            if (response.getStatus() == 201) {
                Notification.show("User created successfully!", 3000, Notification.Position.TOP_CENTER);
                clearForm();
            } else {
                String errorMessage = response.readEntity(String.class);
                Notification.show("Failed to create user: " + errorMessage, 5000, Notification.Position.TOP_CENTER);
            }

        } catch (Exception e) {
            Notification.show("Error creating user: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        userNameField.clear();
        passwordField.clear();
    }

}

