<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>ユーザ新規登録 | ねこぜ家計簿</title>

  <!-- CSS -->
  <link rel="stylesheet" href="css/共通.css">
  <link rel="stylesheet" href="css/newstyle.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <!-- フォント -->
  <link href="https://fonts.googleapis.com/css2?family=Kosugi+Maru&display=swap" rel="stylesheet">
  
</head>

<body>
  <div class="app-container">

    <!-- ▼ ヘッダー -->
    <header class="register-header">
      <h1 class="register-title">ユーザ新規登録</h1>
    </header>

    <!-- ▼ メインエリア -->
    <main class="app-main register-main">

      <c:if test="${not empty err_icon}">
        <div style="color:red; margin-bottom: 5px; font-weight: bold;">${err_icon}</div>
      </c:if>

      <!-- ▼ ① ユーザーアイコン選択エリア -->
      <div class="avatar-selection-area">
        <div class="avatar-wrapper" onclick="openIconModal()">
          <img src="${not empty input_iconPath ? input_iconPath : 'img/user_icon/yellow.png'}" alt="ユーザーアイコン" id="selectedUserIcon" class="selected-avatar">
          <div class="avatar-plus-btn">
            <i class="fas fa-plus"></i>
          </div>
        </div>
      </div>

      <!-- ▼ ② 登録フォーム -->
      <form class="register-form" action="${pageContext.request.contextPath}/user-register" method="post" id="registerForm">
        <input type="hidden" name="selected_icon_path" id="selected_icon_path" value="${not empty input_iconPath ? input_iconPath : 'img/user_icon/yellow.png'}">

        <!-- ① 名前 -->
        <div class="form-group">
          <label class="form-label">
            <span class="label-number">①</span> 名前：
            <c:if test="${not empty err_name}"><span style="color:red; font-size:12px; margin-left:5px;">${err_name}</span></c:if>
          </label>
          <div class="input-inner">
            <i class="far fa-user input-icon"></i>
            <input type="text" name="name" placeholder="名前を入力してください" class="custom-input" value="${input_name}">
          </div>
        </div>

        <!-- ② メールアドレス -->
        <div class="form-group">
          <label class="form-label">
            <span class="label-number">②</span> メールアドレス：
            <c:if test="${not empty err_email}"><span style="color:red; font-size:12px; margin-left:5px;">${err_email}</span></c:if>
          </label>
          <div class="input-inner">
            <i class="far fa-envelope input-icon"></i>
           <input type="email" name="email" id="login-email" inputmode="email" placeholder="abcd@example.com" class="custom-input" value="${input_email}">
          </div>
        </div>

        <!-- ③ パスワード -->
		<div class="form-group">
		  <label class="form-label">
		    <span class="label-number">③</span> パスワード：
		    <c:if test="${not empty err_password}"><span style="color:red; font-size:12px; margin-left:5px;">${err_password}</span></c:if>
		  </label>
		  <div class="input-inner">
		    <i class="fas fa-lock input-icon"></i>
		    <input type="password" name="password" id="login-password" maxlength="8" placeholder="英数字8桁で入力してください" class="custom-input">
		    <!-- ★ 目のアイコンを追加 -->
		    <i class="far fa-eye toggle-password" id="togglePassword"></i>
		  </div>
		</div>

        <!-- ④ 秘密の質問 -->
        <div class="form-group">
          <label class="form-label">
            <span class="label-number">④</span> 秘密の質問：
            <c:if test="${not empty err_secretQuestion}"><span style="color:red; font-size:12px; margin-left:5px;">${err_secretQuestion}</span></c:if>
          </label>

          <div class="input-inner select-inner">
            <i class="far fa-question-circle input-icon"></i>
            <select name="secret_question" class="custom-select">
              <option value="" disabled <c:if test="${empty input_secretQuestion}">selected</c:if>>質問を選択してください</option>
              <option value="1" <c:if test="${input_secretQuestion == '1'}">selected</c:if>>母親の旧姓は？</option>
              <option value="2" <c:if test="${input_secretQuestion == '2'}">selected</c:if>>父親の旧姓は？</option>
              <option value="3" <c:if test="${input_secretQuestion == '3'}">selected</c:if>>出身都道府県は？</option>
            </select>
            <i class="fas fa-chevron-down select-arrow"></i>
          </div>

          <div class="sub-label">こたえ：<c:if test="${not empty err_secretAnswer}"><span style="color:red; font-size:12px; margin-left:5px;">${err_secretAnswer}</span></c:if></div>
          <div class="input-inner no-icon-padding">
            <input type="text" name="secret_answer" placeholder="こたえを入力してください" class="custom-input" value="${input_secretAnswer}">
          </div>
        </div>

        <!-- ⑤ ニックネーム -->
        <div class="form-group">
          <label class="form-label">
            <span class="label-number">⑤</span> ニックネーム：
            <c:if test="${not empty err_nickname}"><span style="color:red; font-size:12px; margin-left:5px;">${err_nickname}</span></c:if>
          </label>
          <div class="input-inner">
            <i class="far fa-smile input-icon"></i>
            <input type="text" name="nickname" placeholder="ニックネームを入力してください" class="custom-input" value="${input_nickname}">
          </div>
        </div>

        <!-- 登録ボタン -->
        <div class="submit-btn-wrapper">
          <button type="submit" class="btn-submit">登録する</button>
        </div>

      </form>
      <!-- ▼ 登録完了ポップアップ -->
      <div id="registerPopup" class="popup" <c:if test="${showPopup}">style="display:flex;"</c:if>>
        <div class="popup-content">
          
          <h2>登録したにゃ！</h2>
      
       <button class="close-popup-btn" onclick="closeRegisterPopup()">とじる</button>
  </div>
</div>
     
		 <!-- ▼ アイコン選択モーダル -->
      <div id="iconModal" class="icon-modal">
        <div class="icon-modal-content">
          <h2>アイコンを選択</h2>

          <div class="icon-list">
            <img src="img/user_icon/black.png" onclick="selectIcon('img/user_icon/black.png')">
            <img src="img/user_icon/blue.png" onclick="selectIcon('img/user_icon/blue.png')">
            <img src="img/user_icon/green.png" onclick="selectIcon('img/user_icon/green.png')">
            <img src="img/user_icon/pink.png" onclick="selectIcon('img/user_icon/pink.png')">
            <img src="img/user_icon/purple.png" onclick="selectIcon('img/user_icon/purple.png')">
            <img src="img/user_icon/red.png" onclick="selectIcon('img/user_icon/red.png')">
            <img src="img/user_icon/skyblue.png" onclick="selectIcon('img/user_icon/skyblue.png')">
            <img src="img/user_icon/yellow.png" onclick="selectIcon('img/user_icon/yellow.png')">
          </div>

          <button class="close-modal-btn" onclick="closeIconModal()">閉じる</button>
        </div>
      </div>

    </main>
  </div>
    

  <!-- ▼ JavaScript -->
  <script>
    function openIconModal() {
      document.getElementById("iconModal").style.display = "flex";
    }

    function closeIconModal() {
      document.getElementById("iconModal").style.display = "none";
    }

    function selectIcon(src) {
      document.getElementById("selectedUserIcon").src = src;
      document.getElementById("selected_icon_path").value = src;
      closeIconModal();
    }
  </script>

  <script>
  document.addEventListener('DOMContentLoaded', () => {
      // メールアドレス用：全角→半角変換 ＆ 不要文字の自動排除
      const emailInput = document.getElementById('login-email');
      if (emailInput) {
          emailInput.addEventListener('input', (e) => {
              let val = e.target.value;
              val = val.replace(/[！-～]/g, (s) => String.fromCharCode(s.charCodeAt(0) - 0xFEE0));
              val = val.replace(/ /g, ' ');
              val = val.replace(/[^a-zA-Z0-9@._\-+]/g, '');
              e.target.value = val;
          });
      }

      // パスワード用：全角→半角変換 ＆ 半角英数字のみ・最大8桁制限
      const passwordInput = document.getElementById('login-password');
      if (passwordInput) {
          passwordInput.addEventListener('input', (e) => {
              let val = e.target.value;
              val = val.replace(/[！-～]/g, (s) => String.fromCharCode(s.charCodeAt(0) - 0xFEE0));
              val = val.replace(/[^a-zA-Z0-9]/g, '');
              if (val.length > 8) {
                  val = val.slice(0, 8);
              }
              e.target.value = val;
          });
      }
  });
  </script>
<script>
function closeRegisterPopup() {
  document.getElementById("registerPopup").style.display = "none";
  window.location.href = "${pageContext.request.contextPath}/login";
}
</script>

<!-- ★ パスワード表示切替用JSの読み込み -->
  <script src="js/password.js"></script>

</body>
</html>
