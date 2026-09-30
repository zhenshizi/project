<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>カレンダー(Day) | ねこぜ家計簿</title>

    <!-- 共通CSS -->
    <link rel="stylesheet" href="css/共通.css">

    <!-- カレンダー(Day)専用CSS -->
    <link rel="stylesheet" href="css/calenderd_sh_style.css">

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

<header class="app-header shared">

<div class="header-title-wrap">
                <h1 class="page-title">カレンダー（Day）（共有）</h1>

                <!-- だれの？ボタン フォルダ/ドロップダウン -->
                <div class="who-dropdown">
                    <button type="button" class="who-btn">だれの？</button>
                    <div class="who-menu">
                        <c:forEach items="${groupMembers}" var="m">
                            <a href="${pageContext.request.contextPath}/calendar-day-share?date=${dateStr}&target_user_id=${m.userId}" class="who-item">${m.nickname}</a>
                        </c:forEach>
                    </div>
                </div>
            </div>
</header>

<main class="app-main">

    <!-- 日付表示バー -->
    <div class="table-header-bar">
        <div class="date-row">
            <div id="date-display" class="date-display">${dateVal.year}年 ${dateVal.monthValue}月 ${dateVal.dayOfMonth}日</div>

            <!-- カレンダーアイコン -->
            <button type="button" id="calendar-btn" class="calendar-btn" aria-label="日付選択">
                <i class="fa-solid fa-calendar-days"></i>
            </button>

            <!-- カレンダー input -->
            <input type="date" id="date-picker" class="date-picker" min="2026-01-01" max="2050-12-31" style="display:none;" value="${dateStr}">
        </div>

        <div class="date-nav">
            <button type="button" id="prev-day" class="nav-arrow-btn" onclick="location.href='${pageContext.request.contextPath}/calendar-day-share?date=${prevDate}&target_user_id=${targetUserId}'">&lt;</button>
            <button type="button" id="next-day" class="nav-arrow-btn" onclick="location.href='${pageContext.request.contextPath}/calendar-day-share?date=${nextDate}&target_user_id=${targetUserId}'">&gt;</button>
        </div>
    </div>

  <table class="expense-table">
    <thead>
        <tr>
            <th class="col-category">カテゴリ</th>
            <th class="col-amount">金額</th>
            <th class="col-memo">メモ</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${dailyList}" var="k">
            <tr <c:if test="${isOwner}">onclick="openRowModal('${k.id}', '${k.categoryName}', '${k.yen}')" style="cursor:pointer;"</c:if>>
                <td class="category-cell"><span class="cat-label ${k.categoryName}">${k.categoryName}</span></td>
                <td class="amount-cell"><fmt:formatNumber value="${k.yen}" type="number"/> 円</td>
                <td class="memo-cell">${k.memo}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty dailyList}">
            <tr>
                <td class="category-cell"></td>
                <td class="amount-cell">0 円</td>
                <td class="memo-cell"></td>
            </tr>
        </c:if>
    </tbody>
</table>

<div id="row-modal" class="row-modal">
  <div class="row-modal-content">
    <p id="modal-category"></p>
    <p id="modal-amount"></p>
    <div class="row-modal-btns">
      <button type="button" id="row-edit" class="row-edit-btn">編集</button>
      <button type="button" id="row-delete" class="row-delete-btn">削除</button>
    </div>
  </div>
</div>

<div id="delete-modal" class="delete-modal">
  <div class="delete-modal-content">
    <p>削除しますか？</p>
    <div class="delete-modal-btns">
      <button type="button" id="delete-yes" class="delete-yes">はい</button>
      <button type="button" id="delete-no" class="delete-no">いいえ</button>
    </div>
  </div>
</div>

<form id="deleteKakeiboForm" action="${pageContext.request.contextPath}/calendar-day-share" method="post" style="display:none;">
    <input type="hidden" name="action" value="delete_kakeibo">
    <input type="hidden" name="date" value="${dateStr}">
    <input type="hidden" name="target_user_id" value="${targetUserId}">
    <input type="hidden" name="id" id="delete_kakeibo_id">
</form>

    <div class="total-area">
        <div class="total-label-box">今日の支出合計</div>
        <div class="total-amount-box"><fmt:formatNumber value="${dayTotal}" type="number"/> 円</div>
    </div>

    <!-- 日記・画像 -->
    <section class="diary-section">
        <div class="diary-block diary-filled">

            <h3 class="diary-subtitle">日記</h3>
            <div class="diary-text-box">
                <p class="diary-text">${diary.content}</p>
            </div>

            <h3 class="diary-subtitle">画像</h3>
            <div class="diary-image-box">
                <div class="diary-image-list" id="diary-image-list">
                    <c:if test="${not empty diary.photo}">
                        <img src="diary-image?user_id=${diary.userId}&date=${dateStr}&mode=true" style="max-width:100px; max-height:100px;">
                    </c:if>
                </div>

                <c:if test="${isOwner}">
                    <!-- モーダル起動ボタン -->
                    <button type="button" class="open-modal-btn" onclick="openDiaryModal()">＋</button>
                </c:if>
            </div>

        </div>
    </section>

    <c:if test="${isOwner}">
        <!-- 編集モーダル -->
        <div id="edit-modal" class="edit-modal">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/calendar-day-share" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="save_diary">
                    <input type="hidden" name="date" value="${dateStr}">
                    <input type="hidden" name="target_user_id" value="${targetUserId}">

                    <h3 class="modal-title">日記編集</h3>

                    <!-- 日記欄 -->
                    <div class="modal-block">
                        <h4 class="modal-subtitle">日記</h4>
                        <textarea name="content" id="modal-diary-text" placeholder="日記を入力してください">${diary.content}</textarea>
                    </div>

                    <!-- 画像欄 -->
                    <div class="modal-block">
                        <h4 class="modal-subtitle">画像</h4>
                        <input type="file" name="photo" accept="image/*">
                    </div>

                    <!-- ボタンエリア -->
                    <div class="modal-btn-area">
                        <button type="button" class="cancel-btn" onclick="closeDiaryModal()">キャンセル</button>
                        <button type="submit" class="save-btn">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </c:if>

</main> <!-- /.app-main -->

    <!-- ★ ボトムナビゲーション -->
    <div id="bottomnav"></div>

</div> <!-- /.app-container -->

<script>
let selectedKakeiboId = null;

function openRowModal(id, cat, yen) {
    selectedKakeiboId = id;
    document.getElementById("modal-category").textContent = "カテゴリ：" + cat;
    document.getElementById("modal-amount").textContent = "金額：" + yen + "円";
    document.getElementById("row-modal").style.display = "flex";
}

if (document.getElementById("row-edit")) {
    document.getElementById("row-edit").addEventListener("click", () => {
        if (selectedKakeiboId) {
            location.href = "${pageContext.request.contextPath}/input?id=" + selectedKakeiboId;
        }
    });

    document.getElementById("row-delete").addEventListener("click", () => {
        document.getElementById("row-modal").style.display = "none";
        document.getElementById("delete-modal").style.display = "flex";
    });

    document.getElementById("delete-no").addEventListener("click", () => {
        document.getElementById("delete-modal").style.display = "none";
    });

    document.getElementById("delete-yes").addEventListener("click", () => {
        if (selectedKakeiboId) {
            document.getElementById("delete_kakeibo_id").value = selectedKakeiboId;
            document.getElementById("deleteKakeiboForm").submit();
        }
    });
}

function openDiaryModal() {
    document.getElementById("edit-modal").style.display = "flex";
}

function closeDiaryModal() {
    document.getElementById("edit-modal").style.display = "none";
}

document.addEventListener("DOMContentLoaded", () => {
    const picker = document.getElementById("date-picker");
    const btn = document.getElementById("calendar-btn");
    if (btn && picker) {
        btn.addEventListener("click", () => {
            picker.showPicker ? picker.showPicker() : picker.click();
        });
        picker.addEventListener("change", (e) => {
            if (e.target.value) {
                location.href = "${pageContext.request.contextPath}/calendar-day-share?date=" + e.target.value + "&target_user_id=${targetUserId}";
            }
        });
    }
});
</script>

<script src="js/who.js"></script>

<!-- ★ フッター自動読み込み用JS -->
<script src="js/bottomnav-loader.js"></script>

</body>
</html>
