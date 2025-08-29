package model.entities;

import model.enums.Gender;
import model.enums.MembershipCategory;
import java.time.LocalDate;

public class Member extends User {
    private MembershipCategory category;
    private int membershipId;

    // Constructor
    public Member(int id, String firstName, String lastName,
                  Gender gender, LocalDate birthDate,
                  String phone, String address, String username,
                  String password, MembershipCategory category, int membershipId) {
        super(id, firstName, lastName, gender, birthDate, phone, address, username, password);
        this.category = category;
        this.membershipId = membershipId;
    }

    // Getter and setters
    public MembershipCategory getCategory() { return category; }
    public void setCategory(MembershipCategory category) { this.category = category; }

    public int getMembershipId() { return membershipId; }
    public void setMembershipId(int membershipId) { this.membershipId = membershipId; }
}