/**
 * パスワードの「目のアイコン」押下中表示/非表示切り替え処理
 */
document.addEventListener('DOMContentLoaded', () => {
    const passwordInput = document.getElementById('login-password');
    const togglePassword = document.getElementById('togglePassword');

    if (passwordInput && togglePassword) {
        // パスワードを表示（平文）にする処理
        const showPassword = () => {
            passwordInput.type = 'text';
            togglePassword.classList.remove('fa-eye');
            togglePassword.classList.add('fa-eye-slash');
        };

        // パスワードを非表示（伏字）にする処理
        const hidePassword = () => {
            passwordInput.type = 'password';
            togglePassword.classList.remove('fa-eye-slash');
            togglePassword.classList.add('fa-eye');
        };

        // --- イベントリスナー設定 ---

        // PC（マウス操作）
        togglePassword.addEventListener('mousedown', showPassword);
        togglePassword.addEventListener('mouseup', hidePassword);
        togglePassword.addEventListener('mouseleave', hidePassword); // 押したままアイコン外へ外れた時

        // スマホ・タブレット（タッチ操作）
        togglePassword.addEventListener('touchstart', (e) => {
            e.preventDefault(); // タッチ時のデフォルト動作（拡大やスクロール等）を防止
            showPassword();
        });
        togglePassword.addEventListener('touchend', hidePassword);
    }
});