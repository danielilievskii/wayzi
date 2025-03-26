package mk.ukim.finki.wayzi.service.impl;

import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.converter.UserConverter;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.exception.AuthenticatedUserNotFoundException;
import mk.ukim.finki.wayzi.repository.StandardUserRepository;
import mk.ukim.finki.wayzi.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final StandardUserRepository standardUserRepository;
    private final UserConverter userConverter;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(StandardUserRepository standardUserRepository, UserConverter userConverter, CustomUserDetailsService userDetailsService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.standardUserRepository = standardUserRepository;
        this.userConverter = userConverter;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new passenger using the provided sign-up data.
     *
     * @param signUpDto the data transfer object containing passenger sign-up information
     * @return the saved User entity
     */
    @Override
    public User signUp(SignUpDto signUpDto) {

//        TODO: Create method checkIfUsernameExits since loadUserByUsername is blocking
//        Optional<UserDetails> existingUser = Optional.ofNullable(this.userDetailsService.loadUserByUsername(passengerSignUpDTO.getEmail()));
//        if (userDetailsService.loadUserByUsername(passengerSignUpDTO.getEmail()) != null) {
//            throw new UserAlreadyExistsException("User with that email already exists!");
//        }

        StandardUser standardUser = signUpDto.toEntity(passwordEncoder);
        StandardUser savedUser = standardUserRepository.save(standardUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        authenticateUser(userDetails, signUpDto.password());

        return savedUser;
    }

//    TODO: Fix authentication (not working)
    private void authenticateUser(UserDetails userDetails, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                password,
                userDetails.getAuthorities()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Retrieves the current authentication object from the security context.
     *
     * @return an Optional containing the current Authentication object if authenticated, otherwise empty
     */
    private Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated);
    }


    /**
     * Retrieves the authenticated user from the security context.
     *
     * @return the authenticated User object
     * @throws RuntimeException if the authenticated user is not found
     */
    public User getAuthenticatedUser() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .orElseThrow(AuthenticatedUserNotFoundException::new);
    }

    /**
     * Retrieves the authenticated passenger from the security context.
     *
     * @return the authenticated Passenger object
     * @throws RuntimeException if the authenticated passenger is not found
     */
    public StandardUser getAuthenticatedStandardUser() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(StandardUser.class::cast)
                .orElseThrow(AuthenticatedUserNotFoundException::new);
    }

    /**
     * Retrieves the ID of the authenticated user.
     *
     * @return the ID of the authenticated user
     */
    public Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();

    }
}
