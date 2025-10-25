package gui;

import model.entities.Employee;
import model.entities.Member;
import model.entities.User;
import model.enums.EmployeeRole;
import model.managers.EmployeeManager;
import model.managers.MemberManager;

import javax.swing.*;

public class LibrarySystemGUI extends JFrame {
    private EmployeeManager employeeManager;
    private MemberManager memberManager;
    private User currentUser;
    private LoginGUI loginGUI;

    public LibrarySystemGUI() {
        initializeManagers();
        showLoginScreen();
    }

    private void initializeManagers() {
        employeeManager = new EmployeeManager("data/employees.csv");
        memberManager = new MemberManager("data/members.csv");
    }

    public void showLoginScreen() {
        loginGUI = new LoginGUI(this);
        loginGUI.setVisible(true);
    }

    public void login(String username, String password) {
        for (Employee emp : employeeManager.getAll()) {
            if (emp.getUsername().equals(username) && emp.getPassword().equals(password)) {
                currentUser = emp;
                loginGUI.dispose();
                showMainMenu();
                return;
            }
        }

        for (Member mem : memberManager.getAll()) {
            if (mem.getUsername().equals(username) && mem.getPassword().equals(password)) {
                currentUser = mem;
                loginGUI.dispose();
                showMainMenu();
                return;
            }
        }

        JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMainMenu() {
        if (currentUser instanceof Employee) {
            Employee emp = (Employee) currentUser;
            if (emp.getRole() == EmployeeRole.ADMINISTRATOR) {
                AdminGUI adminGUI = new AdminGUI(this);
                adminGUI.setVisible(true);
            } else if (emp.getRole() == EmployeeRole.LIBRARIAN) {
                LibrarianGUI librarianGUI = new LibrarianGUI(this);
                librarianGUI.setVisible(true);
            }
        } else if (currentUser instanceof Member) {
            MemberGUI memberGUI = new MemberGUI(this);
            memberGUI.setVisible(true);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
