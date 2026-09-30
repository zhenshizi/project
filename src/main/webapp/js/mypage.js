document.addEventListener("DOMContentLoaded", () => {
  const createBtn = document.querySelectorAll(".form-card .btn-primary")[0]; // 上のとうろく
  const inputBtn  = document.querySelectorAll(".form-card .btn-primary")[1]; // 下のとうろく
  const sharedIdDisplay = document.querySelector(".shared-id-value");

  // ▼ 上のとうろく（共有ID作成）
  createBtn.addEventListener("click", (e) => {
    e.stopPropagation(); // 下のイベントが反応しないようにする
    const sharedId = document.getElementById("create-id").value;
    const email = document.querySelector(".user-email span").textContent;

    document.getElementById("popupEmail").textContent = email;
    document.getElementById("popupSharedId").textContent = sharedId;

    if (sharedId) sharedIdDisplay.textContent = sharedId;

    // 上のポップアップだけ表示
    document.getElementById("sharedIdPopup").style.display = "flex";
  });

  // ▼ 下のとうろく（共有ID入力）
  inputBtn.addEventListener("click", (e) => {
    e.stopPropagation(); // 上のイベントが反応しないようにする
    document.getElementById("bottomRegisterPopup").style.display = "flex";
  });
});

// ▼ ポップアップ閉じる処理
function closeSharedIdPopup() {
  document.getElementById("sharedIdPopup").style.display = "none";
}

function closeBottomRegisterPopup() {
  document.getElementById("bottomRegisterPopup").style.display = "none";
}
