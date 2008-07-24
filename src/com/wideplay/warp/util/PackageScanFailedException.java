package com.wideplay.warp.util;


/**
 * This exception is left public so it can be caught in rare extreme cases (in debug mode).
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public final class PackageScanFailedException extends RuntimeException {
    public PackageScanFailedException(String s, Exception e) {
        super(s, e);
    }
}
