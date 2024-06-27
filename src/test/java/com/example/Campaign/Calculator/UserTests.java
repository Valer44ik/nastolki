package com.example.Campaign.Calculator;

import com.example.Campaign.Calculator.Controllers.userController;
import com.example.Campaign.Calculator.models.Player;
import com.example.Campaign.Calculator.models.User;
import com.example.Campaign.Calculator.repo.PlayerRepository;
import com.example.Campaign.Calculator.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(userController.class)
public class UserTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    public void testLogin_withEmptyLogin() throws Exception {
        mockMvc.perform(post("/")
                        .param("login", "")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "You have to enter both login and password"));
    }

    @Test
    public void testLogin_withEmptyPassword() throws Exception {
        mockMvc.perform(post("/")
                        .param("login", "user1")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "You have to enter both login and password"));
    }

    @Test
    public void testLogin_withInvalidCredentials() throws Exception {
        // Arrange
        Mockito.when(userRepository.findByLoginAndPassword("user1", "wrong-password")).thenReturn(null);

        // Act and Assert
        mockMvc.perform(post("/")
                        .param("login", "user1")
                        .param("password", "wrong-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Invalid username or password"));
    }

    @Test
    public void testLogin_withValidCredentials() throws Exception {
        // Arrange
        User user = new User();
        user.setLogin("user1");
        user.setPassword("password123");
        Mockito.when(userRepository.findByLoginAndPassword("user1", "password123")).thenReturn(user);

        // Act and Assert
        mockMvc.perform(post("/")
                        .param("login", "user1")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/mainPage"))
                .andExpect(flash().attribute("loggedInUser", user));
    }

    @Test
    public void testRegister_withMissingFields() throws Exception {
        mockMvc.perform(post("/register")
                        .param("nickname", "")
                        .param("login", "user1")
                        .param("email", "user1@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "You have to provide all the necessary information"));
    }

    @Test
    public void testRegister_withExistingUser() throws Exception {
        // Arrange
        User existingUser = new User("nickname", "password123", "user1", "user1@example.com");
        Mockito.when(userRepository.findByLogin("user1")).thenReturn(existingUser);

        // Act and Assert
        mockMvc.perform(post("/register")
                        .param("nickname", "nickname")
                        .param("login", "user1")
                        .param("email", "user1@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Username already exists"));
    }

    @Test
    public void testRegister_withValidData() throws Exception {
        // Arrange
        Mockito.when(userRepository.findByLogin("user1")).thenReturn(null);

        // Act and Assert
        mockMvc.perform(post("/register")
                        .param("nickname", "nickname")
                        .param("login", "user1")
                        .param("email", "user1@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testCreatePlayer_withMissingFields() throws Exception {
        mockMvc.perform(post("/createPlayer")
                        .param("nickname", "")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/createPlayer"));
    }

    @Test
    public void testCreatePlayer_withExistingNickname() throws Exception {
        // Arrange
        Player existingPlayer = new Player("nickname", "John", "Doe");
        Mockito.when(playerRepository.findByNickname("nickname")).thenReturn(existingPlayer);

        // Act and Assert
        mockMvc.perform(post("/createPlayer")
                        .param("nickname", "nickname")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/createPlayer"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Username already exists"));
    }

    @Test
    public void testCreatePlayer_withValidData() throws Exception {
        // Arrange
        Mockito.when(playerRepository.findByNickname("nickname")).thenReturn(null);

        // Act and Assert
        mockMvc.perform(post("/createPlayer")
                        .param("nickname", "nickname")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(view().name("createPlayer"));
    }
}
