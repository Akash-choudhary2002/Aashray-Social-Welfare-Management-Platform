import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import volunteerApi from '../../api/volunteerApi';
import { notifyError, notifySuccess } from '../../components/common/toast';

const EMPTY_FORM = { volunteerId: '', volunteerName: '', taskTitle: '', taskDescription: '', assignedDate: '' };

export default function AssignVolunteerTask() {
  const navigate = useNavigate();
  const [form, setForm] = useState(EMPTY_FORM);
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await volunteerApi.assignTask({ ...form, volunteerId: Number(form.volunteerId) });
      notifySuccess('Task assigned');
      navigate('/ngo/volunteer-tasks');
    } catch (err) {
      notifyError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="card">
      <div className="card-body">
        <h3 className="mb-4">Assign Volunteer Task</h3>
        <form onSubmit={handleSubmit}>
          <div className="row">
            <div className="col-md-4 mb-3">
              <label className="form-label">Volunteer ID</label>
              <input
                type="number"
                className="form-control"
                name="volunteerId"
                value={form.volunteerId}
                onChange={handleChange}
                required
              />
              <div className="form-text">The registered user ID of the Volunteer.</div>
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">Volunteer Name</label>
              <input
                className="form-control"
                name="volunteerName"
                value={form.volunteerName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">Assigned Date</label>
              <input
                type="date"
                className="form-control"
                name="assignedDate"
                value={form.assignedDate}
                onChange={handleChange}
                required
              />
            </div>
          </div>
          <div className="mb-3">
            <label className="form-label">Task Title</label>
            <input
              className="form-control"
              name="taskTitle"
              value={form.taskTitle}
              onChange={handleChange}
              required
              maxLength={150}
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Task Description</label>
            <textarea
              className="form-control"
              name="taskDescription"
              value={form.taskDescription}
              onChange={handleChange}
              maxLength={500}
              rows={3}
            />
          </div>
          <button className="btn btn-aashray" type="submit" disabled={submitting}>
            {submitting ? 'Assigning...' : 'Assign Task'}
          </button>
        </form>
      </div>
    </div>
  );
}
