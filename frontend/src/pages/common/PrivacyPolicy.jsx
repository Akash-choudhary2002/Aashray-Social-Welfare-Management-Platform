export default function PrivacyPolicy() {
  return (
    <div>
      <section className="page-hero">
        <h1 className="display-6">Privacy Policy</h1>
        <p>How Aashray collects, uses, and protects your information.</p>
      </section>
      <section className="public-section">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-lg-9" style={{ lineHeight: 1.8 }}>
              <p className="text-muted">
                Aashray collects only the information necessary to operate the platform — your
                name, email, role, and activity related to donations, volunteering, or education
                support. This information is used solely to connect donors, NGOs, educators,
                volunteers, and beneficiaries, and is never sold to third parties.
              </p>
              <h5 className="fw-bold mt-4">Data We Collect</h5>
              <p className="text-muted">
                Account details provided at registration, and records of donations, tasks, and
                assignments created while using the platform.
              </p>
              <h5 className="fw-bold mt-4">How We Protect It</h5>
              <p className="text-muted">
                All accounts are secured with JWT-based authentication and role-based access
                control, so users only ever see the data relevant to their role.
              </p>
              <h5 className="fw-bold mt-4">Contact</h5>
              <p className="text-muted">
                Questions about this policy can be sent to support@aashray.org or through our
                Contact Us page.
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
