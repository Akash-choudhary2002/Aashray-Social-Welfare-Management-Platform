import { useEffect, useState } from 'react';
import educationApi from '../../api/educationApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';
import { notifyError, notifySuccess } from '../../components/common/toast';

const EMPTY_FORM = { fullName: '', age: '', gender: '', city: '' };

export default function NgoStudents() {
  const [students, setStudents] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await educationApi.myStudents();
      setStudents(response.data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const startEdit = (student) => {
    setEditingId(student.id);
    setForm({
      fullName: student.fullName,
      age: student.age ?? '',
      gender: student.gender || '',
      city: student.city
    });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setForm(EMPTY_FORM);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const payload = { ...form, age: form.age ? Number(form.age) : null };
      if (editingId) {
        await educationApi.updateStudent(editingId, payload);
        notifySuccess('Student updated');
      } else {
        await educationApi.registerStudent(payload);
        notifySuccess('Student registered');
      }
      cancelEdit();
      load();
    } catch (err) {
      notifyError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div>
      <h3 className="mb-4">Students</h3>

      <div className="card mb-4">
        <div className="card-body">
          <h5 className="card-title">{editingId ? 'Edit Student' : 'Register New Student'}</h5>
          <form onSubmit={handleSubmit} className="row g-2 align-items-end">
            <div className="col-md-3">
              <label className="form-label">Full Name</label>
              <input
                className="form-control"
                name="fullName"
                value={form.fullName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="col-md-2">
              <label className="form-label">Age</label>
              <input
                type="number"
                min="1"
                className="form-control"
                name="age"
                value={form.age}
                onChange={handleChange}
              />
            </div>
            <div className="col-md-2">
              <label className="form-label">Gender</label>
              <input className="form-control" name="gender" value={form.gender} onChange={handleChange} />
            </div>
            <div className="col-md-3">
              <label className="form-label">City</label>
              <input className="form-control" name="city" value={form.city} onChange={handleChange} required />
            </div>
            <div className="col-md-2 d-flex gap-2">
              <button className="btn btn-aashray" type="submit" disabled={submitting}>
                {editingId ? 'Update' : 'Register'}
              </button>
              {editingId && (
                <button type="button" className="btn btn-outline-secondary" onClick={cancelEdit}>
                  Cancel
                </button>
              )}
            </div>
          </form>
        </div>
      </div>

      {loading && <Loading label="Loading students..." />}
      {error && <ErrorMessage message={error} onRetry={load} />}

      {!loading && !error && (
        <div className="table-responsive">
          <table className="table table-striped align-middle bg-white">
            <thead>
              <tr>
                <th>Name</th>
                <th>Age</th>
                <th>Gender</th>
                <th>City</th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {students.length === 0 && (
                <tr>
                  <td colSpan={6} className="text-center text-muted py-4">
                    No students registered yet.
                  </td>
                </tr>
              )}
              {students.map((s) => (
                <tr key={s.id}>
                  <td>{s.fullName}</td>
                  <td>{s.age ?? '-'}</td>
                  <td>{s.gender || '-'}</td>
                  <td>{s.city}</td>
                  <td>
                    <StatusBadge status={s.status} />
                  </td>
                  <td>
                    <button className="btn btn-sm btn-outline-primary" onClick={() => startEdit(s)}>
                      Edit
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
