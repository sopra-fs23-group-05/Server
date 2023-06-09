package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setLeader(true);
        testUser.setUsername("testUsername");

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    void createUser_validInputs_success() {
        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.isLeader(), createdUser.isLeader());
        assertEquals(testUser.getUsername(), createdUser.getUsername());

    }

    @Test
    void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        // I deleted the field user.name
        // Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        // I deleted the field user.name
        // Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    // Test if exception is thrown for empty usernames. #86
    @Test
    void createUser_emptyInputs_throwsException() {

        // given
        User testEmptyUsernameUser = new User();
        testEmptyUsernameUser.setId(1L);
        testEmptyUsernameUser.setLeader(true);
        testEmptyUsernameUser.setUsername("");

        // then -> attempt to create user with empty username -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testEmptyUsernameUser));
    }

    // Test if exception is thrown for usernames that contain nothing but spaces. #237
    @Test
    void createUser_InputOnlySpaces_throwsException() {

        // given
        User testEmptyUsernameUser = new User();
        testEmptyUsernameUser.setId(1L);
        testEmptyUsernameUser.setLeader(true);
        testEmptyUsernameUser.setUsername("     ");

        // then -> attempt to create user with empty username -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testEmptyUsernameUser));
    }

}
