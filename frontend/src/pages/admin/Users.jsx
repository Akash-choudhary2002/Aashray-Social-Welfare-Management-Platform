import { useEffect, useState } from 'react';
import authApi from '../../api/authApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import { notifyError, notifySuccess } from '../../components/common/toast';

const ROLES = ['ALL', 'ADMIN', 'DONOR', 'NGO', 'EDUCATOR', 'VOLUNTEER'];

export default function Users() {
  const [users, setUsers] = useState([]);
  const [roleFilter, setRoleFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadUsers = async (role = roleFilter) => {
    setLoading(true);
    setError('');
    try {
      const response = role === 'ALL' ? await authApi.getAllUsers() : await authApi.getUsersByRole(role);
      setUsers(response.data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers('ALL');
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleFilterChange = (role) => {
    setRoleFilter(role);
    loadUsers(role);
  };

  const toggleEnabled = async (user) => {
    try {
      await authApi.setUserEnabled(user.id, !user.enabled);
      notifySuccess(`${user.fullName} ${user.enabled ? 'disabled' : 'enabled'}`);
      loadUsers();
    } catch (err) {
      notifyError(err.message);
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <h3 className="mb-0">User Management</h3>
        <select
          className="form-select w-auto"
          value={roleFilter}
          onChange={(e) => handleFilterChange(e.target.value)}
        >
          {ROLES.map((role) => (
            <option key={role} value={role}>
              {role}
            </option>
          ))}
        </select>
      </div>

      {loading && <Loading label="Loading users..." />}
      {error && <ErrorMessage message={error} onRetry={() => loadUsers()} />}

      {!loading && !error && (
        <div className="table-responsive">
          <table className="table table-striped align-middle bg-white">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>City</th>
                <th>Status</th>
                <th>Joined</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {users.length === 0 && (
                <tr>
                  <td colSpan={7} className="text-center text-muted py-4">
                    No users found
                  </td>
                </tr>
              )}
              {users.map((u) => (
                <tr key={u.id}>
                  <td>{u.fullName}</td>
                  <td>{u.email}</td>
                  <td>{u.role}</td>
                  <td>{u.city || '-'}</td>
                  <td>
                    <span className={`badge ${u.enabled ? 'bg-success' : 'bg-secondary'}`}>
                      {u.enabled ? 'Active' : 'Disabled'}
                    </span>
                  </td>
                  <td>{new Date(u.createdAt).toLocaleDateString()}</td>
                  <td>
                    <button
                      className={`btn btn-sm ${u.enabled ? 'btn-outline-danger' : 'btn-outline-success'}`}
                      onClick={() => toggleEnabled(u)}
                    >
                      {u.enabled ? 'Disable' : 'Enable'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
