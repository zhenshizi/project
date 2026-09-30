document.addEventListener("DOMContentLoaded", () => {
  const categoryColors = {
    // 固定費（ブルー系）
    "家賃": "#bae6fd",
    "ガス代": "#c7d2fe",
    "電気代": "#bfdbfe",
    "水道代": "#a5f3fc",
    "通信費": "#93c5fd",
    "保険料": "#dbeafe",
    "教育費": "#cce5ff",
    "固定その他": "#bcd4ff",

    // 変動費（暖色系）
    "美容費": "#fbcfe8",
    "医療費": "#fecdd3",
    "食費": "#fca5a5",
    "被服": "#f9a8d4",
    "交際費": "#fcd5ce",
    "交通費": "#fed7aa",
    "日用品": "#fde68a",
    "趣味": "#fbcfe8",
    "経費": "#ffd6a5",
    "変動その他": "#f8c8dc"
  };

  const chartEl = document.querySelector(".dummy-chart");
  const items = document.querySelectorAll(".category-item[data-name]");

  if (!chartEl) return;

  if (items.length === 0) {
    chartEl.style.background = "conic-gradient(#e5e7eb 0% 100%)";
    return;
  }

  let currentAngle = 0;
  const gradientStops = [];

  items.forEach((item) => {
    const name = item.getAttribute("data-name");
    const rate = parseFloat(item.getAttribute("data-rate")) || 0;
    const color = categoryColors[name] || "#cbd5e1";

    const badge = item.querySelector(".color-badge");
    if (badge) {
      badge.style.backgroundColor = color;
    }

    if (rate > 0) {
      const nextAngle = Math.min(100, currentAngle + rate);
      gradientStops.push(`${color} ${currentAngle}% ${nextAngle}%`);
      currentAngle = nextAngle;
    }
  });

  if (gradientStops.length > 0) {
    if (currentAngle > 0 && currentAngle < 100) {
      const lastIdx = gradientStops.length - 1;
      const lastParts = gradientStops[lastIdx].split(" ");
      gradientStops[lastIdx] = `${lastParts[0]} ${lastParts[1]} 100%`;
    }
    chartEl.style.background = `conic-gradient(${gradientStops.join(", ")})`;
  } else {
    chartEl.style.background = "conic-gradient(#e5e7eb 0% 100%)";
  }
});
