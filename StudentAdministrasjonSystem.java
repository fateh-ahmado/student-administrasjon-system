import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


// StudentAdministrasjonSystem
// -----------------------------------------------

public class StudentAdministrasjonSystem extends Thread {

    static Institutt ims = new Institutt("Bedrift", "Oslo");
    static int funksjonsvalg = -1;
    static Scanner input;

    public static void main(String[] args) throws InterruptedException, FeilInntastingValg {

        settOppTestData();

        input = new Scanner(System.in);

        while (funksjonsvalg != 0) {
            System.out.println("\n-----------------------------------------------");
            System.out.println("Velkommen til hovedmenyen");
            System.out.println("Du kan velge folgende:");
            System.out.println("1. Skriv ut alle emner");
            System.out.println("2. Sok etter en student med navn");
            System.out.println("3. Finn alle studenter i et emne med en karakter (A,B,C,D,E,F)");
            System.out.println("4. Finn karakter til en student i et emne");
            System.out.println("0. for å avslutte");

            try {
                funksjonsvalg = input.nextInt();
            } catch (InputMismatchException e) {
                funksjonsvalg = -1;
            }
            input.nextLine(); // Tøm resten av linjen

            try {
                if (funksjonsvalg == 0) {
                    System.out.println("Avslutter...");
                    // Denne tråden vil avslutte, og dermed avslutter programmet
                } else if (funksjonsvalg >= 1 && funksjonsvalg <= 4) {
                    Thread sokeTrad = new StudentAdministrasjonSystem();
                    sokeTrad.start();
                    sokeTrad.join();

                } else {
                    throw new FeilInntastingValg("Du har tastet feil, velg et tall mellom 0-4");
                }
            } catch (FeilInntastingValg e) {
                System.out.println(e.getMessage());
                break;
            }
        }
        input.close();
    }

    // Testdata
    // -----------------------------------------------
    static void settOppTestData() {
        Student student1 = new Student("Fateh", "Oslo", "12345678", "S1001");
        Student student2 = new Student("Ola", "Bergen", "87654321", "S1002");
        ims.getStudentListe().add(student1);
        ims.getStudentListe().add(student2);

        ims.leggTilEmne("PB1221", "Programmering 1", "Kari Lærer");
        ims.leggTilEmne("PB1222", "Databaser", "Per Lærer");

        ims.registrereEmneForStudent("PB1221", "Fateh");
        ims.registrereEmneForStudent("PB1221", "Ola");
        ims.registrereEmneForStudent("PB1222", "Fateh");

        for (Emne e : ims.getEmneListe()) {
            if (e.getEmnekode().equals("PB1221")) {
                e.getKarakterer().add(new Karakter(student1, e, "A"));
                e.getKarakterer().add(new Karakter(student2, e, "B"));
            } else if (e.getEmnekode().equals("PB1222")) {
                e.getKarakterer().add(new Karakter(student1, e, "C"));
            }
        }
    }

    // Kjøres når tråden starter
    public void run() {
        sokeFunksjonVelger(ims, funksjonsvalg);
    }

    // Statisk metode for menyvalg
    static void sokeFunksjonVelger(Institutt institutt, int funksjonsvalg) {
        String studentNavn = "Fateh";
        String emnekode = "PB1221";
        String bokstavkarakter = "A";

        SokeFunksjoner sokeFunksjoner = new SokeFunksjoner(institutt, funksjonsvalg, studentNavn, emnekode,
                bokstavkarakter);

        switch (funksjonsvalg) {
            case 1:
                institutt.skrivUtAlleEmner();
                break;
            case 2:
                System.out.print("Skriv inn navnet på studenten du vil søke etter: ");
                studentNavn = input.nextLine();
                sokeFunksjoner.finnStudent(studentNavn);
                break;
            case 3:
                System.out.print("Skriv inn emnekode: ");
                emnekode = input.nextLine();
                System.out.print("Skriv inn bokstavkarakter (A,B,C,D,E,F): ");
                bokstavkarakter = input.nextLine();
                sokeFunksjoner.finnAlleStudenterMedKarakter(emnekode, bokstavkarakter);
                break;
            case 4:
                System.out.print("Skriv inn navnet på studenten: ");
                studentNavn = input.nextLine();
                System.out.print("Skriv inn emnekode: ");
                emnekode = input.nextLine();
                sokeFunksjoner.finnEmneKarakter(studentNavn, emnekode);
                break;
        }
    }
}

// Exception class for feil inntasting av valg
// -----------------------------------------------
class FeilInntastingValg extends Exception {
    FeilInntastingValg(String msg) {
        super(msg);
    }
}


class Person {
    private String navn;
    private String adresse;
    private String telefonnummer;

    public Person(String navn, String adresse, String telefonnummer) {
        this.navn = navn;
        this.adresse = adresse;
        this.telefonnummer = telefonnummer;
    }

    public String getNavn() {
        return navn;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    @Override
    public String toString() {
        return "Navn=" + navn + " adresse=" + adresse + " telefonnummer=" + telefonnummer;
    }
}

// -----------------------------------------------

class Ansatt extends Person {
    private String stilling;
    private boolean deltid;

    public Ansatt(String navn, String adresse, String telefonnummer, String stilling, boolean deltid) {
        super(navn, adresse, telefonnummer);
        this.stilling = stilling;
        this.deltid = deltid;
    }

    @Override
    public String toString() {
        return super.toString() + " stilling=" + stilling + " deltid=" + deltid;
    }
}

// -----------------------------------------------

class Student extends Person {
    private String studentId;
    private ArrayList<Emne> emneListe;

    public Student() {
        super("", "", "");
        emneListe = new ArrayList<>();
    }

    public Student(String navn, String adresse, String telefonnummer, String studentId) {
        super(navn, adresse, telefonnummer);
        this.studentId = studentId;
        emneListe = new ArrayList<>();
    }

    public ArrayList<Emne> getEmneListe() {
        return emneListe;
    }

    @Override
    public String toString() {
        return super.toString() + " studentId=" + studentId;
    }
}

class Emne {
    private String emnekode;
    private String emnenavn;
    private String foreleser;
    private ArrayList<Student> studenter;
    private ArrayList<Karakter> karakterer;

    public Emne(String emnekode, String emnenavn, String foreleser) {
        this.emnekode = emnekode;
        this.emnenavn = emnenavn;
        this.foreleser = foreleser;
        studenter = new ArrayList<>();
        karakterer = new ArrayList<>();
    }

    public String getEmnekode() {
        return emnekode;
    }

    public String getEmnenavn() {
        return emnenavn;
    }

    public ArrayList<Student> getStudenter() {
        return studenter;
    }

    public ArrayList<Karakter> getKarakterer() {
        return karakterer;
    }
}

// -----------------------------------------------

class Karakter {
    private Student student;
    private Emne emne;
    private String bokstavKarakter;

    public Karakter(Student student, Emne emne, String bokstavKarakter) {
        this.student = student;
        this.emne = emne;
        this.bokstavKarakter = bokstavKarakter;
    }

    public Student getStudent() {
        return student;
    }

    public Emne getEmne() {
        return emne;
    }

    public String getBokstavKarakter() {
        return bokstavKarakter;
    }
}

// -----------------------------------------------

class Institutt {
    private String navn;
    private String adresse;
    private ArrayList<Ansatt> ansattListe;
    private ArrayList<Emne> emneListe;
    private ArrayList<Student> studentListe;

    public Institutt(String navn, String adresse) {
        this.navn = navn;
        this.adresse = adresse;
        ansattListe = new ArrayList<>();
        emneListe = new ArrayList<>();
        studentListe = new ArrayList<>();
    }

    public ArrayList<Student> getStudentListe() {
        return studentListe;
    }

    public ArrayList<Emne> getEmneListe() {
        return emneListe;
    }

    public String getNavn() {
        return navn;
    }

    // LeggTilEmne
    // -----------------------------------------------
    public void leggTilEmne(String emnekode, String emnenavn, String ansattnavn) {
        Ansatt funnetAnsatt = null;
        for (Ansatt a : ansattListe) {
            if (a.getNavn().equals(ansattnavn)) {
                funnetAnsatt = a;
            }
        }
        Emne nyttEmne = new Emne(emnekode, emnenavn, ansattnavn);
        emneListe.add(nyttEmne);
    }

    // SkrivUtAlleEmner
    // -----------------------------------------------
    public void skrivUtAlleEmner() {
        System.out.println("Alle emner:");
        for (Emne e : emneListe) {
            System.out.println("Emnekode=" + e.getEmnekode() + " Emnenavn=" + e.getEmnenavn());
        }
    }

    // RegistrereEmneForStudent
    // -----------------------------------------------
    public boolean registrereEmneForStudent(String emnekode, String studentnavn) {
        // Finn student
        Student funnetStudent = null;
        for (Student s : studentListe) {
            if (s.getNavn().equals(studentnavn)) {
                funnetStudent = s;
            }
        }
        // Finn emne
        Emne funnetEmne = null;
        for (Emne e : emneListe) {
            if (e.getEmnekode().equals(emnekode)) {
                funnetEmne = e;
            }
        }
        // Sjekk at begge ble funnet
        if (funnetStudent == null || funnetEmne == null) {
            return false;
        }
        // Legg til i hverandres lister
        funnetStudent.getEmneListe().add(funnetEmne);
        funnetEmne.getStudenter().add(funnetStudent);

        return true;
    }
}

// SokeFunksjoner
// -----------------------------------------------

class SokeFunksjoner {
    private Institutt institutt;
    private int funksjonsvalg = -1;
    private String studentnavn;
    private String emnekode;
    private String bokstavkarakter;

    public SokeFunksjoner(Institutt institutt, int funksjonsvalg, String studentnavn, String emnekode,
            String bokstavkarakter) {
        this.institutt = institutt;
        this.funksjonsvalg = funksjonsvalg;
        this.studentnavn = studentnavn;
        this.emnekode = emnekode;
        this.bokstavkarakter = bokstavkarakter;
    }

    // Finne en student
    public void finnStudent(String studentnavn) {
        boolean funnet = false;
        for (Student s : institutt.getStudentListe()) {
            if (s.getNavn().equalsIgnoreCase(studentnavn)) {
                System.out.println("Fant student: " + s);
                funnet = true;
            }
        }
        if (!funnet) {
            System.out.println("Ingen student funnet med navnet " + studentnavn);
        }
    }

    // Finne alle studenter i et emne med en bestemt karakter
    public void finnAlleStudenterMedKarakter(String emnekode, String bokstavkarakter) {
        boolean funnet = false;
        for (Emne e : institutt.getEmneListe()) {
            if (e.getEmnekode().equals(emnekode)) {
                for (Karakter k : e.getKarakterer()) {
                    if (k.getBokstavKarakter().equals(bokstavkarakter)) {
                        System.out.println(
                                "Student med karakter " + bokstavkarakter + " i " + emnekode + ": " + k.getStudent());
                        funnet = true;
                    }
                }
            }
        }
        if (!funnet) {
            System.out.println("Ingen studenter funnet med karakter " + bokstavkarakter + " i emnet " + emnekode);
        }
    }

    // Finne karakter til en student i et emne
    public void finnEmneKarakter(String studentnavn, String emnekode) {
        boolean funnet = false;
        for (Student s : institutt.getStudentListe()) {
            if (s.getNavn().equalsIgnoreCase(studentnavn)) {
                for (Emne e : s.getEmneListe()) {
                    if (e.getEmnekode().equals(emnekode)) {
                        for (Karakter k : e.getKarakterer()) {
                            if (k.getStudent().getNavn().equalsIgnoreCase(studentnavn)) {
                                System.out.println(
                                        "Karakter for " + studentnavn + " i " + emnekode + ": " + k.getBokstavKarakter());
                                funnet = true;
                            }
                        }
                    }
                }
            }
        }
        if (!funnet) {
            System.out.println("Fant ingen karakter for " + studentnavn + " i emnet " + emnekode);
        }
    }
}
