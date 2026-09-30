// ===============================
// モーダル制御共通関数
// ===============================
function openModal(modal) {
  modal.classList.remove("hidden");
}

function closeModal(modal) {
  modal.classList.add("hidden");
}

// ===============================
// DOM取得
// ===============================
const taskList = document.getElementById("task-list");
const addTaskBtn = document.getElementById("add-task-btn");

const taskModal = document.getElementById("task-modal");
const taskInput = document.getElementById("task-input");
const taskOk = document.getElementById("task-ok");
const taskCancel = document.getElementById("task-cancel");

const deleteModal = document.getElementById("delete-modal");
const deleteYes = document.getElementById("delete-yes");
const deleteNo = document.getElementById("delete-no");

const memoAddBtn = document.getElementById("memo-add-btn");
const memoModal = document.getElementById("memo-modal");
const memoInput = document.getElementById("memo-input");
const memoSave = document.getElementById("memo-save");
const memoCancel = document.getElementById("memo-cancel");
const memoDisplay = document.getElementById("memo-display");

// ===============================
// タスク管理用変数
// ===============================
let editingTask = null;
let deletingTask = null;

// ===============================
// メモ管理用変数
// ===============================
let memoList = [];
let deletingMemoIndex = null;

// ===============================
// タスク追加ボタン
// ===============================
addTaskBtn.addEventListener("click", () => {
  editingTask = null;
  taskInput.value = "";
  openModal(taskModal);
});

// ===============================
// タスクOKボタン（追加・編集）
// ===============================
taskOk.addEventListener("click", () => {
  const text = taskInput.value.trim();
  if (!text) return;

  if (editingTask) {
    // 編集
    editingTask.querySelector(".task-text").textContent = text;
  } else {
    // 新規追加
    const li = document.createElement("li");
    li.className = "task-item";

    const label = document.createElement("label");
    label.className = "checkbox-container";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";

    const checkmark = document.createElement("span");
    checkmark.className = "checkmark";

    const taskText = document.createElement("span");
    taskText.className = "task-text";
    taskText.textContent = text;

    label.appendChild(checkbox);
    label.appendChild(checkmark);
    label.appendChild(taskText);

    const deleteBtn = document.createElement("button");
    deleteBtn.className = "delete-btn hidden";
    deleteBtn.textContent = "削除";

    li.appendChild(label);
    li.appendChild(deleteBtn);

    // チェック時の削除ボタン表示
    checkbox.addEventListener("change", () => {
      if (checkbox.checked) {
        deleteBtn.classList.remove("hidden");
      } else {
        deleteBtn.classList.add("hidden");
      }
    });

    // 編集
    taskText.addEventListener("click", () => {
      editingTask = li;
      taskInput.value = taskText.textContent;
      openModal(taskModal);
    });

    // 削除
    deleteBtn.addEventListener("click", () => {
      deletingTask = li;
      openModal(deleteModal);
    });

    taskList.appendChild(li);
  }

  closeModal(taskModal);
});

// ===============================
// タスクキャンセル
// ===============================
taskCancel.addEventListener("click", () => {
  closeModal(taskModal);
});

// ===============================
// 削除確認：はい（タスク・メモ共通）
// ===============================
deleteYes.addEventListener("click", () => {
  if (deletingTask && deletingTask !== "memo") {
    deletingTask.remove();
  } else if (deletingTask === "memo") {
    memoList.splice(deletingMemoIndex, 1);
    renderMemoList();
  }

  deletingTask = null;
  deletingMemoIndex = null;
  closeModal(deleteModal);
});

// ===============================
// 削除確認：いいえ
// ===============================
deleteNo.addEventListener("click", () => {
  deletingTask = null;
  deletingMemoIndex = null;
  closeModal(deleteModal);
});

// ===============================
// メモ追加ボタン
// ===============================
memoAddBtn.addEventListener("click", () => {
  memoInput.value = "";
  deletingMemoIndex = null;
  openModal(memoModal);
});

// ===============================
// メモ保存
// ===============================
memoSave.addEventListener("click", () => {
  const text = memoInput.value.trim();
  if (!text) return;

  if (deletingMemoIndex !== null) {
    memoList[deletingMemoIndex] = text;
  } else {
    memoList.push(text);
  }

  renderMemoList();
  closeModal(memoModal);
});

// ===============================
// メモキャンセル
// ===============================
memoCancel.addEventListener("click", () => {
  closeModal(memoModal);
});

// ===============================
// メモ一覧描画
// ===============================
function renderMemoList() {
  memoDisplay.innerHTML = "";

  memoList.forEach((memo, index) => {
    const wrapper = document.createElement("div");
    wrapper.className = "memo-line";

    const textSpan = document.createElement("span");
    textSpan.className = "memo-text";
    textSpan.textContent = memo;

    textSpan.addEventListener("click", () => {
      memoInput.value = memo;
      deletingMemoIndex = index;
      openModal(memoModal);
    });

    const deleteBtn = document.createElement("button");
    deleteBtn.className = "delete-btn";
    deleteBtn.textContent = "削除";

    deleteBtn.addEventListener("click", () => {
      deletingTask = "memo";
      deletingMemoIndex = index;
      openModal(deleteModal);
    });

    wrapper.appendChild(textSpan);
    wrapper.appendChild(deleteBtn);
    memoDisplay.appendChild(wrapper);
  });
}
