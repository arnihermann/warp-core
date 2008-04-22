package com.wideplay.warp.widgets.binding;

import org.testng.annotations.Test;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class PropertyCacheTest {

    @Test
    public final void doesPropertyExist() {
        assert new ConcurrentPropertyCache()
                .exists("name", ElRequestBinderTest.AnObject.class);

        assert new ConcurrentPropertyCache()
                .exists("name", ElRequestBinderTest.AnObject.class);

        assert !new ConcurrentPropertyCache()
                .exists("notExists", ElRequestBinderTest.AnObject.class);
    }
}
