import java.io.*;
import java.util.Scanner;

public class Main {

    static final String[] files = {
            "students.txt",
            "books.txt",
            "employees.txt",
            "transactions.txt"
    };

    public static void main(String[] args) {

        try {
            initFiles();

            seedEmployee();

            if (login()) {
                System.out.println("Login berhasil! Selamat datang");
            } else {
                System.out.println("Login gagal. Program berhenti.");
            }

        } catch (IOException e) {
            System.err.println("Terjadi error: " + e.getMessage());
        }
    }

    // FILE SETUP
    public static void initFiles() throws IOException {
        for (String fileName : files) {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println(fileName + " dibuat.");
            }
        }
    }

    // SEED DATA

    public static void seedEmployee() throws IOException {
        Employee emp = new Employee("10001", "Agus", "01-01-2001", "admin123");

        if (!employeeExists("employees.txt", emp.nip)) {
            writeData("employees.txt", emp.toFileString());
            System.out.println("Pegawai pertama dibuat.");
        }
    }

    // LOGIN SYSTEM
    public static boolean login() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan NIP: ");
        String nip = scanner.nextLine();

        System.out.print("Masukkan Password: ");
        String password = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader("employees.txt"))) {

            String line;

            while ((line = reader.readLine()) != null) {
                Employee emp = Employee.fromString(line);

                if (emp.nip.equals(nip) && emp.password.equals(password)) {
                    return true;
                }
            }
        }

        return false;
    }

    // FILE WRITE
    public static void writeData(String fileName, String input) throws IOException {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(input + "\n");
        }
    }

    // CHECK EXIST
    public static boolean employeeExists(String fileName, String nip) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = reader.readLine()) != null) {
                Employee emp = Employee.fromString(line);

                if (emp.nip.equals(nip)) {
                    return true;
                }
            }
        }
        return false;
    }
}