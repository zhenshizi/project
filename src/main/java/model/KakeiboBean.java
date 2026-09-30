package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class KakeiboBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private Date date;
    private String memo;
    private BigDecimal yen;
    private String categoryType; // "0":収入, "1":固定, "2":変動, "3":投資・貯蓄
    private boolean mode; // false:個人, true:共有
    private int himokuId;
    private String categoryName; // JOIN費目

    public KakeiboBean() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getYen() {
        return yen;
    }

    public void setYen(BigDecimal yen) {
        this.yen = yen;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public boolean isMode() {
        return mode;
    }

    public boolean getMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public int getHimokuId() {
        return himokuId;
    }

    public void setHimokuId(int himokuId) {
        this.himokuId = himokuId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
