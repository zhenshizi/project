package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class GraphCategoryBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int himokuId;
    private String categoryName;
    private BigDecimal amount;
    private double percentage;

    public GraphCategoryBean() {}

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
