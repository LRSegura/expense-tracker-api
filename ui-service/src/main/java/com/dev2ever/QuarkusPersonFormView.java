package com.dev2ever;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


//@PageTitle("Create User")
//@Route(value = "create-user", layout = QuarkusMainLayout.class)
//@Menu(order = 2, icon = "user-plus")
//@Authenticated // Requires authentication
//@RolesAllowed({"admin"}) // Only admin can create users
public class QuarkusPersonFormView extends Composite<VerticalLayout> {
//
//    private static final Logger logger = Logger.getLogger(QuarkusPersonFormView.class.getName());
//    private static final String PERSONAL_INFORMATION_TITLE = "Create New User";
//    private static final String SAVE_BUTTON_TEXT = "Create User";
//    private static final String CANCEL_BUTTON_TEXT = "Cancel";
//    private static final String MAX_WIDTH = "800px";
//
//    private TextField firstNameField;
//    private TextField lastNameField;
//    private EmailField emailField;
//    private TextField userNameField;
//    private PasswordField passwordField;
//    private Button saveButton;
//    private Button cancelButton;
//
//    @Inject
//    @RestClient
//    UserServiceClient userServiceClient;
//
//    public QuarkusPersonFormView() {
//        createComponents();
//        configureMainLayout();
//        configureFormComponents();
//        configureButtons();
//        buildLayout();
//    }
//
//    private void createComponents() {
//        firstNameField = new TextField("First Name");
//        firstNameField.setRequired(true);
//        firstNameField.setRequiredIndicatorVisible(true);
//
//        lastNameField = new TextField("Last Name");
//        lastNameField.setRequired(true);
//        lastNameField.setRequiredIndicatorVisible(true);
//
//        emailField = new EmailField("Email");
//        emailField.setRequired(true);
//        emailField.setRequiredIndicatorVisible(true);
//        emailField.setErrorMessage("Please enter a valid email address");
//
//        userNameField = new TextField("Username");
//        userNameField.setRequired(true);
//        userNameField.setRequiredIndicatorVisible(true);
//        userNameField.setHelperText("Username for login");
//
//        passwordField = new PasswordField("Password");
//        passwordField.setRequired(true);
//        passwordField.setRequiredIndicatorVisible(true);
//        passwordField.setHelperText("Minimum 8 characters");
//
//        saveButton = new Button(SAVE_BUTTON_TEXT);
//        cancelButton = new Button(CANCEL_BUTTON_TEXT);
//    }
//
//    private void configureMainLayout() {
//        getContent().setWidth("100%");
//        getContent().getStyle().set("flex-grow", "1");
//        getContent().setJustifyContentMode(JustifyContentMode.START);
//        getContent().setAlignItems(Alignment.CENTER);
//    }
//
//    private void configureFormComponents() {
//        firstNameField.setWidth("100%");
//        lastNameField.setWidth("100%");
//        emailField.setWidth("100%");
//        userNameField.setWidth("100%");
//        passwordField.setWidth("100%");
//    }
//
//    private void configureButtons() {
//        saveButton.setWidth("min-content");
//        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        saveButton.addClickListener(this::saveButtonListener);
//
//        cancelButton.setWidth("min-content");
//        cancelButton.addClickListener(e -> clearForm());
//    }
//
//    private void buildLayout() {
//        VerticalLayout mainLayout = new VerticalLayout();
//        mainLayout.setWidth("100%");
//        mainLayout.setMaxWidth(MAX_WIDTH);
//        mainLayout.setHeight("min-content");
//        mainLayout.getStyle().set("background", "var(--lumo-base-color)");
//        mainLayout.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
//        mainLayout.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");
//        mainLayout.getStyle().set("padding", "var(--lumo-space-l)");
//
//        H3 title = new H3(PERSONAL_INFORMATION_TITLE);
//        title.setWidth("100%");
//
//        FormLayout formLayout = new FormLayout();
//        formLayout.setWidth("100%");
//        formLayout.setResponsiveSteps(
//                new FormLayout.ResponsiveStep("0", 1),
//                new FormLayout.ResponsiveStep("500px", 2)
//        );
//        formLayout.add(firstNameField, lastNameField, emailField, userNameField);
//        formLayout.setColspan(passwordField, 2);
//        formLayout.add(passwordField);
//
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        buttonLayout.addClassName(Gap.MEDIUM);
//        buttonLayout.setWidth("100%");
//        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
//        buttonLayout.add(cancelButton, saveButton);
//
//        mainLayout.add(title, formLayout, buttonLayout);
//        getContent().add(mainLayout);
//    }
//
//    private void saveButtonListener(ClickEvent<Button> event) {
//        if (!validateForm()) {
//            return;
//        }
//
//        // Disable form during save
//        setFormEnabled(false);
//
//        UserDto userDto = new UserDto(
//                null,
//                userNameField.getValue(),
//                emailField.getValue(),
//                firstNameField.getValue(),
//                lastNameField.getValue(),
//                passwordField.getValue()
//        );
//
//        try {
//            logger.log(Level.INFO, "Creating user: {0}", userDto.username());
//            Response response = userServiceClient.createUser(userDto);
//
//            if (response.getStatus() == 201) {
//                Notification success = Notification.show(
//                        "User created successfully!",
//                        3000,
//                        Notification.Position.TOP_CENTER
//                );
//                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//                clearForm();
//            } else {
//                String errorMessage = response.readEntity(String.class);
//                logger.log(Level.WARNING, "Failed to create user: {0}", errorMessage);
//
//                Notification error = Notification.show(
//                        "Failed to create user: " + errorMessage,
//                        5000,
//                        Notification.Position.TOP_CENTER
//                );
//                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
//            }
//
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error creating user", e);
//            Notification error = Notification.show(
//                    "Error creating user: " + e.getMessage(),
//                    5000,
//                    Notification.Position.TOP_CENTER
//            );
//            error.addThemeVariants(NotificationVariant.LUMO_ERROR);
//        } finally {
//            setFormEnabled(true);
//        }
//    }
//
//    private boolean validateForm() {
//        boolean valid = true;
//
//        if (firstNameField.isEmpty()) {
//            firstNameField.setInvalid(true);
//            firstNameField.setErrorMessage("First name is required");
//            valid = false;
//        } else {
//            firstNameField.setInvalid(false);
//        }
//
//        if (lastNameField.isEmpty()) {
//            lastNameField.setInvalid(true);
//            lastNameField.setErrorMessage("Last name is required");
//            valid = false;
//        } else {
//            lastNameField.setInvalid(false);
//        }
//
//        if (userNameField.isEmpty()) {
//            userNameField.setInvalid(true);
//            userNameField.setErrorMessage("Username is required");
//            valid = false;
//        } else {
//            userNameField.setInvalid(false);
//        }
//
//        if (emailField.isEmpty() || emailField.isInvalid()) {
//            emailField.setInvalid(true);
//            valid = false;
//        }
//
//        if (passwordField.isEmpty() || passwordField.getValue().length() < 8) {
//            passwordField.setInvalid(true);
//            passwordField.setErrorMessage("Password must be at least 8 characters");
//            valid = false;
//        } else {
//            passwordField.setInvalid(false);
//        }
//
//        return valid;
//    }
//
//    private void setFormEnabled(boolean enabled) {
//        firstNameField.setEnabled(enabled);
//        lastNameField.setEnabled(enabled);
//        emailField.setEnabled(enabled);
//        userNameField.setEnabled(enabled);
//        passwordField.setEnabled(enabled);
//        saveButton.setEnabled(enabled);
//        cancelButton.setEnabled(enabled);
//    }
//
//    private void clearForm() {
//        firstNameField.clear();
//        lastNameField.clear();
//        emailField.clear();
//        userNameField.clear();
//        passwordField.clear();
//
//        // Clear any validation errors
//        firstNameField.setInvalid(false);
//        lastNameField.setInvalid(false);
//        emailField.setInvalid(false);
//        userNameField.setInvalid(false);
//        passwordField.setInvalid(false);
//    }
}
