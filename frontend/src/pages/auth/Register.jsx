import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { notifyError, notifySuccess } from '../../components/common/toast';
import { FaUser, FaEnvelope, FaLock, FaPhone, FaMapMarkerAlt, FaCity, FaUserTag, FaEye, FaEyeSlash, FaHandsHelping } from 'react-icons/fa';

const ROLES = ['DONOR', 'NGO', 'EDUCATOR', 'VOLUNTEER'];

const INITIAL_FORM = {
  fullName: '',
  email: '',
  password: '',
  phoneNumber: '',
  address: '',
  city: '',
  role: 'DONOR'
};

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState(INITIAL_FORM);
  const [submitting, setSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await register(form);
      notifySuccess('Registration successful! Please log in.');
      navigate('/login');
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
        <h2>Join the movement.</h2>
        <p>
          Whether you're donating, volunteering, or running an NGO — Aashray gives you
          one dashboard to coordinate real-world impact.
        </p>
        <div className="auth-stat-row">
          <div className="stat"><b>6</b><span>Roles supported</span></div>
          <div className="stat"><b>Secure</b><span>JWT-based access</span></div>
        </div>
      </div>

      <div className="auth-split-form">
    <div className="card auth-card slide-up" style={{ maxWidth: 560 }}>
      <div className="card-body p-4 p-sm-5">
        <div className="text-center mb-4">
          <h3 className="fw-bold text-secondary mb-1">Create Aashray Account</h3>
          <p className="text-muted small">Join us to make a meaningful difference</p>
        </div>
        
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label fw-semibold text-secondary small">Full Name</label>
            <div className="input-group">
              <span className="input-group-text bg-light border-end-0 text-muted">
                <FaUser />
              </span>
              <input
                className="form-control border-start-0 ps-0"
                name="fullName"
                value={form.fullName}
                onChange={handleChange}
                placeholder="Mahendra More"
                required
                minLength={2}
                maxLength={100}
              />
            </div>
          </div>
          
          <div className="mb-3">
            <label className="form-label fw-semibold text-secondary small">Email Address</label>
            <div className="input-group">
              <span className="input-group-text bg-light border-end-0 text-muted">
                <FaEnvelope />
              </span>
              <input
                type="email"
                className="form-control border-start-0 ps-0"
                name="email"
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
                className="form-control border-start-0 border-end-0 ps-0"
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="Min 8 characters"
                required
                minLength={8}
              />
              <button
                type="button"
                className="btn btn-outline-light border-start-0 text-muted bg-white"
                onClick={() => setShowPassword(!showPassword)}
                style={{ borderColor: '#e2e8f0' }}
              >
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>
            <div className="form-text small">At least 8 characters, with a letter and a number.</div>
          </div>
          
          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label fw-semibold text-secondary small">Phone Number</label>
              <div className="input-group">
                <span className="input-group-text bg-light border-end-0 text-muted">
                  <FaPhone />
                </span>
                <input
                  className="form-control border-start-0 ps-0"
                  name="phoneNumber"
                  value={form.phoneNumber}
                  onChange={handleChange}
                  placeholder="10 digit number"
                  pattern="\d{10}"
                />
              </div>
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label fw-semibold text-secondary small">City</label>
              <div className="input-group">
                <span className="input-group-text bg-light border-end-0 text-muted">
                  <FaCity />
                </span>
                <input
                  className="form-control border-start-0 ps-0"
                  name="city"
                  value={form.city}
                  onChange={handleChange}
                  placeholder="City"
                />
              </div>
            </div>
          </div>
          
          <div className="mb-3">
            <label className="form-label fw-semibold text-secondary small">Full Address</label>
            <div className="input-group">
              <span className="input-group-text bg-light border-end-0 text-muted">
                <FaMapMarkerAlt />
              </span>
              <input
                className="form-control border-start-0 ps-0"
                name="address"
                value={form.address}
                onChange={handleChange}
                placeholder="House No, Street, Landmark"
              />
            </div>
          </div>
          
          <div className="mb-4">
            <label className="form-label fw-semibold text-secondary small">Register As</label>
            <div className="input-group">
              <span className="input-group-text bg-light border-end-0 text-muted">
                <FaUserTag />
              </span>
              <select 
                className="form-select border-start-0 ps-0" 
                name="role" 
                value={form.role} 
                onChange={handleChange}
              >
                {ROLES.map((role) => (
                  <option key={role} value={role}>
                    {role.charAt(0) + role.slice(1).toLowerCase()}
                  </option>
                ))}
              </select>
            </div>
          </div>
          
          <button className="btn btn-aashray w-100 py-2.5 fw-semibold" type="submit" disabled={submitting}>
            {submitting ? 'Creating Account...' : 'Sign Up'}
          </button>
        </form>
        
        <div className="text-center mt-4 pt-2 border-top">
          <span className="text-muted small">Already registered? </span>
          <Link to="/login" className="text-primary small text-decoration-none fw-semibold">
            Login here
          </Link>
        </div>
      </div>
    </div>
      </div>
    </div>
  );
}
