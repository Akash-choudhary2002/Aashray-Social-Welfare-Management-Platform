import { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { notifyError, notifySuccess } from '../../components/common/toast';
import { FaEnvelope, FaLock, FaEye, FaEyeSlash, FaHandsHelping } from 'react-icons/fa';

const ROLE_HOME = {
  ADMIN: '/admin/dashboard',
  DONOR: '/donor/food-donations',
  NGO: '/ngo/available-donations',
  EDUCATOR: '/educator/my-students',
  VOLUNTEER: '/volunteer/my-tasks',
  BENEFICIARY: '/profile'
};

const REMEMBER_KEY = 'aashray_remembered_email';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: '', password: '' });
  const [rememberMe, setRememberMe] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  useEffect(() => {
    const remembered = localStorage.getItem(REMEMBER_KEY);
    if (remembered) {
      setForm((f) => ({ ...f, email: remembered }));
      setRememberMe(true);
    }
  }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const loggedInUser = await login(form.email, form.password);

      if (rememberMe) {
        localStorage.setItem(REMEMBER_KEY, form.email);
      } else {
        localStorage.removeItem(REMEMBER_KEY);
      }

      notifySuccess('Welcome back!');
      const redirectTo = location.state?.from?.pathname || ROLE_HOME[loggedInUser.role] || '/';
      navigate(redirectTo, { replace: true });
    } catch (err) {
      notifyError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="auth-split">
      <div className="auth-split-illustration d-none d-md-flex">
        <div className="auth-brand">
          <FaHandsHelping className="text-warning" />
          Aashray
        </div>
        <h2>Welfare, coordinated.</h2>
        <p>
          One platform connecting donors, NGOs, educators, volunteers and beneficiaries —
          so help reaches the people who need it, faster.
        </p>
        <div className="auth-stat-row">
          <div className="stat"><b>6</b><span>Connected roles</span></div>
          <div className="stat"><b>24/7</b><span>Live coordination</span></div>
          <div className="stat"><b>1</b><span>Unified platform</span></div>
        </div>
      </div>

      <div className="auth-split-form">
        <div className="card auth-card slide-up">
          <div className="card-body p-4 p-sm-5">
            <div className="text-center mb-4">
              <h3 className="fw-bold text-secondary mb-1">Welcome Back</h3>
              <p className="text-muted small">Sign in to your Aashray portal to continue</p>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label fw-semibold text-secondary small">Email Address</label>
                <div className="input-group">
                  <span className="input-group-text bg-light border-end-0 text-muted">
                    <FaEnvelope />
                  </span>
                  <input
                    type="email"
                    name="email"
                    className="form-control border-start-0 ps-0"
                    value={form.email}
                    onChange={handleChange}
                    placeholder="name@example.com"
                    required
                  />
                </div>
              </div>

              <div className="mb-3">
                <label className="form-label fw-semibold text-secondary small">Password</label>
                <div className="input-group">
                  <span className="input-group-text bg-light border-end-0 text-muted">
                    <FaLock />
                  </span>
                  <input
                    type={showPassword ? 'text' : 'password'}
                    name="password"
                    className="form-control border-start-0 border-end-0 ps-0"
                    value={form.password}
                    onChange={handleChange}
                    placeholder="Enter password"
                    required
                  />
                  <button
                    type="button"
                    className="btn btn-outline-light border-start-0 text-muted bg-white"
                    onClick={() => setShowPassword(!showPassword)}
                    style={{ borderColor: '#e2e8f0' }}
                    tabIndex={-1}
                  >
                    {showPassword ? <FaEyeSlash /> : <FaEye />}
                  </button>
                </div>
              </div>

              <div className="d-flex justify-content-between align-items-center mb-4">
                <div className="form-check">
                  <input
                    type="checkbox"
                    className="form-check-input"
                    id="rememberMe"
                    checked={rememberMe}
                    onChange={(e) => setRememberMe(e.target.checked)}
                  />
                  <label className="form-check-label small text-muted" htmlFor="rememberMe">
                    Remember me
                  </label>
                </div>
                <Link to="/forgot-password" className="text-primary small text-decoration-none fw-medium">
                  Forgot password?
                </Link>
              </div>

              <button className="btn btn-aashray w-100 py-2 fw-semibold" type="submit" disabled={submitting}>
                {submitting ? 'Authenticating...' : 'Sign In'}
              </button>
            </form>

            <div className="text-center mt-4 pt-2 border-top">
              <span className="text-muted small">New to Aashray? </span>
              <Link to="/register" className="text-primary small text-decoration-none fw-semibold">
                Create an account
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
