import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import educationApi from '../../api/educationApi';
import Loading from '../../components/common/Loading';
import { notifyError, notifySuccess } from '../../components/common/toast';

const EMPTY_FORM = { studentId: '', educatorId: '', educatorName: '', subject: '', assignmentDate: '' };

export default function AssignEducator() {
  const navigate = useNavigate();
  const [students, setStudents] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    (async () => {
      try {
        const response = await educationApi.myStudents();
        setStudents(response.data || []);
      } catch (err) {
        notifyError(err.message);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await educationApi.assignEducator({
        ...form,
        studentId: Number(form.studentId),
        educatorId: Number(form.educatorId)
      });
      notifySuccess('Educator assigned');
      navigate('/ngo/assignment-history');
    } catch (err) {
      notifyError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <Loading label="Loading students..." />;

  return (
    <div className="card">
      <div className="card-body">
        <h3 className="mb-4">Assign Educator to Student</h3>
        <form onSubmit={handleSubmit}>
          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">Student</label>
              <select className="form-select" name="studentId" value={form.studentId} onChange={handleChange} required>
                <option value="" disabled>
                  Select a student
                </option>
                {students.map((s) => (
                  <option key={s.id} value={s.id}>
                    {s.fullName} ({s.city})
                  </option>
                ))}
              </select>
              {students.length === 0 && (
                <div className="form-text text-danger">Register a student first before assigning an educator.</div>
              )}
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">Assignment Date</label>
              <input
                type="date"
                className="form-control"
                name="assignmentDate"
                value={form.assignmentDate}
                onChange={handleChange}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-3">
              <label className="form-label">Educator ID</label>
              <input
                type="number"
                className="form-control"
                name="educatorId"
                value={form.educatorId}
                onChange={handleChange}
                required
              />
              <div className="form-text">The registered user ID of the Educator.</div>
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">Educator Name</label>
              <input
                className="form-control"
                name="educatorName"
                value={form.educatorName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">Subject</label>
              <input className="form-control" name="subject" value={form.subject} onChange={handleChange} required />
            </div>
          </div>
          <button className="btn btn-aashray" type="submit" disabled={submitting || students.length === 0}>
            {submitting ? 'Assigning...' : 'Assign Educator'}
          </button>
        </form>
      </div>
    </div>
  );
}
