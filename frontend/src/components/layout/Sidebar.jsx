import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { 
  FaTachometerAlt, FaUsers, FaUtensils, FaCoins, FaChild, FaChalkboardTeacher, 
  FaClipboardList, FaPlusCircle, FaHeart, FaHandsHelping, FaHistory, FaUserPlus, 
  FaTasks, FaGraduationCap, FaCheckCircle, FaUser 
} from 'react-icons/fa';

const LINKS_BY_ROLE = {
  ADMIN: [
    { to: '/admin/dashboard', label: 'Dashboard', icon: FaTachometerAlt },
    { to: '/admin/users', label: 'Users', icon: FaUsers },
    { to: '/admin/food-donations', label: 'Food Donations', icon: FaUtensils },
    { to: '/admin/monetary-donations', label: 'Monetary Donations', icon: FaCoins },
    { to: '/admin/students', label: 'Students', icon: FaChild },
    { to: '/admin/assignments', label: 'Educator Assignments', icon: FaChalkboardTeacher },
    { to: '/admin/volunteer-tasks', label: 'Volunteer Tasks', icon: FaClipboardList }
  ],
  DONOR: [
    { to: '/donor/food-donations', label: 'My Food Donations', icon: FaUtensils },
    { to: '/donor/food-donations/new', label: 'Add Food Donation', icon: FaPlusCircle },
    { to: '/donor/monetary-donations', label: 'Monetary Donations', icon: FaCoins },
    { to: '/donor/monetary-donations/new', label: 'Donate Now', icon: FaHeart }
  ],
  NGO: [
    { to: '/ngo/available-donations', label: 'Available Food', icon: FaHandsHelping },
    { to: '/ngo/donation-history', label: 'Donation History', icon: FaHistory },
    { to: '/ngo/students', label: 'Students List', icon: FaChild },
    { to: '/ngo/assign-educator', label: 'Assign Educator', icon: FaUserPlus },
    { to: '/ngo/assignment-history', label: 'Assignment History', icon: FaHistory },
    { to: '/ngo/assign-volunteer', label: 'Assign Volunteer', icon: FaTasks },
    { to: '/ngo/volunteer-tasks', label: 'Volunteer History', icon: FaHistory }
  ],
  EDUCATOR: [{ to: '/educator/my-students', label: 'My Students', icon: FaGraduationCap }],
  VOLUNTEER: [
    { to: '/volunteer/my-tasks', label: 'My Tasks', icon: FaTasks },
    { to: '/volunteer/completed-tasks', label: 'Completed Tasks', icon: FaCheckCircle }
  ],
  BENEFICIARY: [{ to: '/profile', label: 'My Profile', icon: FaUser }]
};

export default function Sidebar({ open }) {
  const { user } = useAuth();
  const links = LINKS_BY_ROLE[user?.role] || [];

  return (
    <aside className={`app-sidebar ${open ? 'open' : ''}`}>
      <div className="sidebar-heading">Navigation Menu</div>
      <nav className="mt-2">
        {links.map((link) => {
          const Icon = link.icon;
          return (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.to.endsWith('donations') || link.to.endsWith('tasks')}
              className={({ isActive }) => (isActive ? 'active' : '')}
            >
              {Icon && <Icon className="fs-5 opacity-75" />}
              <span>{link.label}</span>
            </NavLink>
          );
        })}
      </nav>
    </aside>
  );
}
