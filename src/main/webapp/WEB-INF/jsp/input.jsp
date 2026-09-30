<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>家計簿入力ページ | ねこぜ家計簿</title>

    <!-- 共通CSS -->
    <link rel="stylesheet" href="css/共通.css">

    <!-- カレンダーCSS -->
    <link rel="stylesheet" href="css/in_style.css">
    <!-- カレンダーマーク -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <!-- フォント -->
    <link href="https://fonts.googleapis.com/css2?family=Kosugi+Maru&display=swap" rel="stylesheet">
<script>
    window.USER_IS_SHARE = ${currentUser.isShare ? true : false};
    window.USER_HAS_GROUP = ${not empty currentUser.groupId ? true : false};
</script>
</head>
<body>
    <div class="app-container">

        <!-- ヘッダー -->
        <header class="header">
            <h1 class="page-title">家計簿入力ページ</h1>
        </header>

        <!-- メイン -->
        <main class="app-main">

            <c:if test="${not empty popError}">
                <script>
                    alert("${popError}");
                </script>
            </c:if>

            <form id="inputForm" action="${pageContext.request.contextPath}/input" method="post">
                <input type="hidden" name="id" value="${not empty kakeibo ? kakeibo.id : editId}">
                <input type="hidden" name="date" id="form-date" value="${not empty kakeibo ? kakeibo.date : (not empty dateStr ? dateStr : (not empty inputDate ? inputDate : ''))}">
                <input type="hidden" name="himoku_id" id="form-himoku-id" value="${not empty kakeibo ? kakeibo.himokuId : himokuIdStr}">

                <div class="table-header-bar">
                    <div class="date-row">
                        <div id="date-display" class="date-display"></div>

                        <!-- カレンダーアイコン -->
                        <button type="button" id="calendar-btn" class="calendar-btn" aria-label="日付選択">
                            <i class="fa-solid fa-calendar-days"></i>
                        </button>

                        <!-- カレンダー input -->
                        <input type="date" id="date-picker" class="date-picker" min="2026-01-01" max="2050-12-31" style="display:none;">
                    </div>

                    <div class="date-nav">
                        <button type="button" id="prev-day" class="nav-arrow-btn">&lt;</button>
                        <button type="button" id="next-day" class="nav-arrow-btn">&gt;</button>
                    </div>
                </div>

                <!-- 入力フォーム -->
                <div class="input-group">
                    <input type="text" name="memo" placeholder="内容を入力（例：ランチ）" value="${not empty kakeibo ? kakeibo.memo : memo}">
                </div>

                <div class="input-group price-input">
                    <input type="number" name="yen" placeholder="金額" value="${not empty kakeibo ? kakeibo.yen : yenStr}">
                    <span class="unit">円</span>
                </div>

                <div class="input-group select-wrapper">
                    <c:set var="mVal" value="${not empty kakeibo ? kakeibo.mode : modeStr}" />
                    <select name="mode">
                        <option value="" disabled <c:if test="${empty mVal}">selected</c:if>>カテゴリを選択</option>
                         <option value="false" <c:if test="${mVal == 'false' || mVal == false}">selected</c:if>>個人</option>
                        <option value="true" <c:if test="${mVal == 'true' || mVal == true}">selected</c:if>>共有</option>
                       
                    </select>
                </div>

                <!-- ▼ 収入カテゴリ -->
                <section class="category-group income">
                    <h2 class="category-title">収入</h2>
                    <div class="category-tags">
                        <button type="button" class="tag" data-himoku="0">給与</button>
                        <button type="button" class="tag" data-himoku="1">その他</button>
                    </div>
                </section>

                <!-- ▼ 固定支出カテゴリ -->
                <section class="category-group fixed-expense">
                    <h2 class="category-title">固定支出</h2>
                    <div class="category-tags">
                        <button type="button" class="tag" data-himoku="10">家賃</button>
                        <button type="button" class="tag" data-himoku="11">ガス代</button>
                        <button type="button" class="tag" data-himoku="12">電気代</button>
                        <button type="button" class="tag" data-himoku="13">水道代</button>
                        <button type="button" class="tag" data-himoku="14">通信費</button>
                        <button type="button" class="tag" data-himoku="15">保険料</button>
                        <button type="button" class="tag" data-himoku="16">教育費</button>
                        <button type="button" class="tag" data-himoku="17">その他</button>
                    </div>
                </section>

                <!-- ▼ 変動支出カテゴリ -->
                <section class="category-group variable-expense">
                    <h2 class="category-title">変動支出</h2>
                    <div class="category-tags">
                        <button type="button" class="tag" data-himoku="30">美容費</button>
                        <button type="button" class="tag" data-himoku="31">医療費</button>
                        <button type="button" class="tag" data-himoku="32">食費</button>
                        <button type="button" class="tag" data-himoku="33">被服</button>
                        <button type="button" class="tag" data-himoku="34">交際費</button>
                        <button type="button" class="tag" data-himoku="35">交通費</button>
                        <button type="button" class="tag" data-himoku="36">日用品</button>
                        <button type="button" class="tag" data-himoku="37">趣味</button>
                        <button type="button" class="tag" data-himoku="38">経費</button>
                        <button type="button" class="tag" data-himoku="39">その他</button>
                    </div>
                </section>

                <!-- ▼ 投資・貯蓄カテゴリ -->
                <section class="category-group investment">
                    <h2 class="category-title">投資・貯蓄</h2>
                    <div class="category-tags">
                        <button type="button" class="tag" data-himoku="50">投資</button>
                        <button type="button" class="tag" data-himoku="51">貯蓄</button>
                    </div>
                </section>

                <!-- アクションボタン -->
                <div class="form-actions" style="display: flex; justify-content: space-between; margin-top: 16px; width: 100%;">
                    <button type="button" class="btn-cancel" onclick="location.href='${pageContext.request.contextPath}/calendar-monthly-private'">キャンセル</button>
                    <button type="submit" class="btn-save">保存</button>
                </div>
            </form>

        </main>

        <!-- ★ フッター挿入用のコンテナ -->
        <div id="bottomnav"></div>

    </div> <!-- /.app-container -->

    
    <script>
    document.addEventListener("DOMContentLoaded", () => {
        const formDate = document.getElementById("form-date");
        const dateDisplay = document.getElementById("date-display");
        const datePicker = document.getElementById("date-picker");
        const calendarBtn = document.getElementById("calendar-btn");
        const prevBtn = document.getElementById("prev-day");
        const nextBtn = document.getElementById("next-day");

        // 安全に日付をパースする関数
        function parseDate(val) {
            if (!val || typeof val !== 'string') return new Date();
            const parts = val.trim().split('-');
            if (parts.length === 3) {
                const y = parseInt(parts[0], 10);
                const m = parseInt(parts[1], 10) - 1;
                const d = parseInt(parts[2], 10);
                if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
                    const dObj = new Date(y, m, d);
                    if (!isNaN(dObj.getTime())) return dObj;
                }
            }
            const parsed = new Date(val);
            return isNaN(parsed.getTime()) ? new Date() : parsed;
        }

        // 初期日付の取得（取れなければ今日）
        let currentDate = parseDate(formDate.value);
        if (isNaN(currentDate.getTime())) {
            currentDate = new Date();
        }

        function updateDateUI() {
            if (!currentDate || isNaN(currentDate.getTime())) {
                currentDate = new Date();
            }
            const y = currentDate.getFullYear();
            const m = String(currentDate.getMonth() + 1).padStart(2, '0');
            const d = String(currentDate.getDate()).padStart(2, '0');
            const days = ['日', '月', '火', '水', '木', '金', '土'];
            const dayStr = days[currentDate.getDay()] || '';

            // 画面に表示（JSPのEL式と競合しないよう文字列結合に変更）
            dateDisplay.textContent = y + '年 ' + m + '月 ' + d + '日 (' + dayStr + ')';
            
            // hidden要素とdatePickerに値をセット
            const isoStr = y + '-' + m + '-' + d;
            formDate.value = isoStr;
            datePicker.value = isoStr;
        }

        // 最初に画面を更新
        updateDateUI();

        calendarBtn.addEventListener("click", () => {
            datePicker.showPicker ? datePicker.showPicker() : datePicker.click();
        });

        datePicker.addEventListener("change", (e) => {
            if (e.target.value) {
                currentDate = parseDate(e.target.value);
                updateDateUI();
            }
        });

        prevBtn.addEventListener("click", () => {
            currentDate.setDate(currentDate.getDate() - 1);
            updateDateUI();
        });

        nextBtn.addEventListener("click", () => {
            currentDate.setDate(currentDate.getDate() + 1);
            updateDateUI();
        });

        // 費目ボタン選択
        const tags = document.querySelectorAll(".tag");
        const formHimokuId = document.getElementById("form-himoku-id");

        if (formHimokuId.value) {
            tags.forEach(t => {
                if (t.getAttribute("data-himoku") === formHimokuId.value) {
                    t.classList.add("active");
                }
            });
        }

        tags.forEach(tag => {
            tag.addEventListener("click", () => {
                tags.forEach(t => t.classList.remove("active"));
                tag.classList.add("active");
                formHimokuId.value = tag.getAttribute("data-himoku");
            });
        });
    });
  
    </script>

    <!-- ★ フッター自動読み込み用JS -->
    <script src="js/bottomnav-loader.js"></script>

</body>
</html>
