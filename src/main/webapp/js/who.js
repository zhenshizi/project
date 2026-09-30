

document.addEventListener("DOMContentLoaded", () => {
  const whoBtn = document.querySelector(".who-btn");
  const menu = document.querySelector(".who-menu");

  // ボタンを押したら開閉
  whoBtn.addEventListener("click", (e) => {
    e.stopPropagation(); // 他のクリックイベントに干渉しない
    menu.style.display = (menu.style.display === "block") ? "none" : "block";
  });

  // メニュー外クリックで閉じる
  document.addEventListener("click", (e) => {
    if (!e.target.closest(".who-dropdown")) {
      menu.style.display = "none";
    }
  });
});
