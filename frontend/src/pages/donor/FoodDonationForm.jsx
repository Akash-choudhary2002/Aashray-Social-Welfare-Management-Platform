import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import foodApi from '../../api/foodApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import LocationPicker from '../../components/common/LocationPicker';
import { notifyError, notifySuccess } from '../../components/common/toast';

const FOOD_TYPES = ['COOKED', 'RAW', 'PACKAGED', 'BAKERY', 'FRUITS_VEGETABLES', 'OTHER'];

const EMPTY_FORM = {
  foodName: '',
  quantity: '',
  quantityUnit: 'kg',
  foodType: 'COOKED',
  preparedTime: '',
  expiryTime: '',
  pickupAddress: '',
  latitude: null,
  longitude: null,
  city: '',
  contactNumber: ''
};

// datetime-local inputs need "YYYY-MM-DDTHH:mm"; the backend returns
// full ISO LocalDateTime strings, so trim to the first 16 chars.
function toDatetimeLocal(value) {
  return value ? value.substring(0, 16) : '';
}

export default function FoodDonationForm({ mode }) {
  const isEdit = mode === 'edit';
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState(EMPTY_FORM);
  const [loading, setLoading] = useState(isEdit);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isEdit) return;
    (async () => {
      try {
        const response = await foodApi.myDonations();
        const existing = (response.data || []).find((d) => String(d.id) === String(id));
        if (!existing) {
          setError('Donation not found or no longer editable.');
          return;
        }
        setForm({
          foodName: existing.foodName,
          quantity: existing.quantity,
          quantityUnit: existing.quantityUnit || 'kg',
          foodType: existing.foodType,
          preparedTime: toDatetimeLocal(existing.preparedTime),
          expiryTime: toDatetimeLocal(existing.expiryTime),
          pickupAddress: existing.pickupAddress,
          latitude: existing.latitude ?? null,
          longitude: existing.longitude ?? null,
          city: existing.city,
          contactNumber: existing.contactNumber
        });
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    })();
  }, [isEdit, id]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleLocationChange = ({ latitude, longitude }) => {
    setForm((prev) => ({ ...prev, latitude, longitude }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const payload = { ...form, quantity: Number(form.quantity) };
      if (isEdit) {
        await foodApi.update(id, payload);
        notifySuccess('Donation updated');
      } else {
        await foodApi.create(payload);
        notifySuccess('Donation created');
      }
      navigate('/donor/food-donations');
    } catch (err) {
      notifyError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <Loading label="Loading donation..." />;
  if (error) return <ErrorMessage message={error} />;

  return (
    <div className="card">
      <div className="card-body">
        <h3 className="mb-4">{isEdit ? 'Edit Food Donation' : 'Add Food Donation'}</h3>
        <form onSubmit={handleSubmit}>
          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">Food Name</label>
              <input
                className="form-control"
                name="foodName"
                value={form.foodName}
                onChange={handleChange}
                required
                maxLength={150}
              />
            </div>
            <div className="col-md-3 mb-3">
              <label className="form-label">Quantity</label>
              <input
                type="number"
                min="1"
                className="form-control"
                name="quantity"
                value={form.quantity}
                onChange={handleChange}
                required
              />
            </div>
            <div className="col-md-3 mb-3">
              <label className="form-label">Unit</label>
              <input
                className="form-control"
                name="quantityUnit"
                value={form.quantityUnit}
                onChange={handleChange}
                placeholder="kg / plates / packets"
              />
            </div>
          </div>

          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">Food Type</label>
              <select className="form-select" name="foodType" value={form.foodType} onChange={handleChange}>
                {FOOD_TYPES.map((t) => (
                  <option key={t} value={t}>
                    {t.replaceAll('_', ' ')}
                  </option>
                ))}
              </select>
            </div>
            <div className="col-md-3 mb-3">
              <label className="form-label">Prepared Time</label>
              <input
                type="datetime-local"
                className="form-control"
                name="preparedTime"
                value={form.preparedTime}
                onChange={handleChange}
                required
              />
            </div>
            <div className="col-md-3 mb-3">
              <label className="form-label">Expiry Time</label>
              <input
                type="datetime-local"
                className="form-control"
                name="expiryTime"
                value={form.expiryTime}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <LocationPicker latitude={form.latitude} longitude={form.longitude} onLocationChange={handleLocationChange} />

          <div className="mb-3">
            <label className="form-label">Pickup Address</label>
            <input
              className="form-control"
              name="pickupAddress"
              value={form.pickupAddress}
              onChange={handleChange}
              required
            />
          </div>

          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">City</label>
              <input className="form-control" name="city" value={form.city} onChange={handleChange} required />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">Contact Number</label>
              <input
                className="form-control"
                name="contactNumber"
                value={form.contactNumber}
                onChange={handleChange}
                pattern="\d{10}"
                required
              />
            </div>
          </div>

          <button className="btn btn-aashray" type="submit" disabled={submitting}>
            {submitting ? 'Saving...' : isEdit ? 'Update Donation' : 'Create Donation'}
          </button>
        </form>
      </div>
    </div>
  );
}
