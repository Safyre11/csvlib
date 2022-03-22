package dev.lamotte.csvlib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;


public class CSVDocument<T> {

    private final Class<T> type;

    private final ArrayList<T> values = new ArrayList<>();
    private final TreeMap<Field, String> headers = new TreeMap<>(new ColumnComparator());

    private final HashSet<Option> options = new HashSet<>();

    public CSVDocument(Class<T> type) {
        this.type = type;
        options.add(Option.USE_ANNOTATIONS);
        options.add(Option.USE_HEADER);
    }

    public void add(T value){
        values.add(value);
    }

    public List<T> getValues(){
        return values;
    }

    public void setOption(Option option, boolean value){
        if(value){
            options.add(option);
        } else {
           options.remove(option);
        }
    }

    /**
     * Gets a list of headers for the csv document
     *
     * @return
     */
    public Collection<String> getHeaders() {
        if(headers.isEmpty()){
            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                if(hasOption(Option.USE_ANNOTATIONS)){
                    //If annotation is required but not present skip
                    if(!field.isAnnotationPresent(Column.class)){
                        break;
                    }

                    Column column = field.getAnnotation(Column.class);
                    if(column.header().equals("")){
                        headers.put(field, field.getName());
                    } else {
                        headers.put(field, column.header());
                    }
                } else {
                    //If annotations aren't used, use field name and include all fields
                    headers.put(field, field.getName());
                }
            }
        }
        return headers.values();
    }

    public boolean hasOption(Option option){
        return options.contains(option);
    }

    /**
     * Returns the document as a csv formatted String
     *
     * @return
     * @throws InaccessibleObjectException if a field is not accessible 
     */
    public String print() throws InaccessibleObjectException {
        //Ensure fields have been loaded
        getHeaders();
        StringBuilder sb = new StringBuilder();
        headers.values().forEach(key -> sb.append(key).append(','));
        sb.deleteCharAt(sb.length() - 1);
        sb.append('\n');

        values.forEach(tObject -> {
            headers.keySet().forEach(field -> {
                if(!field.canAccess(tObject)){
                    field.setAccessible(true);
                }
                try {
                    Object value = field.get(tObject);
                    if (value == null){
                        value = field.getAnnotation(Column.class).nullValue();
                    }
                    sb.append(value).append(',');
                } catch (IllegalAccessException ignored) {
                    //Shouldn't be reachable
                }
            });
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');
        });

        return sb.toString();
    }

    public void printToFile(File file, Charset charset) throws IOException, InaccessibleObjectException {
        Files.writeString(file.toPath(), print(), charset);
    }

}