import dev.lamotte.csvlib.CSVDocument;

public class Test {

    public static void main(String[] args) {
        Person person = new Person();

        CSVDocument<Person> document = new CSVDocument<>(Person.class);

        document.add(person);

        System.out.println(document.print());
    }

}
