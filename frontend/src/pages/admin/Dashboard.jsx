import { useEffect, useState } from 'react';
import {
  FaUsers, FaHandHoldingHeart, FaBuilding, FaHandsHelping,
  FaChalkboardTeacher, FaChild, FaUtensils, FaDonate
} from 'react-icons/fa';
import authApi from '../../api/authApi';
import foodApi from '../../api/foodApi';
import monetaryApi from '../../api/monetaryApi';
import educationApi from '../../api/educationApi';
import volunteerApi from '../../api/volunteerApi';
import Loading from '../../components/common/Loading';
import ErrorMessage from '../../components/common/ErrorMessage';
import StatCard from '../../components/common/StatCard';
import StatusBadge from '../../components/common/StatusBadge';

export default function Dashboard() {
  const [userCounts, setUserCounts] = useState({});
  const [foodStats, setFoodStats] = useState({});
  const [monetaryStats, setMonetaryStats] = useState({});
  const [eduStats, setEduStats] = useState({});
  const [volunteerStats, setVolunteerStats] = useState({});
  const [recentFood, setRecentFood] = useState([]);
  const [recentMonetary, setRecentMonetary] = useState([]);
  const [recentTasks, setRecentTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadDashboard = async () => {
    setLoading(true);
    setError('');
    try {
      const [users, food, monetary, edu, vol, rFood, rMonetary, rTasks] = await Promise.all([
        authApi.getDashboardCounts(),
        foodApi.stats(),
        monetaryApi.stats(),
        educationApi.stats(),
        volunteerApi.stats(),
        foodApi.recent(5),
        monetaryApi.recent(5),
        volunteerApi.recent(5)
      ]);
      setUserCounts(users.data || {});
      setFoodStats(food.data || {});
      setMonetaryStats(monetary.data || {});
      setEduStats(edu.data || {});
      setVolunteerStats(vol.data || {});
      setRecentFood(rFood.data || []);
      setRecentMonetary(rMonetary.data || []);
      setRecentTasks(rTasks.data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboard();
  }, []);

  if (loading) return <Loading label="Loading dashboard..." />;
  if (error) return <ErrorMessage message={error} onRetry={loadDashboard} />;

  const totalUsers = Object.values(userCounts).reduce((sum, v) => sum + Number(v || 0), 0);
  const totalFoodDonations = Object.values(foodStats).reduce((sum, v) => sum + Number(v || 0), 0);
  const totalMonetaryDonations = Number(monetaryStats.totalCount ?? monetaryStats.count ?? 0) ||
    Object.values(monetaryStats).filter((v) => typeof v === 'number').reduce((a, b) => a + b, 0);

  return (
    <div>
      <h3 className="mb-4">Admin Dashboard</h3>

      <div className="row g-3 mb-4">
        <div className="col-6 col-md-3">
          <StatCard label="Total Users" value={totalUsers} icon={FaUsers} />
        </div>
        <div className="col-6 col-md-3">
          <StatCard label="Total Donors" value={userCounts.DONOR} icon={FaHandHoldingHeart} />
        </div>
        <div className="col-6 col-md-3">
          <StatCard label="Total NGOs" value={userCounts.NGO} icon={FaBuilding} />
        </div>
        <div className="col-6 col-md-3">
          <StatCard label="Total Volunteers" value={userCounts.VOLUNTEER} icon={FaHandsHelping} />
        </div>
        <div className="col-6 col-md-3">
          <StatCard label="Total Educators" value={userCounts.EDUCATOR} icon={FaChalkboardTeacher} />
        </div>
        <div className="col-6 col-md-3">
          <StatCard label="Total Students" value={eduStats.totalStudents ?? eduStats.studentCount} icon={FaChild} />
        </div>
        <div className="col-6 col-md-3">
          <StatCard label="Total Food Donations" value={totalFoodDonations} icon={FaUtensils} />
        </div>
        <div className="col-6 col-md-3">
          <StatCard label="Total Monetary Donations" value={totalMonetaryDonations} icon={FaDonate} />
        </div>
      </div>

      <div className="row g-3">
        <div className="col-lg-4">
          <div className="card h-100">
            <div className="card-header fw-semibold">Recent Food Donations</div>
            <ul className="list-group list-group-flush">
              {recentFood.length === 0 && <li className="list-group-item text-muted">No donations yet</li>}
              {recentFood.map((d) => (
                <li key={d.id} className="list-group-item d-flex justify-content-between align-items-center">
                  <span>
                    {d.foodName} <span className="text-muted small">by {d.donorName}</span>
                  </span>
                  <StatusBadge status={d.status} />
                </li>
              ))}
            </ul>
          </div>
        </div>
        <div className="col-lg-4">
          <div className="card h-100">
            <div className="card-header fw-semibold">Recent Monetary Donations</div>
            <ul className="list-group list-group-flush">
              {recentMonetary.length === 0 && <li className="list-group-item text-muted">No donations yet</li>}
              {recentMonetary.map((d) => (
                <li key={d.id} className="list-group-item d-flex justify-content-between align-items-center">
                  <span>
                    ₹{d.amount} <span className="text-muted small">by {d.donorName}</span>
                  </span>
                  <StatusBadge status={d.paymentStatus} />
                </li>
              ))}
            </ul>
          </div>
        </div>
        <div className="col-lg-4">
          <div className="card h-100">
            <div className="card-header fw-semibold">Recent Activities (Volunteer Tasks)</div>
            <ul className="list-group list-group-flush">
              {recentTasks.length === 0 && <li className="list-group-item text-muted">No tasks yet</li>}
              {recentTasks.map((t) => (
                <li key={t.id} className="list-group-item d-flex justify-content-between align-items-center">
                  <span>
                    {t.taskTitle} <span className="text-muted small">→ {t.volunteerName}</span>
                  </span>
                  <StatusBadge status={t.status} />
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
