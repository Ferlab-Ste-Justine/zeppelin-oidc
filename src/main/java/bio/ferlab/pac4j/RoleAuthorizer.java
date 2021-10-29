package bio.ferlab.pac4j;

import org.pac4j.core.authorization.authorizer.AbstractRequireAnyAuthorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;

import java.util.List;
import java.util.Set;

public final class RoleAuthorizer extends AbstractRequireAnyAuthorizer<String> {

    public RoleAuthorizer() {
    }

    public RoleAuthorizer(final String... ids) {
        setElements(ids);
    }

    public RoleAuthorizer(final List<String> ids) {
        setElements(ids);
    }

    public RoleAuthorizer(final Set<String> ids) {
        setElements(ids);
    }

    @Override
    protected boolean check(WebContext context, SessionStore sessionStore, UserProfile profile, String element) {
        return profile.getRoles() != null & profile.getRoles().contains(element);
    }
}
