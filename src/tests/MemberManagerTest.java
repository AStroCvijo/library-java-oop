package tests;

import model.entities.Member;
import model.enums.Gender;
import model.enums.MembershipCategory;
import model.managers.MemberManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemberManagerTest {

    private MemberManager memberManager;
    private String testFilename = "test_members.csv";

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(testFilename);
        testFile.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("id,firstName,lastName,gender,birthDate,phone,address,username,password,category,membershipId,lateReturns,lastCancellationDate");
        }
        memberManager = new MemberManager(testFilename);
    }

    @AfterEach
    void tearDown() {
        new File(testFilename).delete();
    }

    @Test
    void addAndGetMember() {
        Member member = new Member(1, "John", "Doe", Gender.MALE, LocalDate.of(1995, 5, 5), "123", "Street", "johndoe", "pass", MembershipCategory.STUDENT, 1, 0);
        memberManager.add(member);
        Member retrievedMember = memberManager.getById(1);
        assertNotNull(retrievedMember);
        assertEquals("John", retrievedMember.getFirstName());
    }

    @Test
    void updateMember() {
        Member member = new Member(1, "John", "Doe", Gender.MALE, LocalDate.of(1995, 5, 5), "123", "Street", "johndoe", "pass", MembershipCategory.STUDENT, 1, 0);
        memberManager.add(member);
        member.setLastName("Smith");
        memberManager.update(member);
        Member retrievedMember = memberManager.getById(1);
        assertEquals("Smith", retrievedMember.getLastName());
    }

    @Test
    void deleteMember() {
        Member member = new Member(1, "John", "Doe", Gender.MALE, LocalDate.of(1995, 5, 5), "123", "Street", "johndoe", "pass", MembershipCategory.STUDENT, 1, 0);
        memberManager.add(member);
        memberManager.delete(1);
        assertNull(memberManager.getById(1));
    }

    @Test
    void getAllMembers() {
        memberManager.add(new Member(1, "John", "Doe", Gender.MALE, LocalDate.of(1995, 5, 5), "123", "Street", "johndoe", "pass", MembershipCategory.STUDENT, 1, 0));
        memberManager.add(new Member(2, "Jane", "Doe", Gender.FEMALE, LocalDate.of(1998, 8, 8), "456", "Avenue", "janedoe", "pass", MembershipCategory.PUPIL, 2, 0));
        List<Member> members = memberManager.getAll();
        assertEquals(2, members.size());
    }
}
