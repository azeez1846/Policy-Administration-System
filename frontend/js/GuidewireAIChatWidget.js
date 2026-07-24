// Guidewire PolicyCenter - Floating AI Co-Pilot Chat Widget ("Guidewire Assist AI")

document.addEventListener('DOMContentLoaded', () => {
    initGuidewireAIChatWidget();
});

function initGuidewireAIChatWidget() {
    if (document.getElementById('gw-ai-chat-btn')) return;

    // Floating Button
    const chatBtn = document.createElement('button');
    chatBtn.id = 'gw-ai-chat-btn';
    chatBtn.innerHTML = `🤖 AI Assist`;
    chatBtn.style.cssText = `
        position: fixed; bottom: 24px; right: 24px; z-index: 99999;
        background: #2563EB; color: #FFFFFF; border: none; border-radius: 24px;
        padding: 12px 20px; font-weight: 700; font-size: 13px; cursor: pointer;
        box-shadow: 0 10px 15px -3px rgba(37, 99, 235, 0.4); display: flex; align-items: center; gap: 8px;
    `;
    chatBtn.onclick = toggleAIChatWindow;

    // Chat Window Container
    const chatWin = document.createElement('div');
    chatWin.id = 'gw-ai-chat-window';
    chatWin.style.cssText = `
        position: fixed; bottom: 80px; right: 24px; z-index: 99999;
        width: 360px; height: 480px; background: #FFFFFF; border-radius: 12px;
        box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.3); border: 1px solid #CBD5E1;
        display: none; flex-direction: column; overflow: hidden;
    `;

    chatWin.innerHTML = `
        <div style="background:#142232; color:#FFFFFF; padding:12px 16px; display:flex; justify-content:space-between; align-items:center;">
            <div style="display:flex; align-items:center; gap:8px;">
                <span style="font-size:16px;">🤖</span>
                <div>
                    <strong style="font-size:13px; display:block;">Guidewire Assist AI</strong>
                    <span style="font-size:10px; color:#A7F3D0;">Online • Neural Policy Assistant</span>
                </div>
            </div>
            <button onclick="toggleAIChatWindow()" style="background:transparent; border:none; color:#FFFFFF; font-size:16px; cursor:pointer;">✕</button>
        </div>

        <div id="gw-ai-chat-messages" style="flex:1; padding:12px; overflow-y:auto; background:#F8FAFC; font-size:12px; display:flex; flex-direction:column; gap:10px;">
            <div style="background:#E2E8F0; color:#0F172A; padding:8px 12px; border-radius:8px; max-width:85%;">
                Hello! I am <strong>Guidewire Assist AI</strong>. Ask me anything about accounts, submissions, rating worksheets, or Gosu rules!
            </div>
        </div>

        <div style="padding:10px; border-top:1px solid #CBD5E1; background:#FFFFFF; display:flex; gap:6px;">
            <input type="text" id="gw-ai-chat-input" placeholder="Ask Guidewire AI..." style="flex:1; padding:8px 12px; border:1px solid #CBD5E1; border-radius:20px; font-size:12px;" onkeydown="if(event.key==='Enter') sendAIChatMessage()">
            <button class="gw-btn gw-btn-primary" style="border-radius:20px; padding:6px 14px; font-size:12px;" onclick="sendAIChatMessage()">Send</button>
        </div>
    `;

    document.body.appendChild(chatBtn);
    document.body.appendChild(chatWin);
}

function toggleAIChatWindow() {
    const win = document.getElementById('gw-ai-chat-window');
    if (!win) return;
    win.style.display = win.style.display === 'none' || win.style.display === '' ? 'flex' : 'none';
}

async function sendAIChatMessage() {
    const input = document.getElementById('gw-ai-chat-input');
    const msgBox = document.getElementById('gw-ai-chat-messages');
    if (!input || !msgBox || !input.value.trim()) return;

    const text = input.value.trim();
    input.value = '';

    // Add User Message Bubble
    const userBubble = document.createElement('div');
    userBubble.style.cssText = `background:#2563EB; color:#FFFFFF; padding:8px 12px; border-radius:8px; align-self:flex-end; max-width:85%;`;
    userBubble.innerText = text;
    msgBox.appendChild(userBubble);
    msgBox.scrollTop = msgBox.scrollHeight;

    try {
        const res = await fetch(`${API_BASE}/ai-assist/chat`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ message: text })
        });
        const data = await res.json();

        // Add AI Message Bubble
        const aiBubble = document.createElement('div');
        aiBubble.style.cssText = `background:#E2E8F0; color:#0F172A; padding:8px 12px; border-radius:8px; align-self:flex-start; max-width:85%; line-height:1.4;`;
        aiBubble.innerHTML = data.reply.replace(/\n/g, '<br>');
        msgBox.appendChild(aiBubble);
        msgBox.scrollTop = msgBox.scrollHeight;
    } catch (e) {
        const aiBubble = document.createElement('div');
        aiBubble.style.cssText = `background:#FEE2E2; color:#991B1B; padding:8px 12px; border-radius:8px; align-self:flex-start; max-width:85%;`;
        aiBubble.innerText = "Error connecting to Guidewire AI service.";
        msgBox.appendChild(aiBubble);
    }
}
