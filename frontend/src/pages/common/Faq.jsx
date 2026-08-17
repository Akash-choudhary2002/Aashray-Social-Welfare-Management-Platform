import { Link } from 'react-router-dom';

const FAQS = [
  {
    q: 'How do I register on Aashray?',
    a: "Click Register in the navigation bar, choose your role (Donor, NGO, Educator, Volunteer, or Beneficiary), and fill in your basic details. You'll be able to log in immediately after your account is created."
  },
  {
    q: 'How do I donate food or money?',
    a: 'Once logged in as a Donor, use the Food Donations or Monetary Donations section of your dashboard to list a food donation with pickup details or make a monetary contribution. You can track the status of every donation from the same dashboard.'
  },
  {
    q: 'How do NGOs work on the platform?',
    a: 'Verified NGOs log in to review available food and monetary donations, accept the ones they can fulfil, and manage beneficiaries, educator assignments, and volunteer tasks — all from one NGO dashboard.'
  },
  {
    q: 'What is the education support process?',
    a: "NGOs register students who need academic support and assign them to available volunteer educators. Educators can then view their assigned students and track mentorship progress over time."
  },
  {
    q: 'How do I register as a volunteer and get tasks?',
    a: "Register with the Volunteer role, then log in to see tasks assigned to you by NGOs. You can view task details, mark progress, and see a history of everything you've completed."
  },
  {
    q: 'How do I contact support if I face an issue?',
    a: "Use the floating chatbot on any page for instant answers, or visit the Contact Us page to send a message with your name, email, and query — our team typically responds within one business day."
  }
];

export default function Faq() {
  return (
    <div>
      <section className="page-hero">
        <h1 className="display-6">Frequently Asked Questions</h1>
        <p>Everything you need to know about registering, donating, and getting support.</p>
      </section>

      <section className="public-section">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-lg-9">
              <div className="accordion faq-accordion" id="faqAccordion">
                {FAQS.map((item, i) => (
                  <div className="accordion-item" key={item.q}>
                    <h2 className="accordion-header">
                      <button
                        className={`accordion-button${i === 0 ? '' : ' collapsed'}`}
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target={`#faq-${i}`}
                        aria-expanded={i === 0}
                        aria-controls={`faq-${i}`}
                      >
                        {item.q}
                      </button>
                    </h2>
                    <div
                      id={`faq-${i}`}
                      className={`accordion-collapse collapse${i === 0 ? ' show' : ''}`}
                      data-bs-parent="#faqAccordion"
                    >
                      <div className="accordion-body">{item.a}</div>
                    </div>
                  </div>
                ))}
              </div>

              <div className="text-center mt-5">
                <p className="text-muted mb-3">Still have questions?</p>
                <Link to="/contact" className="btn btn-aashray px-4">Contact Support</Link>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
