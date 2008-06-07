package com.wideplay.warp.widgets;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import com.wideplay.warp.widgets.routing.PageBook;
import static org.easymock.EasyMock.createNiceMock;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class XmlTemplateParserTest {
    private static final String ANNOTATION_EXPRESSIONS = "Annotation expressions";

    @Test
    public final void annotationKeyExtraction() {
        assert "link".equals(XmlTemplateParser.extractKeyAndContent("@Link")[0]) : "Extraction wrong: ";
        assert "thing".equals(XmlTemplateParser.extractKeyAndContent("@Thing()")[0]) : "Extraction wrong: ";
        assert "thing".equals(XmlTemplateParser.extractKeyAndContent("@Thing(asodkoas)")[0]) : "Extraction wrong: ";
        assert "thing".equals(XmlTemplateParser.extractKeyAndContent("@Thing(asodkoas)  ")[0]) : "Extraction wrong: ";
        assert "thing".equals(XmlTemplateParser.extractKeyAndContent("@Thing(asodkoas)  kko")[0]) : "Extraction wrong: ";

        assert "".equals(XmlTemplateParser.extractKeyAndContent("@Link")[1]) : "Extraction wrong: ";
        assert "".equals(XmlTemplateParser.extractKeyAndContent("@Thing()")[1]) : "Extraction wrong: ";
        assert "asodkoas".equals(XmlTemplateParser.extractKeyAndContent("@Thing(asodkoas)")[1]) : "Extraction wrong: ";
        assert "asodkoas".equals(XmlTemplateParser.extractKeyAndContent("@Thing(asodkoas)  ")[1]) : "Extraction wrong: ";
        assert "asodkoas".equals(XmlTemplateParser.extractKeyAndContent("@Thing(asodkoas)  kko")[1]) : "Extraction wrong: ";
    }

    @Test
    public final void readShowIfWidgetTrue() {
        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), createNiceMock(Injector.class));
        registry.add("showif", ShowIfWidget.class);


        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<xml>@ShowIf(true)<p>hello</p></xml>");

        assert null != widget : " null ";

        final StringBuilder builder = new StringBuilder();
        final Respond mockRespond = new StringBuilderRespond() {
            @Override
            public void write(String text) {
                builder.append(text);
            }

            @Override
            public void write(char text) {
                builder.append(text);
            }

            @Override
            public void chew() {
                builder.deleteCharAt(builder.length() - 1);
            }
        };

        widget.render(new Object(), mockRespond);

        final String value = builder.toString();
//        System.out.println(value);
        assert "<xml><p>hello</p></xml>".equals(value) : "Did not write expected output, instead: " + value;
    }


    @DataProvider(name = ANNOTATION_EXPRESSIONS)
    public Object[][] get() {
        return new Object[][] {
            { "true" },
            { "java.lang.Boolean.TRUE" },
            { "java.lang.Boolean.valueOf('true')" },
            { "true ? true : true" },
            { "'x' == 'x'" },
            { "\"x\" == \"x\"" },
            { "'hello' instanceof java.io.Serializable" },
            { "true; return true" },
            { " 5 >= 2 " },
        };
    }

    @Test(dataProvider = ANNOTATION_EXPRESSIONS)
    public final void readAWidgetWithVariousExpressions(String expression) {
        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), createNiceMock(Injector.class));
        registry.add("showif", ShowIfWidget.class);


        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse(String.format("<xml>@ShowIf(%s)<p>hello</p></xml>", expression));

        assert null != widget : " null ";

        final StringBuilder builder = new StringBuilder();
        final Respond mockRespond = new StringBuilderRespond() {
            @Override
            public void write(String text) {
                builder.append(text);
            }

            @Override
            public void write(char text) {
                builder.append(text);
            }

            @Override
            public void chew() {
                builder.deleteCharAt(builder.length() - 1);
            }
        };

        widget.render(new Object(), mockRespond);

        final String value = builder.toString();
//        System.out.println(value);
        assert "<xml><p>hello</p></xml>".equals(value) : "Did not write expected output, instead: " + value;
    }




    @Test
    public final void readShowIfWidgetFalse() {
        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), createNiceMock(Injector.class));
        registry.add("showif", ShowIfWidget.class);


        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<xml>@ShowIf(false)<p>hello</p></xml>");

        assert null != widget : " null ";

        final StringBuilder builder = new StringBuilder();
        final Respond mockRespond = new StringBuilderRespond() {
            @Override
            public void write(String text) {
                builder.append(text);
            }

            @Override
            public void write(char text) {
                builder.append(text);
            }

            @Override
            public void chew() {
                builder.deleteCharAt(builder.length() - 1);
            }
        };

        widget.render(new Object(), mockRespond);

        final String value = builder.toString();
        assert "<xml></xml>".equals(value) : "Did not write expected output, instead: " + value;
    }


    @Test
    public final void readTextWidgetValues() {
        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), createNiceMock(Injector.class));
        registry.add("showif", ShowIfWidget.class);


        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<xml><div class='${clazz}'>hello <a href='/people/${id}'>${name}</a></div></xml>");

        assert null != widget : " null ";

        final StringBuilder builder = new StringBuilder();
        final Respond mockRespond = new StringBuilderRespond() {
            @Override
            public void write(String text) {
                builder.append(text);
            }
            @Override
            public void write(char text) {
                builder.append(text);
            }

            @Override
            public void chew() {
                builder.deleteCharAt(builder.length() - 1);
            }
        };


        Map<String, String> map = new HashMap<String, String>() {{
            put("name", "Dhanji");
            put("clazz", "content");
            put("id", "12");
        }};

        widget.render(map, mockRespond);

        final String value = builder.toString();
        assert "<xml><div class='content'>hello <a href='/people/12'>Dhanji</a></div></xml>"
                .replaceAll("'", "\"")
                .equals(value) : "Did not write expected output, instead: " + value;
    }


    @Test
    public final void readAndRenderRequireWidget() {
        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), createNiceMock(Injector.class));
        registry.add("meta", HeaderWidget.class);


        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<html>@Meta <head>" +
                            "   @Require <script type='text/javascript' src='my.js'> </script>" +
                            "   @Require <script type='text/javascript' src='my.js'> </script>" +
                            "</head>" +
                            "<div class='${clazz}'>hello <a href='/people/${id}'>${name}</a></div></html>");

        assert null != widget : " null ";

        final Respond respond = new StringBuilderRespond();

        Map<String, String> map = new HashMap<String, String>() {{
            put("name", "Dhanji");
            put("clazz", "content");
            put("id", "12");
        }};

        widget.render(map, respond);

        final String value = respond.toString();
        assert ("<html><head>" +
                "      <script type='text/javascript' src='my.js'> </script>" +
                "</head>" +
                "<div class='content'>hello <a href='/people/12'>Dhanji</a></div></html>")

                .replaceAll("'", "\"")
                .equals(value) : "Did not write expected output, instead: " + value;
    }


    @Test
    public final void readXmlWidget() {
        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), createNiceMock(Injector.class));
        registry.add("showif", ShowIfWidget.class);


        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<xml><div class='${clazz}'>hello</div></xml>");

        assert null != widget : " null ";

        final StringBuilder builder = new StringBuilder();
        final Respond mockRespond = new StringBuilderRespond() {
            @Override
            public void write(String text) {
                builder.append(text);
            }

            @Override
            public void write(char text) {
                builder.append(text);
            }

            @Override
            public void chew() {
                builder.deleteCharAt(builder.length() - 1);
            }
        };


        Map<String, String> map = new HashMap<String, String>() {{
            put("name", "Dhanji");
            put("clazz", "content");
            put("id", "12");
        }};

        widget.render(map, mockRespond);

        final String s = builder.toString();
        assert "<xml><div class=\"content\">hello</div></xml>"
                .equals(s) : "Did not write expected output, instead: " + s;
    }


    @Test
    public final void readXmlWidgetWithChildren() {
        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), createNiceMock(Injector.class));
        registry.add("showif", ShowIfWidget.class);


        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<xml><div class='${clazz}'>hello @ShowIf(false)<a href='/hi/${id}'>hideme</a></div></xml>");

        assert null != widget : " null ";

        final StringBuilder builder = new StringBuilder();
        final Respond mockRespond = new StringBuilderRespond() {
            @Override
            public void write(String text) {
                builder.append(text);
            }

            @Override
            public void write(char text) {
                builder.append(text);
            }

            @Override
            public void chew() {
                builder.deleteCharAt(builder.length() - 1);
            }
        };


        Map<String, String> map = new HashMap<String, String>() {{
            put("name", "Dhanji");
            put("clazz", "content");
            put("id", "12");
        }};

        widget.render(map, mockRespond);

        final String s = builder.toString();
        assert "<xml><div class=\"content\">hello </div></xml>"
                .equals(s) : "Did not write expected output, instead: " + s;
    }

    @EmbedAs("MyFave")
    public static class MyEmbeddedPage {
        private boolean should = true;

        public boolean isShould() {
            return should;
        }

        public void setShould(boolean should) {
            this.should = should;
        }
    }

    @Test
    public final void readEmbedWidgetAndStoredAsPage() {
        final PageBook book = Guice.createInjector()      //hacky, where are you super-packages!
                .getInstance(PageBook.class);

        book.at("/somewhere", new TerminalWidgetChain(), MyEmbeddedPage.class);

        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, book, createNiceMock(Injector.class));
        registry.add("myFave", EmbedWidget.class);

        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<xml><div class='content'>hello @MyFave(should=false)<a href='/hi/${id}'>hideme</a></div></xml>");

        assert null != widget : " null ";

        //tell pagebook to track this as an embedded widget
        book.embedAs(new TerminalWidgetChain(), MyEmbeddedPage.class);

        final Respond mockRespond = new StringBuilderRespond();


        Map<String, String> map = new HashMap<String, String>() {{
            put("name", "Dhanji");
            put("clazz", "content");
            put("id", "12");
        }};

        widget.render(map, mockRespond);

        final String s = mockRespond.toString();
        assert "<xml><div class=\"content\">hello </div></xml>"
                .equals(s) : "Did not write expected output, instead: " + s;
    }


    @Test
    public final void readEmbedWidgetOnly() {
        final PageBook book = Guice.createInjector()      //hacky, where are you super-packages!
                .getInstance(PageBook.class);

//        book.at("/somewhere", new TerminalWidgetChain(), MyEmbeddedPage.class);

        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, book, createNiceMock(Injector.class));
        registry.add("myFave", EmbedWidget.class);

        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<xml><div class='content'>hello @MyFave(should=false)<a href='/hi/${id}'>hideme</a></div></xml>");

        assert null != widget : " null ";

        //tell pagebook to track this as an embedded widget
        book.embedAs(new TerminalWidgetChain(), MyEmbeddedPage.class);

        final Respond mockRespond = new StringBuilderRespond();


        Map<String, String> map = new HashMap<String, String>() {{
            put("name", "Dhanji");
            put("clazz", "content");
            put("id", "12");
        }};

        widget.render(map, mockRespond);

        final String s = mockRespond.toString();
        assert "<xml><div class=\"content\">hello </div></xml>"
                .equals(s) : "Did not write expected output, instead: " + s;
    }


    @Test
    public final void readEmbedWidgetWithArgs() {
        final PageBook book = Guice.createInjector()      //hacky, where are you super-packages!
                .getInstance(PageBook.class);

        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, book, createNiceMock(Injector.class));
        registry.add("myFave", EmbedWidget.class);
        registry.add("with", ArgumentWidget.class);

        Renderable widget =
                new XmlTemplateParser(evaluator, registry)
                    .parse("<xml><div class='content'>hello @MyFave(should=true)<a href='/hi/${id}'> @With(\"me\")<p>showme</p></a></div></xml>");

        assert null != widget : " null ";



        Renderable bodyWrapper = new XmlWidget(new WidgetChain().addWidget(new IncludeWidget(new TerminalWidgetChain(), "'me'", evaluator)),
                "body", evaluator, Collections.<String, String>emptyMap());


        //should include the @With("me") annotated widget from the template above (discarding the <p> tag).
        book.embedAs(bodyWrapper, MyEmbeddedPage.class);

        final Respond mockRespond = new StringBuilderRespond();


        Map<String, String> map = new HashMap<String, String>() {{
            put("name", "Dhanji");
            put("clazz", "content");
            put("id", "12");
        }};

        widget.render(map, mockRespond);

        final String s = mockRespond.toString();
        assert "<xml><div class=\"content\">hello showme</div></xml>"
                .equals(s) : "Did not write expected output, instead: " + s;
    }

}
