/**
 * Copyright (C) 2007 Wideplay Interactive Group.
 *   http://www.wideplay.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wideplay.warp.module.ioc;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * On: 23/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class WarpScopes {

    /**
     *
     */
    public static final Scope DISK = new Scope() {

        public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    /**
     * HTTP servlet request scope.
     *
     * @author crazybob@google.com (Bob Lee), dhanji@gmail.com (Dhanji R. Prasanna)
     */
    public static final Scope REQUEST = new Scope() {
        public <T> Provider<T> scope(Key<T> key, final Provider<T> creator) {
            final String name = key.toString();
            return new Provider<T>() {
                public T get() {
                    HttpServletRequest request = IocContextManager.getRequest();
                    synchronized (request) {
                        @SuppressWarnings("unchecked")
                        T t = (T) request.getAttribute(name);
                        if (t == null) {
                            t = creator.get();
                            request.setAttribute(name, t);
                        }
                        return t;
                    }
                }

                public String toString() {
                    return creator.toString();
                }
            };
        }

        public String toString() {
            return "WarpScopes.REQUEST";
        }
    };

    /**
     * HTTP session scope.
     *
     * @author crazybob@google.com (Bob Lee), dhanji@gmail.com (Dhanji R. Prasanna)
     */
    public static final Scope SESSION = new Scope() {
        public <T> Provider<T> scope(Key<T> key, final Provider<T> creator) {
            final String name = key.toString();
            return new Provider<T>() {
                public T get() {
                    HttpSession session = IocContextManager.getRequest().getSession();
                    synchronized (session) {
                        @SuppressWarnings("unchecked")
                        T t = (T) session.getAttribute(name);
                        if (t == null) {
                            t = creator.get();
                            session.setAttribute(name, t);
                        }
                        return t;
                    }
                }

                public String toString() {
                    return creator.toString();
                }
            };
        }

        public String toString() {
            return "WarpScopes.SESSION";
        }
    };
}
