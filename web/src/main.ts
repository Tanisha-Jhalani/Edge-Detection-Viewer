const canvas = document.getElementById('canvas') as HTMLCanvasElement;
const ctx = canvas.getContext('2d')!;
const fpsEl = document.getElementById('fps') as HTMLSpanElement;
const resEl = document.getElementById('res') as HTMLSpanElement;

const img = new Image();
// 2x2 PNG (black/white checker) encoded as data URL
img.src = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAIAAAD91JpzAAAADUlEQVQImWNgYGD4zwAEAAH7AQg4jv4xAAAAAElFTkSuQmCC';
img.onload = () => {
  canvas.width = img.width;
  canvas.height = img.height;
  resEl.textContent = `${img.width}x${img.height}`;
  let last = performance.now();
  let frames = 0;
  function loop(now: number) {
    frames++;
    if (now - last >= 1000) {
      fpsEl.textContent = frames.toString();
      frames = 0;
      last = now;
    }
    ctx.drawImage(img, 0, 0);
    requestAnimationFrame(loop);
  }
  requestAnimationFrame(loop);
};


