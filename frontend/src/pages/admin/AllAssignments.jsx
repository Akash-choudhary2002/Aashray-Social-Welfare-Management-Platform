import { useEffect, useState } from 'react';
import educationApi from '../../api/educationApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';

export default function AllAssignments() {
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await educationApi.allAssignments();
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

  if (loading) return <Loading label="Loading assignments..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  return (
    <div>
      <h3 className="mb-4">All Educator Assignments</h3>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Student</th>
              <th>Educator</th>
              <th>NGO</th>
              <th>Subject</th>
              <th>Assignment Date</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {assignments.length === 0 && (
              <tr>
                <td colSpan={6} className="text-center text-muted py-4">
                  No assignments found
                </td>
              </tr>
            )}
            {assignments.map((a) => (
              <tr key={a.id}>
                <td>{a.studentName}</td>
                <td>{a.educatorName}</td>
                <td>{a.ngoName}</td>
                <td>{a.subject}</td>
                <td>{a.assignmentDate}</td>
                <td>
                  <StatusBadge status={a.status} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
