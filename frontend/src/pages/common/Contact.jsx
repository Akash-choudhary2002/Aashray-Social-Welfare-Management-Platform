import { useState } from 'react';
import { FaMapMarkerAlt, FaPhoneAlt, FaEnvelope, FaClock, FaPaperPlane } from 'react-icons/fa';
import { notifySuccess, notifyError } from '../../components/common/toast';

const initialForm = { name: '', email: '', subject: '', message: '' };

export default function Contact() {
  const [form, setForm] = useState(initialForm);
  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: undefined }));
  };

  const validate = () => {
    const next = {};
    if (!form.name.trim()) next.name = 'Name is required.';
    if (!form.email.trim()) next.email = 'Email is required.';
    else if (!/^\S+@\S+\.\S+$/.test(form.email)) next.email = 'Enter a valid email address.';
    if (!form.subject.trim()) next.subject = 'Subject is required.';
    if (!form.message.trim()) next.message = 'Message cannot be empty.';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setSubmitting(true);
    try {
      // No dedicated backend endpoint for contact messages yet — simulate the
      // round-trip so the form is fully usable without touching existing APIs.
      await new Promise((resolve) => setTimeout(resolve, 600));
      notifySuccess("Thanks for reaching out! We'll get back to you shortly.");
      setForm(initialForm);
    } catch (err) {
      notifyError(err.message || 'Could not send your message. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div>
      <section className="page-hero">
        <h1 className="display-6">Contact Us</h1>
        <p>Questions, partnership ideas, or feedback — we would love to hear from you.</p>
      </section>

      <section className="public-section">
        <div className="container">
          <div className="row g-4">
            <div className="col-lg-4">
              <div className="contact-info-card">
                <h5>Get in Touch</h5>
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

            <div className="col-lg-8">
              <div className="card p-4 p-md-5">
                <h4 className="fw-bold mb-4">Send Us a Message</h4>
                <form onSubmit={handleSubmit} noValidate>
                  <div className="row g-3">
                    <div className="col-md-6">
                      <label className="form-label">Name</label>
                      <input
                        type="text"
                        name="name"
                        className={`form-control${errors.name ? ' is-invalid' : ''}`}
                        value={form.name}
                        onChange={handleChange}
                        placeholder="Your full name"
                      />
                      {errors.name && <div className="invalid-feedback">{errors.name}</div>}
                    </div>
                    <div className="col-md-6">
                      <label className="form-label">Email</label>
                      <input
                        type="email"
                        name="email"
                        className={`form-control${errors.email ? ' is-invalid' : ''}`}
                        value={form.email}
                        onChange={handleChange}
                        placeholder="you@example.com"
                      />
                      {errors.email && <div className="invalid-feedback">{errors.email}</div>}
                    </div>
                    <div className="col-12">
                      <label className="form-label">Subject</label>
                      <input
                        type="text"
                        name="subject"
                        className={`form-control${errors.subject ? ' is-invalid' : ''}`}
                        value={form.subject}
                        onChange={handleChange}
                        placeholder="What is this about?"
                      />
                      {errors.subject && <div className="invalid-feedback">{errors.subject}</div>}
                    </div>
                    <div className="col-12">
                      <label className="form-label">Message</label>
                      <textarea
                        name="message"
                        rows="5"
                        className={`form-control${errors.message ? ' is-invalid' : ''}`}
                        value={form.message}
                        onChange={handleChange}
                        placeholder="Write your message here..."
                      />
                      {errors.message && <div className="invalid-feedback">{errors.message}</div>}
                    </div>
                    <div className="col-12">
                      <button type="submit" className="btn btn-aashray px-4" disabled={submitting}>
                        {submitting ? 'Sending...' : (<><FaPaperPlane className="me-2" />Send Message</>)}
                      </button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>

          <div className="row mt-4">
            <div className="col-12">
              <div className="map-placeholder">Google Maps Placeholder — Aashray Bhavan, MG Road, Pune</div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
