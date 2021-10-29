package bio.ferlab;

import bio.ferlab.pac4j.RoleAuthorizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.JEESessionStore;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.CommonProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public final class RoleAuthorizerTest {

    private final JEEContext context = new JEEContext(mock(HttpServletRequest.class), mock(HttpServletResponse.class));

    private SessionStore sessionStore;

    private CommonProfile profile;

    private static Stream<Arguments> provideRoleArguments() {
        return Stream.of(
                Arguments.of(Set.of("authorized", "unauthorized"), Set.of("another_authorized", "authorized"), true),
                Arguments.of(Set.of("unauthorized"), Set.of("authorized"), false)
        );
    }


    @BeforeEach
    void setup() {
        sessionStore = JEESessionStore.INSTANCE;
        profile = new CommonProfile();
    }

    @ParameterizedTest
    @MethodSource("provideRoleArguments")
    void isNameAuthorizedTest(final Set<String> ROLES, final Set<String> ELEMENT, final boolean EXPECTED_AUTH) {
        profile.setRoles(ROLES);
        final RoleAuthorizer roleAuthorizer = new RoleAuthorizer(ELEMENT);
        assertEquals(EXPECTED_AUTH, roleAuthorizer.isAuthorized(context, sessionStore, Collections.singletonList(profile)));
    }

}
