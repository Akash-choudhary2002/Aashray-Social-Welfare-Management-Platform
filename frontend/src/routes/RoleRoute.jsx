import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Loading from '../components/common/Loading';

/**
 * Wraps a set of routes so only users whose role is in `roles` can enter.
 * Usage: <Route element={<RoleRoute roles={['ADMIN']} />}> ... </Route>
 */
export default function RoleRoute({ roles = [] }) {
  const { user, initializing, isAuthenticated } = useAuth();

  if (initializing) {
    return <Loading label="Checking permissions..." fullPage />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (roles.length > 0 && !roles.includes(user?.role)) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
}
