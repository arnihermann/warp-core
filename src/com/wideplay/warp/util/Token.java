package com.wideplay.warp.util;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * A simple wrapper around a string denoting it as an token.
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class Token {
    private final String token;
    private final boolean isExpression;

    private Token(String token, boolean expression) {
        this.token = token;
        isExpression = expression;
    }

    public String getToken() {
        return token;
    }


    public boolean isExpression() {
        return isExpression;
    }

    static Token expression(String token) {
        //strip leading ${ and trailing }
        return new Token(token.substring(2, token.length() - 1), true);
    }

    static Token text(String token) {
        return new Token(token, false);
    }
}
