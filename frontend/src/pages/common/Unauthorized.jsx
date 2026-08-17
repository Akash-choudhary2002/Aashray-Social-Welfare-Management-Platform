import { Link } from 'react-router-dom';
import { FaLock } from 'react-icons/fa';

export default function Unauthorized() {
  return (
    <div className="full-page-center">
      <FaLock className="error-icon" style={{ color: 'var(--warning-color)' }} />
       <h1 className="display-4 fw-bold text-secondary mb-2">Hi</h1>
      <p className="lead text-muted mb-4">Welcome to our Web-App.</p>
      <Link to="/" className="btn btn-aashray px-4">
        Back to Home
      </Link>
    </div>
  );
}
