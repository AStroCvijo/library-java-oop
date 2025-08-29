import model.entities.*;
import model.enums.*;
import model.managers.*;
import java.time.LocalDate;

public class Main {
    private static EmployeeManager employeeManager;
    private static MemberManager memberManager;
    private static BookManager bookManager;
    private static GenreManager genreManager;
    private static ReservationManager reservationManager;
    private static PriceListManager priceListManager;
    private static MembershipManager membershipManager;

    public static void main(String[] args) {
        initializeManagers();
        runTestScenario();
    }

    private static void initializeManagers() {
        employeeManager = new EmployeeManager("data/employees.csv");
        memberManager = new MemberManager("data/members.csv");
        bookManager = new BookManager("data/books.csv");
        genreManager = new GenreManager("data/genres.csv");
        reservationManager = new ReservationManager("data/reservations.csv");
        priceListManager = new PriceListManager("data/pricelist.csv");
        membershipManager = new MembershipManager("data/memberships.csv");
    }

    private static void runTestScenario() {
        System.out.println("=== POČETAK TEST SCENARIJA ===");

        // 1. Kreiranje administratora (Pera Perić)
        Employee admin = new Employee(
                1, "Pera", "Perić", Gender.MALE,
                LocalDate.of(1980, 5, 15), "0611234567",
                "Glavna ulica 1", "pera.peric", "admin123",
                EmployeeEducationLevel.BACHELOR, 10, 50000,
                EmployeeRole.ADMINISTRATOR
        );
        employeeManager.add(admin);
        System.out.println("Kreiran administrator: Pera Perić");

        // 2. Dodavanje bibliotekara (Mika Mikić i Nikola Nikolić)
        Employee librarian1 = new Employee(
                2, "Mika", "Mikić", Gender.MALE,
                LocalDate.of(1990, 8, 22), "0612345678",
                "Prva ulica 2", "mika.mikic", "librarian123",
                EmployeeEducationLevel.BACHELOR, 5, 40000,
                EmployeeRole.LIBRARIAN
        );

        Employee librarian2 = new Employee(
                3, "Nikola", "Nikolić", Gender.MALE,
                LocalDate.of(1985, 3, 10), "0613456789",
                "Druga ulica 3", "nikola.nikolic", "librarian456",
                EmployeeEducationLevel.MASTER, 8, 45000,
                EmployeeRole.LIBRARIAN
        );

        employeeManager.add(librarian1);
        employeeManager.add(librarian2);
        System.out.println("Dodati bibliotekari: Mika Mikić i Nikola Nikolić");

        // 3. Prikaz svih zaposlenih
        System.out.println("\nSvi zaposleni:");
        for (Employee emp : employeeManager.getAll()) {
            System.out.println(emp.getFirstName() + " " + emp.getLastName() +
                    " - " + emp.calculateSalary() + " RSD");
        }

        // 4. Uklanjanje Nikole Nikolića
        employeeManager.delete(3);
        System.out.println("\nUklonjen Nikola Nikolić iz sistema\n");

        Membership membership1 = new Membership(
                1, 1, LocalDate.now(), LocalDate.now().plusMonths(1), true, "MONTHLY"
        );

        // 5. Dodavanje članova (Milica Milić i Ana Anić)
        Member member1 = new Member(
                1, "Milica", "Milić", Gender.FEMALE,
                LocalDate.of(1998, 7, 12), "0621112222",
                "Studentska 5", "milica.milic@email.com", "member123",
                MembershipCategory.STUDENT, 1
        );

        Membership membership2 = new Membership(
                2, 2, LocalDate.now(), LocalDate.now().plusYears(1), true, "YEARLY"
        );

        Member member2 = new Member(
                2, "Ana", "Anić", Gender.FEMALE,
                LocalDate.of(1985, 11, 3), "0633334444",
                "Radnička 8", "ana.anic@email.com", "member456",
                null, 2
        );

        membershipManager.add(membership1);
        membershipManager.add(membership2);
        memberManager.add(member1);
        memberManager.add(member2);
        System.out.println("Dodati članovi: Milica Milić (STUDENT) i Ana Anić\n");

        // 6. Dodavanje žanrova
        Genre genre1 = new Genre(1, "Roman", "Književni žanr");
        Genre genre2 = new Genre(2, "Drama", "Dramski žanr");
        Genre genre3 = new Genre(3, "Naučna fantastika", "SF žanr");

        genreManager.add(genre1);
        genreManager.add(genre2);
        genreManager.add(genre3);
        System.out.println("Dodati žanrovi: Roman, Drama, Naučna fantastika\n");

        // 7. Dodavanje knjiga
        Book book1 = new Book(
                1, "Ana Karenjina", "Lav Tolstoj", "9788610033647",
                1877, 1, 1, 1, BookStatus.AVAILABLE
        );

        Book book2 = new Book(
                2, "Ana Karenjina", "Lav Tolstoj", "9788610033647",
                1877, 1, 1, 1, BookStatus.AVAILABLE
        );

        Book book3 = new Book(
                3, "Rat i mir", "Lav Tolstoj", "9788661451072",
                1869, 2, 1, 1 ,BookStatus.AVAILABLE
        );

        Book book4 = new Book(
                4, "1984", "Džordž Orvel", "9788661451071",
                1949, 3, 1, 1, BookStatus.AVAILABLE
        );

        bookManager.add(book1);
        bookManager.add(book2);
        bookManager.add(book3);
        bookManager.add(book4);
        System.out.println("Dodate knjige: Ana Karenjina (2 primerka), Rat i mir, 1984\n");

        // 8. Kreiranje cenovnika
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 25);

        // OSNOVNE PRETPLATE
        PriceListItem monthlySub = new PriceListItem(1, PriceListItemType.MONTHLY_SUBSCRIPTION, "Mesečna pretplata", 1000, startDate, endDate);
        PriceListItem yearlySub = new PriceListItem(2, PriceListItemType.YEARLY_SUBSCRIPTION, "Godišnja pretplata", 10000, startDate, endDate);

        // POPUSTI (kao procenti)
        PriceListItem studentDiscount = new PriceListItem(3, PriceListItemType.STUDENT_DISCOUNT, "Popust za studente (20%)", 0.20, startDate, endDate);
        PriceListItem pensionerDiscount = new PriceListItem(4, PriceListItemType.RETIREE_DISCOUNT, "Popust za penzionere (30%)", 0.30, startDate, endDate);
        PriceListItem childDiscount = new PriceListItem(5, PriceListItemType.CHILD_DISCOUNT, "Popust za decu (40%)", 0.40, startDate, endDate);
        PriceListItem pupilDiscount = new PriceListItem(6, PriceListItemType.PUPIL_DISCOUNT, "Popust za učenike (25%)", 0.25, startDate, endDate);

        // DODATNE USLUGE
        PriceListItem priorityPickup = new PriceListItem(7, PriceListItemType.PRIORITY_PICKUP, "Prioritetno preuzimanje", 200, startDate, endDate);
        PriceListItem priorityReturn = new PriceListItem(8, PriceListItemType.PRIORITY_RETURN, "Prioritetno vraćanje", 150, startDate, endDate);
        PriceListItem extendedRetention = new PriceListItem(9, PriceListItemType.EXTENDED_RETENTION, "Produženo zadržavanje", 50, startDate, endDate);

        // KAZNE
        PriceListItem penalty = new PriceListItem(10, PriceListItemType.PENALTY, "Kazna za kašnjenje", 100, startDate, endDate);

        // Dodavanje u cenovnik
        priceListManager.add(monthlySub);
        priceListManager.add(yearlySub);
        priceListManager.add(studentDiscount);
        priceListManager.add(pensionerDiscount);
        priceListManager.add(childDiscount);
        priceListManager.add(pupilDiscount);
        priceListManager.add(priorityPickup);
        priceListManager.add(priorityReturn);
        priceListManager.add(extendedRetention);
        priceListManager.add(penalty);
        System.out.println("Kreiran cenovnik za period 01.01.2025 - 25.12.2025\n");

        // 9. Izmena cene mesečne pretplate
        PriceListItem updatedMonthlySub = new PriceListItem(
                1, PriceListItemType.MONTHLY_SUBSCRIPTION,
                "Mesečna pretplata", 1200, startDate, endDate
        );
        priceListManager.update(updatedMonthlySub);
        System.out.println("Izmenjena cena mesečne pretplate sa 1000 na 1200 RSD");

        // 10. Prikaz dostupnih knjiga za period 01.11.2025 - 30.11.2025
        LocalDate startPeriod1 = LocalDate.of(2025, 11, 1);
        LocalDate endPeriod1 = LocalDate.of(2025, 11, 30);

        System.out.println("\nDostupne knjige u periodu 01.11.2025 - 30.11.2025:");
        for (Book book : bookManager.getAvailableBooks(startPeriod1, endPeriod1, reservationManager)) {
            System.out.println(book.getTitle() + " - " + book.getAuthor() + " - " + book.getAvailableCopies());
        }

        // 11. Kreiranje rezervacije za Milicu Milić za Anu Karenjinu
        LocalDate pickupDate1 = LocalDate.of(2025, 11, 13);
        Reservation reservation1 = new Reservation(
                1, 1, 1, LocalDate.now(), pickupDate1,
                pickupDate1.plusDays(7), ReservationStatus.PENDING, priceListManager.getById(7).getPrice()
        );

        reservationManager.add(reservation1);
        System.out.println("\nKreirana rezervacija za Milicu Milić za knjigu 'Ana Karenjina' na dan 13.11.2025");

        // 12. Prikaz dostupnih knjiga za period 01.12.2025 - 12.12.2025
        LocalDate startPeriod2 = LocalDate.of(2025, 12, 1);
        LocalDate endPeriod2 = LocalDate.of(2025, 12, 12);

        System.out.println("\nDostupne knjige u periodu 01.12.2025 - 12.12.2025:");
        for (Book book : bookManager.getAvailableBooks(startPeriod2, endPeriod2, reservationManager)) {
            System.out.println(book.getTitle() + " - " + book.getAuthor() + " - " + book.getAvailableCopies());
        }

        // 13. Kreiranje rezervacije za Anu Anić
        LocalDate pickupDate2 = LocalDate.of(2025, 12, 7);
        Reservation reservation2 = new Reservation(
                2, 2, 4, LocalDate.now(), pickupDate2,
                pickupDate2.plusDays(7), ReservationStatus.PENDING, 0
        );

        reservationManager.add(reservation2);
        System.out.println("\nKreirana rezervacija za Anu Anić za knjigu '1984' na dan 07.12.2025");

        // 14. Prikaz svih rezervacija Milice Milić
        System.out.println("\nRezervacije Milice Milić:");
        for (Reservation res : reservationManager.getReservationsByMember(1)) {
            Book book = bookManager.getById(res.getBookId());
            System.out.println(book.getTitle() + " - " + res.getPickupDate() +
                    " - Status: " + res.getStatus());
        }

        System.out.println("\n=== KRAJ TEST SCENARIJA ===");
    }
}