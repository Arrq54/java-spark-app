public class Name {
    private String name, id;

    public String getId() {
        return id;
    }

    public Name(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Name(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

