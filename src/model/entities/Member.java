package model.entities;

import model.enums.Gender;
import model.enums.MembershipCategory;
import java.time.LocalDate;

public class Member extends User {
    private MembershipCategory category;
    private Membership membership;

    public Member(int id, String firstName, String lastName,
                  Gender gender, LocalDate birthDate,
                  String phone, String address, String username,
                  String password, MembershipCategory category) {
        super(id, firstName, lastName, gender, birthDate, phone, address, username, password);
        this.category = category;
    }

    // Getteri i setteri
    public MembershipCategory getCategory() { return category; }
    public void setCategory(MembershipCategory category) { this.category = category; }

    public Membership getMembership() { return membership; }
    public void setMembership(Membership membership) { this.membership = membership; }
}