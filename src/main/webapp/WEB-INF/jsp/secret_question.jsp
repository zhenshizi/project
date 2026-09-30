<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログインページ(秘密の質問) | ねこぜ家計簿</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="css/共通.css">
    <link rel="stylesheet" href="css/sc_style.css">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- フォント -->
    <link href="https://fonts.googleapis.com/css2?family=Kosugi+Maru&display=swap" rel="stylesheet">
  
</head>
<body>
    <div class="app-container">

        <header>
            <h1 class="page-title">ログインページ</h1>
        </header>

        <main>
            <div class="main-illustration">
                <img src="img/3cat.png" alt="猫のイラスト">
            </div>

            <c:if test="${not empty errorMessage}">
                <div style="color: red; text-align: center; margin-bottom: 10px; font-weight: bold;">
                    ${errorMessage}
                </div>
            </c:if>

            <form class="secret-form" action="${pageContext.request.contextPath}/secret-question" method="post">
                <div class="input-group">
                    <i class="far fa-user input-icon"></i>
                    <input type="email" name="email" id="login-email" inputmode="email" placeholder="abcd@example.com" value="${email}" class="custom-input" required>
                </div>

                <div class="question-card">
                    <label class="card-label">秘密の質問：</label>
                    <div class="select-wrapper">
                        <i class="far fa-question-circle input-icon"></i>
                        <select name="secret_question" required>
                            <option value="" disabled <c:if test="${empty secretQuestion}">selected</c:if>>質問を選択してください</option>
                            <option value="1" <c:if test="${secretQuestion == '1'}">selected</c:if>>母親の旧姓は？</option>
                            <option value="2" <c:if test="${secretQuestion == '2'}">selected</c:if>>父親の旧姓は？</option>
                            <option value="3" <c:if test="${secretQuestion == '3'}">selected</c:if>>生まれた都道府県は？</option>
                        </select>
                    </div>
                </div>

                <div class="question-card">
                    <label class="card-label">こたえ：</label>
                    <div class="input-wrapper">
                        <input type="text" name="secret_answer" placeholder="こたえを入力してください" required>
                    </div>
                </div>

                <button type="submit" class="btn-login">ログイン</button>
            </form>

            <div class="info-text">
                <p>ログイン後、設定から<br>パスワードを再設定してください</p>
            </div>

            <div class="team-logo">
                <img src="img/teamlogo.png" alt="ねこぜ〜ず">
            </div>
        </main>

    </div> <!-- /.app-container -->

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
    });
    </script>
</body>
</html>
