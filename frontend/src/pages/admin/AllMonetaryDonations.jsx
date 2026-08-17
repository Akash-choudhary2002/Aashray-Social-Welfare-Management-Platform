import { useEffect, useState } from 'react';
import monetaryApi from '../../api/monetaryApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';

export default function AllMonetaryDonations() {
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await monetaryApi.allDonations();
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

  if (loading) return <Loading label="Loading monetary donations..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  const total = donations
    .filter((d) => d.paymentStatus === 'SUCCESS')
    .reduce((sum, d) => sum + Number(d.amount || 0), 0);

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
        <h3 className="mb-0">All Monetary Donations</h3>
        <span className="badge bg-success fs-6">Total Confirmed: ₹{total.toLocaleString()}</span>
      </div>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Reference #</th>
              <th>Transaction ID</th>
              <th>Donor</th>
              <th>Amount</th>
              <th>Method</th>
              <th>Status</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {donations.length === 0 && (
              <tr>
                <td colSpan={7} className="text-center text-muted py-4">
                  No donations found
                </td>
              </tr>
            )}
            {donations.map((d) => (
              <tr key={d.id}>
                <td>{d.referenceNumber}</td>
                <td className="text-muted small">{d.razorpayPaymentId || '-'}</td>
                <td>
                  {d.donorName} <div className="text-muted small">{d.donorEmail}</div>
                </td>
                <td>₹{d.amount}</td>
                <td>{d.paymentMethod || '-'}</td>
                <td>
                  <StatusBadge status={d.paymentStatus} />
                </td>
                <td>{new Date(d.donationDate).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
