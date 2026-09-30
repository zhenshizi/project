document.addEventListener("DOMContentLoaded", () => {
    const footerContainer = document.getElementById("bottomnav");
    if (!footerContainer) return;

    fetch("components/bottom_nav.jsp")
        .then(response => {
            if (!response.ok) throw new Error("フッターの読み込みに失敗しました");
            return response.text();
        })
        .then(data => {
            footerContainer.innerHTML = data;

			
			
			
            const currentPage = window.location.pathname.split("/").pop() || "index.html";

            // 共有メニューの表示制御 (R94仕様)
            // group_idが存在する、またはisShareがtrueの場合のみ(共有)メニューを表示
            const isShare = (typeof window.USER_IS_SHARE !== "undefined" && window.USER_IS_SHARE) ||
                            (typeof window.USER_HAS_GROUP !== "undefined" && window.USER_HAS_GROUP);
            if (!isShare) {
                const sharedItems = footerContainer.querySelectorAll(".shared-menu-item");
                sharedItems.forEach(item => {
                    item.style.display = "none";
                });
            }

            // 現在表示しているメニュー名を太字表示 (R93仕様)
            const dropupItems = footerContainer.querySelectorAll(".dropup-item");
            dropupItems.forEach(item => {
                const href = item.getAttribute("href");
                if (href && (currentPage === href || currentPage.startsWith(href))) {
                    item.style.fontWeight = "bold";
                }
            });

            // アクティブ状態の設定
            const navItems = footerContainer.querySelectorAll(".nav-item");
            navItems.forEach(item => {
                if (item.getAttribute("data-page") === currentPage) {
                    item.classList.add("active");
                }
            });

            // ドロップアップメニューの開閉処理
            const dropdowns = footerContainer.querySelectorAll(".dropdown-wrapper");
            dropdowns.forEach(dropdown => {
                const btn = dropdown.querySelector(".nav-btn-link");
                if (btn) {
                    btn.addEventListener("click", (e) => {
                        e.stopPropagation();
                        dropdowns.forEach(d => {
                            if (d !== dropdown) d.classList.remove("active");
                        });
                        dropdown.classList.toggle("active");
                    });
                }
            });

            // メニュー外をクリックした時に閉じる
            document.addEventListener("click", () => {
                dropdowns.forEach(d => d.classList.remove("active"));
            });
        })
        .catch(error => console.error(error));
});
