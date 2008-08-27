package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.rendering.CompileError;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class TemplateCompileException extends RuntimeException {
    private final List<CompileError> errors;
    private final List<String> templateLines;
    private final String template;
    private final Class<?> page;
    private final List<CompileError> warnings;

    private static final int COLUMN_PAD = 5;

    public TemplateCompileException(Class<?> page, String template,
                                    List<CompileError> errors, List<CompileError> warnings) {
        this.page = page;
        this.warnings = warnings;
        try {
            //noinspection unchecked
            this.templateLines = IOUtils.readLines(new StringReader(template));
            this.template = template;
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read template to construct error report: " + template);
        }

        this.errors = errors;
    }

//
//    @Override
//    public String getMessage() {
//        if (null == errors)
//            return super.getMessage();
//
//        StringBuilder builder = new StringBuilder("Compilation errors in template for ");
//        builder.append(page.getName());
//        builder.append("\n");
//
//        int i = 1;
//        for (EvaluatorCompiler.CompileErrorDetail error : errors) {
//            final ErrorDetail detail = error.getError();
//
//            builder.append("\n");
//            builder.append(i);
//            builder.append(") ");
//            builder.append(detail.getMessage());
//
//
//            builder.append("\n\n");
//            final ErrorLocationTuple tuple = find(error.getExpression());
//            builder.append(tuple.lines);
//            builder.append("\n");
//
//            //offset column to where expression exists
//            char[] space = new char[tuple.index + detail.getCol() + COLUMN_PAD];
//            Arrays.fill(space, ' ');
//            builder.append(space);
//            builder.append("^\n");    //show an arrow/caret where the error is
//
//            i++;
//        }
//
//        builder.append("Total errors: ");
//        builder.append(errors.size());
//        builder.append("\n\n");
//
//        return builder.toString();
//    }

    //returns the line where this expression occurs and some around it
    private ErrorLocationTuple find(String expression) {
        
        //this kinda sucks, this is tight bound n when it could be much faster
        for (int row = 0; row < templateLines.size(); row++) {
            String line = templateLines.get(row);

            int index = line.indexOf(expression);
            if (index > -1)
                return new ErrorLocationTuple(index, row,
                        (row > 0) ?

                        //add prev line if available
                        String.format(" %d:%s\n %d:%s", row, templateLines.get(row - 1), row + 1, line)
                        : String.format(" %d:%s", row, line));
        }

        //should never happen
        return new ErrorLocationTuple(0, 0, "there was a problem computing error location");
//        throw new AssertionError("Expression compiler error reported in a template that didn't contain the expression! "
//                + expression);
    }

    public List<CompileError> getErrors() {
        return errors;
    }

    public List<CompileError> getWarnings() {
        return warnings;
    }

    private static class ErrorLocationTuple {
        private final int index;
        private final String lines;

        public ErrorLocationTuple(int index, int row, String lines) {
            this.index = index;
            this.lines = lines;
        }
    }
}
