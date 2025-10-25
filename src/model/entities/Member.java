package model.entities;

import model.enums.Gender;
import model.enums.MembershipCategory;
import java.time.LocalDate;

public class Member extends User {
    private MembershipCategory category;
    private int membershipId;
    private int lateReturns;
    private LocalDate lastCancellationDate;

    // Constructor
    public Member(int id, String firstName, String lastName,
                  Gender gender, LocalDate birthDate,
                  String phone, String address, String username,
                  String password, MembershipCategory category, int membershipId, int lateReturns) {
        super(id, firstName, lastName, gender, birthDate, phone, address, username, password);
        this.category = category;
        this.membershipId = membershipId;
        this.lateReturns = lateReturns;
        this.lastCancellationDate = null;
    }

    // Getter and setters
    public MembershipCategory getCategory() { return category; }
    public void setCategory(MembershipCategory category) { this.category = category; }

    public int getMembershipId() { return membershipId; }
    public void setMembershipId(int membershipId) { this.membershipId = membershipId; }

    public int getLateReturns() { return lateReturns; }
    public void setLateReturns(int lateReturns) { this.lateReturns = lateReturns; }

    public LocalDate getLastCancellationDate() { return lastCancellationDate; }
    public void setLastCancellationDate(LocalDate lastCancellationDate) { this.lastCancellationDate = lastCancellationDate; }
}