import { useEffect, useState } from 'react';
import { FaMapMarkerAlt } from 'react-icons/fa';
import foodApi from '../../api/foodApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import LocationMapModal from '../../components/common/LocationMapModal';
import { notifyError, notifySuccess } from '../../components/common/toast';

export default function AvailableDonations() {
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actingId, setActingId] = useState(null);
  const [mapDonation, setMapDonation] = useState(null);

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await foodApi.available();
      setDonations(response.data || []);
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
      if (action === 'accept') {
        await foodApi.accept(id);
        notifySuccess('Donation accepted');
      } else {
        await foodApi.reject(id);
        notifySuccess('Donation rejected');
      }
      load();
    } catch (err) {
      notifyError(err.message);
    } finally {
      setActingId(null);
    }
  };

  if (loading) return <Loading label="Loading available donations..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  return (
    <div>
      <h3 className="mb-4">Available Food Donations</h3>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Food</th>
              <th>Qty</th>
              <th>Type</th>
              <th>City</th>
              <th>Pickup Address</th>
              <th>Contact</th>
              <th>Expiry</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {donations.length === 0 && (
              <tr>
                <td colSpan={8} className="text-center text-muted py-4">
                  No pending donations right now.
                </td>
              </tr>
            )}
            {donations.map((d) => (
              <tr key={d.id}>
                <td>{d.foodName}</td>
                <td>
                  {d.quantity} {d.quantityUnit}
                </td>
                <td>{d.foodType}</td>
                <td>{d.city}</td>
                <td>{d.pickupAddress}</td>
                <td>{d.contactNumber}</td>
                <td>{new Date(d.expiryTime).toLocaleString()}</td>
                <td className="text-nowrap">
                  {d.latitude != null && d.longitude != null && (
                    <button
                      className="btn btn-sm btn-outline-secondary me-2"
                      title="View pickup location"
                      onClick={() => setMapDonation(d)}
                    >
                      <FaMapMarkerAlt size={13} />
                    </button>
                  )}
                  <button
                    className="btn btn-sm btn-success me-2"
                    disabled={actingId === d.id}
                    onClick={() => handleAction(d.id, 'accept')}
                  >
                    Accept
                  </button>
                  <button
                    className="btn btn-sm btn-outline-danger"
                    disabled={actingId === d.id}
                    onClick={() => handleAction(d.id, 'reject')}
                  >
                    Reject
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {mapDonation && (
        <LocationMapModal
          title={mapDonation.foodName}
          address={mapDonation.pickupAddress}
          latitude={mapDonation.latitude}
          longitude={mapDonation.longitude}
          onClose={() => setMapDonation(null)}
        />
      )}
    </div>
  );
}
