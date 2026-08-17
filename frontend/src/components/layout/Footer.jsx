import { Link } from 'react-router-dom';
import {
  FaHandsHelping, FaEnvelope, FaPhoneAlt, FaMapMarkerAlt, FaClock,
  FaFacebookF, FaTwitter, FaInstagram, FaLinkedinIn
} from 'react-icons/fa';

export default function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="site-footer pt-5">
      <div className="container">
        <div className="row g-4 pb-4">
          <div className="col-lg-4 col-md-6">
            <div className="footer-brand mb-3">
              <FaHandsHelping className="text-warning" />
              Aashray
            </div>
            <p className="small mb-3" style={{ maxWidth: 320 }}>
              Aashray is a unified social welfare platform connecting donors, NGOs, educators, and
              volunteers to deliver food, funds, education, and community support to those who need
              it most.
            </p>
            <div className="social-icons">
              <a href="#" aria-label="Facebook"><FaFacebookF size={14} /></a>
              <a href="#" aria-label="Twitter"><FaTwitter size={14} /></a>
              <a href="#" aria-label="Instagram"><FaInstagram size={14} /></a>
              <a href="#" aria-label="LinkedIn"><FaLinkedinIn size={14} /></a>
            </div>
          </div>

          <div className="col-lg-2 col-md-6 col-6">
            <h6>Quick Links</h6>
            <Link to="/" className="footer-link">Home</Link>
            <Link to="/about" className="footer-link">About Us</Link>
            <Link to="/services" className="footer-link">Services</Link>
            <Link to="/contact" className="footer-link">Contact Us</Link>
            <Link to="/faq" className="footer-link">FAQs</Link>
          </div>

          <div className="col-lg-3 col-md-6 col-6">
            <h6>Services</h6>
            <Link to="/services" className="footer-link">Food Donation</Link>
            <Link to="/services" className="footer-link">Monetary Donation</Link>
            <Link to="/services" className="footer-link">Education Support</Link>
            <Link to="/services" className="footer-link">Volunteer Management</Link>
            <Link to="/services" className="footer-link">NGO Collaboration</Link>
          </div>

          <div className="col-lg-3 col-md-6">
            <h6>Contact Us</h6>
            <div className="footer-contact-item">
              <FaMapMarkerAlt />
              <span>4th Floor, Aashray Bhavan, MG Road, Pune, Maharashtra 411001, India</span>
            </div>
            <div className="footer-contact-item">
              <FaPhoneAlt />
              <span>+91 98765 43210</span>
            </div>
            <div className="footer-contact-item">
              <FaEnvelope />
              <span>support@aashray.org</span>
            </div>
            <div className="footer-contact-item">
              <FaClock />
              <span>Mon – Sat, 9:00 AM – 6:30 PM</span>
            </div>
          </div>
        </div>

        <div className="footer-bottom d-flex flex-column flex-md-row align-items-center justify-content-between gap-2 text-center text-md-start">
          <span>&copy; {year} Aashray — AI-Enabled Social Welfare Platform. All rights reserved.</span>
          <div className="d-flex gap-3">
            <Link to="/privacy-policy">Privacy Policy</Link>
            <Link to="/terms">Terms &amp; Conditions</Link>
          </div>
        </div>
      </div>
    </footer>
  );
}
