import { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { FaMapMarkerAlt, FaPlus, FaSearch, FaUtensils } from 'react-icons/fa';
import foodApi from '../../api/foodApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatusBadge from '../../components/common/StatusBadge';
import EmptyState from '../../components/common/EmptyState';
import Pagination from '../../components/common/Pagination';
import LocationMapModal from '../../components/common/LocationMapModal';
import { notifyError, notifySuccess } from '../../components/common/toast';

const PAGE_SIZE = 8;

export default function MyFoodDonations() {
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(1);
  const [mapDonation, setMapDonation] = useState(null);

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await foodApi.myDonations();
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

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this donation? This can only be done while it is still PENDING.')) return;
    try {
      await foodApi.remove(id);
      notifySuccess('Donation deleted');
      load();
    } catch (err) {
      notifyError(err.message);
    }
  };

  // Client-side search/pagination only — same data, same API call as before.
  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return donations;
    return donations.filter((d) =>
      [d.foodName, d.foodType, d.city, d.ngoName, d.status].some((v) => v?.toLowerCase().includes(q))
    );
  }, [donations, search]);

  useEffect(() => setPage(1), [search]);

  const totalPages = Math.max(1, Math.ceil(filtered.length / PAGE_SIZE));
  const pageItems = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  if (loading) return <Loading label="Loading your donations..." />;
  if (error) return <ErrorMessage message={error} onRetry={load} />;

  return (
    <div className="fade-in">
      <div className="page-header d-flex justify-content-between align-items-center flex-wrap gap-2">
        <div>
          <h3 className="page-title mb-1">My Food Donations</h3>
          <p className="text-muted small mb-0">Track and manage every donation you've listed.</p>
        </div>
        <Link to="/donor/food-donations/new" className="btn btn-aashray d-inline-flex align-items-center gap-2">
          <FaPlus size={13} /> Add Donation
        </Link>
      </div>

      <div className="table-toolbar">
        <div className="table-search">
          <FaSearch size={13} />
          <input
            type="text"
            className="form-control form-control-sm"
            placeholder="Search by food, city, NGO, status..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
      </div>

      <div className="table-responsive">
        <table className="table align-middle bg-white mb-0">
          <thead>
            <tr>
              <th>Food</th>
              <th>Qty</th>
              <th>Type</th>
              <th>City</th>
              <th>NGO</th>
              <th>Status</th>
              <th>Expiry</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {pageItems.map((d) => (
              <tr key={d.id}>
                <td className="fw-medium">{d.foodName}</td>
                <td>
                  {d.quantity} {d.quantityUnit}
                </td>
                <td>{d.foodType}</td>
                <td>{d.city}</td>
                <td>{d.ngoName || <span className="text-muted">—</span>}</td>
                <td>
                  <StatusBadge status={d.status} />
                </td>
                <td className="text-muted small">{new Date(d.expiryTime).toLocaleString()}</td>
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
                  {d.status === 'PENDING' && (
                    <>
                      <Link to={`/donor/food-donations/${d.id}/edit`} className="btn btn-sm btn-outline-primary me-2">
                        Edit
                      </Link>
                      <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(d.id)}>
                        Delete
                      </button>
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {filtered.length === 0 && (
          <EmptyState
            icon={FaUtensils}
            title={search ? 'No matching donations' : "You haven't made any donations yet"}
            subtitle={search ? 'Try a different search term.' : 'Add your first food donation to get started.'}
            action={
              !search && (
                <Link to="/donor/food-donations/new" className="btn btn-aashray btn-sm">
                  + Add Donation
                </Link>
              )
            }
          />
        )}

        {filtered.length > 0 && (
          <Pagination page={page} totalPages={totalPages} onPageChange={setPage} totalItems={filtered.length} pageSize={PAGE_SIZE} />
        )}
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
