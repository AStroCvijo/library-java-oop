package tests;

import model.entities.Membership;
import model.enums.MembershipStatus;
import model.managers.MembershipManager;
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

class MembershipManagerTest {

    private MembershipManager membershipManager;
    private String testFilename = "test_memberships.csv";

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(testFilename);
        testFile.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("id,memberId,startDate,endDate,status,type");
        }
        membershipManager = new MembershipManager(testFilename);
    }

    @AfterEach
    void tearDown() {
        new File(testFilename).delete();
    }

    @Test
    void addAndGetMembership() {
        Membership membership = new Membership(1, 1, LocalDate.now(), LocalDate.now().plusYears(1), MembershipStatus.ACTIVE, "YEARLY");
        membershipManager.add(membership);
        Membership retrievedMembership = membershipManager.getById(1);
        assertNotNull(retrievedMembership);
        assertEquals(1, retrievedMembership.getMemberId());
    }

    @Test
    void updateMembership() {
        Membership membership = new Membership(1, 1, LocalDate.now(), LocalDate.now().plusYears(1), MembershipStatus.ACTIVE, "YEARLY");
        membershipManager.add(membership);
        membership.setStatus(MembershipStatus.INACTIVE);
        membershipManager.update(membership);
        Membership retrievedMembership = membershipManager.getById(1);
        assertEquals(MembershipStatus.INACTIVE, retrievedMembership.getStatus());
    }

    @Test
    void deleteMembership() {
        Membership membership = new Membership(1, 1, LocalDate.now(), LocalDate.now().plusYears(1), MembershipStatus.ACTIVE, "YEARLY");
        membershipManager.add(membership);
        membershipManager.delete(1);
        assertNull(membershipManager.getById(1));
    }

    @Test
    void getAllMemberships() {
        membershipManager.add(new Membership(1, 1, LocalDate.now(), LocalDate.now().plusYears(1), MembershipStatus.ACTIVE, "YEARLY"));
        membershipManager.add(new Membership(2, 2, LocalDate.now(), LocalDate.now().plusMonths(1), MembershipStatus.ACTIVE, "MONTHLY"));
        List<Membership> memberships = membershipManager.getAll();
        assertEquals(2, memberships.size());
    }
}
