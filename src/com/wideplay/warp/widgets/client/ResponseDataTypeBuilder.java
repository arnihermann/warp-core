package com.wideplay.warp.widgets.client;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public interface ResponseDataTypeBuilder {

    <T> T to(Class<T> objectClass);
}
