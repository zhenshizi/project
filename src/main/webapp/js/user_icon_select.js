function openIconModal() {
  document.getElementById("iconModal").style.display = "flex";
}

function closeIconModal() {
  document.getElementById("iconModal").style.display = "none";
}

function selectIcon(src) {
  document.getElementById("selectedUserIcon").src = src;
  closeIconModal();
}


// 登録ボタン押下時の処理
document.querySelector(".btn-submit").addEventListener("click", () => {
  document.getElementById("registerPopup").style.display = "flex";
});

// ポップアップ閉じる処理
function closeRegisterPopup() {
  document.getElementById("registerPopup").style.display = "none";
}
