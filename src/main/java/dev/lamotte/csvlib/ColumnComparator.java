package dev.lamotte.csvlib;

import java.lang.reflect.Field;
import java.util.Comparator;

public class ColumnComparator implements Comparator<Field> {

    @Override
    public int compare(Field f1, Field f2) {
        Column c1;
        Column c2;

        if(f1.isAnnotationPresent(Column.class)){
            c1 = f1.getAnnotation(Column.class);
        } else {
            return 1;
        }

        if(f2.isAnnotationPresent(Column.class)){
            c2 = f2.getAnnotation(Column.class);
        } else {
            return 1;
        }

        int ret = c1.index() - c2.index();

        return ret == 0 ? 1 : ret;

    }
}
//smaller higher i.e. -1
//larger lower i.e. 1