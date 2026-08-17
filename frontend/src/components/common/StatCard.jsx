export default function StatCard({ label, value, icon: Icon }) {
  return (
    <div className="card stat-card h-100">
      <div className="card-body d-flex align-items-center justify-content-between p-4">
        <div>
          <div className="stat-value">{value ?? 0}</div>
          <div className="stat-label mt-1">{label}</div>
        </div>
        {Icon && (
          <div className="stat-icon-wrapper">
            <Icon />
          </div>
        )}
      </div>
    </div>
  );
}
