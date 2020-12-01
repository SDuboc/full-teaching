package com.fullteaching.backend.security;

import com.fullteaching.backend.user.User;
import com.fullteaching.backend.user.UserComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    public LoginController controller;

    @Mock
    private UserComponent userComponent;

    @Mock
    private HttpSession httpSession;

    @Test
    public void testLoginUnauthorized() {
        Mockito.when(userComponent.isLoggedUser()).thenReturn(false);
        ResponseEntity<User> login = controller.logIn();
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, login.getStatusCode());
    }

    @Test
    public void testLoginOk() {
        User user = createUser();
        Mockito.when(userComponent.isLoggedUser()).thenReturn(true);
        Mockito.when(userComponent.getLoggedUser()).thenReturn(user);
        ResponseEntity<User> login = controller.logIn();
        Assertions.assertEquals(HttpStatus.OK, login.getStatusCode());
    }

    @Test
    public void testLogoutUnauthorized() {
        Mockito.when(userComponent.isLoggedUser()).thenReturn(false);
        ResponseEntity<Boolean> logout = controller.logOut(httpSession);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, logout.getStatusCode());
    }

    @Test
    public void testLogoutOk() {
        User user = createUser();
        Mockito.when(userComponent.isLoggedUser()).thenReturn(true);
        Mockito.when(userComponent.getLoggedUser()).thenReturn(user);
        ResponseEntity<Boolean> logout = controller.logOut(httpSession);
        Assertions.assertEquals(HttpStatus.OK, logout.getStatusCode());
    }

    private User createUser() {
        User u = new User();
        u.setName("uer");
        return u;
    }

}