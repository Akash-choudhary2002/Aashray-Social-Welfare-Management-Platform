import { useEffect, useState } from 'react';
import volunteerApi from '../../api/volunteerApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';
import { notifyError, notifySuccess } from '../../components/common/toast';

export default function MyTasks() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actingId, setActingId] = useState(null);

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await volunteerApi.myTasks();
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

  const handleAction = async (id, action) => {
    setActingId(id);
    try {
      if (action === 'start') {
        await volunteerApi.startTask(id);
        notifySuccess('Task started');
      } else {
        await volunteerApi.completeTask(id);
        notifySuccess('Task completed');
      }
      load();
    } catch (err) {
      notifyError(err.message);
    } finally {
      setActingId(null);
    }
  };

  if (loading) return <Loading label="Loading your tasks..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  return (
    <div>
      <h3 className="mb-4">My Tasks</h3>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Task</th>
              <th>NGO</th>
              <th>Assigned Date</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {tasks.length === 0 && (
              <tr>
                <td colSpan={5} className="text-center text-muted py-4">
                  No tasks assigned to you right now.
                </td>
              </tr>
            )}
            {tasks.map((t) => (
              <tr key={t.id}>
                <td>
                  {t.taskTitle}
                  {t.taskDescription && <div className="text-muted small">{t.taskDescription}</div>}
                </td>
                <td>{t.ngoName}</td>
                <td>{t.assignedDate}</td>
                <td>
                  <StatusBadge status={t.status} />
                </td>
                <td className="text-nowrap">
                  {t.status === 'ASSIGNED' && (
                    <button
                      className="btn btn-sm btn-primary"
                      disabled={actingId === t.id}
                      onClick={() => handleAction(t.id, 'start')}
                    >
                      Start
                    </button>
                  )}
                  {t.status === 'IN_PROGRESS' && (
                    <button
                      className="btn btn-sm btn-success"
                      disabled={actingId === t.id}
                      onClick={() => handleAction(t.id, 'complete')}
                    >
                      Mark Completed
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
