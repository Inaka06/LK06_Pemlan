class Employee {
    String nip;
    String name;
    String birthDate;
    String password;

    public Employee(String nip, String name, String birthDate, String password) {
        this.nip = nip;
        this.name = name;
        this.birthDate = birthDate;
        this.password = password;
    }

    // convert object to format file
    public String toFileString() {
        return nip + "," + name + "," + birthDate + "," + password;
    }

    // convert file line to object
    public static Employee fromString(String line) {
        String[] parts = line.split(",");
        return new Employee(parts[0], parts[1], parts[2], parts[3]);
    }
}