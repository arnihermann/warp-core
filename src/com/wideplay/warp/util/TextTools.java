package com.wideplay.warp.util;

import com.wideplay.warp.util.Token;
import com.wideplay.warp.rendering.PageRenderException;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * Utility tokenizes text into expressions and raw text, and provides other
 * text conversion utilities.
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class TextTools {
    public static final String EMPTY_STRING = "";


    private TextTools() {
    }

    //tokenizes text into raw text chunks interspersed with expression chunks
    public static List<Token> tokenize(String warpRawText) {
        List<Token> tokens = new ArrayList<Token>();
        
        //simple state machine to iterate the text and break it up into chunks
        char[] characters = warpRawText.toCharArray();

        StringBuilder token = new StringBuilder();
        TokenizerState state = TokenizerState.READING_TEXT;
        for (int i = 0; i < characters.length; i++) {

            //test for start of an expression
            if (TokenizerState.READING_TEXT.equals(state)) {
                if ('$' == characters[i]) {
                    if ('{' == characters[i + 1]) {
                        //YES it is the start of an expr, so close up the existing token & start a new one
                        if (token.length() > 0) {
                            tokens.add(Token.text(token.toString()));
                            token = new StringBuilder();
                        }

                        state = TokenizerState.READING_EXPRESSION;
                    }
                }
            }

            //test for end of an expr
            if (TokenizerState.READING_EXPRESSION.equals(state)) {
                if ('}' == characters[i]) {
                    //YES it is the end of the expr, so close it up and start a new token
                    token.append(characters[i]);

                    tokens.add(Token.expression(token.toString()));
                    token = new StringBuilder();

                    state = TokenizerState.READING_TEXT;
                    continue;   //dont add the trailing } to the new text field
                }
            }

            //add characters to the token normally
            token.append(characters[i]);
        }

        //should never be in reading expr mode at this point
        if (TokenizerState.READING_EXPRESSION.equals(state))
            throw new PageRenderException("Error: ognl expression was not terminated properly: " + token.toString());

        //add last token read if it has any content (is always text)
        if (token.length() > 0)
            tokens.add(Token.text(token.toString()));

        //freeze the token list
        return Collections.unmodifiableList(tokens);
    }

    private enum TokenizerState { READING_TEXT, READING_EXPRESSION }


    public static boolean isEmptyString(String string) {
        return (null == string || EMPTY_STRING.equals(string.trim()));
    }


    //expensive method tests whether string is a valid URI
    public static boolean isValidURI(String uri) {
        if (null == uri)
            return false;

        boolean isValid = true;
        try {
            URI.create(uri);
        } catch(IllegalArgumentException e) {
            isValid = false;
        }

        return isValid;
    }
}
