package com.wideplay.warp.widgets.binding;

import com.google.inject.ImplementedBy;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ImplementedBy(ElRequestBinder.class)
public interface RequestBinder {
    void bind(HttpServletRequest request, Object o);
}
