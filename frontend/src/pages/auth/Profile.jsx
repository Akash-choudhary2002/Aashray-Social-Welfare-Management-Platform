import { useEffect, useState } from 'react';
import authApi from '../../api/authApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import { notifyError, notifySuccess } from '../../components/common/toast';
import { useAuth } from '../../context/AuthContext';
import { FaUser, FaPhone, FaMapMarkerAlt, FaCity, FaEnvelope, FaCalendarAlt, FaShieldAlt, FaSave } from 'react-icons/fa';

export default function Profile() {
  const { refreshProfile } = useAuth();
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState({ fullName: '', phoneNumber: '', address: '', city: '' });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  const loadProfile = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await authApi.getProfile();
      setProfile(response.data);
      setForm({
        fullName: response.data.fullName || '',
        phoneNumber: response.data.phoneNumber || '',
        address: response.data.address || '',
        city: response.data.city || ''
      });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const response = await authApi.updateProfile(form);
      setProfile(response.data);
      await refreshProfile();
      notifySuccess('Profile updated successfully');
    } catch (err) {
      notifyError(err.message);
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <Loading label="Loading profile..." />;
  if (error) return <ErrorMessage message={error} onRetry={loadProfile} />;

  return (
    <div className="container-fluid py-2">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h3 className="fw-bold mb-1">My Account</h3>
          <p className="text-muted small mb-0">Manage your profile details and settings</p>
        </div>
      </div>
      
      <div className="row g-4">
        <div className="col-lg-4">
          <div className="card h-100 shadow-sm border-0">
            <div className="card-body text-center p-4">
              <div 
                className="rounded-circle bg-primary-light text-primary mx-auto d-flex align-items-center justify-content-center mb-3 fw-bold"
                style={{ width: 80, height: 80, fontSize: '2rem' }}
              >
                {profile.fullName ? profile.fullName.charAt(0).toUpperCase() : 'U'}
              </div>
              <h4 className="fw-bold mb-1">{profile.fullName}</h4>
              <span className="badge bg-light text-primary border rounded-pill px-3 py-1.5 small fw-semibold text-uppercase">
                {profile.role}
              </span>
              
              <hr className="my-4" />
              
              <div className="text-start">
                <div className="d-flex align-items-center gap-3 mb-3">
                  <div className="text-muted"><FaEnvelope /></div>
                  <div>
                    <div className="small text-muted">Email Address</div>
                    <div className="fw-semibold text-truncate">{profile.email}</div>
                  </div>
                </div>
                
                <div className="d-flex align-items-center gap-3 mb-3">
                  <div className="text-muted"><FaShieldAlt /></div>
                  <div>
                    <div className="small text-muted">Account Status</div>
                    <div>
                      <span className={`badge-status ${profile.enabled ? 'text-success' : 'text-danger'}`} style={{
                        backgroundColor: profile.enabled ? '#d1fae5' : '#fee2e2',
                        color: profile.enabled ? '#059669' : '#dc2626'
                      }}>
                        {profile.enabled ? 'Active Account' : 'Disabled'}
                      </span>
                    </div>
                  </div>
                </div>
                
                <div className="d-flex align-items-center gap-3">
                  <div className="text-muted"><FaCalendarAlt /></div>
                  <div>
                    <div className="small text-muted">Joined Date</div>
                    <div className="fw-semibold">{new Date(profile.createdAt).toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' })}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div className="col-lg-8">
          <div className="card shadow-sm border-0 h-100">
            <div className="card-body p-4">
              <h5 className="fw-bold mb-4 border-bottom pb-2">Edit Account Information</h5>
              
              <form onSubmit={handleSubmit}>
                <div className="row g-3">
                  <div className="col-md-6 mb-2">
                    <label className="form-label fw-semibold text-secondary small">Full Name</label>
                    <div className="input-group">
                      <span className="input-group-text bg-light border-end-0 text-muted"><FaUser /></span>
                      <input
                        className="form-control border-start-0 ps-0"
                        name="fullName"
                        value={form.fullName}
                        onChange={handleChange}
                        required
                        placeholder="John Doe"
                      />
                    </div>
                  </div>
                  
                  <div className="col-md-6 mb-2">
                    <label className="form-label fw-semibold text-secondary small">Phone Number</label>
                    <div className="input-group">
                      <span className="input-group-text bg-light border-end-0 text-muted"><FaPhone /></span>
                      <input
                        className="form-control border-start-0 ps-0"
                        name="phoneNumber"
                        value={form.phoneNumber}
                        onChange={handleChange}
                        pattern="\d{10}"
                        placeholder="10 digit number"
                      />
                    </div>
                  </div>
                  
                  <div className="col-md-6 mb-2">
                    <label className="form-label fw-semibold text-secondary small">City</label>
                    <div className="input-group">
                      <span className="input-group-text bg-light border-end-0 text-muted"><FaCity /></span>
                      <input 
                        className="form-control border-start-0 ps-0" 
                        name="city" 
                        value={form.city} 
                        onChange={handleChange} 
                        placeholder="e.g. Pune"
                      />
                    </div>
                  </div>
                  
                  <div className="col-md-6 mb-2">
                    <label className="form-label fw-semibold text-secondary small">Address</label>
                    <div className="input-group">
                      <span className="input-group-text bg-light border-end-0 text-muted"><FaMapMarkerAlt /></span>
                      <input 
                        className="form-control border-start-0 ps-0" 
                        name="address" 
                        value={form.address} 
                        onChange={handleChange} 
                        placeholder="Street details"
                      />
                    </div>
                  </div>
                </div>
                
                <div className="mt-4 pt-3 border-top d-flex justify-content-end">
                  <button className="btn btn-aashray px-4 py-2 d-flex align-items-center gap-2" type="submit" disabled={saving}>
                    <FaSave /> {saving ? 'Saving Details...' : 'Save Profile Details'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
