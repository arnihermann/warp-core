package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.wideplay.warp.widgets.rendering.*;
import com.wideplay.warp.widgets.routing.PageBook;
import net.jcip.annotations.NotThreadSafe;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.jetbrains.annotations.NotNull;
import org.mvel.ErrorDetail;

import java.io.StringReader;
import java.util.*;
import java.lang.reflect.TypeVariable;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class XmlTemplateCompiler {
    private final Class<?> page;
    private final WidgetRegistry registry;
    private final PageBook pageBook;

    private final List<EvaluatorCompiler.CompileErrorDetail> errors = new ArrayList<EvaluatorCompiler.CompileErrorDetail>();
    private final List<EvaluatorCompiler.CompileErrorDetail> warnings = new ArrayList<EvaluatorCompiler.CompileErrorDetail>();

    //state variables
    private Element form;
    private final Stack<EvaluatorCompiler> lexicalScopes = new Stack<EvaluatorCompiler>();


    //special widget types (built-in symbol table)
    private static final String REQUIRE_WIDGET = "@require";
    private static final String REPEAT_WIDGET = "repeat";
    private static final String CHOOSE_WIDGET = "choose";


    @Inject
    public XmlTemplateCompiler(Class<?> page, EvaluatorCompiler compiler, WidgetRegistry registry, PageBook pageBook) {
        this.page = page;
        this.registry = registry;
        this.pageBook = pageBook;

        this.lexicalScopes.push(compiler);
    }

    public Renderable parse(String template) {
        WidgetChain widgetChain;
        try {
            final SAXReader reader = new SAXReader();
            reader.setMergeAdjacentText(true);

            widgetChain = walk(reader.read(new StringReader(template)));
        } catch (DocumentException e) {
            throw new TemplateParseException(e);
        }

        if (!errors.isEmpty())
            throw new TemplateCompileException(page, template, errors);

        return widgetChain;
    }

    private WidgetChain walk(Document document) {
        WidgetChain chain = new WidgetChain();
        final WidgetChain docChain = walk(document.getRootElement());

        chain.addWidget(widgetize(null, document.getRootElement(), docChain));

        return chain;
    }

    /**
     *  Walks the DOM recursively, and converts elements into corresponding warp-widgets
     */
    @SuppressWarnings({"JavaDoc"}) @NotNull
    private WidgetChain walk(Element element) {

        WidgetChain widgetChain = new WidgetChain();
        
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);

            if (Dom.isElement(node)) {
                final Element child = (Element) node;

                //push form if this is a form tag
                if (Dom.isForm(node))
                    form = (Element) node;


                //setup a lexical scope if we're going into a repeat widget (by reading the previous node)
                boolean shouldPopScope = lexicalClimb(element, i);

                //continue recursing down, perform a post-order, depth-first traversal of the DOM
                WidgetChain childsChildren;
                try {
                    childsChildren = walk(child);

                    //process the widget itself into a Renderable with child tree
                    if (i > 0)
                        widgetChain.addWidget(widgetize(element.node(i - 1), child, childsChildren));
                    else
                        widgetChain.addWidget(widgetize(null, child, childsChildren));

                } finally {

                    lexicalDescend(node, shouldPopScope);

                }

            } else if (Dom.isTextCommentOrCdata(node)) {
                //process as raw text widget
                try {
                    widgetChain.addWidget(new TextWidget(Dom.stripAnnotation(node.asXML()), lexicalScopes.peek()));
                } catch (ExpressionCompileException e) {
                    errors.add(e.getError());
                }
            }
        }

        //return computed chain, or a terminal
        return widgetChain;
    }
    

    //opposite of XmlTemplateCompiler#lexicalClimb()
    private void lexicalDescend(Node node, boolean shouldPopScope) {

        //pop form
        if (Dom.isForm(node))
            form = null;

        //pop compiler if the scope ends
        if (shouldPopScope) {
            lexicalScopes.pop();
        }
    }



    //called to prepare a new lexical closure; opposite of XmlTemplateCompiler#lexicalDescend()
    private boolean lexicalClimb(Element element, int i) {
        //read annotation on this node only if it is not the root node
        String annotation = i > 0 ? Dom.readAnnotation(element.node(i - 1)) : null;

        if (null != annotation) {
            String[] keyAndContent = Dom.extractKeyAndContent(annotation);

            //setup a new lexical scope if necessary (symbol table changes on each lexical closure encountered)
            final String name = keyAndContent[0];
            if (REPEAT_WIDGET.equalsIgnoreCase(name) || CHOOSE_WIDGET.equalsIgnoreCase(name)) {
                lexicalScopes.push(new MvelEvaluatorCompiler(parseRepeatScope(keyAndContent)));
                return true;
            }

            //setup a new lexical scope for compiling against embedded widgets (sealed closures)
            final PageBook.Page embed = pageBook.forName(name);
            if (null != embed) {
                lexicalScopes.push(new MvelEvaluatorCompiler(embed.pageClass()));
                return true;
            }

        }

        return false;
    }


    /**
     * This method converts an xml element into a specific kind of warp-widget
     */
    @SuppressWarnings({"JavaDoc"}) @NotNull
    private Renderable widgetize(Node preceeding, Element element, WidgetChain childsChildren) {

        //read annotation if available
        String annotation = Dom.readAnnotation(preceeding);

        //if there is no annotation, treat as a raw xml-widget (i.e. tag)
        if (null == annotation)
            try {
                checkUriConsistency(element);
                checkFormFields(element);

                return registry.xmlWidget(childsChildren, element.getName(), Dom.parseAttribs(element.attributes()),
                        lexicalScopes.peek());
            } catch (ExpressionCompileException e) {
                errors.add(e.getError());

                return new TerminalWidgetChain();
            }

        //special case: is this a "require" widget? (used for exporting/interning header tags into embedding pages)
        if (REQUIRE_WIDGET.equalsIgnoreCase(annotation.trim()))
            try {
                return new RequireWidget(Dom.stripAnnotation(element.asXML()), lexicalScopes.peek());
            } catch (ExpressionCompileException e) {
                errors.add(e.getError());

                return new TerminalWidgetChain();
            }

        //process as "normal" widget
        String[] extract = Dom.extractKeyAndContent(annotation);




        //if this is NOT a self-rendering widget, give it an xml child
        final String widgetName = extract[0].trim().toLowerCase();
        if (!registry.isSelfRendering(widgetName))
            try {
                childsChildren = new SingleWidgetChain(registry.xmlWidget(childsChildren, element.getName(),
                        Dom.parseAttribs(element.attributes()), lexicalScopes.peek()));
            } catch (ExpressionCompileException e) {
                errors.add(e.getError());
            }


        
        //key, expression, child widgets
        try {
            return registry.newWidget(widgetName, extract[1], childsChildren, lexicalScopes.peek());
        } catch (ExpressionCompileException e) {
            errors.add(e.getError());

            //this should never be used.
            return new TerminalWidgetChain();
        }
    }




    private Map<String, Class<?>> parseRepeatScope(String[] extract) {
        Repeat repeat = registry.parseRepeat(extract[1]);
        Map<String, Class<?>> context = new HashMap<String, Class<?>>();

        //verify that repeat was parsed properly
        if (null == repeat.var()) {
            errors.add(new EvaluatorCompiler.CompileErrorDetail(extract[1],
                        new ErrorDetail("missing 'var' attribute on @Repeat widget declaration", true))
                );
        }
        if (null == repeat.items()) {
            errors.add(new EvaluatorCompiler.CompileErrorDetail(extract[1],
                        new ErrorDetail("missing 'items' attribute on @Repeat widget declaration", true))
                );
        }

        try {
            Class<?> egressType = lexicalScopes.peek().resolveEgressType(repeat.items());

            Class<?> typeParameter = null;
            if (Collection.class.isAssignableFrom(egressType)) {

                //determine collection parameter type
                typeParameter = lexicalScopes.peek().resolveCollectionTypeParameter(repeat.items());

            } else {
                errors.add(new EvaluatorCompiler.CompileErrorDetail(extract[1],
                        new ErrorDetail("cannot repeat over non-Collections. Please ensure 'items' " +
                                "is a subtype of java.util.Collection", true))
                );
            }


            context.put(repeat.var(), typeParameter);
            context.put(repeat.pageVar(), page);

        } catch (ExpressionCompileException e) {
            errors.add(e.getError());
        }

        return context;
    }




    private void checkFormFields(Element element) {
        if (null == form)
            return;

        Attribute action = form.attribute("action");

        //only look at contextual uris (i.e. hosted by us)
        if (null == action || (!action.getValue().startsWith("/")))
            return;

        final PageBook.Page page = pageBook.get(action.getValue());

        //if we're inside a form do a throw-away compile against the target page
        if ("input".equals(element.getName()) || "textarea".equals(element.getName())) {
            Attribute name = element.attribute("name");

            //skip empty?
            if (null == name) {
                warnings.add(new EvaluatorCompiler.CompileErrorDetail(Dom.asRawXml(element),
                        new ErrorDetail("form field is missing required attribute 'name' (bind path)", true)));
                return;
            }

            //skip submits and buttons
            if (Dom.skippable(element.attribute("type")))
                return;

            //compile expression path
            final String expression = name.getValue();
            try {
                new MvelEvaluatorCompiler(page.pageClass())
                        .compile(expression);
            } catch (ExpressionCompileException e) {
                //very hacky, needed to strip out xmlns attribution
                final String xml = Dom.asRawXml(element);
                warnings.add(new EvaluatorCompiler.CompileErrorDetail(xml,

                        //add error detail with an offset of where the expression occurs
                        new ErrorDetail(1, xml.indexOf(expression) - 1, true,
                                "unknown or unresolvable property in input binding: " + expression)));
            }

        }

    }

    private void checkUriConsistency(Element element) {
        Attribute uriAttrib = element.attribute("action");
        if (null == uriAttrib)
            uriAttrib = element.attribute("src");
        if (null == uriAttrib)
            uriAttrib = element.attribute("href");

        if (null != uriAttrib) {
            
            //verify that such a uri exists in the page book (only if it is contextual--ignore abs+relative URIs)
            final String uri = uriAttrib.getValue();
            if (uri.startsWith("/"))
                if (null == pageBook.get(uri))
                    warnings.add(new EvaluatorCompiler.CompileErrorDetail(uriAttrib.asXML(),
                            new ErrorDetail(1, -1, true,
                                    "no page registered at the linked URI: " + uri)));
        }
    }


}
