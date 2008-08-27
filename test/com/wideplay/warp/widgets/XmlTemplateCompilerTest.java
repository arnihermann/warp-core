package com.wideplay.warp.widgets;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import com.wideplay.warp.widgets.rendering.ExpressionCompileException;
import com.wideplay.warp.widgets.rendering.MvelEvaluatorCompiler;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.SystemMetrics;
import static org.easymock.EasyMock.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class XmlTemplateCompilerTest {
    private static final String ANNOTATION_EXPRESSIONS = "Annotation expressions";
    private Injector injector;
    private PageBook pageBook;
    private SystemMetrics metrics;

    @BeforeMethod
    public void pre() {
        injector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                bind(HttpServletRequest.class).toProvider(mockRequestProviderForContext());
            }
        });

        pageBook = createNiceMock(PageBook.class);
        metrics = createNiceMock(SystemMetrics.class);
    }

    @Test
    public final void annotationKeyExtraction() {
        assert "link".equals(Dom.extractKeyAndContent("@Link")[0]) : "Extraction wrong: ";
        assert "thing".equals(Dom.extractKeyAndContent("@Thing()")[0]) : "Extraction wrong: ";
        assert "thing".equals(Dom.extractKeyAndContent("@Thing(asodkoas)")[0]) : "Extraction wrong: ";
        assert "thing".equals(Dom.extractKeyAndContent("@Thing(asodkoas)  ")[0]) : "Extraction wrong: ";
        assert "thing".equals(Dom.extractKeyAndContent("@Thing(asodkoas)  kko")[0]) : "Extraction wrong: ";

        assert "".equals(Dom.extractKeyAndContent("@Link")[1]) : "Extraction wrong: ";
        final String val = Dom.extractKeyAndContent("@Thing()")[1];
        assert null == (val) : "Extraction wrong: " + val;
        assert "asodkoas".equals(Dom.extractKeyAndContent("@Thing(asodkoas)")[1]) : "Extraction wrong: ";
        assert "asodkoas".equals(Dom.extractKeyAndContent("@Thing(asodkoas)  ")[1]) : "Extraction wrong: ";
        assert "asodkoas".equals(Dom.extractKeyAndContent("@Thing(asodkoas)  kko")[1]) : "Extraction wrong: ";
    }

    @Test
    public final void readShowIfWidgetTrue() {
        final Evaluator evaluator = new MvelEvaluator();

        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), injector);
        registry.add("showif", ShowIfWidget.class);


        final MvelEvaluatorCompiler compiler = new MvelEvaluatorCompiler(TestBackingType.class);
        Renderable widget =
                new XmlTemplateCompiler(Object.class, compiler, registry, pageBook, metrics)
                    .compile("<xml>@ShowIf(true)<p>hello</p></xml>");

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
                new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(Object.class), registry, pageBook, metrics)
                    .compile(String.format("<xml>@ShowIf(%s)<p>hello</p></xml>", expression));

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
        final Injector injector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                bind(HttpServletRequest.class).toProvider(mockRequestProviderForContext());
            }
        });

        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), injector);
        registry.add("showif", ShowIfWidget.class);


        Renderable widget =
                new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(Object.class), registry, pageBook, metrics)
                    .compile("<xml>@ShowIf(false)<p>hello</p></xml>");

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
        final Injector injector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                bind(HttpServletRequest.class).toProvider(mockRequestProviderForContext());
            }
        });
        
        final WidgetRegistry registry = new WidgetRegistry(evaluator, createNiceMock(PageBook.class), injector);
        registry.add("showif", ShowIfWidget.class);


        Renderable widget =
                new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(TestBackingType.class), registry, pageBook, metrics)
                    .compile("<xml><div class='${clazz}'>hello <a href='/people/${id}'>${name}</a></div></xml>");

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


        widget.render(new TestBackingType("Dhanji", "content", 12), mockRespond);

        final String value = builder.toString();
        assert "<xml><div class='content'>hello <a href='/people/12'>Dhanji</a></div></xml>"
                .replaceAll("'", "\"")
                .equals(value) : "Did not write expected output, instead: " + value;
    }

    public static class TestBackingType {
        private String name;
        private String clazz;
        private Integer id;

        public TestBackingType(String name, String clazz, Integer id) {
            this.name = name;
            this.clazz = clazz;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getClazz() {
            return clazz;
        }

        public Integer getId() {
            return id;
        }
    }


    @Test
    public final void readAndRenderRequireWidget() {
        final Evaluator evaluator = new MvelEvaluator();
        final Injector injector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                bind(HttpServletRequest.class).toProvider(mockRequestProviderForContext());
            }
        });


        final PageBook pageBook = injector.getInstance(PageBook.class);

        final WidgetRegistry registry = new WidgetRegistry(evaluator, pageBook, injector);
        registry.add("meta", HeaderWidget.class);


        Renderable widget =
                new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(TestBackingType.class), registry, pageBook, metrics)
                    .compile("<html>@Meta <head>" +
                            "   @Require <script type='text/javascript' src='my.js'> </script>" +
                            "   @Require <script type='text/javascript' src='my.js'> </script>" +
                            "</head>" +
                            "<div class='${clazz}'>hello <a href='/people/${id}'>${name}</a></div></html>");

        assert null != widget : " null ";

        final Respond respond = new StringBuilderRespond();

        widget.render(new TestBackingType("Dhanji", "content", 12), respond);

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
                new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(TestBackingType.class), registry, pageBook, metrics)
                    .compile("<xml><div class='${clazz}'>hello</div></xml>");

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

        widget.render(new TestBackingType("Dhanji", "content", 12), mockRespond);

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
                new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(TestBackingType.class), registry, pageBook, metrics)
                    .compile("<xml><div class='${clazz}'>hello @ShowIf(false)<a href='/hi/${id}'>hideme</a></div></xml>");

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

        widget.render(new TestBackingType("Dhanji", "content", 12), mockRespond);

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
    public final void readEmbedWidgetAndStoreAsPage() {
        final Injector injector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                bind(HttpServletRequest.class).toProvider(mockRequestProviderForContext());
            }
        });
        final PageBook book = injector      //hacky, where are you super-packages!
                .getInstance(PageBook.class);

        book.at("/somewhere", MyEmbeddedPage.class).apply(new TerminalWidgetChain());

        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, book, injector);
        registry.add("myFave", EmbedWidget.class);

        Renderable widget =
                new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(TestBackingType.class), registry, book, metrics)
                    .compile("<xml><div class='content'>hello @MyFave(should=false)<a href='/hi/${id}'>hideme</a></div></xml>");

        assert null != widget : " null ";

        //tell pagebook to track this as an embedded widget
        book.embedAs(MyEmbeddedPage.class).apply(new TerminalWidgetChain());
        final Respond mockRespond = new StringBuilderRespond();

        widget.render(new TestBackingType("Dhanji", "content", 12), mockRespond);

        final String s = mockRespond.toString();
        assert "<xml><div class=\"content\">hello </div></xml>"
                .equals(s) : "Did not write expected output, instead: " + s;
    }


    @Test
    public final void readEmbedWidgetOnly() {
        final Injector injector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                bind(HttpServletRequest.class).toProvider(mockRequestProviderForContext());
            }
        });
        final PageBook book = injector      //hacky, where are you super-packages!
                .getInstance(PageBook.class);

        final Evaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, book, injector);
        registry.add("myFave", EmbedWidget.class);

        Renderable widget =
                new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(TestBackingType.class), registry, pageBook, metrics)
                    .compile("<xml><div class='content'>hello @MyFave(should=false)<a href='/hi/${id}'>hideme</a></div></xml>");

        assert null != widget : " null ";

        //tell pagebook to track this as an embedded widget
        book.embedAs(MyEmbeddedPage.class).apply(new TerminalWidgetChain());

        final Respond mockRespond = new StringBuilderRespond();

        widget.render(new TestBackingType("Dhanji", "content", 12), mockRespond);

        final String s = mockRespond.toString();
        assert "<xml><div class=\"content\">hello </div></xml>"
                .equals(s) : "Did not write expected output, instead: " + s;
    }


    @Test
    public final void readEmbedWidgetWithArgs() throws ExpressionCompileException {

        final Evaluator evaluator = new MvelEvaluator();
        final Injector injector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                bind(HttpServletRequest.class).toProvider(mockRequestProviderForContext());
            }
        });
        final PageBook book = injector.getInstance(PageBook.class);           //hacky, where are you super-packages!

        final WidgetRegistry registry = new WidgetRegistry(evaluator, book, injector);
        registry.add("myFave", EmbedWidget.class);
        registry.add("with", ArgumentWidget.class);

        final MvelEvaluatorCompiler compiler = new MvelEvaluatorCompiler(TestBackingType.class);
        Renderable widget =
                new XmlTemplateCompiler(Object.class, compiler, registry, book, metrics)
                    .compile("<xml><div class='content'>hello @MyFave(should=true)<a href='/hi/${id}'> @With(\"me\")<p>showme</p></a></div></xml>");

        assert null != widget : " null ";


        XmlWidget bodyWrapper = new XmlWidget(new WidgetChain().addWidget(new IncludeWidget(new TerminalWidgetChain(), "'me'", evaluator)),
                "body", compiler, Collections.<String, String>emptyMap());

        bodyWrapper.setRequestProvider(mockRequestProviderForContext());

        //should include the @With("me") annotated widget from the template above (discarding the <p> tag).
        book.embedAs(MyEmbeddedPage.class).apply(bodyWrapper);

        final Respond mockRespond = new StringBuilderRespond();

        widget.render(new TestBackingType("Dhanji", "content", 12), mockRespond);

        final String s = mockRespond.toString();
        assert "<xml><div class=\"content\">hello showme</div></xml>"
                .equals(s) : "Did not write expected output, instead: " + s;
    }

    static Provider<HttpServletRequest> mockRequestProviderForContext() {
        return new Provider<HttpServletRequest>() {
            public HttpServletRequest get() {
                final HttpServletRequest request = createMock(HttpServletRequest.class);
                expect(request.getContextPath())
                        .andReturn("")
                        .anyTimes();
                replay(request);
                
                return request;
            }
        };
    }

}
