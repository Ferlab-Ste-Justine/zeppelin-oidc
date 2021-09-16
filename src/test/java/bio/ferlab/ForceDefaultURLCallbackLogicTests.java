package bio.ferlab;

import bio.ferlab.pac4j.ForceDefaultURLCallbackLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.session.JEESessionStore;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.exception.http.FoundAction;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.util.Pac4jConstants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ForceDefaultURLCallbackLogicTests {

    private ForceDefaultURLCallbackLogic logic;

    private JEEContext context;

    private SessionStore sessionStore;

    private static Stream<Arguments> provideURLs() {
        return Stream.of(
                Arguments.of("", 302, ""),
                Arguments.of(Pac4jConstants.DEFAULT_URL_VALUE, 302, ""),
                Arguments.of(Pac4jConstants.DEFAULT_URL, 302, Pac4jConstants.DEFAULT_URL)
        );
    }

    @BeforeEach
    void setup() {
        logic = new ForceDefaultURLCallbackLogic();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        context = new JEEContext(request, response);
        sessionStore = JEESessionStore.INSTANCE;
    }

    @ParameterizedTest
    @MethodSource("provideURLs")
    void testRedirectToOriginallyRequestedUrl(final String URL, final int EXPECTED_CODE, final String EXPECTED_URL) {
        HttpAction action = logic.redirectToOriginallyRequestedUrl(context, sessionStore, URL);
        assertEquals(EXPECTED_CODE, action.getCode());
        assertEquals(EXPECTED_URL, ((FoundAction) action).getLocation());
    }

}
