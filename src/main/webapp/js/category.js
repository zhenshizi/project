// ▼ 費目ID一覧
const CATEGORY_MAP = {
    "給与": 0,
    "収入その他": 1,
    "家賃": 10,
    "ガス代": 11,
    "電気代": 12,
    "水道代": 13,
    "通信費": 14,
    "保険料": 15,
    "教育費": 16,
    "固定その他": 17,
    "美容費": 30,
    "医療費": 31,
    "食費": 32,
    "被服": 33,
    "交際費": 34,
    "交通費": 35,
    "日用品": 36,
    "趣味": 37,
    "経費": 38,
    "変動その他": 39,
    "投資": 50,
    "貯蓄": 51
};

// ▼ 支出種別の自動判定
function getExpenseType(categoryId) {
    if (categoryId <= 1) return 0;          // 収入
    if (categoryId >= 10 && categoryId <= 17) return 1; // 固定
    if (categoryId >= 30 && categoryId <= 39) return 2; // 変動
    if (categoryId >= 50 && categoryId <= 51) return 3; // 投資・貯蓄
    return null;
}

// ▼ モード（個人／共有）の判定
function getModeValue() {
    const modeSelect = document.querySelector(".select-wrapper select");
    const selected = modeSelect.value;
    if (selected === "個人") return false;
    if (selected === "共有") return true;
    return null;
}

let selectedCategoryId = null;

// ▼ カテゴリボタン押下イベント
document.querySelectorAll(".tag").forEach(btn => {
    btn.addEventListener("click", () => {
        const catName = btn.dataset.cat;
        selectedCategoryId = CATEGORY_MAP[catName];
        const expenseType = getExpenseType(selectedCategoryId);
        const mode = getModeValue();

        console.log("選択カテゴリ:", catName, "ID:", selectedCategoryId, "支出種別:", expenseType, "モード:", mode);

        // active切り替え
        document.querySelectorAll(".tag").forEach(b => b.classList.remove("active"));
        btn.classList.add("active");

        // ▼ DB送信用データひな形
        const payload = {
            date: document.getElementById("date-picker").value,
            price: document.querySelector(".price-input input").value,
            memo: document.querySelector(".input-group input").value,
            categoryId: selectedCategoryId,
            expenseType: expenseType,
            mode: mode
        };

        console.log("送信データ:", payload);
    });
});

