package app.service.authService;

import app.domain.DTO.PasswordChangeDTO;
import app.domain.DTO.RegisterUserDTO;
import app.domain.DTO.SurfaceUserDTO;
import app.domain.User;
import app.exception.auth.DuplicateException;
import app.exception.auth.PasswordException;
import app.exception.auth.SessionException;
import app.service.mainService.UserService;

/**
 * Authentication Service class
 */
public class AuthenticationService {
    private final SessionService sessionService;
    private final UserService userService;

    /**
     * Constructor .
     *
     * @param sessionService dependency session service.
     * @param userService    dependency user service.
     */
    public AuthenticationService(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    /**
     * Login validation method.
     *
     * @param username username
     * @param password password
     * @throws DuplicateException exception when duplicate field
     * @throws PasswordException  exception with password logic
     */
    public void verifyLogin(String username, String password) throws DuplicateException, PasswordException {
        if (!isUsernameExists(username)) {
            throw new DuplicateException("Username is not found!");
        }
        if (!isUsernameAndPasswordMapping(username, password)) {
            throw new PasswordException("Password is incorrect!");
        }
    }

    /**
     * Register validation logic.
     *
     * @param user user register DTO from front-end
     * @throws DuplicateException exception when duplicate field
     * @throws PasswordException  exception with password logic
     */
    public void verifyRegister(RegisterUserDTO user) throws DuplicateException, PasswordException {
        if (isUsernameExists(user.getUsername())) {
            throw new DuplicateException("Existing username!");
        } else if (isIdExists(user.getId())) {
            throw new DuplicateException("Existing id!");
        } else if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new PasswordException("Confirm password must equal to password , please try again!");
        }
    }

    /**
     * Change password validation logic.
     *
     * @param user userDTO from front-end
     * @throws PasswordException exception with password logic
     */
    public void verifyPasswordChangeRequest(PasswordChangeDTO user) throws PasswordException {
        if (this.userService.findByUsernameAndPassword(user.getUsername(), user.getCurrentPassword()) == null) {
            throw new PasswordException("Current password is incorrect!");
        } else if (!user.getNewPassword().equals(user.getConfirmNewPassword())) {
            throw new PasswordException("Confirm password is not mapping!");
        } else if (user.getNewPassword().equals(user.getCurrentPassword())) {
            throw new PasswordException("New password shouldn't equal to current password!");
        }
    }

    /**
     * Check is email exists in database.
     *
     * @param email email input
     * @throws DuplicateException when email isn't exists in database
     */
    @SuppressWarnings("unused")
    public void verifyEmail(String email) throws DuplicateException {
        boolean check = isEmailExists(email);
        if (!isEmailExists(email)) {
            throw new DuplicateException("Email is invalid!");
        }
    }

    /**
     * Check username exists.
     *
     * @param username username
     * @return {@code true/false}
     */
    public boolean isUsernameExists(String username) {
        return this.userService.findByUsername(username) != null;
    }

    /**
     * Check id exists.
     *
     * @param id id
     * @return {@code true/false}
     */
    public boolean isIdExists(String id) {
        return this.userService.findById(id) != null;
    }

    /**
     * Check email exists.
     *
     * @param email email
     * @return {@code true/false}
     */
    private boolean isEmailExists(String email) {
        return this.userService.findByEmail(email) != null;
    }

    /**
     * Check password right or invalid.
     *
     * @param username username
     * @param password password
     * @return {@code true/false}
     */
    public boolean isUsernameAndPasswordMapping(String username, String password) {
        User user = this.userService.findByUsernameAndPassword(username, password);
        if (user == null) {
            return false;
        }
        this.sessionService.createSession(user.getId(), user.getRole());
        return true;
    }

    /**
     * Get login current {@link User}.
     *
     * @return {@link User}
     */
    public SurfaceUserDTO getCurrentUser() throws SessionException {
        String sessionValue = sessionService.verifySession();
        User user = this.userService.findById(sessionValue.split(" ")[0]);
        return new SurfaceUserDTO(user.getId(), user.getName(), user.getUsername(), user.getRole(),
                user.getEmail(), user.getAddress(), user.getPhoneNumber());
    }
}
