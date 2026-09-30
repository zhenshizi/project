document.addEventListener("DOMContentLoaded", () => {
  const dots = document.querySelectorAll(".calendar-table .dot");
  if (dots.length === 0) return;

  function rgbToPastel(r, g, b) {
    r /= 255; g /= 255; b /= 255;
    const max = Math.max(r, g, b), min = Math.min(r, g, b);
    let h = 0, s = 0, l = (max + min) / 2;

    if (max !== min) {
      const d = max - min;
      s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
      switch (max) {
        case r: h = (g - b) / d + (g < b ? 6 : 0); break;
        case g: h = (b - r) / d + 2; break;
        case b: h = (r - g) / d + 4; break;
      }
      h /= 6;
    }
    const hue = h * 360;

    if (s < 0.15) return "#cbd5e1"; // Low saturation -> black/gray icon
    if (hue >= 340 || hue < 15) return "#fecdd3";  // red
    if (hue >= 15 && hue < 70) return "#fde68a";   // yellow
    if (hue >= 70 && hue < 165) return "#bbf7d0";  // green
    if (hue >= 165 && hue < 200) return "#a5f3fc"; // skyblue
    if (hue >= 200 && hue < 245) return "#bae6fd"; // blue
    if (hue >= 245 && hue < 290) return "#c7d2fe"; // purple
    if (hue >= 290 && hue < 340) return "#fbcfe8"; // pink

    return "#bae6fd";
  }

  const targetUserId = window.TARGET_USER_ID;
  if (!targetUserId) return;

  const img = new Image();
  img.crossOrigin = "Anonymous";
  img.onload = () => {
    try {
      const canvas = document.createElement("canvas");
      canvas.width = img.naturalWidth || 32;
      canvas.height = img.naturalHeight || 32;
      const ctx = canvas.getContext("2d");
      ctx.drawImage(img, 0, 0);
      const data = ctx.getImageData(0, 0, canvas.width, canvas.height).data;

      let rSum = 0, gSum = 0, bSum = 0, count = 0;
      for (let i = 0; i < data.length; i += 4) {
        if (data[i + 3] > 50) {
          rSum += data[i];
          gSum += data[i + 1];
          bSum += data[i + 2];
          count++;
        }
      }

      if (count > 0) {
        const pastelColor = rgbToPastel(rSum / count, gSum / count, bSum / count);
        dots.forEach(dot => {
          dot.style.backgroundColor = pastelColor;
        });
      }
    } catch (e) {
      console.log("Could not process user icon color", e);
    }
  };
  img.src = "user-icon-image?user_id=" + targetUserId;
});
