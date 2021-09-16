package bio.ferlab;

import bio.ferlab.pac4j.UsernameAuthorizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.JEESessionStore;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.Pac4jConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public final class UsernameAuthorizerTest {

    private final JEEContext context = new JEEContext(mock(HttpServletRequest.class), mock(HttpServletResponse.class));

    private SessionStore sessionStore;

    private CommonProfile profile;

    private static Stream<Arguments> provideArguments() {
        return Stream.of(
                Arguments.of("zeppelin", "zeppelin", true),
                Arguments.of("unauthorized", "zeppelin", false)
        );
    }

    @BeforeEach
    void setup() {
        sessionStore = JEESessionStore.INSTANCE;
        profile = new CommonProfile();
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void isAuthorizedTest(final String USER, final String ELEMENT, final boolean EXPECTED_AUTH) {
        profile.addAttribute(Pac4jConstants.USERNAME, USER);
        final UsernameAuthorizer usernameAuthorizer = new UsernameAuthorizer(ELEMENT);
        assertEquals(EXPECTED_AUTH, usernameAuthorizer.isAuthorized(context, sessionStore, Collections.singletonList(profile)));
    }

}
