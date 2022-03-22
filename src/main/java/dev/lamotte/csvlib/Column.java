package dev.lamotte.csvlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
/**
 * Interface that denotes a field as a column in a csv document
 */
public @interface Column {

    /**
     * The name of the header for this column. By default, it is an empty String
     * The value of this will be ignored if the CSVDocument doesn't have Option.USE_HEADER set
     *
     * @return
     */
    String header() default "";

    /**
     * The String value to be used if the value for the field is null. By default, it is an empty String.
     *
     * @return
     */
    String nullValue() default "";

    /**
     * Index of this column. This will control the order it appears. Columns are printed in order of smallest to largest index.
     * By default, it is Integer.MAX_VALUE, causing it to always be placed after any Columns that specify an index.
     *
     * @return
     */
    int index() default Integer.MAX_VALUE;

}
