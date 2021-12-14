package org.hbrs.se2.project.npng.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.npng.controller.LoginController;
import org.hbrs.se2.project.npng.controller.exception.DatabaseUserException;
import org.hbrs.se2.project.npng.dto.UserDTO;
import org.hbrs.se2.project.npng.entity.User;
import org.hbrs.se2.project.npng.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;


@Route("Login")
@PageTitle("No-Pain_No-Gain")
//@CssImport("./themes/nopainnogain/components/Login.css")

public class LoginView extends VerticalLayout {

    @Autowired
    private LoginController loginController;

    public LoginView(){
        LoginOverlay component = new LoginOverlay();
        // title erstellen:
        H1 title = new H1();
        title.setId("H1_Login");
        title.getStyle().set("color", "var(--lumo-base-color)");
        title.add(new Text("NP_NG"));
        component.setTitle(title);

        // Loginform erstellen:
        LoginI18n i18n = LoginI18n.createDefault();
        component.setI18n(i18n);
        component.setOpened(true);

        component.addLoginListener(e -> {

            boolean isAuthentificated = false;

            try{

                isAuthentificated = loginController.authentificate(e.getUsername(), e.getPassword());
            } catch (DatabaseUserException databaseException){

                Dialog dialog = new Dialog();
                dialog.add( new Text( databaseException.getReason()) );
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
            }

            if (isAuthentificated){

                this.initializeSessionWithUser();

                if(loginController.getCurrentUser().getRole() == User.STUDENT){

                    UI.getCurrent().navigate(StudentView.class);
                }
                else{

                    UI.getCurrent().navigate(CompanyView.class);
                }
            }
            else{
                component.setError(true);
            }
        });
        add(component);
    }

    private void initializeSessionWithUser(){

        UserDTO userDTO = loginController.getCurrentUser();
        UI.getCurrent().getSession().setAttribute(Globals.CURRENT_USER, userDTO);
    }
}