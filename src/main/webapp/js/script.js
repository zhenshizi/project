document.addEventListener("DOMContentLoaded", () => {

    /* -------------------------
       日付表示
    ------------------------- */
    let currentDate = new Date();
    const dateDisplay = document.getElementById("date-display");
    const datePicker = document.getElementById("date-picker");
    const calendarBtn = document.getElementById("calendar-btn");

    function updateDateDisplay() {
        const y = currentDate.getFullYear();
        const m = currentDate.getMonth() + 1;
        const d = currentDate.getDate();
        const weekNames = ["日","月","火","水","木","金","土"];
        const w = weekNames[currentDate.getDay()];
        dateDisplay.textContent = `${y} 年 ${m} 月 ${d} 日 (${w})`;
        datePicker.value = `${y}-${String(m).padStart(2,"0")}-${String(d).padStart(2,"0")}`;
    }
    updateDateDisplay();

    calendarBtn.addEventListener("click", () => {
        datePicker.style.display = "block";
        datePicker.focus();
        if (typeof datePicker.showPicker === "function") {
            datePicker.showPicker();
        }
    });

    datePicker.addEventListener("change", () => {
        const selected = new Date(datePicker.value);
        if (!isNaN(selected)) {
            currentDate = selected;
            updateDateDisplay();
        }
        datePicker.style.display = "none";
    });

    document.getElementById("prev-day").addEventListener("click", () => {
        currentDate.setDate(currentDate.getDate() - 1);
        updateDateDisplay();
    });
    document.getElementById("next-day").addEventListener("click", () => {
        currentDate.setDate(currentDate.getDate() + 1);
        updateDateDisplay();
    });

    document.addEventListener("click", (e) => {
        const isCalendarBtn = e.target.closest("#calendar-btn");
        const isDatePicker = e.target.closest("#date-picker");
        if (isCalendarBtn || isDatePicker) return;
        datePicker.style.display = "none";
    });


    /* -------------------------
       編集モーダル
    ------------------------- */
    const editBtn = document.querySelector(".open-modal-btn");
    const modal = document.getElementById("edit-modal");
    const cancelBtn = document.querySelector(".cancel-btn");
    const saveBtn = document.querySelector(".save-btn");
    const diaryText = document.querySelector(".diary-text");
    const modalDiaryText = document.getElementById("modal-diary-text");

    editBtn.addEventListener("click", () => {
        modal.style.display = "flex";
        modalDiaryText.value = diaryText.textContent.trim();
    });

    cancelBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });


    /* -------------------------
       拡大モーダル（1回だけ作る）
    ------------------------- */
    const zoomModal = document.createElement("div");
    zoomModal.classList.add("image-modal");
    const zoomImg = document.createElement("img");
    zoomModal.appendChild(zoomImg);
    document.body.appendChild(zoomModal);

    zoomModal.addEventListener("click", () => {
        zoomModal.style.display = "none";
    });

    function attachZoomEvent(img) {
        img.addEventListener("click", () => {
            zoomImg.src = img.src;
            zoomModal.style.display = "flex";
        });
    }


    /* -------------------------
       保存処理
    ------------------------- */
    saveBtn.addEventListener("click", () => {

        /* 日記反映 */
        const textValue = modalDiaryText.value.trim();
        diaryText.textContent = textValue === "" ? "" : textValue;

        /* 画像反映 */
        const diaryImageList = document.getElementById("diary-image-list");
        diaryImageList.innerHTML = "";

        const modalImages = document.querySelectorAll("#modal-image-list .modal-image-item img");

        modalImages.forEach(img => {
            const item = document.createElement("div");
            item.classList.add("diary-image-item");

            const newImg = document.createElement("img");
            newImg.src = img.src;

            item.appendChild(newImg);
            diaryImageList.appendChild(item);

            attachZoomEvent(newImg);  // ★保存後の画像にも拡大イベント付与
        });

        /* 画像がないときは画像リストだけ非表示 */
        const diaryImageBox = document.querySelector(".diary-image-box");
        if (diaryImageList.children.length === 0) {
            diaryImageList.style.display = "none";
        } else {
            diaryImageList.style.display = "flex";
        }

        modal.style.display = "none";
    });


    /* -------------------------
       画像追加・削除機能
    ------------------------- */
    const modalImageList = document.getElementById("modal-image-list");
    const modalImageAddBtn = document.querySelector("#edit-modal .image-add-btn");
    const modalImageInput = document.getElementById("modal-image-input");

    function attachRemoveEvent(button) {
        button.addEventListener("click", (e) => {
            e.target.closest(".modal-image-item").remove();
        });
    }

    document.querySelectorAll(".image-remove-btn").forEach(attachRemoveEvent);

    function handleImageUpload(files) {
        Array.from(files).forEach((file) => {
            const reader = new FileReader();
            reader.onload = (ev) => {
                const item = document.createElement("div");
                item.classList.add("modal-image-item");

                const img = document.createElement("img");
                img.src = ev.target.result;

                const removeBtn = document.createElement("button");
                removeBtn.classList.add("image-remove-btn");
                removeBtn.textContent = "✕";
                attachRemoveEvent(removeBtn);

                item.appendChild(img);
                item.appendChild(removeBtn);
                modalImageList.appendChild(item);
            };
            reader.readAsDataURL(file);
        });
    }

    modalImageAddBtn.addEventListener("click", () => modalImageInput.click());
    modalImageInput.addEventListener("change", (e) => {
        handleImageUpload(e.target.files);
        modalImageInput.value = "";
    });

});
let selectedRow = null;

// カテゴリー行クリックでモーダル表示
document.querySelectorAll(".expense-table tbody tr").forEach(row => {
  row.addEventListener("click", () => {

    selectedRow = row; // 行を記録

    const category = row.querySelector(".cat-label").textContent;
    const amount = row.querySelector(".amount-cell").textContent;

    document.getElementById("modal-category").textContent = `カテゴリ：${category}`;
    document.getElementById("modal-amount").textContent = `金額：${amount}`;

    const rowModal = document.getElementById("row-modal");
    rowModal.style.display = "flex";

    // 編集ボタン
    document.getElementById("row-edit").onclick = (e) => {
      e.stopPropagation(); // ★行クリックイベントを止める
      rowModal.style.display = "none";
      window.location.href = "input.html";
    };

    // 削除ボタン
    document.getElementById("row-delete").onclick = (e) => {
      e.stopPropagation(); // ★行クリックイベントを止める
      rowModal.style.display = "none";
      document.getElementById("delete-modal").style.display = "flex"; // ★即表示される
    };
  });
});

// 削除確認モーダル
document.getElementById("delete-yes").addEventListener("click", () => {
  document.getElementById("delete-modal").style.display = "none";

  if (selectedRow) {
    selectedRow.remove();
    selectedRow = null;
  }
});

document.getElementById("delete-no").addEventListener("click", () => {
  document.getElementById("delete-modal").style.display = "none";
});
const whoBtn = document.querySelector('.who-btn');
const whoMenu = document.querySelector('.who-menu');

whoBtn.addEventListener('click', () => {
    whoMenu.classList.toggle('show');
});


