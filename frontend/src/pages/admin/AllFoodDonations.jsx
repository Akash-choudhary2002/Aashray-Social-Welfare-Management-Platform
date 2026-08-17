import { useEffect, useState } from 'react';
import { FaMapMarkerAlt } from 'react-icons/fa';
import foodApi from '../../api/foodApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';
import LocationMapModal from '../../components/common/LocationMapModal';

export default function AllFoodDonations() {
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [mapDonation, setMapDonation] = useState(null);

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await foodApi.allDonations();
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

  if (loading) return <Loading label="Loading food donations..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  return (
    <div>
      <h3 className="mb-4">All Food Donations</h3>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Food</th>
              <th>Qty</th>
              <th>Type</th>
              <th>City</th>
              <th>Donor</th>
              <th>NGO</th>
              <th>Status</th>
              <th>Expiry</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {donations.length === 0 && (
              <tr>
                <td colSpan={9} className="text-center text-muted py-4">
                  No donations found
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
                <td>{d.donorName}</td>
                <td>{d.ngoName || '-'}</td>
                <td>
                  <StatusBadge status={d.status} />
                </td>
                <td>{new Date(d.expiryTime).toLocaleString()}</td>
                <td className="text-nowrap">
                  {d.latitude != null && d.longitude != null && (
                    <button
                      className="btn btn-sm btn-outline-secondary"
                      title="View pickup location"
                      onClick={() => setMapDonation(d)}
                    >
                      <FaMapMarkerAlt size={13} />
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
