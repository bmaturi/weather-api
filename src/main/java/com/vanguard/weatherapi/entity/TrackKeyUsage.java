package com.vanguard.weatherapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRACK_KEY_USAGE")
public class TrackKeyUsage {

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;

    @Column(name = "KEY_NAME")
    private String keyName;

    @Column(name = "COUNT")
    private Integer count;

    @Column(name = "LAST_USED")
    private Date lastUsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

}
