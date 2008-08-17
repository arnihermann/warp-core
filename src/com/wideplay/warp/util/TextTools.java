package com.wideplay.warp.util;


import com.wideplay.warp.widgets.rendering.EvaluatorCompiler;
import com.wideplay.warp.widgets.rendering.ExpressionCompileException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
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

    //converts comma-separated name/value pairs into expression/variable bindings    
    public static Map<String, String> toBindMap(String expression) {
        if (Strings.empty(expression))
            return Collections.emptyMap();


        boolean escape = false;
        List<String> pairs = new ArrayList<String>();
        int index = 0;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            //skip commas in strings
            if ('"' == c)
                escape = !escape;

            if (!escape && ',' == c) {
                if (index < i)
                    pairs.add(expression.substring(index, i));

                //skip comma & whitespace if any
                for (; i < expression.length() && (',' == expression.charAt(i) || ' ' == expression.charAt(i));)
                    i++;

                //reset new start index
                index = i;
            }

        }

        //add last pair if needed
        if (index < expression.length()) {
            //chew up leading comma & whitespace if any
            for (; ',' == expression.charAt(index) || ' ' == expression.charAt(index); index++);

            final String pair = expression.substring(index, expression.length()).trim();

            //only consider this a pair if it has something in it!
            if (pair.length() > 1)
                pairs.add(pair);
        }

//        if (escape)
//            throw new ExpressionCompileException("unterminated string found in widget annotation: " + expression);


        //nice to preserve insertion order
        final Map<String, String> map = new LinkedHashMap<String, String>();
        for (String pair : pairs) {
            final String[] nameAndValue = pair.split("=", 2);

            //do some validation
            if (nameAndValue.length != 2)
                throw new IllegalArgumentException("Invalid parameter binding format: " + pair);

            Strings.nonEmpty(nameAndValue[0], "Cannot have an empty left hand side target parameter: " + pair);
            Strings.nonEmpty(nameAndValue[1], "Must provide a non-empty right hand side expression: " + pair);

            map.put(nameAndValue[0].trim(), nameAndValue[1].trim());
        }

        return Collections.unmodifiableMap(map);
    }

    //tokenizes text into raw text chunks interspersed with expression chunks
    public static List<Token> tokenize(String warpRawText, EvaluatorCompiler compiler) throws ExpressionCompileException {
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

                    tokens.add(Token.expression(token.toString(), compiler));
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

    public static String[] commaSeparatorRegexSplit(String viewports) {
        //state machine (automaton) to read a comma separated list

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

    //dont pass null or empty string or 1 char
    public static String stripQuotes(String var) {
        return var.substring(1, var.length() - 1);
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
