import { useEffect, useState } from 'react';
import volunteerApi from '../../api/volunteerApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';
import { notifyError, notifySuccess } from '../../components/common/toast';

export default function NgoVolunteerTasks() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actingId, setActingId] = useState(null);

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await volunteerApi.ngoTasks();
      setTasks(response.data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleCancel = async (id) => {
    if (!window.confirm('Cancel this task?')) return;
    setActingId(id);
    try {
      await volunteerApi.cancelTask(id);
      notifySuccess('Task cancelled');
      load();
    } catch (err) {
      notifyError(err.message);
    } finally {
      setActingId(null);
    }
  };

  if (loading) return <Loading label="Loading tasks..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  return (
    <div>
      <h3 className="mb-4">Volunteer Task History</h3>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Task</th>
              <th>Volunteer</th>
              <th>Assigned Date</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {tasks.length === 0 && (
              <tr>
                <td colSpan={5} className="text-center text-muted py-4">
                  No tasks assigned yet.
                </td>
              </tr>
            )}
            {tasks.map((t) => (
              <tr key={t.id}>
                <td>
                  {t.taskTitle}
                  {t.taskDescription && <div className="text-muted small">{t.taskDescription}</div>}
                </td>
                <td>{t.volunteerName}</td>
                <td>{t.assignedDate}</td>
                <td>
                  <StatusBadge status={t.status} />
                </td>
                <td>
                  {(t.status === 'ASSIGNED' || t.status === 'IN_PROGRESS') && (
                    <button
                      className="btn btn-sm btn-outline-danger"
                      disabled={actingId === t.id}
                      onClick={() => handleCancel(t.id)}
                    >
                      Cancel
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
