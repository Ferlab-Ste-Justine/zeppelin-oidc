package bio.ferlab.pac4j;

import org.pac4j.core.authorization.authorizer.AbstractRequireAnyAuthorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;

import java.util.List;
import java.util.Set;

public final class UseridAuthorizer extends AbstractRequireAnyAuthorizer<String> {

    public UseridAuthorizer() { }

    public UseridAuthorizer(final String... ids) {
        setElements(ids);
    }

    public UseridAuthorizer(final List<String> ids) {
        setElements(ids);
    }

    public UseridAuthorizer(final Set<String> ids) { setElements(ids); }

    @Override
    protected boolean check(WebContext context, SessionStore sessionStore, UserProfile profile, String element) {
        return element.equals(profile.getId());
    }
}
