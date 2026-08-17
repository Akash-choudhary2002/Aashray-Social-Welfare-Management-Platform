import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import Sidebar from './Sidebar';
import Footer from './Footer';
import ChatbotWidget from '../chatbot/ChatbotWidget';

export default function DashboardLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar onToggleSidebar={() => setSidebarOpen((prev) => !prev)} />
      <div className="app-shell">
        <Sidebar open={sidebarOpen} />
        {sidebarOpen && (
          <div className="sidebar-overlay show d-md-none" onClick={() => setSidebarOpen(false)} />
        )}
        <main className="app-content fade-in">
          <Outlet />
        </main>
      </div>
      <Footer />
      <ChatbotWidget />
    </div>
  );
}
