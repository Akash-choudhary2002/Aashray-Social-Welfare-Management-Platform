import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';

import { AuthProvider } from './context/AuthContext';
import ErrorBoundary from './components/common/ErrorBoundary';
import ProtectedRoute from './routes/ProtectedRoute';
import RoleRoute from './routes/RoleRoute';

import PublicLayout from './components/layout/PublicLayout';
import DashboardLayout from './components/layout/DashboardLayout';

import Home from './pages/common/Home';
import About from './pages/common/About';
import Services from './pages/common/Services';
import Contact from './pages/common/Contact';
import Faq from './pages/common/Faq';
import PrivacyPolicy from './pages/common/PrivacyPolicy';
import Terms from './pages/common/Terms';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import ForgotPassword from './pages/auth/ForgotPassword';
import Profile from './pages/auth/Profile';
import NotFound from './pages/common/NotFound';
import Unauthorized from './pages/common/Unauthorized';

import Dashboard from './pages/admin/Dashboard';
import Users from './pages/admin/Users';
import AllFoodDonations from './pages/admin/AllFoodDonations';
import AllMonetaryDonations from './pages/admin/AllMonetaryDonations';
import AllStudents from './pages/admin/AllStudents';
import AllAssignments from './pages/admin/AllAssignments';
import AllVolunteerTasks from './pages/admin/AllVolunteerTasks';

import MyFoodDonations from './pages/donor/MyFoodDonations';
import FoodDonationForm from './pages/donor/FoodDonationForm';
import DonateMoney from './pages/donor/DonateMoney';
import MonetaryDonationHistory from './pages/donor/MonetaryDonationHistory';

import AvailableDonations from './pages/ngo/AvailableDonations';
import NgoDonationHistory from './pages/ngo/NgoDonationHistory';
import NgoStudents from './pages/ngo/NgoStudents';
import AssignEducator from './pages/ngo/AssignEducator';
import AssignmentHistory from './pages/ngo/AssignmentHistory';
import AssignVolunteerTask from './pages/ngo/AssignVolunteerTask';
import NgoVolunteerTasks from './pages/ngo/NgoVolunteerTasks';

import EducatorMyStudents from './pages/educator/MyStudents';

import VolunteerMyTasks from './pages/volunteer/MyTasks';
import VolunteerCompletedTasks from './pages/volunteer/CompletedTasks';

export default function App() {
  return (
    <ErrorBoundary>
      <AuthProvider>
        <Routes>
          {/* Public pages (still get Navbar/Footer/Chatbot) */}
          <Route element={<PublicLayout />}>
            <Route path="/" element={<Home />} />
            <Route path="/about" element={<About />} />
            <Route path="/services" element={<Services />} />
            <Route path="/contact" element={<Contact />} />
            <Route path="/faq" element={<Faq />} />
            <Route path="/privacy-policy" element={<PrivacyPolicy />} />
            <Route path="/terms" element={<Terms />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/forgot-password" element={<ForgotPassword />} />
            <Route path="/unauthorized" element={<Unauthorized />} />
          </Route>

          {/* Authenticated pages (Navbar/Sidebar/Footer/Chatbot) */}
          <Route element={<ProtectedRoute />}>
            <Route element={<DashboardLayout />}>
              <Route path="/profile" element={<Profile />} />

              {/* Admin */}
              <Route element={<RoleRoute roles={['ADMIN']} />}>
                <Route path="/admin/dashboard" element={<Dashboard />} />
                <Route path="/admin/users" element={<Users />} />
                <Route path="/admin/food-donations" element={<AllFoodDonations />} />
                <Route path="/admin/monetary-donations" element={<AllMonetaryDonations />} />
                <Route path="/admin/students" element={<AllStudents />} />
                <Route path="/admin/assignments" element={<AllAssignments />} />
                <Route path="/admin/volunteer-tasks" element={<AllVolunteerTasks />} />
              </Route>

              {/* Donor */}
              <Route element={<RoleRoute roles={['DONOR']} />}>
                <Route path="/donor/food-donations" element={<MyFoodDonations />} />
                <Route path="/donor/food-donations/new" element={<FoodDonationForm mode="create" />} />
                <Route path="/donor/food-donations/:id/edit" element={<FoodDonationForm mode="edit" />} />
                <Route path="/donor/monetary-donations" element={<MonetaryDonationHistory />} />
                <Route path="/donor/monetary-donations/new" element={<DonateMoney />} />
              </Route>

              {/* NGO */}
              <Route element={<RoleRoute roles={['NGO']} />}>
                <Route path="/ngo/available-donations" element={<AvailableDonations />} />
                <Route path="/ngo/donation-history" element={<NgoDonationHistory />} />
                <Route path="/ngo/students" element={<NgoStudents />} />
                <Route path="/ngo/assign-educator" element={<AssignEducator />} />
                <Route path="/ngo/assignment-history" element={<AssignmentHistory />} />
                <Route path="/ngo/assign-volunteer" element={<AssignVolunteerTask />} />
                <Route path="/ngo/volunteer-tasks" element={<NgoVolunteerTasks />} />
              </Route>

              {/* Educator */}
              <Route element={<RoleRoute roles={['EDUCATOR']} />}>
                <Route path="/educator/my-students" element={<EducatorMyStudents />} />
              </Route>

              {/* Volunteer */}
              <Route element={<RoleRoute roles={['VOLUNTEER']} />}>
                <Route path="/volunteer/my-tasks" element={<VolunteerMyTasks />} />
                <Route path="/volunteer/completed-tasks" element={<VolunteerCompletedTasks />} />
              </Route>
            </Route>
          </Route>

          <Route path="*" element={<NotFound />} />
        </Routes>
        <ToastContainer />
      </AuthProvider>
    </ErrorBoundary>
  );
}
