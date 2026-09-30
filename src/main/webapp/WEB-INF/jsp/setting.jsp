<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>設定 | ねこぜ家計簿</title>

  <!-- CSS -->
  <link rel="stylesheet" href="css/共通.css">
  <link rel="stylesheet" href="css/st_style.css">
  
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <!-- フォント -->
  <link href="https://fonts.googleapis.com/css2?family=Kosugi+Maru&display=swap" rel="stylesheet">
  
<script>
    window.USER_IS_SHARE = ${currentUser.isShare ? true : false};
    window.USER_HAS_GROUP = ${not empty currentUser.groupId ? true : false};
</script>
</head>

<body>
    <div class="app-container">
        
        <header class="app-header">
            <h1 class="page-title">設定 / マイページ</h1>
        </header>

        <main class="app-main">
            
            <section class="user-profile-section">
                <div class="user-avatar-wrapper">
                  <c:choose>
                    <c:when test="${not empty user.userIcon}">
                      <img src="user-icon-image?user_id=${user.userId}" alt="ユーザーアイコン" class="user-avatar">
                    </c:when>
                    <c:otherwise>
                      <img src="img/user_icon/yellow.png" alt="ユーザーアイコン" class="user-avatar">
                    </c:otherwise>
                  </c:choose>
                </div>
                <div class="user-email">
                    <span>${user.mailAddress}</span>
                </div>
            </section>
            
            <a href="${pageContext.request.contextPath}/user-edit" class="btn-profile-edit">
                <i class="far fa-user icon"></i> プロフィール編集
            </a>

            <section class="shared-id-card">
                <div class="shared-id-title">
                    所属している共有ID : <span class="shared-id-value">${not empty user.groupId ? user.groupId : '未登録'}</span>
                </div>
                <p class="shared-id-desc">
                    共有IDとID作成者のメールアドレスを共有すると<br>日記や家計簿を共有できます
                </p>
            </section>

            <!-- 共有IDを作成エリア -->
            <form action="${pageContext.request.contextPath}/setting" method="post">
                <input type="hidden" name="action" value="create_group">
                <section class="form-card">
                    <div class="card-header-row">
                        <span class="card-label">共有IDを作成</span>
                        <span class="card-sub-label">例：12345678</span>
                    </div>
                    <div class="input-group">
                        <input type="tel" name="group_id" id="create-id" inputmode="numeric" pattern="[0-9]*" maxlength="8" placeholder="8ケタの数字を入力してください" class="custom-input">
                    </div>
                    <c:if test="${not empty err_create_group}">
                        <div style="color:red; font-size:12px; margin-top:5px;">${err_create_group}</div>
                    </c:if>
                    <div class="btn-wrapper">
                        <button type="submit" class="btn-primary">とうろく</button>
                    </div>
                </section>
            </form>

            <!-- 共有ID認証エリア -->
            <form action="${pageContext.request.contextPath}/setting" method="post">
                <input type="hidden" name="action" value="auth_group">
                <section class="form-card">
                    <div class="card-header-row">
                        <span class="card-label">共有IDを入力</span>
                        <span class="card-sub-label">例：12345678</span>
                    </div>
                    <div class="input-group">
                       <input type="tel" name="auth_group_id" id="input-id" inputmode="numeric" pattern="[0-9]*" maxlength="8" placeholder="8ケタの数字を入力してください" class="custom-input">
                    </div>
                    <c:if test="${not empty err_auth_group}">
                        <div style="color:red; font-size:12px; margin-top:5px;">${err_auth_group}</div>
                    </c:if>

                    <div class="card-header-row margin-top">
                        <span class="card-label">共有IDを作成した人のメールアドレスを入力</span>
                    </div>
                    <div class="input-group">
                        <input type="email" name="creator_email" id="input-email" inputmode="email" placeholder="abcd@example.com" class="custom-input">
                    </div>
                    <c:if test="${not empty err_auth_email}">
                        <div style="color:red; font-size:12px; margin-top:5px;">${err_auth_email}</div>
                    </c:if>

                    <div class="btn-wrapper">
                        <button type="submit" class="btn-primary">とうろく</button>
                    </div>
                </section>
            </form>

            <div class="logout-wrapper">
               <button type="button" onclick="confirmLogout()" class="btn-logout" style="border:none; cursor:pointer;">
                   <i class="fas fa-info-circle icon"></i> ログアウト
               </button>
            </div>

            <!-- ▼ ログアウト確認ダイアログ -->
            <form id="logoutForm" action="${pageContext.request.contextPath}/setting" method="post" style="display:none;">
                <input type="hidden" name="action" value="logout">
            </form>

            <!-- ▼ ユーザー情報変更後ポップアップ / 登録完了ポップアップ -->
            <div id="generalPopup" class="popup" <c:if test="${param.showPopup == 'true' || showAuthPopup}">style="display:flex;"</c:if>>
              <div class="popup-content">
                <h2>登録したにゃ！</h2>
                <button class="close-popup-btn" onclick="closeGeneralPopup()">とじる</button>
              </div>
            </div>

            <!-- ▼ 共有ID作成完了ポップアップ -->
            <div id="sharedIdPopup" class="popup" <c:if test="${showGroupPopup}">style="display:flex;"</c:if>>
              <div class="popup-content">
                <h2>登録したにゃ！</h2>
                <p class="popup-email">メールアドレス：<span id="popupEmail">${popupEmail}</span></p>
                <p class="popup-id">共有ID：<span id="popupSharedId">${popupGroupId}</span></p>
                <button class="close-popup-btn" onclick="closeSharedIdPopup()">とじる</button>
              </div>
            </div>

        </main>

        <!-- ★ フッター挿入用のコンテナ -->
        <div id="bottomnav"></div>

    </div> <!-- /.app-container -->

    <script>
    function confirmLogout() {
        if (confirm("ログアウトしますか？")) {
            document.getElementById("logoutForm").submit();
        }
    }

    function closeGeneralPopup() {
        document.getElementById("generalPopup").style.display = "none";
    }

    function closeSharedIdPopup() {
        document.getElementById("sharedIdPopup").style.display = "none";
    }

    document.addEventListener('DOMContentLoaded', () => {
        const forceHalfWidthDigits = (input) => {
            input.addEventListener('input', (e) => {
                let value = e.target.value;
                value = value.replace(/[０-９]/g, (s) => String.fromCharCode(s.charCodeAt(0) - 0xFEE0));
                value = value.replace(/[^0-9]/g, '');
                e.target.value = value;
            });
        };

        const forceHalfWidthEmail = (input) => {
            input.addEventListener('input', (e) => {
                let value = e.target.value;
                value = value.replace(/[！-～]/g, (s) => String.fromCharCode(s.charCodeAt(0) - 0xFEE0));
                value = value.replace(/ /g, ' ');
                value = value.replace(/[^a-zA-Z0-9@._\-+]/g, '');
                e.target.value = value;
            });
        };

        const createIdInput = document.getElementById('create-id');
        const inputIdInput = document.getElementById('input-id');
        const emailInput = document.getElementById('input-email');

        if (createIdInput) forceHalfWidthDigits(createIdInput);
        if (inputIdInput) forceHalfWidthDigits(inputIdInput);
        if (emailInput) forceHalfWidthEmail(emailInput);
    });
    </script>

    <!-- ★ フッター自動読み込み用JS -->
    <script src="js/bottomnav-loader.js"></script>

</body>
</html>
