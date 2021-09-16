package bio.ferlab.pac4j;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.DefaultCallbackLogic;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.util.HttpActionHelper;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.core.exception.http.FoundAction;
import org.pac4j.core.util.CommonHelper;

public class ForceDefaultURLCallbackLogic extends DefaultCallbackLogic {

    @Override
    public HttpAction redirectToOriginallyRequestedUrl(final WebContext context, final SessionStore sessionStore,
                                                          final String defaultUrl) {
        if(CommonHelper.isNotBlank(defaultUrl) && 
            !Pac4jConstants.DEFAULT_URL_VALUE.equals(defaultUrl)) {
            return HttpActionHelper.buildRedirectUrlAction(context, ( new FoundAction(defaultUrl) ).getLocation());
        } else {
            return super.redirectToOriginallyRequestedUrl(context, sessionStore, "");
        }
    }

    
}
