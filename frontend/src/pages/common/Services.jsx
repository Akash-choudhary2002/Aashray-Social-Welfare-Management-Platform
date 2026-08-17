import { Link } from 'react-router-dom';
import {
  FaUtensils, FaDonate, FaChalkboardTeacher, FaHandsHelping, FaUsers, FaRobot
} from 'react-icons/fa';

const SERVICES = [
  {
    icon: <FaUtensils />,
    title: 'Food Donation',
    text: "Donors list surplus meals or groceries with pickup details. Nearby NGOs browse available donations, accept them, and coordinate collection — cutting down food waste while directly feeding families in need.",
    points: ['List surplus food in minutes', 'NGOs accept and schedule pickup', 'Full donation history for donors']
  },
  {
    icon: <FaDonate />,
    title: 'Monetary Donation',
    text: 'Contribute funds securely towards welfare campaigns, education sponsorships, or general NGO operations. Every donation is logged so donors can track exactly where their support goes.',
    points: ['Secure, trackable transactions', 'Support specific causes or NGOs', 'Downloadable donation history']
  },
  {
    icon: <FaChalkboardTeacher />,
    title: 'Education Support',
    text: 'Registered students are matched with volunteer educators who provide mentorship and academic guidance, with NGOs overseeing the assignment and tracking progress over time.',
    points: ['Student-to-educator matching', 'Progress tracked by NGOs', 'Structured mentorship history']
  },
  {
    icon: <FaHandsHelping />,
    title: 'Volunteer Management',
    text: 'NGOs create and assign community tasks — from distribution drives to awareness campaigns — and volunteers pick up, complete, and log their contributions directly on the platform.',
    points: ['Task assignment by NGOs', 'Status tracking to completion', 'Volunteer activity history']
  },
  {
    icon: <FaUsers />,
    title: 'NGO Collaboration',
    text: 'Verified NGOs manage donations, beneficiaries, educators, and volunteers from one dashboard, replacing scattered spreadsheets and phone calls with a single source of truth.',
    points: ['Unified NGO dashboard', 'Beneficiary & volunteer oversight', 'Donation acceptance workflow']
  },
  {
    icon: <FaRobot />,
    title: 'AI Chatbot Support',
    text: 'A floating assistant is available on every page to answer questions about registration, donations, and how the platform works — reducing drop-off for first-time users.',
    points: ['Always-on guidance', 'Answers registration & donation FAQs', 'No waiting for human support']
  }
];

export default function Services() {
  return (
    <div>
      <section className="page-hero">
        <h1 className="display-6">Our Services</h1>
        <p>Every module Aashray offers, built to connect help to those who need it.</p>
      </section>

      <section className="public-section">
        <div className="container">
          <div className="row g-4 stagger">
            {SERVICES.map((s, i) => (
              <div className="col-lg-6" key={s.title}>
                <div className="service-card h-100">
                  <div className="service-icon-wrap">{s.icon}</div>
                  <h4 className="fw-bold mb-2">{s.title}</h4>
                  <p className="text-muted small mb-3">{s.text}</p>
                  <ul className="list-unstyled small text-muted mb-3">
                    {s.points.map((p) => (
                      <li key={p} className="mb-1">&#10003;&nbsp; {p}</li>
                    ))}
                  </ul>
                  <Link to="/register" className="service-link">Learn More &rarr;</Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="public-section bg-alt">
        <div className="container">
          <div className="cta-band">
            <h2>Not sure where to start?</h2>
            <p>Register in under a minute and choose your role — donor, NGO, educator, or volunteer.</p>
            <Link to="/register" className="btn btn-cta">Create Your Free Account</Link>
          </div>
        </div>
      </section>
    </div>
  );
}
