import { useState } from 'react';
import { Link } from 'react-router-dom';
import { FaEnvelope, FaPaperPlane } from 'react-icons/fa';

/**
 * The Auth Service (Phase 1) does not yet expose a password-reset
 * endpoint. This page collects the request so the UI flow is complete,
 * and clearly tells the user it's not wired to the backend yet rather
 * than silently pretending to send an email.
 */
export default function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true);
  };

  return (
    <div className="d-flex justify-content-center">
    <div className="card auth-card slide-up">
      <div className="card-body p-4 p-sm-5">
        <div className="text-center mb-4">
          <h3 className="fw-bold text-secondary mb-1">Recover Password</h3>
          <p className="text-muted small">We'll help you get back into your account</p>
        </div>
        
        {submitted ? (
          <div className="alert alert-info border-0 shadow-sm p-4">
            <h6 className="fw-bold text-info mb-2">Backend Connection Coming Soon</h6>
            <p className="small mb-0 leading-relaxed text-secondary">
              Password recovery is currently a placeholder flow. Please get in touch with an Aashray administrator to reset your password credentials.
            </p>
          </div>
        ) : (
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label className="form-label fw-semibold text-secondary small">Registered Email Address</label>
              <div className="input-group">
                <span className="input-group-text bg-light border-end-0 text-muted">
                  <FaEnvelope />
                </span>
                <input
                  type="email"
                  className="form-control border-start-0 ps-0"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="name@example.com"
                  required
                />
              </div>
            </div>
            
            <button className="btn btn-aashray w-100 py-2.5 fw-semibold d-flex align-items-center justify-content-center gap-2" type="submit">
              <FaPaperPlane className="small" /> Send Reset Link
            </button>
          </form>
        )}
        
        <div className="text-center mt-4 pt-2 border-top">
          <Link to="/login" className="text-primary small text-decoration-none fw-semibold">
            Back to Sign In
          </Link>
        </div>
      </div>
    </div>
    </div>
  );
}
