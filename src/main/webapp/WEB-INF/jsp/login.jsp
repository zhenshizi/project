<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログインページ | ねこぜ家計簿</title>
    
 <link rel="stylesheet" href="css/共通.css">
<link rel="stylesheet" href="css/lg_style.css">
     
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
 <!-- フォント-->
<link href="https://fonts.googleapis.com/css2?family=Kosugi+Maru&display=swap'" rel="stylesheet">
</head>

<body>
    <div class="app-container">
    <div class="container">
    
        <header>
            <h1 class="page-title">ログインページ</h1>
        </header>

        <main>
            <div class="main-illustration">
                <img src="img/3cat.png" alt="猫のイラスト">
            </div>

            <div class="app-branding">
                <h2 class="app-title">家計簿アプリ</h2>
                <p class="app-copy">毎日を、もっと上手に。</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div style="color: red; text-align: center; margin-bottom: 10px; font-weight: bold;">
                    ${errorMessage}
                </div>
            </c:if>

            <form class="login-form" action="${pageContext.request.contextPath}/login" method="post">
                <div class="input-group">
                    <i class="far fa-user input-icon"></i>
                    <input type="email" name="email" id="login-email" inputmode="email" placeholder="abcd@example.com" value="${email}" class="custom-input" required>
                </div>

                <div class="form-group">
                
         <div class="input-group">
    <i class="fas fa-lock input-icon"></i>
    <input type="password" name="password" id="login-password" maxlength="8" placeholder="英数字8ケタを入力してください" required>
	</div>

                </div>

                <button type="submit" class="btn-login">ログイン</button>
            </form>

            <div class="link-wrapper">
                <a href="${pageContext.request.contextPath}/secret-question" class="sub-link">パスワードを忘れた方はこちら</a>

            </div>

            <div class="register-section">
                <p class="register-text">アカウントをお持ちでない方はこちら</p>
                <a href="${pageContext.request.contextPath}/user-register" class="btn-register">新規登録</a>

            </div>

            <div class="team-logo">
                <img src="img/teamlogo.png" alt="ねこぜ〜ず">
            </div>
        </main>
    </div>
    </div>
    <script>
document.addEventListener('DOMContentLoaded', () => {
    // メールアドレス用：全角→半角変換 ＆ 不要文字の自動排除
    const emailInput = document.getElementById('login-email');
    if (emailInput) {
        emailInput.addEventListener('input', (e) => {
            let val = e.target.value;
            // 全角英数・記号を半角に変換
            val = val.replace(/[！-～]/g, (s) => String.fromCharCode(s.charCodeAt(0) - 0xFEE0));
            // 全角スペースを半角に変換
            val = val.replace(/ /g, ' ');
            // メールアドレスに使用できない文字（日本語など）を除去
            val = val.replace(/[^a-zA-Z0-9@._\-+]/g, '');
            e.target.value = val;
        });
    }

    // パスワード用：全角→半角変換 ＆ 半角英数字のみ・最大8桁制限
    const passwordInput = document.getElementById('login-password');
    if (passwordInput) {
        passwordInput.addEventListener('input', (e) => {
            let val = e.target.value;
            // 全角英数を半角に変換
            val = val.replace(/[！-～]/g, (s) => String.fromCharCode(s.charCodeAt(0) - 0xFEE0));
            // 半角英数字（a-z, A-Z, 0-9）以外の文字（記号や日本語）を自動除去
            val = val.replace(/[^a-zA-Z0-9]/g, '');
            // 8桁を超える場合は切り捨て
            if (val.length > 8) {
                val = val.slice(0, 8);
            }
            e.target.value = val;
        });
    }
});
</script>

</body>
</html>
