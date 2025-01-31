package com.jason.supplymanagement.entity;

import jakarta.persistence.*;

/**
 * @author : Jason Stephen
 * @date :Created in 2025-01-20
 */

@Entity
@Table(name = "Logistics_Company")
public class LogisticsCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logistics_company_id")
    private int logisticsCompanyId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "contact_person", nullable = false, length = 50)
    private String contactPerson;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    // Getters and Setters
    public int getLogisticsCompanyId() {
        return logisticsCompanyId;
    }

    public void setLogisticsCompanyId(int logisticsCompanyId) {
        this.logisticsCompanyId = logisticsCompanyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "LogisticsCompany{" +
                "logisticsCompanyId=" + logisticsCompanyId +
                ", name='" + name + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}