package com.wideplay.warp.util.beans;

import org.mvel.MVEL;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 31/08/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class BeanUtilsTest {
    private static final String SUMMARY_1 = "asijdaiosjdaiosjd";
    private static final String SUMMARY_2 = "123asijdaiosjdaiosjasdasdasd";

    @Test
    public final void testAtomicContexts() {                              
        final String expr = "summary";
        final Issue issue = new Issue();
        issue.setSummary(SUMMARY_1);

        Issue issue2 = new Issue();
        issue2.setSummary(SUMMARY_2);

        Map<String, Object> contextMap = new HashMap<String, Object>() {
            private final Object contextObject = issue;

            @Override
            public Object get(Object key) {
                Object value = super.get(key);

                return (null == value) ? MVEL.getProperty((String) key, contextObject) : value;
            }

            @Override
            public boolean containsKey(Object key) {
                return true;
            }
        };


        assert SUMMARY_1.equals(MVEL.eval(expr, contextMap)) : "Summary unequal";
        System.out.println("MVEL prefers context variable to bean");


    }

    @Test
    public final void testContextualExpressions() {
        String expr = "issue.summary";

        Issue issue = new Issue();
        issue.setSummary(SUMMARY_1);

        Issue issue2 = new Issue();
        issue2.setSummary(SUMMARY_2);

        IssueWrapper wrapper = new IssueWrapper();
        wrapper.setIssue(issue2);

        Map<String, Object> context = new HashMap<String, Object>();
        Map<String, Object> contextStored = context;
        context.put("issue", issue);
//        context.remove("issue");

        assert SUMMARY_1.equals(BeanUtils.getFromPropertyExpression(expr, wrapper)) : "Summary unequal";
        System.out.println("MVEL prefers context variable to bean");

        //trying cleared context
        context = new HashMap();

        try {
            BeanUtils.getFromPropertyExpression(expr, wrapper); //MVEL does weird shit -- it remembers context vars
        } catch(NotReadablePropertyException e) {
            assert true;
        }
        System.out.println("MVEL does weird shit -- it remembers context vars even when an empty map is set");



        //trying null context
        context = null;
        try {
            assert SUMMARY_1.equals(BeanUtils.getFromPropertyExpression(expr, wrapper)) : ""; //MVEL does weird shit -- it remembers context vars
        } catch(NotReadablePropertyException e) {
            assert true;
        }
        System.out.println("MVEL does weird shit -- it remembers context vars even when NO map is set (i.e. null)");


        //trying to use the same context but clearing it
        contextStored.clear();
        assert SUMMARY_1.equals(BeanUtils.getFromPropertyExpression(expr, wrapper)) : "";
    }



    public static class IssueWrapper {
        private Issue issue;

        public Issue getIssue() {
            return issue;
        }

        public void setIssue(Issue issue) {
            this.issue = issue;
        }
    }
    
    public static class Issue {
        private String summary;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }
}
