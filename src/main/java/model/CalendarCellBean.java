package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CalendarCellBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int day;
    private String dateStr; // YYYY-MM-DD
    private boolean isCurrentMonth;
    private boolean hasData;
    private String userIconColor; // 既存の互換性のため残す
    
 // ★ 複数ユーザのカラーリストを追加（空リストで初期化してNullPointerExceptionを防止）
    private List<String> userColorList = new ArrayList<>();

    public CalendarCellBean() {}

    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    public String getDateStr() { return dateStr; }
    public void setDateStr(String dateStr) { this.dateStr = dateStr; }

    public boolean isCurrentMonth() { return isCurrentMonth; }
    public boolean getIsCurrentMonth() { return isCurrentMonth; }
    public void setCurrentMonth(boolean isCurrentMonth) { this.isCurrentMonth = isCurrentMonth; }

    public boolean isHasData() { return hasData; }
    public boolean getHasData() { return hasData; }
    public void setHasData(boolean hasData) { this.hasData = hasData; }

    public String getUserIconColor() { return userIconColor; }
    public void setUserIconColor(String userIconColor) { this.userIconColor = userIconColor; }

    // ★ 追加したゲッター・セッター
    public List<String> getUserColorList() {
        if (userColorList == null) {
            userColorList = new ArrayList<>();
        }
        return userColorList;
    }

    public void setUserColorList(List<String> userColorList) {
        this.userColorList = userColorList;
    }
}