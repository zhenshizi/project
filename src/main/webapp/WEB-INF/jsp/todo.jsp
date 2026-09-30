<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TODO | ねこぜ家計簿</title>

  <!-- CSS -->
  <link rel="stylesheet" href="css/共通.css">
  <link rel="stylesheet" href="css/todostyle.css">

  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <!-- フォント -->
  <link href="https://fonts.googleapis.com/css2?family=Kosugi+Maru&display=swap" rel="stylesheet">
  
  <script>
      window.USER_IS_SHARE = ${currentUser.isShare ? true : false};
      window.USER_HAS_GROUP = ${not empty currentUser.groupId ? true : false};
  </script>
</head>

<body>

  <div class="app-container">

    <header class="header">
      <h1 class="page-title">TODO</h1>
    </header>

    <main class="main-content">

      <!-- ▼ 今やること（タスク）セクション -->
      <section class="card todo-card">
        <div class="card-header pink-header">
          <h2>今やること</h2>
        </div>

        <div class="card-body">
          <div class="memo-input-row" style="margin-bottom: 15px;">
            <button id="add-task-btn" class="add-task-btn" onclick="openAddTaskModal()">
              <i class="fa-solid fa-plus-circle add-icon"></i>
              <span>新しいタスク</span>
            </button>
          </div>

          <ul id="task-list" class="task-list">
            <c:forEach items="${todoList}" var="t">
              <c:if test="${not empty t.todo and fn:trim(t.todo) ne ''}">
                <li class="task-item">
                  <label class="checkbox-container">
                    <input type="checkbox" onchange="toggleTaskCheckbox(this, '${t.id}')" <c:if test="${t.status}">checked</c:if>>
                    <span class="checkmark"></span>
                    <span class="task-text" onclick="openEditTaskModal('${t.id}', '${t.todo}')" style="cursor: pointer; <c:if test='${t.status}'>text-decoration:line-through; color:#A3968E;</c:if>">${t.todo}</span>
                  </label>
                  <button type="button" class="delete-btn <c:if test='${!t.status}'>hidden</c:if>" onclick="confirmDeleteTask('${t.id}')">削除</button>
                </li>
              </c:if>
            </c:forEach>
          </ul>
        </div>
      </section>

      <!-- ▼ メモ・アイデアセクション -->
      <section class="card memo-card">
        <div class="card-header green-header">
          <h2>メモ・アイデア</h2>
        </div>

        <div class="card-body memo-body">
          <div class="memo-input-row">
            <button id="memo-add-btn" class="add-task-btn" onclick="openAddMemoModal()">
              <i class="fa-solid fa-plus-circle add-icon"></i>
              <span>新しいメモ・アイデア</span>
            </button>
          </div>

          <!-- メモ一覧描画エリア -->
          <div id="memo-display" class="memo-lines"></div>
        </div>
      </section>

    </main>

    <!-- ▼ サーバーから取得したアイデアの生データ保持用（非表示） -->
    <div id="server-idea-text" style="display:none;"><c:out value="${ideaText}" /></div>

    <!-- ▼ サーバー送信用の隠しフォーム -->
    <form id="todoForm" action="${pageContext.request.contextPath}/todo" method="post" style="display:none;">
      <input type="hidden" name="action" id="todo_action">
      <input type="hidden" name="id" id="todo_id">
      <input type="hidden" name="todo_text" id="todo_text_input">
      <input type="hidden" name="status" id="todo_status_input">
      <input type="hidden" name="idea_text" id="idea_text_input">
    </form>

    <!-- ▼ タスク追加・編集モーダル -->
    <div id="task-modal" class="modal hidden">
      <div class="modal-content">
        <h3>タスク入力</h3>
        <input id="task-input" type="text" placeholder="タスク内容を入力">
        <div class="modal-buttons">
          <button id="task-cancel" type="button" class="btn-cancel" onclick="closeModal(taskModal)">キャンセル</button>
          <button id="task-ok" type="button" class="btn-save" onclick="saveTask()">保存</button>
        </div>
      </div>
    </div>

    <!-- ▼ 削除確認モーダル -->
    <div id="delete-modal" class="modal hidden">
      <div class="modal-content">
        <p>削除しますか？</p>
        <div class="modal-buttons">
          <button id="delete-yes" type="button" class="btn-save" onclick="executeDelete()">はい</button>
          <button id="delete-no" type="button" class="btn-cancel" onclick="closeModal(deleteModal)">いいえ</button>
        </div>
      </div>
    </div>

    <!-- ▼ メモ追加・編集モーダル -->
    <div id="memo-modal" class="modal hidden">
      <div class="modal-content">
        <h3>メモ・アイデア</h3>
        <textarea id="memo-input" placeholder="メモを入力"></textarea>
        <div class="modal-buttons">
          <button id="memo-cancel" type="button" class="btn-cancel" onclick="closeModal(memoModal)">キャンセル</button>
          <button id="memo-save" type="button" class="btn-save" onclick="saveMemo()">保存</button>
        </div>
      </div>
    </div>

    <!-- ★ フッター挿入用のコンテナ -->
    <div id="bottomnav"></div>

  </div> <!-- /.app-container -->

  <script>
    // --- モーダル制御 ---
    function openModal(modal) {
      modal.classList.remove("hidden");
    }
    function closeModal(modal) {
      modal.classList.add("hidden");
    }

    const taskModal = document.getElementById("task-modal");
    const taskInput = document.getElementById("task-input");
    const deleteModal = document.getElementById("delete-modal");
    const memoModal = document.getElementById("memo-modal");
    const memoInput = document.getElementById("memo-input");
    const memoDisplay = document.getElementById("memo-display");

    // --- タスク関連の処理 ---
    let editingTaskId = null;
    let deletingTaskId = null;
    let deleteType = null; // 'task' または 'memo'

    function openAddTaskModal() {
      editingTaskId = null;
      taskInput.value = "";
      openModal(taskModal);
    }

    function openEditTaskModal(id, text) {
      editingTaskId = id;
      taskInput.value = text;
      openModal(taskModal);
    }

    function saveTask() {
      const text = taskInput.value.trim();
      if (!text) return;

      if (editingTaskId) {
        document.getElementById("todo_action").value = "edit_todo";
        document.getElementById("todo_id").value = editingTaskId;
      } else {
        document.getElementById("todo_action").value = "add_todo";
      }
      document.getElementById("todo_text_input").value = text;
      document.getElementById("todoForm").submit();
    }

    function toggleTaskCheckbox(checkbox, id) {
      const li = checkbox.closest('.task-item');
      const deleteBtn = li.querySelector('.delete-btn');
      if (checkbox.checked) {
        deleteBtn.classList.remove("hidden");
      } else {
        deleteBtn.classList.add("hidden");
      }

      document.getElementById("todo_action").value = "toggle_status";
      document.getElementById("todo_id").value = id;
      document.getElementById("todo_status_input").value = checkbox.checked;
      document.getElementById("todoForm").submit();
    }

    function confirmDeleteTask(id) {
      deletingTaskId = id;
      deleteType = 'task';
      openModal(deleteModal);
    }

    // --- メモ関連の処理（タスクと同じクラスを流用してサイズを完全に一致させる） ---
    let memoList = [];
    const rawIdeaText = document.getElementById("server-idea-text").textContent;
    if (rawIdeaText && rawIdeaText.trim() !== "") {
      memoList = rawIdeaText.split(/\r?\n/).filter(line => line.timer !== "" && line.trim() !== "");
    }

    let deletingMemoIndex = null;

    function renderMemoList() {
      memoDisplay.innerHTML = "";

      memoList.forEach((memo, index) => {
        // タスクと同じ .task-item を使うことで、余白やレイアウトを完全にタスクと揃える
        const wrapper = document.createElement("div");
        wrapper.className = "task-item";
        wrapper.style.marginBottom = "10px"; // タスクリストのgap（10px）に合わせる

        const textSpan = document.createElement("span");
        textSpan.className = "task-text"; // タスクと同じクラスにして文字サイズ（13px）を完全同期
        textSpan.textContent = memo;
        textSpan.style.cursor = "pointer";

        textSpan.addEventListener("click", () => {
          memoInput.value = memo;
          deletingMemoIndex = index;
          openModal(memoModal);
        });

        const deleteBtn = document.createElement("button");
        deleteBtn.className = "delete-btn"; // タスクと同じ削除ボタンのスタイル
        deleteBtn.textContent = "削除";

        deleteBtn.addEventListener("click", () => {
          deleteType = 'memo';
          deletingMemoIndex = index;
          openModal(deleteModal);
        });

        wrapper.appendChild(textSpan);
        wrapper.appendChild(deleteBtn);
        memoDisplay.appendChild(wrapper);
      });
    }

    // 初期描画
    renderMemoList();

    function openAddMemoModal() {
      memoInput.value = "";
      deletingMemoIndex = null;
      openModal(memoModal);
    }

    function saveMemo() {
      const text = memoInput.value.trim();
      if (!text) return;

      if (deletingMemoIndex !== null) {
        memoList[deletingMemoIndex] = text;
      } else {
        memoList.push(text);
      }

      syncMemosToServer();
    }

    // --- 削除確認：はい ---
    function executeDelete() {
      if (deleteType === 'task') {
        if (!deletingTaskId) return;
        document.getElementById("todo_action").value = "delete_todo";
        document.getElementById("todo_id").value = deletingTaskId;
        document.getElementById("todoForm").submit();
      } else if (deleteType === 'memo') {
        if (deletingMemoIndex === null) return;
        memoList.splice(deletingMemoIndex, 1);
        syncMemosToServer();
      }
    }

    // メモリストを改行で結合してサーブレットへ送信（保存）する
    function syncMemosToServer() {
      const combinedText = memoList.join("\n");
      document.getElementById("todo_action").value = "save_idea";
      document.getElementById("idea_text_input").value = combinedText;
      document.getElementById("todoForm").submit();
    }
  </script>

  <!-- ★ フッター自動読み込み用JS -->
  <script src="js/bottomnav-loader.js"></script>

</body>
</html>