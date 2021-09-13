package bio.ferlab.pac4j;

import java.util.List;
import java.util.Set;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UsernameAuthorizer extends AbstractRequireAnyAuthorizer<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameAuthorizer.class);

    public UsernameAuthorizer() { }

    public UsernameAuthorizer(final String... usernames) {
        setElements(usernames);
    }

    public UsernameAuthorizer(final List<String> usernames) {
        setElements(usernames);
    }

    public UsernameAuthorizer(final Set<String> usernames) { setElements(usernames); }

    @Override
    protected boolean check(WebContext context, SessionStore sessionStore, UserProfile profile, String element) {
        return element.equals(profile.getUsername());
    }
}
