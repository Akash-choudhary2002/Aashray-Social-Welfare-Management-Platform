/**
 * Client-side pagination control. Purely presentational — pass in the
 * current page, total pages, and a callback; the parent page keeps
 * owning the actual data slicing, so no page's fetching/business logic
 * needs to change to adopt this.
 */
export default function Pagination({ page, totalPages, onPageChange, totalItems, pageSize }) {
  if (totalPages <= 1) return null;

  const pages = [];
  const windowSize = 1;
  for (let p = 1; p <= totalPages; p++) {
    if (p === 1 || p === totalPages || Math.abs(p - page) <= windowSize) {
      pages.push(p);
    } else if (pages[pages.length - 1] !== '...') {
      pages.push('...');
    }
  }

  const start = totalItems === 0 ? 0 : (page - 1) * pageSize + 1;
  const end = Math.min(page * pageSize, totalItems);

  return (
    <div className="aashray-pagination">
      <div className="page-info">
        {totalItems != null ? `Showing ${start}–${end} of ${totalItems}` : `Page ${page} of ${totalPages}`}
      </div>
      <div className="page-buttons">
        <button onClick={() => onPageChange(page - 1)} disabled={page === 1} aria-label="Previous page">
          &lsaquo;
        </button>
        {pages.map((p, idx) =>
          p === '...' ? (
            <span key={`ellipsis-${idx}`} className="px-2 text-muted small d-flex align-items-center">
              …
            </span>
          ) : (
            <button
              key={p}
              className={p === page ? 'active' : ''}
              onClick={() => onPageChange(p)}
              aria-current={p === page ? 'page' : undefined}
            >
              {p}
            </button>
          )
        )}
        <button onClick={() => onPageChange(page + 1)} disabled={page === totalPages} aria-label="Next page">
          &rsaquo;
        </button>
      </div>
    </div>
  );
}
