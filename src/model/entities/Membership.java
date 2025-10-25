package model.entities;

import model.enums.MembershipStatus;

import java.time.LocalDate;

public class Membership {
    private int id;
    private int memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipStatus status;
    private String type;

    public Membership(int id, int memberId, LocalDate startDate, LocalDate endDate, MembershipStatus status, String type) {
        this.id = id;
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.type = type;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public MembershipStatus getStatus() { return status; }
    public void setStatus(MembershipStatus status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isActive() { return status == MembershipStatus.ACTIVE; }
}