const COLOR_MAP = {
  // Food donation
  PENDING: { bg: '#fef3c7', text: '#d97706' },
  ACCEPTED: { bg: '#e0f2fe', text: '#0284c7' },
  REJECTED: { bg: '#fee2e2', text: '#dc2626' },
  PICKED_UP: { bg: '#e0e7ff', text: '#4f46e5' },
  DELIVERED: { bg: '#d1fae5', text: '#059669' },
  EXPIRED: { bg: '#f1f5f9', text: '#475569' },
  // Monetary donation
  SUCCESS: { bg: '#d1fae5', text: '#059669' },
  FAILED: { bg: '#fee2e2', text: '#dc2626' },
  REFUNDED: { bg: '#ede9fe', text: '#7c3aed' },
  // Education assignment
  ACTIVE: { bg: '#e0f2fe', text: '#0284c7' },
  COMPLETED: { bg: '#d1fae5', text: '#059669' },
  CANCELLED: { bg: '#f1f5f9', text: '#475569' },
  // Volunteer task
  ASSIGNED: { bg: '#e0f2fe', text: '#0284c7' },
  IN_PROGRESS: { bg: '#e0e7ff', text: '#4f46e5' }
};

export default function StatusBadge({ status }) {
  if (!status) return null;
  const config = COLOR_MAP[status] || { bg: '#f1f5f9', text: '#475569' };
  
  return (
    <span 
      className="badge-status" 
      style={{ 
        backgroundColor: config.bg, 
        color: config.text,
        border: `1px solid rgba(0,0,0,0.03)`
      }}
    >
      {status.replaceAll('_', ' ')}
    </span>
  );
}
