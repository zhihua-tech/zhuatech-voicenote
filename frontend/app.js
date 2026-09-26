/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
const wave = document.querySelector('#wave');
Array.from({length: 86}, (_, index) => {
  const bar = document.createElement('i');
  bar.style.height = `${8 + Math.abs(Math.sin(index * 1.7)) * 22}px`;
  wave.appendChild(bar);
});

let playing = false;
document.querySelector('#play').addEventListener('click', (event) => {
  playing = !playing;
  event.currentTarget.textContent = playing ? 'Ⅱ' : '▶';
  wave.classList.toggle('playing', playing);
});

document.querySelectorAll('.tabs button').forEach(button => button.addEventListener('click', () => {
  document.querySelectorAll('.tabs button,.tab-view').forEach(item => item.classList.remove('active'));
  button.classList.add('active');
  document.querySelector(`#${button.dataset.tab}`).classList.add('active');
}));

document.querySelectorAll('.note-card').forEach(card => card.addEventListener('click', () => {
  document.querySelectorAll('.note-card').forEach(item => item.classList.remove('selected'));
  card.classList.add('selected');
  document.querySelector('#note-title').textContent = card.dataset.title;
}));

document.querySelector('#search').addEventListener('input', (event) => {
  const keyword = event.target.value.trim().toLowerCase();
  document.querySelectorAll('.note-card').forEach(card => {
    card.hidden = !card.textContent.toLowerCase().includes(keyword);
  });
});

const dialog = document.querySelector('#record-dialog');
document.querySelector('#new-note').addEventListener('click', () => dialog.showModal());
document.querySelector('.close').addEventListener('click', () => dialog.close());

document.querySelector('#process').addEventListener('click', async (event) => {
  event.preventDefault();
  const status = document.querySelector('#process-status');
  status.textContent = '正在执行本地演示流程…';
  const payload = {
    audioObjectKey: 'demo/weekly-meeting.m4a', durationSeconds: 1380, language: 'zh-CN',
    speakerDiarization: true, consentConfirmed: document.querySelector('#consent').checked,
    retentionDays: 30, contextHint: document.querySelector('#context').value
  };
  try {
    const response = await fetch('/api/voicenotes/process', {
      method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(payload)
    });
    if (!response.ok) throw new Error('backend unavailable');
    const result = await response.json();
    status.textContent = result.status === 'READY' ? '处理完成，已生成摘要与行动事项' : '已保存为仅限草稿';
  } catch (_) {
    status.textContent = payload.consentConfirmed ? '本地演示完成，已生成摘要与行动事项' : '未确认授权，已保存为仅限草稿';
  }
  setTimeout(() => dialog.close(), 1200);
});

