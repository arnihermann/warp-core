package com.wideplay.warp.widgets.binding;

import org.testng.annotations.Test;
import com.google.inject.Guice;
import com.wideplay.warp.widgets.Evaluator;

import javax.servlet.http.HttpServletRequest;
import static org.easymock.EasyMock.*;

import java.util.HashMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class ElRequestBinderTest {
    @Test
    public final void bindRequestToPrimitives() {
        final HttpServletRequest request = createMock(HttpServletRequest.class);

        expect(request.getParameterMap())
                .andReturn(new HashMap<String, String[]>() {{
                    put("name", new String[] { "Dhanji" });
                    put("age", new String[] { "27" });
                    put("alive", new String[] { "true" });
                    put("id", new String[] { "12" });
                    put("height", new String[] { "6.0" });
                }});

        replay(request);

        final AnObject o = new AnObject();

        final Evaluator evaluator = Guice.createInjector()
                .getInstance(Evaluator.class);

        new ElRequestBinder(evaluator)
                .bind(request, o);

        assert "Dhanji".equals(o.getName());
        assert 27 == (o.getAge());
        assert 12L == (o.getId());
        assert 6.0 == (o.getHeight());
        assert (o.isAlive());

        verify(request);
    }

    @Test
    public final void bindRequestToPrimitivesAndIgnoreExtras() {
        final HttpServletRequest request = createMock(HttpServletRequest.class);

        expect(request.getParameterMap())
                .andReturn(new HashMap<String, String[]>() {{
                    put("name", new String[] { "Dhanji" });
                    put("age", new String[] { "27" });
                    put("alive", new String[] { "true" });
                    put("id", new String[] { "12" });
                    put("height", new String[] { "6.0" });
                    put("weight", new String[] { "6.0" });
                    put("hiphop", new String[] { "6.0" });
                }});

        replay(request);

        final AnObject o = new AnObject();

        final Evaluator evaluator = Guice.createInjector()
                .getInstance(Evaluator.class);

        new ElRequestBinder(evaluator)
                .bind(request, o);

        assert "Dhanji".equals(o.getName());
        assert 27 == (o.getAge());
        assert 12L == (o.getId());
        assert 6.0 == (o.getHeight());
        assert (o.isAlive());

        verify(request);
    }

    @Test(expectedExceptions = InvalidBindingException.class)
    public final void bindRequestDetectInvalid() {
        final HttpServletRequest request = createMock(HttpServletRequest.class);

        expect(request.getParameterMap())
                .andReturn(new HashMap<String, String[]>() {{
                    put("name.toString()", new String[] { "Dhanji" });
                    put("2 + 12", new String[] { "27" });
                    put("#@!*^&", new String[] { "true" });
                    put("id", new String[] { "12" });
                    put("height", new String[] { "6.0" });
                }});

        replay(request);

        final AnObject o = new AnObject();

        final Evaluator evaluator = Guice.createInjector()
                .getInstance(Evaluator.class);

        new ElRequestBinder(evaluator)
                .bind(request, o);

    }

    public static class AnObject {
        private String name;
        private int age;
        private boolean alive;
        private Long id;
        private double height;

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
