<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>家計簿アプリ - カレンダー(Monthly)個人 | ねこぜ家計簿</title>
    <!-- 共通CSS -->
    <link rel="stylesheet" href="css/共通.css">
    <!-- Monthly専用CSS -->
    <link rel="stylesheet" href="css/calenderm_pn_style.css">
    <!-- フォント -->
    <link href="https://fonts.googleapis.com/css2?family=Kosugi+Maru&display=swap" rel="stylesheet">
<script>
    window.USER_IS_SHARE = ${currentUser.isShare ? true : false};
    window.USER_HAS_GROUP = ${not empty currentUser.groupId ? true : false};
    window.TARGET_USER_ID = ${currentUser.userId};
</script>
</head>
<body>
    <div class="app-container">
        
        <!-- ヘッダーエリア -->
        <header class="app-header">
            <h1 class="page-title">カレンダー（Monthly）個人</h1>
        </header>

        <!-- メインコンテンツエリア -->
        <main class="app-main">
            
            <!-- 上部メッセージバナー -->
            <div class="budget-banner">
                <span class="banner-text">あと <strong class="highlight"><fmt:formatNumber value="${remainBudget}" type="number"/></strong> 円使えます。</span>
                <span class="banner-text">のこり <strong>${remainingDays}</strong> 日</span>
                <span class="banner-text">日割り <strong><fmt:formatNumber value="${dailyBudget}" type="number"/></strong> 円</span>
            </div>

            <!-- 収支カード群（カレンダーの上に2列配置） -->
            <div class="summary-cards-grid">
                <!-- 収入 -->
                <div class="summary-card card-income">
                    <div class="card-badge">収入</div>
                    <div class="card-amount"><fmt:formatNumber value="${income}" type="number"/> <span class="unit">円</span></div>
                </div>
                <!-- 固定支出 -->
                <div class="summary-card card-fixed">
                    <div class="card-badge">固定支出</div>
                    <div class="card-amount"><fmt:formatNumber value="${fixed}" type="number"/> <span class="unit">円</span></div>
                </div>
                <!-- 変動支出 -->
                <div class="summary-card card-variable">
                    <div class="card-badge">変動支出</div>
                    <div class="card-amount"><fmt:formatNumber value="${variable}" type="number"/> <span class="unit">円</span></div>
                </div>
                <!-- 投資・貯蓄 -->
                <div class="summary-card card-saving">
                    <div class="card-badge">投資・貯蓄</div>
                    <div class="card-amount"><fmt:formatNumber value="${invest}" type="number"/> <span class="unit">円</span></div>
                </div>
                <!-- 現在までの収支結果（全幅） -->
                <div class="summary-card card-result full-width">
                    <div class="card-badge">現在までの収支結果</div>
                    <div class="card-amount">
                        <c:if test="${netResult > 0}">+</c:if><fmt:formatNumber value="${netResult}" type="number"/> <span class="unit">円</span>
                    </div>
                </div>
            </div>

            <!-- カレンダーエリア -->
            <div class="calendar-section">
                <div class="calendar-header">
                    <div id="calendar-title" class="calendar-title">${yearMonthStr.substring(0,4)} 年 ${yearMonthStr.substring(5,7)} 月</div>
                    <input type="month" id="month-picker" min="2026-01" max="2050-12" style="display:none;" value="${yearMonthStr}">
                    <button type="button" id="month-picker-btn" style="background:none; border:none; cursor:pointer;"><i class="fa-solid fa-calendar-days"></i></button>
                    <div class="calendar-nav">
                        <button type="button" id="prev-month" class="nav-btn" onclick="location.href='${pageContext.request.contextPath}/calendar-monthly-private?date=${prevYM}'">&lt; 前月</button>
                        <button type="button" id="next-month" class="nav-btn" onclick="location.href='${pageContext.request.contextPath}/calendar-monthly-private?date=${nextYM}'">翌月 &gt;</button>
                    </div>
                </div>

                <table class="calendar-table">
                    <thead>
                        <tr>
                            <th>MON</th>
                            <th>TUE</th>
                            <th>WED</th>
                            <th>THU</th>
                            <th>FRI</th>
                            <th class="sat">SAT</th>
                            <th class="sun">SUN</th>
                        </tr>
                    </thead>
                    <tbody id="calendar-body">
                        <c:forEach items="${cells}" var="cell" varStatus="st">
                            <c:if test="${st.index % 7 == 0}">
                                <tr>
                            </c:if>
                            <c:choose>
                                <c:when test="${cell.currentMonth}">
                                    <td class="<c:if test='${st.index % 7 == 5}'>sat</c:if><c:if test='${st.index % 7 == 6}'>sun</c:if>">
                                        <a href="${pageContext.request.contextPath}/calendar-day-private?date=${cell.dateStr}" class="calendar-day-link">
                                            <span class="date-num">${cell.day}</span>
                                            <c:if test="${cell.hasData}">
                                                <div class="dot" style="background-color: ${cell.userIconColor};"></div>
                                            </c:if>
                                        </a>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td></td>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${st.index % 7 == 6}">
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- ステータス画像エリア（GIF画像） -->
            <div class="status-cat-container">
                <img src="img/animeGIF/${catStatusImg}" alt="猫のステータス" class="status-cat-img">
            </div>

        </main>

        <!-- ★ フッター挿入用のコンテナ -->
        <div id="bottomnav"></div>

    </div> <!-- /.app-container -->

    <script>
    document.addEventListener("DOMContentLoaded", () => {
        const picker = document.getElementById("month-picker");
        const btn = document.getElementById("month-picker-btn");
        if (btn && picker) {
            btn.addEventListener("click", () => {
                picker.showPicker ? picker.showPicker() : picker.click();
            });
            picker.addEventListener("change", (e) => {
                if (e.target.value) {
                    location.href = "${pageContext.request.contextPath}/calendar-monthly-private?date=" + e.target.value;
                }
            });
        }
    });
    </script>
    
    <script src="js/calendar-icon-color.js"></script>

    <!-- ★ フッター自動読み込み用JS -->
    <script src="js/bottomnav-loader.js"></script>

</body>
</html>
