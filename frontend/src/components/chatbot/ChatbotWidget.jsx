import { useEffect, useRef, useState } from 'react';
import { FaCommentDots, FaPaperPlane, FaTimes } from 'react-icons/fa';
import chatbotApi from '../../api/chatbotApi';

const SESSION_KEY = 'aashray_chat_session';

function getOrCreateSessionId() {
  let sessionId = sessionStorage.getItem(SESSION_KEY);
  if (!sessionId) {
    sessionId = `session-${Date.now()}-${Math.floor(Math.random() * 100000)}`;
    sessionStorage.setItem(SESSION_KEY, sessionId);
  }
  return sessionId;
}

export default function ChatbotWidget() {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState([
    { from: 'bot', text: "Hi! I'm the Aashray Assistant. Ask me about registration, donations, or how the platform works." }
  ]);
  const [input, setInput] = useState('');
  const [sending, setSending] = useState(false);
  const scrollRef = useRef(null);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [messages, open]);

  const sendMessage = async (e) => {
    e.preventDefault();
    const trimmed = input.trim();
    if (!trimmed || sending) return;

    setMessages((prev) => [...prev, { from: 'user', text: trimmed }]);
    setInput('');
    setSending(true);

    try {
      const response = await chatbotApi.chat(trimmed, getOrCreateSessionId());
      const reply = response.data?.reply || "Sorry, I couldn't generate a response right now.";
      setMessages((prev) => [...prev, { from: 'bot', text: reply }]);
    } catch (err) {
      setMessages((prev) => [
        ...prev,
        { from: 'bot', text: err.message || 'The assistant is unavailable right now. Please try again shortly.' }
      ]);
    } finally {
      setSending(false);
    }
  };

  return (
    <>
      {open && (
        <div className="chatbot-window">
          <div className="chatbot-header">
            <strong>Aashray Assistant</strong>
            <button
              className="btn btn-sm btn-link text-white p-0"
              onClick={() => setOpen(false)}
              aria-label="Close chat"
            >
              <FaTimes />
            </button>
          </div>
          <div className="chatbot-messages" ref={scrollRef}>
            {messages.map((m, idx) => (
              <div key={idx} className={`chat-bubble ${m.from}`}>
                {m.text}
              </div>
            ))}
            {sending && (
              <div className="chat-bubble bot">
                <span className="typing-indicator">
                  <span /><span /><span />
                </span>
              </div>
            )}
          </div>
          <form className="d-flex border-top p-2" onSubmit={sendMessage}>
            <input
              type="text"
              className="form-control form-control-sm me-2"
              placeholder="Ask a question..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              disabled={sending}
            />
            <button className="btn btn-sm btn-aashray" type="submit" disabled={sending}>
              <FaPaperPlane />
            </button>
          </form>
        </div>
      )}

      <button
        className="chatbot-fab"
        onClick={() => setOpen((prev) => !prev)}
        aria-label="Open chatbot"
        title="Ask the Aashray Assistant"
      >
        <FaCommentDots />
      </button>
    </>
  );
}
