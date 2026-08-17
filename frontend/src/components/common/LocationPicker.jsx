import { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet';
import { FaMapMarkerAlt } from 'react-icons/fa';

// Used only until the donor picks a point — centers roughly on India,
// since that's where this platform operates. Pass defaultCenter to
// override for a different deployment region.
const DEFAULT_CENTER = [28.6139, 77.209];
const OSM_TILE_URL = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
const OSM_ATTRIBUTION = '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors';

// useMapEvents only works from a component rendered inside <MapContainer>,
// so map-click handling has to live in its own tiny child component.
function ClickToSelect({ onSelect }) {
  useMapEvents({
    click(e) {
      onSelect(e.latlng.lat, e.latlng.lng);
    }
  });
  return null;
}

/**
 * "Select Location From Map" button that opens an interactive Leaflet +
 * OpenStreetMap picker in a modal. Click anywhere to drop a pin, or drag
 * an existing pin to fine-tune it. Nothing reaches the form until the
 * donor hits Confirm — only latitude/longitude are returned, no address,
 * no API key, no geocoding call of any kind.
 */
export default function LocationPicker({ latitude, longitude, onLocationChange, defaultCenter = DEFAULT_CENTER }) {
  const [open, setOpen] = useState(false);
  const [draft, setDraft] = useState(null);

  const hasExisting = typeof latitude === 'number' && typeof longitude === 'number';
  const initialCenter = hasExisting ? [latitude, longitude] : defaultCenter;

  useEffect(() => {
    if (!open) return;
    const handleEscape = (e) => {
      if (e.key === 'Escape') setOpen(false);
    };
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [open]);

  const openModal = () => {
    setDraft(hasExisting ? { lat: latitude, lng: longitude } : null);
    setOpen(true);
  };

  const confirm = () => {
    if (!draft) return;
    onLocationChange({ latitude: draft.lat, longitude: draft.lng });
    setOpen(false);
  };

  return (
    <div className="mb-3">
      <label className="form-label d-block">Pickup Location</label>
      <button type="button" className="btn btn-outline-secondary d-flex align-items-center gap-2" onClick={openModal}>
        <FaMapMarkerAlt />
        {hasExisting ? `Location selected (${latitude.toFixed(5)}, ${longitude.toFixed(5)}) — Change` : 'Select Location From Map'}
      </button>

      {open && (
        <div className="simple-modal-backdrop" onClick={() => setOpen(false)}>
          <div
            className="modal-dialog modal-dialog-centered"
            style={{ maxWidth: '520px', width: '100%' }}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title mb-0">Select Pickup Location</h5>
                <button type="button" className="btn-close" aria-label="Close" onClick={() => setOpen(false)} />
              </div>
              <div className="modal-body">
                <div className="map-canvas mb-3">
                  <MapContainer center={initialCenter} zoom={hasExisting ? 16 : 5} style={{ height: '100%', width: '100%' }}>
                    <TileLayer url={OSM_TILE_URL} attribution={OSM_ATTRIBUTION} />
                    <ClickToSelect onSelect={(lat, lng) => setDraft({ lat, lng })} />
                    {draft && (
                      <Marker
                        position={[draft.lat, draft.lng]}
                        draggable
                        eventHandlers={{
                          dragend: (e) => {
                            const pos = e.target.getLatLng();
                            setDraft({ lat: pos.lat, lng: pos.lng });
                          }
                        }}
                      />
                    )}
                  </MapContainer>
                </div>
                <p className="text-muted small mb-0">
                  {draft
                    ? `Selected: ${draft.lat.toFixed(6)}, ${draft.lng.toFixed(6)}`
                    : 'Click anywhere on the map to drop a pin, or drag it afterward to fine-tune.'}
                </p>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-aashray" disabled={!draft} onClick={confirm}>
                  Confirm Location
                </button>
                <button type="button" className="btn btn-outline-secondary" onClick={() => setOpen(false)}>
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
