package com.wideplay.warp.util;

import com.google.inject.ImplementedBy;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@ImplementedBy(JdkLoggingMemoFactory.class)
public interface MemoFactory {
    Memo log(Class<?> aClass);
}
