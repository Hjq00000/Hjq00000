package com.xiaoshu.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

public class Device implements Serializable {
    @Id
    @Column(name = "deviceId")
    private Integer deviceid;

    @Column(name = "devicetypeId")
    private Integer devicetypeid;

    private String devicename;

    private String deviceram;

    private String color;
    
    private String status;

    private Integer price;
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date devicecreatime;
    @Transient
    private Devicetype devicetype;

    public Devicetype getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(Devicetype devicetype) {
		this.devicetype = devicetype;
	}

	private static final long serialVersionUID = 1L;

    /**
     * @return deviceId
     */
    public Integer getDeviceid() {
        return deviceid;
    }

    /**
     * @param deviceid
     */
    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    /**
     * @return devicetypeId
     */
    public Integer getDevicetypeid() {
        return devicetypeid;
    }

    /**
     * @param devicetypeid
     */
    public void setDevicetypeid(Integer devicetypeid) {
        this.devicetypeid = devicetypeid;
    }

    /**
     * @return devicename
     */
    public String getDevicename() {
        return devicename;
    }

    /**
     * @param devicename
     */
    public void setDevicename(String devicename) {
        this.devicename = devicename == null ? null : devicename.trim();
    }

    /**
     * @return deviceram
     */
    public String getDeviceram() {
        return deviceram;
    }

    /**
     * @param deviceram
     */
    public void setDeviceram(String deviceram) {
        this.deviceram = deviceram == null ? null : deviceram.trim();
    }

    /**
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color
     */
    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    /**
     * @return price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return devicecreatime
     */
    public Date getDevicecreatime() {
        return devicecreatime;
    }

    /**
     * @param devicecreatime
     */
    public void setDevicecreatime(Date devicecreatime) {
        this.devicecreatime = devicecreatime;
    }

    @Override
	public String toString() {
		return "Device [deviceid=" + deviceid + ", devicetypeid=" + devicetypeid + ", devicename=" + devicename
				+ ", deviceram=" + deviceram + ", color=" + color + ", status=" + status + ", price=" + price
				+ ", devicecreatime=" + devicecreatime + ", devicetype=" + devicetype + "]";
	}
}