export default function Loading({ label = 'Loading...', fullPage = false }) {
  const content = (
    <div className="d-flex flex-column align-items-center justify-content-center py-5 fade-in">
      <div className="spinner-border text-primary" role="status" aria-hidden="true" />
      <span className="mt-2 text-muted small">{label}</span>
    </div>
  );

  if (fullPage) {
    return <div className="full-page-center">{content}</div>;
  }
  return content;
}
