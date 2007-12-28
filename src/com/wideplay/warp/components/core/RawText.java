package com.wideplay.warp.components.core;

import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.ioc.el.Expressions;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RenderingContext;
import com.wideplay.warp.util.Token;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * A special case component that renders hanging bits of text in the template.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Component
public class RawText implements Renderable, AttributesInjectable {
    private Map<String, Object> attributeNameValuePairs;

    public static final String WARP_RAW_TEXT_PROP_TOKENS = "warpRawTextTokens";
    public static final String WARP_RAW_TEXT_PROP_TAG = "warpRawTextTag";
    public static final String WARP_RAW_TEXT_PROP_ATTRS = "warpRawTextAttributes";
    public static final String WARP_RAW_TEXT_ATTR_MAP = "warpRawTextPropertyMap";   //same as WARP_RAW_TEXT_PROP_ATTRS but stored as PropertyDescriptors

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        HtmlWriter writer = context.getWriter();

        Object[] attributes = null;
        String warpRawTextTag = null;
        List<Token> tokens = null;
        if (null != attributeNameValuePairs) {
            attributes = ComponentSupport.getTagAttributesExcept(new Object[] {}, attributeNameValuePairs);
            warpRawTextTag = (String) attributeNameValuePairs.get(WARP_RAW_TEXT_PROP_TAG);
            tokens = (List<Token>) attributeNameValuePairs.get(WARP_RAW_TEXT_PROP_TOKENS);
        }

        //self closed tags if there is no text to print
        if (nestedComponents.isEmpty() && null != warpRawTextTag) {
            writer.element(warpRawTextTag, attributes);
            writer.end(warpRawTextTag); //html doesn't like self-closed attributes =(
            return;
        }

        //print around tags if necessary
        if (null != warpRawTextTag)
            writer.element(warpRawTextTag, attributes);

        //iterate and write tokens to output, parsing exprs as necessary
        renderTokens(tokens, context);

        //render children now (not sure exactly how the order works?!) --seems to work ok
        ComponentSupport.renderMultiple(context, nestedComponents);

        //close it up
        if (null != warpRawTextTag)
            writer.end(warpRawTextTag);
    }

    private void renderTokens(List<Token> tokens, RenderingContext context) {
        if (null != tokens) {
            for (Token token : tokens) {
                if (token.isExpression()) {
                    Object value = Expressions.evaluate(token.getToken(), context.getContextVars());

                    //stringize the property only if it is not null
                    String outValue = null;
                    if (null != value)
                        outValue= value.toString();

                    context.getWriter().writeRaw(outValue);
                } else
                    context.getWriter().writeRaw(token.getToken());
            }
        }
    }


    public void setAttributeNameValuePairs(Map<String, Object> attributeNameValuePairs) {
        this.attributeNameValuePairs = attributeNameValuePairs;
    }

    public Map<String, Object> getAttributeNameValuePairs() {
        return attributeNameValuePairs;
    }
}
