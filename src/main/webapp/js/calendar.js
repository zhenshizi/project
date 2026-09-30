function parseDate(val) {
    if (!val || typeof val !== 'string') return new Date();
    const parts = val.trim().split('-');
    if (parts.length === 3) {
        const y = parseInt(parts[0], 10);
        const m = parseInt(parts[1], 10) - 1;
        const d = parseInt(parts[2], 10);
        if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
            return new Date(y, m, d);
        }
    }
    const parsed = new Date(val.replace(/-/g, '/'));
    return isNaN(parsed.getTime()) ? new Date() : parsed;
}

let currentDate = new Date();

const dateDisplay = document.getElementById("date-display");
const datePicker = document.getElementById("date-picker");
const calendarBtn = document.getElementById("calendar-btn");

function updateDateDisplay() {
    if (isNaN(currentDate.getTime())) {
        currentDate = new Date();
    }
    const y = currentDate.getFullYear();
    const m = currentDate.getMonth() + 1;
    const d = currentDate.getDate();
    const weekNames = ["日", "月", "火", "水", "木", "金", "土"];
    const w = weekNames[currentDate.getDay()];

    if (dateDisplay) {
        dateDisplay.textContent = `${y} 年 ${m} 月 ${d} 日 (${w})`;
    }
    if (datePicker) {
        datePicker.value = `${y}-${String(m).padStart(2, "0")}-${String(d).padStart(2, "0")}`;
    }
}

updateDateDisplay();

if (calendarBtn && datePicker) {
    calendarBtn.addEventListener("click", () => {
        datePicker.style.display = "block";
        datePicker.focus();
        if (typeof datePicker.showPicker === "function") {
            datePicker.showPicker();
        }
    });

    datePicker.addEventListener("change", () => {
        const selected = parseDate(datePicker.value);
        if (!isNaN(selected.getTime())) {
            currentDate = selected;
            updateDateDisplay();
        }
        datePicker.style.display = "none";
    });
}

const prevBtn = document.getElementById("prev-day");
if (prevBtn) {
    prevBtn.addEventListener("click", () => {
        currentDate.setDate(currentDate.getDate() - 1);
        updateDateDisplay();
    });
}

const nextBtn = document.getElementById("next-day");
if (nextBtn) {
    nextBtn.addEventListener("click", () => {
        currentDate.setDate(currentDate.getDate() + 1);
        updateDateDisplay();
    });
}

document.addEventListener("click", (e) => {
    if (!calendarBtn || !datePicker) return;
    const isCalendarBtn = e.target.closest("#calendar-btn");
    const isDatePicker = e.target.closest("#date-picker");

    if (isCalendarBtn || isDatePicker) return;

    datePicker.style.display = "none";
});
