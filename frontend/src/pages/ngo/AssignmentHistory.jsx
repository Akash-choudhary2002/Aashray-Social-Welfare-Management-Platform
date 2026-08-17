import { useEffect, useState } from 'react';
import educationApi from '../../api/educationApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';
import { notifyError, notifySuccess } from '../../components/common/toast';

export default function AssignmentHistory() {
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actingId, setActingId] = useState(null);

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await educationApi.ngoAssignments();
      setAssignments(response.data || []);
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
    if (!window.confirm('Cancel this assignment?')) return;
    setActingId(id);
    try {
      await educationApi.cancelAssignment(id);
      notifySuccess('Assignment cancelled');
      load();
    } catch (err) {
      notifyError(err.message);
    } finally {
      setActingId(null);
    }
  };

  if (loading) return <Loading label="Loading assignments..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  return (
    <div>
      <h3 className="mb-4">Educator Assignment History</h3>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Student</th>
              <th>Educator</th>
              <th>Subject</th>
              <th>Date</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {assignments.length === 0 && (
              <tr>
                <td colSpan={6} className="text-center text-muted py-4">
                  No assignments made yet.
                </td>
              </tr>
            )}
            {assignments.map((a) => (
              <tr key={a.id}>
                <td>{a.studentName}</td>
                <td>{a.educatorName}</td>
                <td>{a.subject}</td>
                <td>{a.assignmentDate}</td>
                <td>
                  <StatusBadge status={a.status} />
                </td>
                <td>
                  {a.status === 'ACTIVE' && (
                    <button
                      className="btn btn-sm btn-outline-danger"
                      disabled={actingId === a.id}
                      onClick={() => handleCancel(a.id)}
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
