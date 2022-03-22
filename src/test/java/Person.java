import dev.lamotte.csvlib.Column;

public class Person {

    @Column
    private String name = "Bob";

    @Column(header = "years")
    private int age = 21;

    @Column(nullValue = "NULL", index = -1)
    private Object unknown = null;

    private String ignored = "I shouldn't show up";

}
