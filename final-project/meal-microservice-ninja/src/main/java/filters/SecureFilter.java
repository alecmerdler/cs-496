package filters;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Result;
import ninja.exceptions.BadRequestException;

public class SecureFilter implements Filter {

    @Override
    public Result filter(FilterChain filterChain, Context context) {
        String authToken = context.getHeader("authorization");

        if (authToken == null) {
            throw new BadRequestException("missing 'Authorization' header");
        }
        else if (authToken.indexOf("Bearer") != 0) {
            throw new BadRequestException("missing 'Bearer' in 'Authorization' header");
        }
        else {
            GoogleCredential credential = new GoogleCredential().setAccessToken(authToken.replace("Bearer ", ""));
            context.setAttribute("oauthCredential", credential);

            return filterChain.next(context);
        }
    }
}