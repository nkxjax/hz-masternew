package com.example.finalhomework.util_classes;

public class Attraction {
    private int attractionId; // 景区 ID
    private String name; // 景区名称
    private String location; // 景区位置
    private String description; // 景区描述
    private double ticketPrice; // 门票价格
    private String openTime; // 开放时间
    private String closeTime; // 关闭时间

    // 构造方法
    public Attraction() {
    }

    public Attraction(String name, String location, String description, double ticketPrice, String openTime, String closeTime) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.ticketPrice = ticketPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    // Getter 和 Setter 方法
    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    // 重写 toString 方法便于打印对象信息
    @Override
    public String toString() {
        return "Attraction{" +
                "attractionId=" + attractionId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", ticketPrice=" + ticketPrice +
                ", openTime='" + openTime + '\'' +
                ", closeTime='" + closeTime + '\'' +
                '}';
    }
}
