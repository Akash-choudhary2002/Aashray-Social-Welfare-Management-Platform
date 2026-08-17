import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import monetaryApi from '../../api/monetaryApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';

export default function MonetaryDonationHistory() {
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await monetaryApi.myDonations();
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

  if (loading) return <Loading label="Loading your donations..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  const total = donations
    .filter((d) => d.paymentStatus === 'SUCCESS')
    .reduce((sum, d) => sum + Number(d.amount || 0), 0);

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
        <h3 className="mb-0">My Monetary Donations</h3>
        <div className="d-flex gap-2">
          <span className="badge bg-success fs-6 align-self-center">Total Given: ₹{total.toLocaleString()}</span>
          <Link to="/donor/monetary-donations/new" className="btn btn-aashray">
            + Donate Now
          </Link>
        </div>
      </div>
      <div className="table-responsive">
        <table className="table table-striped align-middle bg-white">
          <thead>
            <tr>
              <th>Reference #</th>
              <th>Transaction ID</th>
              <th>Amount</th>
              <th>Method</th>
              <th>Purpose</th>
              <th>Status</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {donations.length === 0 && (
              <tr>
                <td colSpan={7} className="text-center text-muted py-4">
                  You haven't made any monetary donations yet.
                </td>
              </tr>
            )}
            {donations.map((d) => (
              <tr key={d.id}>
                <td>{d.referenceNumber}</td>
                <td className="text-muted small">{d.razorpayPaymentId || '-'}</td>
                <td>₹{d.amount}</td>
                <td>{d.paymentMethod || '-'}</td>
                <td>{d.purposeNote || '-'}</td>
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
