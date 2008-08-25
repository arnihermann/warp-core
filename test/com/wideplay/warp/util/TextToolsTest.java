package com.wideplay.warp.util;

import com.google.inject.Guice;
import com.wideplay.warp.widgets.rendering.EvaluatorCompiler;
import com.wideplay.warp.widgets.rendering.ExpressionCompileException;
import com.wideplay.warp.widgets.rendering.Parsing;
import com.wideplay.warp.widgets.rendering.Token;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 25, 2007 12:06:10 PM
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 */
public class TextToolsTest {
    @DataProvider(name = "tokens")
    public final Object[][] tokens() {
        return new Object[][] {
                { new String[] { "hello ", "expr", "${expr}" } },
                { new String[] { "hello ", "expr", "${expr}", "as", "$asd", "$ {}" } },
                { new String[] { "$$ { ", "{}", "${}" } },
        };
    }

    @Test(dataProvider = "tokens")
    public final void testTokenize(String[] rawStream) throws ExpressionCompileException {
        StringBuilder builder = new StringBuilder();
        for (String chunk : rawStream)
            builder.append(chunk);

        List<Token> tokens = Parsing.tokenize(builder.toString(), Guice.createInjector()
                .getInstance(EvaluatorCompiler.class));

        assert tokens.size() == rawStream.length;

        for (int i = 0; i < rawStream.length; i++) {
            Token token = tokens.get(i);
//            assert rawStream[i].equals(token.getToken());

            if (rawStream[i].startsWith("${") && rawStream[i].endsWith("}"))
                assert token.isExpression();
            else
                assert !token.isExpression();
        }
    }

}
