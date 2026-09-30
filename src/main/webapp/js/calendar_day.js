let currentDate = new Date();

const dateDisplay = document.getElementById("date-display");
const datePicker = document.getElementById("date-picker");
const calendarBtn = document.getElementById("calendar-btn");

function updateDateDisplay() {
  const year = currentDate.getFullYear();
  const month = currentDate.getMonth() + 1;
  const day = currentDate.getDate();
  const weekNames = ["日", "月", "火", "水", "木", "金", "土"];
  const week = weekNames[currentDate.getDay()];

  dateDisplay.textContent = `${year} 年 ${month} 月 ${day} 日 (${week})`;
  datePicker.value = `${year}-${String(month).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
}

updateDateDisplay();

// ▼ カレンダーアイコンを押すとカレンダーを開く（これだけでOK）
calendarBtn.addEventListener("click", () => {
  datePicker.style.display = "block";
  datePicker.focus();
  if (typeof datePicker.showPicker === "function") {
    datePicker.showPicker();
  }
});

// ▼ 日付選択時に反映して閉じる
datePicker.addEventListener("change", () => {
  const selected = new Date(datePicker.value);
  if (!isNaN(selected)) {
    currentDate = selected;
    updateDateDisplay();
  }
  datePicker.style.display = "none";
});

// ▼ 前日・翌日ボタン
document.getElementById("prev-day").addEventListener("click", () => {
  currentDate.setDate(currentDate.getDate() - 1);
  updateDateDisplay();
});

document.getElementById("next-day").addEventListener("click", () => {
  currentDate.setDate(currentDate.getDate() + 1);
  updateDateDisplay();
});



// ▼ 画像追加ボタン
const imageAddBtn = document.getElementById("image-add-btn");
const imageFileInput = document.getElementById("image-file-input");
const diaryImage = document.getElementById("diary-image");

// ボタンを押したらファイル選択を開く
imageAddBtn.addEventListener("click", () => {
    imageFileInput.click();
});

// ファイル選択後に画像をプレビュー表示
imageFileInput.addEventListener("change", () => {
    const file = imageFileInput.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (e) => {
        diaryImage.src = e.target.result;  // 選んだ画像を表示
    };
    reader.readAsDataURL(file);
});

// ▼ 複数画像追加
const imageAddBtn = document.getElementById("image-add-btn");
const imageFileInput = document.getElementById("image-file-input");
const diaryImageList = document.getElementById("diary-image-list");

// ボタンを押したらファイル選択を開く
imageAddBtn.addEventListener("click", () => {
    imageFileInput.click();
});

// ファイル選択後に複数画像を追加
imageFileInput.addEventListener("change", () => {
    const files = Array.from(imageFileInput.files);
    if (files.length === 0) return;

    files.forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
            // ▼ 画像枠を作成
            const item = document.createElement("div");
            item.classList.add("diary-image-item");

            // ▼ 画像タグを作成
            const img = document.createElement("img");
            img.src = e.target.result;

            // ▼ 追加
            item.appendChild(img);
            diaryImageList.appendChild(item);
        };
        reader.readAsDataURL(file);
    });

    // 選択状態をリセット（同じ画像を再度選べるように）
    imageFileInput.value = "";
});

// 画像削除ボタンのクリック処理
document.addEventListener("click", (event) => {
  if (event.target.classList.contains("image-delete-btn")) {
    const imageItem = event.target.closest(".diary-image-item");
    if (imageItem) {
      imageItem.remove(); // 該当画像を削除
    }
  }
});
// モーダル要素を作成
const modal = document.createElement("div");
modal.classList.add("image-modal");
document.body.appendChild(modal);

// モーダル内の画像要素
const modalImg = document.createElement("img");
modal.appendChild(modalImg);

// 画像クリックで拡大表示
document.querySelectorAll(".diary-image").forEach(img => {
  img.addEventListener("click", () => {
    modalImg.src = img.src;
    modal.style.display = "flex";
  });
});

// モーダルクリックで閉じる
modal.addEventListener("click", () => {
  modal.style.display = "none";
});

const editBtn = document.querySelector(".edit-btn");
const modal = document.getElementById("edit-modal");
const cancelBtn = document.querySelector(".cancel-btn");

// ＋ボタンでモーダル表示
editBtn.addEventListener("click", () => {
  modal.style.display = "flex";
});

// キャンセルで閉じる
cancelBtn.addEventListener("click", () => {
  modal.style.display = "none";
});


