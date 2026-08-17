import { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';

const OSM_TILE_URL = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
const OSM_ATTRIBUTION = '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors';

/**
 * A donation's saved location, read-only, via Leaflet + OpenStreetMap.
 * Mount this only when there's a donation to show (the caller
 * conditionally renders it, e.g. `{mapDonation && <LocationMapModal ... />}`)
 * — unmounting on close gives each open a clean map instance.
 */
export default function LocationMapModal({ title, address, latitude, longitude, onClose }) {
  useEffect(() => {
    const handleEscape = (e) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [onClose]);

  const directionsUrl = `https://www.openstreetmap.org/directions?from=&to=${latitude}%2C${longitude}`;

  return (
    <div className="simple-modal-backdrop" onClick={onClose}>
      <div className="modal-dialog modal-dialog-centered" style={{ maxWidth: '520px', width: '100%' }} onClick={(e) => e.stopPropagation()}>
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title mb-0">{title || 'Pickup Location'}</h5>
            <button type="button" className="btn-close" aria-label="Close" onClick={onClose} />
          </div>
          <div className="modal-body">
            <div className="map-canvas map-canvas-sm mb-3">
              <MapContainer center={[latitude, longitude]} zoom={16} style={{ height: '100%', width: '100%' }}>
                <TileLayer url={OSM_TILE_URL} attribution={OSM_ATTRIBUTION} />
                <Marker position={[latitude, longitude]}>{address && <Popup>{address}</Popup>}</Marker>
              </MapContainer>
            </div>
            <p className="text-muted small mb-0">{address}</p>
          </div>
          <div className="modal-footer">
            <a href={directionsUrl} target="_blank" rel="noopener noreferrer" className="btn btn-aashray">
              Get Directions
            </a>
            <button type="button" className="btn btn-outline-secondary" onClick={onClose}>
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
