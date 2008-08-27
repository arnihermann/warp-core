package com.wideplay.warp.widgets;

import com.google.inject.Injector;
import com.wideplay.warp.widgets.rendering.MvelEvaluatorCompiler;
import com.wideplay.warp.widgets.routing.PageBook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import static org.easymock.EasyMock.createMock;
import org.testng.annotations.Test;

import java.io.StringReader;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class XmlLineNumberParsingTest {
    private static final String XML = "<xml>\n\n\n\n\n\n\n" +
            "   <node> helo</node>\n\n" +
            "     <dod/>" +
            "</xml>\n";

    private static final String FAULTY_XML = "<xml>\n\n\n\n\n\n\n" +
            "   <node> ${broken}</node>\n\n" +
            "     <dod/>" +
            "</xml>\n";

//    @Test TODO FIX THIS TEST!!
    public final void lineNumbersCorrectlyFoundInException() {
        final PageBook pageBook = createMock(PageBook.class);

        try {
            new XmlTemplateCompiler(Object.class, new MvelEvaluatorCompiler(Object.class),
                    new WidgetRegistry(createMock(Evaluator.class), pageBook, createMock(Injector.class)), pageBook)

                    .compile(FAULTY_XML);
        } catch (TemplateCompileException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public final void filterParsesLineNumbersIntoAttribute() throws DocumentException {

        final SAXReader reader = new SAXReader();
        reader.setXMLFilter(Dom.newLineNumberFilter());

        final Document document = reader.read(new StringReader(XML));

        assert 1 == lineNumberOf(document, "/xml");
        assert 8 == lineNumberOf(document, "/xml/node");
        assert 10 == lineNumberOf(document, "/xml/dod");

    }

    private static int lineNumberOf(Document document, final String xpath) {
        return Dom.lineNumberOf(((Element) document.selectSingleNode(xpath)));
    }
}
