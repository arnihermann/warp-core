package com.wideplay.warp.widgets;

import org.testng.annotations.Test;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.PackageScanner;
import com.google.inject.Guice;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class PackageScannerTest {

//    @Test
    public final void scanPackageForPagesAndWidgets() {
        Package target = Package.getPackage("com.wideplay.warp.widgets.example");

        final PageBook book = Guice.createInjector().getInstance(PageBook.class);

        new PackageScanner(book).scan(target);

        assert null != book.get("/wiki/search");
        assert null != book.get("/wiki/page/bloogity");
        assert null == book.get("/wiki/bloogity");

    }
}
