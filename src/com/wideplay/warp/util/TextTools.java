package com.wideplay.warp.util;

import com.wideplay.warp.WarpFilter;
import com.wideplay.warp.WarpModule;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * Utility tokenizes text into expressions and raw text, and provides other
 * text conversion utilities.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
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
            throw new IllegalStateException("Error. Expression was not terminated properly: " + token.toString());

        //add last token read if it has any content (is always text)
        if (token.length() > 0)
            tokens.add(Token.text(token.toString()));

        //freeze the token list
        return Collections.unmodifiableList(tokens);
    }

    //TODO make this better (use a regex to validate URI tempalates)
    public static boolean isValidTemplateUri(String uri) {
        return null != uri && uri.contains("{") && uri.contains("}");
    }

    public static String stripAttributePrefix(String attr, String prefix) {
//        System.out.println(attr + " - " + prefix);
        return attr.substring(prefix.length());
    }

    public static String stripExpression(String expr) {
        return expr.substring(2, expr.length() - 1);
    }

    public static boolean isExpression(String attribute) {
        return attribute.startsWith("${");
    }

    public static String extractContextualUri(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }

    public static String extractModuleDirFromFqn(String moduleRootDir, Class<WarpModule> moduleClass) {
        return moduleRootDir.substring(0, moduleRootDir.length() - (WarpFilter.CLASS_EXT.length() + moduleClass.getSimpleName().length()));
    }

    public static String[] commaSeparatorRegexSplit(String viewports) {
        return new String[] { viewports };
        //TODO return viewports.split(",[ ]*") 
    }

    public static String subtractPackagePrefix(Class<?> pageClass, String packageName) {
        return pageClass.getName().substring(packageName.length() + 1);   //remove leading "."
    }


    /**
     *
     * @param template Name of a file
     * @param documentText The content of the file
     * 
     * @return Returns true if the file refers to an XML or DOM-ifyable document.
     */
    public static boolean isXmlTemplate(String template, String documentText) {
        //kind of a weak test to see whether this is an xml template--TODO maybe improve this validating the doc?
        return (null != documentText)
                && (template.endsWith(".html") || template.endsWith(".xhtml"));
    }

    private enum TokenizerState { READING_TEXT, READING_EXPRESSION }


    public static boolean isEmptyString(String string) {
        return (null == string || EMPTY_STRING.equals(string.trim()));
    }

    //URI test regex: (([a-zA-Z][0-9a-zA-Z+\\-\\.]*:)?/{0,2}[0-9a-zA-Z;/?:@&=+$\\.\\-_!~*'()%]+)?(#[0-9a-zA-Z;/?:@&=+$\\.\\-_!~*'()%]+)?
    //Taken from stylus studio message board http://www.stylusstudio.com/xmldev/200108/post10890.html

    private final static Pattern URI_REGEX = Pattern.compile("(([a-zA-Z][0-9a-zA-Z+\\\\-\\\\.]*:)?/{0,2}[0-9a-zA-Z;" +
            "/?:@&=+$\\\\.\\\\-_!~*'()%]+)?(#[0-9a-zA-Z;/?:@&=+$\\\\.\\\\-_!~*'()%]+)?");

    //TODO
    private final static Pattern TEMPLATE_URI_PATTERN = Pattern.compile("(([a-zA-Z][0-9a-zA-Z+\\\\-\\\\.]*:)?/{0,2}[0-9a-zA-Z;" +
            "/?:@&=+$\\\\.\\\\-_!~*'()%]+)?(#[0-9a-zA-Z;/?:@&=+$\\\\.\\\\-_!~*'()%]+)?");


    //less expensive method tests whether string is a valid URI
    public static boolean isValidURI(String uri) {
        return (null != uri)
                && URI_REGEX
                .matcher(uri)
                .matches();
    }
}
