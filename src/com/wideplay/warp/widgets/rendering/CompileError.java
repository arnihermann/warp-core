package com.wideplay.warp.widgets.rendering;

/**
 * Represents a template compile error or warning (may be due to an MVEL compile failure
 *  or a warp-widgets static check failure.
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public abstract class CompileError {

    public static CompileErrorBuilder in(String fragment) {
        return new Builder(fragment);
    }

    public static interface CompileErrorBuilder {
        CompileErrorBuilder near(int line);

        CompileError causedBy(ExpressionCompileException e);

        CompileError causedBy(CompileErrors reason);

        CompileError causedBy(CompileErrors reason, ExpressionCompileException e);

        CompileError causedBy(CompileErrors reason, String cause);
    }

    private static class Builder implements CompileErrorBuilder {
        private final String fragment;
        private int line;

        private Builder(String fragment) {
            this.fragment = fragment;
        }

        public CompileErrorBuilder near(int line) {
            this.line = line;

            return this;
        }

        public CompileError causedBy(ExpressionCompileException e) {
            return new CompileError() {

            };
        }

        public CompileError causedBy(CompileErrors reason) {
            return new CompileError() {

            };
        }

        public CompileError causedBy(CompileErrors reason, ExpressionCompileException e) {
            return new CompileError() {

            };
        }

        public CompileError causedBy(CompileErrors reason, String cause) {
            return new CompileError() {

            };
        }
    }
}
