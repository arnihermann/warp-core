package com.wideplay.misc;

import com.google.inject.TypeLiteral;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class GenericInjectionSpike {
    private static final String RAGTIMEGAL = "ragtimegal";

    @Test
    public final void reificationIsForGirls() throws NoSuchMethodException, IllegalAccessException {
        final GenericInjector injector = new GenericInjector();

        //bind(new TypeLiteral<AGenericInjectee<String>>() {});
        injector.bind(new TypeLiteral<AGenericInjectee<String>>() {}, new AGenericInjectee<String>());

        //bind(new TypeLiteral<AGenericInjectee<Number>>() {});
        injector.bind(new TypeLiteral<AGenericInjectee<Number>>() {}, new AGenericInjectee<Number>());

        //bind(new TypeLiteral<AnotherGenericInjectee<String>>() {});
        injector.bind(new TypeLiteral<AnotherGenericInjectee<String>>() {}, new AnotherGenericInjectee<String>());

        //bind(new TypeLiteral<AnotherGenericInjectee<Number>>() {});
        injector.bind(new TypeLiteral<AnotherGenericInjectee<Number>>() {}, new AnotherGenericInjectee<Number>());

        //bind(String.class).toInstnace("ragtimegal");
        injector.bind(new TypeLiteral<String>() {}, RAGTIMEGAL);

        //bind(new TypeLiteral<List<String>>(){}).toInstance(Arrays.asList("hello", "my", "baby"));
        final List<String> strings = Arrays.asList("hello", "my", "baby");
        injector.bind(new TypeLiteral<List<String>>(){}, strings);

        //bind(new TypeLiteral<List<Number>>(){}).toInstance(Arrays.asList(1, 2, 3, 5, 8, 13));
        final List<Number> numbers = Arrays.<Number>asList(1, 2, 3, 5, 8, 13);
        injector.bind(new TypeLiteral<List<Number>>(){}, numbers);


        //Guice.createInjector().injectMembers(new ATestInjectee());
        final ATestInjectee object = new ATestInjectee();
        injector.inject(object);


        //make sure everything worked
        assert strings.equals(object.stringWrap.getList());
        assert numbers.equals(object.numberWrap.getList());
        assert RAGTIMEGAL.equals(object.thing);

        //one more level deep
        assert strings.equals(object.stringWrap.anotherGenericInjectee.getTs());
        assert numbers.equals(object.numberWrap.anotherGenericInjectee.getTs());

        //see what was injected...
        System.out.println("Injected: " + object);
    }

}
