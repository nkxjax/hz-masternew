package com.example.finalhomework.util_classes;

public class Ticket {     // 票ID
    private int id;
    private int userId;            // 用户ID
    private int attractionId;      // 景点ID
    private int quantity;          // 购买数量
    private double totalPrice;     // 总价格
    private long purchaseTime;     // 购买时间（时间戳）
    private int status;
    private String visitDate;
    private String statusChangeTime; // 状态变更时间

    public Ticket(int userId, int attractionId, int quantity, double totalPrice, long purchaseTime, int status, String visitDate, String statusChangeTime) {
        this.userId = userId;
        this.attractionId = attractionId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.purchaseTime = purchaseTime;
        this.status = status;
        this.visitDate = visitDate;
        this.statusChangeTime = statusChangeTime;
    }

    public Ticket() {
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getStatusChangeTime() { return statusChangeTime; }

    public void setStatusChangeTime(String statusChangeTime) {this.statusChangeTime = statusChangeTime;}

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", userId=" + userId +
                ", attractionId=" + attractionId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", purchaseTime=" + purchaseTime +
                ", status=" + status +
                ", visitDate='" + visitDate + '\'' +
                '}';
    }
}
