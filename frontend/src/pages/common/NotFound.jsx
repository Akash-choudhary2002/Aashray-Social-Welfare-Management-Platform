import { Link } from 'react-router-dom';
import { FaCompass } from 'react-icons/fa';

export default function NotFound() {
  return (
    <div className="full-page-center">
      <FaCompass className="error-icon" />
      <h1 className="display-4 fw-bold text-secondary mb-2">404</h1>
      <p className="lead text-muted mb-4">The page you're looking for doesn't exist or may have moved.</p>
      <Link to="/" className="btn btn-aashray px-4">
        Back to Home
      </Link>
    </div>
  );
}
