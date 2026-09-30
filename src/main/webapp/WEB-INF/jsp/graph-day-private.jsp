<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
    <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
      <!DOCTYPE html>
      <html lang="ja">

      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>日間支出ページ(個人) | ねこぜ家計簿</title>
        <link rel="stylesheet" href="css/共通.css">
        <link rel="stylesheet" href="css/gstyle_d_pn.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <!-- フォント -->
        <link href="https://fonts.googleapis.com/css2?family=Kosugi+Maru&display=swap" rel="stylesheet">
        <script>
          window.USER_IS_SHARE = ${ currentUser.isShare ? true : false };
          window.USER_HAS_GROUP = ${not empty currentUser.groupId ? true : false };
        </script>
      </head>

      <body>

        <div class="app-container">

          <!-- ヘッダー -->
          <header class="header">
            <h1 class="page-title">日間支出ページ（個人）</h1>
          </header>

          <!-- メイン -->
          <main class="app-main">

            <!-- 日付ナビ -->
            <div class="date-selector">
              <button id="prev-day" type="button" class="arrow-btn"
                onclick="location.href='${pageContext.request.contextPath}/graph-day-private?date=${prevDate}'">&lt;</button>

              <div class="date-picker-wrapper">
                <span class="current-date">${dateVal.year}年 ${dateVal.monthValue}月 ${dateVal.dayOfMonth}日</span>
                <button type="button" id="date-btn" class="calendar-btn"><i
                    class="fa-solid fa-calendar-days"></i></button>
                <input type="date" id="date-picker" min="2026-01-01" max="2050-12-31" style="display:none;"
                  value="${dateStr}">
              </div>

              <button id="next-day" type="button" class="arrow-btn"
                onclick="location.href='${pageContext.request.contextPath}/graph-day-private?date=${nextDate}'">&gt;</button>
            </div>

            <!-- 日間支出合計 -->
            <section class="total-card" style="margin: 0 auto;">
              <div class="total-label">日間支出合計</div>
              <div class="total-amount">
                <fmt:formatNumber value="${totalExpense}" type="number" /> <span class="currency">円</span>
              </div>
            </section>

            <!-- グラフ＆カテゴリ -->
            <section class="chart-section" style="margin-top: 20px;">

              <!-- グラフ -->
              <div class="chart-container">
                <div class="dummy-chart">
                  <div class="chart-center-text">
                    <span class="sub">日間支出</span>
                    <span class="val">
                      <fmt:formatNumber value="${totalExpense}" type="number" />円
                    </span>
                  </div>
                </div>
              </div>

              <!-- カテゴリリスト -->
              <ul class="category-list">
                <c:forEach items="${catList}" var="c">
                  <li class="category-item" data-name="${c.categoryName}" data-rate="${c.percentage}">
                    <span class="color-badge"></span>
                    <span class="category-name">${c.categoryName}</span>
                    <span class="category-rate">${c.percentage}%</span>
                  </li>
                </c:forEach>
                <c:if test="${empty catList}">
                  <li class="category-item">
                    <span class="category-name">データがありません</span>
                  </li>
                </c:if>
              </ul>
            </section>

          </main>

          <!-- ★ フッター -->
          <div id="bottomnav"></div>

        </div> <!-- /.app-container -->

        <script>
          document.addEventListener("DOMContentLoaded", () => {
            const picker = document.getElementById("date-picker");
            const btn = document.getElementById("date-btn");
            if (btn && picker) {
              btn.addEventListener("click", () => {
                picker.showPicker ? picker.showPicker() : picker.click();
              });
              picker.addEventListener("change", (e) => {
                if (e.target.value) {
                  location.href = "${pageContext.request.contextPath}/graph-day-private?date=" + e.target.value;
                }
              });
            }
          });
        </script>

        <script src="js/graph-chart.js"></script>

        <!-- ★ フッター自動読み込み用JS -->
        <script src="js/bottomnav-loader.js"></script>

      </body>

      </html>