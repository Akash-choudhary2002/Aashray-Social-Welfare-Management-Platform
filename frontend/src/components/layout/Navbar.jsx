import { useEffect, useRef, useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  FaBars, FaHandsHelping, FaSignOutAlt, FaSearch,
  FaBell, FaUserCircle, FaChevronDown, FaTimes
} from 'react-icons/fa';

const PUBLIC_NAV_LINKS = [
  { to: '/', label: 'Home', end: true },
  { to: '/about', label: 'About Us' },
  { to: '/services', label: 'Services' },
  { to: '/contact', label: 'Contact Us' },
  { to: '/faq', label: 'FAQs' }
];

function initials(name = '') {
  return name
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((n) => n[0]?.toUpperCase())
    .join('') || 'U';
}

export default function Navbar({ onToggleSidebar }) {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [notifOpen, setNotifOpen] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);
  const [mobileNavOpen, setMobileNavOpen] = useState(false);
  const notifRef = useRef(null);
  const menuRef = useRef(null);

  useEffect(() => {
    function handleClickOutside(e) {
      if (notifRef.current && !notifRef.current.contains(e.target)) setNotifOpen(false);
      if (menuRef.current && !menuRef.current.contains(e.target)) setMenuOpen(false);
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar navbar-expand navbar-aashray px-3 px-md-4 py-2 sticky-top" style={{ minHeight: 60 }}>
      <div className="d-flex align-items-center">
        {isAuthenticated ? (
          <button
            className="btn btn-sm btn-outline-light me-3 d-md-none rounded-circle d-flex align-items-center justify-content-center"
            style={{ width: 36, height: 36 }}
            onClick={onToggleSidebar}
            aria-label="Toggle sidebar"
          >
            <FaBars />
          </button>
        ) : (
          <button
            className="btn btn-sm btn-outline-light me-3 d-lg-none rounded-circle d-flex align-items-center justify-content-center"
            style={{ width: 36, height: 36 }}
            onClick={() => setMobileNavOpen(true)}
            aria-label="Open menu"
          >
            <FaBars />
          </button>
        )}
        <Link className="navbar-brand d-flex align-items-center gap-2 text-decoration-none text-white" to="/">
          <FaHandsHelping className="fs-4 text-warning" />
          <span className="fw-bold">Aashray</span>
        </Link>
      </div>

      {isAuthenticated && (
        <div className="navbar-search d-none d-md-flex ms-4">
          <FaSearch size={13} />
          <input type="text" placeholder="Search donations, users, tasks..." />
        </div>
      )}

      {!isAuthenticated && (
        <div className="d-none d-lg-flex ms-4 gap-1">
          {PUBLIC_NAV_LINKS.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.end}
              className={({ isActive }) => `public-nav-link${isActive ? ' active' : ''}`}
            >
              {link.label}
            </NavLink>
          ))}
        </div>
      )}

      <div className="ms-auto d-flex align-items-center gap-2 gap-md-3">
        {isAuthenticated ? (
          <>
            <div className="position-relative" ref={notifRef}>
              <button
                className="notif-bell-btn"
                onClick={() => setNotifOpen((p) => !p)}
                aria-label="Notifications"
              >
                <FaBell size={15} />
                <span className="notif-dot" />
              </button>
              {notifOpen && (
                <div className="notif-dropdown">
                  <div className="notif-item">
                    <strong>Welcome to Aashray</strong>
                    <div className="text-muted small mt-1">Your activity and platform alerts will appear here.</div>
                  </div>
                </div>
              )}
            </div>

            <div className="position-relative user-menu" ref={menuRef}>
              <button className="user-avatar-btn" onClick={() => setMenuOpen((p) => !p)}>
                <span className="user-avatar-circle text-white">{initials(user?.fullName)}</span>
                <span className="small fw-medium d-none d-sm-inline">{user?.fullName?.split(' ')[0]}</span>
                <FaChevronDown size={10} className="opacity-75 d-none d-sm-inline" />
              </button>
              {menuOpen && (
                <div className="user-dropdown">
                  <div className="dropdown-header-custom">
                    <div className="fw-semibold small">{user?.fullName}</div>
                    <div className="text-muted" style={{ fontSize: '0.75rem' }}>{user?.role}</div>
                  </div>
                  <Link to="/profile" onClick={() => setMenuOpen(false)}>
                    <FaUserCircle /> My Profile
                  </Link>
                  <button onClick={handleLogout}>
                    <FaSignOutAlt /> Logout
                  </button>
                </div>
              )}
            </div>
          </>
        ) : (
          <>
            <Link to="/login" className="btn btn-sm btn-outline-light px-3 py-1.5 rounded-pill small fw-medium">
              Login
            </Link>
            <Link to="/register" className="btn btn-sm btn-light text-primary px-3 py-1.5 rounded-pill small fw-medium">
              Register
            </Link>
          </>
        )}
      </div>

      {/* Mobile offcanvas nav for logged-out visitors */}
      {!isAuthenticated && (
        <>
          <div
            className={`sidebar-overlay${mobileNavOpen ? ' show' : ''}`}
            style={mobileNavOpen ? { display: 'block', position: 'fixed', inset: 0, background: 'rgba(15,23,42,0.4)', zIndex: 1200 } : {}}
            onClick={() => setMobileNavOpen(false)}
          />
          <div
            className="navbar-offcanvas bg-white shadow-lg"
            style={{
              position: 'fixed', top: 0, left: mobileNavOpen ? 0 : '-300px',
              width: 280, height: '100vh', zIndex: 1210, transition: 'left 0.25s ease',
              display: 'flex', flexDirection: 'column'
            }}
          >
            <div className="d-flex align-items-center justify-content-between px-3 py-3 border-bottom">
              <span className="fw-bold text-primary d-flex align-items-center gap-2">
                <FaHandsHelping /> Aashray
              </span>
              <button className="btn btn-sm btn-light rounded-circle" onClick={() => setMobileNavOpen(false)} aria-label="Close menu">
                <FaTimes />
              </button>
            </div>
            <div className="offcanvas-body p-3 d-flex flex-column gap-1">
              {PUBLIC_NAV_LINKS.map((link) => (
                <NavLink
                  key={link.to}
                  to={link.to}
                  end={link.end}
                  onClick={() => setMobileNavOpen(false)}
                  className={({ isActive }) => `public-nav-link${isActive ? ' active' : ''}`}
                >
                  {link.label}
                </NavLink>
              ))}
            </div>
          </div>
        </>
      )}
    </nav>
  );
}
