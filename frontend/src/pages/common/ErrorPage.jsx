import { Link } from 'react-router-dom';
import { FaExclamationTriangle } from 'react-icons/fa';

export default function ErrorPage({ message }) {
  return (
    <div className="full-page-center">
      <FaExclamationTriangle className="error-icon" style={{ color: 'var(--warning-color)' }} />
      <h1 className="display-5 fw-bold text-secondary mb-2">Something went wrong</h1>
      <p className="lead text-muted mb-4">{message || 'An unexpected error occurred while loading this page.'}</p>
      <Link to="/" className="btn btn-aashray px-4">
        Back to Home
      </Link>
    </div>
  );
}
