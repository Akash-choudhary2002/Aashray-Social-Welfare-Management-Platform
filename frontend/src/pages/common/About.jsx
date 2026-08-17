import { Link } from 'react-router-dom';
import {
  FaBullseye, FaEye, FaListUl, FaHeart, FaShieldAlt, FaLaptopCode,
  FaRocket, FaHandsHelping
} from 'react-icons/fa';

const OBJECTIVES = [
  'Provide a single platform where donors, NGOs, educators, and volunteers can coordinate welfare activities.',
  'Ensure surplus food reaches beneficiaries quickly through a transparent donation-to-delivery pipeline.',
  'Match students with dedicated educators and track mentorship progress over time.',
  'Give NGOs real-time visibility into donations, volunteer tasks, and beneficiary needs.',
  'Offer instant, AI-assisted guidance so new users never feel lost during registration or donation.'
];

const VALUES = [
  { icon: <FaHeart />, title: 'Compassion First', text: 'Every feature is designed around the people it ultimately serves — donors and beneficiaries alike.' },
  { icon: <FaShieldAlt />, title: 'Trust & Transparency', text: 'Secure, role-based access and clear activity trails keep every contribution accountable.' },
  { icon: <FaHandsHelping />, title: 'Collaboration', text: 'NGOs, educators, and volunteers work from shared dashboards instead of disconnected tools.' },
  { icon: <FaRocket />, title: 'Continuous Improvement', text: "We keep refining the platform based on real feedback from the community that uses it." }
];

export default function About() {
  return (
    <div>
      <section className="page-hero">
        <h1 className="display-6">About Aashray</h1>
        <p>A unified social welfare ecosystem built to connect help with need.</p>
      </section>

      <section className="public-section">
        <div className="container">
          <div className="row g-5 align-items-center">
            <div className="col-lg-6">
              <span className="section-eyebrow">Project Overview</span>
              <h2 className="section-title">What is Aashray?</h2>
              <p className="text-muted" style={{ lineHeight: 1.8 }}>
                Aashray is an AI-enabled social welfare platform that brings together donors, NGOs,
                educators, volunteers, and beneficiaries under one roof. It replaces manual
                coordination — phone calls, spreadsheets, and paper forms — with a secure,
                role-based web application built on a microservices architecture, so every part of
                the giving process is fast, visible, and accountable.
              </p>
              <Link to="/services" className="btn btn-aashray mt-2">Explore Our Services</Link>
            </div>
            <div className="col-lg-6">
              <div className="row g-4">
                <div className="col-sm-6">
                  <div className="value-card">
                    <div className="value-icon"><FaBullseye /></div>
                    <h6 className="fw-bold">Our Mission</h6>
                    <p className="text-muted small mb-0">
                      Make giving and receiving help as simple, fast, and transparent as possible
                      for every community, regardless of scale.
                    </p>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="value-card">
                    <div className="value-icon"><FaEye /></div>
                    <h6 className="fw-bold">Our Vision</h6>
                    <p className="text-muted small mb-0">
                      A future where no surplus meal, willing volunteer, or student in need ever
                      goes unmatched.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="public-section bg-alt">
        <div className="container">
          <div className="row g-5">
            <div className="col-lg-5">
              <span className="section-eyebrow"><FaListUl className="me-1" /> Objectives</span>
              <h2 className="section-title">What we set out to do</h2>
            </div>
            <div className="col-lg-7">
              <ul className="list-group list-group-flush">
                {OBJECTIVES.map((obj) => (
                  <li className="list-group-item bg-transparent px-0" key={obj}>{obj}</li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      </section>

      <section className="public-section">
        <div className="container text-center">
          <span className="section-eyebrow">Team Values</span>
          <h2 className="section-title">Why Aashray?</h2>
          <div className="row g-4 mt-3 stagger">
            {VALUES.map((v) => (
              <div className="col-sm-6 col-lg-3" key={v.title}>
                <div className="value-card text-start h-100">
                  <div className="value-icon">{v.icon}</div>
                  <h6 className="fw-bold">{v.title}</h6>
                  <p className="text-muted small mb-0">{v.text}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="public-section bg-alt">
        <div className="container">
          <div className="row g-5 align-items-center">
            <div className="col-lg-6">
              <span className="section-eyebrow"><FaLaptopCode className="me-1" /> Technology Used</span>
              <h2 className="section-title">Built on a modern, scalable stack</h2>
              <p className="text-muted" style={{ lineHeight: 1.8 }}>
                The frontend is a React single-page app styled with Bootstrap 5, talking to a
                Spring Boot microservices backend (auth, food, monetary, education, volunteer,
                chatbot, and notification services) behind an API gateway with Eureka service
                discovery, secured end-to-end with JWT authentication.
              </p>
            </div>
            <div className="col-lg-6">
              <span className="section-eyebrow">Future Scope</span>
              <h2 className="section-title">Where Aashray is headed</h2>
              <p className="text-muted" style={{ lineHeight: 1.8 }}>
                Planned enhancements include payment gateway integration for monetary donations,
                geo-mapped donation pickups, richer impact analytics for NGOs, and mobile apps for
                donors and volunteers on the go.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="public-section">
        <div className="container">
          <div className="cta-band">
            <h2>Be part of the Aashray community</h2>
            <p>Whether you can give a meal, a rupee, an hour, or a lesson — there's a place for you here.</p>
            <Link to="/register" className="btn btn-cta">Get Started Today</Link>
          </div>
        </div>
      </section>
    </div>
  );
}
