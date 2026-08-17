import { FaInbox } from 'react-icons/fa';

export default function EmptyState({
  icon: Icon = FaInbox,
  title = 'Nothing here yet',
  subtitle = 'There is no data to display right now.',
  action = null
}) {
  return (
    <div className="empty-state fade-in">
      <Icon className="empty-icon" />
      <div className="empty-title">{title}</div>
      <div className="empty-subtitle">{subtitle}</div>
      {action && <div className="mt-3">{action}</div>}
    </div>
  );
}
