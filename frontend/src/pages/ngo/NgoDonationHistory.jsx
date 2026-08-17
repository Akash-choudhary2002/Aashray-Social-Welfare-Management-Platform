import { useEffect, useState } from 'react';
import { FaMapMarkerAlt } from 'react-icons/fa';
import foodApi from '../../api/foodApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';
import LocationMapModal from '../../components/common/LocationMapModal';
import { notifyError, notifySuccess } from '../../components/common/toast';

export default function NgoDonationHistory() {
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actingId, setActingId] = useState(null);
  const [mapDonation, setMapDonation] = useState(null);

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await foodApi.ngoHistory();
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

  const handleUpdate = async (id, status) => {
    setActingId(id);
    try {
      if (status === 'PICKED_UP') {
        await foodApi.markPickedUp(id);
        notifySuccess('Marked as picked up');
      } else {
        await foodApi.markDelivered(id);
        notifySuccess('Marked as delivered');
      }
      load();
    } catch (err) {
      notifyError(err.message);
    } finally {
      setActingId(null);
    }
  };

  if (loading) return <Loading label="Loading donation history..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  return (
    <div>
      <h3 className="mb-4">Donation History</h3>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Food</th>
              <th>Qty</th>
              <th>Donor</th>
              <th>City</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {donations.length === 0 && (
              <tr>
                <td colSpan={6} className="text-center text-muted py-4">
                  No donations handled yet.
                </td>
              </tr>
            )}
            {donations.map((d) => (
              <tr key={d.id}>
                <td>{d.foodName}</td>
                <td>
                  {d.quantity} {d.quantityUnit}
                </td>
                <td>{d.donorName}</td>
                <td>{d.city}</td>
                <td>
                  <StatusBadge status={d.status} />
                </td>
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
                  {d.status === 'ACCEPTED' && (
                    <button
                      className="btn btn-sm btn-primary"
                      disabled={actingId === d.id}
                      onClick={() => handleUpdate(d.id, 'PICKED_UP')}
                    >
                      Mark Picked Up
                    </button>
                  )}
                  {d.status === 'PICKED_UP' && (
                    <button
                      className="btn btn-sm btn-success"
                      disabled={actingId === d.id}
                      onClick={() => handleUpdate(d.id, 'DELIVERED')}
                    >
                      Mark Delivered
                    </button>
                  )}
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
