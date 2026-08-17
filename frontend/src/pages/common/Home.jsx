import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import AnimatedCounter from '../../components/common/AnimatedCounter';
import {
  FaUtensils, FaDonate, FaChalkboardTeacher, FaHandsHelping, FaRobot, FaUsers,
  FaUserCheck, FaSignInAlt, FaHandHoldingHeart, FaCheckCircle, FaShieldAlt,
  FaBolt, FaChartLine, FaQuoteLeft, FaSeedling
} from 'react-icons/fa';

const ROLE_HOME = {
  ADMIN: '/admin/dashboard',
  DONOR: '/donor/food-donations',
  NGO: '/ngo/available-donations',
  EDUCATOR: '/educator/my-students',
  VOLUNTEER: '/volunteer/my-tasks',
  BENEFICIARY: '/profile'
};

const SERVICES = [
  { icon: <FaUtensils />, title: 'Food Donation', text: 'Donors list surplus meals; nearby NGOs accept, pick up, and distribute them to families in need before they go to waste.' },
  { icon: <FaDonate />, title: 'Monetary Donation', text: 'Contribute securely towards welfare campaigns, education sponsorships, and NGO operations with full transaction history.' },
  { icon: <FaChalkboardTeacher />, title: 'Education Support', text: 'Registered students are matched with qualified educators who track mentorship and academic progress over time.' },
  { icon: <FaHandsHelping />, title: 'Volunteer Management', text: 'NGOs assign and track community tasks, letting volunteers pick up, complete, and log real-world social impact work.' },
  { icon: <FaUsers />, title: 'NGO Collaboration', text: 'Verified NGOs coordinate donation pickups, beneficiary assignments, and volunteer tasks from one shared dashboard.' },
  { icon: <FaRobot />, title: 'AI Chatbot Support', text: 'An always-on assistant answers questions about registration, donations, and the platform — no waiting for a human reply.' }
];

const STEPS = [
  { icon: <FaUserCheck />, title: 'Register', text: 'Create a free account as a donor, NGO, educator, or volunteer in under a minute.' },
  { icon: <FaSignInAlt />, title: 'Login', text: 'Securely sign in to your personalized, role-based dashboard.' },
  { icon: <FaHandHoldingHeart />, title: 'Donate / Volunteer / Learn', text: 'Share food, funds, time, or knowledge based on your chosen role.' },
  { icon: <FaCheckCircle />, title: 'NGOs Accept', text: 'Partner NGOs review, accept, and coordinate the contribution or task.' },
  { icon: <FaSeedling />, title: 'Beneficiaries Receive Support', text: 'Families and students receive food, funds, or education support on the ground.' }
];

const WHY_CHOOSE = [
  { icon: <FaShieldAlt />, title: 'Verified & Secure', text: 'JWT-secured accounts and role-based access keep every donation and record protected.' },
  { icon: <FaBolt />, title: 'Fast & Transparent', text: 'Real-time status tracking from donation to delivery, with no hidden steps.' },
  { icon: <FaChartLine />, title: 'Measurable Impact', text: 'Live dashboards show exactly how contributions translate into community outcomes.' },
  { icon: <FaRobot />, title: 'AI-Assisted', text: 'A built-in chatbot guides new users through registration, donations, and support.' }
];

const TESTIMONIALS = [
  { name: 'Mahi More', role: 'Regular Donor', text: 'Listing surplus food takes two minutes, and I can see exactly which NGO picked it up. It finally feels effortless to help.' },
  { name: 'Sanket Pawar', role: 'Volunteer', text: 'The task board keeps me organized — I know exactly what my NGO needs help with each week and can log it instantly.' },
  { name: 'Pooja Chavan', role: 'NGO Coordinator', text: 'Managing donations, students, and volunteers used to mean three different spreadsheets. Aashray brought it all into one place.' },
  { name: 'Shubham Chandghode', role: 'Volunteer Educator', text: 'I can track every student I mentor and their progress without any manual paperwork.' }
];

const PARTNERS = ['Seva Foundation', 'Bright Futures NGO', 'AnnaSetu Trust', 'Vidya Sankalp', 'Manav Sewa Sangh', 'Hope & Home'];

const STATS = [
  { label: 'Total Donations', value: 12480, prefix: '', suffix: '+' },
  { label: 'Partner NGOs', value: 86, suffix: '+' },
  { label: 'Active Volunteers', value: 2350, suffix: '+' },
  { label: 'Students Supported', value: 940, suffix: '+' },
  { label: 'Meals Distributed', value: 58200, suffix: '+' }
];

export default function Home() {
  const { isAuthenticated, user } = useAuth();

  return (
    <div>
      {/* Hero */}
      <section className="hero-public">
        <div className="hero-inner">
          <span className="hero-eyebrow"><FaHandHoldingHeart />  Social Welfare Platform</span>
          <h1>Empowering Communities, Transforming Lives</h1>
          <p className="lead">
            Aashray is a unified welfare ecosystem bridging the gap between those who want to help
            and those in need — connecting donors, NGOs, educators, and volunteers on one platform.
          </p>
          <div className="hero-actions">
            {isAuthenticated ? (
              <Link to={ROLE_HOME[user?.role] || '/profile'} className="btn-hero-primary btn">
                Go to My Dashboard
              </Link>
            ) : (
              <>
                <Link to="/register" className="btn-hero-primary btn">Donate Now</Link>
                <Link to="/register" className="btn-hero-secondary btn">Join as Volunteer</Link>
              </>
            )}
          </div>
        </div>
      </section>

      {/* Impact stats band */}
      <div className="stats-band">
        <div className="container">
          <div className="row g-4 text-center">
            {STATS.map((s) => (
              <div className="col-6 col-md" key={s.label}>
                <div className="counter-wrap">
                  <div className="stat-num">
                    <AnimatedCounter end={s.value} suffix={s.suffix} />
                  </div>
                  <div className="stat-lbl">{s.label}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* About Aashray */}
      <section className="public-section">
        <div className="container">
          <div className="row align-items-center g-5">
            <div className="col-lg-6">
              <span className="section-eyebrow">About Aashray</span>
              <h2 className="section-title">One platform for every kind of giving</h2>
              <p className="text-muted mb-3" style={{ lineHeight: 1.8 }}>
                Aashray brings donors, NGOs, educators, volunteers, and beneficiaries together in a
                single, transparent ecosystem — replacing scattered phone calls and spreadsheets
                with real-time, role-based dashboards.
              </p>
              <div className="row g-4 mt-2">
                <div className="col-sm-6">
                  <h6 className="fw-bold text-primary mb-1">Our Mission</h6>
                  <p className="text-muted small mb-0">
                    To make giving and receiving help as simple, fast, and transparent as possible for every community.
                  </p>
                </div>
                <div className="col-sm-6">
                  <h6 className="fw-bold text-primary mb-1">Our Vision</h6>
                  <p className="text-muted small mb-0">
                    A future where no surplus meal, willing volunteer, or student in need goes unmatched.
                  </p>
                </div>
              </div>
            </div>
            <div className="col-lg-6">
              <div className="row g-3 stagger">
                <div className="col-6">
                  <div className="feature-card text-center">
                    <div className="feature-icon"><FaUtensils /></div>
                    <div className="fw-bold">Food Sharing</div>
                  </div>
                </div>
                <div className="col-6">
                  <div className="feature-card text-center">
                    <div className="feature-icon"><FaDonate /></div>
                    <div className="fw-bold">Fund Support</div>
                  </div>
                </div>
                <div className="col-6">
                  <div className="feature-card text-center">
                    <div className="feature-icon"><FaChalkboardTeacher /></div>
                    <div className="fw-bold">Education</div>
                  </div>
                </div>
                <div className="col-6">
                  <div className="feature-card text-center">
                    <div className="feature-icon"><FaHandsHelping /></div>
                    <div className="fw-bold">Volunteering</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Our Services */}
      <section className="public-section bg-alt">
        <div className="container text-center">
          <span className="section-eyebrow">What We Offer</span>
          <h2 className="section-title">Our Services</h2>
          <p className="section-subtitle mx-auto">
            Every module on Aashray is built around one goal — connecting help to those who need it, faster.
          </p>
          <div className="row g-4 mt-3 stagger">
            {SERVICES.map((s) => (
              <div className="col-md-6 col-lg-4" key={s.title}>
                <div className="service-card text-start">
                  <div className="service-icon-wrap">{s.icon}</div>
                  <h5 className="fw-bold mb-2">{s.title}</h5>
                  <p className="text-muted small mb-3">{s.text}</p>
                  <Link to="/services" className="service-link">Learn More &rarr;</Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="public-section">
        <div className="container text-center">
          <span className="section-eyebrow">The Process</span>
          <h2 className="section-title">How It Works</h2>
          <p className="section-subtitle mx-auto">From sign-up to real-world impact in five simple steps.</p>
          <div className="row g-4 mt-3">
            {STEPS.map((step, i) => (
              <div className="col-6 col-md" key={step.title}>
                <div className="step-item">
                  {i < STEPS.length - 1 && <div className="step-connector d-none d-md-block" />}
                  <div className="step-circle">
                    {step.icon}
                    <span className="step-number">{i + 1}</span>
                  </div>
                  <h5>{step.title}</h5>
                  <p className="text-muted small mb-0">{step.text}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Why Choose Aashray */}
      <section className="public-section bg-alt">
        <div className="container">
          <div className="row align-items-center g-5">
            <div className="col-lg-5">
              <span className="section-eyebrow">Why Choose Us</span>
              <h2 className="section-title">Built for trust, speed, and real impact</h2>
              <p className="text-muted" style={{ lineHeight: 1.8 }}>
                Aashray isn't just a form — it's a complete, secure, and measurable welfare
                management system designed for donors, NGOs, and communities alike.
              </p>
            </div>
            <div className="col-lg-7">
              <div className="row">
                {WHY_CHOOSE.map((w) => (
                  <div className="col-sm-6" key={w.title}>
                    <div className="why-item">
                      <div className="why-icon">{w.icon}</div>
                      <div>
                        <h6>{w.title}</h6>
                        <p>{w.text}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Testimonials */}
      <section className="public-section">
        <div className="container text-center">
          <span className="section-eyebrow">Testimonials</span>
          <h2 className="section-title">Trusted by our community</h2>
          <div className="row g-4 mt-3 stagger">
            {TESTIMONIALS.map((t) => (
              <div className="col-md-6 col-lg-3" key={t.name}>
                <div className="testimonial-card text-start">
                  <FaQuoteLeft className="quote-icon" />
                  <p className="quote-text">{t.text}</p>
                  <div className="testimonial-author">
                    <div className="testimonial-avatar">{t.name.split(' ').map((n) => n[0]).join('')}</div>
                    <div>
                      <strong>{t.name}</strong>
                      <span>{t.role}</span>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Partner NGOs */}
      <section className="public-section bg-alt">
        <div className="container text-center">
          <span className="section-eyebrow">Our Network</span>
          <h2 className="section-title mb-4">Partner NGOs &amp; Sponsors</h2>
          <div className="partner-strip">
            {PARTNERS.map((p) => (
              <div className="partner-pill" key={p}>{p}</div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="public-section">
        <div className="container">
          <div className="cta-band">
            <h2>Ready to make a difference?</h2>
            <p>Join thousands of donors, NGOs, educators, and volunteers already changing lives through Aashray.</p>
            {isAuthenticated ? (
              <Link to={ROLE_HOME[user?.role] || '/profile'} className="btn btn-cta">Go to My Dashboard</Link>
            ) : (
              <Link to="/register" className="btn btn-cta">Get Started Today</Link>
            )}
          </div>
        </div>
      </section>
    </div>
  );
}
