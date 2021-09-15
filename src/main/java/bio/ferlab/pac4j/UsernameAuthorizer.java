package bio.ferlab.pac4j;

import org.pac4j.core.authorization.authorizer.AbstractRequireAnyAuthorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;

import java.util.List;
import java.util.Set;

public final class UsernameAuthorizer extends AbstractRequireAnyAuthorizer<String> {

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
